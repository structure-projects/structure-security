package cn.structured.sample;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 权限模块集成测试 - Remote 模式
 * 
 * 测试远程权限获取模式下的权限拦截行为
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("remote")
public class PermissionRemoteModeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RestTemplate restTemplate;

    /**
     * 辅助方法：登录并获取 JWT token
     */
    private String loginAndGetToken(String username) throws Exception {
        String loginRequestBody = String.format("{\"username\":\"%s\",\"password\":\"123456\"}", username);
        
        MvcResult loginResult = mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestBody))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("data").asText();
    }

    /**
     * 测试远程模式权限检查 - Admin 用户
     */
    @Test
    public void testRemotePermissionCheck_admin() throws Exception {
        // 1. Mock 远程权限服务响应 - Admin 拥有所有权限
        List<String> permissions = Arrays.asList("*:*", "*:*:*");
        ResponseEntity<List> response = new ResponseEntity<>(permissions, org.springframework.http.HttpStatus.OK);
        when(restTemplate.exchange(
            anyString(),
            any(),
            any(),
            eq(List.class)
        )).thenReturn(response);

        // 2. Admin 用户登录获取 token
        String adminToken = loginAndGetToken("admin");

        // 3. Admin 可以访问所有接口
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isOk());

        mockMvc.perform(put("/api/system/config")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\":\"test\",\"value\":\"value\"}"))
            .andExpect(status().isOk());
    }

    /**
     * 测试远程模式权限检查 - 普通用户
     */
    @Test
    public void testRemotePermissionCheck_user() throws Exception {
        // 1. Mock 远程权限服务响应 - User 只有部分权限
        List<String> permissions = Arrays.asList("order:create", "order:read", "user:read");
        ResponseEntity<List> response = new ResponseEntity<>(permissions, org.springframework.http.HttpStatus.OK);
        when(restTemplate.exchange(
            anyString(),
            any(),
            any(),
            eq(List.class)
        )).thenReturn(response);

        // 2. User 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 3. User 可以创建订单
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isOk());

        // 4. User 可以读取订单
        mockMvc.perform(get("/api/order/1")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk());

        // 5. User 不能删除订单
        mockMvc.perform(delete("/api/order/1")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }

    /**
     * 测试远程模式返回空权限列表
     */
    @Test
    public void testRemotePermissionEmpty() throws Exception {
        // 1. Mock 远程权限服务返回空列表
        ResponseEntity<List> response = new ResponseEntity<>(Arrays.asList(), org.springframework.http.HttpStatus.OK);
        when(restTemplate.exchange(
            anyString(),
            any(),
            any(),
            eq(List.class)
        )).thenReturn(response);

        // 2. 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 3. 用户没有任何权限，无法访问接口
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isForbidden());
    }

    /**
     * 测试远程模式服务不可用
     */
    @Test
    public void testRemotePermissionServiceUnavailable() throws Exception {
        // 1. Mock 远程权限服务抛出异常
        when(restTemplate.exchange(
            anyString(),
            any(),
            any(),
            eq(List.class)
        )).thenThrow(new RuntimeException("Service unavailable"));

        // 2. 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 3. 服务不可用时，用户没有权限
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isForbidden());
    }

    /**
     * 测试远程模式返回通配符权限
     */
    @Test
    public void testRemotePermissionWithWildcards() throws Exception {
        // 1. Mock 远程权限服务返回通配符权限
        List<String> permissions = Arrays.asList("order:*", "*:read", "system:*:*");
        ResponseEntity<List> response = new ResponseEntity<>(permissions, org.springframework.http.HttpStatus.OK);
        when(restTemplate.exchange(
            anyString(),
            any(),
            any(),
            eq(List.class)
        )).thenReturn(response);

        // 2. 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 3. 用户有 order:* 权限
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isOk());

        // 4. 用户有 *:read 权限
        mockMvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk());

        // 5. 用户有 system:*:* 权限
        mockMvc.perform(get("/api/system/logs")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk());
    }
}
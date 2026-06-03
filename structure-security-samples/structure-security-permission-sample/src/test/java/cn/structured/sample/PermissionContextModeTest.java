package cn.structured.sample;

import cn.structured.security.permission.IPermissionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 权限模块集成测试 - Context 模式
 * 
 * 测试不同用户角色的权限拦截行为：
 * - admin: 超级管理员，拥有所有权限
 * - user: 普通用户，拥有基础权限
 * - guest: 访客，拥有只读权限
 * 
 * 测试流程：
 * 1. 通过登录端点获取 JWT token
 * 2. 使用 token 访问受保护的接口
 * 3. 验证权限拦截行为
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PermissionContextModeTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

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

    // ==================== Admin 用户测试 ====================

    @Test
    public void testAdminUser_allPermissionsAllowed() throws Exception {
        // 1. Admin 用户登录获取 token
        String adminToken = loginAndGetToken("admin");

        // 2. Admin 可以创建订单
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isOk());

        // 3. Admin 可以读取订单
        mockMvc.perform(get("/api/order/1")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk());

        // 4. Admin 可以删除订单
        mockMvc.perform(delete("/api/order/1")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk());

        // 5. Admin 可以修改系统配置
        mockMvc.perform(put("/api/system/config")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\":\"test\",\"value\":\"value\"}"))
            .andExpect(status().isOk());

        // 6. Admin 可以查看系统日志
        mockMvc.perform(get("/api/system/logs")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk());
    }

    // ==================== User 用户测试 ====================

    @Test
    public void testUserUser_hasOrderPermissions() throws Exception {
        // 1. User 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 2. User 可以创建订单
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isOk());

        // 3. User 可以读取订单
        mockMvc.perform(get("/api/order/1")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk());
    }

    @Test
    public void testUserUser_noDeletePermission() throws Exception {
        // 1. User 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 2. User 不能删除订单（无权限）
        mockMvc.perform(delete("/api/order/1")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testUserUser_hasUserReadPermission() throws Exception {
        // 1. User 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 2. User 可以读取用户信息
        mockMvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk());
    }

    @Test
    public void testUserUser_noSystemPermission() throws Exception {
        // 1. User 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 2. User 不能修改系统配置（无权限）
        mockMvc.perform(put("/api/system/config")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\":\"test\",\"value\":\"value\"}"))
            .andExpect(status().isForbidden());
    }

    // ==================== Guest 用户测试 ====================

    @Test
    public void testGuestUser_hasReadPermission() throws Exception {
        // 1. Guest 用户登录获取 token
        String guestToken = loginAndGetToken("guest");

        // 2. Guest 可以读取订单
        mockMvc.perform(get("/api/order/1")
                .header("Authorization", "Bearer " + guestToken))
            .andExpect(status().isOk());

        // 3. Guest 可以读取用户信息
        mockMvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + guestToken))
            .andExpect(status().isOk());
    }

    @Test
    public void testGuestUser_noWritePermission() throws Exception {
        // 1. Guest 用户登录获取 token
        String guestToken = loginAndGetToken("guest");

        // 2. Guest 不能创建订单（无权限）
        mockMvc.perform(post("/api/order/create")
                .header("Authorization", "Bearer " + guestToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"测试订单\"}"))
            .andExpect(status().isForbidden());

        // 3. Guest 不能删除订单（无权限）
        mockMvc.perform(delete("/api/order/1")
                .header("Authorization", "Bearer " + guestToken))
            .andExpect(status().isForbidden());
    }

    // ==================== 未认证用户测试 ====================

    @Test
    public void testUnauthenticatedUser_forbidden() throws Exception {
        // 未认证用户不能访问受保护的接口
        mockMvc.perform(get("/api/order/1"))
            .andExpect(status().isUnauthorized());
    }

    // ==================== 编程方式权限检查测试 ====================

    @Test
    public void testProgrammaticPermissionCheck() throws Exception {
        // 1. User 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 2. 编程方式检查权限：order:create - 有权限
        mockMvc.perform(get("/api/permission/check")
                .header("Authorization", "Bearer " + userToken)
                .param("permission", "order:create"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.permission").value("order:create"))
            .andExpect(jsonPath("$.hasPermission").value(true));

        // 3. 编程方式检查权限：user:delete - 无权限
        mockMvc.perform(get("/api/permission/check")
                .header("Authorization", "Bearer " + userToken)
                .param("permission", "user:delete"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.permission").value("user:delete"))
            .andExpect(jsonPath("$.hasPermission").value(false));
    }

    // ==================== 获取用户权限列表测试 ====================

    @Test
    public void testGetUserPermissions() throws Exception {
        // 1. User 用户登录获取 token
        String userToken = loginAndGetToken("user");

        // 2. 获取用户权限列表
        mockMvc.perform(get("/api/permission/list")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.permissions").isArray());
    }

    // ==================== 健康检查接口测试 ====================

    @Test
    public void testHealthEndpoint() throws Exception {
        // 健康检查接口无需认证
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    // ==================== 登录失败测试 ====================

    @Test
    public void testLoginWithWrongPassword() throws Exception {
        // 错误密码登录应该失败
        String loginRequestBody = "{\"username\":\"admin\",\"password\":\"wrongpassword\"}";
        
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(500));
    }
}
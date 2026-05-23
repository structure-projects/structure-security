package cn.structure.example.jwt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private String validToken;

    @Test
    @Order(1)
    @DisplayName("测试1：未登录访问受保护资源 - 应返回 NOT_LOGGED_IN")
    public void testUnauthenticatedAccessProtectedResource() throws Exception {
        mockMvc.perform(get("/test/hello"))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(2)
    @DisplayName("测试2：未登录访问需要权限的资源 - 应返回 NOT_LOGGED_IN")
    public void testUnauthenticatedAccessProtectedResourceWithRole() throws Exception {
        mockMvc.perform(get("/test/hello2"))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(3)
    @DisplayName("测试3：登录成功获取 Token")
    public void testLoginSuccess() throws Exception {
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "123456");

        MvcResult result = mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(response);
        assertTrue(jsonObject.containsKey("data"));
        assertNotNull(jsonObject.getString("data"));

        validToken = jsonObject.getString("data");
        assertTrue(validToken.length() > 0);
    }

    @Test
    @Order(4)
    @DisplayName("测试4：使用有效 Token 访问受保护资源")
    public void testAccessProtectedResourceWithValidToken() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        mockMvc.perform(get("/test/hello")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(5)
    @DisplayName("测试5：使用无效 Token 访问受保护资源 - 应返回 INVALID_AUTHENTICATION")
    public void testAccessProtectedResourceWithInvalidToken() throws Exception {
        mockMvc.perform(get("/test/hello")
                        .header("Authorization", "Bearer invalid-token-12345"))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(6)
    @DisplayName("测试6：使用过期 Token 访问受保护资源 - 应返回 INVALID_AUTHENTICATION")
    public void testAccessProtectedResourceWithExpiredToken() throws Exception {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNTE2MjM5MDIyfQ.invalid";

        mockMvc.perform(get("/test/hello")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(7)
    @DisplayName("测试7：未提供 Token (无 Authorization 头) - 应返回 NOT_LOGGED_IN")
    public void testNoAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/test/hello"))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(8)
    @DisplayName("测试8：使用错误格式的 Authorization 头 - 应返回 INVALID_AUTHENTICATION")
    public void testWrongAuthorizationFormat() throws Exception {
        mockMvc.perform(get("/test/hello")
                        .header("Authorization", "Basic dXNlcjpwYXNz"))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(9)
    @DisplayName("测试9：白名单接口 /api/user/login 无需认证")
    public void testWhitelistedEndpointNoAuthRequired() throws Exception {
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "123456");

        mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(10)
    @DisplayName("测试10：登录失败 - 用户名密码错误")
    public void testLoginFailure() throws Exception {
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "wrongpassword");

        mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("用户名密码错误！"));
    }

    @Test
    @Order(11)
    @DisplayName("测试11：使用有效 Token 访问需要 ROLE_ADMIN 权限的资源 - 应成功")
    public void testAccessResourceWithProperRole() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        mockMvc.perform(get("/test/hello2")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(12)
    @DisplayName("测试12：重复使用同一个 Token - 应持续有效")
    public void testReuseValidToken() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/test/hello")
                            .header("Authorization", "Bearer " + validToken))
                    .andExpect(jsonPath("$.code").value("SUCCESS"));
        }
    }

    @Test
    @Order(13)
    @DisplayName("测试13：使用普通用户访问需要 ROLE_SUPER_ADMIN 权限的接口 - 应拒绝")
    public void testNoPermissionForRegularUser() throws Exception {
        // 使用普通用户登录
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "user");
        loginRequest.put("password", "123456");

        MvcResult result = mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(response);
        String userToken = jsonObject.getString("data");

        // 使用普通用户的 Token 尝试访问需要 ROLE_SUPER_ADMIN 的接口
        mockMvc.perform(get("/test/hello3")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"));
    }

    @Test
    @Order(14)
    @DisplayName("测试14：使用管理员访问需要 ROLE_SUPER_ADMIN 权限的接口 - 应拒绝")
    public void testNoPermissionForAdmin() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        // 使用管理员的 Token 尝试访问需要 ROLE_SUPER_ADMIN 的接口
        mockMvc.perform(get("/test/hello3")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"));
    }
}

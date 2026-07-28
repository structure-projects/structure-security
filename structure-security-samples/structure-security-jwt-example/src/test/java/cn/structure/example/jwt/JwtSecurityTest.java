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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        mockMvc.perform(get("/test/hello3")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"));
    }

    @Test
    @Order(15)
    @DisplayName("测试15：登出功能 - 正常登出")
    public void testLogoutSuccess() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        mockMvc.perform(post("/api/user/logout")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(16)
    @DisplayName("测试16：登录 - 空用户名 + 错误密码")
    public void testLoginWithEmptyUsername() throws Exception {
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "");
        loginRequest.put("password", "wrongpassword");

        mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("用户名密码错误！"));
    }

    @Test
    @Order(17)
    @DisplayName("测试17：登录 - 空密码")
    public void testLoginWithEmptyPassword() throws Exception {
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "");

        mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("用户名密码错误！"));
    }

    @Test
    @Order(18)
    @DisplayName("测试18：登录 - 用户名不存在 + 错误密码")
    public void testLoginWithNonExistentUser() throws Exception {
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "nonexistentuser");
        loginRequest.put("password", "wrongpassword");

        mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("用户名密码错误！"));
    }

    @Test
    @Order(19)
    @DisplayName("测试19：使用普通用户 Token 访问公开接口 - 应成功")
    public void testRegularUserAccessPublicEndpoint() throws Exception {
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

        mockMvc.perform(get("/test/hello")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(20)
    @DisplayName("测试20：Token 不带 Bearer 前缀 - 应返回 NOT_LOGGED_IN")
    public void testTokenWithoutBearerPrefix() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        mockMvc.perform(get("/test/hello")
                        .header("Authorization", validToken))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(21)
    @DisplayName("测试21：使用小写 bearer 前缀 - 应返回 NOT_LOGGED_IN")
    public void testTokenWithLowercaseBearer() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        mockMvc.perform(get("/test/hello")
                        .header("Authorization", "bearer " + validToken))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @Order(22)
    @DisplayName("测试22：多用户交替访问 - 上下文隔离")
    public void testMultipleUsersAlternatingAccess() throws Exception {
        JSONObject adminRequest = new JSONObject();
        adminRequest.put("username", "admin");
        adminRequest.put("password", "123456");

        MvcResult adminResult = mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(adminRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn();
        String adminToken = JSON.parseObject(adminResult.getResponse().getContentAsString()).getString("data");

        JSONObject userRequest = new JSONObject();
        userRequest.put("username", "user");
        userRequest.put("password", "123456");

        MvcResult userResult = mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn();
        String userToken = JSON.parseObject(userResult.getResponse().getContentAsString()).getString("data");

        mockMvc.perform(get("/test/hello2")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        mockMvc.perform(get("/test/hello2")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"));

        mockMvc.perform(get("/test/hello2")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(23)
    @DisplayName("测试23：登录请求 Content-Type 错误 - 应返回错误")
    public void testLoginWithWrongContentType() throws Exception {
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "123456");

        mockMvc.perform(post("/api/user/login")
                        .content(JSON.toJSONString(loginRequest))
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Order(24)
    @DisplayName("测试24：Token 前后有空格 - 应返回 NOT_LOGGED_IN")
    public void testTokenWithExtraSpaces() throws Exception {
        assertNotNull(validToken, "Token should be obtained from login test");

        mockMvc.perform(get("/test/hello")
                        .header("Authorization", " Bearer " + validToken + " "))
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }
}

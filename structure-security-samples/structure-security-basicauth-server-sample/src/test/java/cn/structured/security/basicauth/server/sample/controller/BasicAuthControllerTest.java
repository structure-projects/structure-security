package cn.structured.security.basicauth.server.sample.controller;

import cn.structure.common.utils.BasicAuthGenerator;
import cn.structured.security.basicauth.server.sample.config.AbstractIntegrationTest;
import cn.structured.security.basicauth.server.sample.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("Basic Auth Controller 测试")
class BasicAuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("认证功能测试")
    class AuthenticationTests {

        @Test
        @DisplayName("测试：未认证访问受保护接口 - 返回403")
        void testProtectedEndpointWithoutAuth() throws Exception {
            mockMvc.perform(get("/api/protected/hello"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("测试：使用 Admin 用户 Basic Auth 访问受保护接口 - 成功")
        void testProtectedEndpointWithAdminAuth() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "admin123");

            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.user").value("admin"));
        }

        @Test
        @DisplayName("测试：使用 User 用户 Basic Auth 访问受保护接口 - 成功")
        void testProtectedEndpointWithUserAuth() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("user", "user123");

            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.user").value("user"));
        }

        @Test
        @DisplayName("测试：使用 Guest 用户 Basic Auth 访问受保护接口 - 成功")
        void testProtectedEndpointWithGuestAuth() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("guest", "guest123");

            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.user").value("guest"));
        }

        @Test
        @DisplayName("测试：使用错误密码访问 - 返回401")
        void testProtectedEndpointWithWrongPassword() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "wrongpassword");

            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("测试：使用不存在的用户访问 - 返回401")
        void testProtectedEndpointWithNonExistentUser() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("nonexistent", "password");

            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("测试：使用错误格式的 Authorization 头 - 返回403")
        void testProtectedEndpointWithInvalidAuthFormat() throws Exception {
            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("测试：Authorization 头值为空 - 返回403")
        void testProtectedEndpointWithEmptyAuthHeader() throws Exception {
            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, ""))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("测试：无效的 Base64 编码 - 返回401")
        void testProtectedEndpointWithInvalidBase64() throws Exception {
            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, "Basic !@#$%^&*()"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("用户信息接口测试")
    class UserInfoTests {

        @Test
        @DisplayName("测试：获取用户信息 - Admin 用户")
        void testUserInfoAdmin() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "admin123");

            mockMvc.perform(get("/api/protected/user-info")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.username").value("admin"))
                    .andExpect(jsonPath("$.id").value("admin"));
        }

        @Test
        @DisplayName("测试：获取用户信息 - User 用户")
        void testUserInfoUser() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("user", "user123");

            mockMvc.perform(get("/api/protected/user-info")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.username").value("user"))
                    .andExpect(jsonPath("$.id").value("user"));
        }

        @Test
        @DisplayName("测试：未认证访问用户信息接口 - 返回403")
        void testUserInfoWithoutAuth() throws Exception {
            mockMvc.perform(get("/api/protected/user-info"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("权限控制测试")
    class AuthorizationTests {

        @Test
        @DisplayName("测试：Admin 访问 admin-only 接口 - 成功")
        void testAdminOnlyEndpointWithAdmin() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "admin123");

            mockMvc.perform(get("/api/protected/admin-only")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("This is admin only content"));
        }

        @Test
        @DisplayName("测试：User 访问 admin-only 接口 - 权限不足")
        void testAdminOnlyEndpointWithUser() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("user", "user123");

            mockMvc.perform(get("/api/protected/admin-only")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("测试：Guest 访问 admin-only 接口 - 无权限配置默认放行")
        void testAdminOnlyEndpointWithGuest() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("guest", "guest123");

            mockMvc.perform(get("/api/protected/admin-only")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("测试：未认证访问 admin-only 接口 - 返回403")
        void testAdminOnlyEndpointWithoutAuth() throws Exception {
            mockMvc.perform(get("/api/protected/admin-only"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("用户上下文生命周期测试")
    class UserContextLifecycleTests {

        @Test
        @DisplayName("测试：同一用户多次请求 - 用户上下文一致性")
        void testMultipleRequestsSameUser() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "admin123");

            for (int i = 0; i < 3; i++) {
                mockMvc.perform(get("/api/protected/hello")
                                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.success").value(true))
                        .andExpect(jsonPath("$.user").value("admin"));
            }
        }

        @Test
        @DisplayName("测试：不同用户请求隔离 - 用户上下文互不干扰")
        void testDifferentUsersIsolated() throws Exception {
            String adminAuthHeader = BasicAuthGenerator.generate("admin", "admin123");
            String userAuthHeader = BasicAuthGenerator.generate("user", "user123");

            mockMvc.perform(get("/api/protected/user-info")
                            .header(HttpHeaders.AUTHORIZATION, adminAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("admin"));

            mockMvc.perform(get("/api/protected/user-info")
                            .header(HttpHeaders.AUTHORIZATION, userAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user"));

            mockMvc.perform(get("/api/protected/user-info")
                            .header(HttpHeaders.AUTHORIZATION, adminAuthHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("admin"));
        }
    }

    @Nested
    @DisplayName("接口可用性测试")
    class EndpointAvailabilityTests {

        @Test
        @DisplayName("测试：/api/protected/hello 接口可用")
        void testHelloEndpointAvailable() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "admin123");
            mockMvc.perform(get("/api/protected/hello")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：/api/protected/user-info 接口可用")
        void testUserInfoEndpointAvailable() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "admin123");
            mockMvc.perform(get("/api/protected/user-info")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：/api/protected/admin-only 接口可用")
        void testAdminOnlyEndpointAvailable() throws Exception {
            String basicAuthHeader = BasicAuthGenerator.generate("admin", "admin123");
            mockMvc.perform(get("/api/protected/admin-only")
                            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                    .andExpect(status().isOk());
        }
    }
}
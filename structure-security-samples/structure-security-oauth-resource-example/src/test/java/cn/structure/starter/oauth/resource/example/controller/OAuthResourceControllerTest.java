/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structure.starter.oauth.resource.example.controller;

import cn.structure.starter.oauth.resource.example.config.AbstractIntegrationTest;
import cn.structure.starter.oauth.resource.example.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("OAuth Resource Controller 测试")
class OAuthResourceControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("未认证访问测试")
    class UnauthenticatedAccessTests {

        @Test
        @DisplayName("测试：未认证访问公开接口 /test/hello - 返回 NOT_LOGGED_IN")
        void testHelloEndpointWithoutAuth() throws Exception {
            mockMvc.perform(get("/test/hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }

        @Test
        @DisplayName("测试：未认证访问受保护接口 /test/hello2 - 返回 NOT_LOGGED_IN")
        void testHello2EndpointWithoutAuth() throws Exception {
            mockMvc.perform(get("/test/hello2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }

        @Test
        @DisplayName("测试：未认证访问不存在的接口 - 返回 NOT_LOGGED_IN 或 404")
        void testNonExistentEndpointWithoutAuth() throws Exception {
            mockMvc.perform(get("/test/nonexistent"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("认证令牌格式测试")
    class TokenFormatTests {

        @Test
        @DisplayName("测试：使用无效 Bearer Token 访问 - 返回 INVALID_AUTHENTICATION")
        void testInvalidBearerToken() throws Exception {
            mockMvc.perform(get("/test/hello")
                            .header("Authorization", "Bearer invalid-token-12345"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("INVALID_AUTHENTICATION"));
        }

        @Test
        @DisplayName("测试：使用空 Bearer Token 访问 - 返回 NOT_LOGGED_IN")
        void testEmptyBearerToken() throws Exception {
            mockMvc.perform(get("/test/hello")
                            .header("Authorization", "Bearer "))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }

        @Test
        @DisplayName("测试：使用 Basic Auth 格式访问 - 返回 NOT_LOGGED_IN")
        void testBasicAuthFormat() throws Exception {
            mockMvc.perform(get("/test/hello")
                            .header("Authorization", "Basic dXNlcjpwYXNz"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }

        @Test
        @DisplayName("测试：无 Authorization 头访问 - 返回 NOT_LOGGED_IN")
        void testNoAuthHeader() throws Exception {
            mockMvc.perform(get("/test/hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }

        @Test
        @DisplayName("测试：Authorization 头值为空 - 返回 NOT_LOGGED_IN")
        void testEmptyAuthHeader() throws Exception {
            mockMvc.perform(get("/test/hello")
                            .header("Authorization", ""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }
    }

    @Nested
    @DisplayName("权限控制测试")
    class AuthorizationTests {

        @Test
        @DisplayName("测试：未认证访问需要 ROLE_ADMIN 权限的接口 - 返回 NOT_LOGGED_IN")
        void testAdminEndpointWithoutAuth() throws Exception {
            mockMvc.perform(get("/test/hello2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }

        @Test
        @DisplayName("测试：无效 Token 访问需要 ROLE_ADMIN 权限的接口 - 返回 INVALID_AUTHENTICATION")
        void testAdminEndpointWithInvalidToken() throws Exception {
            mockMvc.perform(get("/test/hello2")
                            .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("INVALID_AUTHENTICATION"));
        }
    }

    @Nested
    @DisplayName("HTTP 方法测试")
    class HttpMethodTests {

        @Test
        @DisplayName("测试：GET 请求访问 /test/hello - 返回 NOT_LOGGED_IN")
        void testGetRequest() throws Exception {
            mockMvc.perform(get("/test/hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }

        @Test
        @DisplayName("测试：POST 请求访问 /test/hello - 返回 NOT_LOGGED_IN")
        void testPostRequest() throws Exception {
            mockMvc.perform(post("/test/hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }
    }

    @Nested
    @DisplayName("多次请求测试")
    class MultipleRequestTests {

        @Test
        @DisplayName("测试：多次未认证请求 - 结果一致")
        void testMultipleUnauthenticatedRequests() throws Exception {
            for (int i = 0; i < 3; i++) {
                mockMvc.perform(get("/test/hello"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
            }
        }

        @Test
        @DisplayName("测试：交替访问不同接口 - 结果一致")
        void testAlternatingEndpoints() throws Exception {
            mockMvc.perform(get("/test/hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));

            mockMvc.perform(get("/test/hello2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));

            mockMvc.perform(get("/test/hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
        }
    }

    @Nested
    @DisplayName("接口可用性测试")
    class EndpointAvailabilityTests {

        @Test
        @DisplayName("测试：/test/hello 接口可用")
        void testHelloEndpointAvailable() throws Exception {
            mockMvc.perform(get("/test/hello"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：/test/hello2 接口可用")
        void testHello2EndpointAvailable() throws Exception {
            mockMvc.perform(get("/test/hello2"))
                    .andExpect(status().isOk());
        }
    }
}
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

package cn.structured.example.core.endpoint;

import cn.structured.example.core.config.AbstractIntegrationTest;
import cn.structured.example.core.config.TestConfig;
import cn.structured.security.context.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("Core Endpoint 测试")
class CoreEndpointTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // 清理线程上下文，避免测试间 UserContext 污染
        // MockMvc 在同一线程运行，前一个测试设置的上下文会残留到后续测试
        UserContext.remove();
    }

    @Nested
    @DisplayName("用户上下文设置测试")
    class SetUserContextTests {

        @Test
        @DisplayName("测试：设置用户上下文 - 正常用户ID")
        void testSetUserWithValidId() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "123"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User set: 123"));
        }

        @Test
        @DisplayName("测试：设置用户上下文 - 长用户ID")
        void testSetUserWithLongId() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "9999999999"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User set: 9999999999"));
        }

        @Test
        @DisplayName("测试：设置用户上下文 - 字符串用户ID")
        void testSetUserWithStringId() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "user-abc-123"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User set: user-abc-123"));
        }

        @Test
        @DisplayName("测试：设置用户上下文 - 空用户ID")
        void testSetUserWithEmptyId() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", ""))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User set: "));
        }

        @Test
        @DisplayName("测试：设置用户上下文 - 特殊字符用户ID")
        void testSetUserWithSpecialCharsId() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "user@#$%"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User set: user@#$%"));
        }
    }

    @Nested
    @DisplayName("用户上下文获取测试")
    class GetUserContextTests {

        @Test
        @DisplayName("测试：获取用户上下文 - 未设置时返回空")
        void testGetUserWithoutSet() throws Exception {
            mockMvc.perform(get("/api/core/get-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("No user context"));
        }

        @Test
        @DisplayName("测试：获取用户ID - 未设置时返回null")
        void testGetUserIdWithoutSet() throws Exception {
            mockMvc.perform(get("/api/core/get-user-id"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User ID: null"));
        }

        @Test
        @DisplayName("测试：重复获取用户上下文 - 结果一致")
        void testGetUserMultipleTimes() throws Exception {
            for (int i = 0; i < 3; i++) {
                mockMvc.perform(get("/api/core/get-user"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("No user context"));
            }
        }
    }

    @Nested
    @DisplayName("用户上下文清除测试")
    class ClearUserContextTests {

        @Test
        @DisplayName("测试：清除用户上下文 - 未设置时清除")
        void testClearWithoutSet() throws Exception {
            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User context cleared"));
        }

        @Test
        @DisplayName("测试：重复清除用户上下文 - 幂等性")
        void testMultipleClears() throws Exception {
            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User context cleared"));

            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User context cleared"));

            mockMvc.perform(get("/api/core/get-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("No user context"));
        }

        @Test
        @DisplayName("测试：清除后获取 - 确保上下文为空")
        void testClearThenGet() throws Exception {
            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/core/get-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("No user context"));
        }
    }

    @Nested
    @DisplayName("认证状态测试")
    class AuthenticationStatusTests {

        @Test
        @DisplayName("测试：检查认证状态 - 未设置用户时应为未认证")
        void testCheckAuthenticatedWithoutSet() throws Exception {
            mockMvc.perform(get("/api/core/check-authenticated"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Is authenticated: false"));
        }

        @Test
        @DisplayName("测试：重复检查认证状态 - 结果一致")
        void testCheckAuthenticatedMultipleTimes() throws Exception {
            for (int i = 0; i < 3; i++) {
                mockMvc.perform(get("/api/core/check-authenticated"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Is authenticated: false"));
            }
        }

        @Test
        @DisplayName("测试：清除后检查认证状态 - 应为未认证")
        void testCheckAuthenticatedAfterClear() throws Exception {
            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/core/check-authenticated"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Is authenticated: false"));
        }
    }

    @Nested
    @DisplayName("用户上下文隔离测试")
    class UserContextIsolationTests {

        @Test
        @DisplayName("测试：请求之间上下文隔离 - 每次请求独立")
        void testContextIsolationBetweenRequests() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "isolated-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User set: isolated-user"));

            // 模拟请求间 UserContextFilter 清除上下文的行为
            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/core/get-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("No user context"));
        }

        @Test
        @DisplayName("测试：认证状态隔离 - 每次请求独立")
        void testAuthIsolationBetweenRequests() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "auth-isolation"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/core/check-authenticated"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Is authenticated: false"));
        }

        @Test
        @DisplayName("测试：多用户交替请求 - 上下文互不干扰")
        void testMultipleUsersAlternatingRequests() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "user-a"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "user-b"))
                    .andExpect(status().isOk());

            // 模拟请求间 UserContextFilter 清除上下文的行为
            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/core/get-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("No user context"));
        }
    }

    @Nested
    @DisplayName("接口可用性测试")
    class EndpointAvailabilityTests {

        @Test
        @DisplayName("测试：set-user 接口可用")
        void testSetUserEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/core/set-user")
                            .param("userId", "test"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：get-user 接口可用")
        void testGetUserEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/core/get-user"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：get-user-id 接口可用")
        void testGetUserIdEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/core/get-user-id"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：clear 接口可用")
        void testClearEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/core/clear"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：check-authenticated 接口可用")
        void testCheckAuthenticatedEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/core/check-authenticated"))
                    .andExpect(status().isOk());
        }
    }
}
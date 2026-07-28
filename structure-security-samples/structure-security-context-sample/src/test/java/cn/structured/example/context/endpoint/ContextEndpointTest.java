package cn.structured.example.context.endpoint;

import cn.structured.example.context.config.AbstractIntegrationTest;
import cn.structured.example.context.config.TestConfig;
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
@DisplayName("Context Endpoint 测试")
class ContextEndpointTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // 清理线程上下文，避免测试间 UserContext 污染
        // （如 LoginTests/EndpointAvailabilityTests 设置的用户会在无登录测试中残留）
        UserContext.remove();
    }

    @Nested
    @DisplayName("登录功能测试")
    class LoginTests {

        @Test
        @DisplayName("测试：登录 - 正常用户ID")
        void testLoginWithValidUserId() throws Exception {
            mockMvc.perform(get("/api/context/login")
                            .param("userId", "user-123"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Login success: user-123"));
        }

        @Test
        @DisplayName("测试：登录 - 带用户名")
        void testLoginWithUserName() throws Exception {
            mockMvc.perform(get("/api/context/login")
                            .param("userId", "user-456")
                            .param("userName", "testuser"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Login success: user-456"));
        }

        @Test
        @DisplayName("测试：登录 - 空用户ID")
        void testLoginWithEmptyUserId() throws Exception {
            mockMvc.perform(get("/api/context/login")
                            .param("userId", ""))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Login success: "));
        }

        @Test
        @DisplayName("测试：登录 - 长用户ID")
        void testLoginWithLongUserId() throws Exception {
            mockMvc.perform(get("/api/context/login")
                            .param("userId", "999999999999999"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Login success: 999999999999999"));
        }

        @Test
        @DisplayName("测试：登录 - 特殊字符用户ID")
        void testLoginWithSpecialCharsUserId() throws Exception {
            mockMvc.perform(get("/api/context/login")
                            .param("userId", "user@#$%^&*()"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Login success: user@#$%^&*()"));
        }
    }

    @Nested
    @DisplayName("登出功能测试")
    class LogoutTests {

        @Test
        @DisplayName("测试：登出 - 正常登出")
        void testLogout() throws Exception {
            mockMvc.perform(get("/api/context/logout"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Logout success"));
        }

        @Test
        @DisplayName("测试：登出 - 未登录时登出")
        void testLogoutWithoutLogin() throws Exception {
            mockMvc.perform(get("/api/context/logout"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Logout success"));
        }

        @Test
        @DisplayName("测试：重复登出 - 幂等性")
        void testMultipleLogout() throws Exception {
            mockMvc.perform(get("/api/context/logout"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Logout success"));

            mockMvc.perform(get("/api/context/logout"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Logout success"));
        }
    }

    @Nested
    @DisplayName("当前用户查询测试")
    class CurrentUserTests {

        @Test
        @DisplayName("测试：获取当前用户 - 未登录时返回空")
        void testGetCurrentUserWithoutLogin() throws Exception {
            mockMvc.perform(get("/api/context/current"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("No user logged in"));
        }

        @Test
        @DisplayName("测试：重复获取当前用户 - 结果一致")
        void testGetCurrentUserMultipleTimes() throws Exception {
            for (int i = 0; i < 3; i++) {
                mockMvc.perform(get("/api/context/current"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("No user logged in"));
            }
        }
    }

    @Nested
    @DisplayName("用户存储 - 添加用户测试")
    class UserStoreAddTests {

        @Test
        @DisplayName("测试：添加用户 - 正常用户ID")
        void testAddUserWithValidId() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "store-user-1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User added: store-user-1"));
        }

        @Test
        @DisplayName("测试：添加用户 - 空用户ID")
        void testAddUserWithEmptyId() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", ""))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User added: "));
        }

        @Test
        @DisplayName("测试：添加用户 - 长用户ID")
        void testAddUserWithLongId() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "very-long-user-id-1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User added: very-long-user-id-1234567890"));
        }

        @Test
        @DisplayName("测试：重复添加同一用户 - 覆盖旧值")
        void testAddSameUserMultipleTimes() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "repeat-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User added: repeat-user"));

            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "repeat-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User added: repeat-user"));
        }
    }

    @Nested
    @DisplayName("用户存储 - 查询用户测试")
    class UserStoreGetTests {

        @Test
        @DisplayName("测试：查询用户 - 存在的用户")
        void testGetExistingUser() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "existing-user"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "existing-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User found: existing-user"));
        }

        @Test
        @DisplayName("测试：查询用户 - 不存在的用户")
        void testGetNonExistentUser() throws Exception {
            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "nonexistent-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User not found: nonexistent-user"));
        }

        @Test
        @DisplayName("测试：查询用户 - 空用户ID")
        void testGetUserWithEmptyId() throws Exception {
            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", ""))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User not found: "));
        }
    }

    @Nested
    @DisplayName("用户存储 - 删除用户测试")
    class UserStoreRemoveTests {

        @Test
        @DisplayName("测试：删除用户 - 存在的用户")
        void testRemoveExistingUser() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "to-remove-user"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/context/store/remove")
                            .param("userId", "to-remove-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User removed: to-remove-user"));

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "to-remove-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User not found: to-remove-user"));
        }

        @Test
        @DisplayName("测试：删除用户 - 不存在的用户")
        void testRemoveNonExistentUser() throws Exception {
            mockMvc.perform(get("/api/context/store/remove")
                            .param("userId", "nonexistent-remove-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User removed: nonexistent-remove-user"));
        }

        @Test
        @DisplayName("测试：重复删除用户 - 幂等性")
        void testRemoveSameUserMultipleTimes() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "multi-remove-user"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/context/store/remove")
                            .param("userId", "multi-remove-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User removed: multi-remove-user"));

            mockMvc.perform(get("/api/context/store/remove")
                            .param("userId", "multi-remove-user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User removed: multi-remove-user"));
        }
    }

    @Nested
    @DisplayName("用户存储 - 完整生命周期测试")
    class UserStoreLifecycleTests {

        @Test
        @DisplayName("测试：完整生命周期 - 添加、查询、删除、验证")
        void testFullLifecycle() throws Exception {
            String userId = "lifecycle-user";

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User not found: " + userId));

            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User added: " + userId));

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User found: " + userId));

            mockMvc.perform(get("/api/context/store/remove")
                            .param("userId", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User removed: " + userId));

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User not found: " + userId));
        }

        @Test
        @DisplayName("测试：多用户存储 - 互不干扰")
        void testMultipleUsers() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "user-a"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "user-b"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "user-a"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User found: user-a"));

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "user-b"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User found: user-b"));

            mockMvc.perform(get("/api/context/store/remove")
                            .param("userId", "user-a"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "user-a"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User not found: user-a"));

            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "user-b"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User found: user-b"));
        }
    }

    @Nested
    @DisplayName("接口可用性测试")
    class EndpointAvailabilityTests {

        @Test
        @DisplayName("测试：login 接口可用")
        void testLoginEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/context/login")
                            .param("userId", "test"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：logout 接口可用")
        void testLogoutEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/context/logout"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：current 接口可用")
        void testCurrentEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/context/current"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：store/add 接口可用")
        void testStoreAddEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/context/store/add")
                            .param("userId", "test"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：store/get 接口可用")
        void testStoreGetEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/context/store/get")
                            .param("userId", "test"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("测试：store/remove 接口可用")
        void testStoreRemoveEndpointAvailable() throws Exception {
            mockMvc.perform(get("/api/context/store/remove")
                            .param("userId", "test"))
                    .andExpect(status().isOk());
        }
    }
}
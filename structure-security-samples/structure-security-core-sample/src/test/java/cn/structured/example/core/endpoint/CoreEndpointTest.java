package cn.structured.example.core.endpoint;

import cn.structured.example.core.config.AbstractIntegrationTest;
import cn.structured.example.core.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("Core Endpoint 测试")
class CoreEndpointTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("测试：访问 core 接口 - 需要认证")
    void testCoreEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/core/get-user"))
                .andExpect(status().isUnauthorized());
    }
}
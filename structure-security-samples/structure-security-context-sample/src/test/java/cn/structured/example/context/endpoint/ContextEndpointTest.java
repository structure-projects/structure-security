package cn.structured.example.context.endpoint;

import cn.structured.example.context.config.AbstractIntegrationTest;
import cn.structured.example.context.config.TestConfig;
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
@DisplayName("Context Endpoint 测试")
class ContextEndpointTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("测试：访问 context 接口 - 需要认证")
    void testContextEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/context/current"))
                .andExpect(status().isUnauthorized());
    }
}
package cn.structured.security.basicauth.server.sample.controller;

import cn.structured.security.basicauth.server.sample.config.AbstractIntegrationTest;
import cn.structured.security.basicauth.server.sample.config.TestConfig;
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
@DisplayName("Basic Auth Controller 测试")
class BasicAuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("测试：访问受保护接口 - 需要认证")
    void testProtectedEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/protected/hello"))
                .andExpect(status().isForbidden());
    }
}
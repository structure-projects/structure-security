package cn.structure.starter.oauth.resource.example.controller;

import cn.structure.starter.oauth.resource.example.config.AbstractIntegrationTest;
import cn.structure.starter.oauth.resource.example.config.TestConfig;
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
@DisplayName("OAuth Resource Controller 测试")
class OAuthResourceControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("测试1：访问公开接口 /test/hello - 需要认证")
    void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/test/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }

    @Test
    @DisplayName("测试2：访问需要权限的接口 /test/hello2 - 需要认证")
    void testProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/test/hello2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_LOGGED_IN"));
    }
}
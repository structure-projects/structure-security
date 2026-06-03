package cn.structured.security.basicauth.server.sample;

import cn.structured.security.util.BasicAuthGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasicAuthServerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicEndpointWithoutAuth() throws Exception {
        // 测试公开接口不需要认证
        mockMvc.perform(get("/api/public/demo"))
                .andExpect(status().isOk());
    }

    @Test
    void testProtectedEndpointWithBasicAuth() throws Exception {
        // 测试使用 Basic Auth 访问受保护接口
        String authHeader = BasicAuthGenerator.generate("admin", "admin123");
        
        mockMvc.perform(get("/api/protected/hello")
                        .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user").value("admin"));
    }

    @Test
    void testProtectedEndpointWithWrongPassword() throws Exception {
        // 测试错误的密码
        String authHeader = BasicAuthGenerator.generate("admin", "wrongpass");
        
        mockMvc.perform(get("/api/protected/hello")
                        .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testJwtLogin() throws Exception {
        // 测试 JWT 登录
        String loginRequest = "{\"username\":\"admin\",\"password\":\"admin123\"}";
        
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk());
    }
}

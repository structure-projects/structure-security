package cn.structured.security.basicauth.client.sample;

import cn.structured.security.basicauth.client.BasicAuthGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BasicAuthClientTest {

    @Test
    void testGenerateAndParse() {
        // 测试生成 Basic Auth 头的生成和解析
        String username = "testUser";
        String password = "testPass";
        
        // 生成
        String authHeader = BasicAuthGenerator.generate(username, password);
        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Basic "));
        
        // 解析
        String[] credentials = BasicAuthGenerator.parse(authHeader);
        assertEquals(username, credentials[0]);
        assertEquals(password, credentials[1]);
    }

    @Test
    void testGenerateKnownValue() {
        // 测试已知值
        String username = "admin";
        String password = "admin123";
        String authHeader = BasicAuthGenerator.generate(username, password);
        
        String[] credentials = BasicAuthGenerator.parse(authHeader);
        assertEquals(username, credentials[0]);
        assertEquals(password, credentials[1]);
    }
}

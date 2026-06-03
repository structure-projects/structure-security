package cn.structured.security.basicauth.server.sample.controller;

import cn.structured.security.util.BasicAuthGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 公开接口控制器
 * 不需要认证即可访问
 *
 * @author chuck
 */
@Slf4j
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/demo")
    public Map<String, Object> demo() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("message", "Basic Auth + JWT Integration Demo");
        result.put("description", "This is a public endpoint that doesn't require authentication");
        
        // 使用说明
        Map<String, String> instructions = new HashMap<>();
        instructions.put("basic_auth", "Use Basic Auth header to access protected endpoints");
        instructions.put("jwt_login", "Use /api/user/login to get JWT token");
        instructions.put("jwt_auth", "Use 'Authorization: Bearer <token>' header");
        
        result.put("instructions", instructions);
        
        return result;
    }

    @GetMapping("/help")
    public Map<String, Object> help() {
        Map<String, Object> help = new HashMap<>();
        
        // Basic Auth 使用示例
        String exampleHeader = BasicAuthGenerator.generate("admin", "admin123");
        
        Map<String, String> basicAuthExample = new HashMap<>();
        basicAuthExample.put("curl_example", 
                "curl -u admin:admin123 http://localhost:8082/api/protected/hello");
        basicAuthExample.put("header_example", 
                "Authorization: " + exampleHeader);
        
        help.put("basic_auth", basicAuthExample);
        
        // JWT 使用示例
        Map<String, String> jwtExample = new HashMap<>();
        jwtExample.put("login", "POST /api/user/login with {\"username\":\"admin\",\"password\":\"admin123\"}");
        jwtExample.put("use_token", "Add 'Authorization: Bearer <token>' header to requests");
        
        help.put("jwt_auth", jwtExample);
        
        help.put("credentials", "admin/admin123, user/user123");
        
        return help;
    }
}

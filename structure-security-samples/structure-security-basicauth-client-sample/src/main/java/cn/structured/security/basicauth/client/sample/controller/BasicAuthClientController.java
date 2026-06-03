package cn.structured.security.basicauth.client.sample.controller;

import cn.structured.security.basicauth.client.BasicAuthGenerator;
import cn.structured.security.basicauth.client.sample.service.BasicAuthClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic Auth 客户端示例控制器
 * 演示如何使用 Basic Auth 发送标准请求
 *
 * @author chuck
 */
@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class BasicAuthClientController {

    private final BasicAuthClientService basicAuthClientService;

    /**
     * 生成 Basic Auth 头
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateAuthHeader(
            @RequestParam String username,
            @RequestParam String password) {
        log.info("Generating Basic Auth header for user: {}", username);
        
        String authHeader = BasicAuthGenerator.generate(username, password);
        
        Map<String, String> result = new HashMap<>();
        result.put("authorization", authHeader);
        result.put("username", username);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 解析 Basic Auth 头
     */
    @PostMapping("/parse")
    public ResponseEntity<Map<String, String>> parseAuthHeader(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Parsing Basic Auth header");
        
        try {
            String[] credentials = BasicAuthGenerator.parse(authHeader);
            
            Map<String, String> result = new HashMap<>();
            result.put("username", credentials[0]);
            result.put("password", credentials[1]);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "Invalid Basic Auth header");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 模拟向 Basic Auth 服务器发送请求
     */
    @PostMapping("/send-request")
    public ResponseEntity<Map<String, Object>> sendRequestToServer(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "http://localhost:8082/api/protected/hello") String targetUrl) {
        log.info("Sending request to server with user: {}", username);
        
        Map<String, Object> result = new HashMap<>();
        
        // 生成 Basic Auth 头
        String authHeader = BasicAuthGenerator.generate(username, password);
        
        // 这里演示了发送请求的逻辑
        // 在实际项目中，可以使用 RestTemplate、WebClient 等
        result.put("authorization_header", authHeader);
        result.put("target_url", targetUrl);
        result.put("description", "This demonstrates how to add Basic Auth header to requests");
        
        return ResponseEntity.ok(result);
    }

    /**
     * 演示使用说明
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        Map<String, Object> demo = new HashMap<>();
        demo.put("message", "Basic Auth Client Demo");
        
        // 演示标准的 Basic Auth 头格式
        String exampleHeader = BasicAuthGenerator.generate("demoUser", "demoPass");
        demo.put("example_authorization_header", exampleHeader);
        
        // 使用说明
        Map<String, String> instructions = new HashMap<>();
        instructions.put("step1", "Use /api/client/generate to create Basic Auth header");
        instructions.put("step2", "Add 'Authorization: " + exampleHeader + "' header to your requests");
        instructions.put("step3", "The server will validate the credentials from the header");
        
        demo.put("instructions", instructions);
        
        return ResponseEntity.ok(demo);
    }
}

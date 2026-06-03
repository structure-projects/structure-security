package cn.structured.security.basicauth.client.sample.controller;

import cn.structured.security.basicauth.client.BasicAuthGenerator;
import cn.structured.security.basicauth.client.service.BasicAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/basicauth")
public class BasicAuthTestController {

    @Autowired
    private BasicAuthService basicAuthService;

    @GetMapping("/generate")
    public Map<String, Object> generateAuthHeader(@RequestParam String username, 
                                                  @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        
        String authHeader = BasicAuthGenerator.generate(username, password);
        
        result.put("success", true);
        result.put("authHeader", authHeader);
        result.put("username", username);
        
        log.info("Generated Basic Auth header for user: {}", username);
        
        return result;
    }

    @GetMapping("/generate-default")
    public Map<String, Object> generateDefaultAuthHeader() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String authHeader = basicAuthService.generateAuthHeader();
            result.put("success", true);
            result.put("authHeader", authHeader);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Default username/password not configured. " +
                    "Please set structure.security.basicauth.client.username and " +
                    "structure.security.basicauth.client.password in application.yml");
        }
        
        return result;
    }

    @PostMapping("/parse")
    public Map<String, Object> parseAuthHeader(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        
        String authHeader = request.get("authHeader");
        try {
            String[] credentials = BasicAuthGenerator.parse(authHeader);
            
            result.put("success", true);
            result.put("username", credentials[0]);
            result.put("password", credentials[1]);
            
            log.info("Parsed Basic Auth header for user: {}", credentials[0]);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/demo")
    public Map<String, Object> demo() {
        Map<String, Object> result = new HashMap<>();
        
        // 方式一：静态方法
        String header1 = BasicAuthGenerator.generate("demoUser", "demoPass");
        
        // 方式二：Service 方法（如果配置了默认值）
        String header2 = null;
        try {
            header2 = basicAuthService.generateAuthHeader();
        } catch (Exception e) {
            header2 = "Not configured - see /generate-default for details";
        }
        
        result.put("staticMethodHeader", header1);
        result.put("serviceMethodHeader", header2);
        result.put("demo", "Basic Auth Client Demo");
        
        return result;
    }
}

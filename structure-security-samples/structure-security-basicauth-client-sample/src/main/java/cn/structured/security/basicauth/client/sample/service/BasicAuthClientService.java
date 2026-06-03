package cn.structured.security.basicauth.client.sample.service;

import cn.structured.security.basicauth.client.BasicAuthGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic Auth 客户端服务
 * 演示作为 client 如何调用需要 Basic Auth 的 server
 *
 * @author chuck
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthClientService {

    private final RestTemplate restTemplate;

    /**
     * 使用 Basic Auth 调用服务端
     */
    public Map<String, Object> callWithBasicAuth(String username, String password, String targetUrl) {
        log.info("Calling server with Basic Auth: {}", targetUrl);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成 Basic Auth 头
            String authHeader = BasicAuthGenerator.generate(username, password);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            
            // 发送请求
            ResponseEntity<Map> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                requestEntity,
                Map.class
            );
            
            result.put("success", true);
            result.put("status_code", response.getStatusCode().value());
            result.put("response_body", response.getBody());
            result.put("authorization_header_used", authHeader);
            
            log.info("Request successful, status code: {}", response.getStatusCode());
            
        } catch (Exception e) {
            log.error("Request failed: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 演示多种调用方式
     */
    public Map<String, Object> demoAllCalls() {
        Map<String, Object> demo = new HashMap<>();
        
        demo.put("description", "Basic Auth Client 演示：作为客户端如何调用服务端");
        
        // 使用示例
        Map<String, String> examples = new HashMap<>();
        examples.put("target_url", "http://localhost:8082/api/protected/hello");
        examples.put("username", "admin");
        examples.put("password", "admin123");
        examples.put("curl_command", "curl -u admin:admin123 http://localhost:8082/api/protected/hello");
        examples.put("curl_with_header", "curl http://localhost:8082/api/protected/hello -H \"Authorization: Basic YWRtaW46YWRtaW4xMjM=\"");
        
        demo.put("examples", examples);
        
        return demo;
    }
}

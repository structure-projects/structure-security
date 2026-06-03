package cn.structured.security.basicauth.client.sample.controller;

import cn.structured.security.basicauth.client.sample.service.BasicAuthClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Basic Auth 客户端示例控制器
 * 演示作为 client 如何调用需要 Basic Auth 的 server
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
     * 使用 Basic Auth 调用服务端
     */
    @PostMapping("/call")
    public ResponseEntity<Map<String, Object>> callServer(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "http://localhost:8082/api/protected/hello") String targetUrl) {
        log.info("Calling server with user: {}", username);
        
        Map<String, Object> result = basicAuthClientService.callWithBasicAuth(username, password, targetUrl);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 演示页面信息
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        log.info("Showing demo information");
        
        Map<String, Object> demo = basicAuthClientService.demoAllCalls();
        
        return ResponseEntity.ok(demo);
    }
}

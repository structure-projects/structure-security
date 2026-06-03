package cn.structured.security.basicauth.client.sample.service;

import cn.structured.security.basicauth.client.BasicAuthGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic Auth 客户端服务
 * 演示如何使用 WebClient 发送带 Basic Auth 的请求
 *
 * @author chuck
 */
@Slf4j
@Service
public class BasicAuthClientService {

    /**
     * 使用 WebClient 发送带 Basic Auth 的请求（标准方式）
     */
    public Mono<Map<String, Object>> sendBasicAuthRequest(
            String baseUrl,
            String username,
            String password,
            String endpoint) {
        
        // 标准方式1: 直接使用 WebClient 的 defaultHeaders
        String authHeader = BasicAuthGenerator.generate(username, password);
        
        return WebClient.create(baseUrl)
                .get()
                .uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("response", response);
                    result.put("used_auth_header", authHeader);
                    return result;
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("Request failed: {}", e.getMessage());
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("success", false);
                    errorResult.put("status", e.getStatusCode().value());
                    errorResult.put("error", e.getMessage());
                    return Mono.just(errorResult);
                });
    }

    /**
     * 另一种方式: 使用 WebClient 的基础认证方法
     */
    public Mono<Map<String, Object>> sendBasicAuthRequestWithBuilder(
            String baseUrl,
            String username,
            String password,
            String endpoint) {
        
        // 标准方式2: 使用 WebClient.Builder 的默认认证
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> {
                    headers.setBasicAuth(username, password);
                })
                .build();
        
        return webClient
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("response", response);
                    result.put("method", "Using WebClient basicAuth()");
                    return result;
                });
    }
}

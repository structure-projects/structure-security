package cn.structured.security.basicauth.client.sample;

import cn.structured.security.basicauth.client.BasicAuthGenerator;
import cn.structured.security.basicauth.client.sample.service.BasicAuthClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Basic Auth 客户端示例应用
 * 演示如何使用 Basic Auth 发送标准请求
 *
 * @author chuck
 */
@Slf4j
@SpringBootApplication
public class BasicAuthClientSampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BasicAuthClientSampleApplication.class, args);
        
        log.info("Basic Auth Client Sample Application Started!");
        
        // 演示如何使用 BasicAuthGenerator
        demoBasicAuthUsage();
    }

    private static void demoBasicAuthUsage() {
        log.info("========== Basic Auth 使用演示 ==========");
        
        // 示例 1: 生成 Basic Auth 头
        String authHeader = BasicAuthGenerator.generate("admin", "admin123");
        log.info("生成的 Basic Auth 头: {}", authHeader);
        
        // 示例 2: 解析 Basic Auth 头
        String[] credentials = BasicAuthGenerator.parse(authHeader);
        log.info("解析结果 - 用户名: {}, 密码: {}", credentials[0], credentials[1]);
        
        log.info("=======================================");
    }
}

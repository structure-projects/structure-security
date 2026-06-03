package cn.structured.security.basicauth.server.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Basic Auth 服务端示例应用
 * 演示如何集成 Basic Auth Server 和 JWT
 *
 * @author chuck
 */
@Slf4j
@SpringBootApplication
public class BasicAuthServerSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthServerSampleApplication.class, args);
        
        log.info("Basic Auth Server Sample Application Started!");
        log.info("Test with: curl -u admin:admin123 http://localhost:8082/api/protected/hello");
    }
}

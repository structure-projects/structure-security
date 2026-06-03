package cn.structured.security.basicauth.client.sample;

import cn.structured.security.basicauth.client.sample.service.BasicAuthClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Basic Auth 客户端示例应用
 * 演示作为 client 如何调用需要 Basic Auth 的 server
 *
 * @author chuck
 */
@Slf4j
@SpringBootApplication
public class BasicAuthClientSampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BasicAuthClientSampleApplication.class, args);
        
        log.info("Basic Auth Client Sample Application Started!");
        log.info("Demo: curl http://localhost:8081/api/client/demo");
        log.info("Call server: curl -X POST \"http://localhost:8081/api/client/call?username=admin&password=admin123\"");
    }
}

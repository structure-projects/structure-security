package cn.structured.security.basicauth.client.configuration;

import cn.structured.security.basicauth.client.properties.BasicAuthProperties;
import cn.structured.security.basicauth.client.service.BasicAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic Auth 客户端自动配置类
 *
 * @author chuck
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(BasicAuthProperties.class)
@ConditionalOnProperty(prefix = "structure.security.basicauth.client", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BasicAuthClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthService basicAuthService(BasicAuthProperties properties) {
        log.info("Initializing BasicAuthService");
        return new BasicAuthService(properties);
    }
}

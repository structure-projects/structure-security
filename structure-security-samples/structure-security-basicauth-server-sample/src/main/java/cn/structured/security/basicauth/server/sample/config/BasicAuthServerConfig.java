package cn.structured.security.basicauth.server.sample.config;

import cn.structure.starter.jwt.endpoint.LoginEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic Auth + JWT 配置类
 *
 * @author chuck
 */
@Configuration
public class BasicAuthServerConfig {

    @Bean
    public LoginEndpoint loginEndpoint() {
        return new LoginEndpoint();
    }
}

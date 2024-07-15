package cn.structure.example.jwt.config;

import cn.structure.starter.jwt.endpoint.LoginEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cqliut
 * @version 2023.0625
 * @since 1.0.1
 */
@Configuration
public class JwtConfiguration {

    @Bean
    public LoginEndpoint loginEndpoint() {
        return new LoginEndpoint();
    }
}

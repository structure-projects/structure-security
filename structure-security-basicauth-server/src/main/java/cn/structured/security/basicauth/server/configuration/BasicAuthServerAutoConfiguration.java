package cn.structured.security.basicauth.server.configuration;

import cn.structured.security.basicauth.server.entrypoint.BasicAuthEntryPoint;
import cn.structured.security.basicauth.server.filter.BasicAuthFilter;
import cn.structured.security.basicauth.server.interfaces.CredentialValidator;
import cn.structured.security.basicauth.server.properties.BasicAuthServerProperties;
import cn.structured.security.basicauth.server.validator.InMemoryCredentialValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Basic Auth 服务端自动配置类
 *
 * @author chuck
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(BasicAuthServerProperties.class)
@ConditionalOnProperty(prefix = "structure.security.basicauth.server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BasicAuthServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CredentialValidator credentialValidator(BasicAuthServerProperties properties) {
        log.info("Initializing InMemoryCredentialValidator");
        return new InMemoryCredentialValidator(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint basicAuthEntryPoint(BasicAuthServerProperties properties) {
        log.info("Initializing BasicAuthEntryPoint");
        return new BasicAuthEntryPoint(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthFilter basicAuthFilter(CredentialValidator credentialValidator,
                                           AuthenticationEntryPoint authenticationEntryPoint) {
        log.info("Initializing BasicAuthFilter");
        return new BasicAuthFilter( credentialValidator, authenticationEntryPoint);
    }
}

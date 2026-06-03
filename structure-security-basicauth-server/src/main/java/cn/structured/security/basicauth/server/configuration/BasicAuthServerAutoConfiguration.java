package cn.structured.security.basicauth.server.configuration;

import cn.structured.security.basicauth.server.entrypoint.BasicAuthEntryPoint;
import cn.structured.security.basicauth.server.filter.BasicAuthFilter;
import cn.structured.security.basicauth.server.interfaces.CredentialValidator;
import cn.structured.security.basicauth.server.properties.BasicAuthServerProperties;
import cn.structured.security.basicauth.server.validator.InMemoryCredentialValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        return new BasicAuthFilter(credentialValidator, authenticationEntryPoint);
    }

    /**
     * 当 JWT Web Security 配置不存在时，提供默认的 SecurityFilterChain
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    @ConditionalOnClass(name = "org.springframework.security.config.annotation.web.configuration.EnableWebSecurity")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SecurityFilterChain basicAuthSecurityFilterChain(HttpSecurity httpSecurity, BasicAuthFilter basicAuthFilter) throws Exception {
        log.info("Configuring Basic Auth SecurityFilterChain");
        
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }
}

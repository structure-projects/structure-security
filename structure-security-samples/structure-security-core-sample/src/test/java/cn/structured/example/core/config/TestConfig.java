package cn.structured.example.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@TestConfiguration
@EnableMethodSecurity
public class TestConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public AuthorizationManager<RequestAuthorizationContext> requestAuthorizationContextAuthorizationManager() {
        return (authentication, context) -> new AuthorizationDecision(true);
    }
}
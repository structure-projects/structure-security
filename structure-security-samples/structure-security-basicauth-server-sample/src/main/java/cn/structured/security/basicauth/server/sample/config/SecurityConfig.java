package cn.structured.security.basicauth.server.sample.config;

import cn.structured.security.basicauth.server.filter.BasicAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private BasicAuthFilter basicAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configuring Basic Auth + JWT Security");
        
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**", "/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}

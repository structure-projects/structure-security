package cn.structure.starter.jwt.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.common.constant.SymbolConstant;
import cn.structure.common.enums.NumberEnum;
import cn.structure.starter.jwt.interfaces.ICorsFilter;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.interfaces.ITokenStore;
import cn.structure.starter.jwt.properties.JwtConfig;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Security 配置
 *
 * @author chuck
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Resource
    private AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Resource
    private AccessDeniedHandler accessDeniedHandler;

    @Resource
    private ITokenService tokenService;

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private ITokenStore tokenStore;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        Map<String, List<String>> antMatchers = jwtConfig.getAntMatchers();

        logger.info("Configuring SecurityFilterChain with antMatchers: {}", antMatchers);

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    if (antMatchers != null) {
                        Set<String> keys = antMatchers.keySet();
                        for (String key : keys) {
                            if (key.equals(AuthConstant.UN_AUTHENTICATED)) {
                                List<String> urls = antMatchers.get(key);
                                logger.info("Permitting unauthenticated URLs: {}", urls);
                                for (String url : urls) {
                                    authorize.requestMatchers(url).permitAll();
                                }
                            } else {
                                String[] authUrlStr = key.split(SymbolConstant.MINUS);
                                if (authUrlStr.length < NumberEnum.TWO.getValue()) {
                                    continue;
                                }
                                String type = authUrlStr[NumberEnum.ZERO.getValue()];
                                String str = authUrlStr[NumberEnum.ONE.getValue()];
                                List<String> urls = antMatchers.get(key);
                                for (String url : urls) {
                                    if (type.equals(AuthConstant.ROLE)) {
                                        logger.info("Configuring ROLE access for {}: {}", str, url);
                                        authorize.requestMatchers(url).hasRole(str);
                                    }
                                    if (type.equals(AuthConstant.AUTH)) {
                                        logger.info("Configuring AUTHORITY access for {}: {}", str, url);
                                        authorize.requestMatchers(url).hasAuthority(str);
                                    }
                                }
                            }
                        }
                    }
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        httpSecurity.addFilterBefore(new JwtRequestFilter(tokenService, jwtConfig, tokenStore), UsernamePasswordAuthenticationFilter.class);

        Class<?> aClass = Class.forName(jwtConfig.getCorsFilterClass());
        ICorsFilter iCorsFilter = (ICorsFilter) aClass.getDeclaredConstructor().newInstance();
        httpSecurity.addFilterBefore(iCorsFilter, JwtRequestFilter.class);

        return httpSecurity.build();
    }
}

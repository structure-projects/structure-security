/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structured.security.oauth.starter.resource.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.common.constant.SymbolConstant;
import cn.structure.common.enums.NumberEnum;
import cn.structured.security.filter.UserContextFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 资源服务器配置（适配新的 Spring Security Resource Server）
 * </p>
 */
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Resource
    private OauthResourceProperties properties;
    
    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;
    
    @Resource
    private AccessDeniedHandler accessDeniedHandler;
    
    @Resource
    private JwtDecoder jwtDecoder;
    
    @Resource
    @Qualifier("structureJwtAuthenticationConverter")
    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;

    @Resource
    private UserContextFilter userContextFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                Map<String, List<String>> antMatchers = properties.getAntMatchers();
                if (antMatchers != null) {
                    Set<String> keys = antMatchers.keySet();
                    for (String key : keys) {
                        if (key.equals(AuthConstant.UN_AUTHENTICATED)) {
                            List<String> urls = antMatchers.get(key);
                            String[] urlArray = urls.toArray(new String[0]);
                            auth.requestMatchers(urlArray).permitAll();
                        } else {
                            String[] authUrlStr = key.split(SymbolConstant.MINUS);
                            if (authUrlStr.length < NumberEnum.TWO.getValue()) {
                                continue;
                            }
                            String type = authUrlStr[NumberEnum.ZERO.getValue()];
                            String roleOrAuthority = authUrlStr[NumberEnum.ONE.getValue()];
                            List<String> urls = antMatchers.get(key);
                            String[] urlArray = urls.toArray(new String[0]);
                            
                            if (type.equals(AuthConstant.ROLE)) {
                                auth.requestMatchers(urlArray).hasRole(roleOrAuthority);
                            }
                            if (type.equals(AuthConstant.AUTH)) {
                                auth.requestMatchers(urlArray).hasAuthority(roleOrAuthority);
                            }
                        }
                    }
                }
                // 默认所有请求都需要认证
                auth.anyRequest().authenticated();
            })
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter)
                )
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            // 在 OAuth2 Bearer Token 认证之后，填充 UserContext（业务数据由 RemoteUserProvider 远程获取）
            .addFilterAfter(userContextFilter, BearerTokenAuthenticationFilter.class);
        
        return http.build();
    }
}

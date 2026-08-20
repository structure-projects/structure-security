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

package cn.structure.starter.jwt.configuration;

import cn.structure.common.utils.IResultUtil;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.interfaces.ITokenStore;
import cn.structure.starter.jwt.properties.JwtConfig;
import cn.structure.starter.jwt.properties.SecurityConfig;
import cn.structure.starter.jwt.service.DefaultUserServiceImpl;
import cn.structure.starter.jwt.service.InnerTokenStore;
import cn.structure.starter.jwt.service.JwtDefaultServiceImpl;
import cn.structured.security.configuration.StructureAccessDeniedHandler;
import cn.structured.security.configuration.StructureAuthenticationEntryPoint;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;


/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/10 20:06
 */
@Configuration
@EnableConfigurationProperties({JwtConfig.class, SecurityConfig.class})
@ComponentScan(value = {"cn.structure.starter.jwt.configuration",
        "cn.structure.starter.jwt.filter"})

public class AutoJwtConfiguration {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private ITokenService tokenService;

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new StructureAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new StructureAccessDeniedHandler();
    }


    @Bean
    @ConditionalOnMissingBean(ITokenService.class)
    public ITokenService tokenService() {
        return new JwtDefaultServiceImpl(jwtConfig);
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService() {
        return new DefaultUserServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ITokenStore.class)
    public ITokenStore tokenStore() {
        return new InnerTokenStore(tokenService);
    }

    @Bean
    @ConditionalOnMissingBean(IResultUtil.class)
    public IResultUtil resultUtil() {
        return new ResultUtilSimpleImpl();
    }

}

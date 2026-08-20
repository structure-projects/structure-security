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

package cn.structure.starter.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Security 框架配置
 * </p>
 *
 * @author chuck
 * @version 1.1.0
 * @since 2026-05-24
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("structure.security")
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * 是否启用安全框架
     */
    private boolean enabled = true;

    /**
     * 默认登录页面
     */
    private String defaultLoginUrl;

    /**
     * CORS 跨域过滤器类
     */
    private String corsFilterClass = "cn.structured.security.filter.CorsFilter";

    /**
     * 访问权限配置
     */
    private Map<String, List<String>> antMatchers;

    @PostConstruct
    public void init() {
        logger.info("Security configuration initialized: enabled={}, corsFilterClass={}, defaultLoginUrl={}",
                enabled, corsFilterClass, defaultLoginUrl);
    }

}

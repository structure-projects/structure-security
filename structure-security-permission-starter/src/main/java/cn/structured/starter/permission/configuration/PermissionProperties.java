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

package cn.structured.starter.permission.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.annotation.PostConstruct;

/**
 * 权限配置属性
 *
 * <p>用于配置权限模块的行为</p>
 *
 * <p>配置示例：
 * <pre>
 * {@code
 * structure:
 *   security:
 *     permission:
 *       enabled: true
 * }
 * </pre>
 * </p>
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("structure.security.permission")
public class PermissionProperties {

    private static final Logger logger = LoggerFactory.getLogger(PermissionProperties.class);

    /**
     * 是否启用权限模块
     */
    private boolean enabled = true;

    @PostConstruct
    public void init() {
        logger.info("Permission properties initialized: enabled={}", enabled);
    }
}
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

import cn.structured.starter.permission.service.IPermissionService;
import cn.structured.starter.permission.service.PermissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 权限模块自动配置类
 *
 * <p>根据配置自动注册权限相关的 Bean：
 * <ul>
 *   <li>IPermissionService - 权限服务</li>
 * </ul>
 * </p>
 *
 * <p>自动配置条件：
 * <ul>
 *   <li>structure.security.permission.enabled=true（默认启用）</li>
 * </ul>
 * </p>
 *
 * <p>权限服务使用 IUserProvider 加载用户信息到 UserContext，
 * 然后从 UserContext 获取用户权限进行权限检查。</p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(PermissionProperties.class)
@ConditionalOnProperty(prefix = "structure.security.permission", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PermissionAutoConfiguration {

    /**
     * 创建权限服务 Bean
     *
     * @return IPermissionService 实例
     */
    @Bean("permissionService")
    @ConditionalOnMissingBean(IPermissionService.class)
    public IPermissionService permissionService() {
        log.info("Initializing PermissionService");
        return new PermissionServiceImpl();
    }
}
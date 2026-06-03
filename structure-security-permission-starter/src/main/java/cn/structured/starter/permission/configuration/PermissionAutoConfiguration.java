package cn.structured.starter.permission.configuration;

import cn.structured.security.permission.IPermissionProvider;
import cn.structured.security.permission.IPermissionService;
import cn.structured.starter.permission.provider.RemotePermissionProvider;
import cn.structured.starter.permission.provider.ContextPermissionProvider;
import cn.structured.starter.permission.service.PermissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 权限模块自动配置类
 * 
 * <p>根据配置自动注册权限相关的 Bean：
 * <ul>
 *   <li>RestTemplate - 用于远程权限获取</li>
 *   <li>IPermissionProvider - 权限提供者（根据 providerType 选择实现）</li>
 *   <li>IPermissionService - 权限服务</li>
 * </ul>
 * </p>
 * 
 * <p>自动配置条件：
 * <ul>
 *   <li>structure.security.permission.enabled=true（默认启用）</li>
 *   <li>根据 providerType 选择对应的 PermissionProvider</li>
 * </ul>
 * </p>
 * 
 * <p>权限提供者选择：
 * <ul>
 *   <li>context - 从 Spring Security 上下文获取（默认）</li>
 *   <li>remote - 从远程授权服务器获取</li>
 * </ul>
 * </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(PermissionProperties.class)
@ConditionalOnProperty(prefix = "structure.security.permission", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PermissionAutoConfiguration {

    /**
     * 创建 RestTemplate Bean（用于远程权限获取）
     * 
     * @return RestTemplate 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 创建上下文权限提供者（默认）
     * 
     * @return ContextPermissionProvider 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "structure.security.permission", name = "providerType", havingValue = "context", matchIfMissing = true)
    @ConditionalOnMissingBean(IPermissionProvider.class)
    public IPermissionProvider contextPermissionProvider() {
        log.info("Initializing ContextPermissionProvider");
        return new ContextPermissionProvider();
    }

    /**
     * 创建远程权限提供者
     * 
     * @param properties 权限配置属性
     * @param restTemplate RestTemplate 实例
     * @return RemotePermissionProvider 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "structure.security.permission", name = "providerType", havingValue = "remote")
    @ConditionalOnMissingBean(IPermissionProvider.class)
    public IPermissionProvider remotePermissionProvider(PermissionProperties properties, RestTemplate restTemplate) {
        log.info("Initializing RemotePermissionProvider with url: {}", properties.getRemoteUrl());
        return new RemotePermissionProvider(restTemplate, properties.getRemoteUrl());
    }

    /**
     * 创建权限服务 Bean
     * 
     * @param permissionProvider 权限提供者
     * @return IPermissionService 实例
     */
    @Bean("permissionService")
    @ConditionalOnMissingBean(IPermissionService.class)
    public IPermissionService permissionService(IPermissionProvider permissionProvider) {
        log.info("Initializing PermissionService");
        return new PermissionServiceImpl(permissionProvider);
    }
}
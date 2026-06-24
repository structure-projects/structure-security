package cn.structured.security.configuration;

import cn.structured.security.cache.IUserContextCache;
import cn.structured.security.cache.InMemoryUserContextCache;
import cn.structured.security.filter.UserContextFilter;
import cn.structured.security.interfaces.IUserProvider;
import cn.structured.security.provider.ContextUserProvider;
import cn.structured.security.provider.RemoteUserProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户上下文自动配置
 *
 * <p>启用用户上下文相关配置属性，根据配置选择用户信息提供者</p>
 *
 * <p>用户提供者选择逻辑：
 * <ul>
 *   <li>当 structure.security.context.remote.enabled=true 时，使用 RemoteUserProvider</li>
 *   <li>否则使用 ContextUserProvider（默认）</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableConfigurationProperties(UserContextProperties.class)
public class UserContextAutoConfiguration {

    /**
     * 创建远程用户提供者（当远程模式启用时）
     *
     * @param properties 用户上下文配置属性
     * @return RemoteUserProvider 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "structure.security.context.remote", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean(IUserProvider.class)
    public IUserProvider remoteUserProvider(UserContextProperties properties) {
        return new RemoteUserProvider(properties);
    }

    /**
     * 创建上下文用户提供者（默认）
     *
     * @return ContextUserProvider 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "structure.security.context.remote", name = "enabled", havingValue = "false", matchIfMissing = true)
    @ConditionalOnMissingBean(IUserProvider.class)
    public IUserProvider contextUserProvider() {
        return new ContextUserProvider();
    }

    @Bean
    @ConditionalOnMissingBean(UserContextFilter.class)
    public UserContextFilter userContextFilter(IUserProvider userProvider,IUserContextCache userContextCache) {
        return new UserContextFilter(userProvider, userContextCache);
    }

    @Bean
    @ConditionalOnMissingBean(IUserContextCache.class)
    public IUserContextCache userContextCache() {
        return new InMemoryUserContextCache();
    }
}
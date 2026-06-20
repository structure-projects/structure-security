package cn.structured.starter.context.configuration;

import cn.structured.starter.context.manager.DefaultContextManager;
import cn.structured.starter.context.store.DefaultUserStore;
import cn.structured.starter.context.manager.IContextManager;
import cn.structured.starter.context.store.IUserStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 上下文管理器自动配置
 *
 * <p>注册默认的用户存储和上下文管理器 Bean</p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
@Configuration
public class ContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IUserStore userStore() {
        return new DefaultUserStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public IContextManager contextManager(IUserStore userStore) {
        return new DefaultContextManager(userStore);
    }
}
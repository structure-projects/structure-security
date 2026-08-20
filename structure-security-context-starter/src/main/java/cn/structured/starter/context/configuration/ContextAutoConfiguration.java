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
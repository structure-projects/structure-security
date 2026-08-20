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

package cn.structured.starter.context.manager;

import cn.structured.starter.context.store.IUserStore;

/**
 * 默认上下文管理器
 *
 * <p>注册默认的用户存储和上下文管理器 Bean</p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
public class DefaultContextManager extends AbsContextManager {
    /**
     * 构造函数
     *
     * @param userStore 用户存储
     */
    public DefaultContextManager(IUserStore userStore) {
        super(userStore);
    }
}

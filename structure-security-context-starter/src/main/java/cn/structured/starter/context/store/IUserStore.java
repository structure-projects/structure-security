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

package cn.structured.starter.context.store;

import cn.structured.security.entity.UserContextEntity;

/**
 * 用户存储接口
 *
 * <p>定义用户信息的存储和管理操作，支持多种存储实现方式：
 * <ul>
 *   <li>DefaultUserStore - 内存存储实现</li>
 *   <li>RedisUserStore - Redis存储实现（预留扩展）</li>
 *   <li>RemoteUserStore - 远程服务存储实现（预留扩展）</li>
 * </ul>
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
public interface IUserStore {

    /**
     * 添加用户信息
     *
     * @param user 用户信息
     */
    void addUser(UserContextEntity user);

    /**
     * 根据用户ID删除用户信息
     *
     * @param userId 用户ID
     */
    void removeUser(String userId);

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息，不存在返回null
     */
    UserContextEntity getUser(String userId);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    void updateUser(UserContextEntity user);

}
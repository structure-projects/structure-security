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
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认用户存储实现（内存存储）
 *
 * <p>使用 ConcurrentHashMap 实现线程安全的内存存储</p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
@Slf4j
public class DefaultUserStore implements IUserStore {

    /**
     * 用户信息存储（userId -> UserContextEntity）
     */
    private final Map<String, UserContextEntity> userStore = new ConcurrentHashMap<>();

    @Override
    public void addUser(UserContextEntity user) {
        if (user == null || user.getUserId() == null) {
            log.warn("Cannot add user: user or userId is null");
            return;
        }
        userStore.put(user.getUserId(), user);
        if (log.isDebugEnabled()) {
            log.debug("User added: {}", user.getUserId());
        }
    }

    @Override
    public void removeUser(String userId) {
        if (userId == null) {
            log.warn("Cannot remove user: userId is null");
            return;
        }
        UserContextEntity removed = userStore.remove(userId);
        if (log.isDebugEnabled()) {
            log.debug("User removed: {}, existed: {}", userId, removed != null);
        }
    }

    @Override
    public UserContextEntity getUser(String userId) {
        if (userId == null) {
            log.warn("Cannot get user: userId is null");
            return null;
        }
        return userStore.get(userId);
    }

    @Override
    public void updateUser(UserContextEntity user) {
        if (user == null || user.getUserId() == null) {
            log.warn("Cannot update user: user or userId is null");
            return;
        }
        if (!userStore.containsKey(user.getUserId())) {
            log.warn("Cannot update user: user not found, userId={}", user.getUserId());
            return;
        }
        userStore.put(user.getUserId(), user);
        if (log.isDebugEnabled()) {
            log.debug("User updated: {}", user.getUserId());
        }
    }
}
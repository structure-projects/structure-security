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

import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.starter.context.store.IUserStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 上下文管理抽象类
 *
 * <p>用于抽象上下文管理器的功能，提供抽象方法，子类实现具体功能</p>
 */
@Slf4j
@AllArgsConstructor
public abstract class AbsContextManager implements IContextManager {

    protected final IUserStore userStore;

    @Override
    public void login(UserContextEntity user) {
        if (user == null || user.getUserId() == null) {
            log.warn("Cannot login: user or userId is null");
            return;
        }
        if (userStore != null) {
            userStore.addUser(user);
        }
        UserContext.set(user);
        if (log.isDebugEnabled()) {
            log.debug("User logged in: {}", user.getUserId());
        }
    }

    @Override
    public void logout() {
        UserContextEntity currentUser = UserContext.get();
        if (currentUser != null && currentUser.getUserId() != null) {
            if (log.isDebugEnabled()) {
                log.debug("User logged out: {}", currentUser.getUserId());
            }
            if (userStore != null) {
                userStore.removeUser(currentUser.getUserId());
            }
        }
        UserContext.remove();
    }

    @Override
    public void updateUser(UserContextEntity user) {
        if (user == null || user.getUserId() == null) {
            log.warn("Cannot update user: user or userId is null");
            return;
        }
        if (userStore != null) {
            userStore.updateUser(user);
        }
        UserContextEntity currentUser = UserContext.get();
        if (currentUser != null && user.getUserId().equals(currentUser.getUserId())) {
            UserContext.set(user);
        }
        if (log.isDebugEnabled()) {
            log.debug("User updated: {}", user.getUserId());
        }
    }

    @Override
    public UserContextEntity getUser() {
        return UserContext.get();
    }

    @Override
    public UserContextEntity getUserByUserId(Serializable userId) {
        if (userStore != null) {
            return userStore.getUser(userId.toString());
        }
        return null;
    }
}
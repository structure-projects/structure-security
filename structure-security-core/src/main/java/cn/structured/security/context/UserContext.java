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

package cn.structured.security.context;


import cn.structured.security.entity.UserContextEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息上下文
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@Slf4j
public class UserContext {

    /**
     * ThreadLocal 存储用户信息线程的上下文信息
     */
    private static final ThreadLocal<UserContextEntity> CONTEXT = new ThreadLocal<>();

    /**
     * 获取当前线程的用户信息上下文信息
     *
     * @return 用户信息上下文信息，可能为 null
     */
    public static UserContextEntity get() {
        return CONTEXT.get();
    }

    /**
     * 设置当前线程的用户信息上下文信息
     *
     * @param info 用户信息上下文信息
     */
    public static void set(UserContextEntity info) {
        if (log.isDebugEnabled()) {
            log.debug("Setting DataScope context: {}", info);
        }
        CONTEXT.set(info);
    }

    /**
     * 清除当前线程的用户信息上下文信息
     * <p>
     * 必须在请求/任务结束时调用，避免内存泄漏
     * </p>
     */
    public static void remove() {
        CONTEXT.remove();
        if (log.isDebugEnabled()) {
            log.debug("DataScope context cleared");
        }
    }

    /**
     * 获取当前部门ID
     *
     * @return 部门ID
     */
    public static String getDeptId() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getDeptId();
        }
        return null;
    }

    /**
     * 获取当前部门ID
     *
     * @return 部门ID
     */
    public static Set<String> getDeptIds() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getDeptIds();
        }
        return null;
    }

    /**
     * 获取当前部门ID
     *
     * @return 部门ID
     */
    public static Set<Long> getLoneDeptIds() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getDeptIds().stream().map(Long::parseLong).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * 获取当前部门ID
     *
     * @return 部门ID
     */
    public static Long getLongDeptId() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return Long.parseLong(userContext.getDeptId());
        }
        return null;
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getUserId();
        }
        return null;
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    public static Long getLongUserId() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return Long.parseLong(userContext.getUserId());
        }
        return null;
    }

    /**
     * 获取当前用户角色ID
     *
     * @return 用户角色ID
     */
    public static Set<String> getRoles() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getRoles();
        }
        return null;
    }

    /**
     * 获取当前用户角色ID
     *
     * @return 用户角色ID
     */
    public static Set<Long> getLongRoles() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getRoles().stream().map(Long::parseLong).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * 获取当前用户权限ID
     *
     * @return 用户权限ID
     */
    public static Set<String> getPermissions() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getPermissions();
        }
        return null;
    }

    /**
     * 获取当前用户权限ID
     *
     * @return 用户权限ID
     */
    public static Set<Long> getLongPermissions() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return userContext.getPermissions().stream().map(Long::parseLong).collect(Collectors.toSet());
        }
        return null;
    }
}

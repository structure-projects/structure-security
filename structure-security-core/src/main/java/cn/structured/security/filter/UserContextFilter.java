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

package cn.structured.security.filter;

import cn.structured.security.cache.IUserContextCache;
import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.interfaces.IUserProvider;
import cn.structured.security.util.SecurityUtils;
import jakarta.servlet.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户上下文过滤器
 * <p>
 * 请求处理完成后自动清理上下文，防止内存泄漏。
 * </p>
 *
 */
@Slf4j
@AllArgsConstructor
public class UserContextFilter implements Filter {

    private final IUserProvider userProvider;

    private final IUserContextCache userContextCache;


    @Order(100)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {

            // 获取用户 SESSION_ID / USER_ID
            // 从DataScopeProvider获取数据权限信息
            Long userId = SecurityUtils.getUserId();
            String sessionId = httpRequest.getHeader("sessionId");

            if (null != userId) {
                // 1从缓存中获取用户信息
                UserContextEntity cacheUserContext = userContextCache.get(userId.toString());

                // 对比缓存中是否有用户信息，并且验证 SESSION_ID 是否一致；不一致则刷新
                if (null == cacheUserContext || !Objects.equals(cacheUserContext.getSessionId(), sessionId)) {
                    cacheUserContext = userProvider.loadUser(userId.toString());
                    // 没有用户信息则移除缓存中的用户信息
                    if (null == cacheUserContext) {
                        userContextCache.remove(userId.toString());
                    } else {
                        // 设置用户信息到缓存中
                        userContextCache.set(userId.toString(), cacheUserContext);
                    }
                }

                // 缓存命中或重新加载后，都要设置到当前线程上下文
                if (null != cacheUserContext) {
                    UserContext.set(cacheUserContext);
                }
            }
            // 继续处理请求
            chain.doFilter(request, response);

        } finally {
            // 请求处理完成后清理上下文
            UserContext.remove();
        }
    }

    @Override
    public void destroy() {
        log.info("UserContextFilter destroyed");
    }
}

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
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;

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
        HttpRequest httpRequest = (HttpRequest) request;
        try {

            // 获取用户 SESSION_ID / USER_ID
            // 从DataScopeProvider获取数据权限信息
            Long userId = SecurityUtils.getUserId();
            HttpHeaders headers = httpRequest.headers();
            String sessionId = headers.firstValue("sessionId").orElse(null);

            if (null != userId) {
                // 1从缓存中获取用户信息
                UserContextEntity cacheUserContext = userContextCache.get(userId.toString());

                // 对比缓缓中是否有用户信息 并且验证是否过期,SESSION_ID 是否一致如果过期则刷新
                if (null == cacheUserContext || !cacheUserContext.getSessionId().equals(sessionId)) {
                    cacheUserContext = userProvider.loadUser(userId.toString());
                    // 没有用户信息则移除缓存中的用户信息
                    if (null == cacheUserContext) {
                        userContextCache.remove(userId.toString());
                    } else {
                        // 设置用户信息到缓存中
                        userContextCache.set(userId.toString(), cacheUserContext);
                        // 设置到上下文
                        UserContext.set(cacheUserContext);
                    }
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

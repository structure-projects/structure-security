package cn.structured.security.filter;

import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.interfaces.IUserProvider;
import cn.structured.security.util.SecurityUtils;
import jakarta.servlet.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 从DataScopeProvider获取数据权限信息
            Long userId = SecurityUtils.getUserId();

            if (null != userId) {
                UserContextEntity userContextEntity = userProvider.loadUser(userId.toString());

                // 设置到上下文
                UserContext.set(userContextEntity);

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

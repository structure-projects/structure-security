package cn.structure.starter.jwt.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.interfaces.ITokenStore;
import cn.structure.starter.jwt.properties.JwtConfig;
import cn.structure.starter.jwt.properties.SecurityConfig;
import cn.structured.security.entity.StructureAuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * jwt请求过滤器
 *
 * @author chuck
 */
@Slf4j
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private ITokenService iTokenService;

    private SecurityConfig securityConfig;

    private ITokenStore tokenStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 放行资源和登录
        String requestUri = request.getRequestURI();
        Map<String, List<String>> antMatchers = securityConfig.getAntMatchers();

        List<String> unAuthenticated = antMatchers.get(AuthConstant.UN_AUTHENTICATED);
        AntPathMatcher matcher = new AntPathMatcher();
        for (String uri : unAuthenticated) {
            if (matcher.match(uri, requestUri)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String token = iTokenService.getToken(request);
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        StructureAuthUser user = tokenStore.getUser(token);
        if (user == null) {
            chain.doFilter(request, response);
            return;
        }

        if (Boolean.TRUE.equals(iTokenService.isTokenExpired(token))) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // 认证成功，下个过滤器就会放行了。
        // 每个请求有效，下个请求authentication就变了
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}

package cn.structured.security.basicauth.server.filter;

import cn.structured.security.basicauth.client.BasicAuthGenerator;
import cn.structured.security.basicauth.server.interfaces.CredentialValidator;
import cn.structured.security.basicauth.server.properties.BasicAuthServerProperties;
import cn.structured.security.entity.StructureAuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Basic Auth 认证过滤器
 * <p>
 * 从请求中提取并验证 Basic Auth 认证头
 * </p>
 *
 * @author chuck
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthFilter extends OncePerRequestFilter {

    private final BasicAuthServerProperties properties;
    private final CredentialValidator credentialValidator;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
            log.debug("No Basic Auth header found, proceeding without authentication");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String[] credentials = BasicAuthGenerator.parse(authHeader);
            String username = credentials[0];
            String password = credentials[1];

            if (credentialValidator.validate(username, password)) {
                // 验证成功，设置认证信息
                StructureAuthUser authUser = new StructureAuthUser();
                authUser.setId(username);
                authUser.setUsername(username);
                authUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                authUser.setEnable(true);
                authUser.setUnlocked(true);
                authUser.setUnexpired(true);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Basic Auth authentication successful for user: {}", username);
                filterChain.doFilter(request, response);
            } else {
                log.debug("Basic Auth authentication failed for user: {}", username);
                authenticationEntryPoint.commence(request, response, null);
            }
        } catch (Exception e) {
            log.debug("Basic Auth parsing error: {}", e.getMessage());
            authenticationEntryPoint.commence(request, response, null);
        }
    }
}

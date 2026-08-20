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

package cn.structured.security.basicauth.server.filter;

import cn.structure.common.utils.BasicAuthGenerator;
import cn.structured.security.basicauth.server.interfaces.CredentialValidator;
import cn.structured.security.basicauth.server.permission.PathPermissionChecker;
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
 * 从请求中提取并验证 Basic Auth 认证头，并检查路径权限
 * </p>
 *
 * @author chuck
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthFilter extends OncePerRequestFilter {

    private final CredentialValidator credentialValidator;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final PathPermissionChecker pathPermissionChecker;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String requestPath = request.getRequestURI();

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
                if (pathPermissionChecker.isPermissionEnabled() && 
                    !pathPermissionChecker.hasPermission(username, requestPath)) {
                    log.debug("User {} denied access to path {} (insufficient permissions)", username, requestPath);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

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

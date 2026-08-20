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

package cn.structure.starter.jwt.filter;

import cn.structure.starter.jwt.interfaces.ITokenStore;
import cn.structured.security.entity.StructureAuthUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 登录处理
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/11 9:31
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private ITokenStore iTokenStore;

    public LoginFilter() {
        super(request -> "/login".equals(request.getServletPath()) && "POST".equals(request.getMethod()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken params = new UsernamePasswordAuthenticationToken(username, password);
        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        Authentication authenticate = authenticationManager.authenticate(params);

        // 认证不通过，下面的代码不会执行
        StructureAuthUser details = (StructureAuthUser) authenticate.getPrincipal();
        final String token = iTokenStore.setUser(details);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(token);
        return authenticate;
    }
}

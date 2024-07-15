package cn.structure.starter.jwt.filter;

import cn.structure.starter.jwt.interfaces.ITokenStore;
import cn.structured.security.entity.StructureAuthUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        super(new AntPathRequestMatcher("/login", "POST"));
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

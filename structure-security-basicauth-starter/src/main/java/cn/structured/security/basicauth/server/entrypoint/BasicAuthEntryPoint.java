package cn.structured.security.basicauth.server.entrypoint;

import cn.structured.security.basicauth.server.properties.BasicAuthServerProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Basic Auth 认证入口点
 * <p>
 * 在认证失败时返回 WWW-Authenticate 响应头
 * </p>
 *
 * @author chuck
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthEntryPoint implements AuthenticationEntryPoint {

    private final BasicAuthServerProperties properties;

    @Override
    public void commence(HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException) throws IOException {
        String realm = properties.getRealm();
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
        
        log.debug("Basic Auth challenge sent for realm: {}", realm);
    }
}

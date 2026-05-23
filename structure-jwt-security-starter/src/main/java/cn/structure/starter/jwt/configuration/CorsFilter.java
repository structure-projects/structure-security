package cn.structure.starter.jwt.configuration;

import cn.structure.starter.jwt.interfaces.ICorsFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @author chuck
 */
public class CorsFilter implements ICorsFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        chain.doFilter(req, res);
    }
}

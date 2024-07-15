package cn.structure.starter.jwt.configuration;

import cn.structure.common.entity.IResult;
import cn.structure.common.utils.IResultUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 鉴权失败的处理器
 *
 * @author chuck
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private IResultUtil resultUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        IResult result = resultUtil.unauthorized();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}

package cn.structure.starter.oauth.common.configuration;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.structure.common.entity.IResult;
import cn.structure.common.enums.ExceptionRsType;
import cn.structure.common.utils.IResultUtil;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * <p>
 * 认证入口点（适配新的 Spring Security Resource Server）
 * 2019/11/28 11:36
 * </p>
 *
 * @author chuck
 */
public class StructureAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private IResultUtil resultUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        Throwable cause = authException.getCause();
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(Header.CONTENT_TYPE.toString(), ContentType.JSON.toString());
        try {
            IResult result;
            if (cause instanceof InvalidBearerTokenException || 
                authException instanceof InvalidBearerTokenException) {
                // 坏的 token 或过期 token
                result = resultUtil.fail(ExceptionRsType.INVALID_AUTHENTICATION.getCode(), authException.getMessage());
            } else {
                // 用户未登录
                result = resultUtil.fail(ExceptionRsType.NOT_LOGGED_IN.getCode(), authException.getMessage());
            }
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

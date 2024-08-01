package cn.structured.security.configuration;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.structure.common.entity.IResult;
import cn.structure.common.enums.ExceptionRsType;
import cn.structure.common.utils.IResultUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * chuck
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
                         AuthenticationException authException) throws IOException {
        Throwable cause = authException.getCause();
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(Header.CONTENT_TYPE.toString(), ContentType.JSON.toString());
        IResult result = resultUtil.fail(ExceptionRsType.NOT_LOGGED_IN.getCode(), authException.getMessage());
        response.getWriter().write(JSON.toJSONString(result));
    }
}

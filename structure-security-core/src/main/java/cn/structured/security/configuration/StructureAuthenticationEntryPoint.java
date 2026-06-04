package cn.structured.security.configuration;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.structure.common.entity.IResult;
import cn.structure.common.enums.ExceptionRsType;
import cn.structure.common.utils.IResultUtil;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * <p>
 * 安全认证入口点
 * 处理用户未登录等基础认证异常
 * </p>
 *
 * @author chuck
 * @since 2019/11/28 11:36
 */
@Slf4j
public class StructureAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private IResultUtil resultUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(Header.CONTENT_TYPE.toString(), ContentType.JSON.toString());
        try {
            IResult result = resultUtil.fail(ExceptionRsType.NOT_LOGGED_IN.getCode(), authException.getMessage());
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error("认证异常处理失败 -> {}", e.getMessage());
        }
    }
}
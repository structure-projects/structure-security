package cn.structured.security.configuration;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.structure.common.entity.IResult;
import cn.structure.common.enums.ExceptionRsType;
import cn.structure.common.utils.IResultUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

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
public class StructureAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    private IResultUtil resultUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(Header.CONTENT_TYPE.toString(), ContentType.JSON.toString());
        //无权限
        IResult result = resultUtil.fail(ExceptionRsType.PERMISSION_DENIED.getCode(), accessDeniedException.getMessage());
        response.getWriter().write(JSON.toJSONString(result));
    }
}

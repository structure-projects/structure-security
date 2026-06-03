package cn.structured.security.oauth.starter.resource.configuration;

import cn.structure.common.entity.IResult;
import cn.structure.common.enums.ExceptionRsType;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 自定义权限处理器
 * 2019/11/28 11:36
 * </p>
 *
 * @author chuck
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        ResultUtilSimpleImpl resultUtilSimple = new ResultUtilSimpleImpl();
        try {
            //无权限
            IResult result = resultUtilSimple.fail(ExceptionRsType.PERMISSION_DENIED.getCode(), accessDeniedException.getMessage());
            response.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

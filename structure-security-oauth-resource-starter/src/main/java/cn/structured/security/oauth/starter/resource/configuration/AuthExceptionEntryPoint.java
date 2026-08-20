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

package cn.structured.security.oauth.starter.resource.configuration;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.structure.common.entity.IResult;
import cn.structure.common.enums.ExceptionRsType;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * chuck
 * 2019/11/28 11:36
 * </p>
 *
 * @author chuck
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        Throwable cause = authException.getCause();
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(Header.CONTENT_TYPE.toString(), ContentType.JSON.toString());
        ResultUtilSimpleImpl resultUtilSimple = new ResultUtilSimpleImpl();
        try {
            if (cause instanceof InvalidBearerTokenException || 
                authException instanceof InvalidBearerTokenException) {
                //坏的token或过期token
                IResult result = resultUtilSimple.fail(ExceptionRsType.INVALID_AUTHENTICATION.getCode(), authException.getMessage());
                response.getWriter().write(JSONObject.toJSONString(result));
            } else {
                //用户未登录
                IResult result = resultUtilSimple.fail(ExceptionRsType.NOT_LOGGED_IN.getCode(), authException.getMessage());
                response.getWriter().write(JSONObject.toJSONString(result));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

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

package cn.structured.security.common.dto.login;

import cn.structured.security.common.dto.LoginResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户名密码登录响应
 * 继承统一登录响应DTO，保持向后兼容性
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends LoginResultDTO {

    /**
     * 从LoginResultDTO创建LoginResponse
     */
    public static LoginResponse fromLoginResult(LoginResultDTO result) {
        LoginResponse response = new LoginResponse();
        response.setUserId(result.getUserId());
        response.setUsername(result.getUsername());
        response.setPhone(result.getPhone());
        response.setEmail(result.getEmail());
        response.setAccessToken(result.getAccessToken());
        response.setRefreshToken(result.getRefreshToken());
        response.setTokenType(result.getTokenType());
        response.setExpiresIn(result.getExpiresIn());
        response.setScope(result.getScope());
        response.setLoginType(result.getLoginType());
        return response;
    }
}
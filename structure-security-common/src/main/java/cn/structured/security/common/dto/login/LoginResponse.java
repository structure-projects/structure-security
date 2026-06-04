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
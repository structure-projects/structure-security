package cn.structured.oauth.api.dto.social;

import cn.structured.oauth.api.dto.LoginResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社交登录响应
 * 继承统一登录响应DTO，保持向后兼容性
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SocialLoginResponse extends LoginResultDTO {

    /**
     * 从LoginResultDTO创建SocialLoginResponse
     */
    public static SocialLoginResponse fromLoginResult(LoginResultDTO result) {
        SocialLoginResponse response = new SocialLoginResponse();
        response.setUserId(result.getUserId());
        response.setUsername(result.getUsername());
        response.setPhone(result.getPhone());
        response.setEmail(result.getEmail());
        response.setAccessToken(result.getAccessToken());
        response.setRefreshToken(result.getRefreshToken());
        response.setTokenType(result.getTokenType());
        response.setExpiresIn(result.getExpiresIn());
        response.setScope(result.getScope());
        response.setPlatformUserId(result.getPlatformUserId());
        response.setPlatformCode(result.getPlatformCode());
        response.setIsNewUser(result.getIsNewUser());
        response.setLoginType(result.getLoginType());
        return response;
    }
}
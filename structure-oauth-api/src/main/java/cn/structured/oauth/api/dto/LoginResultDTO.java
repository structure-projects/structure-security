package cn.structured.oauth.api.dto;

import lombok.Data;

/**
 * 统一登录响应DTO
 * 整合所有登录方式的返回字段：用户名密码登录、手机号登录、邮箱登录、社交登录
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class LoginResultDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型（默认Bearer）
     */
    private String tokenType = "Bearer";

    /**
     * 令牌过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 第三方平台用户ID（社交登录时使用）
     */
    private String platformUserId;

    /**
     * 平台编码（社交登录时使用）
     */
    private String platformCode;

    /**
     * 是否新用户（社交登录自动注册时使用）
     */
    private Boolean isNewUser;

    /**
     * 登录类型：password, phone, email, social
     */
    private String loginType;

    /**
     * 从Oauth2TokenDTO创建LoginResultDTO
     */
    public static LoginResultDTO fromOauth2Token(Oauth2TokenDTO token) {
        LoginResultDTO result = new LoginResultDTO();
        result.setAccessToken(token.getAccessToken());
        result.setRefreshToken(token.getRefreshToken());
        result.setTokenType(token.getTokenType());
        if (token.getExpires() != null) {
            result.setExpiresIn(token.getExpires().longValue());
        }
        return result;
    }
}
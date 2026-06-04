package cn.structured.security.common.dto;

import lombok.Data;

/**
 * @author chuck
 * @version 2024/07/22 下午8:22
 * @since 1.8
 */
@Data
public class Oauth2TokenDTO {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌类型
     */
    private String tokenType;
    /**
     * 有效时间（秒）
     */
    private Integer expires;
}

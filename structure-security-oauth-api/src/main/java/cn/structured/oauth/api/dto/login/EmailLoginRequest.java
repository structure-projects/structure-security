package cn.structured.oauth.api.dto.login;

import lombok.Data;

/**
 * 邮箱验证码登录请求
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class EmailLoginRequest {

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;
}
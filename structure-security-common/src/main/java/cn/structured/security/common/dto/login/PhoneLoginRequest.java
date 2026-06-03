package cn.structured.security.common.dto.login;

import lombok.Data;

/**
 * 手机号验证码登录请求
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class PhoneLoginRequest {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
}
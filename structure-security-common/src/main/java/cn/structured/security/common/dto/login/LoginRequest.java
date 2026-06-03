package cn.structured.security.common.dto.login;

import lombok.Data;

/**
 * 登录请求
 * 支持用户名、手机号、邮箱三种登录方式
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     * 用于用户名登录
     */
    private String username;

    /**
     * 手机号
     * 用于手机号登录
     */
    private String phone;

    /**
     * 邮箱
     * 用于邮箱登录
     */
    private String email;

    /**
     * 密码
     * 登录凭证，必填
     */
    private String password;

    /**
     * 图形验证码
     * 可选，用于防暴力破解
     */
    private String code;

    /**
     * 图形验证码key
     * 获取验证码时返回的唯一标识
     */
    private String key;

    /**
     * 登录类型
     * 可选值：USERNAME, PHONE, EMAIL
     * 不传则自动根据传入参数识别
     */
    private String loginType;
}
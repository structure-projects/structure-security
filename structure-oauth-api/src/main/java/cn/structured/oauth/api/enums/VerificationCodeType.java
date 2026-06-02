package cn.structured.oauth.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码业务类型
 *
 * @author chuck
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum VerificationCodeType {

    LOGIN("login", "用户登录验证", "您正在进行用户登录操作，当前操作的验证码为%s,验证码在5分钟内有效！"),
    REGISTER("register", "用户注册验证", "您正在进行用户注册操作，当前操作的验证码为%s,验证码在5分钟内有效！"),
    RESET_PASSWORD("resetPassword", "用户重置密码验证", "您正在进行密码重置操作，当前操作的验证码为%s,验证码在5分钟内有效！");

    private String code;

    private String name;

    private String text;
}

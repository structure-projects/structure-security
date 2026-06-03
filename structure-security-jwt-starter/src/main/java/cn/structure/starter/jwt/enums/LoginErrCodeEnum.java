package cn.structure.starter.jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录异常枚举类
 *
 * @author cqliut
 * @version 2023.0707
 * @since 1.0.1
 */
@Getter
@AllArgsConstructor
public enum LoginErrCodeEnum {

    USER_PASSWORD_ERR("", "用户名密码错误！"),
    USER_DISABLED("", "用户失效！"),
    USER_LOCKED_ERR("", "用户锁定！");
    private String code;

    private String msg;

}

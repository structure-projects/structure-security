package cn.structured.security.common.dto.user;

import lombok.Data;

/**
 * 注册用户DTO
 *
 * @author chuck
 * @since 2024/7/17
 */
@Data
public class RegeditUserDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
}
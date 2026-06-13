package cn.structured.security.common.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 重置密码
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class UserRestPasswordDTO {

    /**
     * 通过手机号验证
     */
    @NotBlank
    private String phone;

    /**
     * 验证码
     */
    @NotBlank
    private String code;

    /**
     * 密码
     */
    @NotBlank
    private String password;

}

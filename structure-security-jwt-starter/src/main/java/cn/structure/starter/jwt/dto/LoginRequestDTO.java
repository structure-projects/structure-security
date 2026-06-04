package cn.structure.starter.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用于默认登录的DTO
 *
 * @author chuck
 */
@Getter
@Setter
@ToString
@Schema(description = "登录的DTO")
public class LoginRequestDTO {

    @Schema(description = "用户名", example = "tom")
    private String username;
    @Schema(description = "密码", example = "123456")
    private String password;
}

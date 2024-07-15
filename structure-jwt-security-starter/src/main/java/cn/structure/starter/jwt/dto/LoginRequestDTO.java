package cn.structure.starter.jwt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "登录的DTO")
public class LoginRequestDTO {

    @ApiModelProperty(value = "用户名", example = "tom")
    private String username;
    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
}

package cn.structured.oauth.api.dto.login;

import cn.structured.oauth.api.enums.VerificationCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 发送邮箱验证码
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@Schema(description = "发送邮箱验证码-DTO")
public class SendEmailCodeDTO {

    /**
     * 验证码类型
     */
    @NotNull
    @Schema(description = "验证码类型")
    private VerificationCodeType codeType;

    /**
     * 邮箱地址
     */
    @NotBlank
    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "验证码")
    private String code;

}

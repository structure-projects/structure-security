package cn.structured.oauth.api.dto.login;

import cn.structured.oauth.api.enums.VerificationCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 发送短信验证码
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@Schema(description = "发送短信验证码-DTO")
public class SendSmsCodeDTO {

    /**
     * 验证码类型
     */
    @NotNull
    @Schema(description = "验证码类型")
    private VerificationCodeType codeType;

    /**
     * 手机号
     */
    @NotBlank
    @Schema(description = "手机号")
    private String phone;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;

}

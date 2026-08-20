/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structured.security.common.dto.login;

import cn.structured.security.common.enums.VerificationCodeType;
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

package cn.structured.security.common.dto.captcha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码响应DTO
 * 用于返回验证码信息
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaResponseDTO {

    /**
     * 验证码唯一标识key
     */
    private String key;

    /**
     * 验证码图片（Base64编码）
     */
    private String image;
}
package cn.structured.security.common.dto.social;

import lombok.Data;

/**
 * 社交登录请求
 * 支持多种社交平台：微信、钉钉等
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class SocialLoginRequest {

    /**
     * 平台编码
     * 如：weChat、dingTalk
     */
    private String platformCode;

    /**
     * 授权码
     * 第三方平台返回的授权码
     */
    private String authCode;

    /**
     * 扩展参数
     * 可选的JSON字符串，用于传递额外参数
     */
    private String extraParams;
}
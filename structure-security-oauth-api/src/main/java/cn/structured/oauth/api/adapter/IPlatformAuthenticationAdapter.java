package cn.structured.oauth.api.adapter;

import cn.structured.oauth.api.dto.social.SocialUserInfo;

/**
 * 平台认证适配器
 * 定义第三方平台认证的标准接口
 *
 * @author chuck
 * @since JDK1.8
 */
public interface IPlatformAuthenticationAdapter {

    /**
     * 认证授权码
     * 使用授权码换取平台用户ID
     *
     * @param code 授权码
     * @return 平台用户ID
     */
    String authentication(String code);

    /**
     * 获取平台用户信息
     *
     * @param platformUserId 平台用户ID
     * @return 平台用户信息
     */
    SocialUserInfo getUserInfo(String platformUserId);

    /**
     * 获取平台编码
     *
     * @return 平台唯一编码
     */
    String getPlatformCode();
}
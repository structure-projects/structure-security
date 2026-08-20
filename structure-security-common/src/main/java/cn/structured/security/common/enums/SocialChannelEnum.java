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

package cn.structured.security.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 社交渠道枚举
 * 支持的第三方登录渠道
 *
 * @author chuck
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum SocialChannelEnum {

    // ============ 国内常用渠道 ============
    WE_CHAT("weChat", "微信"),
    WE_CHAT_MINI_APP("weChatMiniApp", "微信小程序"),
    DING_TALK("dingTalk", "钉钉"),
    DING_TALK_MINI_APP("dingTalkMiniApp", "钉钉小程序"),
    WE_COM("weCom", "企业微信"),
    FEI_SHU("feiShu", "飞书"),
    FEI_SHU_MINI_APP("feiShuMiniApp", "飞书小程序"),
    QQ("qq", "QQ"),
    QQ_MINI_APP("qqMiniApp", "QQ小程序"),
    WEIBO("weiBo", "微博"),

    // ============ 国际常用渠道 ============
    GITHUB("github", "GitHub"),
    GOOGLE("google", "Google"),
    FACEBOOK("facebook", "Facebook"),
    TWITTER("twitter", "Twitter"),
    LINKEDIN("linkedin", "LinkedIn"),

    // ============ 其他渠道 ============
    PHONE("phone", "手机号"),
    EMAIL("email", "邮箱");

    /** 渠道编码 */
    private final String code;

    /** 渠道名称 */
    private final String name;

    /**
     * 根据编码获取枚举
     */
    public static SocialChannelEnum getByCode(String code) {
        for (SocialChannelEnum channel : values()) {
            if (channel.getCode().equals(code)) {
                return channel;
            }
        }
        return null;
    }
}
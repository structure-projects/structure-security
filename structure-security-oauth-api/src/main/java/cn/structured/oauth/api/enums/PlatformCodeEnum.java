package cn.structured.oauth.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台编码常量
 *
 * @author chuck
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum PlatformCodeEnum {

    PHONE("phone"),
    EMAIL("email"),
    DING_TALK("dingTalk"),
    WE_CHAT("weChat");

    private String code;

}

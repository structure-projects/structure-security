package cn.structured.security.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginInfoEntity {

    /**
     * 登录时间
     */
    protected LocalDateTime loginTime;

    /**
     * 登录IP
     */
    protected String loginIp;

    /**
     * 登录地点
     */
    protected String loginLocation;

    /**
     * 登录设备
     */
    protected String loginDevice;

    /**
     * 登录浏览器
     */
    protected String loginBrowser;
    /**
     * 登录操作系统
     */
    protected String loginOs;
    /**
     * 登录语言
     */
    protected String loginLanguage;
}

package cn.structured.security.common.bo;

import lombok.Data;

/**
 * 消息BO
 * 用于发送各类消息通知
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class MessageBO {

    /**
     * 消息类型：sms（短信）、email（邮件）、push（推送）
     */
    private String type;

    /**
     * 接收人（手机号/邮箱/设备ID）
     */
    private String receiver;

    /**
     * 消息模板ID
     */
    private String templateId;

    /**
     * 消息内容（JSON格式的模板参数）
     */
    private String content;

    /**
     * 消息主题（邮件/推送使用）
     */
    private String subject;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID
     */
    private String businessId;
}
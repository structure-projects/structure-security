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
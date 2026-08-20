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
 * 审计BO
 *
 * @author chuck
 * @since 2024/7/17
 */
@Data
public class AuditBO {

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 用户名
     */
    private String username;

    /**
     * IP
     */
    private String ip;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 状态
     */
    private String status;

}

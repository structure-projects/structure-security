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

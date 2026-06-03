package cn.structured.oauth.api.bo.risk;

import lombok.Data;

/**
 * 发送风控BO
 *
 * @author chuck
 * @since 2024/7/17
 */
@Data
public class SendRiskBO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 风险事件
     */
    private String event;

    /**
     * 风险事件信息
     */
    private String eventMessage;

}

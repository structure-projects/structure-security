package cn.structured.oauth.api.bo.risk;

import lombok.Data;

import java.util.List;

/**
 * 风险检测BO
 *
 * @author chuck
 * @since 2024/7/17
 */
@Data
public class RiskResultBO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 风险结果
     */
    private String riskResult;

    /**
     * 风险评分
     */
    private String riskScore;

    /**
     * 风险内容集合
     */
    private List<RiskResultItem> resultItems;



}

package cn.structured.security.common.bo.risk;

import lombok.Data;

/**
 * 风险检测BO
 *
 * @author chuck
 * @since 2024/7/17
 */
@Data
public class RiskResultItem {

    /**
     * 风险标签
     */
    private String riskTag;

    /**
     * 风险类型
     */
    private String riskType;

    /**
     * 风险等级
     */
    private Integer riskLevel;

    /**
     * 风险描述
     */
    private String riskDesc;

    /**
     * 风险评分
     */
    private String riskScore;
}

package cn.structured.security.common.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * JWT策略配置DTO
 *
 * @author chuck
 * @since 1.0.4
 */
@Data
@Schema(description = "JWT策略配置 - DTO")
public class JwtStrategyConfigDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "客户端ID")
    private String clientId;

    @Schema(description = "策略类型: AUTHORIZATION, CUSTOMIZATION")
    private String strategyType;

    @Schema(description = "策略名称")
    private String strategyName;

    @Schema(description = "策略配置JSON")
    private String configJson;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "优先级(数字越大优先级越高)")
    private Integer priority;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新人")
    private String updatedBy;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
}

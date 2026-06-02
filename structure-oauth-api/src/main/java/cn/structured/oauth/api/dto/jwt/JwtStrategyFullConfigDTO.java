package cn.structured.oauth.api.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * JWT完整策略配置DTO（包含所有Claims）
 *
 * @author chuck
 * @since 1.0.4
 */
@Data
@Schema(description = "JWT完整策略配置（含Claims） - DTO")
public class JwtStrategyFullConfigDTO {

    @Schema(description = "策略配置")
    private JwtStrategyConfigDTO strategy;

    @Schema(description = "Claim配置列表")
    private List<JwtClaimConfigDTO> claims;
}

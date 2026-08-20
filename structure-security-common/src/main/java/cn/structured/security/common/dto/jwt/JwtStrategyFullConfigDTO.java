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

package cn.structured.security.common.dto.jwt;

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

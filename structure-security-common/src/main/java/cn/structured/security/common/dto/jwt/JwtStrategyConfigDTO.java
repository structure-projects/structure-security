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

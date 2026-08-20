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

package cn.structured.security.common.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户端DTO
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@Schema(description = "客户端 - DTO")
public class ClientDTO {

    @Schema(description = "客户端ID")
    private String clientId;

    @Schema(description = "资源ID集合")
    private List<String> resourceIds;

    @Schema(description = "客户端密匙")
    private String clientSecret;

    @Schema(description = "客户端申请的权限范围")
    private String scope;

    @Schema(description = "客户端支持的grant_type")
    private List<String> authorizedGrantTypes;

    @Schema(description = "客户端所拥有的Spring Security的权限值")
    private List<String> authorities;

    @Schema(description = "访问令牌有效时间值")
    private Integer accessTokenValidity;

    @Schema(description = "更新令牌有效时间值")
    private Integer refreshTokenValidity;

    @Schema(description = "附加信息")
    private String additionalInformation;

    @Schema(description = "用户是否自动Approval操作")
    private Boolean autoApprove;

    @Schema(description = "web重定向地址")
    private String webServerRedirectUri;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

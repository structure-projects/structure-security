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

package cn.structured.security.common.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户社交绑定DTO
 * 用于返回用户与社交平台的绑定信息
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSocialBindDTO {

    private Long id;

    private String platformCode;

    private String platformUserId;

    private String nickname;

    private String avatar;

    private LocalDateTime bindTime;

    private Boolean enabled;
}
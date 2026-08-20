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

package cn.structured.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserContextEntity {

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 过期时间
     */
    private Long expiresIn;

    /**
     * 用户ID
     */
    protected String userId;

    /**
     * 用户名
     */
    protected String username;

    /**
     * 昵称
     */
    protected String nickname;

    /**
     * 头像
     */
    protected String avatar;

    /**
     * 邮箱
     */
    protected String email;

    /**
     * 手机号
     */
    protected String mobile;

    /**
     * 部门ID
     */
    protected String deptId;

    /**
     * 租户ID
     */
    protected String tenantId;

    /**
     * 数据部门ID
     */
    protected Set<String> deptIds;

    /**
     * 角色ID
     */
    protected Set<String> roles;


    /**
     * 权限ID
     */
    protected Set<String> permissions;

    /**
     * 登录时间
     */
    protected LocalDateTime loginTime;


    /**
     * 列级字段可见性配置
     * <p>
     * key: 资源名称（如 "order"）
     * value: 该资源下隐藏的字段列表
     * </p>
     */
    private Map<String, List<String>> hiddenFields = new HashMap<>();

}

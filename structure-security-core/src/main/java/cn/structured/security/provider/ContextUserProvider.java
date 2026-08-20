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

package cn.structured.security.provider;

import cn.structured.security.context.UserContext;
import cn.structured.security.entity.StructureAuthUser;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.interfaces.IUserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认用户信息提供者（仅从 SecurityContext 提取基础认证信息）
 *
 * <p>从 Spring Security 上下文获取用户信息并设置到 UserContext。
 * 这是 JWT/Token 中能直接提供的信息的最小集合。</p>
 *
 * <p><b>填充字段：</b>userId、permissions、loginTime</p>
 * <p><b>不填充的字段（保持 null）：</b>deptId、tenantId、deptIds、roles</p>
 *
 * <p>需要这些业务字段时，请启用 {@link RemoteUserProvider}（配置
 * {@code structure.security.context.remote.enabled=true}），
 * 它会通过远程 API 获取完整的用户业务信息。</p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
@Slf4j
public class ContextUserProvider implements IUserProvider {

    @Override
    public UserContextEntity loadUser(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("User not authenticated in SecurityContext");
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof StructureAuthUser authUser) {
            UserContextEntity user = convertToUserContextEntity(authUser);
            UserContext.set(user);
            log.debug("User loaded from SecurityContext: {}", userId);
            return user;
        }

        log.debug("Principal is not StructureAuthUser type");
        return null;
    }

    private UserContextEntity convertToUserContextEntity(StructureAuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        Set<String> permissions = (authorities != null ? authorities.stream() : java.util.stream.Stream.<GrantedAuthority>empty())
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return UserContextEntity.builder()
                .userId(authUser.getId() != null ? authUser.getId().toString() : null)
                .permissions(permissions)
                .loginTime(LocalDateTime.now())
                .build();
    }
}
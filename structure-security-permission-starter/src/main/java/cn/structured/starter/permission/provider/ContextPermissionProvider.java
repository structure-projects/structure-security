package cn.structured.starter.permission.provider;

import cn.structured.security.entity.StructureAuthUser;
import cn.structured.security.permission.IPermissionProvider;
import cn.structured.security.permission.UserPerm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 上下文权限提供者
 * 
 * <p>从 Spring Security 上下文获取当前认证用户的权限信息</p>
 * 
 * <p>工作原理：
 * <ol>
 *   <li>从 SecurityContextHolder 获取当前 Authentication 对象</li>
 *   <li>获取 Principal（必须是 StructureAuthUser 类型）</li>
 *   <li>从 StructureAuthUser 的 getAuthorities() 方法获取权限列表</li>
 *   <li>将每个权限字符串解析为 UserPerm 对象</li>
 * </ol>
 * </p>
 * 
 * <p>这是默认的权限提供者实现，适用于已完成认证的请求</p>
 */
@Slf4j
public class ContextPermissionProvider implements IPermissionProvider {

    @Override
    public Set<UserPerm> getPermissions(String userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return Collections.emptySet();
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof StructureAuthUser)) {
                return Collections.emptySet();
            }

            StructureAuthUser user = (StructureAuthUser) principal;
            Set<UserPerm> permissions = new HashSet<>();
            
            for (GrantedAuthority authority : user.getAuthorities()) {
                UserPerm perm = UserPerm.parse(authority.getAuthority());
                if (perm != null) {
                    permissions.add(perm);
                }
            }
            
            return permissions;
        } catch (Exception e) {
            log.warn("Failed to get permissions from security context: {}", e.getMessage());
            return Collections.emptySet();
        }
    }
}
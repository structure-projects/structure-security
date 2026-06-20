package cn.structured.starter.permission.service;

import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.util.PermissionMatcher;
import cn.structured.security.entity.UserPerm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 *
 * <p>提供基于通配符的权限检查功能</p>
 *
 * <p>工作流程：
 * <ol>
 *   <li>通过 IUserProvider 加载用户信息到 UserContext</li>
 *   <li>从 UserContext 获取用户权限集合</li>
 *   <li>使用 PermissionMatcher 进行权限匹配</li>
 *   <li>返回匹配结果</li>
 * </ol>
 * </p>
 *
 * <p>支持的权限格式：
 * <ul>
 *   <li>order:create - 精确权限</li>
 *   <li>order:* - 资源级通配</li>
 *   <li>*:read - 动作级通配</li>
 *   <li>system:order:create - 三层权限</li>
 *   <li>*:*:* - 超级权限</li>
 * </ul>
 * </p>
 */
@Slf4j
public class PermissionServiceImpl implements IPermissionService {

    @Override
    public boolean hasPermission(String permission) {
        Set<UserPerm> perms = getUserPermissions();
        return PermissionMatcher.hasPerm(perms, permission);
    }

    @Override
    public Set<UserPerm> getUserPermissions() {
        try {
            UserContextEntity user = UserContext.get();
            if (user == null) {
                return Collections.emptySet();
            }

            Set<String> permissions = user.getPermissions();
            if (permissions == null || permissions.isEmpty()) {
                return Collections.emptySet();
            }

            return permissions.stream()
                    .map(UserPerm::parse)
                    .filter(p -> p != null)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.warn("Failed to get user permissions: {}", e.getMessage());
            return Collections.emptySet();
        }
    }
}
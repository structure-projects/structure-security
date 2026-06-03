package cn.structured.starter.permission.service;

import cn.structured.security.permission.IPermissionService;
import cn.structured.security.permission.PermissionMatcher;
import cn.structured.security.permission.UserPerm;
import cn.structured.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

/**
 * 权限服务实现
 * 
 * <p>提供基于通配符的权限检查功能</p>
 * 
 * <p>工作流程：
 * <ol>
 *   <li>通过 PermissionProvider 获取当前用户的权限集合</li>
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

    private final cn.structured.security.permission.IPermissionProvider permissionProvider;

    /**
     * 创建权限服务实现
     * 
     * @param permissionProvider 权限提供者
     */
    public PermissionServiceImpl(cn.structured.security.permission.IPermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
    }

    @Override
    public boolean hasPermission(String permission) {
        Set<UserPerm> perms = getUserPermissions();
        return PermissionMatcher.hasPerm(perms, permission);
    }

    @Override
    public Set<UserPerm> getUserPermissions() {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId == null) {
                return Collections.emptySet();
            }
            return permissionProvider.getPermissions(String.valueOf(userId));
        } catch (Exception e) {
            log.warn("Failed to get user permissions: {}", e.getMessage());
            return Collections.emptySet();
        }
    }
}
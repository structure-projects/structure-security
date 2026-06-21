package cn.structured.starter.permission.service;

import cn.structured.security.entity.UserPerm;import java.util.Set;

/**
 * 权限服务接口
 * 
 * <p>提供权限检查功能，支持通配符权限匹配</p>
 * 
 * <p>使用示例：
 * <pre>
 * {@code
 * @Autowired
 * private IPermissionService permissionService;
 * 
 * if (permissionService.hasPermission("order:create")) {
 *     // 执行需要权限的操作
 * }
 * }
 * </pre>
 * </p>
 */
public interface IPermissionService {

    /**
     * 检查是否具有指定权限
     * 
     * @param permission 权限字符串（如 "order:create", "system:order:read"）
     * @return true 表示有权限，false 表示无权限
     */
    boolean hasPermission(String permission);

    /**
     * 获取当前用户的权限集合
     * 
     * @return 用户权限集合
     */
    Set<UserPerm> getUserPermissions();
}
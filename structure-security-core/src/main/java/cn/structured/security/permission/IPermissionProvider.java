package cn.structured.security.permission;

import java.util.Set;

/**
 * 权限提供者接口
 * 
 * <p>定义从不同来源获取用户权限的接口，支持多种实现方式：
 * <ul>
 *   <li>ContextPermissionProvider - 从 Spring Security 上下文获取</li>
 *   <li>HeaderPermissionProvider - 从请求头获取</li>
 *   <li>RemotePermissionProvider - 从远程授权服务器获取</li>
 * </ul>
 * </p>
 */
public interface IPermissionProvider {

    /**
     * 获取用户权限集合
     * 
     * @param userId 用户ID
     * @return 用户权限集合
     */
    Set<UserPerm> getPermissions(String userId);
}
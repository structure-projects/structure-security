package cn.structured.starter.context.manager;

import cn.structured.security.entity.UserContextEntity;

import java.io.Serializable;

/**
 * 上下文管理器接口
 *
 * <p>用于管理上下文信息，如用户信息、权限信息、审计信息等</p>
 *
 * <p>使用示例：
 * <pre>
 * {@code
 * public void doSomething() {
 *     // 获取当前用户信息
 *     UserContextEntity user = UserContext.get();
 *     // 获取当前用户权限
 *     Set<UserPerm> perms = PermissionService.getUserPermissions();
 * }
 * }
 * </pre>
 * </p>
 */
public interface IContextManager {

    /**
     * 登录
     *
     * @param user 用户信息
     */
    void login(UserContextEntity user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    void updateUser(UserContextEntity user);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    UserContextEntity getUser();

    /**
     * 获取用户信息（通过 userId 获取）
     *
     * @return 当前用户信息
     */
    UserContextEntity getUserByUserId(Serializable userId);
}
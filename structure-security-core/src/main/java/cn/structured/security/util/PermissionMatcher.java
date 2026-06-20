package cn.structured.security.util;

import cn.structured.security.entity.UserPerm;

import java.util.Set;

/**
 * 权限匹配器
 * 
 * <p>实现权限通配符匹配算法，支持多层级权限匹配</p>
 * 
 * <p>支持的通配符规则：
 * <table>
 *   <tr><th>权限写法</th><th>含义</th><th>示例</th></tr>
 *   <tr><td>order:create</td><td>精确匹配</td><td>仅匹配 order:create</td></tr>
 *   <tr><td>order:*</td><td>资源级通配</td><td>匹配 order:create, order:delete 等</td></tr>
 *   <tr><td>*:read</td><td>动作级通配</td><td>匹配 order:read, user:read 等</td></tr>
 *   <tr><td>*:*</td><td>超级权限</td><td>匹配所有二层权限</td></tr>
 *   <tr><td>system:order:create</td><td>三层精确匹配</td><td>仅匹配 system:order:create</td></tr>
 *   <tr><td>system:order:*</td><td>三层资源级通配</td><td>匹配 system:order:create 等</td></tr>
 *   <tr><td>system:*:read</td><td>三层中间通配</td><td>匹配 system:order:read, system:user:read 等</td></tr>
 *   <tr><td>*:*:*</td><td>三层超级权限</td><td>匹配所有三层权限</td></tr>
 * </table>
 * </p>
 * 
 * <p>匹配规则：
 * <ul>
 *   <li>层级必须相同才能匹配（如 order:* 不匹配 system:order:create）</li>
 *   <li>每个层级支持通配符 * 匹配任意值</li>
 *   <li>不是字符串包含匹配，而是结构匹配</li>
 * </ul>
 * </p>
 */
public final class PermissionMatcher {

    private PermissionMatcher() {
        // 工具类，禁止实例化
    }

    /**
     * 检查用户权限集合是否包含所需权限
     * 
     * @param perms 用户权限集合
     * @param required 所需权限字符串
     * @return true 表示有权限，false 表示无权限
     */
    public static boolean hasPerm(Set<UserPerm> perms, String required) {
        if (perms == null || perms.isEmpty()) {
            return false;
        }
        UserPerm requiredPerm = UserPerm.parse(required);
        if (requiredPerm == null) {
            return false;
        }
        for (UserPerm perm : perms) {
            if (match(perm, requiredPerm)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查用户权限集合是否包含所需权限（字符串参数版本）
     * 
     * @param perms 用户权限集合
     * @param required 所需权限字符串
     * @return true 表示有权限，false 表示无权限
     */
    public static boolean hasPerm(Set<UserPerm> perms, UserPerm required) {
        if (perms == null || perms.isEmpty()) {
            return false;
        }
        if (required == null) {
            return false;
        }
        for (UserPerm perm : perms) {
            if (match(perm, required)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查用户权限是否匹配所需权限
     * 
     * @param userPerm 用户权限
     * @param required 所需权限字符串
     * @return true 表示匹配，false 表示不匹配
     */
    public static boolean match(UserPerm userPerm, String required) {
        UserPerm requiredPerm = UserPerm.parse(required);
        return match(userPerm, requiredPerm);
    }

    /**
     * 检查用户权限是否匹配所需权限
     * 
     * @param userPerm 用户权限
     * @param requiredPerm 所需权限
     * @return true 表示匹配，false 表示不匹配
     */
    public static boolean match(UserPerm userPerm, UserPerm requiredPerm) {
        if (userPerm == null || requiredPerm == null) {
            return false;
        }
        
        int userLevel = userPerm.getLevel();
        int requiredLevel = requiredPerm.getLevel();
        
        if (userLevel != requiredLevel) {
            return false;
        }
        
        for (int i = 0; i < userLevel; i++) {
            String userPart = userPerm.getPart(i);
            String requiredPart = requiredPerm.getPart(i);
            
            if (!matchPart(userPart, requiredPart)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 匹配单个权限部分
     * 
     * @param userPart 用户权限的某一部分
     * @param requiredPart 所需权限的对应部分
     * @return true 表示匹配，false 表示不匹配
     */
    public static boolean matchPart(String userPart, String requiredPart) {
        if ("*".equals(userPart)) {
            return true;
        }
        return userPart != null && userPart.equals(requiredPart);
    }
}
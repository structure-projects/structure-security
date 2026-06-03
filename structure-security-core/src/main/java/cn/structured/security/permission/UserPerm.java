package cn.structured.security.permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * 权限模型
 * 
 * <p>结构化的权限表示，支持多层级权限结构（如 order:create, system:order:create）</p>
 * 
 * <p>权限格式：{resource}:{action} 或 {module}:{resource}:{action}</p>
 * 
 * <p>示例：
 * <ul>
 *   <li>order:create - 订单创建权限</li>
 *   <li>order:* - 订单所有权限（资源级通配）</li>
 *   <li>*:read - 所有资源的读取权限（动作级通配）</li>
 *   <li>system:order:create - 系统订单创建权限（三层）</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPerm {

    /**
     * 权限各部分，按冒号分隔后的列表
     */
    private List<String> parts;

    /**
     * 创建权限对象
     * 
     * @param parts 权限各部分
     * @return UserPerm 权限对象
     */
    public static UserPerm of(String... parts) {
        return new UserPerm(Arrays.asList(parts));
    }

    /**
     * 从字符串解析权限对象
     * 
     * @param permissionStr 权限字符串（如 "order:create"）
     * @return UserPerm 权限对象，解析失败返回 null
     */
    public static UserPerm parse(String permissionStr) {
        if (permissionStr == null || permissionStr.isEmpty()) {
            return null;
        }
        String[] splitParts = permissionStr.split(":");
        if (splitParts.length == 0) {
            return null;
        }
        return new UserPerm(Arrays.asList(splitParts));
    }

    /**
     * 获取权限层级
     * 
     * @return 层级数（如 order:create 返回 2，system:order:create 返回 3）
     */
    public int getLevel() {
        return parts != null ? parts.size() : 0;
    }

    /**
     * 获取指定位置的权限部分
     * 
     * @param index 索引位置（从0开始）
     * @return 权限部分，索引越界返回 null
     */
    public String getPart(int index) {
        if (parts == null || index < 0 || index >= parts.size()) {
            return null;
        }
        return parts.get(index);
    }

    /**
     * 转换为字符串表示
     * 
     * @return 权限字符串（如 "order:create"）
     */
    @Override
    public String toString() {
        if (parts == null || parts.isEmpty()) {
            return "";
        }
        return String.join(":", parts);
    }
}
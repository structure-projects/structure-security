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
     * 用户ID
     */
    protected String userId;

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

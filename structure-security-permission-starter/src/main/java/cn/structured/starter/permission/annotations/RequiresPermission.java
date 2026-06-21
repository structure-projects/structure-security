package cn.structured.starter.permission.annotations;

import java.lang.annotation.*;

/**
 * 权限注解
 * 
 * <p>用于方法级别的权限控制，标注在需要权限的方法上</p>
 * 
 * <p>使用示例：
 * <pre>
 * {@code
 * @RequiresPermission("order:create")
 * public void createOrder() {
 *     // 需要 order:create 权限
 * }
 * 
 * @RequiresPermission("system:user:read")
 * public User getUser(Long id) {
 *     // 需要 system:user:read 权限
 * }
 * }
 * </pre>
 * </p>
 * 
 * <p>注意：该注解需要配合 Spring Security 的方法安全配置使用</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {

    /**
     * 需要的权限字符串
     * 
     * @return 权限字符串（如 "order:create"）
     */
    String value();
}
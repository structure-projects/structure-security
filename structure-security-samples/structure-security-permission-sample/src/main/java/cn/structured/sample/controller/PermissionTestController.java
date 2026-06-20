package cn.structured.sample.controller;

import cn.structured.starter.permission.service.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限测试控制器
 * 
 * 演示如何使用权限注解和编程方式进行权限检查
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class PermissionTestController {

    private final IPermissionService permissionService;

    public PermissionTestController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 健康检查接口（无需认证）
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "permission-sample");
        return result;
    }

    /**
     * 需要 order:create 权限
     */
    @PostMapping("/order/create")
    @PreAuthorize("@permissionService.hasPermission('order:create')")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> order) {
        log.info("Creating order with permissions check passed");
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "订单创建成功");
        result.put("order", order);
        return result;
    }

    /**
     * 需要 order:read 权限
     */
    @GetMapping("/order/{id}")
    @PreAuthorize("@permissionService.hasPermission('order:read')")
    public Map<String, Object> getOrder(@PathVariable Long id) {
        log.info("Getting order {} with permissions check passed", id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("orderId", id);
        result.put("data", "订单详情数据");
        return result;
    }

    /**
     * 需要 order:* 权限（资源级通配）
     */
    @DeleteMapping("/order/{id}")
    @PreAuthorize("@permissionService.hasPermission('order:*')")
    public Map<String, Object> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order {} with permissions check passed", id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "订单删除成功");
        result.put("orderId", id);
        return result;
    }

    /**
     * 需要 *:read 权限（动作级通配）
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("@permissionService.hasPermission('user:read')")
    public Map<String, Object> getUser(@PathVariable Long id) {
        log.info("Getting user {} with permissions check passed", id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("userId", id);
        result.put("data", "用户详情数据");
        return result;
    }

    /**
     * 需要三层权限 system:config:edit
     */
    @PutMapping("/system/config")
    @PreAuthorize("@permissionService.hasPermission('system:config:edit')")
    public Map<String, Object> updateConfig(@RequestBody Map<String, Object> config) {
        log.info("Updating system config with permissions check passed");
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "配置更新成功");
        result.put("config", config);
        return result;
    }

    /**
     * 需要三层通配权限 system:*:*
     */
    @GetMapping("/system/logs")
    @PreAuthorize("@permissionService.hasPermission('system:*:*')")
    public Map<String, Object> getSystemLogs() {
        log.info("Getting system logs with permissions check passed");
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("logs", "系统日志数据");
        return result;
    }

    /**
     * 编程方式检查权限
     */
    @GetMapping("/permission/check")
    public Map<String, Object> checkPermission(@RequestParam String permission) {
        boolean hasPermission = permissionService.hasPermission(permission);
        log.info("Checking permission '{}': {}", permission, hasPermission);
        
        Map<String, Object> result = new HashMap<>();
        result.put("permission", permission);
        result.put("hasPermission", hasPermission);
        return result;
    }

    /**
     * 获取当前用户的权限列表
     */
    @GetMapping("/permission/list")
    public Map<String, Object> getUserPermissions() {
        Map<String, Object> result = new HashMap<>();
        result.put("permissions", permissionService.getUserPermissions());
        return result;
    }
}
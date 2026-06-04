package cn.structured.security.basicauth.server.sample.controller;

import cn.structured.security.entity.StructureAuthUser;
import cn.structured.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 受保护资源控制器
 * 可以通过 Basic Auth 或 JWT Token 访问
 *
 * @author chuck
 */
@Slf4j
@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            StructureAuthUser user = SecurityUtils.getAuthUser();
            result.put("success", true);
            result.put("message", "Hello from protected endpoint!");
            result.put("user", user.getUsername());
            result.put("authorities", user.getAuthorities());
            log.info("Protected endpoint accessed by user: {}", user.getUsername());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Not authenticated");
            log.warn("Unauthenticated access attempt");
        }
        
        return result;
    }

    @GetMapping("/user-info")
    public Map<String, Object> userInfo() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            StructureAuthUser user = SecurityUtils.getAuthUser();
            result.put("success", true);
            result.put("username", user.getUsername());
            result.put("id", user.getId());
            result.put("authorities", user.getAuthorities());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Not authenticated");
        }
        
        return result;
    }

    /**
     * 仅管理员可访问
     */
    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> adminOnly() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "This is admin only content");
        return result;
    }
}

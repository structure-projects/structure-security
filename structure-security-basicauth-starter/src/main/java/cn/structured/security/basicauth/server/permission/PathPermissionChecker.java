package cn.structured.security.basicauth.server.permission;

import cn.structured.security.basicauth.server.properties.BasicAuthServerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * 路径权限检查器
 * <p>
 * 用于检查用户是否有权限访问指定的URL路径
 * </p>
 *
 * @author chuck
 */
@Slf4j
@RequiredArgsConstructor
public class PathPermissionChecker {

    private final BasicAuthServerProperties properties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 检查用户是否有权限访问指定路径
     *
     * @param username 用户名
     * @param requestPath 请求路径
     * @return true 表示有权限，false 表示无权限
     */
    public boolean hasPermission(String username, String requestPath) {
        if (username == null || requestPath == null) {
            log.debug("Permission check failed: username or requestPath is null");
            return false;
        }

        List<String> allowedPaths = properties.getPermissionsForUser(username);
        
        if (allowedPaths == null || allowedPaths.isEmpty()) {
            log.debug("No permissions configured for user: {}, allowing access", username);
            return true;
        }

        for (String allowedPath : allowedPaths) {
            if (pathMatcher.match(allowedPath, requestPath)) {
                log.debug("User {} has permission to access path {} (matched pattern: {})", 
                        username, requestPath, allowedPath);
                return true;
            }
        }

        log.debug("User {} does NOT have permission to access path {}", username, requestPath);
        return false;
    }

    /**
     * 判断是否启用了路径权限控制
     *
     * @return true 表示启用，false 表示未启用
     */
    public boolean isPermissionEnabled() {
        return properties.getPermissions() != null && !properties.getPermissions().isEmpty();
    }
}
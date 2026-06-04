package cn.structured.security.basicauth.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic Auth 服务端配置属性
 *
 * <p>配置示例：</p>
 * <pre>{@code
 * structure:
 *   security:
 *     basicauth:
 *       server:
 *         enabled: true
 *         realm: "My Application"
 *         use-user-service: true
 *         users:
 *           admin: secret
 *           user: password
 *         permissions:
 *           admin:
 *             - /api/protected/**
 *             - /api/admin/**
 *           user:
 *             - /api/protected/hello
 *             - /api/protected/user-info
 * }</pre>
 *
 * @author chuck
 */
@Data
@ConfigurationProperties(prefix = "structure.security.basicauth.server")
public class BasicAuthServerProperties {

    /**
     * 是否启用 Basic Auth 服务端
     */
    private boolean enabled = true;

    /**
     * 认证领域（Realm）
     */
    private String realm = "Application";

    /**
     * 是否使用 UserDetailsService 获取用户
     */
    private boolean useUserService = false;

    /**
     * 预设用户列表（用户名 -> 密码）
     */
    private Map<String, String> users = new HashMap<>();

    /**
     * 用户路径权限配置（用户名 -> 允许访问的路径列表）
     */
    private Map<String, List<String>> permissions = new HashMap<>();

    /**
     * 获取用户允许访问的路径列表
     */
    public List<String> getPermissionsForUser(String username) {
        return permissions.getOrDefault(username, new ArrayList<>());
    }
}

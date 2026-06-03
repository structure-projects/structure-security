package cn.structured.security.basicauth.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
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
 *         users:
 *           admin: secret
 *           user: password
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
     * 预设用户列表（用户名 -> 密码）
     */
    private Map<String, String> users = new HashMap<>();
}

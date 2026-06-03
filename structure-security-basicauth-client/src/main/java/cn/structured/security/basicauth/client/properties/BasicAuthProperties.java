package cn.structured.security.basicauth.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Basic Auth 客户端配置属性
 *
 * <p>配置示例：</p>
 * <pre>{@code
 * structure:
 *   security:
 *     basicauth:
 *       client:
 *         enabled: true
 *         username: admin
 *         password: secret
 * }</pre>
 *
 * @author chuck
 */
@Data
@ConfigurationProperties(prefix = "structure.security.basicauth.client")
public class BasicAuthProperties {

    /**
     * 是否启用 Basic Auth 客户端
     */
    private boolean enabled = true;

    /**
     * 默认用户名
     */
    private String username;

    /**
     * 默认密码
     */
    private String password;
}

package cn.structured.security.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.annotation.PostConstruct;

/**
 * 用户上下文配置属性
 *
 * <p>用于配置用户信息获取的行为，支持远程获取用户信息</p>
 *
 * <p>配置示例：
 * <pre>
 * {@code
 * structure:
 *   security:
 *     context:
 *       remote:
 *         enabled: true
 *         user-info-url: https://auth-server/api/users/{userId}
 *         basic-auth:
 *           enabled: true
 *           username: admin
 *           password: password
 * }
 * </pre>
 * </p>
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("structure.security.context")
public class UserContextProperties {

    private static final Logger logger = LoggerFactory.getLogger(UserContextProperties.class);

    /**
     * 远程配置
     */
    private Remote remote = new Remote();

    @Getter
    @Setter
    @ToString
    public static class Remote {

        /**
         * 是否启用远程用户信息获取
         */
        private boolean enabled = false;

        /**
         * 远程用户信息接口 URL，{userId} 会被替换为实际用户ID
         */
        private String userInfoUrl;

        /**
         * Basic Auth 配置
         */
        private BasicAuth basicAuth = new BasicAuth();
    }

    @Getter
    @Setter
    @ToString
    public static class BasicAuth {

        /**
         * 是否启用 Basic Auth
         */
        private boolean enabled = false;

        /**
         * Basic Auth 用户名
         */
        private String username;

        /**
         * Basic Auth 密码
         */
        private String password;
    }

    @PostConstruct
    public void init() {
        logger.info("UserContext properties initialized: {}", this);
    }
}
package cn.structured.starter.permission.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.annotation.PostConstruct;

/**
 * 权限配置属性
 *
 * <p>用于配置权限模块的行为</p>
 *
 * <p>配置示例：
 * <pre>
 * {@code
 * structure:
 *   security:
 *     permission:
 *       enabled: true
 * }
 * </pre>
 * </p>
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("structure.security.permission")
public class PermissionProperties {

    private static final Logger logger = LoggerFactory.getLogger(PermissionProperties.class);

    /**
     * 是否启用权限模块
     */
    private boolean enabled = true;

    @PostConstruct
    public void init() {
        logger.info("Permission properties initialized: enabled={}", enabled);
    }
}
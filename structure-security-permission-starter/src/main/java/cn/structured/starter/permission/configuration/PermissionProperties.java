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
 *       providerType: context
 *       remoteUrl: https://auth-server/api/permissions/{userId}
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

    /**
     * 远程权限接口 URL，{userId} 会被替换为实际用户ID
     */
    private String remoteUrl;

    /**
     * 权限提供者类型：
     * <ul>
     *   <li>context - 从 Spring Security 上下文获取（默认）</li>
     *   <li>remote - 从远程授权服务器获取</li>
     * </ul>
     */
    private String providerType = "context";

    @PostConstruct
    public void init() {
        logger.info("Permission properties initialized: enabled={}, remoteUrl={}, providerType={}",
                enabled, remoteUrl, providerType);
    }
}
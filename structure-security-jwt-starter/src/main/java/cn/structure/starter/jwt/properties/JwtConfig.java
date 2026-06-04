package cn.structure.starter.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * <p>
 * JWT 认证配置
 * </p>
 *
 * @author chuck
 * @version 1.1.0
 * @since 2021/7/10 19:58
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("structure.jwt")
public class JwtConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    /**
     * JWT 加密密钥
     */
    private String secret = "JWT";

    /**
     * Token 有效时间（秒）
     * 默认 9 小时
     */
    private long jwtTokenValidity = 9 * 60 * 60;

    @PostConstruct
    public void init() {
        logger.info("JWT configuration initialized: secret length={}, validity={}s",
                secret != null ? secret.length() : 0, jwtTokenValidity);
    }

}

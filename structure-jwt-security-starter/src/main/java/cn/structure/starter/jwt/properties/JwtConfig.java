package cn.structure.starter.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Jwt配置
 * </p>
 *
 * @author chuck
 * @version 1.0.1
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
     * 加密串
     */
    private String secret = "JWT";

    /**
     * token 有效时间
     */
    private long jwtTokenValidity = 9 * 60 * 60;

    private String corsFilterClass = "cn.structure.starter.jwt.configuration.CorsFilter";

    /**
     * 访问权限
     */
    private Map<String, List<String>> antMatchers;

}

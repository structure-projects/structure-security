package cn.structure.starter.oauth.resource.example.config;

import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * bean配置（适配新的 Spring Security OAuth2 Resource Server）
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/8 15:04
 * @deprecated 新的 Spring Security OAuth2 Resource Server 不再需要 TokenStore
 */
@Configuration
@Deprecated
public class BeanConfig {

    // 注意：在新的 Spring Security OAuth2 Resource Server 架构中
    // 不再需要 TokenStore，因为现在使用 JwtDecoder 来验证 token
    // JwtDecoder 已经在 AutoResourceConfiguration 中配置了

}

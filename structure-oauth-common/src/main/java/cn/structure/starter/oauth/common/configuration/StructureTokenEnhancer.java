package cn.structure.starter.oauth.common.configuration;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * <p>
 * Token 自定义增强器（适配新的 Spring Authorization Server）
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/6/24 14:05
 */
public class StructureTokenEnhancer implements OAuth2TokenCustomizer<OAuth2TokenContext> {

    @Override
    public void customize(OAuth2TokenContext context) {
        // 在新的架构中，token 增强通过 OAuth2TokenCustomizer 实现
        // 如果有需要，可以在这里添加自定义逻辑
    }
}

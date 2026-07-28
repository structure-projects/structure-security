package cn.structured.security.oauth.starter.resource.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.starter.oauth.common.configuration.StructureResourceAccessTokenConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.FileCopyUtils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <p>
 * 自动装配（适配新的 Spring Security Resource Server）
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/7 20:24
 */
@Configuration
@EnableConfigurationProperties({OauthResourceProperties.class})
@EnableMethodSecurity(prePostEnabled = true)
@Import(value = {ResourceServerConfig.class})
public class AutoResourceConfiguration {

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthExceptionEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder jwtDecoder() {
        try {
            org.springframework.core.io.Resource resource = new ClassPathResource(AuthConstant.PUBLIC_CERT);
            String publicKeyContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
            
            // 清除公钥中的 PEM 格式标记
            publicKeyContent = publicKeyContent
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyContent);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            
            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure JWT decoder", e);
        }
    }

    /**
     * 自定义 JWT 认证转换器。
     * <p>必须覆盖 Spring Security 默认的 {@code JwtAuthenticationConverter}，
     * 将 JWT claims 还原为 {@link cn.structured.security.entity.StructureAuthUser}，
     * 否则 principal 是 Jwt 对象，SecurityUtils 无法提取 userId。</p>
     *
     * <p>Bean 名不使用默认的 {@code jwtAuthenticationConverter}，避免与 Spring Boot
     * OAuth2 自动配置创建的同名 {@code JwtAuthenticationConverter} 发生覆盖。</p>
     */
    @Bean("structureJwtAuthenticationConverter")
    public Converter<Jwt, AbstractAuthenticationToken> structureJwtAuthenticationConverter() {
        return new StructureResourceAccessTokenConverter();
    }
}

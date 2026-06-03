package cn.structure.starter.oauth.common.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structured.security.entity.StructureAuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * JWT 声明集构建器（适配新的 Spring Security）
 * </p>
 *
 * @author chuck
 */
public class StructureAccessTokenConverter {

    /**
     * 构建 JWT 声明集
     */
    public static JwtClaimsSet.Builder buildJwtClaims(Authentication authentication, 
            String issuer, String subject, Instant issuedAt, Instant expiresAt) {
        
        Map<String, Object> claims = new LinkedHashMap<>();
        
        // 添加用户名
        claims.put("sub", subject);
        
        // 添加用户 ID
        Object principal = authentication.getPrincipal();
        if (principal instanceof StructureAuthUser) {
            StructureAuthUser user = (StructureAuthUser) principal;
            claims.put(AuthConstant.USER_ID, user.getId());
        }
        
        // 添加权限信息
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            claims.put("authorities", authorities);
        }
        
        return JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claims(c -> c.putAll(claims));
    }
}

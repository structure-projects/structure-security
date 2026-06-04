package cn.structure.starter.oauth.common.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.common.constant.SymbolConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * JWT 认证转换器（适配新的 Spring Security Resource Server）
 * </p>
 *
 * @author chuck
 */
public class StructureResourceAccessTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

    /**
     * 从 JWT 中提取权限信息
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        
        if (!claims.containsKey(AuthConstant.AUTHORITIES)) {
            // 参数不包含任何权限，存入默认权限标识
            return AuthorityUtils.createAuthorityList("ONLY_USER");
        }
        
        Object authoritiesObj = claims.get(AuthConstant.AUTHORITIES);
        
        if (authoritiesObj instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authoritiesObj);
        } else if (authoritiesObj instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<String> authoritiesCollection = (Collection<String>) authoritiesObj;
            return authoritiesCollection.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }
    }

    /**
     * 简单的权限工具类
     */
    private static class AuthorityUtils {
        public static List<GrantedAuthority> createAuthorityList(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;
        }

        public static List<GrantedAuthority> commaSeparatedStringToAuthorityList(String authorityString) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (org.springframework.util.StringUtils.hasText(authorityString)) {
                String[] roles = authorityString.split(",");
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role.trim()));
                }
            }
            return authorities;
        }
    }
}

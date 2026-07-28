package cn.structure.starter.oauth.common.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.common.constant.SymbolConstant;
import cn.structured.security.entity.StructureAuthUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * JWT 认证转换器（OAuth2 Resource Server 端）
 * 将 JWT claims 还原为 StructureAuthUser，保持与 JWT Starter 一致的 principal 类型
 * </p>
 *
 * @author chuck
 */
public class StructureResourceAccessTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();

        // 从 JWT c laims 还原用户信息
        StructureAuthUser authUser = new StructureAuthUser();

        // userId：来自 AuthConstant.USER_ID claim，fallback 到 sub
        Object userIdClaim = claims.get(AuthConstant.USER_ID);
        if (userIdClaim != null) {
            if (userIdClaim instanceof Number) {
                authUser.setId(((Number) userIdClaim).longValue());
            } else {
                authUser.setId(Long.parseLong(userIdClaim.toString()));
            }
        } else {
            // fallback: 尝试用 sub 作为 userId
            String sub = jwt.getSubject();
            if (sub != null) {
                try {
                    authUser.setId(Long.parseLong(sub));
                } catch (NumberFormatException e) {
                    authUser.setId(sub);
                }
            }
        }

        // 用户名：来自 sub claim
        authUser.setUsername(jwt.getSubject());

        // 设置默认账户状态（从 JWT 只能拿到认证通过的信息，默认启用）
        authUser.setEnable(true);
        authUser.setUnlocked(true);
        authUser.setUnexpired(true);

        // 权限：从 authorities claim 解析
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        authUser.setAuthorities(authorities);

        return new UsernamePasswordAuthenticationToken(authUser, null, authorities);
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

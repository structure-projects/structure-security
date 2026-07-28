package cn.structure.starter.jwt.service;

import cn.structure.common.constant.AuthConstant;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.properties.JwtConfig;
import cn.structured.security.entity.StructureAuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jwt 默认的实现
 *
 * @author chuck
 */
@NoArgsConstructor
@AllArgsConstructor
public class JwtDefaultServiceImpl implements ITokenService {

    private JwtConfig jwtConfig;

    @Override
    public StructureAuthUser getUserInfoFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        StructureAuthUser authUser = new StructureAuthUser();
        authUser.setId(claims.get(AuthConstant.USER_ID, String.class));
        authUser.setUsername(claims.get("sub", String.class));
        return authUser;
    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generateToken(StructureAuthUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AuthConstant.USER_ID, userDetails.getId());
        claims.put("sub", userDetails.getUsername());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    @Override
    public String generateTokenWithPermissions(StructureAuthUser userDetails, List<String> permissions) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AuthConstant.USER_ID, userDetails.getId());
        claims.put("sub", userDetails.getUsername());

        claims.put(AuthConstant.AUTHORITIES, permissions);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    @Override
    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getJwtTokenValidity() * 1000))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    @Override
    public String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }
}

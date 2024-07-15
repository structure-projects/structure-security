package cn.structure.starter.jwt.configuration;

import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.properties.JwtConfig;
import cn.structured.security.entity.StructureAuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
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
        authUser.setId((String) claims.get("id"));
        authUser.setPassword((String) claims.get("username"));
        return authUser;
    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
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
        claims.put("id", userDetails.getId());
        claims.put("username", userDetails.getUsername());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    @Override
    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getJwtTokenValidity() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret()).compact();
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

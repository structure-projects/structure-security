package cn.structure.starter.jwt.interfaces;

import cn.structured.security.entity.StructureAuthUser;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * tokenService
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/10 20:07
 */
public interface ITokenService {
    /**
     * 通过token转换为用户
     *
     * @param token 用户token
     * @return {@link StructureAuthUser}
     */
    StructureAuthUser getUserInfoFromToken(String token);

    /**
     * 通过token转换为Claims
     *
     * @param token 用户token
     * @return {@link StructureAuthUser}
     */
    Claims getAllClaimsFromToken(String token);

    /**
     * 校验token是否失效
     *
     * @param token 用户token
     * @return {@link StructureAuthUser}
     */
    Boolean isTokenExpired(String token);

    /**
     * 通过用户信息生成token
     *
     * @param userDetails 用户详情
     * @return {@link String}
     */
    String generateToken(StructureAuthUser userDetails);

    /**
     * 通过claims生成token
     *
     * @param claims  参数
     * @param subject 主体
     * @return {@link String}
     */
    String doGenerateToken(Map<String, Object> claims, String subject);

    /**
     * 通过请求头中获取token
     *
     * @param request 请求
     * @return {@link String}
     */
    String getToken(HttpServletRequest request);
}

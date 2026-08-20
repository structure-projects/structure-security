/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structure.starter.jwt.interfaces;

import cn.structured.security.entity.StructureAuthUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
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
     * 通过用户信息和权限列表生成token
     *
     * @param userDetails 用户详情
     * @param permissions 权限列表
     * @return {@link String}
     */
    String generateTokenWithPermissions(StructureAuthUser userDetails, List<String> permissions);

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

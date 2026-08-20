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

package cn.structure.starter.jwt.service;

import cn.structure.common.enums.ResultCodeEnum;
import cn.structure.common.exception.CommonException;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.interfaces.ITokenStore;
import cn.structured.security.entity.StructureAuthUser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内部存储的实现
 *
 * @author cqliut
 * @version 2023.0625
 * @since 1.0.1
 */
public class InnerTokenStore implements ITokenStore {

    private final ITokenService tokenService;

    private final Map<String, StructureAuthUser> userStore = new ConcurrentHashMap<>();

    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public InnerTokenStore(ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public StructureAuthUser getUser(String token) {
        return userStore.get(token);
    }

    @Override
    public String setUser(StructureAuthUser user) {
        String token = tokenStore.containsKey(user.getUsername()) ? tokenStore.get(user.getUsername()) : generateToken(user);
        Boolean tokenExpired = tokenService.isTokenExpired(token);
        return Boolean.TRUE.equals(tokenExpired) ? generateToken(user) : token;
    }

    @Override
    public String refreshToken(String refreshToken) {
        StructureAuthUser authUser = userStore.get(refreshToken);
        if (null != authUser) {
            return generateToken(authUser);
        }
        throw new CommonException(ResultCodeEnum.UNAUTHORIZED.getCode(), "refresh token 已失效");
    }

    @Override
    public void clearStore(String token) {
        StructureAuthUser authUser = userStore.remove(token);
        tokenStore.remove(authUser.getUsername());
    }

    public synchronized String generateToken(StructureAuthUser user) {
        String token = tokenService.generateToken(user);
        tokenStore.put(user.getUsername(), token);
        userStore.put(token, user);
        return token;
    }
}

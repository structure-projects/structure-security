/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     `http://www.apache.org/licenses/LICENSE-2.0`
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structured.security.provider;

import cn.structure.common.entity.ResultVO;
import cn.structure.common.utils.BasicAuthGenerator;
import cn.structure.common.utils.HttpClientUtil;
import cn.structured.security.configuration.UserContextProperties;
import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.interfaces.IUserProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 远程用户信息提供者
 *
 * <p>从远程授权服务器获取用户信息并设置到 UserContext。
 * 适用于微服务架构中，需要从用户中心获取用户信息的场景。</p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
@Slf4j
@Component
public class RemoteUserProvider implements IUserProvider {

    private final HttpClient httpClient;
    private final UserContextProperties properties;
    private final String basicAuthHeader;
    private final ObjectMapper objectMapper;

    @Autowired
    public RemoteUserProvider(UserContextProperties properties) {
        this.properties = properties;
        this.httpClient = HttpClientUtil.getHttpClient();
        this.objectMapper = new ObjectMapper();

        if (properties.getRemote().getBasicAuth().isEnabled()) {
            String username = properties.getRemote().getBasicAuth().getUsername();
            String password = properties.getRemote().getBasicAuth().getPassword();
            if (username != null && password != null) {
                this.basicAuthHeader = BasicAuthGenerator.generate(username, password);
                log.info("Basic Auth enabled for remote user provider");
            } else {
                this.basicAuthHeader = null;
            }
        } else {
            this.basicAuthHeader = null;
        }
    }

    @Override
    public UserContextEntity loadUser(String userId) {
        if (!properties.getRemote().isEnabled()) {
            log.debug("Remote user provider is not enabled");
            return null;
        }

        String userInfoUrl = properties.getRemote().getUserInfoUrl();
        if (userInfoUrl == null || userInfoUrl.isEmpty()) {
            log.warn("Remote user info URL is not configured");
            return null;
        }

        try {
            String url = userInfoUrl.replace("{userId}", userId);
            URI uri = new URIBuilder(url).build();

            HttpGet httpGet = new HttpGet(uri);
            if (basicAuthHeader != null) {
                httpGet.setHeader("Authorization", basicAuthHeader);
            }
            httpGet.setHeader("Accept", "application/json");

            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                // 解析响应体
                log.debug("Response body: {}", responseBody);
                // 创建 ResultVO 对象
                ResultVO<UserContextEntity> resultVO = objectMapper.readValue(
                        responseBody,
                        new TypeReference<>() {
                        }
                );
                if (!resultVO.getSuccess()) {
                    log.warn("Failed to load user from remote server, error message: {}", resultVO.getMsg());
                    return null;
                }
                UserContextEntity user = resultVO.getData();
                if (user != null) {
                    UserContext.set(user);
                    log.debug("User loaded from remote server: {}", userId);
                }
                return user;
            } else {
                log.warn("Failed to load user from remote server, status code: {}", statusCode);
                return null;
            }
        } catch (URISyntaxException | IOException e) {
            log.error("Failed to load user from remote server: {}", userId, e);
            return null;
        }
    }
}
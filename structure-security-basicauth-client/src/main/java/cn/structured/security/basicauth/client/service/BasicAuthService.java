package cn.structured.security.basicauth.client.service;

import cn.structured.security.basicauth.client.properties.BasicAuthProperties;
import cn.structured.security.common.util.BasicAuthGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Basic Auth 服务类
 * <p>
 * 提供便捷的 Basic Auth 认证头生成方法
 * </p>
 *
 * @author chuck
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthService {

    private final BasicAuthProperties properties;

    /**
     * 使用默认配置生成 Basic Auth 认证头
     *
     * @return Basic Auth 认证头值
     */
    public String generateAuthHeader() {
        if (!StringUtils.hasText(properties.getUsername()) || !StringUtils.hasText(properties.getPassword())) {
            throw new IllegalStateException("Default username and password must be configured");
        }
        return BasicAuthGenerator.generate(properties.getUsername(), properties.getPassword());
    }

    /**
     * 使用指定的用户名和密码生成 Basic Auth 认证头
     *
     * @param username 用户名
     * @param password 密码
     * @return Basic Auth 认证头值
     */
    public String generateAuthHeader(String username, String password) {
        return BasicAuthGenerator.generate(username, password);
    }

    /**
     * 解析 Basic Auth 认证头
     *
     * @param authHeader 认证头值
     * @return 包含用户名和密码的数组 [username, password]
     */
    public String[] parseAuthHeader(String authHeader) {
        return BasicAuthGenerator.parse(authHeader);
    }
}

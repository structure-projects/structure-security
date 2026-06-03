package cn.structured.security.basicauth.server.validator;

import cn.structured.security.basicauth.server.interfaces.CredentialValidator;
import cn.structured.security.basicauth.server.properties.BasicAuthServerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 内存凭证验证器
 * <p>
 * 使用配置文件中预设的用户列表进行验证
 * </p>
 *
 * @author chuck
 */
@Slf4j
@RequiredArgsConstructor
public class InMemoryCredentialValidator implements CredentialValidator {

    private final BasicAuthServerProperties properties;

    @Override
    public boolean validate(String username, String password) {
        if (username == null || password == null) {
            log.debug("Validation failed: username or password is null");
            return false;
        }

        Map<String, String> users = properties.getUsers();
        if (users == null || users.isEmpty()) {
            log.debug("Validation failed: no users configured");
            return false;
        }

        String storedPassword = users.get(username);
        boolean valid = storedPassword != null && storedPassword.equals(password);
        
        if (valid) {
            log.debug("Validation successful for user: {}", username);
        } else {
            log.debug("Validation failed for user: {}", username);
        }
        
        return valid;
    }
}

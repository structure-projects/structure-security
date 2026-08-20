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

package cn.structured.security.basicauth.server.validator;

import cn.structured.security.basicauth.server.interfaces.CredentialValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户服务凭证验证器
 * <p>
 * 使用 UserDetailsService 获取用户信息进行验证
 * </p>
 *
 * @author chuck
 */
@Slf4j
@RequiredArgsConstructor
public class UserServiceCredentialValidator implements CredentialValidator {

    private final UserDetailsService userDetailsService;

    @Override
    public boolean validate(String username, String password) {
        if (username == null || password == null) {
            log.debug("Validation failed: username or password is null");
            return false;
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (userDetails == null) {
                log.debug("Validation failed: user not found by UserDetailsService");
                return false;
            }

            String storedPassword = userDetails.getPassword();
            boolean valid = storedPassword != null && storedPassword.equals(password);
            
            if (valid) {
                log.debug("Validation successful for user: {} via UserDetailsService", username);
            } else {
                log.debug("Validation failed for user: {} via UserDetailsService", username);
            }
            
            return valid;
        } catch (UsernameNotFoundException e) {
            log.debug("Validation failed: user not found - {}", username);
            return false;
        }
    }
}
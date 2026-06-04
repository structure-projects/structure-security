# Structure Security Basic Auth Server

Basic Auth 服务端模块，用于验证 Basic Auth 凭证。

## 功能特性

- 验证 Basic Auth 认证头
- 可自定义凭证验证器接口
- 内存凭证验证器（配置文件支持）
- Spring Security 过滤器集成
- 自动配置支持

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-basicauth-server</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置

```yaml
structure:
  security:
    basicauth:
      server:
        enabled: true
        realm: "My Application"
        users:
          admin: secret
          user: password123
```

### 3. 集成 Spring Security 配置

```java
import cn.structured.security.basicauth.server.filter.BasicAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private BasicAuthFilter basicAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

### 4. 自定义凭证验证器

```java
import cn.structured.security.basicauth.server.interfaces.CredentialValidator;
import org.springframework.stereotype.Component;

@Component
public class CustomCredentialValidator implements CredentialValidator {

    @Override
    public boolean validate(String username, String password) {
        // 自定义验证逻辑，例如从数据库查询
        return userRepository.existsByUsernameAndPassword(username, password);
    }
}
```

## 配置属性说明

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `structure.security.basicauth.server.enabled` | boolean | true | 是否启用 Basic Auth 服务端 |
| `structure.security.basicauth.server.realm` | string | "Application" | 认证领域 |
| `structure.security.basicauth.server.users` | Map | {} | 预设用户列表（用户名 -> 密码） |

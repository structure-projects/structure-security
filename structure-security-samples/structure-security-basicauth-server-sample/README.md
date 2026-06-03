# Basic Auth Server 示例

演示如何使用 Spring Security 内置的 Basic Auth 认证。

## 功能特点

- 使用 Spring Security 内置的 httpBasic() 认证
- 内存用户存储，支持 admin 和 user 两个账号
- 受保护的 REST API 端点

## 快速开始

### 1. 启动服务端

```bash
cd structure-security-basicauth-server-sample
mvn spring-boot:run
```

### 2. 测试 Basic Auth 认证

```bash
# 使用 -u 参数
curl -u admin:admin123 http://localhost:8082/api/protected/hello

# 使用 -H 参数直接设置 Authorization 头
curl http://localhost:8082/api/protected/hello -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### 3. 查看用户信息

```bash
curl -u admin:admin123 http://localhost:8082/api/protected/user-info
```

### 4. 测试无权限访问

```bash
# 不带认证，会返回 401
curl http://localhost:8082/api/protected/hello
```

## 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | ROLE_ADMIN |
| user | user123 | ROLE_USER |

## 核心代码说明

### SecurityConfig

使用 Spring Security 内置的 httpBasic() 认证：

```java
http
    .csrf(AbstractHttpConfigurer::disable)
    .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/api/public/**").permitAll()
        .anyRequest().authenticated()
    )
    .httpBasic(httpBasic -> {});
```

## API 说明

### 公开接口（无需认证）

- `GET /api/public/demo` - 演示接口
- `GET /api/public/help` - 帮助信息

### 受保护接口（需要 Basic Auth）

- `GET /api/protected/hello` - 欢迎接口
- `GET /api/protected/user-info` - 用户信息接口

## License

Apache License 2.0

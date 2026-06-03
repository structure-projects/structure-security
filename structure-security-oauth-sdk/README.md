# Structure Security OAuth SDK

OAuth 2.0 客户端 SDK，提供远程认证服务调用功能。

## 功能特性

- 提供 OAuth 2.0 认证客户端
- 支持远程服务调用配置
- 自动配置支持

## 主要组件

### AuthClient

OAuth 认证客户端，用于与远程认证服务交互。

```java
@Autowired
private AuthClient authClient;

// 使用示例
LoginResultDTO result = authClient.login(loginRequest);
```

### AuthClientConfig

认证客户端配置。

```yaml
structure:
  security:
    oauth:
      client:
        server-url: https://your-auth-server.com
        client-id: your-client-id
        client-secret: your-client-secret
```

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-oauth-sdk</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置认证服务

在 `application.yml` 中配置认证服务地址。

### 3. 使用 AuthClient

在代码中注入并使用 `AuthClient`。

## 项目依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-oauth-sdk</artifactId>
    <version>${revision}</version>
</dependency>
```

## License

Apache License 2.0

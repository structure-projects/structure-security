# Structure Security JWT Starter

这是一个 Spring Security JWT 认证和授权的 Starter 模块。

## 功能特性

- 提供 JWT Token 的生成、验证和刷新
- 支持自定义用户详情服务
- 提供登录和认证相关的 Endpoint
- 集成 Spring Security 配置
- 支持 CORS 配置
- 提供访问拒绝和认证入口点处理

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-jwt-starter</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置应用

创建 `application.yml` 或 `application.properties`：

```yaml
spring:
  security:
    jwt:
      secret: your-jwt-secret-key-at-least-256-bits-long
      issuer: structure-security
      expiration: 7200  # 2小时，单位：秒
```

### 3. 实现用户服务

```java
@Component
public class CustomUserServiceImpl implements ITokenService {
    
    @Override
    public StructureAuthUser loadUserByUsername(String username) {
        // 从数据库或其他地方加载用户信息
        return new StructureAuthUser(username, "encoded-password", 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
```

## 配置选项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| spring.security.jwt.secret | JWT 签名密钥 | 必填 |
| spring.security.jwt.issuer | JWT 签发者 | structure-security |
| spring.security.jwt.expiration | Token 过期时间（秒） | 7200 |

## 使用示例

### 登录获取 Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password"}'
```

### 使用 Token 访问受保护资源

```bash
curl http://localhost:8080/api/protected \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

## License

Apache License 2.0

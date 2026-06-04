# Structure Security OAuth Resource Starter

OAuth 2.0 资源服务器 Starter 模块，用于保护 API 资源。

## 功能特性

- OAuth 2.0 Token 验证
- 资源访问控制
- 支持 JWT 和 Opaque Token
- 集成 Spring Security 配置
- 公钥配置支持

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-oauth-resource-starter</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置应用

```yaml
structure:
  security:
    oauth:
      resource:
        jwt:
          public-key-location: classpath:public.cert
```

### 3. 保护资源

```java
@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public String getResource() {
        return "Protected Resource";
    }
}
```

## 配置选项

| 配置项 | 说明 |
|--------|------|
| structure.security.oauth.resource.jwt.public-key-location | JWT 公钥位置 |
| structure.security.oauth.resource.jwt.issuer-uri | Token 签发者 URI |

## 项目依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-oauth-resource-starter</artifactId>
    <version>${revision}</version>
</dependency>
```

## License

Apache License 2.0

# JWT Example

这是一个 Spring Security JWT 认证的示例工程。

## 功能特性

- JWT Token 生成和验证
- 用户登录
- 受保护 API 访问
- 完整的集成测试

## 快速开始

### 1. 运行应用

```bash
mvn spring-boot:run
```

或直接运行主类 `JwtExampleApplication`。

### 2. 测试 API

#### 登录获取 Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password"}'
```

#### 使用 Token 访问受保护资源

```bash
curl http://localhost:8080/api/protected/hello \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

## 项目依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-jwt-starter</artifactId>
</dependency>
```

## 运行测试

```bash
mvn test
```

## License

Apache License 2.0

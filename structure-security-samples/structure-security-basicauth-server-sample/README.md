# Basic Auth Server Sample

Basic Auth 服务端示例工程，集成了 JWT

## 功能特点

- Basic Auth 认证
- JWT Token 认证
- 两种认证方式共存
- 权限控制

## 快速开始

### 1. 运行应用

```bash
mvn spring-boot:run
```

### 2. 测试接口

#### 测试用户

- `admin/admin123`
- `user/user123`

#### Basic Auth 方式

```bash
# 使用 Basic Auth 直接访问受保护接口
curl -u admin:admin123 http://localhost:8082/api/protected/hello

# 或者使用完整 Header
curl http://localhost:8082/api/protected/hello \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

#### JWT 方式

```bash
# 1. 登录获取 JWT Token
curl -X POST http://localhost:8082/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. 使用 Token 访问
curl http://localhost:8082/api/protected/hello \
  -H "Authorization: Bearer <your_token>"
```

#### 查看帮助

```bash
curl http://localhost:8082/api/public/help
```

## 认证方式说明

### Basic Auth 流程

1. 客户端将用户名密码用冒号连接，Base64 编码
2. 在请求头中添加 `Authorization: Basic <encoded>`
3. 服务器验证凭证

### JWT 流程

1. 使用 `/api/user/login` 登录获取 Token
2. 使用 `Authorization: Bearer <token>` 访问资源

## 配置说明

```yaml
structure:
  security:
    basicauth:
      server:
        enabled: true
        realm: "Basic Auth Demo"
        users:
          admin: admin123
          user: user123
    jwt:
      secret: your-secret-key
```

## 项目依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-basicauth-server</artifactId>
</dependency>
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-jwt-starter</artifactId>
</dependency>
```

## License

Apache License 2.0

# Structure Security Samples

这是 Structure Security 框架的示例项目集合。

## 模块列表

| 模块 | 说明 | 端口 |
|------|------|------|
| structure-security-jwt-example | JWT 认证示例 | - |
| structure-security-permission-sample | 权限系统示例 | - |
| structure-security-oauth-resource-example | OAuth2 资源服务器示例 | - |
| structure-security-basicauth-client-sample | Basic Auth 客户端示例 | 8081 |
| structure-security-basicauth-server-sample | Basic Auth 服务端示例（集成 JWT） | 8082 |

## 快速开始

### Basic Auth 客户端示例

```bash
cd structure-security-basicauth-client-sample
mvn spring-boot:run
```

测试端点：
- `GET /api/basicauth/demo` - 演示基本用法
- `GET /api/basicauth/generate?username=user&password=pass` - 生成 Basic Auth 头
- `POST /api/basicauth/parse` - 解析 Basic Auth 头

### Basic Auth 服务端示例（集成 JWT）

```bash
cd structure-security-basicauth-server-sample
mvn spring-boot:run
```

测试端点：
- `GET /api/auth/demo-flow` - 查看认证流程
- `POST /api/auth/basic-to-jwt` - Basic Auth 登录换取 JWT
- `GET /api/protected/hello` - 需要认证的受保护端点

测试用户：
- `admin/admin123` - 管理员
- `user/user123` - 普通用户
- `guest/guest123` - 访客

## 完整测试流程

### 1. 启动 Basic Auth 服务端

```bash
# 在 8082 端口启动
cd structure-security-basicauth-server-sample
mvn spring-boot:run
```

### 2. 使用客户端测试

可以使用 curl 或 Postman 测试：

```bash
# 生成 Basic Auth 头（使用客户端示例）
curl "http://localhost:8081/api/basicauth/generate?username=admin&password=admin123"

# 或者直接测试服务端
curl -u admin:admin123 http://localhost:8082/api/protected/hello

# Basic Auth 登录换取 JWT
curl -X POST http://localhost:8082/api/auth/basic-to-jwt \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 运行测试

```bash
# 运行所有示例测试
cd structure-security-samples
mvn test

# 运行特定模块测试
cd structure-security-basicauth-client-sample
mvn test
```

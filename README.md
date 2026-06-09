# Structure Security

一套基于 Spring Security 的企业级安全认证与授权框架，提供完整的 JWT 认证、权限管理、OAuth2 支持和 Basic Auth 等功能模块。

## 特性

- **🔐 多种认证方式**：支持 JWT、Basic Auth、OAuth2 等多种认证方式
- **🛡️ 权限管理**：提供基于通配符的多层级权限模型，支持多种权限获取方式
- **🚀 开箱即用**：提供 Spring Boot Starter 自动配置，快速集成
- **⚡ 高性能**：支持本地缓存、分布式权限获取等优化方案
- **🔧 灵活扩展**：丰富的扩展点和接口，支持自定义实现

## 技术栈

| 技术 | 说明 | 版本 |
|------|------|------|
| Spring Boot | Web 框架 | 4.0.6 |
| Spring Security | 安全框架 | 由 Spring Boot 管理 |
| JJWT | JWT 库 | 0.12.7 |
| Java | 开发语言 | 17 (LTS) |
| Maven | 项目构建 | 3.9+ |
| Jakarta EE | Servlet API | 6.0.0 |
| structure-common | 基础组件 | 1.4.0 |

## 环境要求

### 运行环境要求

- **JDK 版本**: 17+ (推荐使用 JDK 17 或 JDK 21 LTS)
- **Maven 版本**: 3.9+
- **操作系统**: 支持所有主流操作系统 (Windows, Linux, macOS)

### 兼容性矩阵

| JDK 版本 | 支持情况 | 说明 |
|---------|---------|------|
| JDK 8-11 | ❌ 不支持 | 需要使用 structure-security 1.0.x 版本 |
| **JDK 17** | ✅ **完全支持** | **推荐使用 (LTS)** |
| JDK 21 | ✅ 完全支持 | 推荐使用 (LTS) |
| JDK 22+ | ✅ 兼容 | 向前兼容 |

## 模块说明

### 核心模块

```
structure-security/
├── structure-security-dependencies/     # 依赖管理模块
├── structure-security-common/           # 公共模块（业务对象、接口定义）
├── structure-security-core/             # 核心模块（安全基础组件）
```

### Starter 模块

| 模块 | 说明 |
|------|------|
| structure-security-jwt-starter | JWT 认证 Starter |
| structure-security-permission-starter | 权限管理 Starter |
| structure-security-basicauth-starter | Basic Auth Starter |
| structure-security-oauth-sdk | OAuth2 SDK |
| structure-security-oauth-common | OAuth2 公共模块 |
| structure-security-oauth-resource-starter | OAuth2 资源服务器 Starter |

### 示例模块

```
structure-security-samples/
├── structure-security-jwt-example/              # JWT 认证示例
├── structure-security-permission-sample/        # 权限系统示例
├── structure-security-basicauth-server-sample/  # Basic Auth 服务端示例
├── structure-security-oauth-resource-example/   # OAuth2 资源服务器示例
```

## 快速开始

### 添加依赖

首先添加依赖管理模块：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-security-dependencies</artifactId>
            <version>1.1.1-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

然后根据需要选择 Starter 依赖：

#### JWT 认证

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-jwt-starter</artifactId>
</dependency>
```

#### 权限管理

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-permission-starter</artifactId>
</dependency>
```

#### Basic Auth

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-basicauth-starter</artifactId>
</dependency>
```

### 配置应用

#### JWT 认证配置

```yaml
structure:
  security:
    enabled: true
    antMatchers:
      unAuthenticated:
        - /api/user/login
        - /doc.html
        - /webjars/**
  jwt:
    secret: your-secret-key-at-least-256-bits-long  # JWT 加密密钥
    jwtTokenValidity: 32400                           # Token 有效期（秒），默认 9 小时
```

#### 权限管理配置

```yaml
structure:
  security:
    permission:
      enabled: true
      provider-type: context  # context 或 remote
```

### 实现用户服务

```java
@Service
public class UserServiceImpl implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StructureAuthUser user = new StructureAuthUser();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEnable(true);
        user.setUnlocked(true);
        user.setUnexpired(true);
        
        List&lt;GrantedAuthority&gt; authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        return user;
    }
}
```

## 快速使用

### JWT 认证

#### 登录获取 Token

```bash
POST /api/user/login
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

响应：

```json
{
    "code": 200,
    "message": "操作成功",
    "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 访问受保护资源

```bash
GET /api/protected/resource
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 权限检查

使用注解方式：

```java
@PreAuthorize("@permissionService.hasPermission('order:create')")
public void createOrder() {
    // 需要 order:create 权限
}
```

使用编程方式：

```java
@Autowired
private IPermissionService permissionService;

public void createOrder() {
    if (permissionService.hasPermission("order:create")) {
        // 有权限，执行操作
    }
}
```

## 配置说明

### Security 配置项

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `structure.security.enabled` | 是否启用安全框架 | true | ❌ |
| `structure.security.default-login-url` | 默认登录页面 | - | ❌ |
| `structure.security.corsFilterClass` | CORS 过滤器类 | 内置类 | ❌ |
| `structure.security.antMatchers` | 路径权限配置 | - | ❌ |

### JWT 配置项

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `structure.jwt.secret` | JWT 加密密钥 | - | ✅ |
| `structure.jwt.jwtTokenValidity` | Token 有效期（秒） | 32400 (9小时) | ❌ |

### 权限配置项

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `structure.security.permission.enabled` | 是否启用权限模块 | true | ❌ |
| `structure.security.permission.provider-type` | 权限提供者类型 | context | ❌ |
| `structure.security.permission.remote-url` | 远程权限接口 URL | - | ❌ |

## 模块详细文档

### 核心模块
- [structure-security-common](./structure-security-common/README.md) - 公共模块文档
- [structure-security-core](./structure-security-core/README.md) - 核心模块文档

### Starter 模块
- [structure-security-jwt-starter](./structure-security-jwt-starter/README.md) - JWT 认证 Starter 文档
- [structure-security-permission-starter](./structure-security-permission-starter/README.md) - 权限管理 Starter 文档
- [structure-security-basicauth-starter](./structure-security-basicauth-starter/README.md) - Basic Auth Starter 文档
- [structure-security-oauth-common](./structure-security-oauth-common/README.md) - OAuth2 公共模块文档
- [structure-security-oauth-sdk](./structure-security-oauth-sdk/README.md) - OAuth2 SDK 文档
- [structure-security-oauth-resource-starter](./structure-security-oauth-resource-starter/README.md) - OAuth2 资源服务器 Starter 文档

### 示例项目
- [structure-security-samples](./structure-security-samples/README.md) - 示例项目总览
  - [structure-security-jwt-example](./structure-security-samples/structure-security-jwt-example/README.md) - JWT 认证示例
  - [structure-security-permission-sample](./structure-security-samples/structure-security-permission-sample/README.md) - 权限管理示例
  - [structure-security-basicauth-server-sample](./structure-security-samples/structure-security-basicauth-server-sample/README.md) - Basic Auth 服务端示例
  - [structure-security-oauth-resource-example](./structure-security-samples/structure-security-oauth-resource-example/README.md) - OAuth2 资源服务器示例

## 开发者维护

### 本地构建

```bash
cd structure-security-dependencies
mvn clean install -Dmaven.test.skip=true -Drevision=1.1.1-SNAPSHOT

# 或使用脚本
./scripts/install.sh 1.1.1-SNAPSHOT
```

### 发布到 Maven 仓库

```bash
./scripts/release.sh
```

### 项目结构

```
structure-security/
├── .github/workflows/        # GitHub Actions 工作流
├── scripts/                  # 构建和发布脚本
├── structure-security-dependencies/    # 依赖管理
├── structure-security-common/          # 公共模块
├── structure-security-core/            # 核心模块
├── structure-security-jwt-starter/     # JWT Starter
├── structure-security-permission-starter/  # 权限 Starter
├── structure-security-basicauth-starter/   # Basic Auth Starter
├── structure-security-oauth-sdk/       # OAuth2 SDK
├── structure-security-oauth-common/    # OAuth2 公共模块
├── structure-security-oauth-resource-starter/  # OAuth2 资源服务器 Starter
└── structure-security-samples/         # 示例项目
```

## 版本历史

详细变更记录请查看 [CHANGELOG.md](./CHANGELOG.md)。

### 当前版本

- **1.1.1-SNAPSHOT**：开发中版本
- **1.1.0-SNAPSHOT**：JDK 17 + Spring Boot 4.0.6 升级
- **1.0.3**：安全增强，CI/CD 集成
- **1.0.1**：Bug 修复
- **1.0.0**：初始版本

## 升级指南

从 1.0.x 升级到 1.1.x 请参考 [UPGRADE_GUIDE.md](./UPGRADE_GUIDE.md)。

## 贡献者

感谢以下贡献者的付出：

- **chuck** - 主要开发者
- **chuckLcq** - 项目维护者
- **Chuanqiang Liu** - 贡献者

## 许可证

Apache License 2.0

## 联系方式

- 📧 **邮箱**: support@structured.cn
- 🌐 **官网**: https://projects.structured.cn
- 💬 **GitHub Issues**: https://github.com/structure-projects/structure-security/issues


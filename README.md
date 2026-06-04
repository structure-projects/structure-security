# structure-security

一款基于 Spring Security 的 JWT 安全认证starter，简化 Spring Boot 项目的 JWT 安全认证开发。

## 技术栈

| 技术 | 说明 | 版本 |
|------|------|------|
| Spring Boot | Web框架 | 4.0.6 |
| Spring Security | 安全框架 | 6.2.3 |
| JJWT | JWT库 | 0.12.7 |
| Java | 开发语言 | 17 (LTS) |
| Maven | 项目构建 | 3.9+ |
| Jakarta EE | Servlet API | 6.0 |

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

```
structure-security/
├── structure-security-dependencies/     # 依赖管理模块
├── structure-security-core/             # 核心模块，提供安全基础组件
└── structure-jwt-security-starter/      # JWT安全认证starter
```

## 快速启用

### 1. 添加Maven依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-jwt-security-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

### 2. 配置 application.yml

项目提供了多环境配置文件，可以根据需要选择使用：

**方式一：直接在 application.yml 中配置（简单场景）**

```yaml
server:
  port: 8801
structure:
  security:
    enabled: true
    antMatchers:
      unAuthenticated:
        - /api/user/login
        - /doc.html
        - /webjars/**
  jwt:
    secret: your-secret-key-here  # JWT加密密钥
    jwtTokenValidity: 32400       # token有效期(秒)，默认9小时
```

**方式二：使用独立的安全配置文件（推荐）**

项目提供了完整的安全配置文件，位于 `resources/security/` 目录：

| 配置文件 | 说明 | 使用场景 |
|---------|------|----------|
| `security/application-security.yml` | 基础安全配置 | 开发/测试/生产基础模板 |
| `security/application-prod.yml` | 生产环境配置 | 生产环境部署 |
| `test/resources/application-test.yml` | 测试环境配置 | 单元测试/集成测试 |

**配置示例**（以 application-dev.yml 为例）：

```yaml
spring:
  config:
    import: classpath:security/application-security.yml

server:
  port: 8801
```

详细配置说明请参考：
- [基础安全配置](./structure-jwt-security-example/src/main/resources/security/application-security.yml)
- [生产环境配置](./structure-jwt-security-example/src/main/resources/security/application-prod.yml)
- [测试环境配置](./structure-jwt-security-example/src/test/resources/application-test.yml)

### 3. 实现用户服务

实现 `UserDetailsService` 接口或在配置类中注入自定义实现：

```java
@Service
public class UserServiceImpl implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户逻辑，返回 StructureAuthUser
        StructureAuthUser user = new StructureAuthUser();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEnable(true);
        user.setUnlocked(true);
        user.setUnexpired(true);
        
        // 必须设置 authorities，否则会出现空指针异常
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        return user;
    }
}
```

### 4. 启动类配置

```java
@SpringBootApplication
public class JwtExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtExampleApplication.class, args);
    }
}
```

## 快速使用

### 登录获取Token

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

### 访问受保护资源

在请求头中添加Token：

```bash
GET /api/protected/resource
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 登出

```bash
POST /api/user/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 配置说明

### Security 框架配置项

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `structure.security.enabled` | 是否启用安全框架 | true | ❌ |
| `structure.security.default-login-url` | 默认登录页面 | - | ❌ |
| `structure.security.corsFilterClass` | CORS过滤器类 | 内置类 | ❌ |
| `structure.security.antMatchers` | 路径权限配置 | - | ❌ |

### JWT 认证配置项

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `structure.jwt.secret` | JWT加密密钥，建议32位以上 | JWT | ✅ |
| `structure.jwt.jwtTokenValidity` | Token有效期(秒) | 32400(9小时) | ❌ |

### Ant 风格路径匹配说明

| 符号 | 说明 | 示例 |
|------|------|------|
| `?` | 匹配单个字符 | `/api/user?` 匹配 `/api/user1`, `/api/userA` |
| `*` | 匹配零个或多个路径段 | `/api/*` 匹配 `/api/user`, `/api/user/123` |
| `**` | 匹配零个或多个路径段（包含子目录） | `/**/login` 匹配 `/login`, `/api/login`, `/api/v1/login` |

路径权限配置在 `structure.security.antMatchers` 下：

| 配置项 | 说明 |
|--------|------|
| `unAuthenticated` | 不需要认证即可访问的路径列表 |

### 多环境配置

项目支持多环境配置，通过 `spring.profiles.active` 指定环境：

```bash
# 开发环境
java -jar app.jar --spring.profiles.active=dev

# 测试环境
java -jar app.jar --spring.profiles.active=test

# 生产环境
java -jar app.jar --spring.profiles.active=prod
```

### 核心接口

- **ITokenService**: Token生成与解析服务
- **ITokenStore**: Token存储服务
- **ICorsFilter**: CORS跨域过滤器接口

### StructureAuthUser 用户实体

实现 `UserDetails` 接口的用户实体，包含以下属性：

| 属性 | 说明 |
|------|------|
| `id` | 用户ID |
| `username` | 用户名 |
| `password` | 密码 |
| `enable` | 是否启用 |
| `unlocked` | 是否锁定 |
| `unexpired` | 是否过期 |
| `authorities` | 用户权限集合 |

## 开发者维护

### 本地构建

```bash
# 安装依赖并构建
cd structure-security-dependencies
mvn clean install -Dmaven.test.skip=true -Drevision=1.1.0

# 或使用脚本
./scripts/install.sh 1.1.0
```

### 项目结构

```
structure-jwt-security-starter/src/main/java/cn/structure/starter/jwt/
├── configuration/    # 自动配置类
│   ├── AutoConfiguration.java
│   ├── CorsFilter.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtRequestFilter.java
│   └── WebSecurityConfig.java
├── dto/              # 数据传输对象
├── endpoint/        # 登录登出端点
├── enums/           # 枚举类
├── filter/          # 过滤器
├── interfaces/      # 核心接口
├── properties/      # 配置属性类
└── service/         # 服务实现
```

### 发布到Maven仓库

```bash
# 更新版本号后执行
./scripts/release.sh
```

## License

Apache License 2.0

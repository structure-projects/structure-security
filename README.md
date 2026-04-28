# structure-security

一款基于 Spring Security 的 JWT 安全认证starter，简化 Spring Boot 项目的 JWT 安全认证开发。

## 技术栈

| 技术 | 说明 | 版本 |
|------|------|------|
| Spring Boot | Web框架 | 2.7.18 |
| Spring Security | 安全框架 | 5.7.x |
| JJWT | JWT库 | 0.12.7 |
| Java | 开发语言 | 8+ |
| Maven | 项目构建 | 3.x |

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
    <version>1.0.3</version>
</dependency>
```

### 2. 配置application.yml

```yaml
server:
  port: 8801
structure:
  jwt:
    secret: your-secret-key-here  # JWT加密密钥
    jwtTokenValidity: 32400       # token有效期(秒)，默认9小时
    antMatchers:
      unAuthenticated:           # 不需要认证的接口
        - /api/user/login
        - /doc.html
        - /webjars/**
        - /swagger-resources/**
        - /v2/api-docs/**
```

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

### JwtConfig 配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `structure.jwt.secret` | JWT加密密钥 | JWT |
| `structure.jwt.jwtTokenValidity` | Token有效期(秒) | 32400(9小时) |
| `structure.jwt.antMatchers.unAuthenticated` | 无需认证的接口列表 | - |

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

## 开发者维护

### 本地构建

```bash
# 安装依赖并构建
cd structure-security-dependencies
mvn clean install -Dmaven.test.skip=true -Drevision=1.0.3

# 或使用脚本
./scripts/install.sh 1.0.3
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

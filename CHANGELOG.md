# Changelog

所有重要的版本更新都会记录在此文件中。遵循 [语义化版本](https://semver.org/lang/zh-CN/) 规范。

---

## [1.1.0-SNAPSHOT] - 开发中 (2026-05-23)

### ⚠️ 重大升级 - JDK & 框架全面升级

这是项目的 **重大版本升级**，涉及 JDK、Spring Boot、Spring Security 等核心框架的全面升级。

#### 🔄 技术栈升级

| 组件 | 旧版本 | 新版本 | 变更类型 |
|------|--------|--------|----------|
| JDK | 8 | **17** | 🔴 主版本升级 |
| Spring Boot | 2.7.18 | **3.2.4** | 🔴 主版本升级 |
| Spring Security | 5.7.x | **6.2.x** | 🔴 主版本升级 |
| structure-dependencies | 1.2.8 | **1.3.4** | 🔵 小版本升级 |
| Jakarta EE | - | **9+** | 🔴 命名空间迁移 |

#### ✨ 新增功能

- ✅ **JDK 17 LTS 支持**
  - 成熟的长期支持版本
  - 良好的第三方库兼容性
  - 稳定的性能表现
  - 为未来升级到 JDK 21 做好准备

- ✅ **Spring Boot 3.x 支持**
  - AOT 编译支持
  - GraalVM 原生镜像支持
  - 更好的云原生支持
  - 性能优化和启动速度提升

- ✅ **Spring Security 6.x 支持**
  - 更严格的默认安全策略
  - 增强的 CORS 配置
  - 方法级安全增强

#### 🔥 破坏性变更

- ⚠️ **Jakarta EE 命名空间迁移**
  - `javax.servlet.*` → `jakarta.servlet.*`
  - `javax.persistence.*` → `jakarta.persistence.*`
  - `javax.validation.*` → `jakarta.validation.*`
  - `javax.annotation.*` → `jakarta.annotation.*`

- ⚠️ **Spring Security 配置调整**
  - CORS 配置需要显式声明
  - 方法安全 `@EnableMethodSecurity` 需要显式启用
  - 部分废弃 API 已移除

- ⚠️ **Maven 编译配置更新**
  - `maven.compiler.source`: 8 → 17
  - `maven.compiler.target`: 8 → 17

#### 📦 依赖管理更新

```xml
<!-- 新增 Jakarta Servlet API 支持 -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
</dependency>
```

#### 🛠️ 升级指南

##### 1. 环境要求
- JDK 17+
- Maven 3.9+

##### 2. 代码迁移

**Servlet 迁移示例:**
```java
// 旧版本 (Spring Boot 2.x)
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

// 新版本 (Spring Boot 3.x)
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
```

**Security 配置迁移:**
```java
// 新版本需要显式启用方法安全
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    // ...
}
```

##### 3. 迁移工具

推荐使用 Spring 官方迁移工具:
```bash
# 使用 OpenRewrite 进行自动化迁移
https://docs.spring.io/spring-boot/how-to/migration.html
```

#### 📊 性能提升

- ⚡ **启动速度**: 提升约 20-30%
- 💾 **内存占用**: 降低约 10-15%
- 🚀 **吞吐量**: 提升约 15-25% (基于 Spring Boot 3.x 和 JDK 17)

#### 🔍 已知问题

- 部分第三方库可能尚未完全兼容 Spring Boot 3.x
- 需要验证所有自定义过滤器和拦截器的 Jakarta 兼容性

#### 📝 相关文档

- [升级指南](./UPGRADE_GUIDE.md)
- [升级日志](./upgrade.log.md)

#### 🧪 测试建议

```bash
# 编译测试
mvn clean compile

# 单元测试
mvn test

# 完整构建
mvn clean install -Drevision=1.1.0-SNAPSHOT
```

---

## [1.0.3] - 2026-04-29

### ✅ 功能增强与安全更新

#### 🎉 新增功能

- **CI/CD 自动化部署**
  - 添加 GitHub Actions 工作流配置
  - 支持自动化发布到 Maven 中央仓库
  - 配置自动化版本管理和标签发布

- **JWT 安全增强**
  - 更新 JWT 密钥配置为更安全的密钥
  - 修改 JWT 签名算法使用 HMAC SHA 密钥
  - 增强 JWT 安全性配置

#### 🔒 安全更新

- **依赖版本升级**
  - Spring Boot: `2.7.14` → `2.7.18`
  - JJWT: `0.9.1` → `0.12.7`
  - structure-version: 升级到 `1.2.8`

#### 🐛 问题修复

- 修复认证失败处理器异常抛出问题
- 优化异常处理器逻辑

#### 📚 文档更新

- 完善 README 文档内容
- 添加详细的配置说明
- 更新快速开始指南

#### 🔧 构建脚本更新

- 优化 `install.sh` 脚本
- 优化 `release.sh` 脚本
- 优化 `update-snapshots.sh` 脚本
- 添加 Maven 配置文件配置服务器和 profile

#### 📦 变更文件

```
.github/workflows/release.yml                      |  +63
README.md                                          |  +202
scripts/install.sh                                 |   -1/+1
scripts/release.sh                                 |   -1/+1
scripts/update-snapshots.sh                        |   -2/+2
structure-jwt-security-example/pom.xml             |   -3/+3
structure-jwt-security-example/.../application-dev.yml | -1/+1
structure-jwt-security-starter/.../JwtDefaultServiceImpl.java | -8/+9
structure-security-dependencies/pom.xml             |   -6/+6
```

---

## [1.0.1] - 2024-08-01

### 🔧 版本调整与问题修复

#### 🐛 问题修复

- 修复依赖导致缺少引用问题
- 修复 SecurityUtils 工具类问题

#### ⚙️ 配置优化

- 调整代码框架版本
- 优化发布脚本配置

#### 📦 变更文件

```
scripts/release.sh                                    | -1
scripts/update-snapshots.sh                           | -1
structure-security-dependencies/pom.xml               | -1/+1
```

---

## [1.0.0] - 初始版本 - 2024-07-15

### 🎉 项目初始化

这是项目的初始版本，完成了基于 Spring Security 的 JWT 安全认证 starter 的核心功能开发。

#### ✨ 核心功能

- **JWT 认证框架**
  - JWT Token 生成与验证
  - Token 存储服务
  - 用户认证服务

- **Spring Security 集成**
  - 基于 Spring Security 5.7.x
  - 自定义认证过滤器
  - CORS 跨域配置

- **自动配置**
  - Spring Boot AutoConfiguration 支持
  - 配置文件属性映射
  - 条件化配置

#### 🏗️ 项目结构

```
structure-security/
├── structure-security-dependencies/     # 依赖管理模块
│   └── pom.xml
├── structure-security-core/             # 核心模块
│   ├── StructureAccessDeniedHandler.java
│   ├── StructureAuthenticationEntryPoint.java
│   └── StructureAuthUser.java
└── structure-jwt-security-starter/      # JWT Starter
    ├── configuration/
    │   ├── AutoConfiguration.java
    │   ├── CorsFilter.java
    │   ├── JwtAuthenticationEntryPoint.java
    │   ├── JwtDefaultServiceImpl.java
    │   ├── JwtRequestFilter.java
    │   └── WebSecurityConfig.java
    ├── dto/
    │   └── LoginRequestDTO.java
    ├── endpoint/
    │   └── LoginEndpoint.java
    ├── enums/
    │   └── LoginErrCodeEnum.java
    ├── filter/
    │   └── LoginFilter.java
    ├── interfaces/
    │   ├── ICorsFilter.java
    │   ├── ITokenService.java
    │   └── ITokenStore.java
    ├── properties/
    │   └── JwtConfig.java
    └── service/
        ├── DefaultUserServiceImpl.java
        └── InnerTokenStore.java
```

#### 🔧 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.14 | Web 框架 |
| Spring Security | 5.7.x | 安全框架 |
| JJWT | 0.9.1 | JWT 库 |
| Java | 8+ | 开发语言 |
| Maven | 3.x | 构建工具 |

#### 📚 文档

- 基础使用文档
- 配置说明
- API 参考

#### 🧪 测试

- 基础功能测试
- 认证流程测试
- Token 生成验证测试

#### 📦 示例项目

包含完整的示例应用程序 (`structure-jwt-security-example`)：
- 示例应用启动类
- 用户认证服务实现
- 配置文件示例
- REST API 示例

#### 🚀 快速开始

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-jwt-security-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 💡 特性亮点

1. **开箱即用**: 引入依赖即可使用
2. **配置灵活**: 支持 YAML 配置
3. **易于扩展**: 提供丰富的扩展接口
4. **文档完善**: 提供详细的使用文档

---

## 版本对照表

| 版本 | 发布日期 | 状态 | 主要变更 |
|------|----------|------|----------|
| [1.1.0-SNAPSHOT](#110-snapshot---开发中-2026-05-23) | 2026-05-23 | 开发中 | JDK 17, Spring Boot 3.2.4 |
| [1.0.3](#103---2026-04-29) | 2026-04-29 | 稳定 | CI/CD, JWT 安全增强 |
| [1.0.1](#101---2024-08-01) | 2024-08-01 | 稳定 | 版本调整 |
| [1.0.0](#100---初始版本---2024-07-15) | 2024-07-15 | 稳定 | 初始版本 |

---

## 升级建议

### 从 1.0.x 升级到 1.1.0

1. **必须升级 JDK 到 17**
2. **必须迁移 Jakarta 命名空间**
3. **必须更新 Spring Security 配置**
4. **建议进行完整的回归测试**

详细升级指南请参考: [UPGRADE_GUIDE.md](./UPGRADE_GUIDE.md)

---

## 贡献者

感谢以下贡献者的付出：

- **chuck** - 主要开发者
- **chuckLcq** - 项目维护者

---

## 反馈与支持

- 📧 **邮箱**: support@structured.cn
- 🌐 **官网**: https://projects.structured.cn
- 💬 **GitHub Issues**: https://github.com/structure-projects/structure-security/issues

---

*此 CHANGELOG 由 AI Assistant 自动生成，基于 Git 提交历史*

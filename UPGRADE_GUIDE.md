# structure-security 升级指南

## 📋 版本升级日志

### 升级时间
**2026-05-23**

### 升级版本
- **structure-dependencies**: 1.2.8 → 1.3.4
- **structure.version**: 1.2.8 → 1.3.4
- **Spring Boot**: 2.7.18 → 3.2.4
- **Spring Security**: 5.7.x → 6.2.x
- **JDK**: 8 → 21
- **Maven**: 3.x → 3.9+

---

## 🔄 详细升级内容

### 1. **structure-security-dependencies/pom.xml**

#### 变更项：

| 配置项 | 旧版本 | 新版本 | 说明 |
|--------|--------|--------|------|
| Parent Version | 1.2.8 | 1.3.4 | 升级父依赖版本 |
| structure.version | 1.2.8 | 1.3.4 | 结构版本号 |
| maven.compiler.source | 8 | 21 | Java源码编译版本 |
| maven.compiler.target | 8 | 21 | Java目标运行版本 |
| jakarta.servlet-api | ✗ | 已添加 | 新增Jakarta Servlet支持 |
| lombok | ✗ | 已添加 | 新增Lombok依赖管理 |

**关键配置示例：**

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.3.4</version>
</parent>

<properties>
    <structure.version>1.3.4</structure.version>
    <jjwt.version>0.12.7</jjwt.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```

---

### 2. **structure-security-core/pom.xml**

#### 变更项：

| 配置项 | 旧版本 | 新版本 | 说明 |
|--------|--------|--------|------|
| maven.compiler.source | 8 | 21 | Java源码编译版本 |
| maven.compiler.target | 8 | 21 | Java目标运行版本 |

**关键配置示例：**

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

---

### 3. **structure-jwt-security-starter/pom.xml**

#### 变更项：

| 配置项 | 旧版本 | 新版本 | 说明 |
|--------|--------|--------|------|
| javax.servlet | javax.servlet-api | jakarta.servlet-api | Jakarta EE 9+ 命名空间迁移 |

**迁移说明：**

Spring Boot 3.x 基于 Jakarta EE 9+ (EE 8 的后继版本)，需要将 `javax.*` 命名空间迁移到 `jakarta.*`。

**变更示例：**

```xml
<!-- 旧版本 (Spring Boot 2.x) -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
</dependency>

<!-- 新版本 (Spring Boot 3.x) -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
</dependency>
```

---

### 4. **structure-jwt-security-example/pom.xml**

#### 变更项：

| 配置项 | 旧版本 | 新版本 | 说明 |
|--------|--------|--------|------|
| spring-boot-parent | 2.7.18 | 3.2.4 | Spring Boot主版本升级 |
| structure.version | 1.2.8 | 1.3.4 | 结构版本号 |
| maven-compiler-plugin source | 8 | 17 | Java源码编译版本 |
| maven-compiler-plugin target | 8 | 17 | Java目标运行版本 |
| dependencyManagement位置 | 依赖后面 | 依赖前面 | POM结构优化 |

**关键配置示例：**

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-parent</artifactId>
    <version>3.2.4</version>
</parent>

<properties>
    <structure.version>1.3.4</structure.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-boot-parent</artifactId>
            <version>${structure.version}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## ⚠️ 重要变更说明

### 1. Jakarta EE 命名空间迁移

从 Spring Boot 2.x 升级到 3.x 后，所有 `javax.*` 命名空间的类都需要迁移到 `jakarta.*` 命名空间。

**常见需要修改的类：**

| 旧包名 (javax) | 新包名 (jakarta) |
|----------------|------------------|
| `javax.servlet.*` | `jakarta.servlet.*` |
| `javax.persistence.*` | `jakarta.persistence.*` |
| `javax.validation.*` | `jakarta.validation.*` |
| `javax.annotation.*` | `jakarta.annotation.*` |

### 2. Spring Security 6.x 变更

Spring Security 6.x 相比 5.x 有以下主要变更：

- **弃用警告**: 许多之前 `@Deprecated` 的方法已被移除
- **HTTP/2 支持**: 默认启用，更好的性能
- **CORS 配置**: 配置方式更加严格
- **方法安全**: `@EnableMethodSecurity` 需要显式启用

### 3. JDK 17 LTS 支持

升级到 JDK 17 后，获得稳定的长期支持版本：

- **成熟稳定**: 经过多个版本迭代，稳定可靠
- **LTS 支持**: 长期支持版本，商业支持有保障
- **良好兼容性**: 第三方库和框架的最佳兼容性
- **性能优化**: 持续的性能改进和优化

---

## 🔧 构建和测试

### 本地构建

```bash
# 清理并构建所有模块
cd structure-security-dependencies
mvn clean install -Dmaven.test.skip=true -Drevision=1.1.0-SNAPSHOT

# 验证构建
mvn clean verify -Drevision=1.1.0-SNAPSHOT
```

### 依赖检查

```bash
# 查看依赖树
mvn dependency:tree

# 检查依赖冲突
mvn dependency:analyze
```

---

## 📊 依赖版本汇总

### 父依赖管理

| 依赖 | 版本 | 作用域 |
|------|------|--------|
| structure-common | 1.3.4 | compile |
| structure-security-core | ${revision} | compile |
| jjwt | 0.12.7 | compile |
| jakarta.servlet-api | (由parent管理) | provided |
| lombok | (由parent管理) | compile |

### 子模块依赖

| 模块 | 主要依赖 |
|------|----------|
| structure-security-core | spring-boot-starter-security, spring-security-web, structure-common |
| structure-jwt-security-starter | spring-boot-autoconfigure, jakarta.servlet-api, jjwt |
| structure-jwt-security-example | spring-boot-starter-web, structure-common |

---

## 🐛 已知问题和解决方案

### 1. IDE 不识别 JDK 17

**问题**: IDE 显示 "Java 17 is not supported"

**解决方案**:
```bash
# 确保本地安装了 JDK 17
java -version  # 应显示 17.x.x

# 在 IDE 中设置项目 SDK 为 JDK 17
# IntelliJ IDEA: File → Project Structure → Project → SDK
```

### 2. Maven 构建失败

**问题**: "Could not resolve dependencies"

**解决方案**:
```bash
# 清理 Maven 缓存
mvn dependency:purge-local-repository
mvn clean install -U
```

### 3. Jakarta 命名空间导入错误

**问题**: "The import javax.servlet cannot be resolved"

**解决方案**: 全局替换项目中所有 `javax.servlet` 为 `jakarta.servlet`

---

## ✅ 升级检查清单

在完成升级后，请确认以下项目：

- [ ] 所有模块成功编译无错误
- [ ] 所有单元测试通过
- [ ] 集成测试环境配置正确
- [ ] API 文档已更新
- [ ] 部署脚本已验证
- [ ] 监控和日志配置已验证
- [ ] 团队成员已完成本地环境升级

---

## 📚 相关文档

- [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Spring Security 6.0 Migration Guide](https://github.com/spring-projects/spring-security/wiki/Spring-Security-6.0-Migration-Guide)
- [Jakarta EE 9 Platform](https://jakarta.ee/specifications/platform/9/)
- [JDK 17 Release Notes](https://jdk.java.net/17/release-notes)

---

## 📞 支持

如有问题，请提交 Issue 到：[GitHub Issues](https://github.com/structure-projects/structure-security/issues)

---

## 🔄 版本历史

| 日期 | 版本 | 变更类型 | 主要内容 |
|------|------|----------|----------|
| 2026-05-23 | 1.1.0-SNAPSHOT | 重大升级 | JDK 17, Spring Boot 3.2.4, Spring Security 6.2.x |
| 2024-XX-XX | 1.0.2 | 功能更新 | (历史版本) |
| 2023-XX-XX | 1.0.1 | 缺陷修复 | (历史版本) |
| 2021-XX-XX | 1.0.0 | 初始版本 | 基础功能发布 |

---

**维护者**: structure team  
**最后更新**: 2026-05-23

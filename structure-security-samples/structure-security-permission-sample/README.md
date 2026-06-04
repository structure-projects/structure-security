# Permission Sample

这是一个权限管理的示例工程，演示了权限控制的使用。

## 功能特性

- 权限校验
- 上下文权限提供者
- 远程权限提供者（支持缓存）
- 完整的集成测试

## 快速开始

### 1. 运行应用

```bash
mvn spring-boot:run
```

或直接运行主类 `PermissionSampleApplication`。

### 2. 测试 API

#### 上下文权限模式

使用默认配置（上下文权限模式）：

```bash
curl http://localhost:8080/api/permission/test \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### 远程权限模式

使用 `application-remote.yml` 配置：

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=remote
```

## 配置说明

### 权限提供者类型

```yaml
structure:
  security:
    permission:
      provider-type: context  # 或 remote
```

### 远程权限模式配置

```yaml
structure:
  security:
    permission:
      provider-type: remote
      remote-url: http://localhost:8081/api/permissions/{userId}
      cache:
        enabled: true
        ttl: 1800
        max-size: 10000
```

## 项目依赖

```xml
<dependencies>
    <dependency>
        <groupId>cn.structured</groupId>
        <artifactId>structure-security-permission-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>cn.structured</groupId>
        <artifactId>structure-security-jwt-starter</artifactId>
    </dependency>
</dependencies>
```

## 运行测试

```bash
mvn test
```

## License

Apache License 2.0

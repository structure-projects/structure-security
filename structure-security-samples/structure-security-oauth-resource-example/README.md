# OAuth Resource Server Sample

这是一个 OAuth 2.0 资源服务器示例工程。

## 功能特性

- OAuth 2.0 资源访问控制
- JWT Token 验证
- 公钥配置支持

## 快速开始

### 1. 运行应用

```bash
mvn spring-boot:run
```

或直接运行主类 `ResourceApplication`。

### 2. 配置说明

在 `application.yml` 中配置公钥位置：

```yaml
structure:
  security:
    oauth:
      resource:
        jwt:
          public-key-location: classpath:public.cert
```

## 项目依赖

```xml
<dependencies>
    <dependency>
        <groupId>cn.structured</groupId>
        <artifactId>structure-security-oauth-resource-starter</artifactId>
    </dependency>
</dependencies>
```

## License

Apache License 2.0

# Structure Security Core

安全核心模块，提供基础的安全实体、工具类和接口定义。

## 功能特性

- 用户认证实体 `StructureAuthUser`
- 安全工具类 `SecurityUtils`
- 异常处理配置 `StructureAccessDeniedHandler`
- 认证入口点 `StructureAuthenticationEntryPoint`
- 权限校验核心接口和工具

## 主要组件

### StructureAuthUser

用户信息实体，包含用户名、密码、权限等信息。

```java
StructureAuthUser user = new StructureAuthUser(
    "username",
    "password",
    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
);
```

### SecurityUtils

提供获取当前登录用户等常用方法。

```java
StructureAuthUser currentUser = SecurityUtils.getCurrentUser();
String username = SecurityUtils.getCurrentUsername();
boolean isAdmin = SecurityUtils.hasRole("ROLE_ADMIN");
```

## 项目依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-core</artifactId>
    <version>${revision}</version>
</dependency>
```

## License

Apache License 2.0

# Structure Security OAuth API

OAuth 2.0 相关的 API 定义和数据传输对象。

## 功能特性

- OAuth 2.0 相关的数据传输对象 (DTO)
- 业务对象 (BO) 定义
- 平台认证适配器接口
- 认证和授权相关的枚举定义

## 主要组件

### DTO 对象

- `LoginRequestDTO`: 登录请求对象
- `LoginResultDTO`: 登录结果对象
- `Oauth2TokenDTO`: OAuth 2.0 Token 对象

### 枚举定义

- `PlatformCodeEnum`: 平台类型枚举
- `SocialChannelEnum`: 社交渠道枚举
- `VerificationCodeType`: 验证码类型枚举
- `ErrAuthEnum`: 认证错误枚举

### 适配器接口

- `IPlatformAuthenticationAdapter`: 平台认证适配器接口

## 项目依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-oauth-api</artifactId>
    <version>${revision}</version>
</dependency>
```

## License

Apache License 2.0

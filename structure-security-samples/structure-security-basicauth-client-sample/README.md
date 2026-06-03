# Basic Auth Client Sample

Basic Auth 客户端示例工程

## 功能特点

- 演示如何使用 Basic Auth 发送标准请求
- Basic Auth 头的生成和解析
- 演示如何使用 WebClient 发送带 Basic Auth 的请求

## 快速开始

### 1. 运行应用

```bash
mvn spring-boot:run
```

### 2. 测试接口

#### 生成 Basic Auth 头

```bash
curl -X POST "http://localhost:8081/api/client/generate?username=admin&password=admin123"
```

#### 解析 Basic Auth 头

```bash
curl -X POST http://localhost:8081/api/client/parse \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

#### 查看使用演示

```bash
curl http://localhost:8081/api/client/demo
```

## 主要 API 概述

### BasicAuthGenerator

```java
// 生成 Basic Auth 头
String authHeader = BasicAuthGenerator.generate("admin", "admin123");

// 解析 Basic Auth 头
String[] credentials = BasicAuthGenerator.parse(authHeader);
```

### 标准请求格式

```bash
# 方式 1: 使用 curl -u
curl -u admin:admin123 http://your-server/api/endpoint

# 方式 2: 直接设置 Header
curl http://your-server/api/endpoint \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## 项目依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-basicauth-client</artifactId>
</dependency>
```

## License

Apache License 2.0

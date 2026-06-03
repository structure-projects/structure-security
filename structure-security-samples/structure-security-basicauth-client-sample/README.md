# Basic Auth Client 示例

演示作为 client 如何调用需要 Basic Auth 的 server

## 功能特点

- 使用 RestTemplate 调用需要 Basic Auth 认证的服务端
- 演示如何生成和使用 Basic Auth header

## 快速开始

### 1. 启动服务端

首先需要启动 Basic Auth Server 示例：

```bash
cd ../structure-security-basicauth-server-sample
mvn spring-boot:run
```

### 2. 启动客户端

```bash
mvn spring-boot:run
```

### 3. 测试接口

#### 查看演示信息

```bash
curl http://localhost:8081/api/client/demo
```

#### 使用 Basic Auth 调用服务端

```bash
curl -X POST "http://localhost:8081/api/client/call?username=admin&password=admin123"
```

## 核心代码说明

### BasicAuthClientService 使用 RestTemplate 发送带 Basic Auth 的请求：

```java
String authHeader = BasicAuthGenerator.generate(username, password);

HttpHeaders headers = new HttpHeaders();
headers.set(HttpHeaders.AUTHORIZATION, authHeader);

ResponseEntity<Map> response = restTemplate.exchange(
    targetUrl,
    HttpMethod.GET,
    new HttpEntity<>(headers),
    Map.class
);
```

## 直接 curl 命令

你也可以直接用 curl 测试服务端：

```bash
# 使用 -u 参数
curl -u admin:admin123 http://localhost:8082/api/protected/hello

# 使用 -H 参数直接设置 Authorization 头
curl http://localhost:8082/api/protected/hello -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## License

Apache License 2.0

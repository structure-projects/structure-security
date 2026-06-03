# Structure Security Basic Auth Client

Basic Auth 客户端模块，用于生成 Base64 编码的 Basic Authentication 请求头。

## 功能特性

- 生成 Basic Auth 认证头
- 解析 Basic Auth 认证头
- Spring Boot 自动配置支持
- 配置属性支持

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-basicauth-client</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置 (可选)

```yaml
structure:
  security:
    basicauth:
      client:
        enabled: true
        username: admin
        password: secret
```

### 3. 使用方式

#### 方式一：使用 BasicAuthGenerator（静态方法）

```java
import cn.structured.security.basicauth.client.BasicAuthGenerator;

// 生成认证头
String authHeader = BasicAuthGenerator.generate("admin", "secret");
// 结果: Basic YWRtaW46c2VjcmV0

// 解析认证头
String[] credentials = BasicAuthGenerator.parse("Basic YWRtaW46c2VjcmV0");
String username = credentials[0]; // admin
String password = credentials[1]; // secret
```

#### 方式二：使用 BasicAuthService（Spring Bean）

```java
import cn.structured.security.basicauth.client.service.BasicAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Autowired
    private BasicAuthService basicAuthService;

    public void callExternalApi() {
        // 使用配置的默认用户名和密码
        String authHeader = basicAuthService.generateAuthHeader();
        
        // 或使用指定的用户名和密码
        String customHeader = basicAuthService.generateAuthHeader("user", "password");
    }
}
```

#### 方式三：配合 RestTemplate 使用

```java
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ApiClient {

    @Autowired
    private BasicAuthService basicAuthService;
    
    @Autowired
    private RestTemplate restTemplate;

    public String callApi(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuthService.generateAuthHeader());
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }
}
```

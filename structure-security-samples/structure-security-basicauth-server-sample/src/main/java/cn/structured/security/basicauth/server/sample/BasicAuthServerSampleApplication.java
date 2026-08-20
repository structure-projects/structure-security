/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structured.security.basicauth.server.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Basic Auth 服务端示例应用
 * 演示如何集成 Basic Auth Server 和 JWT
 *
 * @author chuck
 */
@Slf4j
@SpringBootApplication
public class BasicAuthServerSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthServerSampleApplication.class, args);
        
        log.info("Basic Auth Server Sample Application Started!");
        log.info("Test with: curl -u admin:admin123 http://localhost:8082/api/protected/hello");
    }
}

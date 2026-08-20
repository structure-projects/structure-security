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

package cn.structure.example.jwt.endpoint;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {
    @RequestMapping("/hello")
    public ResResultVO<String> hello() {
        return ResultUtilSimpleImpl.success("hello world");
    }

    @RequestMapping("/hello2")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResResultVO<String> hello2() {
        return ResultUtilSimpleImpl.success("hello world");
    }

    @RequestMapping("/hello3")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResResultVO<String> hello3() {
        return ResultUtilSimpleImpl.success("hello world from super admin");
    }

}

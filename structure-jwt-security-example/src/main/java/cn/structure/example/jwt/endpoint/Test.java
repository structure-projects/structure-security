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

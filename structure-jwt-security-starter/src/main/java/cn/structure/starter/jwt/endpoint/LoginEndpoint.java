package cn.structure.starter.jwt.endpoint;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.exception.CommonException;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.starter.jwt.dto.LoginRequestDTO;
import cn.structure.starter.jwt.enums.LoginErrCodeEnum;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.interfaces.ITokenStore;
import cn.structured.security.entity.StructureAuthUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 登录逻辑
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 10:53
 */
@RestController
@Api(tags = "登录模块")
@RequestMapping(value = "/api/user")
public class LoginEndpoint {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private ITokenStore tokenStore;

    @Resource
    private ITokenService tokenService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录请求")
    public ResResultVO<String> login(@Validated @RequestBody LoginRequestDTO loginDto) {
        UsernamePasswordAuthenticationToken params = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(params);
            Object principal = authenticate.getPrincipal();
            StructureAuthUser user = (StructureAuthUser) principal;
            String token = tokenStore.setUser(user);
            return ResultUtilSimpleImpl.success(token);
        } catch (CommonException ce) {
            return ResultUtilSimpleImpl.fail(ce.getCode(), ce.getMessage(), null);
        } catch (BadCredentialsException be) {
            return ResultUtilSimpleImpl.fail(LoginErrCodeEnum.USER_PASSWORD_ERR.getCode(), LoginErrCodeEnum.USER_PASSWORD_ERR.getMsg(), null);
        } catch (DisabledException de) {
            return ResultUtilSimpleImpl.fail(LoginErrCodeEnum.USER_DISABLED.getCode(), LoginErrCodeEnum.USER_DISABLED.getMsg(), null);
        } catch (LockedException ce) {
            return ResultUtilSimpleImpl.fail(LoginErrCodeEnum.USER_LOCKED_ERR.getCode(), LoginErrCodeEnum.USER_LOCKED_ERR.getMsg(), null);
        }
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "登出")
    public ResResultVO<Void> logout(HttpServletRequest request) {
        String token = tokenService.getToken(request);
        tokenStore.clearStore(token);
        return ResultUtilSimpleImpl.success(null);
    }


}

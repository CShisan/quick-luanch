package com.quick.auth.controller;

import com.quick.auth.entity.CaptchaEntity;
import com.quick.auth.entity.RefreshTokenEntity;
import com.quick.auth.service.AuthService;
import com.quick.common.base.Result;
import com.quick.common.handler.ResultHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CShisan
 */
@Tag(name = "【通用】认证接口", description = "AuthController")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/refresh-token")
    @Operation(summary = "刷新token")
    public Result<RefreshTokenEntity> refreshToken(@RequestParam("token") String token) {
        return ResultHandler.ok(authService.refreshToken(token));
    }

    @GetMapping("/captcha")
    @Operation(summary = "获取验证码")
    public Result<CaptchaEntity> captcha() {
        return ResultHandler.ok(authService.getCaptcha());
    }

}

package com.quick.auth.service;

import com.quick.auth.entity.CaptchaEntity;
import com.quick.auth.entity.LoginEntity;
import com.quick.auth.entity.RefreshTokenEntity;
import com.quick.auth.security.datasource.UserDetailsService;
import com.quick.auth.utils.JwtUtil;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author CShisan
 */
@Service
public class AuthService {
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final EnvUtil envUtil;

    @Autowired
    public AuthService(RedisUtil redisUtil, JwtUtil jwtUtil, UserDetailsService userDetailsService, EnvUtil envUtil) {
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.envUtil = envUtil;
    }

    /**
     * 刷新token
     *
     * @param token token
     * @return token
     */
    public RefreshTokenEntity refreshToken(String token) {
        // 校验token
        boolean validToken = jwtUtil.validAndNotExp(jwtUtil.refreshTokenKey(), token);
        ExceptionHandler.assertTrue(validToken, CodeEnum.USER_AUTH_TOKEN_REFRESH_FAIL);

        // 校验ip
        String ip = jwtUtil.getClaimsByToken(token).get("ip", String.class);
        ExceptionHandler.assertTrue(IpUtil.validate(ip), CodeEnum.USER_AUTH_TOKEN_REFRESH_FAIL);

        // 获取用户信息
        Long uid = jwtUtil.getClaimsByToken(token).get("uid", Long.class);
        ExceptionHandler.requireNotNull(uid);
        LoginEntity entity = (LoginEntity) userDetailsService.loadUserByUid(uid);
        entity.setPassword("");

        // 构造新token并返回
        jwtUtil.deleteToken(entity.getUid());
        Map<String, Object> result = jwtUtil.putCacheToken(entity);
        result.put("entity", entity);
        return JsonUtil.e2e(result, RefreshTokenEntity.class);
    }

    /**
     * 获取验证码
     *
     * @return captcha
     */
    public CaptchaEntity getCaptcha() {
        CaptchaEntity captcha = JsonUtil.e2e(CaptchaUtil.getCaptcha(), CaptchaEntity.class);
        // 非生产直接赋值123
        StringBuilder keyBuilder = new StringBuilder(captcha.getCaptchaSign());
        envUtil.executeDev(() -> keyBuilder.replace(0, keyBuilder.length(), "123"));
        StringBuilder codeBuilder = new StringBuilder(captcha.getValidCode());
        envUtil.executeDev(() -> codeBuilder.replace(0, codeBuilder.length(), "123"));
        redisUtil.set(CaptchaEntity.getKey(keyBuilder.toString()), codeBuilder.toString(), 5L, TimeUnit.MINUTES);
        // 生产环境则置空验证码字段返回
        envUtil.executeProd(() -> captcha.setValidCode(null));
        return captcha;
    }
}

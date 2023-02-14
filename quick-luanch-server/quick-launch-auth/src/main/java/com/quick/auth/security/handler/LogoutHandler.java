package com.quick.auth.security.handler;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.utils.JwtUtil;
import com.quick.common.enums.CodeEnum;
import com.quick.common.handler.ResultHandler;
import com.quick.common.utils.HttpUtil;
import com.quick.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功处理类
 *
 * @author Administrator
 */
@Component
public class LogoutHandler implements LogoutSuccessHandler {
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Autowired
    public LogoutHandler(JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        // 删除redis缓存
        LoginEntity entity = (LoginEntity) authentication.getPrincipal();
        String tokenKey = jwtUtil.fullTokenKey(entity.getUid());
        String refreshTokenKey = jwtUtil.fullRefreshTokenKey(entity.getUid());
        redisUtil.batchDelete(tokenKey, refreshTokenKey);
        HttpUtil.writeResponse(httpServletResponse, ResultHandler.ok(CodeEnum.OK));
    }
}

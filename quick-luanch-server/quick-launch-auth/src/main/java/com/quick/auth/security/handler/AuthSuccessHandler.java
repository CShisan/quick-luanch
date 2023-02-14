package com.quick.auth.security.handler;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.utils.JwtUtil;
import com.quick.common.handler.ResultHandler;
import com.quick.common.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CShisan
 */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 获取security上下文中的用户登录对象
        LoginEntity entity = (LoginEntity) authentication.getPrincipal();

        // 获取缓存中的token,没有则创建并更新到缓存
        HttpUtil.writeResponse(response, ResultHandler.ok(jwtUtil.putCacheToken(entity)));
    }
}

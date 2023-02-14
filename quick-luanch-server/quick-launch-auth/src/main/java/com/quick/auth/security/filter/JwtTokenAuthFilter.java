package com.quick.auth.security.filter;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.security.datasource.UserDetailsService;
import com.quick.auth.security.exception.AuthException;
import com.quick.auth.security.handler.AuthFailHandler;
import com.quick.auth.security.token.AuthToken;
import com.quick.auth.utils.JwtUtil;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author CShisan
 */
@Slf4j
@Component
public class JwtTokenAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthFailHandler authFailHandler;

    @Autowired
    public JwtTokenAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, AuthFailHandler authFailHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authFailHandler = authFailHandler;
    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        try {
            String token = request.getHeader(jwtUtil.config().getHeader());
            if (this.isValidToken(token)) {
                LoginEntity entity = JsonUtil.e2e(jwtUtil.getClaimsByToken(token), LoginEntity.class);
                // 获取UID以便MybatisPlus进行数据填充
                request.setAttribute("uid", entity.getUid());
                // token有效且auth为空时重新设置AuthToken
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (CheckUtil.isNull(auth)) {
                    entity = (LoginEntity) userDetailsService.loadUserByUid(entity.getUid());
                    AuthToken authToken = new AuthToken(entity, entity.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 统一拦截所有登录异常实现自定义返回
            CodeEnum code = CodeEnum.USER_AUTH_ERROR;
            String message = CodeEnum.USER_AUTH_ERROR.getMessage();
            if (e instanceof ServiceException) {
                ServiceException exception = (ServiceException) e;
                code = exception.getCodeEnum();
                message = exception.getMessage();
            }
            AuthException exception = new AuthException(code, message, e);
            this.unsuccessfulAuthentication(request, response, exception);
        }
    }

    /**
     * 校验token
     */
    private boolean isValidToken(String token) {
        String prefix = jwtUtil.tokenValuePrefix();
        if (CheckUtil.isEmpty(token) || Objects.equals(token, prefix)) {
            return false;
        }
        return jwtUtil.validAndNotExp(jwtUtil.tokenKey(), token);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 自定义认证错误异常则清除上下文
        if (failed instanceof AuthException) {
            SecurityContextHolder.clearContext();
        }
        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, failed);
        authFailHandler.onAuthenticationFailure(request, response, failed);
    }
}

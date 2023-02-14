package com.quick.auth.security.handler;

import com.quick.auth.security.exception.AuthException;
import com.quick.common.enums.CodeEnum;
import com.quick.common.handler.LogHandler;
import com.quick.common.handler.ResultHandler;
import com.quick.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CShisan
 */
@Slf4j
@Component
public class AuthFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        // 若不是自定义认证异常则取默认错误码/错误信息
        CodeEnum codeEnum = CodeEnum.USER_AUTH_ERROR;
        String message = CodeEnum.USER_AUTH_ERROR.getMessage();
        String errorMessage = e.getMessage();
        StackTraceElement[] stackTraces = e.getStackTrace();
        if (e instanceof AuthException) {
            AuthException exception = (AuthException) e;
            codeEnum = exception.getCodeEnum();
            message = exception.getMessage();
            errorMessage = exception.getErrorMessage();
            stackTraces = exception.getThrowable().getStackTrace();
        }
        LogHandler.exception("认证异常", message, errorMessage, e.getStackTrace()[0], stackTraces);
        HttpUtil.writeResponse(response, ResultHandler.fail(codeEnum.getCode(), message));
    }
}

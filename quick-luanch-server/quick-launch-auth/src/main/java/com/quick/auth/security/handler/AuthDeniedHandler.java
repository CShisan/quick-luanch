package com.quick.auth.security.handler;

import com.quick.auth.security.exception.AuthDeniedException;
import com.quick.common.enums.CodeEnum;
import com.quick.common.handler.LogHandler;
import com.quick.common.handler.ResultHandler;
import com.quick.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CShisan
 */
@Slf4j
public class AuthDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        // 若不是自定义认证异常则取默认错误码/错误信息
        CodeEnum codeEnum = CodeEnum.USER_AUTH_ACCESS_DENIED;
        String message = CodeEnum.USER_AUTH_ACCESS_DENIED.getMessage();
        String errorMessage = e.getMessage();
        StackTraceElement[] stackTraces = e.getStackTrace();
        if (e instanceof AuthDeniedException) {
            AuthDeniedException exception = (AuthDeniedException) e;
            codeEnum = exception.getCodeEnum();
            message = exception.getMessage();
            errorMessage = exception.getErrorMessage();
            stackTraces = exception.getThrowable().getStackTrace();
        }
        LogHandler.exception("授权异常", message, errorMessage, e.getStackTrace()[0], stackTraces);
        HttpUtil.writeResponse(response, ResultHandler.fail(codeEnum.getCode(), message));
    }
}

package com.quick.common.exception;

import com.quick.common.base.Result;
import com.quick.common.enums.CodeEnum;
import com.quick.common.handler.LogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

/**
 * 全局异常处理类
 *
 * @author CShisan
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 业务异常处理
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public Result<Object> handleCustomException(ServiceException e) {
        Throwable throwable = e.getThrowable();
        StackTraceElement[] stackTraces = Optional.ofNullable(throwable).map(Throwable::getStackTrace).orElse(new StackTraceElement[0]);
        LogHandler.exception("业务异常", e.getMessage(), e.getErrorMessage(), e.getStackTrace()[0], stackTraces);
        Result<Object> result = new Result<>();
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());
        return result;
    }

    /**
     * 处理系统异常，均抛出 500
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleErrorException(Exception e) {
        StackTraceElement[] stackTraces = e.getStackTrace();
        LogHandler.exception("系统异常", CodeEnum.FAIL.getMessage(), e.getMessage(), e.getStackTrace()[0], stackTraces);
        Result<Object> result = new Result<>();
        result.setCode(CodeEnum.FAIL.getCode());
        result.setMessage(CodeEnum.FAIL.getMessage());
        return result;
    }

    /**
     * 参数验证错误
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handlerNotValidException(MethodArgumentNotValidException e) {
        // 修改不显示字段提示，保护实体信息
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = Optional.ofNullable(fieldError).map(FieldError::getDefaultMessage).orElse("参数格式错误");
        LogHandler.exception("参数异常", message, e.getMessage(), e.getStackTrace()[0], e.getStackTrace());
        Result<Object> result = new Result<>();
        result.setCode(CodeEnum.FAIL.getCode());
        result.setMessage(message);
        return result;
    }
}

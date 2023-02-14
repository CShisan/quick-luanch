package com.quick.common.exception;


import com.quick.common.enums.CodeEnum;

import java.util.Optional;

/**
 * 业务异常
 *
 * @author CShisan
 */
public class ServiceException extends RuntimeException {
    private final CodeEnum codeEnum;
    private final Integer code;
    private final String message;
    private final Throwable throwable;

    public ServiceException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
        this.throwable = new Throwable(codeEnum.getMessage());
    }

    public ServiceException(CodeEnum codeEnum, Throwable throwable) {
        super(throwable.getMessage());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
        this.throwable = throwable;
    }

    public ServiceException(CodeEnum codeEnum, String message) {
        super(message);
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = message;
        this.throwable = new Throwable(message);
    }

    public ServiceException(CodeEnum codeEnum, String message, Throwable throwable) {
        super(throwable.getMessage());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = message;
        this.throwable = throwable;
    }

    public ServiceException(CodeEnum codeEnum, String message, String errorMessage) {
        super(errorMessage);
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = message;
        this.throwable = new Throwable(errorMessage);
    }

    public CodeEnum getCodeEnum() {
        return codeEnum;
    }

    public Integer getCode() {
        return Optional.ofNullable(code).orElse(codeEnum.getCode());
    }

    @Override
    public String getMessage() {
        return Optional.ofNullable(message).orElse(codeEnum.getMessage());
    }

    public String getErrorMessage() {
        return super.getMessage();
    }

    public Throwable getThrowable() {
        return this.throwable;
    }
}

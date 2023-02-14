package com.quick.auth.security.exception;

import com.quick.common.enums.CodeEnum;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author yuanbai
 */
public class AuthDeniedException extends AccessDeniedException {
    private final CodeEnum codeEnum;
    private final int code;
    private final String message;
    private final Throwable throwable;

    public AuthDeniedException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
        this.throwable = new Throwable(codeEnum.getMessage());
    }

    public AuthDeniedException(CodeEnum codeEnum, Throwable throwable) {
        super(throwable.getMessage());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
        this.throwable = throwable;
    }

    public AuthDeniedException(CodeEnum codeEnum, String message) {
        super(message);
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = message;
        this.throwable = new Throwable(message);
    }

    public AuthDeniedException(CodeEnum codeEnum, String message, Throwable throwable) {
        super(throwable.getMessage());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = message;
        this.throwable = throwable;
    }

    public AuthDeniedException(CodeEnum codeEnum, String message, String errorMessage) {
        super(errorMessage);
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = message;
        this.throwable = new Throwable(errorMessage);
    }

    public CodeEnum getCodeEnum() {
        return this.codeEnum;
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getErrorMessage() {
        return super.getMessage();
    }

    public Throwable getThrowable() {
        return this.throwable;
    }
}

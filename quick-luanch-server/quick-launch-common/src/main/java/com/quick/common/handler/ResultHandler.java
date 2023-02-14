package com.quick.common.handler;


import com.quick.common.base.Result;
import com.quick.common.enums.CodeEnum;

/**
 * @author CShisan
 **/
public class ResultHandler {

    public static <T> Result<T> ok(T data, String msg, Integer code) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok(T data, CodeEnum resultEnum) {
        Result<T> r = new Result<>();
        r.setCode(resultEnum.getCode());
        r.setMessage(resultEnum.getMessage());
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok() {
        return ok(null, CodeEnum.OK);
    }

    public static <T> Result<T> ok(T data) {
        return ok(data, CodeEnum.OK);
    }

    public static <T> Result<T> ok(String msg) {
        Result<T> r = new Result<>();
        r.setCode(CodeEnum.OK.getCode());
        r.setMessage(msg);
        r.setData(null);
        return r;
    }

    public static <T> Result<T> ok(T data, String msg) {
        Result<T> r = new Result<>();
        r.setCode(CodeEnum.OK.getCode());
        r.setMessage(msg);
        r.setData(data);
        return r;
    }


    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(null);
        return r;
    }

    public static <T> Result<T> fail(String msg) {
        return fail(CodeEnum.FAIL.getCode(), msg);
    }

    public static <T> Result<T> fail(CodeEnum resultEnum) {
        return fail(resultEnum.getCode(), resultEnum.getMessage());
    }

    public static <T> Result<T> fail() {
        return fail(CodeEnum.FAIL.getCode(), CodeEnum.FAIL.getMessage());
    }

}

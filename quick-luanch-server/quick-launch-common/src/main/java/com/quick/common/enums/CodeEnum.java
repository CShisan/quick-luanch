package com.quick.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CShisan
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {
    /**
     * 系统状态code
     */
    OK(200, "操作成功"),
    NO(400, "操作失败"),
    PARAM_ERROR(403, "参数有误"),
    NOT_FOUND(404, "找不到资源"),
    FAIL(500, "系统异常,请稍后再试"),

    /**
     * 用户状态code
     * 认证：01 - 20
     * 用户信息：21 - 50
     */
    USER_AUTH_ERROR(10701, "认证失败"),
    USER_AUTH_ACCESS_DENIED(10702, "权限不足"),
    USER_AUTH_TOKEN_REFRESH_FAIL(10703, "Token刷新失败"),
    USER_WX_LOGIN_ERROR(10726, "微信登录失败"),
    USER_WX_ENCODE_ERROR(10727, "微信加密数据解密失败"),

    /**
     * 业务状态code
     * 数据：01-20
     */
    SERVICE_DATA_NULL(10901, "数据为空"),
    SERVICE_DATA_ERROR(10902, "数据异常");


    private final int code;
    private final String message;
    private static final Map<Integer, CodeEnum> RELATION = new HashMap<>(32);

    static {
        for (CodeEnum value : values()) {
            RELATION.put(value.code, value);
        }
    }


    public static CodeEnum codeOf(int code) {
        return RELATION.get(code);
    }
}

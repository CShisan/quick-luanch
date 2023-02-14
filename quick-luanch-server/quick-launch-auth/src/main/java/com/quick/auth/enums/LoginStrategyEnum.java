package com.quick.auth.enums;

import com.quick.auth.security.exception.AuthException;
import com.quick.auth.security.provider.AuthProvider;
import com.quick.auth.security.provider.PhonePwAuthProvider;
import com.quick.auth.security.provider.UidPwAuthProvider;
import com.quick.auth.security.provider.WxAppletAuthProvider;
import com.quick.auth.security.token.AuthToken;
import com.quick.auth.security.token.PhonePwAuthToken;
import com.quick.auth.security.token.UidPwAuthToken;
import com.quick.auth.security.token.WxAppletAuthToken;
import com.quick.common.enums.CodeEnum;
import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author CShisan
 */
@Getter
@AllArgsConstructor
public enum LoginStrategyEnum {
    /**
     * 登录策略枚举
     */
    UID_PW("uid-pw", UidPwAuthToken::new, UidPwAuthProvider.class),
    PHONE_PW("phone-pw", PhonePwAuthToken::new, PhonePwAuthProvider.class),
    WX_APPLET("wx-applet", WxAppletAuthToken::new, WxAppletAuthProvider.class);

    private static final String TYPE_NAME = "loginType";
    private static final Map<String, LoginStrategyEnum> TYPE_RELATION = new HashMap<>(16);
    private static final Map<Class<? extends AuthProvider>, LoginStrategyEnum> PROVIDER_RELATION = new HashMap<>(16);

    static {
        for (LoginStrategyEnum value : values()) {
            TYPE_RELATION.put(value.type, value);
            PROVIDER_RELATION.put(value.provider, value);
        }
    }

    private final String type;
    private final Function<String, AuthToken> function;
    private final Class<? extends AuthProvider> provider;

    /**
     * 根据type获取对应策略token
     *
     * @param body body
     * @return token
     */
    public static AuthToken token(String body) {
        // 获取登录类型
        String type = JsonUtil.getField(body, TYPE_NAME);
        // 遍历获取对应token
        LoginStrategyEnum strategy = TYPE_RELATION.get(type);
        if (CheckUtil.nonNull(strategy)) {
            return strategy.function.apply(body);
        }
        throw new AuthException(CodeEnum.USER_AUTH_ERROR, "登录异常");
    }

    /**
     * 根据AuthProvider获取对应type
     *
     * @param clazz clazz
     * @return type
     */
    public static LoginStrategyEnum typeOf(Class<? extends AuthProvider> clazz) {
        return PROVIDER_RELATION.get(clazz);
    }
}

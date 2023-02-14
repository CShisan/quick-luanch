package com.quick.auth.utils;

import com.quick.common.utils.CheckUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author CShisan
 */
public class PwdEncoderUtil {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * 用BCryptPasswordEncoder加密
     *
     * @param password 明文密码
     * @return 加密密码
     */
    public static String encodePwd(String password) {
        return ENCODER.encode(password);
    }

    /**
     * 校验密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 已加密密码
     * @return status
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return CheckUtil.allNonBlank(rawPassword, encodedPassword) && ENCODER.matches(rawPassword, encodedPassword);
    }

}

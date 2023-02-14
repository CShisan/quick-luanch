package com.quick.auth.utils;

import com.quick.auth.entity.CaptchaEntity;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author yuanbai
 */
@Component
public class AuthCaptchaUtil {
    private final RedisUtil redisUtil;

    @Autowired
    public AuthCaptchaUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 验证验证码
     *
     * @param captcha captcha
     * @return status
     */
    public boolean validateCaptcha(CaptchaEntity captcha) {
        ExceptionHandler.requireNotNull(captcha, CodeEnum.SERVICE_DATA_ERROR, "验证码不能为空");
        String captchaSign = captcha.getCaptchaSign();
        ExceptionHandler.requireNotBlank(captchaSign, CodeEnum.SERVICE_DATA_ERROR, "验证码不能为空");

        // 获取验证码
        String key = CaptchaEntity.getKey(captchaSign);
        String sourceCode = redisUtil.get(key);
        ExceptionHandler.assertTrue(CheckUtil.nonNull(sourceCode), CodeEnum.SERVICE_DATA_ERROR, "请先获取验证码");

        // 删除redis的验证码并比对
        redisUtil.delete(key);
        return Objects.equals(sourceCode, captcha.getValidCode());
    }
}

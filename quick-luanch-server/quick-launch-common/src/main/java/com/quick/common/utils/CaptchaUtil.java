package com.quick.common.utils;

import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author CShisan
 */
public class CaptchaUtil {
    /**
     * 长度
     */
    private static final int LEN = 4;
    /**
     * 宽度
     */
    private static final int WIDTH = 130;
    /**
     * 高度
     */
    private static final int HEIGHT = 48;
    /**
     * 字体
     */
    private static final int FONT = Captcha.FONT_1;
    /**
     * 字符类型
     */
    private static final int CHAR_TYPE = Captcha.TYPE_DEFAULT;

    /**
     * 获取验证码
     *
     * @see CaptchaUtil#getCaptcha(String)
     */
    public static Map<String, Object> getCaptcha() {
        return getCaptcha(null);
    }

    /**
     * 获取验证码
     *
     * @param captchaType 验证码类型
     */
    public static Map<String, Object> getCaptcha(String captchaType) {
        // 根据验证码类型获取验证码
        boolean isBlankType = CheckUtil.isBlank(captchaType);
        Captcha captcha = isBlankType ? CaptchaTypeEnum.randomCaptcha() : CaptchaTypeEnum.valueOf(captchaType).captcha.get();

        // 设置属性
        setCaptchaAttribute(captcha);

        // 生成签名
        String captchaSign = UUID.randomUUID().toString().replace("-", "");

        // 构建结果
        Map<String, Object> result = new HashMap<>(5);
        result.put("captchaSign", captchaSign);
        result.put("captcha", captcha.toBase64());
        result.put("validCode", captcha.text());
        return result;
    }

    /**
     * 设置验证码属性
     */
    private static void setCaptchaAttribute(Captcha captcha) {
        try {
            captcha.setFont(FONT);
            captcha.setCharType(CHAR_TYPE);
            captcha.setLen(LEN);
            captcha.setWidth(WIDTH);
            captcha.setHeight(HEIGHT);
        } catch (IOException | FontFormatException e) {
            throw new ServiceException(CodeEnum.FAIL, "验证码生成失败", e);
        }
    }

    @Getter
    @AllArgsConstructor
    enum CaptchaTypeEnum {
        /**
         * 验证码类型
         */
        SPEC("spec", SpecCaptcha::new),
        GIF("gif", GifCaptcha::new);

        public final String type;
        public final Supplier<Captcha> captcha;

        public static Captcha randomCaptcha() {
            CaptchaTypeEnum[] values = CaptchaTypeEnum.values();
            int index = new Random().nextInt(values.length - 1);
            return values[index].captcha.get();
        }
    }
}

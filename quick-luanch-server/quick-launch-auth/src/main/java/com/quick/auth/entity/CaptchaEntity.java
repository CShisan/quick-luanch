package com.quick.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author CShisan
 */
@Data
@Schema(description = "验证码实体")
public class CaptchaEntity {
    @Schema(description = "key前缀", hidden = true)
    private static final String CAPTCHA_PREFIX = "captcha_";

    @Schema(description = "验证码图片, base64编码", hidden = true)
    private String captcha;

    @Schema(description = "验证码")
    private String validCode;

    @Schema(description = "验证码标识")
    private String captchaSign;

    public static String getKey(String captchaSign) {
        return CAPTCHA_PREFIX.concat(captchaSign);
    }
}

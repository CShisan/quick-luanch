package com.quick.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@Schema(description = "手机号码密码登录实体")
@EqualsAndHashCode(callSuper = true)
public class PhonePwEntity extends LoginEntity {
    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "验证码")
    private CaptchaEntity captcha;

}

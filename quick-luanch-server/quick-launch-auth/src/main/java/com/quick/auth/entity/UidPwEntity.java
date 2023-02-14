package com.quick.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@Schema(description = "UID密码登录实体")
@EqualsAndHashCode(callSuper = true)
public class UidPwEntity extends LoginEntity {
    @Schema(description = "验证码")
    private CaptchaEntity captcha;
}

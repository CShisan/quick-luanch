package com.quick.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "微信登录实体")
public class WxLoginEntity extends LoginEntity {
    @Schema(description = "小程序静默登录code")
    private String code;

    @Schema(description = "用户信息的加密数据")
    private String encryptedData;

    @Schema(description = "加密算法的初始向量")
    private String iv;

    @Schema(description = "微信接口认证错误code")
    private String errCode;

    @Schema(description = "微信接口认证错误msg")
    private String errMsg;

    @Schema(description = "小程序openid")
    private String openid;

    @Schema(description = "sessionKey")
    private String sessionKey;

    @Schema(description = "手机号码(国外手机号码有区号)")
    private String phoneNumber;

    @Schema(description = "手机号(无区号)")
    private String purePhoneNumber;

    @Schema(description = "区号")
    private String countryCode;

    private static final String USERNAME_PREFIX = "微信用户_";

    public static String getUsername(String username) {
        return USERNAME_PREFIX.concat(username);
    }
}

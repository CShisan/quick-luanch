package com.quick.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user", autoResultMap = true)
public class User extends BaseEntity {
    /**
     * uid
     */
    private Long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 微信openid
     */
    private String openId;

    /**
     * 启用标志
     */
    private Boolean enable;

    /**
     * 锁定标志
     */
    private Boolean locked;

    /**
     * 最后登录时间
     */
    private Date loginLastTime;

    /**
     * 最后登录IP
     */
    private String loginLastIp;
}

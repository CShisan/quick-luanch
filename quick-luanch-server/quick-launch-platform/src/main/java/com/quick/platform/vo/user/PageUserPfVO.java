package com.quick.platform.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author CShisan
 */
@Data
@Schema(description = "用户信息分页VO")
public class PageUserPfVO {
    @Schema(description = "UID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    @Schema(description = "用户名称")
    private String username;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginLastTime;

    @Schema(description = "最后登录IP")
    private String loginLastIp;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "是否锁定")
    private Boolean locked;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

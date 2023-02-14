package com.quick.platform.vo.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.quick.platform.vo.role.InfoRolePfVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author CShisan
 */
@Data
@Schema(description = "用户信息详情VO")
public class DetailUserPfVO {
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

    @Schema(description = "角色集")
    private List<InfoRolePfVO> roles;
}

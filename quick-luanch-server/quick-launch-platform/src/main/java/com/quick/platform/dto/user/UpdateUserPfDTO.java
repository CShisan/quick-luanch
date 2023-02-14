package com.quick.platform.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色信息更新DTO")
public class UpdateUserPfDTO {
    @Schema(description = "用户ID", type = "string")
    @NotNull(message = "用户ID不能为空")
    private Long uid;

    @Schema(description = "角色IDS")
    private List<Long> roleIds;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像url")
    private String avatar;
}

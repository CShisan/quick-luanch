package com.quick.platform.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色信息更新DTO")
public class UpdateRolePfDTO {
    @Schema(description = "角色ID", type = "string")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "角色KEY")
    private String roleKey;

    @Schema(description = "角色名称")
    private String roleName;
}

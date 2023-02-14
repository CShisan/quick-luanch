package com.quick.platform.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "权限信息更新DTO")
public class UpdatePermissionPfDTO {
    @Schema(description = "权限ID", type = "string")
    @NotNull(message = "权限ID不能为空")
    private Long permissionId;

    @Schema(description = "权限KEY")
    private String permissionKey;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "父节点ID", type = "string")
    private Long pid;

    @Schema(description = "是否启用")
    private Boolean enable;
}

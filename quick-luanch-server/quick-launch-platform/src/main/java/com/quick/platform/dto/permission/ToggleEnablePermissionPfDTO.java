package com.quick.platform.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "权限启用状态切换DTO")
public class ToggleEnablePermissionPfDTO {
    @Schema(description = "权限ID")
    @NotNull(message = "权限ID不能为空")
    private Long permissionId;

    @Schema(description = "启用状态")
    @NotNull(message = "启用状态不能为空")
    private Boolean enable;
}

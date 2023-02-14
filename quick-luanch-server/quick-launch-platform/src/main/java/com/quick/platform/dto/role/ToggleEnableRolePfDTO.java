package com.quick.platform.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色启用状态切换DTO")
public class ToggleEnableRolePfDTO {
    @Schema(description = "角色ID", type = "string")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "启用状态")
    @NotNull(message = "启用状态不能为空")
    private Boolean enable;
}

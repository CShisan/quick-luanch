package com.quick.platform.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "权限信息新增DTO")
public class SavePermissionPfDTO {
    @Schema(description = "权限KEY")
    @NotBlank(message = "权限KEY不能为空")
    private String permissionKey;

    @Schema(description = "权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    @Schema(description = "是否启用")
    @NotNull(message = "启用标志不能为空")
    private Boolean enable;

    @Schema(description = "父节点ID")
    @NotNull(message = "父节点ID不能为空")
    @Min(value = 0, message = "非法父节点")
    private Long pid;
}

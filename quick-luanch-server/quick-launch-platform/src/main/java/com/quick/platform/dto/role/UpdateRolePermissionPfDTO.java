package com.quick.platform.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色关联的权限DTO")
public class UpdateRolePermissionPfDTO {
    @Schema(description = "角色ID", type = "string")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "权限ID集合", type = "string")
    private List<Long> permissionIds;
}

package com.quick.platform.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author CShisan
 */
@Data
@Schema(description = "权限删除DTO")
public class DeletePermissionPfDTO {
    @Schema(description = "权限ID", type = "string")
    private Long permissionId;
}

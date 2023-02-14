package com.quick.platform.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "权限绑定资源DTO")
public class BindResourcePermissionPfDTO {
    @Schema(description = "权限ID", type = "string")
    @NotNull(message = "权限ID不能为空")
    private Long permissionId;

    @Schema(description = "资源ID", type = "string")
    private List<Long> resourceIds;
}

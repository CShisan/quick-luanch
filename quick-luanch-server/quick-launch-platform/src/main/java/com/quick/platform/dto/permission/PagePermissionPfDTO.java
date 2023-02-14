package com.quick.platform.dto.permission;

import com.quick.common.base.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@Schema(description = "权限信息分页DTO")
@EqualsAndHashCode(callSuper = true)
public class PagePermissionPfDTO extends BasePage {
    @Schema(description = "权限ID", type = "string")
    private Long permissionId;
}

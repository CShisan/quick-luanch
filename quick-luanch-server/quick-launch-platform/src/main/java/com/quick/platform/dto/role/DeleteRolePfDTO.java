package com.quick.platform.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色信息删除DTO")
public class DeleteRolePfDTO {
    @Schema(description = "角色ID", type = "string")
    @NotNull(message = "角色ID不能为空")
    @Min(value = 2, message = "非法删除角色")
    private Long roleId;
}

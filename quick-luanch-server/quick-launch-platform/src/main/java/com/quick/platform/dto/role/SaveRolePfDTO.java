package com.quick.platform.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色信息新增DTO")
public class SaveRolePfDTO {
    @Schema(description = "角色KEY")
    @NotBlank(message = "角色KEY不能为空")
    private String roleKey;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
}

package com.quick.platform.dto.role;

import com.quick.common.base.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色信息分页DTO")
@EqualsAndHashCode(callSuper = true)
public class PageRolePfDTO extends BasePage {
    @Schema(description = "角色名称")
    private String roleName;
}

package com.quick.platform.dto.resources;

import com.quick.common.base.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "资源信息分页DTO")
@EqualsAndHashCode(callSuper = true)
public class PageResourcesPfDTO extends BasePage {
    @Schema(description = "资源ID", type = "string")
    private Long resourceId;

    @Schema(description = "资源描述")
    private String description;
}

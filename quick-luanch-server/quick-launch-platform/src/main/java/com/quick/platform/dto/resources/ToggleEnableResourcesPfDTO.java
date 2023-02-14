package com.quick.platform.dto.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "资源启用状态切换DTO")
public class ToggleEnableResourcesPfDTO {
    @Schema(description = "资源ID")
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @Schema(description = "启用状态")
    @NotNull(message = "启用状态不能为空")
    private Boolean enable;
}

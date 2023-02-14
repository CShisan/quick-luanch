package com.quick.platform.dto.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "资源信息更新DTO")
public class UpdateResourcesPfDTO {
    @Schema(description = "资源ID", type = "string")
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @Schema(description = "资源路径")
    @NotBlank(message = "资源路径不能为空")
    private String path;

    @Schema(description = "资源描述")
    @NotBlank(message = "资源描述不能为空")
    private String description;

    @Schema(description = "父资源ID", type = "string")
    @NotNull(message = "父资源ID不能为空")
    private Long pid;
}

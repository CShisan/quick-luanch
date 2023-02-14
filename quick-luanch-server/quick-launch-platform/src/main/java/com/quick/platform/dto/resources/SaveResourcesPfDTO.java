package com.quick.platform.dto.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "资源信息新增DTO")
public class SaveResourcesPfDTO {
    @Schema(description = "资源路径")
    private String path;

    @Schema(description = "资源描述")
    private String description;

    @Schema(description = "父资源ID", type = "string")
    private Long pid;
}

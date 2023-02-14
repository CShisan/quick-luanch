package com.quick.platform.dto.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "资源信息删除DTO")
public class DeleteResourcesPfDTO {
    @Schema(description = "资源ID", type = "string")
    private Long resourceId;
}

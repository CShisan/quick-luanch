package com.quick.platform.vo.resources;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "资源信息详情VO")
public class DetailResourcesPfVO {
    @Schema(description = "资源ID", type = "string")
    private Long resourceId;

    @Schema(description = "资源路径")
    private String path;

    @Schema(description = "资源描述")
    private String description;

    @Schema(description = "父资源ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;

    @Schema(description = "父资源描述")
    private String parentDec;

    @Schema(description = "是否启用")
    private Boolean enable;
}

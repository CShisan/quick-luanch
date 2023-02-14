package com.quick.platform.vo.resources;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "权限关联的资源列表VO")
public class RelationResourcesPfVO {
    @Schema(description = "资源ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long resourceId;

    @Schema(description = "资源描述")
    private String description;
}

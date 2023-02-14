package com.quick.platform.vo.resources;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author CShisan
 */
@Data
@Schema(description = "资源树VO")
public class TreeResourcesPfVO {
    @Schema(description = "资源ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long resourceId;

    @Schema(description = "资源描述")
    private String description;

    @Schema(description = "子权限集合")
    private List<TreeResourcesPfVO> children;
}

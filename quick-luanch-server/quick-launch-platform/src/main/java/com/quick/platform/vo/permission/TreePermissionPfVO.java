package com.quick.platform.vo.permission;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author CShisan
 */
@Data
@Schema(description = "权限树VO")
public class TreePermissionPfVO {
    @Schema(description = "权限ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long permissionId;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "叶子节点标记")
    private Boolean leafFlag;

    @Schema(description = "子权限集合")
    private List<TreePermissionPfVO> children;
}

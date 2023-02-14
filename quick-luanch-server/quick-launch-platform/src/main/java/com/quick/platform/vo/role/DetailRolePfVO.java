package com.quick.platform.vo.role;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色信息详情VO")
public class DetailRolePfVO {
    @Schema(description = "角色ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @Schema(description = "角色KEY")
    private String roleKey;

    @Schema(description = "角色名称")
    private String roleName;
}

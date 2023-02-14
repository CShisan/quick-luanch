package com.quick.platform.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author CShisan
 */
@Data
@Schema(description = "用户锁定状态切换DTO")
public class ToggleLockUserPfDTO {
    @Schema(description = "UID", type = "string")
    @NotNull(message = "UID不能为空")
    private Long uid;

    @Schema(description = "锁定状态")
    @NotNull(message = "锁定状态不能为空")
    private Boolean locked;
}

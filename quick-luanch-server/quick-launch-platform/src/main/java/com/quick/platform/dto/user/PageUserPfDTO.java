package com.quick.platform.dto.user;

import com.quick.common.base.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@Schema(description = "用户分页DTO")
@EqualsAndHashCode(callSuper = true)
public class PageUserPfDTO extends BasePage {
    @Schema(description = "UID", type = "string")
    private Long uid;
}

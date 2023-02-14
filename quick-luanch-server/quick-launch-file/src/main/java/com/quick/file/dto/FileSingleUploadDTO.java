package com.quick.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文件单次上传信息DTO")
public class FileSingleUploadDTO extends FileUploadDTO {
}

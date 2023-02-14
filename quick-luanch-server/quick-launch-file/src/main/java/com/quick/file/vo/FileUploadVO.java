package com.quick.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CShisan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文件上传信息VO")
public class FileUploadVO<T> {
    @Schema(description = "存储路径")
    private String path;
    @Schema(description = "访问路径")
    private String fullPath;
    @Schema(description = "其他数据")
    private T data;
}

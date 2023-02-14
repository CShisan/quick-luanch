package com.quick.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文件分片上传信息DTO")
public class FileMultipartUploadDTO extends FileUploadDTO {
    @Schema(description = "MD5")
    private String md5;
    @Schema(description = "分片次序")
    private Integer chunkSequence;
    @Schema(description = "分片最大大小")
    private Integer chunkMaxSize;
    @Schema(description = "分片总数")
    private Integer chunkAmount;
}

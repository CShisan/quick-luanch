package com.quick.file.dto;

import com.quick.file.enums.UploadMappingEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author CShisan
 */
@Data
@Schema(description = "文件上传信息DTO")
public class FileUploadDTO {
    @Schema(description = "文件")
    private MultipartFile file;
    @Schema(description = "文件映射枚举")
    private UploadMappingEnum mapping;
    @Schema(description = "文件存储路径", hidden = true)
    private String path;
    @Schema(description = "文件字节数组", hidden = true)
    private byte[] bytes;
}

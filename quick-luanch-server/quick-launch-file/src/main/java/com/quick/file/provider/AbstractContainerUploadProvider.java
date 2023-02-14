package com.quick.file.provider;

import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import com.quick.file.dto.FileUploadDTO;
import com.quick.file.enums.UploadMappingEnum;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

/**
 * @author CShisan
 */
public abstract class AbstractContainerUploadProvider implements ContainerUploadProvider {
    /**
     * 转换Byte
     */
    protected byte[] toBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ServiceException(CodeEnum.FAIL, "【文件上传服务】转换Byte失败", e);
        }
    }

    /**
     * 存储路径
     */
    protected String path(FileUploadDTO dto, String bucketBasePath) {
        UploadMappingEnum mapping = dto.getMapping();
        String suffix = this.fileNameSuffix(dto.getFile());
        String date = DateFormatUtils.format(new Date(), "/yyyy/MM/dd/");
        String bucket = mapping.getBucket().getKey();
        String type = mapping.getType().getKey();
        return bucketBasePath + bucket + "/" + type + date + System.currentTimeMillis() + random() + suffix;
    }

    /**
     * 文件名后缀
     */
    protected String fileNameSuffix(MultipartFile file) {
        String filename = Optional.ofNullable(file).map(MultipartFile::getOriginalFilename).orElse("");
        int index = filename.lastIndexOf(".");
        return index == -1 ? "" : filename.substring(index);
    }

    /**
     * 随机数
     */
    protected int random() {
        return 1000 + new Random().nextInt(8999);
    }
}

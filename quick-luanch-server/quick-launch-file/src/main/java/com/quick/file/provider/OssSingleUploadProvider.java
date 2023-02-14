package com.quick.file.provider;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.quick.common.config.AliyunConfig;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import com.quick.file.dto.FileSingleUploadDTO;
import com.quick.file.dto.FileUploadDTO;
import com.quick.file.factory.ContainerProviderFactory;
import com.quick.file.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * @author CShisan
 */
@Component
public class OssSingleUploadProvider extends AbstractOssUploadProvider {
    private final AliyunConfig.OssConfig config;
    private final OSS client;

    @Autowired
    protected OssSingleUploadProvider(OssUtil ossUtil) {
        this.config = ossUtil.config();
        this.client = ossUtil.client();
    }

    @Override
    public void upload(FileUploadDTO dto) {
        if (dto instanceof FileSingleUploadDTO) {
            this.upload((FileSingleUploadDTO) dto);
            return;
        }
        throw new ServiceException(CodeEnum.PARAM_ERROR, "【文件上传服务】容器提供者不支持该DTO");
    }

    /**
     * 上传
     */
    protected void upload(FileSingleUploadDTO dto) {
        // 转换
        if (Objects.isNull(dto.getBytes())) {
            byte[] bytes = this.toBytes(dto.getFile());
            dto.setBytes(bytes);
        }

        // 生成存储路径
        if (Objects.isNull(dto.getPath())) {
            String path = this.path(dto, this.bucketBasePath());
            dto.setPath(path);
        }

        // 上传
        this.uploading(dto.getBytes(), dto.getPath());
    }

    /**
     * 上传
     */
    protected void uploading(byte[] bytes, String path) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            client.putObject(config.getBucketName(), path, is);
        } catch (OSSException | ClientException e) {
            throw new ServiceException(CodeEnum.FAIL, "上传失败", e);
        }
    }

    @Override
    protected AliyunConfig.OssConfig config() {
        return config;
    }

    @Override
    @PostConstruct
    public void register() {
        ContainerProviderFactory.register(this);
    }
}

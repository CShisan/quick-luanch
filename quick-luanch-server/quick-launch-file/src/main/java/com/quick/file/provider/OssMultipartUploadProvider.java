package com.quick.file.provider;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.quick.common.config.AliyunConfig;
import com.quick.common.enums.CodeEnum;
import com.quick.common.enums.RedisKeyEnum;
import com.quick.common.exception.ServiceException;
import com.quick.common.utils.RedisUtil;
import com.quick.file.dto.FileMultipartUploadDTO;
import com.quick.file.dto.FileUploadDTO;
import com.quick.file.factory.ContainerProviderFactory;
import com.quick.file.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Component
public class OssMultipartUploadProvider extends AbstractOssUploadProvider {
    private final AliyunConfig.OssConfig config;
    private final OSS client;
    private final RedisUtil redisUtil;

    @Autowired
    protected OssMultipartUploadProvider(OssUtil ossUtil, RedisUtil redisUtil) {
        this.config = ossUtil.config();
        this.client = ossUtil.client();
        this.redisUtil = redisUtil;
    }

    @Override
    protected AliyunConfig.OssConfig config() {
        return config;
    }

    @Override
    public void upload(FileUploadDTO dto) {
        if (dto instanceof FileMultipartUploadDTO) {
            this.upload((FileMultipartUploadDTO) dto);
            return;
        }
        throw new ServiceException(CodeEnum.PARAM_ERROR, "【文件上传服务】容器提供者不支持该DTO");
    }

    public void upload(FileMultipartUploadDTO dto) {
        // 转换
        if (Objects.isNull(dto.getBytes())) {
            byte[] bytes = this.toBytes(dto.getFile());
            dto.setBytes(bytes);
        }

        // 上传
        this.uploading(dto);
    }

    /**
     * 上传
     */
    protected void uploading(FileMultipartUploadDTO dto) {
        try {
            // 初始化分片
            String path = this.path(dto);
            String bucketName = config.getBucketName();
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, path);
            InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
            String uploadId = result.getUploadId();

            // 获取当前分片次序、大小
            byte[] bytes = dto.getBytes();
            int chunkSequence = Optional.ofNullable(dto.getChunkSequence()).orElse(1);
            int chunkAmount = Optional.ofNullable(dto.getChunkAmount()).orElse(1);
            int chunkSize = Optional.ofNullable(dto.getChunkMaxSize()).map(item ->
                    Math.min(item, bytes.length)
            ).orElse(1);

            // 设置文件存储路径、上传ID、分片大小、分片号,上传
            UploadPartRequest uploadPartRequest = new UploadPartRequest(bucketName, path);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setPartSize(chunkSize);
            uploadPartRequest.setPartNumber(chunkSequence);
            uploadPartRequest.setInputStream(new ByteArrayInputStream(bytes));

            // 上传分片
            client.uploadPart(uploadPartRequest);

            // 最后一个分片上传完就合并分片
            ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, path, uploadId);
            PartListing partListing = client.listParts(listPartsRequest);
            List<PartSummary> parts = partListing.getParts();
            if (Objects.nonNull(parts) && (parts.size() == chunkAmount)) {
                List<PartETag> eTagKeys = parts.stream().map(item ->
                        new PartETag(item.getPartNumber(), item.getETag())
                ).collect(Collectors.toList());
                client.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, path, uploadId, eTagKeys));
            }
        } catch (OSSException | ClientException e) {
            throw new ServiceException(CodeEnum.FAIL, "上传失败", e);
        }
    }

    /**
     * 获取存储路径
     */
    protected String path(FileMultipartUploadDTO dto) {
        String key = RedisKeyEnum.SYSTEM_FILE_UPLOAD_MULTIPART_NAME.format(dto.getMd5());
        String storePath = redisUtil.get(key);
        if (Objects.isNull(storePath)) {
            storePath = this.path(dto, this.bucketBasePath());
            redisUtil.set(key, storePath, 60L, TimeUnit.MINUTES);
        }
        return storePath;
    }

    @Override
    @PostConstruct
    public void register() {
        ContainerProviderFactory.register(this);
    }
}

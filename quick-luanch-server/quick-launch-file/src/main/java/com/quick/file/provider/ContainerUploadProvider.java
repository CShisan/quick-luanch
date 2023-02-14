package com.quick.file.provider;

import com.quick.file.dto.FileUploadDTO;

/**
 * @author CShisan
 */
public interface ContainerUploadProvider {
    /**
     * 上传
     *
     * @param dto dto
     */
    void upload(FileUploadDTO dto);

    /**
     * 访问路径前缀
     *
     * @return path
     */
    String pathPrefix();

    /**
     * 容器桶基路径
     *
     * @return path
     */
    String bucketBasePath();

    /**
     * 容器工厂注册
     */
    void register();
}

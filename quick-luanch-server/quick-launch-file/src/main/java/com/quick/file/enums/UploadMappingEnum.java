package com.quick.file.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.quick.file.handler.upload.ImageWithThumbnailUploadHandler;
import com.quick.file.handler.upload.VideoWithThumbnailUploadHandler;
import com.quick.file.provider.ContainerUploadProvider;
import com.quick.file.provider.OssSingleUploadProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author CShisan
 */
@Getter
@AllArgsConstructor
public enum UploadMappingEnum {
    /**
     * 文件映射
     */
    AVATAR(1, "头像", UploadBucketEnum.AVATAR, UploadTypeEnum.IMAGE, ImageWithThumbnailUploadHandler.class, OssSingleUploadProvider.class),
    TEST_VIDEO_THUMBNAIL(2, "测试-视频(缩略图)", UploadBucketEnum.TEST_VIDEO_THUMBNAIL, UploadTypeEnum.VIDEO, VideoWithThumbnailUploadHandler.class, OssSingleUploadProvider.class);

    @JsonValue
    private final int code;
    private final String desc;
    private final UploadBucketEnum bucket;
    private final UploadTypeEnum type;
    private final Class<?> handler;
    private final Class<? extends ContainerUploadProvider> provider;
}

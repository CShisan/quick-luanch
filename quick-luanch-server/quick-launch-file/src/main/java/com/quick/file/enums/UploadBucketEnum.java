package com.quick.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author CShisan
 */
@Getter
@AllArgsConstructor
public enum UploadBucketEnum {
    /**
     * 文件桶
     */
    AVATAR(1, "avatar", "头像"),
    TEST_VIDEO_THUMBNAIL(2, "test-video-thumbnail", "测试-视频(缩略图)");

    private final int code;
    private final String key;
    private final String name;
}

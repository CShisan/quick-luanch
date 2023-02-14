package com.quick.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author CShisan
 */
@Getter
@AllArgsConstructor
public enum UploadTypeEnum {
    /**
     * 文件类型
     */
    IMAGE(1, "image", "图片", new ArrayList<>(Arrays.asList(".jpg", ".png", "bmp", "gif"))),
    VIDEO(2, "video", "视频", new ArrayList<>(Arrays.asList(".mp4", ".avi"))),
    APK(3, "apk", "APK文件", new ArrayList<>(Collections.singletonList(".apk")));

    private final int code;
    private final String key;
    private final String name;
    private final List<String> fileFormat;
}

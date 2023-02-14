package com.quick.common.utils;

import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author CShisan
 */
public class ImageUtil {
    /**
     * @see ImageUtil#compress(byte[], float)
     */
    public static byte[] compress(byte[] bytes) {
        return compress(bytes, 0.6f);
    }

    /**
     * 图片压缩
     */
    public static byte[] compress(byte[] bytes, float scale) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Thumbnails.of(is).scale(scale).toOutputStream(os);
            return os.toByteArray();
        } catch (IOException e) {
            throw new ServiceException(CodeEnum.FAIL, "【图片工具】压缩失败", e);
        }
    }
}

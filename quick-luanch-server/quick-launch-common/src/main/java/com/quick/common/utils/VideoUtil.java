package com.quick.common.utils;

import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.exception.ServiceException;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
public class VideoUtil {
    /**
     * 截取指定位置视频帧
     */
    public static List<byte[]> grabFrame(byte[] bytes, List<Double> positions) {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(stream);
        // 遍历位置截帧
        try {
            grabber.start();
            boolean hasLength = grabber.getLengthInFrames() != 0;
            ExceptionHandler.assertTrue(hasLength, CodeEnum.PARAM_ERROR, "【视频工具】视频时长为0");
            List<byte[]> result = positions.stream().map(item ->
                    grabFrame(grabber, item)
            ).collect(Collectors.toList());
            grabber.stop();
            return result;
        } catch (FrameGrabber.Exception e) {
            throw new ServiceException(CodeEnum.FAIL, "【视频工具】截帧失败", e);
        }
    }

    /**
     * 截取指定位置视频帧
     */
    public static byte[] grabFrame(FFmpegFrameGrabber grabber, double position) {
        try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
            // 跳转到指定帧
            int length = grabber.getLengthInFrames();
            grabber.setFrameNumber((int) (length * position));
            Frame frame = grabber.grabImage();

            // 转换byte[]
            BufferedImage bufferedImage = converter.convert(frame);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", os);
            return os.toByteArray();
        } catch (IOException e) {
            throw new ServiceException(CodeEnum.FAIL, "【视频工具】视频截帧失败", e);
        }
    }
}

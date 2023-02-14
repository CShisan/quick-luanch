package com.quick.file.handler.upload;

import com.quick.common.utils.ImageUtil;
import com.quick.common.utils.VideoUtil;
import com.quick.file.dto.FileSingleUploadDTO;
import com.quick.file.dto.FileUploadDTO;
import com.quick.file.enums.UploadTypeEnum;
import com.quick.file.provider.OssSingleUploadProvider;
import com.quick.file.vo.FileUploadVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CShisan
 */
@Component
public class VideoWithThumbnailUploadHandler extends AbstractUploadHandler<List<FileUploadVO<String>>> implements UploadPreCheckHandler {
    private final OssSingleUploadProvider singleUploadProvider;

    @Autowired
    public VideoWithThumbnailUploadHandler(OssSingleUploadProvider singleUploadProvider) {
        this.singleUploadProvider = singleUploadProvider;
    }

    @Override
    public FileUploadVO<List<FileUploadVO<String>>> upload(FileUploadDTO dto) {
        // 预检查格式
        MultipartFile file = dto.getFile();
        String suffix = this.fileNameSuffix(file);
        this.preCheck(suffix, UploadTypeEnum.VIDEO.getFileFormat());

        // 上传
        FileUploadVO<List<FileUploadVO<String>>> result = super.upload(dto);

        // 截帧
        List<byte[]> frames = VideoUtil.grabFrame(dto.getBytes(), Arrays.asList(0.1d, 0.5d, 0.9d));

        // 遍历上传截帧
        List<FileUploadVO<String>> data = new ArrayList<>();
        for (int i = 0; i < frames.size(); i++) {
            // 生成截帧存储路径
            String thumbnailPath = thumbnailPath(dto.getPath(), String.format("thumbnail%s.jpg", i + 1));
            // 上传截帧
            FileSingleUploadDTO thumbDTO = new FileSingleUploadDTO();
            thumbDTO.setPath(thumbnailPath);
            thumbDTO.setBytes(ImageUtil.compress(frames.get(i), 0.25f));
            singleUploadProvider.upload(thumbDTO);
            // 构造截帧上传信息VO
            String fullThumbnailPath = singleUploadProvider.pathPrefix().concat(thumbnailPath);
            data.add(new FileUploadVO<>(thumbnailPath, fullThumbnailPath, String.format("第%s帧", i + 1)));
        }
        result.setData(data);
        return result;
    }

    /**
     * 生成截帧存储路径
     */
    private String thumbnailPath(String path, String thumbnailSuffix) {
        int index = path.lastIndexOf(".");
        String suffix = path.substring(index);
        return path.replace(suffix, thumbnailSuffix);
    }

    @Override
    @PostConstruct
    public void register() {
        super.register();
    }
}

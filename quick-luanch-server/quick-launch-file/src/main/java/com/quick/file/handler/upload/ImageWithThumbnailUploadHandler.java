package com.quick.file.handler.upload;

import com.quick.common.utils.ImageUtil;
import com.quick.file.dto.FileSingleUploadDTO;
import com.quick.file.dto.FileUploadDTO;
import com.quick.file.enums.UploadTypeEnum;
import com.quick.file.provider.OssSingleUploadProvider;
import com.quick.file.vo.FileUploadVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author CShisan
 */
@Component
public class ImageWithThumbnailUploadHandler extends AbstractUploadHandler<FileUploadVO<String>> implements UploadPreCheckHandler {
    private final OssSingleUploadProvider singleUploadProvider;

    @Autowired
    public ImageWithThumbnailUploadHandler(OssSingleUploadProvider singleUploadProvider) {
        this.singleUploadProvider = singleUploadProvider;
    }

    @Override
    public FileUploadVO<FileUploadVO<String>> upload(FileUploadDTO dto) {
        // 预检查格式
        String suffix = this.fileNameSuffix(dto.getFile());
        this.preCheck(suffix, UploadTypeEnum.IMAGE.getFileFormat());

        // 上传
        FileUploadVO<FileUploadVO<String>> result = super.upload(dto);

        // 生成缩略图路径
        String thumbnailPath = this.thumbnailPath(dto.getPath());

        // 压缩
        byte[] compress = ImageUtil.compress(dto.getBytes());

        // 上传缩略图
        FileSingleUploadDTO thumbDTO = new FileSingleUploadDTO();
        thumbDTO.setBytes(compress);
        thumbDTO.setPath(thumbnailPath);
        singleUploadProvider.upload(thumbDTO);
        String fullThumbnailPath = singleUploadProvider.pathPrefix().concat(thumbnailPath);
        result.setData(new FileUploadVO<>(thumbnailPath, fullThumbnailPath, "缩略图"));
        return result;
    }

    /**
     * 生成缩略图路径
     */
    private String thumbnailPath(String path) {
        int index = path.lastIndexOf(".");
        String suffix = path.substring(index);
        return path.replace(suffix, "thumbnail".concat(suffix));
    }

    @Override
    @PostConstruct
    public void register() {
        super.register();
    }
}

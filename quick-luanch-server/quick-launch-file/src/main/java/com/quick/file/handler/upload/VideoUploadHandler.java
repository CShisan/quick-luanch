package com.quick.file.handler.upload;

import com.quick.file.dto.FileUploadDTO;
import com.quick.file.enums.UploadTypeEnum;
import com.quick.file.vo.FileUploadVO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author CShisan
 */
@Component
public class VideoUploadHandler extends AbstractUploadHandler<Object> implements UploadPreCheckHandler {
    @Override
    public FileUploadVO<Object> upload(FileUploadDTO dto) {
        // 预检查格式
        String suffix = this.fileNameSuffix(dto.getFile());
        this.preCheck(suffix, UploadTypeEnum.VIDEO.getFileFormat());
        return super.upload(dto);
    }

    @Override
    @PostConstruct
    public void register() {
        super.register();
    }
}

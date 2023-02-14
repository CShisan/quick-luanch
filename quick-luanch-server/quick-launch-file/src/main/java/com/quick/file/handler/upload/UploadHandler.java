package com.quick.file.handler.upload;

import com.quick.file.dto.FileUploadDTO;
import com.quick.file.vo.FileUploadVO;

/**
 * @author CShisan
 */
public interface UploadHandler<T> {
    /**
     * 上传
     *
     * @param dto dto
     * @return vo
     */
    FileUploadVO<T> upload(FileUploadDTO dto);

    /**
     * 文件处理器工厂注册
     */
    void register();
}

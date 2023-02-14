package com.quick.file.handler.upload;

import com.quick.file.dto.FileUploadDTO;
import com.quick.file.enums.UploadMappingEnum;
import com.quick.file.factory.ContainerProviderFactory;
import com.quick.file.factory.UploadHandlerFactory;
import com.quick.file.provider.ContainerUploadProvider;
import com.quick.file.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

/**
 * @author CShisan
 */
public abstract class AbstractUploadHandler<T> implements UploadHandler<T> {
    @Override
    public FileUploadVO<T> upload(FileUploadDTO dto) {
        // 获取容器
        ContainerUploadProvider provider = this.provider(dto.getMapping());

        // 上传
        provider.upload(dto);

        // 构造结果
        return this.result(provider.pathPrefix(), dto.getPath());
    }

    /**
     * 获取容器provider
     */
    protected ContainerUploadProvider provider(UploadMappingEnum mapping) {
        return ContainerProviderFactory.classOf(mapping.getProvider());
    }

    /**
     * 文件名后缀
     */
    protected String fileNameSuffix(MultipartFile file) {
        String filename = Optional.ofNullable(file).map(MultipartFile::getOriginalFilename).orElse("");
        int index = filename.lastIndexOf(".");
        return index == -1 ? "" : filename.substring(index);
    }

    /**
     * 构造返回结果
     */
    protected FileUploadVO<T> result(String prefix, String path) {
        prefix = Objects.toString(prefix, "");
        FileUploadVO<T> result = new FileUploadVO<>();
        result.setPath(path);
        result.setFullPath(prefix.concat(path));
        return result;
    }

    @Override
    public void register() {
        UploadHandlerFactory.register(this);
    }
}

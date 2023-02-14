package com.quick.file.handler.upload;

import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;

import java.util.List;

/**
 * @author CShisan
 */
public interface UploadPreCheckHandler {
    /**
     * 预检查
     *
     * @param suffix    文件后缀
     * @param whitelist 白名单
     */
    default void preCheck(String suffix, List<String> whitelist) {
        boolean support = whitelist.stream().anyMatch(suffix::equals);
        ExceptionHandler.assertTrue(support, CodeEnum.SERVICE_DATA_ERROR, "【文件上传服务】不支持该格式");
    }
}

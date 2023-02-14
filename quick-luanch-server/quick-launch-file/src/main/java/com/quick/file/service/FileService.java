package com.quick.file.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.file.dto.ExcelBatchWriteDTO;
import com.quick.file.dto.ExcelWriteDTO;
import com.quick.file.dto.FileUploadDTO;
import com.quick.file.enums.UploadMappingEnum;
import com.quick.file.factory.UploadHandlerFactory;
import com.quick.file.handler.excel.ExcelHandler;
import com.quick.file.handler.excel.WriteExcelHandler;
import com.quick.file.handler.upload.UploadHandler;
import com.quick.file.vo.FileUploadVO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.BiFunction;

/**
 * @author CShisan
 */
@Service
public class FileService {
    /**
     * 文件上传
     * {@link UploadMappingEnum}
     */
    public FileUploadVO<?> upload(FileUploadDTO dto) {
        UploadMappingEnum mapping = dto.getMapping();
        UploadHandler<?> handler = UploadHandlerFactory.classOf(mapping.getHandler());
        return handler.upload(dto);
    }

    /**
     * 导出
     */
    public void export(ExcelWriteDTO dto, Collection<?> collection) {
        ExceptionHandler.assertTrue(dto.necessityCheck(), CodeEnum.FAIL, "导出参数异常");
        ExcelHandler handler = new WriteExcelHandler();
        handler.write(dto, collection);
    }

    /**
     * 批量导出
     */
    public void batchExport(ExcelBatchWriteDTO dto, BiFunction<Long, Long, IPage<?>> function) {
        ExceptionHandler.assertTrue(dto.necessityCheck(), CodeEnum.FAIL, "批量导出参数异常");
        ExcelHandler handler = new WriteExcelHandler();
        handler.batchWrite(dto, function);
    }
}

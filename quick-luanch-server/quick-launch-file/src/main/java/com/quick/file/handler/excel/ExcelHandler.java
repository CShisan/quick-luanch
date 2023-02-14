package com.quick.file.handler.excel;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import com.quick.common.utils.CheckUtil;
import com.quick.file.dto.ExcelBatchWriteDTO;
import com.quick.file.dto.ExcelWriteDTO;
import org.apache.http.Consts;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.function.BiFunction;

/**
 * @author CShisan
 */
public interface ExcelHandler {
    /**
     * 写
     *
     * @param dto        dto
     * @param collection collection
     */
    default void write(ExcelWriteDTO dto, Collection<?> collection) {
        throw new ServiceException(CodeEnum.FAIL, "【Excel处理器】不支持该方法");
    }

    /**
     * 批量写
     *
     * @param dto      dto
     * @param function function
     */
    default void batchWrite(ExcelBatchWriteDTO dto, BiFunction<Long, Long, IPage<?>> function) {
        throw new ServiceException(CodeEnum.FAIL, "【Excel处理器】不支持该方法");
    }

    /**
     * 设置response
     *
     * @param response response
     * @param fileName 文件名
     */
    default void response(HttpServletResponse response, String fileName) {
        String name = new String(fileName.getBytes(), Consts.UTF_8);
        ContentDisposition disposition = ContentDisposition.attachment().filename(name).build();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(Consts.UTF_8.name());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());
    }

    /**
     * 添加WriterBuilder
     *
     * @param builder       ExcelWriterBuilder
     * @param writeHandlers writeHandlers
     */
    default void addWriteHandler(ExcelWriterBuilder builder, Collection<WriteHandler> writeHandlers) {
        if (CheckUtil.nonNull(writeHandlers)) {
            writeHandlers.forEach(builder::registerWriteHandler);
        }
    }
}
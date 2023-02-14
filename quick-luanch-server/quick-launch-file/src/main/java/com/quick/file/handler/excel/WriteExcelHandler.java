package com.quick.file.handler.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import com.quick.file.dto.ExcelBatchWriteDTO;
import com.quick.file.dto.ExcelWriteDTO;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.function.BiFunction;

/**
 * @author CShisan
 */
public class WriteExcelHandler implements ExcelHandler {
    @Override
    public void write(ExcelWriteDTO dto, Collection<?> collection) {
        try {
            // 设置响应头
            this.response(dto.getResponse(), dto.getFileName());

            // 构造writer
            ServletOutputStream os = dto.getResponse().getOutputStream();
            ExcelWriterBuilder builder = EasyExcel.write(os, dto.getClazz()).excelType(ExcelTypeEnum.XLSX);
            this.addWriteHandler(builder, dto.getWriteHandlers());

            // 写入
            try (ExcelWriter writer = builder.build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet(dto.getSheetName()).build();
                writer.write(collection, writeSheet);
            }
        } catch (IOException e) {
            throw new ServiceException(CodeEnum.FAIL, "Excel导出失败", e);
        }
    }

    @Override
    public void batchWrite(ExcelBatchWriteDTO dto, BiFunction<Long, Long, IPage<?>> function) {
        try {
            // 设置响应头
            this.response(dto.getResponse(), dto.getFileName());

            // 构造writer
            ServletOutputStream os = dto.getResponse().getOutputStream();
            ExcelWriterBuilder builder = EasyExcel.write(os, dto.getClazz()).excelType(ExcelTypeEnum.XLSX);
            this.addWriteHandler(builder, dto.getWriteHandlers());

            // 写入
            try (ExcelWriter writer = builder.build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet(dto.getSheetName()).build();
                Long readCounts = dto.getReadCounts();
                for (long current = 1, pages = 1; current <= pages; current++) {
                    IPage<?> page = function.apply(current, readCounts);
                    writer.write(page.getRecords(), writeSheet);
                    pages = page.getPages();
                }
            }
        } catch (IOException e) {
            throw new ServiceException(CodeEnum.FAIL, "Excel导出失败", e);
        }
    }
}

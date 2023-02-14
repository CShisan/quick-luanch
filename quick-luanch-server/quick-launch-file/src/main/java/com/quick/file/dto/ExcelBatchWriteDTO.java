package com.quick.file.dto;

import com.alibaba.excel.write.handler.WriteHandler;
import com.quick.common.utils.CheckUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author CShisan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Excel批量导出DTO")
public class ExcelBatchWriteDTO {
    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "表名称")
    private String sheetName;

    @Schema(description = "MODEL")
    private Class<?> clazz;

    @Schema(description = "响应体")
    private HttpServletResponse response;

    @Schema(description = "处理器")
    private List<WriteHandler> writeHandlers;

    @Schema(description = "每次读取数量")
    private Long readCounts;

    public boolean necessityCheck() {
        return CheckUtil.anyNonBlank(fileName, sheetName) && CheckUtil.allNonNull(clazz, response, readCounts);
    }
}
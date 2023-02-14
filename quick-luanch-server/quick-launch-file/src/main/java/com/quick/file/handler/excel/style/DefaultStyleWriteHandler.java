package com.quick.file.handler.excel.style;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.quick.common.utils.CheckUtil;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author yuanbai
 */
@Data
public class DefaultStyleWriteHandler implements CellWriteHandler {
    private WriteCellStyle headStyle;
    private List<WriteCellStyle> contentStyles;
    private static final int MAX_COLUMN_WIDTH = 255;
    private final Map<Integer, Map<Integer, Integer>> cache = MapUtils.newHashMapWithExpectedSize(8);

    public DefaultStyleWriteHandler() {
    }

    public DefaultStyleWriteHandler(WriteCellStyle headStyle, List<WriteCellStyle> contentStyles) {
        this.headStyle = headStyle;
        this.contentStyles = contentStyles;
    }

    public DefaultStyleWriteHandler(WriteCellStyle headStyle, WriteCellStyle contentStyle) {
        this.headStyle = headStyle;
        Optional.ofNullable(contentStyle).ifPresent(item -> this.contentStyles = ListUtils.newArrayList(contentStyle));
    }

    @Override
    public int order() {
        return -50000;
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (Optional.ofNullable(context.getHead()).orElse(false)) {
            this.setHeadCellStyle(context);
        } else {
            this.setContentCellStyle(context);
        }
        this.setColumnWidth(context);
    }

    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        if (this.processing(context) && CheckUtil.nonNull(this.headStyle)) {
            WriteCellData<?> cellData = context.getFirstCellData();
            WriteCellStyle.merge(this.headStyle, cellData.getOrCreateStyle());
        }
    }

    protected void setContentCellStyle(CellWriteHandlerContext context) {
        if (this.processing(context) && CheckUtil.nonEmpty(this.contentStyles)) {
            WriteCellData<?> cellData = context.getFirstCellData();
            int index = 0;
            if (Optional.ofNullable(context.getRelativeRowIndex()).map(item -> item > 0).orElse(false)) {
                index = context.getRelativeRowIndex() % this.contentStyles.size();
            }
            WriteCellStyle.merge(this.contentStyles.get(index), cellData.getOrCreateStyle());
        }
    }

    protected boolean processing(CellWriteHandlerContext context) {
        return CheckUtil.nonNull(context.getFirstCellData());
    }

    protected void setColumnWidth(CellWriteHandlerContext context) {
        this.setColumnWidth(context.getWriteSheetHolder(), context.getCellDataList(), context.getCell(), context.getHead());
    }

    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (!isHead && CheckUtil.isEmpty(cellDataList)) {
            return;
        }
        Map<Integer, Integer> maxColumnWidthMap = this.cache.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>(16));
        Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
        if (columnWidth >= 0) {
            columnWidth = Math.min(columnWidth, MAX_COLUMN_WIDTH);
            Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
            if (CheckUtil.isNull(maxColumnWidth) || columnWidth > maxColumnWidth) {
                maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
                writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
            }
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        }
        WriteCellData<?> cellData = cellDataList.get(0);
        CellDataTypeEnum type = cellData.getType();
        return Optional.ofNullable(type).map(item -> {
            switch (type) {
                case STRING:
                    return cellData.getStringValue().getBytes().length;
                case BOOLEAN:
                    return cellData.getBooleanValue().toString().getBytes().length;
                case NUMBER:
                    return cellData.getNumberValue().toString().getBytes().length;
                default:
                    return -1;
            }
        }).orElse(-1);
    }
}

package com.gms.entity.excel;

import com.gms.components.PixelUtils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 *
 * @author vvthanh
 */
public abstract class BaseView {

    protected CellStyle cellStyleBorder;

    protected Workbook workbook;
    protected String sheetName;

    protected short fontSize = 12;
    protected String fontName = "Times New Roman";
    protected boolean useStyle = true;

    public BaseView() {
        cellStyleBorder = null;
    }

    public void setUseStyle(boolean useStyle) {
        this.useStyle = useStyle;
    }

    protected Sheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName);
    }

    protected Sheet getSheet(String sheetName) {
        return workbook.getSheet(sheetName);
    }

    protected Sheet getCurrentSheet() {
        return workbook.getSheet(sheetName);
    }

    protected Row createRow() {
        return createRow(getCurrentSheet().getPhysicalNumberOfRows());
    }
    
    protected Row createRow(String sheetName) {
        return createRow(getSheet(sheetName).getPhysicalNumberOfRows());
    }

    protected Row createRow(int index) {
        Row row = getCurrentSheet().getRow(index);
        return row != null ? row : getCurrentSheet().createRow(index);
    }

    protected Row createTitleRow(String title) {
        Row row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        setFont(cell, Short.valueOf("20"), true, false, Font.U_NONE, IndexedColors.BLACK.getIndex());
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        return row;
    }

    protected Row createTitleDateRow(String title) {
        Row row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        setFont(cell, Short.valueOf("13"), true, true, Font.U_NONE, IndexedColors.BLACK.getIndex());
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        return row;
    }

    protected Row createTitleDateRow(String title, HorizontalAlignment alignment, int cellIndex) {
        Row row = createRow();
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(title);
        setFont(cell, Short.valueOf("13"), true, true, Font.U_NONE, IndexedColors.BLACK.getIndex());
        cell.getCellStyle().setAlignment(alignment);
        return row;
    }

    protected Row createFilterRow(String label, Object value, int cellIndex) {
        Row row = createRow();
        Cell cellLabel = row.createCell(cellIndex);
        cellLabel.setCellValue(label);
        setFont(cellLabel, fontSize, false, false, Font.U_NONE, IndexedColors.BLACK.getIndex());

        Cell cellValue = row.createCell(cellIndex + 1);
        if (value != null && (value.getClass() == Integer.class || value.getClass() == Float.class || value.getClass() == Double.class)) {
            cellValue.setCellValue(Double.valueOf(String.valueOf(value)));
        } else {
            cellValue.setCellValue(String.valueOf(value));
        }
        setFont(cellValue, fontSize, false, true, Font.U_NONE, IndexedColors.BLACK.getIndex());
        return row;
    }
    
    protected Row createNormalRow(String label,boolean bold, int cellIndex) {
        Row row = createRow();
        Cell cellLabel = row.createCell(cellIndex);
        cellLabel.setCellValue(label);
        setFont(cellLabel, fontSize, bold, false, Font.U_NONE, IndexedColors.BLACK.getIndex());
        return row;
    }

    protected Row createTableHeaderRow(String... labels) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);
        th.setBorderTop(BorderStyle.THIN);
        th.setBorderLeft(BorderStyle.THIN);
        th.setBorderRight(BorderStyle.THIN);
        th.setBorderBottom(BorderStyle.THIN);

        Row row = createRow();
        Cell cell;
        int cellIndex = 0;
        for (String label : labels) {
            if (label != null) {
                cell = createCell(row, cellIndex);
                cell.setCellValue(label);
                cell.setCellStyle(th);
            }
            cellIndex++;
        }
        return row;
    }

    private CellStyle getCellStyle() {
        if (cellStyleBorder == null) {
            cellStyleBorder = workbook.createCellStyle();
            cellStyleBorder.setBorderTop(BorderStyle.THIN);
            cellStyleBorder.setBorderLeft(BorderStyle.THIN);
            cellStyleBorder.setBorderRight(BorderStyle.THIN);
            cellStyleBorder.setBorderBottom(BorderStyle.THIN);
        }
        return cellStyleBorder;
    }

    protected Row createTableRow(Object... content) {
        Row row = createRow();
        Cell cell;
        int cellIndex = 0;
        for (Object col : content) {
            if (content != null) {
                cell = createCell(row, cellIndex);
                if (col != null && (col.getClass() == Integer.class || col.getClass() == Float.class || col.getClass() == Double.class)) {
                    cell.setCellValue(Double.valueOf(String.valueOf(col)));
                } else {
                    cell.setCellValue(String.valueOf(col));
                }
                cell.setCellStyle(getCellStyle());
            }
            cellIndex++;
        }
        return row;
    }

    protected Row createTableRowBold(Object... content) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.LEFT);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);
        th.setBorderTop(BorderStyle.THIN);
        th.setBorderLeft(BorderStyle.THIN);
        th.setBorderRight(BorderStyle.THIN);
        th.setBorderBottom(BorderStyle.THIN);

        CellStyle thNumber = workbook.createCellStyle();
        thNumber.setWrapText(true);
        thNumber.setAlignment(HorizontalAlignment.RIGHT);
        thNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        thNumber.setFont(font);
        thNumber.setBorderTop(BorderStyle.THIN);
        thNumber.setBorderLeft(BorderStyle.THIN);
        thNumber.setBorderRight(BorderStyle.THIN);
        thNumber.setBorderBottom(BorderStyle.THIN);

        Row row = createRow();
        Cell cell;
        int cellIndex = 0;
        for (Object col : content) {
            if (content != null) {
                cell = createCell(row, cellIndex);
                if (col != null && (col.getClass() == Integer.class || col.getClass() == Float.class || col.getClass() == Double.class)) {
                    cell.setCellValue(Double.valueOf(String.valueOf(col)));
                    cell.setCellStyle(thNumber);
                } else {
                    cell.setCellValue(String.valueOf(col));
                    cell.setCellStyle(th);
                }
            }
            cellIndex++;
        }
        return row;
    }

    protected Row createTableRowNoBorder(Object... content) {
        Row row = createRow();
        Cell cell;
        int cellIndex = 0;
        for (Object col : content) {
            if (content != null) {
                cell = createCell(row, cellIndex);
                cell.setCellValue(String.valueOf(col));
                setFont(cell, fontSize, false, false, Font.U_NONE, IndexedColors.BLACK.getIndex());
            }
            cellIndex++;
        }
        return row;
    }

    protected Row createTableEmptyRow(String label, int rowNumber) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setColor(IndexedColors.RED.getIndex());
        font.setBold(true);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);

        Row row = createRow();
        Cell cell = createCell(row, 0);
        cell.setCellValue("Không có thông tin");
        cell.setCellStyle(cellStyle);

        setMerge(row.getRowNum(), row.getRowNum(), 0, rowNumber - 1, true);
        return row;
    }

    protected Cell createCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            cell = row.createCell(index);
        }
        cell.getCellStyle().setBorderTop(BorderStyle.NONE);
        cell.getCellStyle().setBorderLeft(BorderStyle.NONE);
        cell.getCellStyle().setBorderRight(BorderStyle.NONE);
        cell.getCellStyle().setBorderBottom(BorderStyle.NONE);
        setFont(cell, fontSize, false, false, Font.U_NONE);
        return cell;
    }

    protected void setBold(Cell cell) {
        setFont(cell, fontSize, true, false, Font.U_NONE);
    }

    protected void setBold(Cell cell, short size) {
        setFont(cell, size, true, false, Font.U_NONE);
    }

    protected void setBold(Cell cell, short size, byte underline) {
        setFont(cell, size, true, false, underline);
    }

    protected void setBold(Cell cell, byte underline) {
        setFont(cell, fontSize, true, false, underline);
    }

    protected void setSize(Cell cell, short size) {
        setFont(cell, size, false, false, Font.U_NONE);
    }

    protected void setFont(Cell cell, short size, boolean bold, boolean italic, byte underline) {
        if (useStyle) {
            setFont(cell, size, bold, italic, underline, IndexedColors.BLACK.getIndex());
        }
    }

    protected void setFont(Cell cell, short size, boolean bold, boolean italic, byte underline, short color) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) size);
        font.setFontName(fontName);
        font.setColor(color);
        font.setBold(bold);
        font.setItalic(italic);
        font.setUnderline(underline);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        cell.setCellStyle(style);
    }

    protected void setComment(Cell cell, String message) {
        Drawing drawing = cell.getSheet().createDrawingPatriarch();
        CreationHelper factory = cell.getSheet().getWorkbook().getCreationHelper();

        // When the comment box is visible, have it show in a 1x3 space
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 1);
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + 1);
        anchor.setDx1(100);
        anchor.setDx2(1000);
        anchor.setDy1(100);
        anchor.setDy2(1000);

        // Create the comment and set the text+author
        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(message);
        comment.setString(str);
        comment.setAuthor("TURNUS");
        // Assign the comment to the cell
        cell.setCellComment(comment);
    }

    protected void setLink(Workbook wb, Cell cell, String address, HyperlinkType linkType) {
        CreationHelper helper = wb.getCreationHelper();
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.getIndex());
        style.setFont(font);

        Hyperlink link = helper.createHyperlink(linkType);
        link.setAddress(address);
        cell.setHyperlink(link);
        cell.setCellStyle(style);
    }

    /**
     *
     * @param sheet
     * @param numRow
     * @param untilRow
     * @param numCol
     * @param untilCol
     * @param border
     */
    protected void setMerge(Sheet sheet, int numRow, int untilRow, int numCol, int untilCol, boolean border) {
        CellRangeAddress cellMerge = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
        sheet.addMergedRegion(cellMerge);
        if (border) {
            setBordersToMergedCells(sheet, cellMerge);
        }
    }

    protected void setMerge(int numRow, int untilRow, int numCol, int untilCol, boolean border) {
        CellRangeAddress cellMerge = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
        getCurrentSheet().addMergedRegion(cellMerge);
        if (border) {
            setBordersToMergedCells(getCurrentSheet(), cellMerge);
        }
    }

    /**
     *
     * @param sheet
     * @param rangeAddress
     */
    protected void setBordersToMergedCells(Sheet sheet, CellRangeAddress rangeAddress) {
        RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
    }

    protected int getLastRowNumber() {
        return getCurrentSheet().getPhysicalNumberOfRows();
    }

    /**
     *
     * @param cell
     * @param top
     * @param left
     * @param right
     * @param bottom
     */
    protected void setBorders(Cell cell, BorderStyle top, BorderStyle left, BorderStyle right, BorderStyle bottom) {
        cell.getCellStyle().setBorderTop(top);
        cell.getCellStyle().setBorderLeft(left);
        cell.getCellStyle().setBorderRight(right);
        cell.getCellStyle().setBorderBottom(bottom);
    }

    /**
     *
     * @param cell
     */
    protected void setBorders(Cell cell) {
        setBorders(cell, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    /**
     * Tạo thư viện sheet
     *
     * @param options
     */
    protected void createOptions(HashMap<String, HashMap<String, String>> options) {
        Sheet sheet = createSheet("Thư viện");
        sheet.setRowBreak(0);
        Row row = null;
        Row subRow = null;
        Cell cell = null;
        int index = 0;

        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        for (Map.Entry<String, HashMap<String, String>> entry : options.entrySet()) {
            int i = 0;
            sheet.setColumnWidth(index + 1, PixelUtils.pixel2WidthUnits(410));
            HashMap<String, String> option = entry.getValue();
            row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            cell = row.createCell(row.getPhysicalNumberOfCells());
            cell.setCellValue(StringUtils.capitalize(option.get("").replaceAll("Chọn", "").trim().toLowerCase()));
            row.createCell(row.getPhysicalNumberOfCells());
            row.createCell(row.getPhysicalNumberOfCells());

            for (Map.Entry<String, String> item : option.entrySet()) {
                if (item.getKey().equals("")) {
                    continue;
                }
                i += 1;
                subRow = sheet.getRow(i);
                if (subRow == null) {
                    subRow = sheet.createRow(i);
                }
                cell = subRow.createCell(index);
                cell.setCellStyle(style);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.setCellValue(item.getKey());

                cell = subRow.createCell(index + 1);
                cell.setCellStyle(style);
                cell.setCellValue(item.getValue());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

                subRow.createCell(index + 2);
            }
            index += 3;
        }
    }

    protected void setColors(int numRow, int untilRow, int numCol, int untilCol, IndexedColors indexedColors) {
        Sheet currentSheet = getCurrentSheet();
        Row row;
        for (int r = numRow; r <= untilRow; r++) {
            row = currentSheet.getRow(r);
            for (int c = numCol; c <= untilCol; c++) {
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.cloneStyleFrom(row.getCell(c).getCellStyle());
                cellStyle.setFillForegroundColor(indexedColors.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                row.getCell(c).setCellStyle(cellStyle);
            }
        }
    }
}

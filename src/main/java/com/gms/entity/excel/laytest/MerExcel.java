package com.gms.entity.excel.laytest;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.laytest.MerForm;
import com.gms.entity.form.laytest.MerTableForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 *
 * @author NamAnh_HaUI
 */
public class MerExcel extends BaseView implements IExportExcel {

    private MerForm form;
    private String extension;

    public MerExcel(MerForm merForm, String extension) {
        this.form = merForm;
        this.extension = extension;
        this.sheetName = "BC_MER";
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable();

        workbook.write(out);
        return out.toByteArray();
    }

    @Override
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
    }

    /**
     * Tạo phần header cho file excel
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {

        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);

        //Dòng đầu tiên để trắng
        Row row = createRow();
        setMerge(1, 1, 1, 9, false);
        row = createRow();
        Cell cell = row.createCell(1);
        cell.setCellValue(form.getTitle());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        row = createRow();
        setMerge(2, 2, 1, 9, false);
        cell = row.createCell(1);
        cell.setCellValue(String.format("Từ %s đến %s", form.getStartDate(), form.getEndDate()));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        row = createRow();
        cell = row.createCell(1);
        cell.setCellValue(String.format("Tên nhân viên XN tại cộng đồng: %s", form.getStaffName()));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        cell = row.createCell(1);
        cell.setCellValue(String.format("Ngày báo cáo: %s", TextUtils.formatDate(new Date(), "dd/MM/yyyy")));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable() {

        Sheet sheet = getCurrentSheet();
        Row row = createRow();
        Cell cell;

        // Font cho header bảng
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        // Font cho title bảng
        Font fontTitle = workbook.createFont();
        fontTitle.setFontHeightInPoints(fontSize);
        fontTitle.setFontName(fontName);
        fontTitle.setBold(true);
        fontTitle.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());

        // Set cho header của bảng (normal)
        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);

        // Set cho header của bảng (dương tính)
        CellStyle bgDuongtinh = workbook.createCellStyle();
        bgDuongtinh.setFillBackgroundColor(IndexedColors.CORAL.getIndex());
        bgDuongtinh.setWrapText(true);
        bgDuongtinh.setAlignment(HorizontalAlignment.CENTER);
        bgDuongtinh.setVerticalAlignment(VerticalAlignment.CENTER);
        bgDuongtinh.setFont(font);

        // Set cho header của bảng (âm tính)
        CellStyle bgAmtinh = workbook.createCellStyle();
        bgAmtinh.setWrapText(true);
        bgAmtinh.setAlignment(HorizontalAlignment.CENTER);
        bgAmtinh.setVerticalAlignment(VerticalAlignment.CENTER);
        bgAmtinh.setFont(font);
        bgAmtinh.setFillBackgroundColor(IndexedColors.SEA_GREEN.getIndex());

        // Font cho cell của row trong bảng
        Font fontRowTbl = workbook.createFont();
        fontRowTbl.setFontHeightInPoints(fontSize);
        fontRowTbl.setFontName(fontName);
        fontRowTbl.setBold(false);

        // Style cho cell của row trong bảng
        CellStyle thRowTbl = workbook.createCellStyle();
        thRowTbl.setWrapText(true);
        thRowTbl.setAlignment(HorizontalAlignment.CENTER);
        thRowTbl.setVerticalAlignment(VerticalAlignment.CENTER);
        thRowTbl.setFont(fontRowTbl);

        // Set cho tiêu đề bảng
        CellStyle thTitle = workbook.createCellStyle();
        thTitle.setWrapText(false);
        thTitle.setAlignment(HorizontalAlignment.LEFT);
        thTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        thTitle.setFont(fontTitle);

        //Tiêu đề
        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Bảng 1: Số khách hàng nhận dịch vụ xét nghiệm tại cộng đồng và nhận kết quả theo nhóm giới tính và nhóm tuổi");
        cell.setCellStyle(thTitle);

        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Nhóm tuổi");
        cell.setCellStyle(th);
        setBorders(cell);

        cell = createCell(row, 4);

        cell.setCellValue("Dương tính");
        cell.setCellStyle(th);
        setBorders(cell);

        cell = createCell(row, 7);

        cell.setCellValue("Âm tính");
        cell.setCellStyle(th);
        setBorders(cell);

        row = createRow();
        cell = createCell(row, 4);
        cell.setCellValue("Nam");
        cell.setCellStyle(th);

        cell = createCell(row, 5);
        cell.setCellStyle(th);
        cell.setCellValue("Nữ");

        cell = createCell(row, 6);
        cell.setCellStyle(th);
        cell.setCellValue("Tổng");

        cell = createCell(row, 7);
        cell.setCellValue("Nam");
        cell.setCellStyle(th);

        cell = createCell(row, 8);
        cell.setCellStyle(th);
        cell.setCellValue("Nữ");

        cell = createCell(row, 9);
        cell.setCellStyle(th);
        cell.setCellValue("Tổng");
        int startTbl01 = getLastRowNumber();
        setMerge(7, 8, 1, 3, false);
        setMerge(7, 7, 4, 6, false);
        setMerge(7, 7, 7, 9, false);
        if (form.getTable01Forms() != null && form.getTable01Forms().size() > 0) {
            int index = getLastRowNumber();
            int startIndex = getLastRowNumber();
            for (MerTableForm item : form.getTable01Forms()) {

                row = createRow();
                cell = createCell(row, 1);
                cell.setCellValue(item.getAge().equals(" tuổi") ? "Không rõ" : item.getAge());
                cell.setCellStyle(thRowTbl);
                setBorders(cell);

                cell = createCell(row, 4);
                cell.setCellValue(item.getPositiveMale());
                cell.setCellStyle(thRowTbl);
                setBorders(cell);

                cell = createCell(row, 5);
                cell.setCellValue(item.getPositiveFemale());
                cell.setCellStyle(thRowTbl);
                setBorders(cell);

                cell = createCell(row, 6);
                cell.setCellValue(item.getPositive());
                cell.setCellStyle(thRowTbl);
                setBorders(cell);

                cell = createCell(row, 7);
                cell.setCellValue(item.getNegativeMale());
                cell.setCellStyle(thRowTbl);
                setBorders(cell);

                cell = createCell(row, 8);
                cell.setCellValue(item.getNegativeFemale());
                cell.setCellStyle(thRowTbl);
                setBorders(cell);

                cell = createCell(row, 9);
                cell.setCellValue(item.getNegative());
                cell.setCellStyle(thRowTbl);
                setBorders(cell);

                setMerge(index, index, 1, 3, false);

                index++;

            }
            //Dòng tổng
            startIndex = startIndex + 1;

            row = createRow();
            cell = createCell(row, 1);
            setBold(cell);
            setBorders(cell);
            cell.setCellValue("Tổng cộng:");
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

            cell = createCell(row, 4);
            setBold(cell);
            setBorders(cell);
            cell.setCellType(CellType.FORMULA);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            String fml = "SUM(E" + (startIndex) + ":E" + (index) + ")";
            cell.setCellFormula(fml);

            cell = createCell(row, 5);
            setBold(cell);
            setBorders(cell);
            cell.setCellType(CellType.FORMULA);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            fml = "SUM(F" + (startIndex) + ":F" + (index) + ")";
            cell.setCellFormula(fml);

            cell = createCell(row, 6);
            setBold(cell);
            setBorders(cell);
            cell.setCellType(CellType.FORMULA);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            fml = "SUM(G" + (startIndex) + ":G" + (index) + ")";
            cell.setCellFormula(fml);

            cell = createCell(row, 7);
            setBold(cell);
            setBorders(cell);
            cell.setCellType(CellType.FORMULA);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            fml = "SUM(H" + (startIndex) + ":H" + (index) + ")";
            cell.setCellFormula(fml);

            cell = createCell(row, 8);
            setBold(cell);
            setBorders(cell);
            cell.setCellType(CellType.FORMULA);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            fml = "SUM(I" + (startIndex) + ":I" + (index) + ")";
            cell.setCellFormula(fml);

            cell = createCell(row, 9);
            setBold(cell);
            setBorders(cell);
            cell.setCellType(CellType.FORMULA);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            fml = "SUM(J" + (startIndex) + ":J" + (index) + ")";
            cell.setCellFormula(fml);

            setMerge(index, index, 1, 3, false);
        }
        //Bảng 2
        //Tiêu đề
        row = createRow();
        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Bảng 2: Số khách hàng nhận dịch vụ xét nghiệm tại cộng đồng và nhận kết quả theo nhóm đối tượng");
        cell.setCellStyle(thTitle);

        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Nhóm đối tượng");
        cell.setCellStyle(th);
        setBorders(cell);

        cell = createCell(row, 4);

        cell.setCellValue("Dương tính");
        cell.setCellStyle(th);
        setBorders(cell);

        cell = createCell(row, 7);

        cell.setCellValue("Âm tính");
        cell.setCellStyle(th);
        setBorders(cell);
        int lastRow = getLastRowNum();
        row = createRow();
        cell = createCell(row, 4);
        cell.setCellValue("Nam");
        cell.setCellStyle(th);

        cell = createCell(row, 5);
        cell.setCellStyle(th);
        cell.setCellValue("Nữ");

        cell = createCell(row, 6);
        cell.setCellStyle(th);
        cell.setCellValue("Tổng");

        cell = createCell(row, 7);
        cell.setCellValue("Nam");
        cell.setCellStyle(th);

        cell = createCell(row, 8);
        cell.setCellStyle(th);
        cell.setCellValue("Nữ");

        cell = createCell(row, 9);
        cell.setCellStyle(th);
        cell.setCellValue("Tổng");

        setMerge(lastRow, lastRow + 1, 1, 3, false);
        setMerge(lastRow, lastRow, 4, 6, false);
        setMerge(lastRow, lastRow, 7, 9, false);
        int rowNum = getLastRowNumber();
        if (form.getTable02Forms() != null && form.getTable02Forms().size() > 0) {
            
            for (MerTableForm item : form.getTable02Forms()) {
                if (item.getPosition() > -1 && item.getoParentID() == 0) {
                    
                    row = createRow();
                    cell = createCell(row, 1);
                    cell.setCellValue(item.getoValue().equals("") ? "" : item.getoValue());
                    cell.getCellStyle().setWrapText(true);
                    setBorders(cell);

                    cell = createCell(row, 4);
                    cell.setCellValue(item.getPositiveMale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 5);
                    cell.setCellValue(item.getPositiveFemale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 6);
                    cell.setCellValue(item.getPositive());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 7);
                    cell.setCellValue(item.getNegativeMale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 8);
                    cell.setCellValue(item.getNegativeFemale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 9);
                    cell.setCellValue(item.getNegative());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    
                    setMerge(rowNum, rowNum, 1, 3, false);
                    rowNum++;
                } 
                
            }
            
        }
        int rowNumer = getLastRowNumber();
        if (form.getTable02Forms() != null && form.getTable02Forms().size() > 0) {
            for (MerTableForm lastItem : form.getTable02Forms()) {
                if (lastItem.getPosition() == -1 && lastItem.getoParentID() == 0 && lastItem.getTong() > 0) {
                    row = createRow();
                    cell = createCell(row, 1);
                    cell.setCellValue(lastItem.getoValue().equals("") ? "" : "  " + lastItem.getoValue());
                    cell.getCellStyle().setWrapText(true);
                    setBorders(cell);

                    cell = createCell(row, 4);
                    cell.setCellValue(lastItem.getPositiveMale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 5);
                    cell.setCellValue(lastItem.getPositiveFemale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 6);
                    cell.setCellValue(lastItem.getPositive());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 7);
                    cell.setCellValue(lastItem.getNegativeMale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 8);
                    cell.setCellValue(lastItem.getNegativeFemale());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);

                    cell = createCell(row, 9);
                    cell.setCellValue(lastItem.getNegative());
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    
                    setMerge(rowNumer, rowNumer, 1, 3, false);
                    rowNumer++;
                }
            }
        }
        setBordersToMergedCells(sheet);
    }

    /**
     * Set border cho merged cells
     *
     * @param sheet
     */
    private void setBordersToMergedCells(Sheet sheet) {
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (CellRangeAddress rangeAddress : mergedRegions) {
            if (rangeAddress.isInRange(1, 3)
                    || rangeAddress.isInRange(2, 3)
                    || rangeAddress.isInRange(3, 3)
                    || rangeAddress.isInRange(4, 3)
                    || rangeAddress.isInRange(5, 3)
                    || rangeAddress.isInRange(6, 3)) {
                continue;
            }
            RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
        }
    }

    /**
     * Get the very last used row
     *
     * @return
     */
    public int getLastRowNum() {
        int rowCount = 0;
        rowCount = this.getCurrentSheet().getLastRowNum();
        if (rowCount == 0) {
            rowCount = this.getCurrentSheet().getPhysicalNumberOfRows();
        }
        return rowCount;
    }
}

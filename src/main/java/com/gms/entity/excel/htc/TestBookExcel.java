/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.excel.htc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc.TestBookForm;
import com.gms.entity.form.htc.TestBookTableForm;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 *
 * @author Admin
 */
public class TestBookExcel extends BaseView implements IExportExcel {
    
    private TestBookForm form;
    private String extension;
    
    public TestBookExcel(TestBookForm form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Sổ XN HIV-2674 QĐ-BYT";
    }
    
    /**
     * Set border for merged region
     * @param workBook
     * @param sheet 
     */
    private void setBordersToMergedCells(Workbook workBook, Sheet sheet, List<Integer> rowIgnores) {
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (CellRangeAddress rangeAddress : mergedRegions) {
            for (int index : rowIgnores) {
                if (rangeAddress.containsRow(index)) {
                    continue;
                }
                RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
            }
            
        }
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

    /**
     * Tạo phần header cho sổ tvxn
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {

        Sheet currentSheet = getCurrentSheet();
        
        // Setting style
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        CellStyle noBorder = workbook.createCellStyle();
        noBorder.setFont(font);
        noBorder.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);
        th.setBorderTop(BorderStyle.THIN);
        th.setBorderRight(BorderStyle.THIN);
        th.setBorderBottom(BorderStyle.THIN);
        th.setBorderLeft(BorderStyle.THIN);

        CellStyle th_rotate = workbook.createCellStyle();
        th_rotate.setWrapText(true);
        th_rotate.setAlignment(HorizontalAlignment.CENTER);
        th_rotate.setVerticalAlignment(VerticalAlignment.CENTER);
        th_rotate.setFont(font);
        th_rotate.setRotation((short) 90);
        th_rotate.setBorderTop(BorderStyle.THIN);
        th_rotate.setBorderRight(BorderStyle.THIN);
        th_rotate.setBorderBottom(BorderStyle.THIN);
        th_rotate.setBorderLeft(BorderStyle.THIN);

        // Set width for row
        currentSheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(250));
        currentSheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(100));
        currentSheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(100));
        currentSheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(150));
        
        //Dòng đầu tiên để trắng
        Row row = createRow();

        row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue("Tên cơ sở tư vấn xét nghiệm: " + form.getSiteName());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        setMerge(2, 2, 0, 12, false);
        cell = row.createCell(0);
        cell.setCellValue(form.getTitle().toUpperCase());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Thời gian từ đến
        Font createFont = workbook.createFont();
        createFont.setFontName("Times New Roman");
        createFont.setFontHeightInPoints((short) 12);
        
        row = createRow();
        cell = row.createCell(4);
        cell.setCellValue("TỪ");
        cell.setCellStyle(noBorder);
        
        cell = row.createCell(5); 
        setMerge(3, 3, 5, 6, true);
        cell.setCellValue(form.getStart());
        cell.getCellStyle().setFont(createFont);
        
        cell = row.createCell(7);
        cell.setCellValue("ĐẾN");
        cell.setCellStyle(noBorder);
        
        cell = row.createCell(8); 
        setMerge(3, 3, 8, 9, true);
        cell.setCellValue(form.getEnd());
        cell.getCellStyle().setFont(createFont);
        
    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable(){
        
        Sheet currentSheet = getCurrentSheet();
        
        // Setting style
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        CellStyle noBorder = workbook.createCellStyle();
        noBorder.setFont(font);
        noBorder.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);
        th.setBorderTop(BorderStyle.THIN);
        th.setBorderRight(BorderStyle.THIN);
        th.setBorderBottom(BorderStyle.THIN);
        th.setBorderLeft(BorderStyle.THIN);

        CellStyle th_rotate = workbook.createCellStyle();
        th_rotate.setWrapText(true);
        th_rotate.setAlignment(HorizontalAlignment.CENTER);
        th_rotate.setVerticalAlignment(VerticalAlignment.CENTER);
        th_rotate.setFont(font);
        th_rotate.setRotation((short) 90);
        th_rotate.setBorderTop(BorderStyle.THIN);
        th_rotate.setBorderRight(BorderStyle.THIN);
        th_rotate.setBorderBottom(BorderStyle.THIN);
        th_rotate.setBorderLeft(BorderStyle.THIN);

        // Nội dung bảng
        Row row = createRow();
        Cell cell = row.createCell(0);
        
        
        row.setHeightInPoints(50);

        cell = row.createCell(0);
        setMerge(4, 6, 0, 0, false);
        cell.setCellValue("TT");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(1);
        setMerge(4, 4, 1, 6, false);
        cell.setCellValue("THÔNG TIN KHÁCH HÀNG");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(7);
        setMerge(4, 6, 7, 7, false);
        cell.setCellValue("NGÀY LẤY MẪU");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(8);
        setMerge(4, 6, 8, 8, false);
        cell.setCellValue("Ngày xét nghiệm");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(9);
        cell.setCellValue("KẾT QUẢ XÉT NGHIỆM");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(10);
        setMerge(4, 6, 10, 10, false);
        cell.setCellValue("KẾT LUẬN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(11);
        setMerge(4, 6, 11, 11, false);
        cell.setCellValue("KẾT QUẢ KHẲNG ĐỊNH");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(12);
        setMerge(4, 6, 12, 12, false);
        cell.setCellValue("GHI CHÚ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(13);
        setMerge(4, 6, 13, 13, false);
        cell.setCellValue("TƯ VẤN VIÊN TRƯỚC XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Dòng nhỏ
        row = createRow();
        row.setHeightInPoints(50);

        cell = row.createCell(1);
        setMerge(5, 6, 1, 1, false);
        cell.setCellValue("HỌ TÊN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(2);
        setMerge(5, 6, 2, 2, false);
        cell.setCellValue("MÃ SỐ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(3);
        setMerge(5, 5, 3, 4, false);
        cell.setCellValue("NĂM SINH");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(5);
        setMerge(5, 6, 5, 5, false);
        cell.setCellValue("ĐỊA CHỈ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(6);
        setMerge(5, 6, 6, 6, false);
        cell.setCellValue("ĐỐI TƯỢNG");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(9);
        setMerge(5, 6, 9, 9, false);
        cell.setCellValue("SP\n" +
"(ghi rõ tên sinh phẩm)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        row = createRow();
        row.setHeightInPoints(50);

        cell = row.createCell(3);
        cell.setCellValue("Nam");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(4);
        cell.setCellValue("Nữ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        row = createRow();
        row.setHeightInPoints(50);
        
        cell = row.createCell(0);
        cell.setCellValue("(1)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(1);
        cell.setCellValue("(2)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(2);
        cell.setCellValue("(3)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(3);
        cell.setCellValue("(4)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(4);
        cell.setCellValue("(5)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(5);
        cell.setCellValue("(6)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(6);
        cell.setCellValue("(7)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(7);
        cell.setCellValue("(8)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(8);
        cell.setCellValue("(9)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(9);
        cell.setCellValue("(10)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(10);
        cell.setCellValue("(11)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(11);
        cell.setCellValue("(12)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(12);
        cell.setCellValue("(13)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(13);
        cell.setCellValue("(14)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        setBordersToMergedCells(workbook, currentSheet, Arrays.asList(2));
        
        // Load data records
        int index = 0;
        if (form.getTable() != null) {
            for (TestBookTableForm testForm : form.getTable()) {

                row = createRow();
                cell = createCell(row, 0);
                setBorders(cell);
                cell.setCellValue(index += 1);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 1);
                setBorders(cell);
                cell.setCellValue(testForm.getPatientName());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 2);
                setBorders(cell);
                cell.setCellValue(testForm.getCode());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 3);
                setBorders(cell);
                cell.setCellValue(testForm.getGender().equals("1") ? testForm.getYearOfBirth() : "" );
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 4);
                setBorders(cell);
                cell.setCellValue(testForm.getGender().equals("2") ? testForm.getYearOfBirth() : "" );
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 5);
                setBorders(cell);
                cell.setCellValue(testForm.getPermanentAddress());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setWrapText(true);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                //Địa chỉ
                cell = createCell(row, 6);
                setBorders(cell);
                cell.setCellValue(testForm.getObjectGroupID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 7);
                setBorders(cell);
                cell.setCellValue(testForm.getPreTestTime());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 8);
                setBorders(cell);
                cell.setCellValue(testForm.getPreTestTime());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 9);
                setBorders(cell);
                StringBuilder result = new StringBuilder();
                result.append(StringUtils.isNotEmpty(testForm.getBioNameResult()) ? testForm.getBioNameResult() : "");
                result.append(" \n");
                result.append(StringUtils.isNotEmpty(testForm.getBioName()) ? testForm.getBioName() : "");
                cell.setCellValue(result.toString());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);

                cell = createCell(row, 10);
                setBorders(cell);
                cell.setCellValue(testForm.getTestResultsID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);

                cell = createCell(row, 11);
                setBorders(cell);
                cell.setCellValue(testForm.getConfirmResultID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 12);
                setBorders(cell);
                cell.setCellValue("");
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 13);
                setBorders(cell);
                cell.setCellValue(testForm.getStaffBeforeTestID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

            }
        } else {
            int lastRowNum = getLastRowNumber();
            row = createRow();
            cell = createCell(row, 0);
            setBorders(cell);
            cell.setCellValue("Không có dữ liệu");
            setMerge(lastRowNum, lastRowNum, 0, 12, true);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }
        
    }
    
    @Override
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
    }
}

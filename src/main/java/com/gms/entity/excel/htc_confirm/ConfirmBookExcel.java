/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.excel.htc_confirm;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc_confirm.ConfirmBookForm;
import com.gms.entity.form.htc_confirm.ConfirmBookTableForm;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
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
public class ConfirmBookExcel extends BaseView implements IExportExcel  {
    private ConfirmBookForm form;
    private String extension;

    public ConfirmBookExcel(ConfirmBookForm form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Sổ XN HIV-2674 QĐ-BYT";
    }
    
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
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
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


        // Set width for row
        currentSheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(115));
        currentSheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(380));
        currentSheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(250));
        currentSheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(250));
        currentSheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(19, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(20, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(21, PixelUtils.pixel2WidthUnits(200));
        currentSheet.setColumnWidth(22, PixelUtils.pixel2WidthUnits(200));
        
        //Dòng đầu tiên để trắng
        Row row = createRow();


        row = createRow();
        setMerge(1, 1, 0, 22, false);
        Cell cell = row.createCell(0);
        cell.setCellValue(form.getTitle().toUpperCase());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Từ ngày %s đến ngày %s", form.getStart(), form.getEnd()));
        setFont(cell, fontSize, true, true, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        setMerge(2, 2, 0, 22, false);

        // Thời gian từ đến
        Font createFont = workbook.createFont();
        createFont.setFontName("Times New Roman");
        createFont.setFontHeightInPoints((short) 12);
    }
     
    private void createTable() throws Exception {
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

        Row row = createRow();
        row = createRow();
        row = createRow();
        Cell cell = row.createCell(0);
        row.setHeightInPoints(50);
        setMerge(5, 7, 0, 0, false);
        cell.setCellValue("TT");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(1);
        setMerge(5, 5, 1, 6, false);
        cell.setCellValue("THÔNG TIN KHÁCH HÀNG\n" +
                            "(từ cơ sở gửi mẫu)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(7);
        setMerge(5, 7, 7, 7, false);
        cell.setCellValue("ĐƠN VỊ GỬI MẪU");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(8);
        setMerge(5, 7, 8, 8, false);
        cell.setCellValue("NGÀY XN KHẲNG ĐỊNH");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        
        
        cell = row.createCell(9);
        setMerge(5, 5, 9, 14, false);
        cell.setCellValue("KẾT QUẢ XÉT NGHIỆM");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(15);
        setMerge(5, 7, 15, 15, false);
        cell.setCellValue("KẾT LUẬN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(16);
        setMerge(5, 7, 16, 16, false);
        cell.setCellValue("MÃ SỐ LƯU MẪU XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(17);
        setMerge(5, 7, 17, 17, false);
        cell.setCellValue("MÃ SỐ BN của PXN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(18);
        setMerge(5, 6, 18, 19, false);
        cell.setCellValue("XN NHANH");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(20);
        setMerge(5, 6, 20, 21, false);
        cell.setCellValue("XN TẢI LƯỢNG VIRUS");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(22);
        setMerge(5, 7, 22, 22, false);
        cell.setCellValue("GHI CHÚ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        row = createRow();
        row.setHeightInPoints(50);
        cell = row.createCell(1);
        setMerge(6, 7, 1, 1, false);
        cell.setCellValue("HỌ TÊN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(2);
        setMerge(6, 7, 2, 2, false);
        cell.setCellValue("MÃ SỐ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(3);
        setMerge(6, 6, 3, 4, false);
        cell.setCellValue("NĂM SINH");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(5);
        setMerge(6, 7, 5, 5, false);
        cell.setCellValue("ĐỊA CHỈ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(6);
        setMerge(6, 7, 6, 6, false);
        cell.setCellValue("ĐỐI TƯỢNG");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(9);
        setMerge(6, 6, 9, 10, false);
        cell.setCellValue("SP1\n" +
                            "(ghi rõ tên sinh phẩm)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(11);
        setMerge(6, 6, 11, 12, false);
        cell.setCellValue("SP2\n" +
                            "(ghi rõ tên sinh phẩm)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(13);
        setMerge(6, 6, 13, 14, false);
        cell.setCellValue("SP3\n" +
                            "(ghi rõ tên sinh phẩm)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        row = createRow();
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
        
        cell = row.createCell(9);
        cell.setCellValue("Ngày XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(10);
        cell.setCellValue("Kết quả XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(11);
        cell.setCellValue("Ngày XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(12);
        cell.setCellValue("Kết quả XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(13);
        cell.setCellValue("Ngày XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(14);
        cell.setCellValue("Kết quả XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(18);
        cell.setCellValue("Ngày XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(19);
        cell.setCellValue("Kết quả XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(20);
        cell.setCellValue("Ngày XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        cell = row.createCell(21);
        cell.setCellValue("Kết quả XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        setBordersToMergedCells(workbook, currentSheet, Arrays.asList(1,2));
        row = createRow();
        row.setHeightInPoints(25);
        cell = createCell(row, 0);
        setBorders(cell);
        cell.setCellValue("(1)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 1);
        setBorders(cell);
        cell.setCellValue("(2)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.getCellStyle().setWrapText(true);

        cell = createCell(row, 2);
        setBorders(cell);
        cell.setCellValue("(3)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 3);
        setBorders(cell);
        cell.setCellValue("(4)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 4);
        setBorders(cell);
        cell.setCellValue("(5)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 5);
        setBorders(cell);
        cell.setCellValue("(6)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.getCellStyle().setWrapText(true);

        cell = createCell(row, 6);
        setBorders(cell);
        cell.setCellValue("(7)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.getCellStyle().setWrapText(true);

        cell = createCell(row, 7);
        setBorders(cell);
        cell.setCellValue("(8)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.getCellStyle().setWrapText(true);

        cell = createCell(row, 8);
        setBorders(cell);
        cell.setCellValue("(9)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 9);
        setBorders(cell);
        cell.setCellValue("(10)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 10);
        setBorders(cell);
        cell.setCellValue("(11)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);


        cell = createCell(row, 11);
        setBorders(cell);
        cell.setCellValue("(12)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.getCellStyle().setWrapText(true);

        cell = createCell(row, 12);
        setBorders(cell);
        cell.setCellValue("(13)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 13);
        setBorders(cell);
        cell.setCellValue("(14)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.getCellStyle().setWrapText(true);

        cell = createCell(row, 14);
        setBorders(cell);
        cell.setCellValue("(15)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 15);
        setBorders(cell);
        cell.setCellValue("(16)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.getCellStyle().setWrapText(true);

        cell = createCell(row, 16);
        setBorders(cell);
        cell.setCellValue("(17)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 17);
        setBorders(cell);
        cell.setCellValue("(18)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 18);
        setBorders(cell);
        cell.setCellValue("(19)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        
        cell = createCell(row, 19);
        setBorders(cell);
        cell.setCellValue("(20)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        
        cell = createCell(row, 20);
        setBorders(cell);
        cell.setCellValue("(21)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        
        cell = createCell(row, 21);
        setBorders(cell);
        cell.setCellValue("(22)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        
        cell = createCell(row, 22);
        setBorders(cell);
        cell.setCellValue("(23)");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        
        int index = 0;
        if (form.getTable() != null && form.getTable().size() > 0) {
            
            for (ConfirmBookTableForm confirmForm : form.getTable()) {
                
                row = createRow();
                cell = createCell(row, 0);
                setBorders(cell);
                cell.setCellValue(index += 1);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 1);
                setBorders(cell);
                cell.setCellValue(confirmForm.getFullname());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 2);
                setBorders(cell);
                cell.setCellValue(confirmForm.getCode());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 3);
                setBorders(cell);
                if(confirmForm.getYearOfBirth() != null){
                    cell.setCellValue(confirmForm.getGender().equals("1") ? String.valueOf(confirmForm.getYearOfBirth()) : "" );
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 4);
                setBorders(cell);
                if(confirmForm.getYearOfBirth() != null){
                    cell.setCellValue(confirmForm.getGender().equals("2") ? String.valueOf(confirmForm.getYearOfBirth()) : "");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 5);
                setBorders(cell);
                cell.setCellValue(confirmForm.getAddressFull());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 6);
                setBorders(cell);
                cell.setCellValue(confirmForm.getObjectGroupID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 7);
                setBorders(cell);
                cell.setCellValue(confirmForm.getSourceSiteID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 8);
                setBorders(cell);
                cell.setCellValue(confirmForm.getConfirmTime());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                
                cell = createCell(row, 9);
                setBorders(cell);
                cell.setCellValue(confirmForm.getFirstBioDate());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                
                cell = createCell(row, 10);
                setBorders(cell);
                cell.setCellValue(confirmForm.getBioNameResult1() + "\n" + confirmForm.getBioName1());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 11);
                setBorders(cell);
                cell.setCellValue(confirmForm.getSecondBioDate());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 12);
                setBorders(cell);
                cell.setCellValue(confirmForm.getBioNameResult2() + "\n" + confirmForm.getBioName2());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 13);
                setBorders(cell);
                cell.setCellValue(confirmForm.getThirdBioDate());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 14);
                setBorders(cell);
                cell.setCellValue(confirmForm.getBioNameResult3() + "\n" + confirmForm.getBioName3());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 15);
                setBorders(cell);
                cell.setCellValue(confirmForm.getResultID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 16);
                setBorders(cell);
                cell.setCellValue(confirmForm.getSampleSaveCode());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 17);
                setBorders(cell);
                cell.setCellValue(confirmForm.getSourceID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 18);
                setBorders(cell);
                cell.setCellValue(confirmForm.getEarlyHivDate());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 19);
                setBorders(cell);
                cell.setCellValue(confirmForm.getEarlyHiv());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 20);
                setBorders(cell);
                cell.setCellValue(confirmForm.getVirusLoadDate());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, 21);
                setBorders(cell);
                cell.setCellValue(confirmForm.getVirusLoad());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, 22);
                setBorders(cell);
                cell.setCellValue(confirmForm.getNote());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            }
        } else {
            row = createRow();
            cell = createCell(row, 0);
            setBorders(cell);
            cell.setCellValue("Không có dữ liệu");
            setMerge(9, 9, 0, 22, true);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }
    }
}

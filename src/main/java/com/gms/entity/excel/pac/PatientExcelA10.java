/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacPatientForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
 * @author Admin
 */
public class PatientExcelA10 extends BaseView implements IExportExcel {

    private PacPatientForm form;
    private String extension;

    public PatientExcelA10(PacPatientForm form, String extension) {
//        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "So A10_YTCS";
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

    private void createHeader() throws Exception {
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        
        HashMap<String, String> labels = (new PacPatientInfoEntity()).getAttributeLabels();
        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(150));

        //Dòng đầu tiên để trắng
        Row row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue("Sổ A10/YTCS");
        cell.getCellStyle().setFont(font);
        setBold(cell);

        row = createRow();
        setMerge(1, 1, 0, 12, false);
        cell = row.createCell(0);
        cell.setCellValue(form.getTitle().toUpperCase());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        
        row = createRow();
        setMerge(2, 2, 0, 12, false);
        cell = row.createCell(0);
        cell.setCellValue("Từ " + form.getStartDate() + " đến " + form.getEndDate());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        row = createRow();

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue("Đơn vị báo cáo: ");
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        setBold(cell);

        cell = row.createCell(2);
        cell.setCellValue(form.getSiteName());
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        //Họ tên
        if (form.getSearch().getFullname() != null && !"".equals(form.getSearch().getFullname())) {
            row = createRow();

            cell = row.createCell(0);
            cell.setCellValue(labels.get("fullname") + ":");
            setBold(cell);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

            cell = row.createCell(2);
            cell.setCellValue(form.getSearch().getFullname());
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }

        //Năm sinh
        if (form.getSearch().getYearOfBirth() != null && !"".equals(form.getSearch().getYearOfBirth())) {
            row = createRow();

            cell = row.createCell(0);
            cell.setCellValue(labels.get("yearOfBirth") + ":");
            setBold(cell);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

            cell = row.createCell(2);
            cell.setCellValue(form.getSearch().getYearOfBirth());
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }

        if (form.getSearch().getGenderID() != null && !"".equals(form.getSearch().getGenderID())) {
            row = createRow();

            cell = row.createCell(0);
            cell.setCellValue(labels.get("genderID") + ":");
            setBold(cell);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

            cell = row.createCell(2);
            cell.setCellValue(form.getOptions().get(ParameterEntity.GENDER).get(form.getSearch().getGenderID()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }

        //CMND
        if (form.getSearch().getIdentityCard() != null && !"".equals(form.getSearch().getIdentityCard())) {
            row = createRow();

            cell = row.createCell(0);
            cell.setCellValue(labels.get("identityCard") + ":");
            setBold(cell);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

            cell = row.createCell(2);
            cell.setCellValue(form.getSearch().getIdentityCard());
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }

        //BHYT
        if (form.getSearch().getHealthInsuranceNo() != null && !"".equals(form.getSearch().getHealthInsuranceNo())) {
            row = createRow();

            cell = row.createCell(0);
            cell.setCellValue(labels.get("healthInsuranceNo") + ":");
            setBold(cell);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

            cell = row.createCell(2);
            cell.setCellValue(form.getSearch().getHealthInsuranceNo());
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }

        if (form.getSearch().getObjectGroupID() != null && !"".equals(form.getSearch().getObjectGroupID())) {
            row = createRow();

            cell = row.createCell(0);
            cell.setCellValue(labels.get("objectGroupID") + ":");
            setBold(cell);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

            cell = row.createCell(2);
            cell.setCellValue(form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(form.getSearch().getObjectGroupID()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }

        row = createRow();

        cell = row.createCell(0);
        cell.setCellValue("Ngày xuất báo cáo: ");
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        setBold(cell);

        cell = row.createCell(2);
        cell.setCellValue(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
    }

    private void createTable() throws Exception {
        Sheet sheet = getCurrentSheet();
        Row row = createRow();
        Cell cell;

        // Font cho header bảng
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);
        

        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);
        
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

        int rowIndex = getLastRowNumber();
        //Tiêu đề
        row = createRow();
        cell = createCell(row, 0);
        cell.setCellValue("TT");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 0, 0, false);

        cell = createCell(row, 1);
        cell.setCellValue("Ngày vào sổ");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 1, 1, false);

        cell = createCell(row, 2);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 2, 2, false);

        cell = createCell(row, 3);
        cell.setCellValue("Năm sinh");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex, 3, 4, false);
        

        cell = createCell(row, 5);
        cell.setCellValue("Dân tộc");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 5, 5, false);

        cell = createCell(row, 6);
        cell.setCellValue("Địa chỉ thường trú");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 6, 6, false);

        cell = createCell(row, 7);
        cell.setCellValue("Địa chỉ hiện tại");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 7, 7, false);

        cell = createCell(row, 8);
        cell.setCellValue("Số CMND");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 8, 8, false);

        cell = createCell(row, 9);
        cell.setCellValue("Số thẻ BHYT");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 9, 9, false);

        cell = createCell(row, 10);
        cell.setCellValue("Đối tượng*");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex, 10, 11, false);

        cell = createCell(row, 12);
        cell.setCellValue("Ngày xét nghiệm khẳng định");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 12, 12, false);

        cell = createCell(row, 13);
        cell.setCellValue("Nơi khẳng định nhiễm HIV");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 13, 13, false);

        cell = createCell(row, 14);
        cell.setCellValue("Nơi quản lý điều trị ARV");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 14, 14, false);

        cell = createCell(row, 15);
        cell.setCellValue("Ngày BĐ điều trị");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 15, 15, false);

        cell = createCell(row, 16);
        cell.setCellValue("Ngày tử vong");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 16, 16, false);

        cell = createCell(row, 17);
        cell.setCellValue("Ghi chú");
        cell.setCellStyle(th);
        setBorders(cell);
        setMerge(rowIndex, rowIndex + 1, 17, 17, false);

        row = createRow();
        cell = createCell(row, 3);
        cell.setCellValue("Nam");
        cell.setCellStyle(th);

        cell = createCell(row, 4);
        cell.setCellValue("Nữ");
        cell.setCellStyle(th);

        cell = createCell(row, 10);
        cell.setCellValue("Đtượng");
        cell.setCellStyle(th);

        cell = createCell(row, 11);
        cell.setCellValue("TĐó: TG có thai");
        cell.setCellStyle(th);

        row = createRow();
        cell = createCell(row, 0);
        cell.setCellValue("(1)");
        cell.setCellStyle(th);
        cell = createCell(row, 1);
        cell.setCellValue("(2)");
        cell.setCellStyle(th);
        cell = createCell(row, 2);
        cell.setCellValue("(3)");
        cell.setCellStyle(th);
        cell = createCell(row, 3);
        cell.setCellValue("(4)");
        cell.setCellStyle(th);
        cell = createCell(row, 4);
        cell.setCellValue("(5)");
        cell.setCellStyle(th);
        cell = createCell(row, 5);
        cell.setCellValue("(6)");
        cell.setCellStyle(th);
        cell = createCell(row, 6);
        cell.setCellValue("(7)");
        cell.setCellStyle(th);
        cell = createCell(row, 7);
        cell.setCellValue("(8)");
        cell.setCellStyle(th);
        cell = createCell(row, 8);
        cell.setCellValue("(9)");
        cell.setCellStyle(th);
        cell = createCell(row, 9);
        cell.setCellValue("(10)");
        cell.setCellStyle(th);
        cell = createCell(row, 10);
        cell.setCellValue("(11)");
        cell.setCellStyle(th);
        cell = createCell(row, 11);
        cell.setCellValue("(12)");
        cell.setCellStyle(th);
        cell = createCell(row, 12);
        cell.setCellValue("(13)");
        cell.setCellStyle(th);
        cell = createCell(row, 13);
        cell.setCellValue("(14)");
        cell.setCellStyle(th);
        cell = createCell(row, 14);
        cell.setCellValue("(15)");
        cell.setCellStyle(th);
        cell = createCell(row, 15);
        cell.setCellValue("(16)");
        cell.setCellStyle(th);
        cell = createCell(row, 16);
        cell.setCellValue("(17)");
        cell.setCellStyle(th);
        cell = createCell(row, 17);
        cell.setCellValue("(18)");
        cell.setCellStyle(th);

        int index = 0;
        
        if (form.getDataPage().getData() != null && form.getDataPage().getData().size() > 0) {
            for (PacPatientInfoEntity item : form.getDataPage().getData()) {
                int count = getLastRowNumber();
                row = createRow();
                cell = createCell(row, 0);
                cell.setCellValue(index += 1);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                setMerge(count, count + 4, 0, 0, false);
                
                cell = createCell(row, 1);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue(TextUtils.formatDate(item.getReviewProvinceTime(), "dd/MM/yyyy"));
                setMerge(count, count + 4, 1, 1, false);

                cell = createCell(row, 2);
                setBorders(cell);
                cell.setCellValue(StringUtils.isEmpty(item.getFullname()) ? "" : item.getFullname());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                setMerge(count, count + 4, 2, 2, false);

                cell = createCell(row, 3);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue("1".equals(item.getGenderID()) ? ((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth())) : "");
                setMerge(count, count + 4, 3, 3, false);
                
                cell = createCell(row, 4);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue("2".equals(item.getGenderID()) ? ((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth())) : "");
                setMerge(count, count + 4, 4, 4, false);

                cell = createCell(row, 5);
                setBorders(cell);
                cell.setCellValue(StringUtils.isEmpty(item.getRaceID()) ? "" : form.getOptions().get(ParameterEntity.RACE).get(item.getRaceID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                setMerge(count, count + 4, 5, 5, false);

                cell = createCell(row, 6);
                setBorders(cell);
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentAddressFull()) ? "" : item.getPermanentAddressFull());

                cell = createCell(row, 7);
                setBorders(cell);
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentAddressFull()) ? "" : item.getCurrentAddressFull());
                
                cell = createCell(row, 8);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue(StringUtils.isEmpty(item.getIdentityCard()) ? "" : item.getIdentityCard());

                cell = createCell(row, 9);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue(StringUtils.isEmpty(item.getHealthInsuranceNo()) ? "" : item.getHealthInsuranceNo());

                cell = createCell(row, 10);
                setBorders(cell);
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(item.getObjectGroupID()));

                cell = createCell(row, 11);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue("");

                cell = createCell(row, 12);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue(item.getConfirmTime()== null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                setMerge(count, count + 4, 12, 12, false);
                
                cell = createCell(row, 13);
                setBorders(cell);
                cell.setCellValue(StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                setMerge(count, count + 4, 13, 13, false);
                
                cell = createCell(row, 14);
                setBorders(cell);
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                cell.setCellValue(StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID()));
                
                cell = createCell(row, 15);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue(item.getStartTreatmentTime()== null ? "" : TextUtils.formatDate(item.getStartTreatmentTime(), "dd/MM/yyyy"));
                
                cell = createCell(row, 16);
                cell.setCellStyle(thRowTbl);
                setBorders(cell);
                cell.setCellValue(item.getDeathTime() == null ? "" : TextUtils.formatDate(item.getDeathTime(), "dd/MM/yyyy"));
                
                cell = createCell(row, 17);
                setBorders(cell);
                cell.setCellValue(StringUtils.isEmpty(item.getNote()) ? "" : item.getNote());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                setMerge(count, count + 4, 17, 17, false);
                
                for(int i = 0; i < 4; i++){
                    row = createRow();
                    cell = createCell(row, 6);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 7);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 8);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 9);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 10);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 11);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 14);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 15);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                    
                    cell = createCell(row, 16);
                    cell.setCellStyle(thRowTbl);
                    setBorders(cell);
                    cell.setCellValue("");
                }
//                count += 5;
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
            if(rangeAddress.isInRange(1, 3) 
                    || rangeAddress.isInRange(2, 3)
                    || rangeAddress.isInRange(3, 3)
                    || rangeAddress.isInRange(4, 3)
                    || rangeAddress.isInRange(5, 3)
                    || rangeAddress.isInRange(6, 3)
                    ){
                continue;
            }
            RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
        }
    }
}

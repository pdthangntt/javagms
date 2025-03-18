package com.gms.entity.excel.htc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc.MerForm;
import com.gms.entity.form.htc.MerTable01Form;
import com.gms.entity.form.htc.MerTable02Form;
import com.gms.entity.form.htc.MerTable03Form;
import com.gms.entity.form.htc.MerTable04Form;
import com.gms.entity.form.htc.MerTable05Form;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
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
 * @author TrangBN
 */
public class MerExcel extends BaseView implements IExportExcel {

    private MerForm form;
    private String extension;
    private HashMap<String, String> serviceOption;

    public MerExcel(MerForm merForm, String extension, 
            HashMap<String, String> serviceOption) {
        this.form = merForm;
        this.extension = extension;
        this.sheetName = "BC_MER";
        this.serviceOption = serviceOption;
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
        
        row = createRow();
        CellStyle createCellStyle = this.workbook.createCellStyle();
        createCellStyle.setFillBackgroundColor(fontSize);
        setMerge(1, 1, 3, 11, false);
        Cell cell = row.createCell(1);
        cell.setCellValue("Tên cơ sở TV&XN: ");
        cell.getCellStyle().setFont(font);
        cell = row.createCell(3);
        cell.setCellValue(form.getSiteName());
        cell.getCellStyle().setFont(font);

        row = createRow();
        setMerge(2, 2, 3, 11, false);
        cell = row.createCell(1);
        cell.setCellValue("Phường/Xã: ");
        cell.getCellStyle().setFont(font);
        cell = row.createCell(3);
        cell.setCellValue(form.getSiteWard());
        cell.getCellStyle().setFont(font);
        
        row = createRow();
        setMerge(3, 3, 3, 11, false);
        cell = row.createCell(1);
        cell.setCellValue("Quận/Huyện: ");
        cell.getCellStyle().setFont(font);
        cell = row.createCell(3);
        cell.setCellValue(form.getSiteDistrict());
        cell.getCellStyle().setFont(font);
        
        row = createRow();
        setMerge(4, 4, 3, 11, false);
        cell = row.createCell(1);
        cell.setCellValue("Tỉnh/Thành phố: ");
        cell.getCellStyle().setFont(font);
        cell = row.createCell(3);
        cell.setCellValue(form.getSiteProvince());
        cell.getCellStyle().setFont(font);
        
        row = createRow();
        setMerge(5, 5, 3, 11, false);
        cell = row.createCell(1);
        cell.setCellValue("Thời gian báo cáo: ");
        cell.getCellStyle().setFont(font);
        cell = row.createCell(3);
        cell.setCellValue("từ " + form.getStartDate() + " đến " + form.getEndDate());
        cell.getCellStyle().setFont(font);
        
        row = createRow();
        setMerge(6, 6, 3, 11, false);
        cell = row.createCell(1);
        cell.setCellValue("Ngày xuất báo cáo: ");
        cell.getCellStyle().setFont(font);
        cell = row.createCell(3);
        cell.setCellValue(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
        cell.getCellStyle().setFont(font);
        
        row = createRow();
        
        row = createRow();
        cell = row.createCell(8);
        cell.setCellValue(StringUtils.upperCase(form.getTitle() + " - CHƯƠNG TRÌNH TƯ VẤN XÉT NGHIỆM HIV"));
        setBold(cell);
        
        row = createRow();
        
    }
    
    /**
     * Tạo bảng trong excel
     */
    private void createTable() {
        
        Row row = createRow();
        Cell cell;
        Sheet sheet = getCurrentSheet();
        int numberService = form.getServices().size() + 1;
        
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
        fontTitle.setColor( HSSFColor.HSSFColorPredefined.RED.getIndex());
        
        // Set cho header của bảng (normal)
        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);

        CellStyle th_rotate = workbook.createCellStyle();
        th_rotate.setWrapText(true);
        th_rotate.setAlignment(HorizontalAlignment.CENTER);
        th_rotate.setVerticalAlignment(VerticalAlignment.CENTER);
        th_rotate.setFont(font);
        th_rotate.setRotation((short) 90);
        
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
        
//        -----------
        //Tiêu đề
        cell = createCell(row, 1);
        cell.setCellValue("Bảng 1: Số bạn tình/bạn chích được giới thiệu và đồng ý xét nghiệm qua kênh \"Theo dấu bạn tình/bạn chích\"");
        cell.setCellStyle(thTitle);
        row = createRow();
        
        row = createRow();
        int lastRowNum1 = getLastRowNum();
        setMerge(lastRowNum1, lastRowNum1 + 2, 1, 1, false);
        cell = createCell(row, 1);
        cell.setCellValue("Tên cơ sở");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 2);
        setMerge(lastRowNum1, lastRowNum1, 2, 29, false);
        cell.setCellValue("Số bạn tình/bạn chích được giới thiệu qua kênh \"Theo dấu bạn tình/bạn chích\" ngày tư vấn từ " + form.getStartDate() + " đến " + form.getEndDate());
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 30);
        setMerge(lastRowNum1, lastRowNum1, 30, 57, false);
        cell.setCellValue("Số bạn tình/bạn chích được giới thiệu và đồng ý xét nghiệm qua kênh \"Theo dấu bạn tình/bạn chích\" ngày xét nghiệm sàng lọc từ " + form.getStartDate() + " đến " + form.getEndDate());
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        row = createRow();
        cell = createCell(row, 2);
        cell.setCellValue("Nam");
        setMerge(lastRowNum1 + 1, lastRowNum1 + 1, 2, 15, false);
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell = createCell(row, 16);
        setMerge(lastRowNum1 + 1, lastRowNum1 + 1, 16, 29, false);
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nữ");
        cell = createCell(row, 30);
        setMerge(lastRowNum1 + 1, lastRowNum1 + 1, 30, 43, false);
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nam");
        cell = createCell(row, 44);
        setMerge(lastRowNum1 + 1, lastRowNum1 + 1, 44, 57, false);
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nữ");
        
        row = createRow();
        cell = createCell(row, 2);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 3);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 4);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 5);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 6);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 7);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 8);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 9);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 10);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 11);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 12);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 13);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 14);
            cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 15);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 16);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 17);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 18);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 19);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 20);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 21);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 22);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 23);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 24);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 25);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 26);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 27);
        cell.setCellValue("44-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 28);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 29);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 30);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 31);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 32);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 33);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 34);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 35);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 36);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 37);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 38);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 39);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 40);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 41);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 42);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 43);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 44);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 45);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 46);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 47);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 48);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 49);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 50);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 51);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 52);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 53);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 54);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 55);
        cell.setCellValue("44-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 56);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 57);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        int lastRow1;
        if (form.getTable01Forms() != null) {
            int index = 0;
            int rowIndex = getLastRowNum();
            for (Map.Entry<String, HashMap<String, MerTable01Form>> entry : form.getTable01Forms().entrySet()) {
                
                // Tạo bảng 01
                row = createRow();
                rowIndex = 1 + rowIndex;
                
                index += 1;
                cell = createCell(row, 1);
                cell.setCellValue(form.getSiteName());
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set undefinedAge
                cell = createCell(row, 2);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getUnDefinedAge() == 0 ? "" : entry.getValue().get("introduced-nam").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 3);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getUnderOneAge() == 0 ? "" : entry.getValue().get("introduced-nam").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set oneToFour
                cell = createCell(row, 4);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getOneToFour() == 0 ? "" : entry.getValue().get("introduced-nam").getOneToFour()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 5);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getFiveToNine() == 0 ? "" : entry.getValue().get("introduced-nam").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 6);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getTenToFourteen() == 0 ? "" : entry.getValue().get("introduced-nam").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 7);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("introduced-nam").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 8);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("introduced-nam").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyFiveTwentyNine
                cell = createCell(row, 9);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("introduced-nam").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 10);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("introduced-nam").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 11);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("introduced-nam").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fortyToFortyFour
                cell = createCell(row, 12);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("introduced-nam").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fortyToFortyFour
                cell = createCell(row, 13);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("introduced-nam").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 14);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getOverFifty() == 0 ? "" : entry.getValue().get("introduced-nam").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 15);
                cell.setCellValue(entry.getValue().get("introduced-nam") == null || entry.getValue().get("introduced-nam").getSum() == 0 ? "" : entry.getValue().get("introduced-nam").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Dương tính nữ
                // Set undefinedAge
                cell = createCell(row, 16);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getUnDefinedAge() == 0 ? "" : entry.getValue().get("introduced-nu").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 17);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getUnderOneAge() == 0 ? "" : entry.getValue().get("introduced-nu").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set oneToFive
                cell = createCell(row, 18);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getOneToFour()== 0 ? "" : entry.getValue().get("introduced-nu").getOneToFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 19);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getFiveToNine()== 0 ? "" : entry.getValue().get("introduced-nu").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 20);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getTenToFourteen() == 0 ? "" : entry.getValue().get("introduced-nu").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 21);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("introduced-nu").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 22);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("introduced-nu").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 23);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("introduced-nu").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 24);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("introduced-nu").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 25);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("introduced-nu").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 26);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("introduced-nu").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 27);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("introduced-nu").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 28);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getOverFifty() == 0 ? "" : entry.getValue().get("introduced-nu").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 29);
                cell.setCellValue(entry.getValue().get("introduced-nu") == null || entry.getValue().get("introduced-nu").getSum() == 0 ? "" : entry.getValue().get("introduced-nu").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Âm tính nam
                // Set undefinedAge
                cell = createCell(row, 30);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getUnDefinedAge() == 0 ? "" : entry.getValue().get("agreed-nam").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 31);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getUnderOneAge() == 0 ? "" : entry.getValue().get("agreed-nam").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set oneToFive
                cell = createCell(row, 32);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getOneToFour() == 0 ? "" : entry.getValue().get("agreed-nam").getOneToFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 33);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getFiveToNine() == 0 ? "" : entry.getValue().get("agreed-nam").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 34);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getTenToFourteen() == 0 ? "" : entry.getValue().get("agreed-nam").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 35);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("agreed-nam").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 36);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("agreed-nam").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 37);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("agreed-nam").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 38);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("agreed-nam").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 39);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("agreed-nam").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 40);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("agreed-nam").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 41);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("agreed-nam").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 42);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getOverFifty() == 0 ? "" : entry.getValue().get("agreed-nam").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 43);
                cell.setCellValue(entry.getValue().get("agreed-nam") == null || entry.getValue().get("agreed-nam").getSum() == 0 ? "" : entry.getValue().get("agreed-nam").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Âm tính nữ
                // Set undefinedAge
                cell = createCell(row, 44);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getUnDefinedAge() == 0 ? "" : entry.getValue().get("agreed-nu").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 45);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getUnderOneAge() == 0 ? "" : entry.getValue().get("agreed-nu").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set OneToFour
                cell = createCell(row, 46);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getOneToFour() == 0 ? "" : entry.getValue().get("agreed-nu").getOneToFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 47);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getFiveToNine() == 0 ? "" : entry.getValue().get("agreed-nu").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 48);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getTenToFourteen() == 0 ? "" : entry.getValue().get("agreed-nu").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 49);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("agreed-nu").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 50);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("agreed-nu").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 51);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("agreed-nu").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 52);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("agreed-nu").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 53);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("agreed-nu").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 54);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("agreed-nu").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 55);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("agreed-nu").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 56);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getOverFifty() == 0 ? "" : entry.getValue().get("agreed-nu").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 57);
                cell.setCellValue(entry.getValue().get("agreed-nu") == null || entry.getValue().get("agreed-nu").getSum() == 0 ? "" : entry.getValue().get("agreed-nu").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
            }
        } else {
            lastRow1 = getLastRowNum()+1;
            row = createRow();
            cell = createCell(row, 1);
            setMerge(lastRow1, lastRow1, 1, 57, false);
            cell.setCellValue("Không có dữ liệu");
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }
//        --------
        //Tiêu đề
        row = createRow();
        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Bảng 2: Kết quả xét nghiệm nhiễm mới theo cơ sở dịch vụ, giới tính và nhóm tuổi");
        cell.setCellStyle(thTitle);
        row = createRow();
        
        row = createRow();
        int lastRowNum2 = getLastRowNum();
        setMerge(lastRowNum2, lastRowNum2 + 2, 1, 1, false);
        cell = createCell(row, 1);
        cell.setCellValue("Tên cơ sở");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 2);
        setMerge(lastRowNum2, lastRowNum2 + 2, 2, 2, false);
        cell.setCellValue("Dịch vụ");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 3);
        setMerge(lastRowNum2, lastRowNum2, 3, 24, false);
        cell.setCellValue("Kết quả xét nghiệm nhiễm mới <6 tháng ngày xét nghiệm nhiễm mới từ " + form.getStartDate() + " đến " + form.getEndDate());
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 25);
        setMerge(lastRowNum2, lastRowNum2, 25, 46, false);
        cell.setCellValue("Kết quả xét nghiệm nhiễm mới >12 tháng ngày xét nghiệm nhiễm mới từ " + form.getStartDate() + " đến " + form.getEndDate());
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        row = createRow();
        cell = createCell(row, 3);
        cell.setCellValue("Nam");
        setMerge(lastRowNum2 + 1, lastRowNum2 + 1, 3, 13, false);
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell = createCell(row, 14);
        setMerge(lastRowNum2 + 1, lastRowNum2 + 1, 14, 24, false);
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nữ");
        cell = createCell(row, 25);
        setMerge(lastRowNum2 + 1, lastRowNum2 + 1, 25, 35, false);
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nam");
        cell = createCell(row, 36);
        setMerge(lastRowNum2 + 1, lastRowNum2 + 1, 36, 46, false);
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nữ");
        
        row = createRow();
        cell = createCell(row, 3);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 4);
        cell.setCellValue("<15");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 5);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 6);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 7);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 8);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 9);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 10);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 11);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 12);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 13);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 14);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 15);
        cell.setCellValue("<15");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 16);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 17);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 18);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 19);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 20);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 21);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 22);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 23);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 24);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 25);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 26);
        cell.setCellValue("<15");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 27);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 28);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 29);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 30);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 31);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 32);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 33);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 34);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 35);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 36);
        
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 37);
        cell.setCellValue("<15");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 38);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 39);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 40);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 41);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 42);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 43);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 44);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 45);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 46);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        int lastRow2;
        if (form.getTable02Forms() != null) {
            int index = 0;
            int rowIndex = getLastRowNum();
            int minMergedRange = getLastRowNum();
            for (Map.Entry<String, HashMap<String, MerTable02Form>> entry : form.getTable02Forms().entrySet()) {
                
                // Tạo bảng 02
                row = createRow();
                rowIndex = 1 + rowIndex;
                
                if ((index == 0 || rowIndex >= minMergedRange + numberService) && index < form.getTable02Forms().entrySet().size() ) {
                    if (numberService != 1) {
                        setMerge(minMergedRange + 1, minMergedRange + numberService, 1, 1, false);
                    }
                    minMergedRange += numberService;
                }
                index += 1;
                
                cell = createCell(row, 1);
                cell.setCellValue(form.getSiteName());
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                cell = createCell(row, 2);
                cell.setCellValue(StringUtils.isEmpty(serviceOption.get(entry.getKey())) ? "Tổng cộng" : serviceOption.get(entry.getKey()));
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Dương tính nam
                // Set undefinedAge
                cell = createCell(row, 3);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getUnDefinedAge() == 0 ? "" : entry.getValue().get("duongtinh-nam").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underFifteen
                cell = createCell(row, 4);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getUnderFifteen()== 0 ? "" : entry.getValue().get("duongtinh-nam").getUnderFifteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 5);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("duongtinh-nam").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 6);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("duongtinh-nam").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyFiveTwentyNine
                cell = createCell(row, 7);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("duongtinh-nam").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 8);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("duongtinh-nam").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 9);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("duongtinh-nam").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fortyToFortyFour
                cell = createCell(row, 10);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("duongtinh-nam").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fortyToFortyFour
                cell = createCell(row, 11);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("duongtinh-nam").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 12);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getOverFifty() == 0 ? "" : entry.getValue().get("duongtinh-nam").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 13);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getSum() == 0 ? "" : entry.getValue().get("duongtinh-nam").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Dương tính nữ
                // Set undefinedAge
                cell = createCell(row, 14);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getUnDefinedAge() == 0 ? "" : entry.getValue().get("duongtinh-nu").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 15);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getUnderFifteen()== 0 ? "" : entry.getValue().get("duongtinh-nu").getUnderFifteen()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 16);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("duongtinh-nu").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 17);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("duongtinh-nu").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 18);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("duongtinh-nu").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 19);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("duongtinh-nu").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 20);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("duongtinh-nu").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 21);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("duongtinh-nu").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 22);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("duongtinh-nu").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 23);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getOverFifty() == 0 ? "" : entry.getValue().get("duongtinh-nu").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 24);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getSum() == 0 ? "" : entry.getValue().get("duongtinh-nu").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Âm tính nam
                // Set undefinedAge
                cell = createCell(row, 25);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getUnDefinedAge() == 0 ? "" : entry.getValue().get("amtinh-nam").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 26);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getUnderFifteen()== 0 ? "" : entry.getValue().get("amtinh-nam").getUnderFifteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 27);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("amtinh-nam").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 28);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("amtinh-nam").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 29);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("amtinh-nam").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 30);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("amtinh-nam").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 31);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("amtinh-nam").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 32);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("amtinh-nam").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 33);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("amtinh-nam").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 34);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getOverFifty() == 0 ? "" : entry.getValue().get("amtinh-nam").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 35);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getSum() == 0 ? "" : entry.getValue().get("amtinh-nam").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Âm tính nữ
                // Set undefinedAge
                cell = createCell(row, 36);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getUnDefinedAge() == 0 ? "" : entry.getValue().get("amtinh-nu").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 37);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getUnderFifteen()== 0 ? "" : entry.getValue().get("amtinh-nu").getUnderFifteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 38);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("amtinh-nu").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 39);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("amtinh-nu").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 40);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("amtinh-nu").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 41);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("amtinh-nu").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 42);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("amtinh-nu").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 43);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("amtinh-nu").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 44);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("amtinh-nu").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 45);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getOverFifty() == 0 ? "" : entry.getValue().get("amtinh-nu").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 46);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getSum() == 0 ? "" : entry.getValue().get("amtinh-nu").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
            }
        } else {
            lastRow2 = getLastRowNum()+1;
            row = createRow();
            cell = createCell(row, 1);
            setMerge(lastRow2, lastRow2, 1, 46, false);
            cell.setCellValue("Không có dữ liệu");
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }
//        ---------
        // Tạo bảng 03
        row = createRow();
        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Bảng 3: Kết quả xét nghiệm nhiễm mới theo cơ sở dịch vụ và nhóm đối tượng");
        cell.setCellStyle(thTitle);
        row = createRow();
        row = createRow();
        int lastRow3 = getLastRowNum();
        
        cell = createCell(row, 1);
        setMerge(lastRow3, lastRow3+1, 1, 1, false);
        cell.setCellValue("Tên cơ sở");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 2);
        setMerge(lastRow3, lastRow3+1, 2, 2, false);
        cell.setCellValue("Dịch vụ");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 3);
        setMerge(lastRow3, lastRow3, 3, 16, false);
        cell.setCellValue("Kết quả xét nghiệm nhiễm mới <6 tháng ngày xét nghiệm nhiễm mới từ " + form.getStartDate() + " đến " + form.getEndDate());
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 17);
        setMerge(lastRow3, lastRow3, 17, 30, false);
        cell.setCellValue("Kết quả xét nghiệm nhiễm mới >12 tháng ngày xét nghiệm nhiễm mới từ " + form.getStartDate() + " đến " + form.getEndDate());
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        row = createRow();
        
        lastRow3 = getLastRowNum();
        cell = createCell(row, 3);
        setMerge(lastRow3, lastRow3, 3, 4, false);
        cell.setCellValue("Nghiện trích ma túy");
        cell.getRow().setHeight(PixelUtils.pixel2WidthUnits(25));
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 5);
        setMerge(lastRow3, lastRow3, 5, 6, false);
        cell.setCellValue("Phụ nữ bán dâm");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 7);
        setMerge(lastRow3, lastRow3, 7, 8, false);
        cell.setCellValue("Nam có quan hệ tình dục với nam");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 9);
        setMerge(lastRow3, lastRow3, 9, 10, false);
        cell.setCellValue("Người chuyển giới");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        // 03i
        cell = createCell(row, 11);
        setMerge(lastRow3, lastRow3, 11, 12, false);
        cell.setCellValue("Phạm nhân");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 13);
        setMerge(lastRow3, lastRow3, 13, 14, false);
        cell.setCellValue("Khác");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        //03i
        cell = createCell(row, 15);
        setMerge(lastRow3, lastRow3, 15, 16, false);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 17);
        setMerge(lastRow3, lastRow3, 17, 18, false);
        cell.setCellValue("Nghiện chích ma túy");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 19);
        setMerge(lastRow3, lastRow3, 19, 20, false);
        cell.setCellValue("Phụ nữ bán dâm");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 21);
        setMerge(lastRow3, lastRow3, 21, 22, false);
        cell.setCellValue("Nam có quan hệ tình dục với nam");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 23);
        setMerge(lastRow3, lastRow3, 23, 24, false);
        cell.setCellValue("Người chuyển giới");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        //03i
        cell = createCell(row, 25);
        setMerge(lastRow3, lastRow3, 25, 26, false);
        cell.setCellValue("Phạm nhân");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 27);
        setMerge(lastRow3, lastRow3, 27, 28, false);
        cell.setCellValue("Khác");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        //03i
        cell = createCell(row, 29);
        setMerge(lastRow3, lastRow3, 29, 30, false);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        //Loop to create row content
        if (form.getTable03Forms()!= null) {
            int index = 0;
            int rowIndex = getLastRowNum();
            int minMergedRange = getLastRowNum();
            
            for (Map.Entry<String, HashMap<String, MerTable03Form>> entry : form.getTable03Forms().entrySet()) {
                
                // Tạo bảng 3
                row = createRow();
                lastRow3  = getLastRowNum();
                rowIndex = 1 + rowIndex;
                
                if ((index == 0 || rowIndex >= minMergedRange + numberService) && index < form.getTable03Forms().entrySet().size() ) {
                    if (numberService != 1) {
                        setMerge(minMergedRange + 1, minMergedRange + numberService, 1, 1, false);
                    }
                    minMergedRange += numberService;
                }
                index += 1;
                
                cell = createCell(row, 1);
                cell.setCellValue(form.getSiteName());
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                cell = createCell(row, 2);
                cell.setCellValue(StringUtils.isEmpty(serviceOption.get(entry.getKey())) ? "Tổng cộng" : serviceOption.get(entry.getKey()));
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                // Dương tính
                // Nghiện chích ma túy
                cell = createCell(row, 3);
                setMerge(lastRow3, lastRow3, 3, 4, false);
                cell.setCellValue(entry.getValue().get("nho6") == null || entry.getValue().get("nho6").getNcmt()== 0 ? "" : entry.getValue().get("nho6").getNcmt()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Phụ nữ bán dâm
                cell = createCell(row, 5);
                setMerge(lastRow3, lastRow3, 5, 6, false);
                cell.setCellValue(entry.getValue().get("nho6") == null || entry.getValue().get("nho6").getPhuNuBanDam()== 0 ? "" : entry.getValue().get("nho6").getPhuNuBanDam()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Nam có quan hệ tình dục với nam
                cell = createCell(row, 7);
                setMerge(lastRow3, lastRow3, 7, 8, false);
                cell.setCellValue(entry.getValue().get("nho6") == null || entry.getValue().get("nho6").getMsm()== 0 ? "" : entry.getValue().get("nho6").getMsm()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Người chuyển giới
                cell = createCell(row, 9);
                setMerge(lastRow3, lastRow3, 9, 10, false);
                cell.setCellValue(entry.getValue().get("nho6") == null || entry.getValue().get("nho6").getChuyenGioi()== 0 ? "" : entry.getValue().get("nho6").getChuyenGioi()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 11);
                setMerge(lastRow3, lastRow3, 11, 12, false);
                cell.setCellValue(entry.getValue().get("nho6") == null || entry.getValue().get("nho6").getPhamNhan()== 0 ? "" : entry.getValue().get("nho6").getPhamNhan()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 13);
                setMerge(lastRow3, lastRow3, 13, 14, false);
                cell.setCellValue(entry.getValue().get("nho6") == null || entry.getValue().get("nho6").getKhac()== 0 ? "" : entry.getValue().get("nho6").getKhac()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                // Tổng
                cell = createCell(row, 15);
                setMerge(lastRow3, lastRow3, 15, 16, false);
                cell.setCellValue(entry.getValue().get("nho6") == null || entry.getValue().get("nho6").getSum()== 0 ? "" : entry.getValue().get("nho6").getSum()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                // Âm tính
                // Nghiện chích ma túy
                cell = createCell(row, 17);
                setMerge(lastRow3, lastRow3, 17, 18, false);
                cell.setCellValue(entry.getValue().get("lon12") == null || entry.getValue().get("lon12").getNcmt()== 0 ? "" : entry.getValue().get("lon12").getNcmt()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Phụ nữ bán dâm
                cell = createCell(row, 19);
                setMerge(lastRow3, lastRow3, 19, 20, false);
                cell.setCellValue(entry.getValue().get("lon12") == null || entry.getValue().get("lon12").getPhuNuBanDam()== 0 ? "" : entry.getValue().get("lon12").getPhuNuBanDam()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Nam có quan hệ tình dục với nam
                cell = createCell(row, 21);
                setMerge(lastRow3, lastRow3, 21, 22, false);
                cell.setCellValue(entry.getValue().get("lon12") == null || entry.getValue().get("lon12").getMsm()== 0 ? "" : entry.getValue().get("lon12").getMsm()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Người chuyển giới
                cell = createCell(row, 23);
                setMerge(lastRow3, lastRow3, 23, 24, false);
                cell.setCellValue(entry.getValue().get("lon12") == null || entry.getValue().get("lon12").getChuyenGioi()== 0 ? "" : entry.getValue().get("lon12").getChuyenGioi()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Tổng
                cell = createCell(row, 25);
                setMerge(lastRow3, lastRow3, 25, 26, false);
                cell.setCellValue(entry.getValue().get("lon12") == null || entry.getValue().get("lon12").getPhamNhan()== 0 ? "" : entry.getValue().get("lon12").getPhamNhan()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 27);
                setMerge(lastRow3, lastRow3, 27, 28, false);
                cell.setCellValue(entry.getValue().get("lon12") == null || entry.getValue().get("lon12").getKhac()== 0 ? "" : entry.getValue().get("lon12").getKhac()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 29);
                setMerge(lastRow3, lastRow3, 29, 30, false);
                cell.setCellValue(entry.getValue().get("lon12") == null || entry.getValue().get("lon12").getSum()== 0 ? "" : entry.getValue().get("lon12").getSum()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
            }
        } else {
            lastRow3 = getLastRowNum()+1;
            row = createRow();
            cell = createCell(row, 1);
            setMerge(lastRow3, lastRow3, 1, 30, false);
            cell.setCellValue("Không có dữ liệu");
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }
//---------
        //Tiêu đề
        row = createRow();
        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Bảng 4: Số khách hàng nhận dịch vụ HTC và quay lại nhận kết quả theo nhóm giới tính và nhóm tuổi");
        cell.setCellStyle(thTitle);
        row = createRow();
        
        row = createRow();
        int lastRowNum4 = getLastRowNum();
        setMerge(lastRowNum4, lastRowNum4 + 2, 1, 1, false);
        cell = createCell(row, 1);
        cell.setCellValue("Tên cơ sở");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 2);
        setMerge(lastRowNum4, lastRowNum4 + 2, 2, 2, false);
        cell.setCellValue("Dịch vụ");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 3);
        setMerge(lastRowNum4, lastRowNum4, 3, 30, false);
        cell.setCellValue("Dương tính");
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 31);
        setMerge(lastRowNum4, lastRowNum4, 31, 58, false);
        cell.setCellValue("Âm tính");
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        row = createRow();
        cell = createCell(row, 3);
        cell.setCellValue("Nam");
        setMerge(lastRowNum4 + 1, lastRowNum4 + 1, 3, 16, false);
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell = createCell(row, 17);
        setMerge(lastRowNum4 + 1, lastRowNum4 + 1, 17, 30, false);
        cell.setCellStyle(bgDuongtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nữ");
        cell = createCell(row, 31);
        setMerge(lastRowNum4 + 1, lastRowNum4 + 1, 31, 44, false);
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nam");
        cell = createCell(row, 45);
        setMerge(lastRowNum4 + 1, lastRowNum4 + 1, 45, 58, false);
        cell.setCellStyle(bgAmtinh);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Nữ");
        
        row = createRow();
        cell = createCell(row, 3);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 4);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 5);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 6);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 7);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 8);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 9);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 10);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 11);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 12);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 13);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 14);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 15);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 16);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 17);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 18);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 19);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 20);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 21);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 22);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 23);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 24);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 25);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 26);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 27);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 28);
        cell.setCellValue("44-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 29);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 30);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 31);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 32);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 33);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 34);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 35);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 36);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 37);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 38);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 39);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 40);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 41);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 42);
        cell.setCellValue("45-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 43);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 44);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 45);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 46);
        cell.setCellValue("<1");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 47);
        cell.setCellValue("1-4");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 48);
        cell.setCellValue("5-9");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 49);
        cell.setCellValue("10-14");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 50);
        cell.setCellValue("15-19");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 51);
        cell.setCellValue("20-24");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 52);
        cell.setCellValue("25-29");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 53);
        cell.setCellValue("30-34");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 54);
        cell.setCellValue("35-39");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 55);
        cell.setCellValue("40-44");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 56);
        cell.setCellValue("44-49");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 57);
        cell.setCellValue("≥50");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 58);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th_rotate);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        int lastRow4;
        if (form.getTable04Forms() != null) {
            int index = 0;
            int rowIndex = getLastRowNum();
            int minMergedRange = getLastRowNum();
            
            for (Map.Entry<String, HashMap<String, MerTable04Form>> entry : form.getTable04Forms().entrySet()) {
                
                // Tạo bảng 04
                row = createRow();
                rowIndex = 1 + rowIndex;
                
                if ((index == 0 || rowIndex >= minMergedRange + numberService) && index < form.getTable04Forms().entrySet().size() ) {
                    if (numberService != 1) {
                        setMerge(minMergedRange + 1, minMergedRange + numberService, 1, 1, false);
                    }
                    minMergedRange += numberService;
                }
                index += 1;
                
                cell = createCell(row, 1);
                cell.setCellValue(form.getSiteName());
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                cell = createCell(row, 2);
                cell.setCellValue(StringUtils.isEmpty(serviceOption.get(entry.getKey())) ? "Tổng cộng" : serviceOption.get(entry.getKey()));
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Dương tính nam
                // Set undefinedAge
                cell = createCell(row, 3);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getUnDefinedAge() == 0 ? "" : entry.getValue().get("duongtinh-nam").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 4);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getUnderOneAge() == 0 ? "" : entry.getValue().get("duongtinh-nam").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set oneToFour
                cell = createCell(row, 5);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getOneToFour() == 0 ? "" : entry.getValue().get("duongtinh-nam").getOneToFour()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 6);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getFiveToNine() == 0 ? "" : entry.getValue().get("duongtinh-nam").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 7);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getTenToFourteen() == 0 ? "" : entry.getValue().get("duongtinh-nam").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 8);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("duongtinh-nam").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 9);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("duongtinh-nam").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyFiveTwentyNine
                cell = createCell(row, 10);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("duongtinh-nam").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 11);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("duongtinh-nam").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 12);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("duongtinh-nam").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fortyToFortyFour
                cell = createCell(row, 13);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("duongtinh-nam").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fortyToFortyFour
                cell = createCell(row, 14);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("duongtinh-nam").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 15);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getOverFifty() == 0 ? "" : entry.getValue().get("duongtinh-nam").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 16);
                cell.setCellValue(entry.getValue().get("duongtinh-nam") == null || entry.getValue().get("duongtinh-nam").getSum() == 0 ? "" : entry.getValue().get("duongtinh-nam").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Dương tính nữ
                // Set undefinedAge
                cell = createCell(row, 17);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getUnDefinedAge() == 0 ? "" : entry.getValue().get("duongtinh-nu").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 18);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getUnderOneAge() == 0 ? "" : entry.getValue().get("duongtinh-nu").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set oneToFive
                cell = createCell(row, 19);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getOneToFour()== 0 ? "" : entry.getValue().get("duongtinh-nu").getOneToFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 20);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getFiveToNine()== 0 ? "" : entry.getValue().get("duongtinh-nu").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 21);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getTenToFourteen() == 0 ? "" : entry.getValue().get("duongtinh-nu").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 22);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("duongtinh-nu").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 23);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("duongtinh-nu").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 24);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("duongtinh-nu").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 25);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("duongtinh-nu").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 26);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("duongtinh-nu").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 27);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("duongtinh-nu").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 28);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("duongtinh-nu").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 29);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getOverFifty() == 0 ? "" : entry.getValue().get("duongtinh-nu").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 30);
                cell.setCellValue(entry.getValue().get("duongtinh-nu") == null || entry.getValue().get("duongtinh-nu").getSum() == 0 ? "" : entry.getValue().get("duongtinh-nu").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Âm tính nam
                // Set undefinedAge
                cell = createCell(row, 31);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getUnDefinedAge() == 0 ? "" : entry.getValue().get("amtinh-nam").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 32);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getUnderOneAge() == 0 ? "" : entry.getValue().get("amtinh-nam").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set oneToFive
                cell = createCell(row, 33);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getOneToFour() == 0 ? "" : entry.getValue().get("amtinh-nam").getOneToFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 34);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getFiveToNine() == 0 ? "" : entry.getValue().get("amtinh-nam").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 35);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getTenToFourteen() == 0 ? "" : entry.getValue().get("amtinh-nam").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 36);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("amtinh-nam").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 37);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("amtinh-nam").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 38);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("amtinh-nam").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 39);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("amtinh-nam").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 40);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("amtinh-nam").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 41);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("amtinh-nam").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 42);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("amtinh-nam").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 43);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getOverFifty() == 0 ? "" : entry.getValue().get("amtinh-nam").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 44);
                cell.setCellValue(entry.getValue().get("amtinh-nam") == null || entry.getValue().get("amtinh-nam").getSum() == 0 ? "" : entry.getValue().get("amtinh-nam").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);

                // Âm tính nữ
                // Set undefinedAge
                cell = createCell(row, 45);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getUnDefinedAge() == 0 ? "" : entry.getValue().get("amtinh-nu").getUnDefinedAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set underOneAge
                cell = createCell(row, 46);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getUnderOneAge() == 0 ? "" : entry.getValue().get("amtinh-nu").getUnderOneAge() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set OneToFour
                cell = createCell(row, 47);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getOneToFour() == 0 ? "" : entry.getValue().get("amtinh-nu").getOneToFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FiveToNine
                cell = createCell(row, 48);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getFiveToNine() == 0 ? "" : entry.getValue().get("amtinh-nu").getFiveToNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set tenToFourteen
                cell = createCell(row, 49);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getTenToFourteen() == 0 ? "" : entry.getValue().get("amtinh-nu").getTenToFourteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set fifteenToNineteen
                cell = createCell(row, 50);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getFifteenToNineteen() == 0 ? "" : entry.getValue().get("amtinh-nu").getFifteenToNineteen() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set twentyToTwentyFour
                cell = createCell(row, 51);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getTwentyToTwentyFour() == 0 ? "" : entry.getValue().get("amtinh-nu").getTwentyToTwentyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set TwentyFiveToTwentyNine
                cell = createCell(row, 52);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getTwentyFiveToTwentyNine() == 0 ? "" : entry.getValue().get("amtinh-nu").getTwentyFiveToTwentyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyToThirtyFour
                cell = createCell(row, 53);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getThirtyToThirtyFour() == 0 ? "" : entry.getValue().get("amtinh-nu").getThirtyToThirtyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set ThirtyFiveToThirtyNine
                cell = createCell(row, 54);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getThirtyFiveToThirtyNine() == 0 ? "" : entry.getValue().get("amtinh-nu").getThirtyFiveToThirtyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyToFortyFour
                cell = createCell(row, 55);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getFortyToFortyFour() == 0 ? "" : entry.getValue().get("amtinh-nu").getFortyToFortyFour() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set FortyFiveToFortyNine
                cell = createCell(row, 56);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getFortyFiveToFortyNine() == 0 ? "" : entry.getValue().get("amtinh-nu").getFortyFiveToFortyNine() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set overFifty
                cell = createCell(row, 57);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getOverFifty() == 0 ? "" : entry.getValue().get("amtinh-nu").getOverFifty() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Set sum
                cell = createCell(row, 58);
                cell.setCellValue(entry.getValue().get("amtinh-nu") == null || entry.getValue().get("amtinh-nu").getSum() == 0 ? "" : entry.getValue().get("amtinh-nu").getSum() + "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
            }
        } else {
            lastRow4 = getLastRowNum()+1;
            row = createRow();
            cell = createCell(row, 1);
            setMerge(lastRow4, lastRow4, 1, 38, false);
            cell.setCellValue("Không có dữ liệu");
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }
        
        // Tạo bảng 05
        row = createRow();
        row = createRow();
        cell = createCell(row, 1);
        cell.setCellValue("Bảng 5: Số khách hàng nhận dịch vụ HTC và quay lại nhận kết quả theo nhóm đối tượng");
        cell.setCellStyle(thTitle);
        row = createRow();
        row = createRow();
        int lastRow5 = getLastRowNum();
        
        cell = createCell(row, 1);
        setMerge(lastRow5, lastRow5+1, 1, 1, false);
        cell.setCellValue("Tên cơ sở");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 2);
        setMerge(lastRow5, lastRow5+1, 2, 2, false);
        cell.setCellValue("Dịch vụ");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 3);
        setMerge(lastRow5, lastRow5, 3, 16, false);
        cell.setCellValue("Dương tính");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 17);
        setMerge(lastRow5, lastRow5, 17, 30, false);
        cell.setCellValue("Âm tính");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        row = createRow();
        
        lastRow5 = getLastRowNum();
        cell = createCell(row, 3);
        setMerge(lastRow5, lastRow5, 3, 4, false);
        cell.setCellValue("Nghiện trích ma túy");
        cell.getRow().setHeight(PixelUtils.pixel2WidthUnits(25));
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 5);
        setMerge(lastRow5, lastRow5, 5, 6, false);
        cell.setCellValue("Phụ nữ bán dâm");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 7);
        setMerge(lastRow5, lastRow5, 7, 8, false);
        cell.setCellValue("Nam có quan hệ tình dục với nam");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 9);
        setMerge(lastRow5, lastRow5, 9, 10, false);
        cell.setCellValue("Người chuyển giới");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 11);
        setMerge(lastRow5, lastRow5, 11, 12, false);
        cell.setCellValue("Phạm nhân");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 13);
        setMerge(lastRow5, lastRow5, 13, 14, false);
        cell.setCellValue("Khác");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 15);
        setMerge(lastRow5, lastRow5, 15, 16, false);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 17);
        setMerge(lastRow5, lastRow5, 17, 18, false);
        cell.setCellValue("Nghiện chích ma túy");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 19);
        setMerge(lastRow5, lastRow5, 19, 20, false);
        cell.setCellValue("Phụ nữ bán dâm");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 21);
        setMerge(lastRow5, lastRow5, 21, 22, false);
        cell.setCellValue("Nam có quan hệ tình dục với nam");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        cell = createCell(row, 23);
        setMerge(lastRow5, lastRow5, 23, 24, false);
        cell.setCellValue("Người chuyển giới");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        
        cell = createCell(row, 25);
        setMerge(lastRow5, lastRow5, 25, 26, false);
        cell.setCellValue("Phạm nhân");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 27);
        setMerge(lastRow5, lastRow5, 27, 28, false);
        cell.setCellValue("Khác");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        cell = createCell(row, 29);
        setMerge(lastRow5, lastRow5, 29, 30, false);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cell);
        
        //Loop to create row content
        if (form.getTable05Forms()!= null) {
            int index = 0;
            int rowIndex = getLastRowNum();
            int minMergedRange = getLastRowNum();
            
            for (Map.Entry<String, HashMap<String, MerTable05Form>> entry : form.getTable05Forms().entrySet()) {

                // Tạo bảng 4
                row = createRow();
                lastRow5  = getLastRowNum();
                rowIndex = 1 + rowIndex;
                if ((index == 0 || rowIndex >= minMergedRange + numberService) && index < form.getTable05Forms().entrySet().size() ) {
                    if (numberService != 1) {
                        setMerge(minMergedRange + 1, minMergedRange + numberService, 1, 1, false);
                    }
                    minMergedRange += numberService;
                }
                index += 1;
                
                cell = createCell(row, 1);
                cell.setCellValue(form.getSiteName());
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                cell = createCell(row, 2);
                cell.setCellValue(StringUtils.isEmpty(serviceOption.get(entry.getKey())) ? "Tổng cộng" : serviceOption.get(entry.getKey()));
//                serviceOption.get(entry.getKey())
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                // Dương tính
                // Nghiện chích ma túy
                cell = createCell(row, 3);
                setMerge(lastRow5, lastRow5, 3, 4, false);
                cell.setCellValue(entry.getValue().get("duongtinh") == null || entry.getValue().get("duongtinh").getNcmt()== 0 ? "" : entry.getValue().get("duongtinh").getNcmt()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Phụ nữ bán dâm
                cell = createCell(row, 5);
                setMerge(lastRow5, lastRow5, 5, 6, false);
                cell.setCellValue(entry.getValue().get("duongtinh") == null || entry.getValue().get("duongtinh").getPhuNuBanDam()== 0 ? "" : entry.getValue().get("duongtinh").getPhuNuBanDam()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Nam có quan hệ tình dục với nam
                cell = createCell(row, 7);
                setMerge(lastRow5, lastRow5, 7, 8, false);
                cell.setCellValue(entry.getValue().get("duongtinh") == null || entry.getValue().get("duongtinh").getMsm()== 0 ? "" : entry.getValue().get("duongtinh").getMsm()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Người chuyển giới
                cell = createCell(row, 9);
                setMerge(lastRow5, lastRow5, 9, 10, false);
                cell.setCellValue(entry.getValue().get("duongtinh") == null || entry.getValue().get("duongtinh").getChuyenGioi()== 0 ? "" : entry.getValue().get("duongtinh").getChuyenGioi()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 11);
                setMerge(lastRow5, lastRow5, 11, 12, false);
                cell.setCellValue(entry.getValue().get("duongtinh") == null || entry.getValue().get("duongtinh").getPhamNhan()== 0 ? "" : entry.getValue().get("duongtinh").getPhamNhan()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 13);
                setMerge(lastRow5, lastRow5, 13, 14, false);
                cell.setCellValue(entry.getValue().get("duongtinh") == null || entry.getValue().get("duongtinh").getKhac()== 0 ? "" : entry.getValue().get("duongtinh").getKhac()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                // Tổng
                cell = createCell(row, 15);
                setMerge(lastRow5, lastRow5, 15, 16, false);
                cell.setCellValue(entry.getValue().get("duongtinh") == null || entry.getValue().get("duongtinh").getSum()== 0 ? "" : entry.getValue().get("duongtinh").getSum()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                // Âm tính
                // Nghiện chích ma túy
                cell = createCell(row, 17);
                setMerge(lastRow5, lastRow5, 17, 18, false);
                cell.setCellValue(entry.getValue().get("amtinh") == null || entry.getValue().get("amtinh").getNcmt()== 0 ? "" : entry.getValue().get("amtinh").getNcmt()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Phụ nữ bán dâm
                cell = createCell(row,19);
                setMerge(lastRow5, lastRow5, 19, 20, false);
                cell.setCellValue(entry.getValue().get("amtinh") == null || entry.getValue().get("amtinh").getPhuNuBanDam()== 0 ? "" : entry.getValue().get("amtinh").getPhuNuBanDam()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Nam có quan hệ tình dục với nam
                cell = createCell(row, 21);
                setMerge(lastRow5, lastRow5, 21, 22, false);
                cell.setCellValue(entry.getValue().get("amtinh") == null || entry.getValue().get("amtinh").getMsm()== 0 ? "" : entry.getValue().get("amtinh").getMsm()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                // Người chuyển giới
                cell = createCell(row, 23);
                setMerge(lastRow5, lastRow5, 23, 24, false);
                cell.setCellValue(entry.getValue().get("amtinh") == null || entry.getValue().get("amtinh").getChuyenGioi()== 0 ? "" : entry.getValue().get("amtinh").getChuyenGioi()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 25);
                setMerge(lastRow5, lastRow5, 25, 26, false);
                cell.setCellValue(entry.getValue().get("amtinh") == null || entry.getValue().get("amtinh").getPhamNhan()== 0 ? "" : entry.getValue().get("amtinh").getPhamNhan()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                cell = createCell(row, 27);
                setMerge(lastRow5, lastRow5, 27, 28, false);
                cell.setCellValue(entry.getValue().get("amtinh") == null || entry.getValue().get("amtinh").getKhac()== 0 ? "" : entry.getValue().get("amtinh").getKhac()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
                
                // Tổng
                cell = createCell(row, 29);
                setMerge(lastRow5, lastRow5, 29, 30, false);
                cell.setCellValue(entry.getValue().get("amtinh") == null || entry.getValue().get("amtinh").getSum()== 0 ? "" : entry.getValue().get("amtinh").getSum()+ "");
                cell.setCellStyle(thRowTbl);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(cell);
            }
        } else {
            lastRow5 = getLastRowNum()+1;
            row = createRow();
            cell = createCell(row, 1);
            setMerge(lastRow5, lastRow5, 1, 30, false);
            cell.setCellValue("Không có dữ liệu");
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
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
    
    /**
     * Get the very last used row
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
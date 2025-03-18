package com.gms.entity.excel.htc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc.VisitBookForm;
import com.gms.entity.form.htc.VisitBookTableForm;
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
 * Xuất file excel sổ tư vấn xét nghiệm
 *
 * @author TrangBN
 */
public class VisitBookExcel extends BaseView implements IExportExcel {

    private VisitBookForm form;
    private String extension;

    public VisitBookExcel(VisitBookForm form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Sổ TVNXN HIV-2674 QĐ-BYT";
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
        currentSheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(115));
        currentSheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(175));
        currentSheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(380));
        currentSheet.setColumnWidth(19, PixelUtils.pixel2WidthUnits(175));
        currentSheet.setColumnWidth(20, PixelUtils.pixel2WidthUnits(100));
        currentSheet.setColumnWidth(22, PixelUtils.pixel2WidthUnits(160));
        currentSheet.setColumnWidth(23, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(24, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(25, PixelUtils.pixel2WidthUnits(150));
        
        //Dòng đầu tiên để trắng
        Row row = createRow();

        row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue("Tên cơ sở tư vấn xét nghiệm: " + form.getSiteName());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        setMerge(2, 2, 0, 22, false);
        cell = row.createCell(0);
        cell.setCellValue(form.getTitle().toUpperCase());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Thời gian từ đến
        Font createFont = workbook.createFont();
        createFont.setFontName("Times New Roman");
        createFont.setFontHeightInPoints((short) 12);
        
        row = createRow();
        cell = row.createCell(7);
        cell.setCellValue("TỪ");
        cell.setCellStyle(noBorder);
        
        cell = row.createCell(8); 
        setMerge(3, 3, 8, 9, true);
        cell.setCellValue(form.getStart());
        cell.getCellStyle().setFont(createFont);
        
        cell = row.createCell(10);
        cell.setCellValue("ĐẾN");
        cell.setCellStyle(noBorder);
        
        cell = row.createCell(11); 
        setMerge(3, 3, 11, 12, true);
        cell.setCellValue(form.getEnd());
        cell.getCellStyle().setFont(createFont);
        
    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable(){
        
        // Đối tượng nguy cơ
        final String NGHIEN_CHICH_MA_TUY    = "1";
        final String PHU_NU_BAN_DAM         = "2";
        final String MSM                    = "3";
        final String VO_CHONG_BAN_CHICH     = "4";
        final String PHU_NU_MANG_THAI       = "5";
        final String BENH_NHAN_LAO          = "6";
        final String DOI_TUONG_KHAC         = "7";
        // Kết quả XN sàng lọc
        final String NON_REACTIVE           = "1";
        final String REACTIVE               = "2";
        final String UNKNOWN_RESULT         = "3";
        // Kết quả XN khẳng định
        final String REACTIVE_CONFIRM       = "2";
        
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
        setMerge(4, 5, 0, 0, false);
        cell.setCellValue("TT");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(1);
        setMerge(4, 5, 1, 1, false);
        cell.setCellValue("Ngày lấy máu xét nghiệm sàng lọc");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(2);
        setMerge(4, 4, 2, 3, false);
        cell.setCellValue("Khách hàng");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(4);
        setMerge(4, 4, 4, 5, false);
        cell.setCellValue("Năm sinh");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(6);
        setMerge(4, 5, 6, 6, false);
        cell.setCellValue("Địa chỉ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(7);
        setMerge(4, 4, 7, 13, false);
        cell.setCellValue("Nhóm nguy cơ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(14);
        setMerge(4, 4, 14, 15, false);
        cell.setCellValue("Xét nghiệm");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(16);
        setMerge(4, 4, 16, 18, false);
        cell.setCellValue("Kết quả xét nghiệm sàng lọc");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(19);
        setMerge(4, 5, 19, 19, false);
        cell.setCellValue("Kết quả xét nghiệm khẳng định");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(20);
        setMerge(4, 4, 20, 22, false);
        cell.setCellValue("Nhận kết quả và tư vấn sau XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(23);
        setMerge(4, 5, 23, 23, false);
        cell.setCellValue("Dịch vụ giới thiệu tiếp sau tư vấn");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(24);
        setMerge(4, 5, 24, 24, false);
        cell.setCellValue("Tên tư vấn viên trước XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(25);
        setMerge(4, 5, 25, 25, false);
        cell.setCellValue("Tên tư vấn viên sau XN");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Dòng nhỏ
        row = createRow();
        row.setHeightInPoints(120);

        cell = row.createCell(2);
        cell.setCellValue("Mã số");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(3);
        cell.setCellValue("Họ và Tên");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(4);
        cell.setCellValue("Nam");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(5);
        cell.setCellValue("Nữ");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(7);
        cell.setCellValue("Nghiện chích ma túy (NCMT)");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(8);
        cell.setCellValue("Phụ nữ bán dâm (PNBD)");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(9);
        cell.setCellValue("Nam quan hệ tình dục với nam (MSM)");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(10);
        cell.setCellValue("Vợ/chồng/bạn tình của người nhiễm HIV");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(11);
        cell.setCellValue("Phụ nữ mang thai");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(12);
        cell.setCellValue("BN Lao");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(13);
        cell.setCellValue("Các đối tượng khác");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(14);
        cell.setCellValue("Có");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(15);
        cell.setCellValue("Không");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(16);
        cell.setCellValue("Âm tính");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(17);
        cell.setCellValue("Dương tính (có phản ứng)");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(18);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(19);
        cell.setCellStyle(th_rotate);

        cell = row.createCell(20);
        cell.setCellValue("Có (ghi ngày tháng)");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(21);
        cell.setCellValue("Không");
        cell.setCellStyle(th_rotate);

        cell = row.createCell(22);
        cell.setCellValue("CMND (Đối với các KH Dương tính)");
        cell.setCellStyle(th_rotate);
        
        setBordersToMergedCells(workbook, currentSheet, Arrays.asList(2));
        
        // Load data records
        int index = 0;
        if (form.getTable() != null) {
            for (VisitBookTableForm visitForm : form.getTable()) {

                row = createRow();
                cell = createCell(row, 0);
                setBorders(cell);
                cell.setCellValue(index += 1);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 1);
                setBorders(cell);
                cell.setCellValue(visitForm.getAdvisoryeTime());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 2);
                setBorders(cell);
                cell.setCellValue(visitForm.getCode());
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 3);
                setBorders(cell);
                cell.setCellValue(visitForm.getPatientName());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 4);
                setBorders(cell);
                if ("1".equals(visitForm.getGender())) {
                    cell.setCellValue(visitForm.getYearOfBirth());
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 5);
                setBorders(cell);
                if ("2".equals(visitForm.getGender())) {
                    cell.setCellValue(visitForm.getYearOfBirth());
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                //Địa chỉ
                cell = createCell(row, 6);
                setBorders(cell);
                cell.setCellValue(visitForm.getPermanentAddress());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setWrapText(true);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                //Nhóm nguy cơ
                cell = createCell(row, 7);
                setBorders(cell);
                if (NGHIEN_CHICH_MA_TUY.equals(visitForm.getObjectGroupID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 8);
                setBorders(cell);
                if (PHU_NU_BAN_DAM.equals(visitForm.getObjectGroupID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 9);
                setBorders(cell);
                if (MSM.equals(visitForm.getObjectGroupID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 10);
                setBorders(cell);
                if (VO_CHONG_BAN_CHICH.equals(visitForm.getObjectGroupID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 11);
                setBorders(cell);
                if (StringUtils.isNotBlank(visitForm.getObjectGroupID()) && PHU_NU_MANG_THAI.equals(visitForm.getObjectGroupID().substring(0, 1))) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 12);
                setBorders(cell);
                if (BENH_NHAN_LAO.equals(visitForm.getObjectGroupID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 13);
                setBorders(cell);
                if (DOI_TUONG_KHAC.equals(visitForm.getObjectGroupID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                // Xét nghiệm
                cell = createCell(row, 14);
                setBorders(cell);
                if (visitForm.getIsAgreePreTest()!=null && visitForm.getIsAgreePreTest()) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 15);
                setBorders(cell);
                if (visitForm.getIsAgreePreTest()!=null && !visitForm.getIsAgreePreTest()) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                // Kết quả xét nghiệm sàng lọc
                cell = createCell(row, 16);
                setBorders(cell);
                if (NON_REACTIVE.equals(visitForm.getTestResultsID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 17);
                setBorders(cell);
                if (REACTIVE.equals(visitForm.getTestResultsID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 18);
                setBorders(cell);
                if (UNKNOWN_RESULT.equals(visitForm.getTestResultsID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                // Kết quả XN khẳng định
                cell = createCell(row, 19);
                setBorders(cell);
                visitForm.setIsAgreeTest(visitForm.getIsAgreeTest() == null ? false : visitForm.getIsAgreeTest());
                cell.setCellValue(visitForm.getIsAgreeTest() == true && (visitForm.getConfirmResultID() != null && !"".equals(visitForm.getConfirmResultID())) ? visitForm.getConfirmResult() : (visitForm.getIsAgreeTest() == true ? "Chờ kết quả" : ""));
                
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                // Nhận kết quả và tư vấn sau XN
                cell = createCell(row, 20);
                setBorders(cell);
                if (StringUtils.isNotEmpty(visitForm.getResultsPatienTime())) {
                    cell.setCellValue(visitForm.getResultsPatienTime());
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 21);
                setBorders(cell);
                if (StringUtils.isEmpty(visitForm.getResultsPatienTime()) && StringUtils.isNotEmpty(visitForm.getConfirmResultID())) {
                    cell.setCellValue("x");
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, 22);
                setBorders(cell);
                if (StringUtils.isNotEmpty(visitForm.getConfirmResultID())
                        && REACTIVE_CONFIRM.equals(visitForm.getConfirmResultID())) {
                    cell.setCellValue(visitForm.getPatientID());
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                // Dịch vụ giới thiệu tiếp sau tư vấn
                cell = createCell(row, 23);
                setBorders(cell);
                cell.setCellValue(visitForm.getExchangeService());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                // Nhân viên TV trước XN 
                cell = createCell(row, 24);
                setBorders(cell);
                cell.setCellValue(visitForm.getStaffBeforeTest());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                // Nhân viên TV sau XN 
                cell = createCell(row, 25);
                setBorders(cell);
                cell.setCellValue(visitForm.getStaffAfter());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            }
        } else {
            row = createRow();
            cell = createCell(row, 0);
            setBorders(cell);
            cell.setCellValue("Không có dữ liệu");
            setMerge(6, 6, 0, 25, true);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }
        
    }
    
    @Override
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
    }

}

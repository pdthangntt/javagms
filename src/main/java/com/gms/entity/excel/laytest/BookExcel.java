package com.gms.entity.excel.laytest;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.laytest.LaytestBookForm;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 *
 * @author pdThang
 */
public class BookExcel extends BaseView implements IExportExcel {

    private LaytestBookForm form;
    private String extension;

    public BookExcel(LaytestBookForm form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Sổ TVNXN HIV-2674 QĐ-BYT";
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
     * Tạo header cho excel
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {
        HashMap<String, String> labels = (new PacPatientInfoEntity()).getAttributeLabels();

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

        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(85));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(85));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(350));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(175));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(140));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(300));

        //Dòng đầu tiên để trắng
        Row row = createRow();

        row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue("Cán bộ xét nghiệm tại cộng đồng: " + form.getSiteName());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        setMerge(2, 2, 0, 16, false);
        cell = row.createCell(0);
        cell.setCellValue(form.getTitle().toUpperCase());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Thời gian từ đến
        row = createRow();
        setMerge(3, 3, 0, 16, false);
        cell = row.createCell(0);
        cell.setCellValue(String.format("Ngày xét nghiệm từ ngày: %s đến ngày %s", form.getStart(), form.getEnd()));
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        row = createRow();
    }

    private void createTable() {
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

        row.setHeightInPoints(23);

        int cellIndex = 0;
        int rowHeader = 5;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("TT");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 1;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("Tên khách hàng");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 1;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("Mã xét nghiệm tại cộng đồng");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 1;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader, cellIndex, cellIndex + 1, true);
        cell.setCellValue("Năm sinh");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 2;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("Số điện thoại");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 1;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("Địa chỉ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 1;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("Đối tượng");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 1;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("Ngày xét nghiệm HIV");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 1;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader, cellIndex, cellIndex + 3, true);
        cell.setCellValue("Xét nghiệm tại cộng đồng");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 4;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader, cellIndex, cellIndex + 2, true);
        cell.setCellValue("Xét nghiệm khẳng định");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cellIndex += 3;
        cell = row.createCell(cellIndex);
        setMerge(rowHeader, rowHeader + 1, cellIndex, cellIndex, true);
        cell.setCellValue("Ghi chú");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Dòng nhỏ
        row = createRow(6);
        row.setHeightInPoints(55);

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
        cell.setCellValue("Sinh phẩm");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(10);
        cell.setCellValue("Vạch chứng (có/không)");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(11);
        cell.setCellValue("Vạch kết quả");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(12);
        cell.setCellValue("Kết luận");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(13);
        cell.setCellValue("Kết quả XNKĐ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(14);
        cell.setCellValue("Ngày XNKĐ");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        cell = row.createCell(15);
        cell.setCellValue("Nơi chuyển gửi");
        setBold(cell);
        cell.setCellStyle(th);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Load data records
        int index = 0;
        if (form.getTable() != null) {
            for (HtcLaytestEntity model : form.getTable()) {
                int i = 0;
                row = createRow();

                cell = createCell(row, i);
                setBorders(cell);
                cell.setCellValue(index += 1);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getPatientName());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getCode());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                if ("1".equals(model.getGenderID())) {
                    cell.setCellValue(model.getYearOfBirth());
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                setBorders(cell);
                if ("2".equals(model.getGenderID())) {
                    cell.setCellValue(model.getYearOfBirth());
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getPatientPhone());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getPermanentAddressFull());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue("".equals(model.getObjectGroupID()) ? "" : form.getOptions().get("test-object-group").get(model.getObjectGroupID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getAdvisoryeTime()== null ? "" : TextUtils.formatDate(model.getAdvisoryeTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(StringUtils.isEmpty(model.getBioName()) ? "" : form.getOptions().get(ParameterEntity.BIOLOGY_PRODUCT_TEST).get(model.getBioName()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(("".equals(model.getControlLine()) || model.getControlLine() == null) ? "" : "0".equals(model.getControlLine()) ? "Không" : "Có" );
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(("".equals(model.getTestLine()) || model.getTestLine() == null) ? "" : "0".equals(model.getTestLine()) ? "Không" : "Có" );
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue("".equals(model.getTestResultsID()) ? "" : form.getOptions().get("test-results").get(model.getTestResultsID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue("".equals(model.getConfirmResultsID()) ? "" : form.getOptions().get("test-result-confirm").get(model.getConfirmResultsID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getConfirmTime()== null ? "" : TextUtils.formatDate(model.getConfirmTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getArrivalSite());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                cell = createCell(row, i += 1);
                setBorders(cell);
                cell.setCellValue(model.getNote());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                cell.getCellStyle().setWrapText(true);
                
                
            }
        } else {
            row = createRow();
            cell = createCell(row, 0);
            setBorders(cell);
            cell.setCellValue("Không có dữ liệu");
            setMerge(7, 7, 0, 16, true);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
        }

    }

}

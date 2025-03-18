package com.gms.entity.excel.report;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.report.TT09Form;
import com.gms.entity.form.tt09.TablePhuluc01Form;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 *
 * @author vvthanh
 */
public class TT09Excel extends BaseView implements IExportExcel {

    private final TT09Form form;
    private final HashMap<String, HashMap<String, String>> options;
    private final HashMap<String, SiteEntity> sites;
    private final String extension;

    public TT09Excel(TT09Form form, HashMap<String, HashMap<String, String>> options, HashMap<String, SiteEntity> sites, String extension) {
        this.form = form;
        this.options = options;
        this.sites = sites;
        this.extension = extension;
        this.sheetName = String.format("TT092012TTBYT tháng %s năm %s", form.getMonth(), form.getYear());
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable();

        getCurrentSheet().getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
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
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(35));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(65));

        setMerge(1, 1, 0, 17, false);
        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
        Integer month = Integer.valueOf(form.getMonth());
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s từ %s đến %s", month < 10 ? "0" + month : month, form.getYear(), form.getStartDate(), form.getEndDate()), cellIndexFilter);
        createFilterRow("Dịch vụ", form.getServiceLabel(), cellIndexFilter);
        createFilterRow("Cơ sở", form.getSiteLabel(), cellIndexFilter);
        createRow();
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {
        getCurrentSheet();
        createRow();

        createTableHeaderRow("TT", "Đối tượng xét nghiệm", "Nhóm tuổi", null, null, null, null, null, null, null, null, null, "Giới", null, null, null, "Tổng");
        createTableHeaderRow(null, null, "<15", null, "15- <25", null, "25-49", null, ">49", null, "Không rõ", null, "Nam", null, "Nữ");
        createTableHeaderRow(null, null, "XN", "(+)", "XN", "(+)", "XN", "(+)", "XN", "(+)", "XN", "(+)", "XN", "(+)", "XN", "(+)", "XN", "(+)");

        int index = getLastRowNumber();
        setMerge(index - 3, index - 1, 0, 0, true); //STT
        setMerge(index - 3, index - 1, 1, 1, true); //Đối tượng XN
        setMerge(index - 3, index - 3, 2, 11, true); //Dòng tuổi
        setMerge(index - 3, index - 3, 12, 15, true); //Dòng giới
        setMerge(index - 3, index - 2, 16, 17, true); //Dòng tổng

        //Gộp dòng tiêu đề thứ 03
        for (int i = 2; i <= 15; i = i + 2) {
            setMerge(index - 2, index - 2, i, i + 1, true); //Dòng tổng row 3
        }

        int indexSum = getLastRowNumber();
        int indexTotal = 0;
        for (TablePhuluc01Form item : form.getTable()) {
            if (item.getPosition() == -1) {
                continue;
            }
            indexTotal++;
            createTableRow(item.getStt(), item.getDoituongxetnghiem(), item.getAge0_15Tong(), item.getAge0_15DuongTinh(), item.getAge15_25Tong(), item.getAge15_25DuongTinh(),
                    item.getAge25_49Tong(), item.getAge25_49DuongTinh(), item.getAge49_maxTong(), item.getAge49_maxDuongTinh(),
                    item.getAge_noneTong(), item.getAge_noneDuongtinh(), item.getGenderMaleTong(), item.getGenderMaleDuongTinh(),
                    item.getGenderFemaleTong(), item.getGenderFemaleDuongTinh(), item.getTong(), item.getTongDuongTinh());
        }

        for (TablePhuluc01Form item : form.getTable()) {
            if (item.getPosition() != -1 || item.getTong() == 0 || !item.getoParentID().equals(Long.valueOf("0"))) {
                continue;
            }
            createTableRow("", item.getDoituongxetnghiem(), item.getAge0_15Tong(), item.getAge0_15DuongTinh(), item.getAge15_25Tong(), item.getAge15_25DuongTinh(),
                    item.getAge25_49Tong(), item.getAge25_49DuongTinh(), item.getAge49_maxTong(), item.getAge49_maxDuongTinh(),
                    item.getAge_noneTong(), item.getAge_noneDuongtinh(), item.getGenderMaleTong(), item.getGenderMaleDuongTinh(),
                    item.getGenderFemaleTong(), item.getGenderFemaleDuongTinh(), item.getTong(), item.getTongDuongTinh());
        }

        //Dòng tổng
        Row row = createRow();
        Cell cell = createCell(row, 0);
        setBorders(cell);

        cell = createCell(row, 1);
        setBold(cell);
        setBorders(cell);
        cell.setCellValue("Tổng cộng:");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 2);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        String fml = "SUM(C" + (indexSum + 1) + ":C" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 3);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(D" + (indexSum + 1) + ":D" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 4);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(E" + (indexSum + 1) + ":E" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 5);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(F" + (indexSum + 1) + ":F" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 6);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(G" + (indexSum + 1) + ":G" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 7);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(H" + (indexSum + 1) + ":H" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 8);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(I" + (indexSum + 1) + ":I" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 9);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(J" + (indexSum + 1) + ":J" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 10);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(K" + (indexSum + 1) + ":K" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 11);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(L" + (indexSum + 1) + ":L" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 12);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(M" + (indexSum + 1) + ":M" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 13);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(N" + (indexSum + 1) + ":N" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 14);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(O" + (indexSum + 1) + ":O" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 15);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(P" + (indexSum + 1) + ":P" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 16);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(Q" + (indexSum + 1) + ":Q" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);

        cell = createCell(row, 17);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(R" + (indexSum + 1) + ":R" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
    }
}

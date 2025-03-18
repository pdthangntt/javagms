package com.gms.entity.excel.report;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.report.TT03QuarterForm;
import com.gms.entity.form.tt03.Table02Form;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class TT03QuarterExcel extends BaseView implements IExportExcel {

    private final TT03QuarterForm form;
    private final HashMap<String, HashMap<String, String>> options;
    private final HashMap<String, SiteEntity> sites;
    private final String extension;

    public TT03QuarterExcel(TT03QuarterForm form, HashMap<String, HashMap<String, String>> options, HashMap<String, SiteEntity> sites, String extension) {
        this.form = form;
        this.options = options;
        this.sites = sites;
        this.extension = extension;
        this.sheetName = String.format("TT032015TTBYT - Quý %s năm %s", Integer.valueOf(form.getQuarter()) + 1, form.getYear());
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable02();

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
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(400));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 6, false);

        createRow();
        createTitleRow(String.format("%s - %s", form.getTitle(), form.isPAC() ? "TUYẾN TỈNH" : "TUYẾN HUYỆN"));
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Kỳ báo cáo", String.format("Quý 0%s/%s từ %s đến %s", Integer.valueOf(form.getQuarter()) + 1, form.getYear(), form.getStartDate(), form.getEndDate()), cellIndexFilter);
        createFilterRow("Dịch vụ", form.getServiceLabel(), cellIndexFilter);
        createFilterRow("Cơ sở", form.getSiteLabel(), cellIndexFilter);
        createRow();
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable02() {
        getCurrentSheet();
        createRow();
        createTitleDateRow("Bảng 2. Tư vấn, xét nghiệm HIV", HorizontalAlignment.LEFT, 1);
        //Header table
        createTableHeaderRow("TT", "Danh mục báo cáo", "Số người được tư vấn trước xét nghiệm", "Số người được xét nghiệm HIV", null, "Số người nhận được kết quả xét nghiệm");
        createTableHeaderRow(null, null, null, "Tổng", "HIV (+)", "Tổng", "HIV (+)");
        int index = getLastRowNumber();
        setMerge(index - 2, index - 1, 0, 0, true);
        setMerge(index - 2, index - 1, 1, 1, true);
        setMerge(index - 2, index - 1, 2, 2, true);
        setMerge(index - 2, index - 2, 3, 4, true);
        setMerge(index - 2, index - 2, 5, 6, true);

        List<Integer> remove = new ArrayList<>();
        int indexTotal = 0;
        for (Table02Form item : form.getTable02()) {
            if (item.getPosition() <= -1) {
                continue;
            }
            if (!item.getoParentID().equals(Long.parseLong("0"))) {
                remove.add(getLastRowNumber() + 1);
            }
            indexTotal++;
            String stt = String.valueOf(item.getStt());
            if (item.getDanhMucBaoCao().contains("Thời kỳ mang thai") || item.getDanhMucBaoCao().contains("Giai đoạn chuyển dạ, đẻ")) {
                stt = "";
            }
            createTableRow(stt, item.getDanhMucBaoCao(), item.getTuVanTruocXetNghiem(), item.getDuocXetNghiemTong(), item.getDuocXetNghiemDuongTinh(), item.getNhanKetQuaTong(), item.getNhanKetQuaDuongtinh());
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
        String fml = "SUM(C" + (index + 1) + ":C" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- C" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 3);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(D" + (index + 1) + ":D" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- D" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 4);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(E" + (index + 1) + ":E" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- E" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 5);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(F" + (index + 1) + ":F" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- F" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 6);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(G" + (index + 1) + ":G" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- G" + integer;
        }
        cell.setCellFormula(fml);
    }
}

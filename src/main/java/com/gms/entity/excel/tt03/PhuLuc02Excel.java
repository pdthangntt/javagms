package com.gms.entity.excel.tt03;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.tt03.TT03Form;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author vvthanh
 */
public class PhuLuc02Excel extends BaseView implements IExportExcel {

    private TT03Form form;
    private String extension;

    public PhuLuc02Excel(TT03Form form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Phụ lục 02-TT032015TTBYT";
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
        if (form.getTable02() != null) {
            Table02Excel table02 = new Table02Excel(workbook, sheetName, form.getTable02());
            table02.build();
        }
        getCurrentSheet().getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        workbook.write(out);
        return out.toByteArray();
    }

    private void createHeader() throws Exception {
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(35));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(65));

        setMerge(1, 1, 0, 6, false);

        //Dòng đầu tiên để trắng
        Row row = createRow();

        row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue(form.getTitle());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Báo cáo từ %s đến %s", form.getStartDate(), form.getEndDate()));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        StringBuilder service = new StringBuilder();
        service.append("Dịch vụ: ");
        for (Map.Entry<String, String> entry : form.getServices().entrySet()) {
            service.append(entry.getValue());
            service.append(",");
        }
        service.deleteCharAt(service.length() - 1);
        cell = row.createCell(0);
        cell.setCellValue(service.toString());
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Cơ sở: %s", form.getSiteName()));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Ngày báo cáo: %s", TextUtils.formatDate(new Date(), "dd/MM/yyyy")));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
    }
}

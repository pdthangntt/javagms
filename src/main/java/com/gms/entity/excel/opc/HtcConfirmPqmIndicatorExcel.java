package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmHtcConfirmReportForm;
import com.gms.entity.form.opc_arv.PqmHtcConfirmReportTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class HtcConfirmPqmIndicatorExcel extends BaseView implements IExportExcel {

    private PqmHtcConfirmReportForm form;
    private String extension;

    public HtcConfirmPqmIndicatorExcel(PqmHtcConfirmReportForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "BC Chi so";
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
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));

        setMerge(1, 1, 0, 7, false);

        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Kỳ báo cáo", String.format("từ %s đến %s", form.getStartDate(), form.getEndDate()), cellIndexFilter);
        String donvi = "";
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            for (String site : form.getSites()) {
                donvi = donvi + form.getSiteOptions().getOrDefault(site, "") + ", ";
            }
            StringBuilder sb = new StringBuilder(donvi);
            sb.deleteCharAt(sb.length() - 2);
            donvi = sb.toString();
            createFilterRow("Cơ sở gửi mẫu", donvi, cellIndexFilter);
        }

        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();

        createTableHeaderRow("STT",
                "Chỉ số",
                "Nhóm tuổi", "", "", "",
                "Giới tính", "",
                "Nhóm đối tượng", "", "", "", "", "", "", "", "", "",
                "Tổng");
        int line = getLastRowNumber();
        setMerge(line - 1, line, 0, 0, false);
        setMerge(line - 1, line, 1, 1, false);
        setMerge(line - 1, line, 18, 18, false);
        setMerge(line - 1, line - 1, 2, 5, false);
        setMerge(line - 1, line - 1, 6, 7, false);
        setMerge(line - 1, line - 1, 8, 17, false);

        createTableHeaderRow("",
                "", "<15", "15-<25", "25-49", ">49", "Nam", "Nữ", "Nghiện chích ma túy (NCMT)", "Người hành nghề mại dâm", "Phụ nữ mang thai",
                "Người bán máu /hiến máu /cho máu", "Bệnh nhân nghi ngờ AIDS", "Bệnh nhân Lao", "Bệnh nhân mắc các nhiễm trùng LTQĐTD",
                "Thanh niên khám tuyển nghĩa vụ quân sự", "Nam quan hệ tình dục với nam (MSM)", "Các đối tượng khác", "");

        int index = 1;
        for (PqmHtcConfirmReportTable item : form.getItems()) {
            createTableRow(index++, item.getIndicators(),
                    item.getUnder15(),
                    item.getM15to25(),
                    item.getM25to49(),
                    item.getOver49(),
                    item.getMale(),
                    item.getFemale(),
                    item.getNtmt(),
                    item.getMd(),
                    item.getPnmt(),
                    item.getNhm(),
                    item.getAids(),
                    item.getLao(),
                    item.getLtqdtd(),
                    item.getNvqs(),
                    item.getMsm(),
                    item.getOther(),
                    item.getSum()
            );
        }

    }
}

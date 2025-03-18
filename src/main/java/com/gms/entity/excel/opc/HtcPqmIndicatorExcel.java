package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.HtcPqmReportForm;
import com.gms.entity.form.opc_arv.HtcReportTable;
import com.gms.entity.form.opc_arv.RegimenTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class HtcPqmIndicatorExcel extends BaseView implements IExportExcel {

    private HtcPqmReportForm form;
    private String extension;

    public HtcPqmIndicatorExcel(HtcPqmReportForm form, String extension) {
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
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s", form.getMonth() < 10 ? "0" + form.getMonth() : form.getMonth(), form.getYear()), cellIndexFilter);
        String donvi = "";
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            for (String site : form.getSites()) {
                donvi = donvi + form.getSiteOptions().getOrDefault(site, "") + ", ";
            }
            StringBuilder sb = new StringBuilder(donvi);
            sb.deleteCharAt(sb.length() - 2);
            donvi = sb.toString();
            createFilterRow("Đơn vị", donvi, cellIndexFilter);
        }

        createRow();
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
        setMerge(line - 1, line, 23, 23, false);
        setMerge(line - 1, line - 1, 2, 10, false);
        setMerge(line - 1, line - 1, 11, 12, false);
        setMerge(line - 1, line - 1, 13, 22, false);

        createTableHeaderRow("",
                "", "10-14",
                "15-19",
                "20-24",
                "25-29",
                "30-34",
                "35-39",
                "40-44",
                "45-49",
                "50+", "Nam", "Nữ", "Nghiện chích ma túy (NCMT)", "Người hành nghề mại dâm", "Phụ nữ mang thai",
                "Người bán máu /hiến máu /cho máu", "Bệnh nhân nghi ngờ AIDS", "Bệnh nhân Lao", "Bệnh nhân mắc các nhiễm trùng LTQĐTD",
                "Thanh niên khám tuyển nghĩa vụ quân sự", "Nam quan hệ tình dục với nam (MSM)", "Các đối tượng khác", "");

        int index = 1;
        for (HtcReportTable item : form.getItems()) {
            createTableRow(index++, item.getIndicators(),
                    item.getI10x14(),
                    item.getI15x19(),
                    item.getI20x24(),
                    item.getI25x29(),
                    item.getI30x34(),
                    item.getI35x39(),
                    item.getI40x44(),
                    item.getI45x49(),
                    item.getI50x(),
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

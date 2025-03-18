package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.InsuranceForm;
import com.gms.entity.form.opc_arv.InsuranceTable;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author vvThành
 */
public class InsuranceExcel extends BaseView implements IExportExcel {

    private InsuranceForm form;
    private HashMap<String, HashMap<String, String>> options;
    private String extension;

    public InsuranceExcel(InsuranceForm form, HashMap<String, HashMap<String, String>> options, String extension) {
        this.form = form;
        this.options = options;
        this.extension = extension;
        this.sheetName = "BC BHYT";
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
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(400));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(19, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 4, false);

        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Kỳ báo cáo", String.format("Từ %s đến %s", form.getStartDate(), form.getEndDate()), cellIndexFilter);
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {
        getCurrentSheet();
        createRow();

        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Chỉ số báo cáo");
        if (!form.isOpcManager()) {
            headers.add("Số lượng");
        } else {
            for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
                headers.add(this.options.get("siteOpcFrom").getOrDefault(String.valueOf(entry.getKey()), "#" + entry.getKey()));
            }
            headers.add("Tổng cộng");
        }
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        //row table
        List<Object> row = new ArrayList<>();
        row.add("I");
        row.add("Số bệnh nhân sử dụng thẻ bảo hiểm y tế");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            row.add("");
        }
        if (form.isOpcManager()) {
            row.add("");
        }
        createTableRowBold(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("1");
        row.add("Số bệnh nhân có thẻ bảo hiểm y tế");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getInsurance());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getInsurance());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("1.1");
        row.add("Hưởng 100% từ quỹ BHYT");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getInsurance100());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getInsurance100());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("1.2");
        row.add("Hưởng 95% từ quỹ BHYT");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getInsurance95());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getInsurance95());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("1.3");
        row.add("Hưởng 80% từ quỹ BHYT");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getInsurance80());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getInsurance80());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("1.4");
        row.add("Khác");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getInsuranceOther());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getInsuranceOther());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("2");
        row.add("Số bệnh nhân chưa có thẻ bảo hiểm y tế");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getInsuranceNone());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getInsuranceNone());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("II");
        row.add("Số thẻ BHYT sắp hết hạn");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            row.add("");
        }
        if (form.isOpcManager()) {
            row.add("");
        }
        createTableRowBold(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("1");
        row.add("Trong vòng 01 tháng tới");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getExpire1());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getExpire1());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("2");
        row.add("Trong vòng 02 tháng tới");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getExpire2());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getExpire2());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("3");
        row.add("Trong vòng 03 tháng tới");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getExpire3());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getExpire3());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("III");
        row.add("Số bệnh nhân có thẻ BHYT phân theo tuyến điều trị");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            row.add("");
        }
        if (form.isOpcManager()) {
            row.add("");
        }
        createTableRowBold(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("1");
        row.add("Đúng tuyến");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getDungtuyen());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getDungtuyen());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("2");
        row.add("Trái tuyến");
        for (Map.Entry<Long, InsuranceTable> entry : form.getTable().entrySet()) {
            InsuranceTable item = entry.getValue();
            row.add(item.getTraituyen());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable().getTraituyen());
        }
        createTableRow(row.toArray(new Object[row.size()]));
    }
}

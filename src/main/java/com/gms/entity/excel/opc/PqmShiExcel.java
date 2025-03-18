package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmShiForm;
import com.gms.entity.form.opc_arv.PqmShiTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class PqmShiExcel extends BaseView implements IExportExcel {

    private PqmShiForm form;
    private String extension;

    public PqmShiExcel(PqmShiForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "Dulieu";
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
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(200));

        setMerge(1, 1, 0, 4, false);

        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s", form.getMonth(), form.getYear()), cellIndexFilter);

        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();

        createTableHeaderRow(
                "STT", "Cơ sở KCB", "Số ca điều trị bằng thẻ BHYT", "Số ca được cấp thuốc từ 3 tháng trở lên qua BHYT");
        int i = 1;

        if (form.getItems() != null && !form.getItems().isEmpty()) {
            for (PqmShiTable item : form.getItems()) {
                createTableRow(i++, item.getSite(),
                        item.getArv(),
                        item.getDrug()
                );
            }
            int index = getLastRowNumber();
            createTableRow("Tổng cộng", "",
                    form.getTotalArv(),
                    form.getTotalDrug()
            );
            setMerge(index, index, 0, 1, true);
        } else {
            int index = getLastRowNumber();
            createTableHeaderRow(
                    "Không có thông tin");
            setMerge(index, index, 0, 3, true);

        }

    }
}

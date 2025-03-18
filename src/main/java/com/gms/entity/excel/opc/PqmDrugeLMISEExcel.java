package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmeLMISEForm;
import com.gms.entity.form.opc_arv.PqmEstimateTable;
import com.gms.entity.form.opc_arv.PqmeLMISETable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class PqmDrugeLMISEExcel extends BaseView implements IExportExcel {

    private PqmeLMISEForm form;
    private String extension;

    public PqmDrugeLMISEExcel(PqmeLMISEForm form, String extension) {
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
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 5, false);

        createRow();
        createTitleRow("Kết quả chỉ số thuốc ARV (eLMIS)");
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Kỳ báo cáo", String.format("Quý %s năm %s", form.getMonth(), form.getYear()), cellIndexFilter);
        if (StringUtils.isNotBlank(form.getTab()) && form.getTab().equals("detail") && StringUtils.isNotEmpty(form.getSiteSearch())) {
            createFilterRow("Cơ sở", form.getSiteSearch(), cellIndexFilter);
        }

        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();

        createTableHeaderRow(
                "STT",
                "Tên thuốc",
                "Số lượng thuốc tồn cuối kỳ",
                "Số lượng thuốc để đạt dự trù an toàn 2 tháng",
                "Số lượng thuốc sử dụng trong kỳ",
                "Số lượng thuốc theo kế hoạch phân bố trong kỳ");
        int i = 1;

        if (form.getItems() != null && !form.getItems().isEmpty()) {
            for (PqmeLMISETable item : form.getItems()) {
                createTableRow(i++, item.getDrugName(),
                        item.getTon(),
                        item.getThang(),
                        item.getSuDung(),
                        item.getTrongKy()
                );
            }
        } else {
            int index = getLastRowNumber();
            createTableHeaderRow(
                    "Không có thông tin");
            setMerge(index, index, 0, 5, true);

        }

    }
}

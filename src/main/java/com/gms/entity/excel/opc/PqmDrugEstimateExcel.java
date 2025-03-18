package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmEstimateForm;
import com.gms.entity.form.opc_arv.PqmEstimateTable;
import com.gms.entity.form.opc_arv.RegimenTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author DSNAnh
 */
public class PqmDrugEstimateExcel extends BaseView implements IExportExcel {

    private PqmEstimateForm form;
    private String extension;

    public PqmDrugEstimateExcel(PqmEstimateForm form, String extension) {
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
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(250));

        setMerge(1, 1, 0, 5, false);

        createRow();
        createTitleRow("Chỉ số thuốc đã cấp trên số thuốc kế hoạch");
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
                "STT", "Tên thuốc", "Đơn vị tính", "Số lượng thuốc sử dụng trong kỳ", "Số lượng thuốc theo kế hoạch phân bố trong kỳ");
        int i = 1;

        if (form.getItems() != null && !form.getItems().isEmpty()) {
            for (PqmEstimateTable item : form.getItems()) {
                createTableRow(i++, item.getDrugNameLowercase(),
                        item.getDrugUomLowercase(),
                        item.getPlannedQuantity(),
                        item.getDispensedQuantity()
                );
            }
        } else {
            int index = getLastRowNumber();
            createTableHeaderRow(
                    "Không có thông tin");
            setMerge(index, index, 0, 4, true);
            
        }

    }
}

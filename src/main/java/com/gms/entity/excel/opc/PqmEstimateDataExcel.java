package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PqmDrugEstimateDataEntity;
import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmEstimateForm;
import com.gms.entity.form.opc_arv.PqmShiTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class PqmEstimateDataExcel extends BaseView implements IExportExcel {

    private PqmEstimateForm form;
    private String extension;

    public PqmEstimateDataExcel(PqmEstimateForm form, String extension) {
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
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
    }

    private String removeNull(String text) {
        if (StringUtils.isEmpty(text) || text.equals("null")) {
            return "";
        }
        return text;
    }

    private void createTable() throws Exception {
        getCurrentSheet();

        
        
        createTableHeaderRow(
                "STT",
                "provinceCode",
                "dataCode",
                "facilityName",
                "facilityCode",
                "version",
                "quarter",
                "year",
                "drugName",
                "drugUom",
                "drugNameLowercase",
                "drugUomLowercase",
                "plannedQuantity",
                "dispensedQuantity"
                );
        int i = 1;
        if (form.getItemAPI() != null && !form.getItemAPI().isEmpty()) {
            for (PqmDrugEstimateDataEntity item : form.getItemAPI()) {
                createTableRow(i++,
                        item.getProvinceCode(),
                        item.getDataCode(),
                        item.getFacilityName(),
                        item.getFacilityCode(),
                        item.getVersion(),
                        item.getQuarter(),
                        item.getYear(),
                        item.getDrugName(),
                        item.getDrugUom(),
                        item.getDrugNameLowercase(),
                        item.getDrugUomLowercase(),
                        item.getPlannedQuantity(),
                        item.getDispensedQuantity()
                        
                );
            }
        } else {
            int index = getLastRowNumber();
            createTableHeaderRow(
                    "Không có thông tin");
            setMerge(index, index, 0, 3, true);

        }

    }
}

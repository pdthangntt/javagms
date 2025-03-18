package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmForm;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class PqmDataExcel extends BaseView implements IExportExcel {

    private PqmForm form;
    private String extension;

    public PqmDataExcel(PqmForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;

    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (!form.getMapDataHub().isEmpty()) {
            for (Map.Entry<String, List<PqmDataEntity>> entry : form.getMapDataHub().entrySet()) {
                String key = entry.getKey();
                List<PqmDataEntity> value = entry.getValue();
                this.sheetName = key;
                createSheet(key);
                createHeader();
                createTable(value, key);

            }
        } else {
            this.sheetName = "DulieudataPQM";
            createSheet("DulieudataPQM");
            createHeader();
            createTableEmty();
        }

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
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));

    }

    private Map<String, String> mapObject() {
        Map<String, String> map = new HashMap<>();
        map.put("4", "PP");
        map.put("8", "PP");
        map.put("14", "PP");
        map.put("16", "PP");
        map.put("18", "PP");
        map.put("33", "PP");
        map.put("13", "TG");
        map.put("2", "FSW");
        map.put("1", "PWID");
        map.put("3", "MSM");
        map.put("7", "Others");
        return map;
    }

    private void createTable(List<PqmDataEntity> items, String siteName) throws Exception {
        getCurrentSheet();

        createTableHeaderRow(
                "Year",
                "Month",
                "Indicator Code",
                "Sex",
                "Age Group",
                "Key Population",
                "OPC Code",
                "Quarter",
                "Quantity",
                "Type",
                "District",
                "Province",
                "District Code",
                "Province Code");

        int index = 1;
        if (items != null && !items.isEmpty()) {
            for (PqmDataEntity item : items) {
                createTableRow(
                        item.getYear(),
                        item.getMonth(),
                        item.getIndicatorCode(),
                        item.getSexID().equals("nam") ? "Male" : item.getSexID().equals("nu") ? "Female" : "N/A",
                        item.getAgeGroup() == null ? "N/A" : item.getAgeGroup().equals("none") ? "N/A" : item.getAgeGroup(),
                        StringUtils.isEmpty(item.getObjectGroupID()) ? "N/A" :mapObject().getOrDefault(item.getObjectGroupID(), "Others"),
                        siteName,
                        item.getMonth() < 4 ? "1" : item.getMonth() > 3 && item.getMonth() < 7 ? "2" : item.getMonth() > 6 && item.getMonth() < 10 ? "3" : item.getMonth() > 9 ? "4" : "",
                        item.getQuantity(),
                        "Month",
                        form.getDistrictNamePQM().getOrDefault(item.getDistrictID(), "") == null ? "" : form.getDistrictNamePQM().getOrDefault(item.getDistrictID(), ""),
                        form.getProvinceNamePQM().getOrDefault(item.getProvinceID(), "") == null ? "" : form.getProvinceNamePQM().getOrDefault(item.getProvinceID(), ""),
                        form.getDistrictPQM().getOrDefault(item.getDistrictID(), "") == null ? "" : form.getDistrictPQM().getOrDefault(item.getDistrictID(), ""),
                        form.getProvincePQM().getOrDefault(item.getProvinceID(), "") == null ? "" : form.getProvincePQM().getOrDefault(item.getProvinceID(), "")
                        
                );
            }
        }

    }
    
    private void createTableEmty() throws Exception {
        getCurrentSheet();
        setMerge(1, 1, 0, 8, false);
        createRow();
        createTitleRow("Không có dữ liệu data hub");
        

    }
}

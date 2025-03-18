package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author othoa
 */
public class PqmPlanDataExcel extends BaseView implements IExportExcel {

    private List<PqmDrugPlanDataEntity> items;
    private String extension;
    private Integer year;
    private Integer month;
    private String site;
    private String tab;
    private String siteNameSearch;
    private String province_name;

    public PqmPlanDataExcel(List<PqmDrugPlanDataEntity> items, Integer year, Integer month, String site, String extension, String tab, String siteNameSearch, String province_name) {
        this.useStyle = true;
        this.items = items;
        this.year = year;
        this.month = month;
        this.site = site;
        this.tab = tab;
        this.siteNameSearch = siteNameSearch;
        this.extension = extension;
        this.sheetName = "Data";
        this.province_name = province_name;
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

        String text = "ThuocArvDuTruAnToan_" + "Thang" + this.month + "_Nam" + this.year + "_" + TextUtils.removeDiacritical(province_name);

        return String.format("%s.%s", text, extension);
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
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();
        createTableHeaderRow(
                "STT",
                "month",
                "year",
                "provinceID",
                "siteCode",
                "siteName",
                "drugName",
                "unit",
                "totalDrugBacklog",
                "totalDrugPlan2Month"
        );
        int i = 1;
        if (items != null && !items.isEmpty()) {
            for (PqmDrugPlanDataEntity item : items) {
                createTableRow(
                        i++,
                        item.getMonth(),
                        item.getYear(),
                        item.getProvinceID(),
                        item.getSiteCode(),
                        item.getSiteName(),
                        item.getDrugName(),
                        item.getUnit(),
                        item.getTotalDrugBacklog(),
                        item.getTotalDrugPlan2Month()
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

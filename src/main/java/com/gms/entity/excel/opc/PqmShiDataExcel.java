package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmShiForm;
import com.gms.entity.form.opc_arv.PqmShiTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class PqmShiDataExcel extends BaseView implements IExportExcel {

    private PqmShiForm form;
    private String extension;

    public PqmShiDataExcel(PqmShiForm form, String extension) {
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
                "siteID",
                "provinceID",
                "province_code",
                "data_code",
                "version",
                "month",
                "year",
                "sex",
                "age_group",
                "shi_art",
                "shi_mmd",
                "facility_name",
                "facility_code");
        int i = 1;

        if (form.getApiDatas() != null && !form.getApiDatas().isEmpty()) {
            for (PqmShiDataEntity item : form.getApiDatas()) {
                createTableRow(i++,
                        item.getSiteID(),
                        removeNull(item.getProvinceID()),
                        removeNull(item.getProvince_code()),
                        removeNull(item.getData_code()),
                        removeNull(item.getVersion()),
                        item.getMonth(),
                        item.getYear(),
                        removeNull(item.getSex()),
                        removeNull(item.getAge_group()),
                        item.getShi_art(),
                        item.getShi_mmd(),
                        removeNull(item.getFacility_name()),
                        removeNull(item.getFacility_code())
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

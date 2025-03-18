package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PqmDrugeLMISEDataEntity;
import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmeLMISEForm;
import com.gms.entity.form.opc_arv.PqmShiTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class PqmElmisDataExcel extends BaseView implements IExportExcel {

    private PqmeLMISEForm form;
    private String extension;

    public PqmElmisDataExcel(PqmeLMISEForm form, String extension) {
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
                "year",
                "quarter",
                "provinceID",
                "drugName",
                "ton",
                "thang",
                "suDung",
                "trongKy");
        int i = 1;
        if (form.getItemAPI() != null && !form.getItemAPI().isEmpty()) {
            for (PqmDrugeLMISEDataEntity item : form.getItemAPI()) {
                createTableRow(i++,
                        item.getSiteID(),
                        item.getYear(),
                        item.getQuarter(),
                        removeNull(item.getProvinceID()),
                        removeNull(item.getDrugName()),
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
            setMerge(index, index, 0, 3, true);

        }

    }
}

package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmExportForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdthang
 */
public class PqmExportExcel extends BaseView implements IExportExcel {

    private PqmExportForm form;
    private String extension;
    private String tab;

    public PqmExportExcel(PqmExportForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = form.getSheetName();
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
        return String.format("%s%s", form.getFileName(), extension);
    }

    private void createHeader() throws Exception {
        int O = 0;
        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));

    }

    private void createTable() throws Exception {

        List<String> headers = new ArrayList<>();
        for (String header : form.getHead()) {
            headers.add(header);
        }
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        List<Object> row;
        for (LinkedList<String> data : form.getDatas()) {
            row = new ArrayList<>();
            for (String string : data) {
                row.add(string);
            }
            createTableRow(row.toArray(new Object[row.size()]));
        }

    }
}

package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmExportAllForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdthang
 */
public class PqmExportPlanExcel extends BaseView implements IExportExcel {

    private PqmExportAllForm form;
    private String extension;
    private String tab;

    public PqmExportPlanExcel(PqmExportAllForm form, String extension) {
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
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(120));
        
        
        //Dòng đầu tiên để trắng
        Row row = createRow();
        int rownum = row.getRowNum();
        int cellIndexFilter = 1;

        row = createTitleRow(form.getTitle().toUpperCase());
        rownum = row.getRowNum();
        setMerge(rownum, rownum, 0, 12, false);

        createRow();
        
        for (Map.Entry<String, String> en : form.getFilter().entrySet()) {
            String key = en.getKey();
            String value = en.getValue();
            
            createFilterRow(key, value, cellIndexFilter);
        }
        
//
//        //Thông tin tìm kiếm
//        createFilterRow("Địa chỉ", "".equals(form.getTitleLocationDisplay()) ? form.getProvinceName() : form.getTitleLocationDisplay(), cellIndexFilter);
//
//        //filter info default
//        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
//        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);

    }

    private void createTable() throws Exception {
        
        createRow();
        int index = getLastRowNumber();

        List<String> headers = new ArrayList<>();
        for (String header : form.getHead()) {
            headers.add(header);
        }
        createTableHeaderRow(headers.toArray(new String[headers.size()]));
        headers = new ArrayList<>();
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        
        headers.add("Tồn đầu kỳ");
        headers.add("Nhập trong kỳ");
        headers.add("Xuất cho bệnh nhân trong kỳ");
        headers.add("Xuất điều chuyển trong kỳ");
        headers.add("Hư hao");
        headers.add("Tồn cuối kỳ");
        
        createTableHeaderRow(headers.toArray(new String[headers.size()]));
        
        setMerge(index, index + 1, 0 , 0 , true);  
        setMerge(index, index + 1, 1 , 1 , true);  
        setMerge(index, index + 1, 2 , 2 , true);  
        setMerge(index, index + 1, 3 , 3 , true);  
        setMerge(index, index + 1, 4 , 4 , true);  
        setMerge(index, index + 1, 5 , 5 , true);  
        setMerge(index, index + 1, 6 , 6 , true);  
        setMerge(index, index + 1, 7 , 7 , true);  
        setMerge(index, index + 1, 8 , 8 , true);  
        setMerge(index, index , 9 , 14 , true);   
        

        List<Object> row;
        for (LinkedList<String> data : form.getDatas()) {
            row = new ArrayList<>();
            for (String string : data) {
                row.add(StringUtils.isBlank(string) ? "" : string);
            }
            createTableRow(row.toArray(new Object[row.size()]));
        }

    }
}

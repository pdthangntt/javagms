package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.OpcQuickTreatmentForm;
import com.gms.entity.form.opc_arv.QuickTreatmentTable;
import com.gms.entity.form.opc_arv.TT03Form;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author TrangBN
 */
public class QuickTreatmentExcel extends BaseView implements IExportExcel {

    private OpcQuickTreatmentForm form;
    private HashMap<String, HashMap<String, String>> options;
    private String extension;

    public QuickTreatmentExcel(OpcQuickTreatmentForm form, HashMap<String, HashMap<String, String>> options, String extension) {
        this.form = form;
        this.options = options;
        this.extension = extension;
        this.sheetName = "ARV_90";
    }

    public QuickTreatmentExcel(TT03Form data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createBody();
        
        getCurrentSheet().getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        workbook.write(out);
        return out.toByteArray();
    }

    @Override
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
    }

    /**
     * Tạo header cho excel
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(35));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(400));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(150));

        createNormalRow(form.getSiteAgency().toUpperCase(), false,0);
        createNormalRow(form.getSiteName().toUpperCase(), true,0);
        createNormalRow("             ----------------------------------", false,0);

        createRow();
        createTitleRow("BÁO CÁO ĐIỀU TRỊ  ARV VÀ CẤP THUỐC ARV TỐI ĐA 90 NGÀY");
        createTitleDateRow(String.format("Từ %s đến %s", form.getStartDate(), form.getEndDate()));
        setMerge(4, 4, 0, 6, false);
        setMerge(5, 5, 0, 6, false);
    }

    private void createBody() {
        getCurrentSheet();
        createRow();

        int line = getLastRowNumber();        
        //Header table
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Cơ sở điều trị HIV/AIDS");
        headers.add("Số người nhiễm HIV đang được ĐT ARV");
        headers.add("Điều trị ARV nhanh");
        headers.add("");
        headers.add("Số người bệnh đang được cấp thuốc ARV tối đa 90 ngày trong kỳ báo cáo");
        headers.add("Số người được cấp ARV 3 tháng qua BHYT");
        headers.add("Ghi chú");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));
        setMerge(7, 7, 3, 4, false);
        
        headers = new ArrayList<>();
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Số người được ĐT ARV nhanh trong kỳ báo cáo");
        headers.add("Số người được ĐT ARV trong ngày trong kỳ báo cáo");
        headers.add("");
        headers.add("");
        headers.add("");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));
        setMerge(7, 8, 0, 0, false);
        setMerge(7, 8, 1, 1, false);
        setMerge(7, 8, 2, 2, false);
        setMerge(7, 8, 5, 5, false);
        setMerge(7, 8, 6, 6, false);
        setMerge(7, 8, 7, 7, false);

        // Lặp dòng dữ liệu
        int order = 0;
        for (QuickTreatmentTable quick : form.getTable()) {
            order += 1;
            List<Object> row = new ArrayList<>();
            row.add(order);
            row.add(form.getOptions().get(String.valueOf(quick.getSiteID())));
            row.add(quick.getArvTreatment());
            row.add(quick.getArvQuickTreatment());
            row.add(quick.getArvTreatmentDuringDay());
            row.add(quick.getReceiveMedicineMax90());
            row.add(quick.getReceiveMedicineThreeMonth());
            row.add(quick.getNote());
            createTableRow(row.toArray(new Object[row.size()]));
        }
        
        if (form.isOpcManager()) {
            List<Object> row = new ArrayList<>();
            row.add("Tổng số");
            row.add("");
            row.add(form.getSum().getArvTreatment());
            row.add(form.getSum().getArvQuickTreatment());
            row.add(form.getSum().getArvTreatmentDuringDay());
            row.add(form.getSum().getReceiveMedicineMax90());
            row.add(form.getSum().getReceiveMedicineThreeMonth());
            row.add("");
            createTableRow(row.toArray(new Object[row.size()]));
            line = getLastRowNumber(); 
            setMerge(line-1, line-1, 0, 1, false);
        }
    }
}

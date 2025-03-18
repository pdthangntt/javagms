package com.gms.entity.excel.htc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc.PositiveForm;
import com.gms.entity.form.htc.PositiveTableForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * Format and create excel file
 *
 * @author pdThang
 */
public class PositiveExcel extends BaseView implements IExportExcel {

    private PositiveForm form;
    private String extension;

    public PositiveExcel(PositiveForm form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Duong tinh HIV-TT092012TTBYT";
    }

    @Override
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable();
        if (form.getOptions() != null) {
            //Định nghĩa options sheet
            createOptions(form.getOptions());
        }

        workbook.write(out);
        return out.toByteArray();
    }

    /**
     * Tạo header cho excel
     * @throws Exception 
     */
    private void createHeader() throws Exception {

        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(139));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(239));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(239));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(220));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(102));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(257));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(257));
        
        setMerge(1, 1, 0, 12, false);
        setMerge(2, 2, 0, 12, false);
        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        createTitleDateRow(String.format("Thời gian từ %s đến %s", form.getStart(), form.getEnd()));
        //Tạo thêm dòng trắng
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        if (form.getServices() != null && form.getServices().size() > 0) {
            StringBuilder service = new StringBuilder();
            for (Map.Entry<String, String> entry : form.getServices().entrySet()) {
                service.append(entry.getValue());
                service.append(",");
            }
            service.deleteCharAt(service.length() - 1);
            createFilterRow("Dịch vụ", service.toString(), cellIndexFilter);
        }
        if (form.getObjects() != null && form.getObjects().size() > 0 && form.getObjects().size() < 33) {
            StringBuilder object = new StringBuilder();
            for (Map.Entry<String, String> entry : form.getObjects().entrySet()) {
                object.append(entry.getValue());
                object.append(",");
            }
            object.deleteCharAt(object.length() - 1);
            createFilterRow("Đối tượng", object.toString(), cellIndexFilter);
        }
        createFilterRow("Cơ sở", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        
    }
    
    private void createTable() throws Exception {
         createRow();
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Họ tên");
        headers.add("Dân tộc");
        headers.add("Giới tính");
        headers.add("Năm sinh");
        headers.add("CMTND");
        headers.add("Nơi cư trú hiện tại");
        headers.add("Nơi đăng ký hộ khẩu thường trú");
        headers.add("Nghề nghiệp");
        headers.add("Đối tượng");
        headers.add("Nguy cơ");
        headers.add("Đường lây");
        headers.add("Ngày XN khẳng định");
        headers.add("Phòng XN khẳng định");
        headers.add("Nơi lấy mẫu máu XN");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        if (form.getTable() != null && form.getTable().size() > 0) {
            List<Object> row;
            int index = 0;
            for (PositiveTableForm item : form.getTable()) {
                row = new ArrayList<>();
                row.add(index += 1);
                
                row.add(StringUtils.isEmpty(item.getPatientName()) ? "" : item.getPatientName());
                row.add(StringUtils.isEmpty(item.getRace()) ? "" : item.getRace());
                row.add(StringUtils.isEmpty(item.getGender()) ? "" : item.getGender());
                row.add(StringUtils.isEmpty(item.getYearOfBirth()) ? "" : item.getYearOfBirth());
                row.add(StringUtils.isEmpty(item.getPatientID()) ? "" : item.getPatientID());
                row.add(StringUtils.isEmpty(item.getCurrentAddress()) ? "" : item.getCurrentAddress());
                row.add(StringUtils.isEmpty(item.getPermanentAddress()) ? "" : item.getPermanentAddress());
                row.add(StringUtils.isEmpty(item.getJob()) ? "" : item.getJob());
                row.add(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : item.getObjectGroupID());
                row.add(StringUtils.isEmpty(item.getRiskBehaviorID()) ? "" : item.getRiskBehaviorID());
                row.add(StringUtils.isEmpty(item.getModeOfTransmission()) ? "" : item.getModeOfTransmission());
                
                row.add(StringUtils.isEmpty(item.getConfirmTime()) ? "" : item.getConfirmTime());
                row.add(StringUtils.isEmpty(item.getSiteConfirmTest()) ? "" : item.getSiteConfirmTest());
                row.add(StringUtils.isEmpty(item.getBloodPlace()) ? "" : item.getBloodPlace());
                
                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }


}

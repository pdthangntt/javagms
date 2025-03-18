package com.gms.entity.excel.htc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc.TransferSuccessForm;
import com.gms.entity.form.htc.TransferSuccessTableForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Format and create excel file
 *
 * @author NamAnh_HaUI
 */
public class TransferSuccessExcel extends BaseView implements IExportExcel {

    private TransferSuccessForm form;
    private String extension;

    public TransferSuccessExcel(TransferSuccessForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "BC DS CGĐT thanh cong";
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
        workbook.write(out);
        return out.toByteArray();
    }

    private void createHeader() throws Exception {

        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(239));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(155));

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

    private void createTable() {
        String format = "dd/MM/yyyy";
        getCurrentSheet();
        //Dòng đầu tiên để trắng
        Row row = createRow();
        int index = getLastRowNumber();
        //Tiêu đề
        createTableHeaderRow("TT", "Khách hàng","", "Năm sinh", "",
                "Địa chỉ cư trú", "Địa chỉ thường trú", "Số điện thoại liên hệ", "Ngày chuyển gửi", 
                "Tên cơ sở chuyển đến", "Ngày đăng ký điều trị", "Tên cơ sở điều trị", "Mã số điều trị");
        createTableHeaderRow("", "Mã số","Họ tên", "Nam", "Nữ","", "", "", "", "", "", "", "");
        setMerge(index, index + 1, 0, 0, true);
        setMerge(index, index, 1, 2, true);
        setMerge(index, index, 3, 4, true);
        setMerge(index, index + 1, 5, 5, true);
        setMerge(index, index + 1, 6, 6, true);
        setMerge(index, index + 1, 7, 7, true);
        setMerge(index, index + 1, 8, 8, true);
        setMerge(index, index + 1, 9, 9, true);
        setMerge(index, index + 1, 10, 10, true);
        setMerge(index, index + 1, 11, 11, true);
        setMerge(index, index + 1, 12, 12, true);
        int stt = 1;
        if (form.getTable() != null && form.getTable().size() > 0) {
            for (TransferSuccessTableForm item : form.getTable()) {
                createTableRow(stt ++, 
                    item.getPatientID() == null ? "" : item.getPatientID(), 
                    item.getPatientName() == null ? "" : item.getPatientName(),
                    item.getGenderID() == null ? "" : !"Nam".equals(item.getGenderID()) ? "" : item.getYearOfBirth(), 
                    item.getGenderID() == null ? "" : !"Nữ".equals(item.getGenderID()) ? "" : item.getYearOfBirth(), 
                    item.getCurrentAddress() == null ? "" : item.getCurrentAddress(),
                    item.getPermanentAddress()== null ? "" : item.getPermanentAddress(), 
                    item.getPhone() == null ? "" : item.getPhone(), 
                    item.getExchangeTime() == null ? "" : item.getExchangeTime(), 
                    StringUtils.isEmpty(item.getArrivalSite()) ? "" : item.getArrivalSite(), 
                    item.getRegisterTherapyTime() == null ? "" : item.getRegisterTherapyTime(), 
                    item.getRegisteredTherapySite() == null ? "" : item.getRegisteredTherapySite(),
                    item.getTherapyNo() == null ? "" : item.getTherapyNo()
                );
            }
        }
    }
}

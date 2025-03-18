package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacAidsReportForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author DSNAnh
 */
public class AidsReportExcel extends BaseView implements IExportExcel {
    private PacAidsReportForm form;
    private String extension;

    public AidsReportExcel(PacAidsReportForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Danh sach benh nhan AIDS";
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
        int total = form.getItems() != null ? form.getItems().size() : 0;
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 13, false);
        setMerge(2, 2, 0, 13, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        
        //Tạo thêm dòng trắng
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        if (StringUtils.isNotEmpty(form.getTitleLocation())) {
            createFilterRow("Địa chỉ ", form.getTitleLocation(), cellIndexFilter);
        } else {
            createFilterRow("Địa chỉ", form.getProvinceName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getAidsFrom()) || StringUtils.isNotEmpty(form.getSearch().getAidsTo())) {
            createFilterRow("Ngày chẩn đoán AIDS", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getAidsFrom()) ? ("từ " + form.getSearch().getAidsFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getAidsTo()) ? ("đến " + form.getSearch().getAidsTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo())) {
            createFilterRow("Ngày XNKĐ", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) ? ("từ " + form.getSearch().getConfirmTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo()) ? ("đến " + form.getSearch().getConfirmTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInputTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getInputTimeTo())) {
            createFilterRow("Ngày nhập liệu", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getInputTimeFrom()) ? ("từ " + form.getSearch().getInputTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getInputTimeTo()) ? ("đến " + form.getSearch().getInputTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getDeathTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getDeathTimeTo())) {
            createFilterRow("Ngày tử vong", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getDeathTimeFrom()) ? ("từ " + form.getSearch().getDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getDeathTimeTo()) ? ("đến " + form.getSearch().getDeathTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getUpdateTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getUpdateTimeTo())) {
            createFilterRow("Ngày cập nhật", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getUpdateTimeFrom()) ? ("từ " + form.getSearch().getUpdateTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getUpdateTimeTo()) ? ("đến " + form.getSearch().getUpdateTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getAgeFromParam()) || StringUtils.isNotEmpty(form.getSearch().getAgeToParam())) {
            createFilterRow("Tuổi", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getAgeFromParam()) ? ("từ " + form.getSearch().getAgeFromParam()) : ""), StringUtils.isNotEmpty(form.getSearch().getAgeToParam()) ? ("đến " + form.getSearch().getAgeToParam()) : ""), cellIndexFilter);
        }
        if (form.getSearch().getGenderIDs() != null && form.getSearch().getGenderIDs().size() > 0) {
            StringBuilder name = new StringBuilder();
            for(String id : form.getSearch().getGenderIDs()){
                name.append(form.getOptions().get(ParameterEntity.GENDER).get(id));
                name.append(",");
            }
            createFilterRow("Giới tính", name.deleteCharAt(name.length() - 1).toString(), cellIndexFilter);
        }
        if (form.getSearch().getRaceIDs()!= null && form.getSearch().getRaceIDs().size() > 0) {
            StringBuilder name = new StringBuilder();
            for(String id : form.getSearch().getRaceIDs()){
                name.append(form.getOptions().get(ParameterEntity.RACE).get(id));
                name.append(",");
            }
            createFilterRow("Dân tộc", name.deleteCharAt(name.length() - 1).toString(), cellIndexFilter);
        }
        
        if (form.getSearch().getTransmisionIDs()!= null && form.getSearch().getTransmisionIDs().size() > 0) {
            StringBuilder name = new StringBuilder();
            for(String id : form.getSearch().getTransmisionIDs()){
                name.append(form.getOptions().get(ParameterEntity.MODE_OF_TRANSMISSION).get(id));
                name.append(",");
            }
            createFilterRow("Đường lây nhiễm", name.deleteCharAt(name.length() - 1).toString(), cellIndexFilter);
        }
        if (form.getSearch().getTestObjectIDs()!= null && form.getSearch().getTestObjectIDs().size() > 0) {
            StringBuilder name = new StringBuilder();
            for(String id : form.getSearch().getTestObjectIDs()){
                name.append(form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(id));
                name.append(",");
            }
            createFilterRow("Nhóm đối tượng", name.deleteCharAt(name.length() - 1).toString(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getCode())) {
            createFilterRow("Mã số", form.getSearch().getCode(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getName())) {
            createFilterRow("Họ và tên", form.getSearch().getName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getBlood())) {
            createFilterRow("Nơi lấy mẫu XN", form.getOptions().get(ParameterEntity.BLOOD_BASE).get(form.getSearch().getBlood()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getPlaceTest())) {
            createFilterRow("Nơi XN khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getSearch().getPlaceTest()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getFacility())) {
            createFilterRow("Nơi điều trị", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getSearch().getFacility()), cellIndexFilter);
        }
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createFilterRow("Tổng số bệnh nhân AIDS", "" + total, cellIndexFilter);
    }
    
    private void createTable() throws Exception {
        String format = "dd/MM/yyyy";
        getCurrentSheet();
        //Dòng đầu tiên để trắng
        Row row = createRow();
        //Tiêu đề
        createTableHeaderRow("TT", "Họ và tên", "Năm sinh", "Giới tính", "Số CMND", "Số thẻ BHYT", 
                "Địa chỉ thường trú", "Địa chỉ hiện tại", "Ngày XN khẳng định", "Nơi XN khẳng định", 
                "Ngày chuẩn đoán AIDS", "Ngày tử vong", "Nguyên nhân tử vong", "Ngày nhập liệu");
        int stt = 1;
        if (form.getItems() != null && form.getItems().size() > 0) {
            for (PacPatientInfoEntity item : form.getItems()) {
                StringBuilder causeOfDeath = new StringBuilder();
                causeOfDeath.append("");
                if(item.getCauseOfDeath() != null && item.getCauseOfDeath().size() > 0){
                    for(String id : item.getCauseOfDeath()){
                        causeOfDeath.append(form.getOptions().get(ParameterEntity.CAUSE_OF_DEATH).get(id));
                        causeOfDeath.append(",");
                    }
                }
                createTableRow(stt ++, item.getFullname(), item.getYearOfBirth(),
                    StringUtils.isEmpty(item.getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getGenderID()), 
                    item.getIdentityCard() == null ? "" : item.getIdentityCard(), 
                    StringUtils.isEmpty(item.getHealthInsuranceNo()) ? "" : item.getHealthInsuranceNo(), 
                    item.getPermanentAddressFull() == null ? "" : item.getPermanentAddressFull(),
                    item.getCurrentAddressFull() == null ? "" : item.getCurrentAddressFull(), 
                    item.getConfirmTime()== null ? "" : TextUtils.formatDate(item.getConfirmTime(), format), 
                    StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()), 
                    item.getAidsStatusDate()== null ? "" : TextUtils.formatDate(item.getAidsStatusDate(), format), 
                    item.getDeathTime()== null ? "" : TextUtils.formatDate(item.getDeathTime(), format),
                    StringUtils.isNotEmpty(causeOfDeath.toString()) && causeOfDeath.length() > 0 ? causeOfDeath.deleteCharAt(causeOfDeath.length() - 1).toString() : "",
                    item.getCreateAT()== null ? "" : TextUtils.formatDate(item.getCreateAT(), format)
                );
            }
        }
    }
}

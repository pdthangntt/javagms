package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacAcceptForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class AcceptExcel extends BaseView implements IExportExcel {

    private PacAcceptForm form;
    private String extension;

    public AcceptExcel(PacAcceptForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Danh sach da ra soat";
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

    /**
     * Tạo header cho excel
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {
        HashMap<String, String> labels = (new PacPatientInfoEntity()).getAttributeLabels();

        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(180));
        sheet.setColumnWidth(1 + 2, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(2 + 2, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(3 + 2, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(4 + 2, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(5 + 2, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(6 + 2, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(7 + 2, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(8 + 2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9 + 2, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10 + 2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(11 + 2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(11 + 2, PixelUtils.pixel2WidthUnits(100));

        //Dòng đầu tiên để trắng
        setMerge(1, 1, 0, 8, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());

        setMerge(2, 2, 0, 8, false);
        createTitleDateRow((StringUtils.isEmpty(form.getSearch().getConfirmTimeFrom()) || StringUtils.isEmpty(form.getSearch().getConfirmTimeTo())) ? form.getConfirmTime() : form.getSearch().getConfirmTimeFrom().equals(form.getSearch().getConfirmTimeTo()) ? String.format("Ngày XN khẳng định %s", form.getSearch().getConfirmTimeFrom()) : String.format("Ngày XN khẳng định từ %s đến %s", form.getSearch().getConfirmTimeFrom(), form.getSearch().getConfirmTimeTo()));
        createRow();
        
        int cellIndexFilter = 2;
        
        if (StringUtils.isNotEmpty(form.getSearch().getFullname())) {
            createFilterRow(labels.get("fullname"), form.getSearch().getFullname(), cellIndexFilter);
        }
        if (form.getSearch().getYearOfBirth() != null) {
            createFilterRow(labels.get("yearOfBirth"), form.getSearch().getYearOfBirth().toString(), cellIndexFilter);
        }
        if (form.getSearch().getGenderID() != null) {
            createFilterRow(labels.get("genderID"), form.getOptions().get(ParameterEntity.GENDER).get(form.getSearch().getGenderID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getIdentityCard())) {
            createFilterRow(labels.get("identityCard"), form.getSearch().getIdentityCard(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getHealthInsuranceNo())) {
            createFilterRow(labels.get("healthInsuranceNo"), form.getSearch().getHealthInsuranceNo(), cellIndexFilter);
        }
        if (form.getSearch().getStatusOfResidentID() != null) {
            createFilterRow(labels.get("statusOfResidentID"), form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(form.getSearch().getStatusOfResidentID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getPermanentProvinceID())) {
            createFilterRow("hokhau".equals(form.getSearch().getAddressFilter()) ? "Tỉnh/Thành phố thường trú " : "Tỉnh/Thành phố tạm trú ", form.getProvinceNames().get(form.getSearch().getPermanentProvinceID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getPermanentDistrictName())) {
            createFilterRow("hokhau".equals(form.getSearch().getAddressFilter()) ?  "Quận/Huyện thường trú " : "Quận/Huyện tạm trú ", form.getPermanentDistrictName(), cellIndexFilter);
        }
        if (form.getPermanentWardName() != null && !"".equals(form.getPermanentWardName())) {
            createFilterRow("hokhau".equals(form.getSearch().getAddressFilter()) ?  "Xã/Phường thường trú " : "Xã/Phường tạm trú ", form.getPermanentWardName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSiteConfirmID())) {
            createFilterRow("Cơ sở khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getSearch().getSiteConfirmID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSiteTreatmentFacilityID())) {
            createFilterRow("Nơi điều trị", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getSearch().getSiteTreatmentFacilityID()), cellIndexFilter);
        }
        
        
        createFilterRow("Tên đơn vị ", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable() {

        createRow();
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Mã");
        if (form.isIsVAAC()) {
            headers.add("Tỉnh quản lý");
        }
        headers.add("Họ tên");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Số điện thoại");
        headers.add("Địa chỉ thường trú");
        headers.add("Địa chỉ hiện tại");
        headers.add("Hiện trạng cư trú");
        headers.add("Ngày XN khẳng định");
        headers.add("Cơ sở khẳng định");
        headers.add("Trạng thái điều trị");
        headers.add("Ngày xã rà soát");
        headers.add("Ngày huyện duyệt rà soát");
        headers.add("Ngày nhập liệu");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        // loop to create row content
        if (form.getItems().size() > 0) {
            List<Object> row;
            int index = 0;
            for (PacPatientInfoEntity item : form.getItems()) {
                row = new ArrayList<>();
                // Số thứ tự
                row.add(index += 1);
                row.add(item.getID());
                if (form.isIsVAAC()) {
                    row.add(StringUtils.isEmpty(item.getProvinceID()) ? "" : form.getProvinceNames().get(item.getProvinceID()));
                }
                row.add(StringUtils.isEmpty(item.getFullname()) ? "" : item.getFullname());
                row.add((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth()));
                row.add(StringUtils.isEmpty(item.getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getGenderID()));
                row.add(StringUtils.isEmpty(item.getIdentityCard()) ? "" : item.getIdentityCard());
                row.add(StringUtils.isEmpty(item.getHealthInsuranceNo()) ? "" : item.getHealthInsuranceNo());
                row.add(StringUtils.isEmpty(item.getPatientPhone()) ? "" : item.getPatientPhone());
                row.add(StringUtils.isEmpty(item.getPermanentAddressFull()) ? "" : item.getPermanentAddressFull());
                row.add(StringUtils.isEmpty(item.getCurrentAddressFull()) ? "" : item.getCurrentAddressFull());
                row.add(StringUtils.isEmpty(item.getStatusOfResidentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(item.getStatusOfResidentID()));
                row.add(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));
                row.add(StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()));
                row.add(item.getReviewWardTime()== null ? "" : TextUtils.formatDate(item.getReviewWardTime(), "dd/MM/yyyy"));
                row.add(item.getReviewDistrictTime() == null ? "" : TextUtils.formatDate(item.getReviewDistrictTime(), "dd/MM/yyyy"));
                row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));
                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }

}

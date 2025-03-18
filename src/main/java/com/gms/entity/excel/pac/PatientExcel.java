package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacPatientForm;
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
public class PatientExcel extends BaseView implements IExportExcel {

    private PacPatientForm form;
    private String extension;

    public PatientExcel(PacPatientForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Danh sach quan ly";
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
        int o = 0;
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(o++, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 8, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());
        createRow();

        int cellIndexFilter = 1;
        if (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo())) {
            createFilterRow("Ngày XN khẳng định", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) ? ("từ " + form.getSearch().getConfirmTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo()) ? ("đến " + form.getSearch().getConfirmTimeTo()) : ""), cellIndexFilter);
        }

        if (form.getSearch().getFullname() != null) {
            createFilterRow(labels.get("fullname"), form.getSearch().getFullname(), cellIndexFilter);
        }

        if (form.getSearch().getYearOfBirth() != null) {
            createFilterRow(labels.get("yearOfBirth"), String.valueOf(form.getSearch().getYearOfBirth()), cellIndexFilter);
        }
        if (form.getSearch().getGenderID() != null) {
            createFilterRow(labels.get("genderID"), form.getOptions().get(ParameterEntity.GENDER).get(form.getSearch().getGenderID()), cellIndexFilter);
        }
        if (form.getSearch().getIdentityCard() != null) {
            createFilterRow(labels.get("identityCard"), form.getSearch().getIdentityCard(), cellIndexFilter);
        }
        if (form.getSearch().getHealthInsuranceNo() != null) {
            createFilterRow(labels.get("healthInsuranceNo"), form.getSearch().getHealthInsuranceNo(), cellIndexFilter);
        }
        if (form.getSearch().getStatusOfResidentIDs() != null) {
            String statusOfResident = "";
            for (String s : form.getSearch().getStatusOfResidentIDs()) {
                if (statusOfResident.equals("")) {
                    statusOfResident = form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(s);
                } else {
                    statusOfResident += "," + form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(s);
                }
            }
            createFilterRow(labels.get("statusOfResidentID"), statusOfResident, cellIndexFilter);
        }
        if (form.getSearch().getStatusOfTreatmentID() != null) {
            createFilterRow(labels.get("statusOfTreatmentID"), form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(form.getSearch().getStatusOfTreatmentID()), cellIndexFilter);
        }
        String addressType = "hokhau".equals(form.getSearch().getAddressFilter()) ? "thường trú " : "tạm trú ";
        if (form.getPermanentProvinceName() != null && !"".equals(form.getPermanentProvinceName())) {
            createFilterRow("Tỉnh/Thành phố " + addressType, form.getPermanentProvinceName(), cellIndexFilter);
        }
        if (form.getPermanentDistrictName() != null && !"".equals(form.getPermanentDistrictName())) {
            createFilterRow("Quận/Huyện " + addressType, form.getPermanentDistrictName(), cellIndexFilter);
        }
        if (form.getPermanentWardName() != null && !"".equals(form.getPermanentWardName())) {
            createFilterRow("Xã/Phường " + addressType, form.getPermanentWardName(), cellIndexFilter);
        }

        if (StringUtils.isNotEmpty(form.getSearch().getSiteConfirmID())) {
            createFilterRow("Cơ sở khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getSearch().getSiteConfirmID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSiteTreatmentFacilityID())) {
            createFilterRow("Nơi điều trị", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getSearch().getSiteTreatmentFacilityID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeTo())) {
            createFilterRow("Ngày báo tử vong", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeFrom()) ? ("từ " + form.getSearch().getRequestDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeTo()) ? ("đến " + form.getSearch().getRequestDeathTimeTo()) : ""), cellIndexFilter);
        }
        createRow();
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất excel", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);

    }

    private void createTable() throws Exception {
        createRow();

        List<String> headers = new ArrayList<>();
        headers.add("TT");
        if (form.getSearch().isIsVAAC()) {
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
        headers.add("Nơi điều trị");
        if ("review".equals(form.getTab())) {
            headers.add("Ngày bắt đầu điều trị");
        }
        if ("review".equals(form.getTab())) {
            headers.add("Ngày biến động điều trị");
        }
        headers.add("Ngày quản lý");
        if ("dead".equals(form.getTab())) {
            headers.add("Ngày báo tử vong");
        }
        if ("dead".equals(form.getTab())) {
            headers.add("Ngày tử vong");
        }
        headers.add("Ngày nhập liệu");

        headers.add("Ngày cập nhật");
        headers.add("Ngày yêu cầu rà soát lại");
        headers.add("Ngày rà soát lại");
        headers.add("Ngày huyện duyệt rà soát lại");
        headers.add("Mã HIV Info");
        headers.add("Tỉnh/Thành phố thường trú");
        headers.add("Quận/Huyện thường trú");
        headers.add("Phường/xã thường trú");
        headers.add("Tỉnh/Thành phố tạm trú");
        headers.add("Quận/Huyện tạm trú");
        headers.add("Phường/xã tạm trú");
        headers.add("Mã IMS");

        createTableHeaderRow(headers.toArray(new String[headers.size()]));
        if (form.getItems().size() > 0) {
            List<Object> row;
            int index = 0;
            for (PacPatientInfoEntity item : form.getItems()) {
                row = new ArrayList<>();

                row.add(index += 1);
                if (form.getSearch().isIsVAAC()) {
                    row.add(StringUtils.isEmpty(item.getProvinceID()) ? "" : form.getOptions().get("provinces").get(item.getProvinceID()));
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
                row.add(StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID()));
                if ("review".equals(form.getTab())) {
                    row.add(item.getStartTreatmentTime() == null ? "" : TextUtils.formatDate(item.getStartTreatmentTime(), "dd/MM/yyyy"));
                }
                if ("review".equals(form.getTab())) {
                    row.add(item.getChangeTreatmentDate() == null ? "" : TextUtils.formatDate(item.getChangeTreatmentDate(), "dd/MM/yyyy"));
                }
                row.add(item.getReviewProvinceTime() == null ? "" : TextUtils.formatDate(item.getReviewProvinceTime(), "dd/MM/yyyy"));
                if ("dead".equals(form.getTab())) {
                    row.add(item.getRequestDeathTime() == null ? "" : TextUtils.formatDate(item.getRequestDeathTime(), "dd/MM/yyyy"));
                }
                if ("dead".equals(form.getTab())) {
                    row.add(item.getDeathTime() == null ? "" : TextUtils.formatDate(item.getDeathTime(), "dd/MM/yyyy"));
                }
                row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));

                row.add(item.getUpdateAt() == null ? "" : TextUtils.formatDate(item.getUpdateAt(), "dd/MM/yyyy"));
                row.add(item.getCheckProvinceTime() == null ? "" : TextUtils.formatDate(item.getCheckProvinceTime(), "dd/MM/yyyy"));
                row.add(item.getCheckWardTime() == null ? "" : TextUtils.formatDate(item.getCheckWardTime(), "dd/MM/yyyy"));
                row.add(item.getCheckDistrictTime() == null ? "" : TextUtils.formatDate(item.getCheckDistrictTime(), "dd/MM/yyyy"));
                row.add(form.getHivCodeOptions().getOrDefault(item.getID() + "", ""));

                row.add(form.getOptions().get("provinces").get(item.getPermanentProvinceID()));
                row.add(form.getOptions().get("districts").get(item.getPermanentDistrictID()));
                row.add(form.getOptions().get("wards").get(item.getPermanentWardID()));

                row.add(form.getOptions().get("provinces").get(item.getCurrentProvinceID()));
                row.add(form.getOptions().get("districts").get(item.getCurrentDistrictID()));
                row.add(form.getOptions().get("wards").get(item.getCurrentWardID()));
                row.add(item.getID());

                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }

}

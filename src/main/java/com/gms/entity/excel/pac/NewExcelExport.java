package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.ManageStatusEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacNewExportForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author NamAnh_HaUI
 */
public class NewExcelExport extends BaseView implements IExportExcel {

    private PacNewExportForm form;
    private String extension;

    public NewExcelExport(PacNewExportForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "DS Phat hien";
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
        HashMap<String, String> labels = (new PacPatientInfoEntity()).getAttributeLabels();

        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 8, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());
        createRow();

        int cellIndexFilter = 1;
        if (StringUtils.isNotEmpty(form.getTitleLocation())) {
            createFilterRow("Địa chỉ ", form.getTitleLocation(), cellIndexFilter);
        } else {
            createFilterRow("Địa chỉ", form.getProvinceName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getCode())) {
            createFilterRow("Mã số ", form.getSearch().getCode(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getName())) {
            createFilterRow("Họ và tên ", form.getSearch().getName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getDeathTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getDeathTimeTo())) {
            createFilterRow("Ngày quản lý", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getDeathTimeFrom()) ? ("từ " + form.getSearch().getDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getDeathTimeTo()) ? ("đến " + form.getSearch().getDeathTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo())) {
            createFilterRow("Ngày XN khẳng định", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) ? ("từ " + form.getSearch().getConfirmTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo()) ? ("đến " + form.getSearch().getConfirmTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInputTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getInputTimeTo())) {
            createFilterRow("Ngày nhập liệu", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getInputTimeFrom()) ? ("từ " + form.getSearch().getInputTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getInputTimeTo()) ? ("đến " + form.getSearch().getInputTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getAgeFromParam()) || StringUtils.isNotEmpty(form.getSearch().getAgeToParam())) {
            createFilterRow("Tuổi", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getAgeFromParam()) ? ("từ " + form.getSearch().getAgeFromParam()) : ""), StringUtils.isNotEmpty(form.getSearch().getAgeToParam()) ? ("đến " + form.getSearch().getAgeToParam()) : ""), cellIndexFilter);
        }
        if (form.getSearch().getGenderIDs() != null && !form.getSearch().getGenderIDs().isEmpty()) {
            StringBuilder gender = new StringBuilder();
            for (String s : form.getSearch().getGenderIDs()) {
                gender.append(form.getOptions().get(ParameterEntity.GENDER).get(s));
                gender.append(", ");
            }
            createFilterRow(labels.get("genderID"), gender.deleteCharAt(gender.length() - 2).toString(), cellIndexFilter);
        }
        if (form.getSearch().getResidentIDs() != null && !form.getSearch().getResidentIDs().isEmpty()) {
            StringBuilder resident = new StringBuilder();
            for (String s : form.getSearch().getResidentIDs()) {
                resident.append(form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(s));
                resident.append(", ");
            }
            createFilterRow("Hiện trạng cư trú", resident.deleteCharAt(resident.length() - 2).toString(), cellIndexFilter);
        }
        if (form.getSearch().getTransmisionIDs() != null && !form.getSearch().getTransmisionIDs().isEmpty()) {
            StringBuilder modeOfTransmission = new StringBuilder();
            for (String s : form.getSearch().getTransmisionIDs()) {
                modeOfTransmission.append(form.getOptions().get(ParameterEntity.MODE_OF_TRANSMISSION).get(s));
                modeOfTransmission.append(", ");
            }
            createFilterRow(labels.get("modeOfTransmissionID"), modeOfTransmission.deleteCharAt(modeOfTransmission.length() - 2).toString(), cellIndexFilter);
        }
        if (form.getSearch().getTestObjectIDs() != null && !form.getSearch().getTestObjectIDs().isEmpty()) {
            StringBuilder objectGroup = new StringBuilder();
            for (String s : form.getSearch().getTestObjectIDs()) {
                objectGroup.append(form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(s));
                objectGroup.append(", ");
            }
            createFilterRow(labels.get("objectGroupID"), objectGroup.deleteCharAt(objectGroup.length() - 2).toString(), cellIndexFilter);
        }
        if (form.getSearch().getTreamentIDs() != null && !form.getSearch().getTreamentIDs().isEmpty()) {
            StringBuilder treament = new StringBuilder();
            for (String s : form.getSearch().getTreamentIDs()) {
                treament.append(form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(s));
                treament.append(", ");
            }
            createFilterRow(labels.get("statusOfTreatmentID"), treament.deleteCharAt(treament.length() - 2).toString(), cellIndexFilter);
        }

        if (form.getSearch().getRaceIDs() != null && !form.getSearch().getRaceIDs().isEmpty()) {
            StringBuilder race = new StringBuilder();
            for (String s : form.getSearch().getRaceIDs()) {
                race.append(form.getOptions().get(ParameterEntity.RACE).get(s));
                race.append(",");
            }
            createFilterRow(labels.get("raceID"), race.deleteCharAt(race.length() - 1).toString(), cellIndexFilter);
        }
        if (form.getSearch().getPatientStatus() != null) {
            createFilterRow("Trạng thái người nhiễm", "1".equals(form.getSearch().getPatientStatus()) ? "Còn sống" : "Tử vong", cellIndexFilter);
        }
        // Ngày chẩn đoán AIDS
        if (StringUtils.isNotEmpty(form.getSearch().getAidsFrom()) || StringUtils.isNotEmpty(form.getSearch().getAidsTo())) {
            createFilterRow("Ngày chẩn đoán AIDS ", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getAidsFrom()) ? ("từ " + form.getSearch().getAidsFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getAidsTo()) ? ("đến " + form.getSearch().getAidsTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getUpdateTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getUpdateTimeTo())) {
            createFilterRow("Ngày cập nhật ", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getUpdateTimeFrom()) ? ("từ " + form.getSearch().getUpdateTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getUpdateTimeTo()) ? ("đến " + form.getSearch().getUpdateTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getBlood())) {
            createFilterRow("Nơi lấy mẫu XN ", form.getOptions().get(ParameterEntity.BLOOD_BASE).get(form.getSearch().getBlood()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getPlaceTest())) {
            createFilterRow("Nơi XN khẳng định ", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getSearch().getPlaceTest()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getFacility())) {
            createFilterRow("Nơi điều trị ", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getSearch().getFacility()), cellIndexFilter);
        }
        
        createRow();
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createRow();
        createFilterRow("Tổng số người nhiễm phát hiện", String.valueOf(form.getItems().size()), cellIndexFilter);
        createRow();
    }

    private void createTable() throws Exception {
        List<String> headers = new ArrayList<>();
        headers.add("TT");

        if (form.isIsVAAC()) {
            headers.add("Tỉnh quản lý");
        }
        headers.add("Họ và tên");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Địa chỉ thường trú");
        headers.add("Địa chỉ hiện tại");
        headers.add("Hiện trạng cư trú");
        headers.add("Ngày XN khẳng định");
        headers.add("Nơi XN khẳng định");
        headers.add("Trạng thái điều trị");
        headers.add("Ngày BĐ điều trị");
        headers.add("Nơi điều trị");
        headers.add("Ngày chẩn đoán AIDS");
        headers.add("Ngày tử vong");
        headers.add("Ngày quản lý");
        headers.add("Ngày nhập liệu");
        headers.add("Ghi chú");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        if (form.getItems().size() > 0) {
            List<Object> row;
            int index = 0;
            for (PacPatientInfoEntity item : form.getItems()) {
                row = new ArrayList<>();

                row.add(index += 1);
                if (form.isIsVAAC()) {
                    row.add(StringUtils.isNotEmpty(item.getProvinceID()) && form.getAllProvincesName() != null && !form.getAllProvincesName().isEmpty() ? form.getAllProvincesName().get(item.getProvinceID()) : "");
                }

                row.add(StringUtils.isEmpty(item.getFullname()) ? "" : item.getFullname());
                row.add((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth()));
                row.add(StringUtils.isEmpty(item.getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getGenderID()));
                row.add(StringUtils.isEmpty(item.getIdentityCard()) ? "" : item.getIdentityCard());
                row.add(StringUtils.isEmpty(item.getHealthInsuranceNo()) ? "" : item.getHealthInsuranceNo());
                row.add(StringUtils.isEmpty(item.getPermanentAddressFull()) ? "" : item.getPermanentAddressFull());
                row.add(StringUtils.isEmpty(item.getCurrentAddressFull()) ? "" : item.getCurrentAddressFull());
                row.add(StringUtils.isEmpty(item.getStatusOfResidentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(item.getStatusOfResidentID()));
                row.add(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : StringUtils.isNotEmpty(form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID())) ? form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()) : "");
                row.add(StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : StringUtils.isNotEmpty(form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID())) ? form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()) : "");
                row.add(item.getStartTreatmentTime() == null ? "" : TextUtils.formatDate(item.getStartTreatmentTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID()));
                row.add(item.getAidsStatusDate() == null ? "" : TextUtils.formatDate(item.getAidsStatusDate(), "dd/MM/yyyy"));
                row.add(item.getDeathTime() == null ? "" : TextUtils.formatDate(item.getDeathTime(), "dd/MM/yyyy"));
                row.add(item.getReviewProvinceTime() == null ? "" : TextUtils.formatDate(item.getReviewProvinceTime(), "dd/MM/yyyy"));
                row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));
                row.add(StringUtils.isNotEmpty(item.getNote()) ? item.getNote() : "");
                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }
}

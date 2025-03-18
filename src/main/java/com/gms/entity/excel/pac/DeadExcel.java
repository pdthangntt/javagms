package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.ManageStatusEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacDeadForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 *
 * @author NamAnh_HaUI
 */
public class DeadExcel extends BaseView implements IExportExcel {

    private PacDeadForm form;
    private String extension;

    public DeadExcel(PacDeadForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "DS tu vong";
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
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(500));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 8, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());
        createRow();

        int cellIndexFilter = 1;
        if (StringUtils.isNotEmpty(form.getTitleLocation())) {
            createFilterRow("Địa chỉ", form.getTitleLocation(), cellIndexFilter);
        } else {
            createFilterRow("Địa chỉ", form.getProvinceName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getDeathTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getDeathTimeTo())) {
            createFilterRow("Ngày tử vong", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getDeathTimeFrom()) ? ("từ " + form.getSearch().getDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getDeathTimeTo()) ? ("đến " + form.getSearch().getDeathTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo())) {
            createFilterRow("Ngày XN khẳng định", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getConfirmTimeFrom()) ? ("từ " + form.getSearch().getConfirmTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getConfirmTimeTo()) ? ("đến " + form.getSearch().getConfirmTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInputTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getInputTimeTo())) {
            createFilterRow("Ngày nhập liệu", String.format("%s %s", (StringUtils.isNotEmpty(form.getSearch().getInputTimeFrom()) ? ("từ " + form.getSearch().getInputTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getInputTimeTo()) ? ("đến " + form.getSearch().getInputTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeTo())) {
            createFilterRow("Ngày báo tử vong", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeFrom()) ? ("từ " + form.getSearch().getRequestDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getRequestDeathTimeTo()) ? ("đến " + form.getSearch().getRequestDeathTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getUpdateTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getUpdateTimeTo())) {
            createFilterRow("Ngày cập nhật", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getUpdateTimeFrom()) ? ("từ " + form.getSearch().getUpdateTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getUpdateTimeTo()) ? ("đến " + form.getSearch().getUpdateTimeTo()) : ""), cellIndexFilter);
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
        if (form.getSearch().getRaceIDs() != null && !form.getSearch().getRaceIDs().isEmpty()) {
            StringBuilder race = new StringBuilder();
            for (String s : form.getSearch().getRaceIDs()) {
                race.append(form.getOptions().get(ParameterEntity.RACE).get(s));
                race.append(",");
            }
            createFilterRow(labels.get("raceID"), race.deleteCharAt(race.length() - 1).toString(), cellIndexFilter);
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
        createRow();
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createRow();
        createFilterRow("Tổng số người nhiễm tử vong", String.valueOf(form.getItems().size()), cellIndexFilter);
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
        headers.add("Ngày XN khẳng định");
        headers.add("Nơi XN khẳng định");
        headers.add("Ngày tử vong");
        headers.add("Nguyên nhân tử vong");
        headers.add("Ngày báo tử vong");
        headers.add("Ngày nhập liệu");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        if (form.getItems() != null && form.getItems().size() > 0) {
            List<Object> row;
            int index = 0;
            for (PacPatientInfoEntity item : form.getItems()) {
                row = new ArrayList<>();
                row.add(index += 1);
                if (form.isIsVAAC()) {
                    headers.add(StringUtils.isNotEmpty(item.getProvinceID()) && form.getAllProvincesName() != null && !form.getAllProvincesName().isEmpty() ? form.getAllProvincesName().get(item.getProvinceID()) : "");
                }
                row.add(StringUtils.isEmpty(item.getFullname()) ? "" : item.getFullname());
                row.add((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth()));
                row.add(StringUtils.isEmpty(item.getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getGenderID()));
                row.add(StringUtils.isEmpty(item.getIdentityCard()) ? "" : item.getIdentityCard());
                row.add(StringUtils.isEmpty(item.getHealthInsuranceNo()) ? "" : item.getHealthInsuranceNo());
                row.add(StringUtils.isEmpty(item.getPermanentAddressFull()) ? "" : item.getPermanentAddressFull());
                row.add(StringUtils.isEmpty(item.getCurrentAddressFull()) ? "" : item.getCurrentAddressFull());
                row.add(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));
                row.add(item.getDeathTime() == null ? "" : TextUtils.formatDate(item.getDeathTime(), "dd/MM/yyyy"));

                List<String> causeOfDeath = new LinkedList<>();
                if (item.getCauseOfDeath() != null && item.getCauseOfDeath().size() > 0) {
                    for (String string : item.getCauseOfDeath()) {
                        causeOfDeath.add(form.getOptions().get(ParameterEntity.CAUSE_OF_DEATH).get(string));
                    }
                }
                row.add(causeOfDeath.isEmpty() ? "" : String.join("; ", causeOfDeath));
                row.add(item.getRequestDeathTime() == null ? "" : TextUtils.formatDate(item.getRequestDeathTime(), "dd/MM/yyyy"));
                row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));
                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }
}

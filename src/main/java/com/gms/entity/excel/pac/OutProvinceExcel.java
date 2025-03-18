package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.OutProvinceFrom;
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
public class OutProvinceExcel extends BaseView implements IExportExcel {

    private OutProvinceFrom form;
    private String extension;

    public OutProvinceExcel(OutProvinceFrom form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Danh sach ngoai tinh";
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

        int i = 0;
        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(40));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(120));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(form.getTab() != null && form.getTab().equals("vaac") ? 230 : 100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(form.getTab() != null && form.getTab().equals("vaac") ? 100 : 230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));

        //Dòng đầu tiên để trắng
        setMerge(1, 1, 0, 10, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());

        setMerge(2, 2, 0, 10, false);
        createTitleDateRow(form.getConfirmTime());
        createRow();

        int cellIndexFilter = 1;

        if (StringUtils.isNotEmpty(form.getSearch().getProvinceID())) {
            createFilterRow("Tỉnh/TP quản lý", form.getOptions().get("provinces").get(form.getSearch().getProvinceID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getProvinceID())) {
            createFilterRow("Tỉnh/TP phát hiện", form.getOptions().get("provinces").get(form.getSearch().getDetectProvinceID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getProvinceID())) {
            createFilterRow("Tỉnh/TP thường trú", form.getOptions().get("provinces").get(form.getSearch().getPermanentProvinceID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getProvinceID())) {
            createFilterRow("Tỉnh/TP tạm trú", form.getOptions().get("provinces").get(form.getSearch().getCurrentProvinceID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getFullname())) {
            createFilterRow(labels.get("fullname"), form.getSearch().getFullname(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getYearOfBirth())) {
            createFilterRow(labels.get("yearOfBirth"), form.getSearch().getYearOfBirth(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getGenderID())) {
            createFilterRow(labels.get("genderID"), form.getOptions().get(ParameterEntity.GENDER).get(form.getSearch().getGenderID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getIdentityCard())) {
            createFilterRow(labels.get("identityCard"), form.getSearch().getIdentityCard(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getBloodBaseID())) {
            createFilterRow("Cơ sở lấy mẫu", form.getOptions().get(ParameterEntity.BLOOD_BASE).get(form.getSearch().getBloodBaseID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSourceServiceID())) {
            createFilterRow("Nguồn thông tin", form.getOptions().get(ParameterEntity.SERVICE).get(form.getSearch().getSourceServiceID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSiteConfirmID())) {
            createFilterRow("Cơ sở khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getSearch().getSiteConfirmID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSiteTreatmentFacilityID())) {
            createFilterRow("Nơi điều trị", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getSearch().getSiteTreatmentFacilityID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getStatus())) {
            createFilterRow("Trạng thái", form.getOptions().get("status-review").get(form.getSearch().getStatus()), cellIndexFilter);
        }

        createFilterRow("Tên đơn vị ", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất danh sách", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable() {

        createRow();
        List<String> headers = new ArrayList<>();
        headers.add("STT");
        headers.add("Tỉnh phát hiện");
        if (form.getTab() != null && !form.getTab().equals("vaac")) {
            headers.add("Tỉnh quản lý");
        }
        headers.add("Mã");
        headers.add("Họ tên");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Địa chỉ thường trú");
        headers.add("Địa chỉ hiện tại");
        headers.add("Cơ sở lấy máu");
        headers.add("Ngày XN khẳng định");
        headers.add("Cơ sở khẳng định");
        headers.add("Trạng thái điều trị");
        headers.add("Ngày bắt đầu điều trị");
        headers.add("Nơi điều trị");
        if (form.getTab() != null && form.getTab().equals("review")) {
            headers.add("Nguồn thông tin");
        }
        if (form.getTab() != null && !form.getTab().equals("review")) {
            headers.add("Ngày tỉnh rà soát");
        }
        if (form.getTab() != null && !form.getTab().equals("review")) {
            headers.add("Ngày TW yêu cầu rà soát lại");
        }
        headers.add("Ngày nhập liệu");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        // loop to create row content
        if (form.getDataPage().getData().size() > 0) {
            List<Object> row;
            int index = 0;
            for (PacPatientInfoEntity item : form.getDataPage().getData()) {
                row = new ArrayList<>();
                // Số thứ tự
                row.add(index += 1);
                row.add(StringUtils.isEmpty(item.getDetectProvinceID()) ? "" : form.getOptions().get("provinces").get(item.getDetectProvinceID()));
                if (form.getTab() != null && !form.getTab().equals("vaac")) {
                    row.add(StringUtils.isEmpty(item.getProvinceID()) ? "" : form.getOptions().get("provinces").get(item.getProvinceID()));
                }
                row.add(item.getID().toString());
                row.add(StringUtils.isEmpty(item.getFullname()) ? "" : item.getFullname());
                row.add((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth()));
                row.add(StringUtils.isEmpty(item.getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getGenderID()));
                row.add(StringUtils.isEmpty(item.getIdentityCard()) ? "" : item.getIdentityCard());
                row.add(StringUtils.isEmpty(item.getHealthInsuranceNo()) ? "" : item.getHealthInsuranceNo());
                row.add(StringUtils.isEmpty(item.getPermanentAddressFull()) ? "" : item.getPermanentAddressFull());
                row.add(StringUtils.isEmpty(item.getCurrentAddressFull()) ? "" : item.getCurrentAddressFull());
                row.add(StringUtils.isEmpty(item.getBloodBaseID()) ? "" : form.getOptions().get(ParameterEntity.BLOOD_BASE).get(item.getBloodBaseID()));
                row.add(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));
                row.add(StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()));
                row.add(item.getStartTreatmentTime() == null ? "" : TextUtils.formatDate(item.getStartTreatmentTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID()));
                if (form.getTab() != null && form.getTab().equals("review")) {
                    row.add(StringUtils.isEmpty(item.getSourceServiceID()) ? "" : form.getOptions().get(ParameterEntity.SERVICE).get(item.getSourceServiceID()));
                }
                if (form.getTab() != null && !form.getTab().equals("review")) {
                    row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getReviewProvinceTime(), "dd/MM/yyyy"));
                }
                if (form.getTab() != null && !form.getTab().equals("review")) {
                    row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getReviewVaacTime(), "dd/MM/yyyy"));
                }
                row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));

                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }

}

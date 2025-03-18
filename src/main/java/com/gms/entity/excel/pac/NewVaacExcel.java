package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacNewForm;
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
public class NewVaacExcel extends BaseView implements IExportExcel {

    private PacNewForm form;
    private String extension;
    private String tab;

    public NewVaacExcel(PacNewForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Trung uong yeu cau ra soat";
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
        int O = 0;
        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(80));

        setMerge(1, 1, 0, 8, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());
        createRow();

        int cellIndexFilter = 1;
        if (form.getProvinceName() != null && !"".equals(form.getProvinceName())) {
            createFilterRow("Tỉnh/Thành phố thường trú", form.getProvinceName(), cellIndexFilter);
        }
        if (form.getDistrictName() != null && !"".equals(form.getDistrictName())) {
            createFilterRow("Quận/Huyện thường trú", form.getDistrictName(), cellIndexFilter);
        }
        if (form.getWardName() != null && !"".equals(form.getWardName())) {
            createFilterRow("Xã/Phường thường trú", form.getWardName(), cellIndexFilter);
        }

        if (!StringUtils.isEmpty(form.getSearch().getFullname())) {
            createFilterRow(labels.get("fullname"), form.getSearch().getFullname(), cellIndexFilter);
        }
        if (form.getSearch().getYearOfBirth() != 0 || form.getSearch().getYearOfBirth() != null) {
            createFilterRow(labels.get("yearOfBirth"), form.getSearch().getYearOfBirth(), cellIndexFilter);
        }
        if (!StringUtils.isEmpty(form.getSearch().getGenderID())) {
            createFilterRow(labels.get("genderID"), form.getOptions().get(ParameterEntity.GENDER).get(form.getSearch().getGenderID()), cellIndexFilter);
        }
        if (!StringUtils.isEmpty(form.getSearch().getIdentityCard())) {
            createFilterRow(labels.get("identityCard"), form.getSearch().getIdentityCard(), cellIndexFilter);
        }
        if (!StringUtils.isEmpty(form.getSearch().getBlood())) {
            createFilterRow("Cơ sở lấy mẫu", form.getOptions().get(ParameterEntity.BLOOD_BASE).get(form.getSearch().getBlood()), cellIndexFilter);
        }
        if (!StringUtils.isEmpty(form.getSearch().getService())) {
            createFilterRow("Nguồn thông tin", form.getOptions().get(ParameterEntity.SERVICE).get(form.getSearch().getService()), cellIndexFilter);
        }
        if (!StringUtils.isEmpty(form.getSearch().getSiteConfirmID())) {
            createFilterRow("Cơ sở khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getSearch().getSiteConfirmID()), cellIndexFilter);
        }
        if (!StringUtils.isEmpty(form.getSearch().getSiteTreatmentFacilityID())) {
            createFilterRow("Nơi điều trị", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getSearch().getSiteTreatmentFacilityID()), cellIndexFilter);
        }
        createRow();
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        createRow();

        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Mã");
        headers.add("Họ tên");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Số điện thoại");
        headers.add("Địa chỉ thường trú");
        headers.add("Địa chỉ hiện tại");
        headers.add("Ngày XN khẳng định");
        headers.add("Cơ sở khẳng định");
        headers.add("Trạng thái điều trị");
        headers.add("Ngày bắt đầu điều trị");
        headers.add("Nơi điều trị");
        headers.add("Ngày TW yêu cầu rà soát");
        headers.add("Ngày tỉnh rà soát");
        headers.add("Ngày nhập liệu");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        if (form.getItems().size() > 0) {
            List<Object> row;
            int index = 0;
            for (PacPatientInfoEntity item : form.getItems()) {
                row = new ArrayList<>();

                row.add(index += 1);
                row.add(item.getID());
                row.add(StringUtils.isEmpty(item.getFullname()) ? "" : item.getFullname());
                row.add((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth()));
                row.add(StringUtils.isEmpty(item.getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getGenderID()));
                row.add(StringUtils.isEmpty(item.getIdentityCard()) ? "" : item.getIdentityCard());
                row.add(StringUtils.isEmpty(item.getHealthInsuranceNo()) ? "" : item.getHealthInsuranceNo());
                row.add(StringUtils.isEmpty(item.getPatientPhone()) ? "" : item.getPatientPhone());
                row.add(StringUtils.isEmpty(item.getPermanentAddressFull()) ? "" : item.getPermanentAddressFull());
                row.add(StringUtils.isEmpty(item.getCurrentAddressFull()) ? "" : item.getCurrentAddressFull());
                row.add(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));
                row.add(StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()));
                row.add(item.getStartTreatmentTime() == null ? "" : TextUtils.formatDate(item.getStartTreatmentTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID()));

                row.add(item.getReviewVaacTime() == null ? "" : TextUtils.formatDate(item.getReviewVaacTime(), "dd/MM/yyyy"));
                row.add(item.getReviewProvinceTime() == null ? "" : TextUtils.formatDate(item.getReviewProvinceTime(), "dd/MM/yyyy"));
                row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));
                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }
}

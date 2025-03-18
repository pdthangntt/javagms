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
public class NewExcel extends BaseView implements IExportExcel {

    private PacNewForm form;
    private String extension;
    private String tab;

    public NewExcel(PacNewForm form, String extension, String tab) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.tab = tab;
        this.sheetName = "Danh sach chua ra soat";
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
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(60));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(355));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(O++, PixelUtils.pixel2WidthUnits(150));

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
        if (form.getProvinceName() != null && !"".equals(form.getProvinceName())) {
            createFilterRow("Tỉnh/Thành phố thường trú", form.getProvinceName(), cellIndexFilter);
        }
        if (form.getDistrictName() != null && !"".equals(form.getDistrictName())) {
            createFilterRow("Quận/Huyện thường trú", form.getDistrictName(), cellIndexFilter);
        }
        if (form.getWardName() != null && !"".equals(form.getWardName())) {
            createFilterRow("Xã/Phường thường trú", form.getWardName(), cellIndexFilter);
        }

        if (tab.equals(PacTabEnum.NEW_OUT_PROVINCE.getKey()) && !"".equals(form.getSearch().getAcceptPermanentTime())) {
            createFilterRow("Trạng thái", "2".equals(form.getSearch().getAcceptPermanentTime()) ? "Chưa chuyển" : "Đã chuyển", cellIndexFilter);
        }
        //detectProvinceID
        if (tab.equals(PacTabEnum.NEW_OTHER_PROVINCE.getKey()) && form.getSearch().getSearchDetectProvinceID() != null && !"".equals(form.getSearch().getSearchDetectProvinceID())) {
            createFilterRow("Tỉnh thành phát hiện", form.getDetectProvinceName(), cellIndexFilter);
        }
        if (form.getSearch().getBloodBase() != null && !"".equals(form.getSearch().getBloodBase())) {
            createFilterRow("Cơ sở lấy mẫu", form.getOptions().get(ParameterEntity.BLOOD_BASE).get(form.getSearch().getBloodBase()), cellIndexFilter);
        }

        if (form.getSearch().getService() != null && !"".equals(form.getSearch().getService())) {
            createFilterRow("Nguồn thông tin", form.getOptions().get(ParameterEntity.SERVICE).get(form.getSearch().getService()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSiteConfirmID())) {
            createFilterRow("Cơ sở khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getSearch().getSiteConfirmID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getSiteTreatmentFacilityID())) {
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
        headers.add("Cơ sở lấy mẫu");
        headers.add("Ngày XN khẳng định");
        headers.add("Cơ sở khẳng định");

        if (tab.equals(PacTabEnum.NEW_OUT_PROVINCE.getKey())) {
            headers.add("Trạng thái");
        }

        headers.add("Nguồn thông tin");
        headers.add("Ngày nhập liệu");
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
                row.add(StringUtils.isEmpty(item.getPatientPhone()) ? "" : item.getPatientPhone());
                row.add(StringUtils.isEmpty(item.getPermanentAddressFull()) ? "" : item.getPermanentAddressFull());
                row.add(StringUtils.isEmpty(item.getCurrentAddressFull()) ? "" : item.getCurrentAddressFull());
                row.add(StringUtils.isEmpty(item.getBloodBaseID()) ? "" : form.getOptions().get(ParameterEntity.BLOOD_BASE).get(item.getBloodBaseID()));
                row.add(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                row.add(StringUtils.isEmpty(item.getSiteConfirmID()) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));
                if (tab.equals(PacTabEnum.NEW_OUT_PROVINCE.getKey())) {
                    row.add(item.getAcceptPermanentTime() == null || "".equals(String.valueOf(item.getAcceptPermanentTime())) ? "Chưa chuyển" : "Đã chuyển");
                }
                row.add(StringUtils.isEmpty(item.getSourceServiceID()) ? "" : form.getOptions().get(ParameterEntity.SERVICE).get(item.getSourceServiceID()));
                row.add(item.getCreateAT() == null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));
                createTableRow(row.toArray(new Object[row.size()]));
            }
        }
    }
}

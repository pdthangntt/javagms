package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.OpcPatientForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Admin
 */
public class OpcPatientExcel extends BaseView implements IExportExcel {

    private OpcPatientForm form;
    private String extension;

    public OpcPatientExcel(OpcPatientForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "DSBN quan ly";
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
        String format = "dd/MM/yyyy";
        String startDate = StringUtils.isNotEmpty(form.getRegistrationTimeFrom()) ? form.getRegistrationTimeFrom() : form.getDataPage().getTotalRecords() > 0 ? form.getStartDate() : "";
        String endDate = StringUtils.isEmpty(form.getRegistrationTimeTo()) ? TextUtils.formatDate(new Date(), format) : form.getRegistrationTimeTo();
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(110));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(130));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(130));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(130));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));

        setMerge(1, 1, 0, form.isIsOpcManager() ? 17 : 16, false);
        setMerge(2, 2, 0, form.isIsOpcManager() ? 17 : 16, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        if (form.getDataPage().getTotalRecords() > 0 && StringUtils.isNotEmpty(startDate)) {
            createTitleDateRow("Ngày đăng ký từ " + startDate + " đến " + endDate);
        }
        if (form.getDataPage().getTotalRecords() == 0 && StringUtils.isNotEmpty(form.getRegistrationTimeFrom())) {
            createTitleDateRow("Ngày đăng ký từ " + form.getRegistrationTimeFrom() + " đến " + endDate);
        }

        //Tạo thêm dòng trắng
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        createFilterRow("Cơ sở điều trị", form.getSiteName(), cellIndexFilter);
        if (StringUtils.isNotEmpty(form.getProvinceName())) {
            createFilterRow("Tỉnh/TP thường trú", form.getProvinceName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getDistrictName())) {
            createFilterRow("Quận/huyện thường trú", form.getDistrictName(), cellIndexFilter);
        }

        if (StringUtils.isNotEmpty(form.getSearch().getStatusOfTreatmentID())) {
            createFilterRow("Trạng thái điều trị", form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(form.getSearch().getStatusOfTreatmentID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getTreatmentRegimenID())) {
            createFilterRow("Phác đồ hiện tại", form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(form.getSearch().getTreatmentRegimenID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getTreatmentTimeFrom()) || StringUtils.isNotEmpty(form.getTreatmentTimeTo())) {
            createFilterRow("Ngày điều trị ARV", String.format("%s  %s", (StringUtils.isNotEmpty(form.getTreatmentTimeFrom()) ? ("từ " + form.getTreatmentTimeFrom()) : ""), StringUtils.isNotEmpty(form.getTreatmentTimeTo()) ? ("đến " + form.getTreatmentTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getRegistrationType())) {
            createFilterRow("Loại đăng ký", form.getOptions().get(ParameterEntity.REGISTRATION_TYPE).get(form.getSearch().getRegistrationType()), cellIndexFilter);
        }

        createFilterRow("Ngày xuất Excel", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createFilterRow("Tổng số bệnh nhân được quản lý", "" + form.getDataPage().getTotalRecords(), cellIndexFilter);
    }

    private void createTable() throws Exception {
        String format = "dd/MM/yyyy";
        getCurrentSheet();
        //Dòng đầu tiên để trắng
        Row row = createRow();
        //Tiêu đề
        if (!form.isIsOpcManager()) {
            createTableHeaderRow("TT", "Mã bệnh án", "Họ và tên", "Ngày sinh",
                    "Giới tính", "Trạng thái điều trị",
                    "Ngày điều trị ARV đầu tiên", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Loại đăng ký", "Ngày XN TLVR gần nhất", "Ngày khám gần nhất",
                    "Ngày bắt đầu INH",
                    "Ngày BĐ Cotrimoxazole",
                    "Ngày BĐ điều trị Lao", "Ngày kết thúc", "Lý do kết thúc", "Ngày chuyển đi",
                    "Ngày XN khẳng định",
                    "Nơi XN khẳng định",
                    "Nhóm đối tượng",
                    "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú",
                    "Địa chỉ hiện tại",
                    "Tỉnh/Thành phố thường trú",
                    "Quận/Huyện thường trú",
                    "Phường/xã thường trú",
                    "Số nhà/Đường phố/Tổ/Ấp thường trú",
                    "Tỉnh/Thành phố tạm trú",
                    "Quận/Huyện tạm trú",
                    "Phường/xã tạm trú",
                    "Số nhà/Đường phố/Tổ/Ấp tạm trú");
        } else {
            createTableHeaderRow("TT", "Mã bệnh án", "Họ và tên", "Ngày sinh",
                    "Giới tính", "Trạng thái điều trị",
                    "Ngày điều trị ARV đầu tiên", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Loại đăng ký", "Ngày XN TLVR gần nhất", "Ngày khám gần nhất",
                    "Ngày bắt đầu INH",
                    "Ngày BĐ Cotrimoxazole",
                    "Ngày BĐ điều trị Lao", "Ngày kết thúc", "Lý do kết thúc", "Ngày chuyển đi",
                    "Ngày XN khẳng định",
                    "Nơi XN khẳng định",
                    "Nhóm đối tượng",
                    "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú",
                    "Địa chỉ hiện tại", "Cơ sở điều trị",
                    "Tỉnh/Thành phố thường trú",
                    "Quận/Huyện thường trú",
                    "Phường/xã thường trú",
                    "Số nhà/Đường phố/Tổ/Ấp thường trú",
                    "Tỉnh/Thành phố tạm trú",
                    "Quận/Huyện tạm trú",
                    "Phường/xã tạm trú",
                    "Số nhà/Đường phố/Tổ/Ấp tạm trú");
        }
        if (form.getDataPage().getTotalRecords() == 0) {
            createTableEmptyRow("Không tìm thấy thông tin", form.isIsOpcManager() ? 24 : 23);
            return;
        }
        int stt = 1;
        if (form.getItems() != null && form.getItems().size() > 0) {
            for (OpcArvEntity item : form.getItems()) {
                if (!form.isIsOpcManager()) {
                    createTableRow(stt++, item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                            StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                            StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                            item.getFirstTreatmentTime() == null ? "" : TextUtils.formatDate(item.getFirstTreatmentTime(), format),
                            item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                            StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                            StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                            item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                            StringUtils.isEmpty(item.getRegistrationType()) ? "" : form.getOptions().get(ParameterEntity.REGISTRATION_TYPE).get(item.getRegistrationType()),
                            item.getViralLoadTime() == null ? "" : TextUtils.formatDate(item.getViralLoadTime(), format),
                            item.getLastExaminationTime() == null ? "" : TextUtils.formatDate(item.getLastExaminationTime(), format),
                            item.getInhFromTime() == null ? "" : TextUtils.formatDate(item.getInhFromTime(), format),
                            item.getCotrimoxazoleFromTime() == null ? "" : TextUtils.formatDate(item.getCotrimoxazoleFromTime(), format),
                            item.getLaoStartTime() == null ? "" : TextUtils.formatDate(item.getLaoStartTime(), format),
                            item.getEndTime() == null ? "" : TextUtils.formatDate(item.getEndTime(), format),
                            StringUtils.isEmpty(item.getEndCase()) ? "" : form.getOptions().get(ParameterEntity.ARV_END_CASE).get(item.getEndCase()),
                            item.getTranferFromTime() == null ? "" : TextUtils.formatDate(item.getTranferFromTime(), format),
                            item.getPatient().getConfirmTime() == null ? "" : TextUtils.formatDate(item.getPatient().getConfirmTime(), format),
                            item.getPatient().getConfirmSiteID() == null ? "" : item.getPatient().getConfirmSiteID().equals(Long.valueOf("0")) ? "" : item.getPatient().getConfirmSiteID().equals(Long.valueOf("-1")) ? item.getPatient().getConfirmSiteName() : form.getOptions().get("siteConfirm").get(item.getPatient().getConfirmSiteID() + ""),
                            StringUtils.isEmpty(item.getObjectGroupID()) ? "" : form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(item.getObjectGroupID()),
                            item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(), StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(), item.getPermanentAddressFull(),
                            item.getCurrentAddressFull(),
                            form.getOptions().get("provinces").get(item.getPermanentProvinceID()),
                            form.getOptions().get("districts").get(item.getPermanentDistrictID()),
                            form.getOptions().get("wards").get(item.getPermanentWardID()),
                            StringUtils.isEmpty(item.getPermanentAddress()) ? "" : item.getPermanentAddress() + ", " + (StringUtils.isEmpty(item.getPermanentAddressStreet()) ? "" : item.getPermanentAddressStreet() + ", ") + (StringUtils.isEmpty(item.getPermanentAddressGroup()) ? "" : item.getPermanentAddressGroup() + ", "),
                            form.getOptions().get("provinces").get(item.getCurrentProvinceID()),
                            form.getOptions().get("districts").get(item.getCurrentDistrictID()),
                            form.getOptions().get("wards").get(item.getCurrentWardID()),
                            StringUtils.isEmpty(item.getCurrentAddress()) ? "" : item.getCurrentAddress() + ", " + (StringUtils.isEmpty(item.getCurrentAddressStreet()) ? "" : item.getCurrentAddressStreet() + ", ") + (StringUtils.isEmpty(item.getCurrentAddressGroup()) ? "" : item.getCurrentAddressGroup() + ", ")
                            );
                } else {
                    createTableRow(stt++, item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                            StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                            StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                            item.getFirstTreatmentTime() == null ? "" : TextUtils.formatDate(item.getFirstTreatmentTime(), format),
                            item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                            StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                            StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                            item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                            StringUtils.isEmpty(item.getRegistrationType()) ? "" : form.getOptions().get(ParameterEntity.REGISTRATION_TYPE).get(item.getRegistrationType()),
                            item.getViralLoadTime() == null ? "" : TextUtils.formatDate(item.getViralLoadTime(), format),
                            item.getLastExaminationTime() == null ? "" : TextUtils.formatDate(item.getLastExaminationTime(), format),
                            item.getInhFromTime() == null ? "" : TextUtils.formatDate(item.getInhFromTime(), format),
                            item.getCotrimoxazoleFromTime() == null ? "" : TextUtils.formatDate(item.getCotrimoxazoleFromTime(), format),
                            item.getLaoStartTime() == null ? "" : TextUtils.formatDate(item.getLaoStartTime(), format),
                            item.getEndTime() == null ? "" : TextUtils.formatDate(item.getEndTime(), format),
                            StringUtils.isEmpty(item.getEndCase()) ? "" : form.getOptions().get(ParameterEntity.ARV_END_CASE).get(item.getEndCase()),
                            item.getTranferFromTime() == null ? "" : TextUtils.formatDate(item.getTranferFromTime(), format),
                            item.getPatient().getConfirmTime() == null ? "" : TextUtils.formatDate(item.getPatient().getConfirmTime(), format),
                            item.getPatient().getConfirmSiteID() == null ? "" : item.getPatient().getConfirmSiteID().equals(Long.valueOf("0")) ? "" : item.getPatient().getConfirmSiteID().equals(Long.valueOf("-1")) ? item.getPatient().getConfirmSiteName() : form.getOptions().get("siteConfirm").get(item.getPatient().getConfirmSiteID() + ""),
                            StringUtils.isEmpty(item.getObjectGroupID()) ? "" : form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(item.getObjectGroupID()),
                            item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(), StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(), item.getPermanentAddressFull(),
                            item.getCurrentAddressFull(),
                            form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteID().toString()),
                            form.getOptions().get("provinces").get(item.getPermanentProvinceID()),
                            form.getOptions().get("districts").get(item.getPermanentDistrictID()),
                            form.getOptions().get("wards").get(item.getPermanentWardID()),
                            StringUtils.isEmpty(item.getPermanentAddress()) ? "" : item.getPermanentAddress() + ", " + (StringUtils.isEmpty(item.getPermanentAddressStreet()) ? "" : item.getPermanentAddressStreet() + ", ") + (StringUtils.isEmpty(item.getPermanentAddressGroup()) ? "" : item.getPermanentAddressGroup() + ", "),
                            form.getOptions().get("provinces").get(item.getCurrentProvinceID()),
                            form.getOptions().get("districts").get(item.getCurrentDistrictID()),
                            form.getOptions().get("wards").get(item.getCurrentWardID()),
                            StringUtils.isEmpty(item.getCurrentAddress()) ? "" : item.getCurrentAddress() + ", " + (StringUtils.isEmpty(item.getCurrentAddressStreet()) ? "" : item.getCurrentAddressStreet() + ", ") + (StringUtils.isEmpty(item.getCurrentAddressGroup()) ? "" : item.getCurrentAddressGroup() + ", ")
                            );
                }
            }
        }
    }
}

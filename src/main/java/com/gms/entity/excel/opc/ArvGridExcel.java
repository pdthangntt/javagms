/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author DSNAnh
 */
public class ArvGridExcel extends BaseView implements IExportExcel {

    private OpcPatientForm form;
    private String extension;

    public ArvGridExcel(OpcPatientForm form, String extension) {
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
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
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
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, form.isIsOpcManager() ? 17 : 16, false);

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
        if (StringUtils.isNotEmpty(form.getSearch().getCode())) {
            createFilterRow("Mã bệnh án", form.getSearch().getCode(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getName())) {
            createFilterRow("Họ và tên", form.getSearch().getName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getIdentityCard())) {
            createFilterRow("Số CMND", form.getSearch().getIdentityCard(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInsurance())) {
            createFilterRow("Tình trạng BHYT", form.getOptions().get(ParameterEntity.HAS_HEALTH_INSURANCE).get(form.getSearch().getInsurance()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInsuranceType())) {
            createFilterRow("Loại thẻ BHYT", form.getOptions().get(ParameterEntity.INSURANCE_TYPE).get(form.getSearch().getInsuranceType()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInsuranceNo())) {
            createFilterRow("Số thẻ BHYT", form.getSearch().getInsuranceNo(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInsuranceExpiry())) {
            createFilterRow("Thời gian hết hạn BHYT", form.getOptions().get("insuranceExpiryOptions").get(form.getSearch().getInsuranceExpiry()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getProvinceName())) {
            createFilterRow("Tỉnh/TP thường trú", form.getProvinceName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getDistrictName())) {
            createFilterRow("Quận/huyện thường trú", form.getDistrictName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getTreatmentStageTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getTreatmentStageTimeTo())) {
            createFilterRow("Ngày biến động", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getTreatmentStageTimeFrom()) ? ("từ " + form.getSearch().getTreatmentStageTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getTreatmentStageTimeTo()) ? ("đến " + form.getSearch().getTreatmentStageTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getRegistrationTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getRegistrationTimeTo())) {
            createFilterRow("Ngày đăng ký", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getRegistrationTimeFrom()) ? ("từ " + form.getSearch().getRegistrationTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getRegistrationTimeTo()) ? ("đến " + form.getSearch().getRegistrationTimeTo()) : ""), cellIndexFilter);
        }

        if (StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeTo())) {
            createFilterRow("Ngày điều trị ARV", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeFrom()) ? ("từ " + form.getSearch().getTreatmentTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeTo()) ? ("đến " + form.getSearch().getTreatmentTimeTo()) : ""), cellIndexFilter);
        }

        StringBuilder statusOfTreatment = new StringBuilder();
        statusOfTreatment.append("");
        if (form.getSearch().getStatusOfTreatmentIDs() != null && form.getSearch().getStatusOfTreatmentIDs().size() > 0) {
            for (String id : form.getSearch().getStatusOfTreatmentIDs()) {
                if (form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(id) != null) {
                    statusOfTreatment.append(form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(id));
                    statusOfTreatment.append(",");
                }
            }
        }
        if (StringUtils.isNotEmpty(form.getSearch().getStatusOfTreatmentID())) {
            createFilterRow("Trạng thái điều trị", StringUtils.isNotEmpty(statusOfTreatment.toString()) && statusOfTreatment.length() > 0 ? (statusOfTreatment.deleteCharAt(statusOfTreatment.length() - 1).toString()) : "", cellIndexFilter);
        }

        StringBuilder treatmentRegiment = new StringBuilder();
        treatmentRegiment.append("");
        if (form.getSearch().getTreatmentRegimenIDs() != null && form.getSearch().getTreatmentRegimenIDs().size() > 0) {
            for (String id : form.getSearch().getTreatmentRegimenIDs()) {
                if (form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(id) != null) {
                    treatmentRegiment.append(form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(id));
                    treatmentRegiment.append(",");
                }
            }
        }
        if (StringUtils.isNotEmpty(form.getSearch().getTreatmentRegimenID())) {
            createFilterRow("Phác đồ hiện tại", StringUtils.isNotEmpty(treatmentRegiment.toString()) && treatmentRegiment.length() > 0 ? (treatmentRegiment.deleteCharAt(treatmentRegiment.length() - 1).toString()) : "", cellIndexFilter);
        }

        StringBuilder treatmentStage = new StringBuilder();
        treatmentStage.append("");
        if (form.getSearch().getTreatmentStageIDs() != null && form.getSearch().getTreatmentStageIDs().size() > 0) {
            for (String id : form.getSearch().getTreatmentStageIDs()) {
                if (form.getOptions().get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT).get(id) != null) {
                    treatmentStage.append(form.getOptions().get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT).get(id));
                    treatmentStage.append(",");
                }
            }
        }
        if (StringUtils.isNotEmpty(form.getSearch().getTreatmentStageID())) {
            createFilterRow("Trạng thái biến động", StringUtils.isNotEmpty(treatmentStage.toString()) && treatmentStage.length() > 0 ? (treatmentStage.deleteCharAt(treatmentStage.length() - 1).toString()) : "", cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getDateOfArrivalFrom()) || StringUtils.isNotEmpty(form.getSearch().getDateOfArrivalTo())) {
            createFilterRow("Ngày chuyển gửi", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getDateOfArrivalFrom()) ? ("từ " + form.getSearch().getDateOfArrivalFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getDateOfArrivalTo()) ? ("đến " + form.getSearch().getDateOfArrivalTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getTranferToTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getTranferToTimeTo())) {
            createFilterRow("Ngày tiếp nhận", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getTranferToTimeFrom()) ? ("từ " + form.getSearch().getTranferToTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getTranferToTimeTo()) ? ("đến " + form.getSearch().getTranferToTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getTranferFromTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getTranferFromTimeTo())) {
            createFilterRow("Ngày chuyển đi", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getTranferFromTimeFrom()) ? ("từ " + form.getSearch().getTranferFromTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getTranferFromTimeTo()) ? ("đến " + form.getSearch().getTranferFromTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getGsph())) {
            createFilterRow("Trạng thái chuyển GSPH", form.getOptions().get("transferGSPH").get(form.getSearch().getGsph()), cellIndexFilter);
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

            if (StringUtils.isEmpty(form.getSearch().getTab())) {
                createTableHeaderRow("Mã bệnh án", "Họ và tên", "Ngày sinh", "Giới tính", "Trạng thái điều trị", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Ngày XN TLVR gần nhất", "Ngày khám gần nhất", "Trạng thái chuyển GSPH", "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú", "Địa chỉ hiện tại", "Ngày chuyển gửi", "Cơ sở chuyển gửi", "Ngày tiếp nhận", "Ngày phản hồi", "Ngày chuyển đi", "Cơ sở chuyển đi", "Ngày CSCĐ tiếp nhận", "Ngày CSCĐ phản hồi", "Trạng thái chuyển gửi", 
                        "Tỉnh/Thành phố thường trú",
                        "Quận/Huyện thường trú",
                        "Phường/xã thường trú",
                        "Tỉnh/Thành phố tạm trú",
                        "Quận/Huyện tạm trú",
                        "Phường/xã tạm trú");
            }
            if (form.getSearch().getTab().equals("bhyt")) {
                createTableHeaderRow("Mã bệnh án", "Họ và tên", "Ngày sinh", "Giới tính", "Trạng thái điều trị", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Số CMND", "Địa chỉ thường trú", "Địa chỉ hiện tại", "Tình trạng BHYT", "Số thẻ BHYT", "Loại thẻ BHYT", "Tỷ lệ thanh toán BHYT", "Ngày hết hạn thẻ BHYT");

            }
            if (form.getSearch().getTab().equals("stage")) {
                createTableHeaderRow("Mã bệnh án", "Họ và tên", "Ngày sinh", "Giới tính", "Trạng thái điều trị", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Trạng thái chuyển GSPH", "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú", "Địa chỉ hiện tại", "Trạng thái biến động", "Ngày biến động", "Trạng thái chuyển gửi");
            }
        } else {
            if (StringUtils.isEmpty(form.getSearch().getTab())) {
                createTableHeaderRow("Mã bệnh án", "Họ và tên", "Ngày sinh", "Giới tính", "Trạng thái điều trị", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Ngày XN TLVR gần nhất", "Ngày khám gần nhất", "Trạng thái chuyển GSPH", "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú", "Địa chỉ hiện tại", "Ngày chuyển gửi", "Cơ sở chuyển gửi", "Ngày tiếp nhận", "Ngày phản hồi", "Ngày chuyển đi", "Cơ sở chuyển đi", "Ngày CSCĐ tiếp nhận", "Ngày CSCĐ phản hồi", "Trạng thái chuyển gửi", "Cơ sở điều trị", "Quận/Huyện thường trú",
                        "Phường/xã thường trú",
                        "Tỉnh/Thành phố tạm trú",
                        "Quận/Huyện tạm trú",
                        "Phường/xã tạm trú");
            }
            if (form.getSearch().getTab().equals("bhyt")) {
                createTableHeaderRow("Mã bệnh án", "Họ và tên", "Ngày sinh", "Giới tính", "Trạng thái điều trị", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Số CMND", "Địa chỉ thường trú", "Địa chỉ hiện tại", "Tình trạng BHYT", "Số thẻ BHYT", "Loại thẻ BHYT", "Tỷ lệ thanh toán BHYT", "Ngày hết hạn thẻ BHYT", "Cơ sở điều trị");

            }
            if (form.getSearch().getTab().equals("stage")) {
                createTableHeaderRow("Mã bệnh án", "Họ và tên", "Ngày sinh", "Giới tính", "Trạng thái điều trị", "Ngày điều trị ARV", "Phác đồ hiện tại", "Bậc phác đồ hiện tại", "Ngày đăng ký", "Trạng thái chuyển GSPH", "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú", "Địa chỉ hiện tại", "Trạng thái biến động", "Ngày biến động", "Trạng thái chuyển gửi", "Cơ sở điều trị");
            }
        }
        if (form.getDataPage().getTotalRecords() == 0) {
            if (!form.isIsOpcManager()) {
                createTableEmptyRow("Không tìm thấy thông tin", StringUtils.isEmpty(form.getSearch().getTab()) ? 23 : 16);
            } else {
                createTableEmptyRow("Không tìm thấy thông tin", StringUtils.isEmpty(form.getSearch().getTab()) ? 24 : 17);
            }
            return;
        }
        if (form.getItems() != null && form.getItems().size() > 0) {
            for (OpcArvEntity item : form.getItems()) {
                if (!form.isIsOpcManager()) {
                    if (StringUtils.isEmpty(form.getSearch().getTab())) {
                        createTableRow(item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                                StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                                StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                                item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                                StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                                StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                                item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                                item.getViralLoadTime() == null ? "" : TextUtils.formatDate(item.getViralLoadTime(), format),
                                item.getLastExaminationTime() == null ? "" : TextUtils.formatDate(item.getLastExaminationTime(), format),
                                item.getTransferTimeGSPH() == null ? "Chưa chuyển" : "Đã chuyển",
                                item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(),
                                StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(),
                                item.getPermanentAddressFull(),
                                item.getCurrentAddressFull(),
                                item.getDateOfArrival() == null ? "" : TextUtils.formatDate(item.getDateOfArrival(), format),
                                item.getSourceSiteID() == null || item.getSourceSiteID() == 0 ? "" : item.getSourceSiteID() == -1 ? item.getSourceArvSiteName() : form.getOptions().get("siteOpcTo").get(String.valueOf(item.getSourceSiteID())),
                                item.getTranferToTime() == null ? "" : TextUtils.formatDate(item.getTranferToTime(), format),
                                item.getFeedbackResultsReceivedTime() == null ? "" : TextUtils.formatDate(item.getFeedbackResultsReceivedTime(), format),
                                item.getTranferFromTime() == null ? "" : TextUtils.formatDate(item.getTranferFromTime(), format),
                                item.getTransferSiteID() == null || item.getTransferSiteID() == 0 ? "" : item.getTransferSiteID() == -1 ? (StringUtils.isEmpty(item.getTransferSiteName()) ? "" : item.getTransferSiteName()) : (form.getOptions().get("siteOpcFrom").get(String.valueOf(item.getTransferSiteID())) == null ? "" : form.getOptions().get("siteOpcFrom").get(String.valueOf(item.getTransferSiteID()))),
                                item.getTranferToTimeOpc() == null ? "" : TextUtils.formatDate(item.getTranferToTimeOpc(), format),
                                item.getFeedbackResultsReceivedTimeOpc() == null ? "" : TextUtils.formatDate(item.getFeedbackResultsReceivedTimeOpc(), format),
                                (item.getTranferFromTime() != null && item.getTranferToTimeOpc() == null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã chuyển gửi" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã tiếp nhận" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() != null) ? "Chuyển gửi thành công" : "", form.getOptions().get("provinces").get(item.getPermanentProvinceID()),
                                form.getOptions().get("districts").get(item.getPermanentDistrictID()),
                                form.getOptions().get("wards").get(item.getPermanentWardID()),
                                form.getOptions().get("provinces").get(item.getCurrentProvinceID()),
                                form.getOptions().get("districts").get(item.getCurrentDistrictID()),
                                form.getOptions().get("wards").get(item.getCurrentWardID()));
                    }
                    if (form.getSearch().getTab().equals("bhyt")) {
                        createTableRow(item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                                StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                                StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                                item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                                StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                                StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                                item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                                item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(),
                                item.getPermanentAddressFull(),
                                item.getCurrentAddressFull(),
                                StringUtils.isEmpty(item.getInsurance()) ? "" : form.getOptions().get(ParameterEntity.HAS_HEALTH_INSURANCE).get(item.getInsurance()),
                                StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(),
                                StringUtils.isEmpty(item.getInsuranceType()) ? "" : form.getOptions().get(ParameterEntity.INSURANCE_TYPE).get(item.getInsuranceType()),
                                StringUtils.isEmpty(item.getInsurancePay()) ? "" : form.getOptions().get(ParameterEntity.INSURANCE_PAY).get(item.getInsurancePay()),
                                item.getInsuranceExpiry() == null ? "" : TextUtils.formatDate(item.getInsuranceExpiry(), format));
                    }
                    if (form.getSearch().getTab().equals("stage")) {
                        createTableRow(item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                                StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                                StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                                item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                                StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                                StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                                item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                                item.getTransferTimeGSPH() == null ? "Chưa chuyển" : "Đã chuyển",
                                item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(),
                                StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(),
                                item.getPermanentAddressFull(),
                                item.getCurrentAddressFull(),
                                StringUtils.isEmpty(item.getTreatmentStageID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT).get(item.getTreatmentStageID()),
                                item.getTreatmentStageTime() == null ? "" : TextUtils.formatDate(item.getTreatmentStageTime(), format),
                                (item.getTranferFromTime() != null && item.getTranferToTimeOpc() == null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã chuyển gửi" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã tiếp nhận" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() != null) ? "Chuyển gửi thành công" : ""
                        );
                    }
                } else {
                    if (StringUtils.isEmpty(form.getSearch().getTab())) {
                        createTableRow(item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                                StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                                StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                                item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                                StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                                StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                                item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                                item.getViralLoadTime() == null ? "" : TextUtils.formatDate(item.getViralLoadTime(), format),
                                item.getLastExaminationTime() == null ? "" : TextUtils.formatDate(item.getLastExaminationTime(), format),
                                item.getTransferTimeGSPH() == null ? "Chưa chuyển" : "Đã chuyển",
                                item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(),
                                StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(),
                                item.getPermanentAddressFull(),
                                item.getCurrentAddressFull(),
                                item.getDateOfArrival() == null ? "" : TextUtils.formatDate(item.getDateOfArrival(), format),
                                item.getSourceSiteID() == null || item.getSourceSiteID() == 0 ? "" : item.getSourceSiteID() == -1 ? item.getSourceArvSiteName() : form.getOptions().get("siteOpcTo").get(String.valueOf(item.getSourceSiteID())),
                                item.getTranferToTime() == null ? "" : TextUtils.formatDate(item.getTranferToTime(), format),
                                item.getFeedbackResultsReceivedTime() == null ? "" : TextUtils.formatDate(item.getFeedbackResultsReceivedTime(), format),
                                item.getTranferFromTime() == null ? "" : TextUtils.formatDate(item.getTranferFromTime(), format),
                                item.getTransferSiteID() == null || item.getTransferSiteID() == 0 ? "" : item.getTransferSiteID() == -1 ? (StringUtils.isEmpty(item.getTransferSiteName()) ? "" : item.getTransferSiteName()) : (form.getOptions().get("siteOpcFrom").get(String.valueOf(item.getTransferSiteID())) == null ? "" : form.getOptions().get("siteOpcFrom").get(String.valueOf(item.getTransferSiteID()))),
                                item.getTranferToTimeOpc() == null ? "" : TextUtils.formatDate(item.getTranferToTimeOpc(), format),
                                item.getFeedbackResultsReceivedTimeOpc() == null ? "" : TextUtils.formatDate(item.getFeedbackResultsReceivedTimeOpc(), format),
                                (item.getTranferFromTime() != null && item.getTranferToTimeOpc() == null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã chuyển gửi" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã tiếp nhận" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() != null) ? "Chuyển gửi thành công" : "",
                                form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteID().toString()), form.getOptions().get("provinces").get(item.getPermanentProvinceID()),
                                form.getOptions().get("districts").get(item.getPermanentDistrictID()),
                                form.getOptions().get("wards").get(item.getPermanentWardID()),
                                form.getOptions().get("provinces").get(item.getCurrentProvinceID()),
                                form.getOptions().get("districts").get(item.getCurrentDistrictID()),
                                form.getOptions().get("wards").get(item.getCurrentWardID()));
                    }
                    if (form.getSearch().getTab().equals("bhyt")) {
                        createTableRow(item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                                StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                                StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                                item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                                StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                                StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                                item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                                item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(),
                                item.getPermanentAddressFull(),
                                item.getCurrentAddressFull(),
                                StringUtils.isEmpty(item.getInsurance()) ? "" : form.getOptions().get(ParameterEntity.HAS_HEALTH_INSURANCE).get(item.getInsurance()),
                                StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(),
                                StringUtils.isEmpty(item.getInsuranceType()) ? "" : form.getOptions().get(ParameterEntity.INSURANCE_TYPE).get(item.getInsuranceType()),
                                StringUtils.isEmpty(item.getInsurancePay()) ? "" : form.getOptions().get(ParameterEntity.INSURANCE_PAY).get(item.getInsurancePay()),
                                item.getInsuranceExpiry() == null ? "" : TextUtils.formatDate(item.getInsuranceExpiry(), format),
                                form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteID().toString()));
                    }
                    if (form.getSearch().getTab().equals("stage")) {
                        createTableRow(item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                                StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                                StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                                item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
                                StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()),
                                StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()),
                                item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                                item.getTransferTimeGSPH() == null ? "Chưa chuyển" : "Đã chuyển",
                                item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(),
                                StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(),
                                item.getPermanentAddressFull(),
                                item.getCurrentAddressFull(),
                                StringUtils.isEmpty(item.getTreatmentStageID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT).get(item.getTreatmentStageID()),
                                item.getTreatmentStageTime() == null ? "" : TextUtils.formatDate(item.getTreatmentStageTime(), format),
                                (item.getTranferFromTime() != null && item.getTranferToTimeOpc() == null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã chuyển gửi" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() == null) ? "Đã tiếp nhận" : (item.getTranferFromTime() != null && item.getTranferToTimeOpc() != null && item.getFeedbackResultsReceivedTimeOpc() != null) ? "Chuyển gửi thành công" : "",
                                form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteID().toString())
                        );
                    }
                }
            }
        }
    }
}

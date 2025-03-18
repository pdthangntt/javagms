package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.OpcTreatmentFluctuationsForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author DSNAnh
 */
public class TreatmentFluctuationsExcel extends BaseView implements IExportExcel {

    private OpcTreatmentFluctuationsForm form;
    private String extension;

    public TreatmentFluctuationsExcel(OpcTreatmentFluctuationsForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "DS bien dong dieu tri";
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
        String startDate = StringUtils.isNotEmpty(form.getTreatmentStageTimeFrom()) ? form.getTreatmentStageTimeFrom() : form.getDataPage().getTotalRecords() > 0 ? form.getStartDate() : "";
        String endDate = StringUtils.isEmpty(form.getTreatmentStageTimeTo()) ? TextUtils.formatDate(new Date(), format) : form.getTreatmentStageTimeTo();
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(100));
        int ix = 16;
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(ix++, PixelUtils.pixel2WidthUnits(200));

        setMerge(1, 1, 0, form.isIsOpcManager() ? 15 : 14, false);
        setMerge(2, 2, 0, form.isIsOpcManager() ? 15 : 14, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        if (form.getDataPage().getTotalRecords() > 0 && StringUtils.isNotEmpty(startDate)) {
            createTitleDateRow("Từ " + startDate + " đến " + endDate);
        }
        if (form.getDataPage().getTotalRecords() == 0 && StringUtils.isNotEmpty(form.getTreatmentStageTimeFrom())) {
            createTitleDateRow("Từ " + form.getTreatmentStageTimeFrom() + " đến " + endDate);
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
            createFilterRow("Quận/Huyện thường trú", form.getDistrictName(), cellIndexFilter);
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
        createFilterRow("Ngày xuất Excel", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createFilterRow("Tổng số bệnh nhân biến động", "" + form.getDataPage().getTotalRecords(), cellIndexFilter);
    }

    private void createTable() throws Exception {
        String format = "dd/MM/yyyy";
        getCurrentSheet();
        //Dòng đầu tiên để trắng
        Row row = createRow();
        //Tiêu đề
        if (!form.isIsOpcManager()) {
            createTableHeaderRow("STT", "Mã bệnh án", "Họ và tên", "Ngày sinh",
                    "Giới tính", "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú",
                    "Địa chỉ hiện tại", "Trạng thái biến động", "Ngày biến động", "Ngày đăng ký", "Trạng thái điều trị", "Ngày điều trị ARV",
                    "Tỉnh/Thành phố thường trú",
                    "Quận/Huyện thường trú",
                    "Phường/xã thường trú",
                    "Số nhà/Đường phố/Tổ/Ấp thường trú",
                    "Tỉnh/Thành phố tạm trú",
                    "Quận/Huyện tạm trú",
                    "Phường/xã tạm trú",
                    "Số nhà/Đường phố/Tổ/Ấp tạm trú");
        } else {
            createTableHeaderRow("STT", "Mã bệnh án", "Họ và tên", "Ngày sinh",
                    "Giới tính", "Số CMND", "Số thẻ BHYT", "Địa chỉ thường trú",
                    "Địa chỉ hiện tại", "Trạng thái biến động", "Ngày biến động", "Ngày đăng ký", "Trạng thái điều trị", "Ngày điều trị ARV", "Cơ sở điều trị",
                    "Tỉnh/Thành phố thường trú",
                    "Quận/Huyện thường trú",
                    "Phường/xã thường trú",
                    "Số nhà/Đường phố/Tổ/Ấp thường trú",
                    "Tỉnh/Thành phố tạm trú",
                    "Quận/Huyện tạm trú",
                    "Phường/xã tạm trú",
                    "Số nhà/Đường phố/Tổ/Ấp tạm trú");
        }
        if (form.getItems() == null || form.getItems().isEmpty()) {
            createTableEmptyRow("Không tìm thấy thông tin", form.isIsOpcManager() ? 15 : 14);
            return;
        }
        int stt = 1;
        if (form.getItems() != null && form.getItems().size() > 0) {
            for (OpcArvEntity item : form.getItems()) {
                if (!form.isIsOpcManager()) {
                    createTableRow(stt++, item.getCode(), item.getPatient().getFullName(), TextUtils.formatDate(item.getPatient().getDob(), format),
                            StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()),
                            item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(), StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(), item.getPermanentAddressFull(),
                            item.getCurrentAddressFull(),
                            StringUtils.isEmpty(item.getTreatmentStageID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT).get(item.getTreatmentStageID()),
                            item.getTreatmentStageTime() == null ? "" : TextUtils.formatDate(item.getTreatmentStageTime(), format),
                            item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                            StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                            item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
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
                            item.getPatient().getIdentityCard() == null ? "" : item.getPatient().getIdentityCard(), StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo(), item.getPermanentAddressFull(),
                            item.getCurrentAddressFull(),
                            StringUtils.isEmpty(item.getTreatmentStageID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT).get(item.getTreatmentStageID()),
                            item.getTreatmentStageTime() == null ? "" : TextUtils.formatDate(item.getTreatmentStageTime(), format),
                            item.getRegistrationTime() == null ? "" : TextUtils.formatDate(item.getRegistrationTime(), format),
                            StringUtils.isEmpty(item.getStatusOfTreatmentID()) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()),
                            item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format),
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

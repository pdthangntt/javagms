package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.controller.backend.OpcVisitController;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.OpcVisitForm;
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
public class OpcVisitExcel extends BaseView implements IExportExcel {

    private OpcVisitForm form;
    private String extension;

    public OpcVisitExcel(OpcVisitForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Danh sach tai kham";
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
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
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
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(250));

        //Dòng đầu tiên để trắng
        setMerge(1, 1, 0, 8, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());

        int cellIndexFilter = 2;

        if (StringUtils.isNotEmpty(form.getSearch().getCode())) {
            createFilterRow("Mã bệnh án ", form.getSearch().getCode(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getName())) {
            createFilterRow("Họ tên ", form.getSearch().getName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getIdentityCard())) {
            createFilterRow("Số CMND ", form.getSearch().getIdentityCard(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getInsuranceNo())) {
            createFilterRow("Số thẻ BHYT ", form.getSearch().getInsuranceNo(), cellIndexFilter);
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
        headers.add("Mã bệnh án");
        headers.add("Họ và tên");
        headers.add("Ngày sinh");
        headers.add("Giới tính");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Ngày hết hạn BHYT");
        headers.add("Tỷ lệ thanh toán BHYT");
        headers.add("Tuyến ĐK bảo hiểm");
        headers.add("Ngày khám gần nhất");
        headers.add("Ngày hẹn khám");
        if (!form.getSearch().getTab().equals("0")) {
            headers.add("Số ngày trễ hẹn");
        }
        headers.add("Ngày điều trị ARV");
        headers.add("Phác đồ hiện tại");
        headers.add("Địa chỉ thường trú");
        headers.add("Địa chỉ hiện tại");
        if (form.getSearch().isIsOpcManage()) {
            headers.add("Cơ sở điều trị");
        }

        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        // loop to create row content
        if (form.getItems().size() > 0) {
            List<Object> row;
            int index = 0;
            for (OpcArvEntity item : form.getItems()) {
                row = new ArrayList<>();
                // Số thứ tự
                row.add(index += 1);

                row.add(item.getCode());
                row.add(item.getPatient() != null ? item.getPatient().getFullName() : "");
                row.add(item.getPatient() != null ? TextUtils.formatDate(item.getPatient().getDob(), "dd/MM/yyyy") : "");
                row.add(StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()));
                row.add(StringUtils.isEmpty(item.getPatient().getIdentityCard()) ? "" : item.getPatient().getIdentityCard());
                row.add(StringUtils.isEmpty(item.getInsuranceNo()) ? "" : item.getInsuranceNo());
                row.add(item.getInsuranceExpiry() != null ? TextUtils.formatDate(item.getInsuranceExpiry(), "dd/MM/yyyy") : "");
                row.add(StringUtils.isEmpty(item.getInsurancePay()) ? "" : form.getOptions().get(ParameterEntity.INSURANCE_PAY).get(item.getInsurancePay()));
                row.add(StringUtils.isEmpty(item.getRouteID()) ? "" : form.getOptions().get(ParameterEntity.ROUTE).get(item.getRouteID()));
                row.add(item.getLastExaminationTime() != null ? TextUtils.formatDate(item.getLastExaminationTime(), "dd/MM/yyyy") : "");
                row.add(item.getAppointmentTime() != null ? TextUtils.formatDate(item.getAppointmentTime(), "dd/MM/yyyy") : "");
                if (!form.getSearch().getTab().equals("0")) {
                    row.add(form.subtractDate(item.getAppointmentTime()) < 1 ? "" : form.subtractDate(item.getAppointmentTime()));
                }
                row.add(item.getTreatmentTime() != null ? TextUtils.formatDate(item.getTreatmentTime(), "dd/MM/yyyy") : "");
                row.add(StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()));
                row.add(item.getPermanentAddressFull());
                row.add(item.getCurrentAddressFull());
                if (form.getSearch().isIsOpcManage()) {
                    row.add(form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteID() + ""));
                }

                createTableRow(row.toArray(new Object[row.size()]));

            }
        }
    }

}

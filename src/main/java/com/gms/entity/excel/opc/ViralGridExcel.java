/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.ViralGridForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author DSNAnh
 */
public class ViralGridExcel extends BaseView implements IExportExcel  {
    private ViralGridForm form;
    private String extension;

    public ViralGridExcel(ViralGridForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "QLTLVR";
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
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(300));
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
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));

        setMerge(1, 1, 0, 15, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        
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
        if (StringUtils.isNotEmpty(form.getSearch().getInsuranceNo())) {
            createFilterRow("Số thẻ BHYT", form.getSearch().getInsuranceNo(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getProvinceName())) {
            createFilterRow("Tỉnh/TP thường trú", form.getProvinceName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getDistrictName())) {
            createFilterRow("Quận/huyện thường trú", form.getDistrictName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeFrom()) || StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeTo())) {
            createFilterRow("Ngày điều trị ARV", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeFrom()) ? ("từ " + form.getSearch().getTreatmentTimeFrom()) : ""), StringUtils.isNotEmpty(form.getSearch().getTreatmentTimeTo()) ? ("đến " + form.getSearch().getTreatmentTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getViralTimeForm()) || StringUtils.isNotEmpty(form.getSearch().getViralTimeTo())) {
            createFilterRow("Ngày xét nghiệm", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getViralTimeForm()) ? ("từ " + form.getSearch().getViralTimeForm()) : ""), StringUtils.isNotEmpty(form.getSearch().getViralTimeTo()) ? ("đến " + form.getSearch().getViralTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getViralRetryForm()) || StringUtils.isNotEmpty(form.getSearch().getViralRetryTo())) {
            createFilterRow("Ngày hẹn xét nghiệm lại", String.format("%s  %s", (StringUtils.isNotEmpty(form.getSearch().getViralRetryForm()) ? ("từ " + form.getSearch().getViralRetryForm()) : ""), StringUtils.isNotEmpty(form.getSearch().getViralRetryTo()) ? ("đến " + form.getSearch().getViralRetryTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSearch().getResultID())) {
            createFilterRow("Kết quả xét nghiệm", form.getOptions().get(ParameterEntity.VIRUS_LOAD).get(form.getSearch().getResultID()), cellIndexFilter);
        }
        createFilterRow("Ngày xuất Excel", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createFilterRow("Tổng số", "" + form.getDataPage().getTotalRecords(), cellIndexFilter);
    }

    private void createTable() throws Exception {
        String format = "dd/MM/yyyy";
        getCurrentSheet();
        //Dòng đầu tiên để trắng
        Row row = createRow();
        List<String> headers = new ArrayList<>();
        //Tiêu đề
        if(form.getSearch().getTab().equals("0")){
            headers.add("Mã bệnh án");
            headers.add("Họ và tên");
            headers.add("Ngày sinh");
            headers.add("Giới tính");
            headers.add("Số CMND");
            headers.add("Số thẻ BHYT");
            headers.add("Địa chỉ thường trú");
            headers.add("Địa chỉ hiện tại");
            headers.add("Ngày xét nghiệm");
            headers.add("Lý do xét nghiệm");
            headers.add("Kết quả xét nghiệm");
            headers.add("Ngày hẹn xét nghiệm");
            headers.add("Số lượt XNTLVR");
            headers.add("Ngày điều trị ARV");
            headers.add("Phác đồ hiện tại");
            headers.add("Bậc phác đồ hiện tại");
            if (form.isIsOpcManager()) {
                headers.add("Cơ sở điều trị");
            }
            createTableHeaderRow(headers.toArray(new String[headers.size()]));
        } else {
            headers.add("Mã bệnh án");
            headers.add("Họ và tên");
            headers.add("Ngày sinh");
            headers.add("Giới tính");
            headers.add("Số CMND");
            headers.add("Số thẻ BHYT");
            headers.add("Địa chỉ thường trú");
            headers.add("Địa chỉ hiện tại");
            headers.add("Ngày xét nghiệm");
            headers.add("Lý do xét nghiệm");
            headers.add("Ngày hẹn xét nghiệm");
            headers.add("Ngày điều trị ARV");
            headers.add("Phác đồ hiện tại");
            headers.add("Bậc phác đồ hiện tại");
            if (form.isIsOpcManager()) {
                headers.add("Cơ sở điều trị");
            }
            createTableHeaderRow(headers.toArray(new String[headers.size()]));
        }
        if (form.getDataPage().getTotalRecords() == 0) {
            createTableEmptyRow("Không tìm thấy thông tin",form.getSearch().getTab().equals("0")? 16 : 14);
            return;
        }
        if (form.getItems() != null && form.getItems().size() > 0) {
            List<Object> rowTbl;
            for (OpcArvEntity item : form.getItems()) {
                rowTbl = new ArrayList<>();
                StringBuilder causeOfDeath = new StringBuilder();
                causeOfDeath.append("");
                if(item.getViralLoadCauses() != null && item.getViralLoadCauses().size() > 0){
                    for(String id : item.getViralLoadCauses()){
                        if(form.getOptions().get(ParameterEntity.VIRAL_LOAD_SYMTOM).get(id) != null){
                            causeOfDeath.append(form.getOptions().get(ParameterEntity.VIRAL_LOAD_SYMTOM).get(id));
                            causeOfDeath.append(",");
                        }
                    }
                }
                if(form.getSearch().getTab().equals("0")){
                    
                    rowTbl.add(item.getCode());
                    rowTbl.add(item.getPatient().getFullName());
                    rowTbl.add(TextUtils.formatDate(item.getPatient().getDob(), format));
                    rowTbl.add(StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()));
                    rowTbl.add(StringUtils.isNotEmpty(item.getPatient().getIdentityCard()) ? item.getPatient().getIdentityCard() : "");
                    rowTbl.add(StringUtils.isEmpty(item.getInsuranceNo()) || item.getInsuranceNo().equals("null") ? "" : item.getInsuranceNo());
                    rowTbl.add(item.getPermanentAddressFull());
                    rowTbl.add(item.getCurrentAddressFull());
                    rowTbl.add(item.getViralLoadTime() == null ? "" : TextUtils.formatDate(item.getViralLoadTime(), format));
                    rowTbl.add(StringUtils.isNotEmpty(causeOfDeath.toString()) && causeOfDeath.length() > 0 ? (causeOfDeath.deleteCharAt(causeOfDeath.length() - 1).toString()) : "");
                    rowTbl.add((StringUtils.isEmpty(item.getViralLoadResult()) ? "" : form.getOptions().get(ParameterEntity.VIRUS_LOAD).get(item.getViralLoadResult())));
                    rowTbl.add(item.getViralLoadRetryTime() == null ? "" : TextUtils.formatDate(item.getViralLoadRetryTime(), format));
                    rowTbl.add(form.getCountTimeVirals().get(String.valueOf(item.getID())));
                    rowTbl.add(item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format));
                    rowTbl.add(StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()));
                    rowTbl.add(StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()));
                    
                    if(form.isIsOpcManager()){
                        rowTbl.add(form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteID() + ""));
                    }
                } else {
                    rowTbl.add(item.getCode());
                    rowTbl.add(item.getPatient().getFullName());
                    rowTbl.add(TextUtils.formatDate(item.getPatient().getDob(), format));
                    rowTbl.add(StringUtils.isEmpty(item.getPatient().getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getPatient().getGenderID()));
                    rowTbl.add(StringUtils.isNotEmpty(item.getPatient().getIdentityCard()) ? item.getPatient().getIdentityCard() : "");
                    rowTbl.add(StringUtils.isNotEmpty(item.getInsuranceNo()) ? item.getInsuranceNo() : "" );
                    rowTbl.add(item.getPermanentAddressFull());
                    rowTbl.add(item.getCurrentAddressFull());
                    rowTbl.add(item.getViralLoadTime() == null ? "" : TextUtils.formatDate(item.getViralLoadTime(), format));
                    rowTbl.add(StringUtils.isNotEmpty(causeOfDeath.toString()) && causeOfDeath.length() > 0 ? causeOfDeath.deleteCharAt(causeOfDeath.length() - 1).toString() : "");
                    rowTbl.add(item.getViralLoadRetryTime() == null ? "" : TextUtils.formatDate(item.getViralLoadRetryTime(), format));
                    rowTbl.add(item.getTreatmentTime() == null ? "" : TextUtils.formatDate(item.getTreatmentTime(), format));
                    rowTbl.add(StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()));
                    rowTbl.add(StringUtils.isEmpty(item.getTreatmentRegimenStage()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGINMEN_STAGE).get(item.getTreatmentRegimenStage()));
                    if(form.isIsOpcManager()){
                        rowTbl.add(form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteID() + ""));
                    }
                }
                createTableRow(rowTbl.toArray(new Object[rowTbl.size()]));
            }
        }
    }
}

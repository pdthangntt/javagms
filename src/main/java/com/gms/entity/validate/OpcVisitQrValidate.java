/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.form.opc_arv.OpcVisitForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author Admin
 */
@Component
public class OpcVisitQrValidate implements Validator  {
    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcVisitForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OpcVisitForm form = (OpcVisitForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        
        if(StringUtils.isNotEmpty(form.getCausesChange()) && form.getCausesChange().length() > 255 && 
                ((StringUtils.isNotEmpty(form.getTreatmentRegimenID()) && StringUtils.isNotEmpty(form.getBaseTreatmentRegimenID()) && !form.getTreatmentRegimenID().equals(form.getBaseTreatmentRegimenID())) || 
                (StringUtils.isNotEmpty(form.getTreatmentRegimenStage()) && StringUtils.isNotEmpty(form.getBaseTreatmentRegimenStage()) && !form.getTreatmentRegimenStage().equals(form.getBaseTreatmentRegimenStage())))){
            validateLength(form.getCausesChange(), 255, "causesChange", "Lý do thay đổi bậc phác đồ/phác đồ không được quá 255 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getPatientWeight()) && form.getPatientWeight().length() > 5){
            validateLength(form.getPatientWeight().trim(), 5, "patientWeight", "Cân nặng không được quá 5 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getPatientHeight()) && form.getPatientHeight().length() > 3){
            validateLength(form.getPatientHeight().trim(), 3, "patientHeight", "Chiều cao không được quá 3 ký tự", errors);
        }
        
        if(errors.getFieldError("msg") == null){
            validateLength(form.getMsg(), 255, "msg", "Lời dặn đơn thuốc không được quá 255 ký tự", errors);
        }
        
        if(errors.getFieldError("diagnostic") == null){
            validateLength(form.getDiagnostic(), 500, "diagnostic", "Chẩn đoán không được quá 500 ký tự", errors);
        }
        
        if(errors.getFieldError("clinical") == null){
            validateLength(form.getClinical(), 220, "clinical", "Lâm sàng không được quá 220 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getCircuit()) && errors.getFieldError("circuit") == null){
            if(form.getCircuit().trim().length() > 3){
                errors.rejectValue("circuit", "circuit", "Mạch không được quá 3 ký tự");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getBodyTemperature()) && errors.getFieldError("bodyTemperature") == null){
            if(form.getBodyTemperature().trim().length() > 5){
                errors.rejectValue("bodyTemperature", "bodyTemperature", "Thân nhiệt không được quá 5 ký tự");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getBloodPressure()) && errors.getFieldError("bloodPressure") == null){
            if(form.getBloodPressure().trim().length() > 8){
                errors.rejectValue("bloodPressure", "bloodPressure", "Huyết áp không được quá 8 ký tự");
            }
        }
        
        
        if(StringUtils.isNotEmpty(form.getPatientWeight()) && errors.getFieldError("patientWeight") == null){
            try{
                if(Math.signum(Double.parseDouble(form.getPatientWeight())) == -1.0){
                    errors.rejectValue("patientWeight", "patientWeight", "Cân nặng phải là số dương");
                }
            } catch (NumberFormatException ex){
                errors.rejectValue("patientWeight", "patientWeight", "Cân nặng không hợp lệ");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getPatientHeight()) && errors.getFieldError("patientHeight") == null){
            try{
                if(Math.signum(Double.parseDouble(form.getPatientHeight())) == -1.0){
                    errors.rejectValue("patientHeight", "patientHeight", "Chiều cao phải là số dương");
                }
            } catch (NumberFormatException ex){
                errors.rejectValue("patientHeight", "patientHeight", "Chiều cao không hợp lệ");
            }
        }
        
        if (StringUtils.isEmpty(form.getStageID())) {
            errors.rejectValue("stageID", "stageID", "Giai đoạn điều trị không được để trống");
        }
        
        if (StringUtils.isEmpty(form.getInsurance())) {
            errors.rejectValue("insurance", "insurance", "BN có thẻ BHYT không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getDaysReceived())&& form.getDaysReceived().trim().length() > 0 && errors.getFieldError("daysReceived") == null) {
            try{
                Integer.parseInt(form.getDaysReceived());
            } catch (NumberFormatException ex){
                errors.rejectValue("daysReceived", "daysReceived", "Số ngày nhận thuốc không hợp lệ");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getNote()) && errors.getFieldError("note") == null){
            validateLength(form.getNote(), 500, "note", "Ghi chú không quá 500 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getInsuranceSite()) && errors.getFieldError("insuranceSite") == null){
            validateLength(form.getInsuranceSite(), 255, "insuranceSite", "Nơi ĐK KCB ban đầu không quá 255 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getInsuranceNo()) && errors.getFieldError("insuranceNo") == null){
            validateLength(form.getInsuranceNo(), 50, "insuranceNo", "Số thẻ BHYT không quá 50 ký tự", errors);
        }
        
        try {
            Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
            checkDateFormat(form.getInsuranceExpiry(), "insuranceExpiry", "Ngày hết hạn thẻ BHYT không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getAppointmentTime(), "appointmentTime", "Ngày hẹn khám không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getSupplyMedicinceDate(), "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getRegimenStageDate(), "regimenStageDate", "Ngày thay đổi bậc phác đồ không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getRegimenDate(), "regimenDate", "Ngày thay đổi phác đồ không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getPregnantEndDate(), "pregnantEndDate", "Ngày kết thúc thai kỳ không hợp lệ", errors, sdfrmt);
            if (StringUtils.isNotEmpty(form.getAppointmentTime()) && checkDateFormat(form.getAppointmentTime(), "appointmentTime", "Ngày hẹn khám không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám không hợp lệ", errors, sdfrmt)) {
                Date appointmentTime = sdfrmt.parse(form.getAppointmentTime());
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                if (appointmentTime.compareTo(lastExaminationTime) < 0 && errors.getFieldError("appointmentTime") == null) {
                    errors.rejectValue("appointmentTime", "appointmentTime", "Ngày hẹn khám phải lớn hơn ngày khám");
                }
            }
            if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt)) {
                Date pregnantStartDate = sdfrmt.parse(form.getPregnantStartDate());
                if (now.compareTo(pregnantStartDate) < 0 && errors.getFieldError("pregnantStartDate") == null) {
                    errors.rejectValue("pregnantStartDate", "pregnantStartDate", "Ngày bắt đầu thai kỳ không được lớn hơn ngày hiện tại");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getPregnantEndDate()) && checkDateFormat(form.getPregnantEndDate(), "pregnantEndDate", "Ngày kết thúc thai kỳ không hợp lệ", errors, sdfrmt)) {
                Date pregnantStartDate = sdfrmt.parse(form.getPregnantStartDate());
                Date pregnantEndDate = sdfrmt.parse(form.getPregnantEndDate());
                if (pregnantStartDate.compareTo(pregnantEndDate) > 0 && errors.getFieldError("pregnantEndDate") == null) {
                    errors.rejectValue("pregnantEndDate", "pregnantEndDate", "Ngày kết thúc thai kỳ không được nhỏ hơn ngày bắt đầu thai kỳ");
                }
            }
            
//            if (StringUtils.isNotEmpty(form.getRegimenDate()) && checkDateFormat(form.getRegimenDate(), "regimenDate", "Ngày thay đổi phác đồ không hợp lệ", errors, sdfrmt) && 
//                    StringUtils.isNotEmpty(form.getTreatmentTime())) {
//                Date regimenDate = sdfrmt.parse(form.getRegimenDate());
//                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
//                if (treatmentTime.compareTo(regimenDate) < 0 && errors.getFieldError("regimenDate") == null) {
//                    errors.rejectValue("regimenDate", "regimenDate", "Ngày thay đổi phác đồ không được lớn hơn ngày điều trị ARV (" + form.getTreatmentTime() + ")");
//                }
//            }
            
            if(StringUtils.isNotEmpty(form.getBaseTreatmentRegimenStage()) && StringUtils.isNotEmpty(form.getTreatmentRegimenStage()) && 
                    !form.getBaseTreatmentRegimenStage().equals(form.getTreatmentRegimenStage()) && StringUtils.isEmpty(form.getRegimenStageDate())){
                errors.rejectValue("regimenStageDate", "regimenStageDate", "Ngày thay đổi bậc phác đồ không được để trống");
            }
            
            if(StringUtils.isNotEmpty(form.getBaseTreatmentRegimenID()) && StringUtils.isNotEmpty(form.getTreatmentRegimenID()) && 
                    !form.getBaseTreatmentRegimenID().equals(form.getTreatmentRegimenID()) && StringUtils.isEmpty(form.getRegimenDate())){
                errors.rejectValue("regimenDate", "regimenDate", "Ngày thay đổi phác đồ không được để trống");
            }
            
//            if (StringUtils.isNotEmpty(form.getRegimenStageDate()) && checkDateFormat(form.getRegimenStageDate(), "regimenStageDate", "Ngày thay đổi bậc phác đồ không hợp lệ", errors, sdfrmt) && 
//                    StringUtils.isNotEmpty(form.getTreatmentTime())) {
//                Date regimenStageDate = sdfrmt.parse(form.getRegimenStageDate());
//                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
//                if (treatmentTime.compareTo(regimenStageDate) < 0 && errors.getFieldError("regimenStageDate") == null) {
//                    errors.rejectValue("regimenStageDate", "regimenStageDate", "Ngày thay đổi bậc phác đồ không được lớn hơn ngày ngày điều trị ARV (" + form.getTreatmentTime() + ")");
//                }
//            }
            
            if (StringUtils.isNotEmpty(form.getReceivedWardDate()) && checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt)) {
                Date receivedWardDate = sdfrmt.parse(form.getReceivedWardDate());
                if (now.compareTo(receivedWardDate) < 0 && errors.getFieldError("receivedWardDate") == null) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate", "Ngày nhận thuốc tại xã không được lớn hơn ngày hiện tại ");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getReceivedWardDate()) && checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getTreatmentTime())) {
                Date receivedWardDate = sdfrmt.parse(form.getReceivedWardDate());
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                if (treatmentTime.compareTo(receivedWardDate) > 0 && errors.getFieldError("receivedWardDate") == null) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate", "Ngày nhận thuốc tại xã không được nhỏ hơn ngày điều trị ARV (" + form.getTreatmentTime() + ")");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getReceivedWardDate()) && checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getRegistrationTime())) {
                Date receivedWardDate = sdfrmt.parse(form.getReceivedWardDate());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (registrationTime.compareTo(receivedWardDate) > 0 && errors.getFieldError("receivedWardDate") == null) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate", "Ngày nhận thuốc tại xã không được nhỏ hơn ngày đăng ký (" + form.getRegistrationTime() +")");
                }
            }
            
            
            
            if (StringUtils.isNotEmpty(form.getSupplyMedicinceDate()) && checkDateFormat(form.getSupplyMedicinceDate(), "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không hợp lệ", errors, sdfrmt)) {
                Date supplyMedicinceDate = sdfrmt.parse(form.getSupplyMedicinceDate());
                if (now.compareTo(supplyMedicinceDate) < 0 && errors.getFieldError("supplyMedicinceDate") == null) {
                    errors.rejectValue("supplyMedicinceDate", "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không được lớn hơn ngày hiện tại ");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getSupplyMedicinceDate()) && checkDateFormat(form.getSupplyMedicinceDate(), "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getTreatmentTime())) {
                Date supplyMedicinceDate = sdfrmt.parse(form.getSupplyMedicinceDate());
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                if (treatmentTime.compareTo(supplyMedicinceDate) > 0 && errors.getFieldError("supplyMedicinceDate") == null) {
                    errors.rejectValue("supplyMedicinceDate", "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không được nhỏ hơn ngày điều trị ARV (" + form.getTreatmentTime() + ")");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getSupplyMedicinceDate()) && checkDateFormat(form.getSupplyMedicinceDate(), "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getRegistrationTime())) {
                Date supplyMedicinceDate = sdfrmt.parse(form.getSupplyMedicinceDate());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (registrationTime.compareTo(supplyMedicinceDate) > 0 && errors.getFieldError("supplyMedicinceDate") == null) {
                    errors.rejectValue("supplyMedicinceDate", "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không được nhỏ hơn ngày đăng ký (" + form.getRegistrationTime() +")");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám không hợp lệ", errors, sdfrmt)) {
                String validLastExaminationTime = null;
                if(!form.getLastExaminationTime().contains("/")){
                    validLastExaminationTime = form.getLastExaminationTime().substring(0, 2) + "/" + form.getLastExaminationTime().substring(2, 4) + "/" + form.getLastExaminationTime().substring(4, form.getLastExaminationTime().length());
                } else {
                    validLastExaminationTime = form.getLastExaminationTime();
                }
                Date lastExaminationTime = sdfrmt.parse(validLastExaminationTime);
                if (now.compareTo(lastExaminationTime) < 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime", "Ngày khám  không được lớn hơn ngày hiện tại");
                }
            }
            
            if (StringUtils.isEmpty(form.getLastExaminationTime()) || "dd/mm/yyyy".equals(form.getLastExaminationTime())) {
                if (errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime", "Ngày khám  không được để trống");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getTreatmentTime())) {
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                if (treatmentTime.compareTo(lastExaminationTime) > 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime", "Ngày khám không được nhỏ hơn ngày điều trị ARV (" + form.getTreatmentTime() + ")");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getRegistrationTime())) {
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (registrationTime.compareTo(lastExaminationTime) > 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime", "Ngày khám không được nhỏ hơn ngày đăng ký (" + form.getRegistrationTime() +")");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám không hợp lệ", errors, sdfrmt) && 
                    StringUtils.isNotEmpty(form.getEndTime())) {
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                Date endTime = sdfrmt.parse(form.getEndTime());
                if (endTime.compareTo(lastExaminationTime) < 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime", "Ngày khám không được lớn hơn ngày kết thúc (" + form.getEndTime() +")");
                }
            }
            
//            if(StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && StringUtils.isEmpty(form.getTreatmentRegimenID()) && form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())){
//                errors.rejectValue("treatmentRegimenID", "treatmentRegimenID", "Phác đồ hiện tại không được để trống");
//            }
//            
//            if(StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && StringUtils.isEmpty(form.getTreatmentRegimenStage()) && form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())){
//                errors.rejectValue("treatmentRegimenStage", "treatmentRegimenStage", "Bậc phác đồ hiện tại không được để trống");
//            }
            
            
            
        } catch(ParseException ex){}
    }
    /**
     * Check date format
     *
     * @param inputValue
     * @param id
     * @param message
     * @param errors
     * @param sdfrmt
     */
    private boolean checkDateFormat(String inputValue, String id, String message, Errors errors, SimpleDateFormat sdfrmt) {
        if (StringUtils.isEmpty(inputValue) || "NaN/NaN/NaN".equals(inputValue)) {
            return false;
        }
        if(!inputValue.contains("/")){
            inputValue = inputValue.substring(0, 2) + "/" + inputValue.substring(2, 4) + "/" + inputValue.substring(4, inputValue.length());
        }
        sdfrmt.setLenient(false);
        try {
            sdfrmt.parse(inputValue);
        } catch (ParseException ex) {
            if(errors.getFieldError(id) == null)
            {
                errors.rejectValue(id, id, message);
            }
            return false;
        }
        return true;
    }
    
     // Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, Errors errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.rejectValue(id, id, message);
        }
    }
}

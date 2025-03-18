/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.form.HtcVisitForm;
import com.gms.entity.form.htc.HtcTransferTreatmentForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author Admin
 */
@Component
public class HtcRequestAdditionValidate  implements Validator {
    protected static final String FORMATDATE = "dd/MM/yyyy";
    
    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcTransferTreatmentForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HtcVisitForm form = (HtcVisitForm) o;
        if(StringUtils.isEmpty(form.getCauseChange())){
            errors.rejectValue("causeChange", "causeChange", "Lý do yêu cầu không được để trống");
        }
        if(StringUtils.isNotEmpty(form.getPatientID()) && form.getPatientID().length() > 50){
            errors.rejectValue("patientID", "patientID", "Số CMND không được quá 50 ký tự");
        }
        
        if (StringUtils.isNotEmpty(form.getPatientID()) && !TextUtils.removeDiacritical(form.getPatientID().trim()).matches(RegexpEnum.CMND)
                && errors.getFieldError("patientID") == null) {
            errors.rejectValue("patientID", "patientID", "Số CMND không chứa ký tự đặc biệt/chữ tiếng việt, số âm, số thập phân");
        }
        if(StringUtils.isNotEmpty(form.getCauseChange()) && form.getCauseChange().length() > 255){
            errors.rejectValue("causeChange", "causeChange", "Lý do yêu cầu không được quá 255 ký tự");
        }
        //Địa chỉ thường trú
        if (StringUtils.isNotEmpty(form.getPermanentAddress()) && form.getPermanentAddress().length() > 500) {
            errors.rejectValue("permanentAddress", "permanentAddress", "Số nhà không được quá 500 ký tự");
        }
        
        if (StringUtils.isNotEmpty(form.getPermanentAddressGroup()) && form.getPermanentAddressGroup().length() > 500) {
            errors.rejectValue("permanentAddressGroup", "permanentAddressGroup", "Tổ/ấp/Khu phố không được quá 500 ký tự");
        }

        if (StringUtils.isNotEmpty(form.getPermanentAddressStreet()) && form.getPermanentAddressStreet().length() > 500) {
            errors.rejectValue("permanentAddressStreet", "permanentAddressStreet", "Đường phố không được quá 500 ký tự");
        }
        
        if (form.getPermanentProvinceID() == null || "".equals(form.getPermanentProvinceID())) {
            errors.rejectValue("permanentProvinceID", "permanentProvinceID", "Tỉnh/Thành phố không được để trống");
        }

        if (form.getPermanentDistrictID() == null || "".equals(form.getPermanentDistrictID())) {
            errors.rejectValue("permanentDistrictID", "permanentDistrictID", "Quận/Huyện không được để trống");
        }

        if (form.getPermanentWardID() == null || "".equals(form.getPermanentWardID())) {
            errors.rejectValue("permanentWardID", "permanentWardID", "Phường/xã không được để trống");
        }

        //Địa chỉ cư trú
        if (StringUtils.isNotEmpty(form.getCurrentAddress()) && form.getCurrentAddress().length() > 500) {
            errors.rejectValue("currentAddress", "currentAddress", "Số nhà không được quá 500 ký tự");
        }
        
        if (StringUtils.isNotEmpty(form.getCurrentAddressGroup()) && form.getCurrentAddressGroup().length() > 500) {
            errors.rejectValue("currentAddressGroup", "currentAddressGroup", "Tổ/ấp/Khu phố không được quá 500 ký tự");
        }

        if (StringUtils.isNotEmpty(form.getCurrentAddressStreet()) && form.getCurrentAddressStreet().length() > 500) {
            errors.rejectValue("currentAddressStreet", "currentAddressStreet", "Đường phố không được quá 500 ký tự");
        }
        
        if (form.getCurrentProvinceID()== null || "".equals(form.getCurrentProvinceID())) {
            errors.rejectValue("currentProvinceID", "currentProvinceID", "Tỉnh/Thành phố không được để trống");
        }

        if (form.getCurrentDistrictID()== null || "".equals(form.getCurrentDistrictID())) {
            errors.rejectValue("currentDistrictID", "currentDistrictID", "Quận/Huyện không được để trống");
        }

        if (form.getCurrentWardID()== null || "".equals(form.getCurrentWardID())) {
            errors.rejectValue("currentWardID", "currentWardID", "Phường/xã không được để trống");
        }
        
        if (form.getPatientName() == null || "".equals(form.getPatientName())) {
            errors.rejectValue("patientName", "patientName", "Họ và tên KH không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getPatientName()) && form.getPatientName().length() > 100 && errors.getFieldError("patientName") == null) {
            errors.rejectValue("patientName", "patientName", "Họ và tên KH không được quá 100 ký tự");
        }

        

        // Kiểm tra định ký tự đặc biêt của tên khách hàng
        if (StringUtils.isNotBlank(form.getPatientName()) && !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)
                  && errors.getFieldError("patientName") == null) {
            errors.rejectValue("patientName", "patientName", "Họ và tên chỉ chứa các kí tự chữ cái");
        }
        if(StringUtils.isNotEmpty(form.getResultsSiteTime()) && form.getConfirmTestStatus().equals("2") && !form.getResultsSiteTime().contains("/")){
            String resultsSiteTime = form.getResultsSiteTime();
            form.setResultsSiteTime(resultsSiteTime.substring(0, 2) + "/" + resultsSiteTime.substring(2, 4) + "/" + resultsSiteTime.substring(4, 8));
        }
//        if (StringUtils.isEmpty(form.getResultsSiteTime()) && form.getConfirmTestStatus().equals("2")) {
//            errors.rejectValue("resultsSiteTime", "resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ không được để trống");
//        }
//        
//        if (StringUtils.isNotEmpty(form.getResultsSiteTime()) && isThisDateValid(form.getResultsSiteTime()) && errors.getFieldError("resultsSiteTime") == null) {
//            Date feedbackResultsReceivedTimeOpc = TextUtils.convertDate(form.getResultsSiteTime(), FORMATDATE);
//            if (feedbackResultsReceivedTimeOpc.compareTo(new Date()) > 0) {
//                errors.rejectValue("resultsSiteTime", "resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ không được lớn hơn ngày hiện tại");
//            }
//        }
//        
//        if(StringUtils.isNotEmpty(form.getResultsSiteTime()) && !isThisDateValid(form.getResultsSiteTime()) && form.getConfirmTestStatus().equals("2") && errors.getFieldError("resultsSiteTime") == null){
//            errors.rejectValue("resultsSiteTime", "resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ không hợp lệ");
//        }
        
        if (StringUtils.isEmpty(form.getYearOfBirth())) {
            errors.rejectValue("yearOfBirth", "yearOfBirth", "Năm sinh không được để trống");
        }
        
        if (StringUtils.isEmpty(form.getGenderID())) {
            errors.rejectValue("genderID", "genderID", "Giới tính không được để trống");
        }
        
        if (StringUtils.isEmpty(form.getObjectGroupID())) {
            errors.rejectValue("objectGroupID", "objectGroupID", "Nhóm đối tượng không được để trống");
        }

        if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && !form.getYearOfBirth().matches("[0-9]{0,4}")  && errors.getFieldError("yearOfBirth") == null) {
            errors.rejectValue("yearOfBirth", "yearOfBirth", "Năm sinh gồm bốn chữ số vd: 1994");
        } else if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && (Integer.parseInt(form.getYearOfBirth()) < 1900
                || Integer.parseInt(form.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR))  && errors.getFieldError("yearOfBirth") == null) {
            errors.rejectValue("yearOfBirth", "yearOfBirth", "Năm sinh hợp lệ từ 1900 - năm hiện tại");
        }
    }
    /**
     * Check valid date
     *
     * @param dateToValidate
     * @return
     */
    private boolean isThisDateValid(String dateToValidate) {

        if (StringUtils.isEmpty(dateToValidate)) {
            return false;
        }
        
        dateToValidate = dateToValidate.toLowerCase();
        if (dateToValidate.contains("d") || dateToValidate.contains("m") || dateToValidate.contains("y")) {
            return false;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return Pattern.matches("[0-9]{1,2}(/|-)[0-9]{1,2}(/|-)[0-9]{4}", dateToValidate);
    }
}

package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.TreatmentStageEnum;
import com.gms.entity.form.opc_arv.OpcStageForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author TrangBN
 */
@Component
public class OpcStageEndValidate implements Validator {

    protected static final String FORMATDATE = "dd/MM/yyyy";

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcStageForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OpcStageForm form = (OpcStageForm) o;
        Date currentDate = new Date();
        
        Map<String, String> treatmentStatus = new HashMap<>();
        for (StatusOfTreatmentEnum value : StatusOfTreatmentEnum.values()) {
            treatmentStatus.put(value.getKey(), value.getLabel());
        }
        
        String endTime = StringUtils.isNotEmpty(form.getEndTime()) ? form.getEndTime().replace("/", "") : form.getEndTime();
        endTime = insertString(endTime, "/");
        String treatmentTime = StringUtils.isNotEmpty(form.getTreatmentTime()) ? form.getTreatmentTime().replace("/", "") : form.getTreatmentTime();
        treatmentTime = insertString(treatmentTime, "/");
        String registrationTime = StringUtils.isNotEmpty(form.getRegistrationTime()) ? form.getRegistrationTime().replace("/", "") : form.getRegistrationTime();
        registrationTime = insertString(registrationTime, "/");
        String treatmentStageTime = StringUtils.isNotEmpty(form.getTreatmentStageTime()) ? form.getTreatmentStageTime().replace("/", "") : form.getTreatmentStageTime();
        treatmentStageTime = insertString(treatmentStageTime, "/");
        String confirmTime = StringUtils.isNotEmpty(form.getConfirmTime()) ? form.getConfirmTime().replace("/", "") : form.getConfirmTime();
        confirmTime = TextUtils.formatDate("ddMMyyyy", FORMATDATE, confirmTime);
        
        
        
        try {
            // Ngày kết thúc
            if (errors.getFieldError("endTime") == null) {
                if (TextUtils.convertDate(endTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(currentDate)) > 0) {
                    errors.rejectValue("endTime", "endTime", "Ngày kết thúc không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("endTime") == null) {
                if (TextUtils.convertDate(treatmentTime, FORMATDATE) != null && TextUtils.convertDate(endTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
                    errors.rejectValue("endTime", "endTime", "Ngày kết thúc không được nhỏ hơn ngày điều trị ARV");
                }
            }
            if (errors.getFieldError("endTime") == null && TextUtils.convertDate(endTime, FORMATDATE) != null && TextUtils.convertDate(treatmentTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
                errors.rejectValue("endTime", "endTime", "Ngày kết thúc không được nhỏ hơn ngày điều trị ARV");
            }

            if (errors.getFieldError("endTime") == null && TextUtils.convertDate(endTime, FORMATDATE) != null && TextUtils.convertDate(registrationTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(registrationTime, FORMATDATE))) < 0) {
                errors.rejectValue("endTime", "endTime", "Ngày kết thúc không được nhỏ hơn ngày đăng ký");
            }

            if (errors.getFieldError("endTime") == null && StringUtils.isEmpty(form.getEndTime())) {
                errors.rejectValue("endTime", "endTime", "Ngày kết thúc không được để trống");
            }
            
            if (StringUtils.isNotEmpty(form.getEndTime()) && !isThisDateValid(endTime)) {
                errors.rejectValue("endTime", "endTime", "Ngày kết thúc không hợp lệ");
            }
            
            // Lý do kết thúc
            if (StringUtils.isEmpty(form.getEndCase()) && errors.getFieldError("endCase") == null) {
                errors.rejectValue("endCase", "endCase", "Lý do kết thúc không được để trống");
            }
            
            if (StringUtils.isNotEmpty(form.getEndCase()) && form.getEndCase().equals(ArvEndCaseEnum.END.getKey()) && errors.getFieldError("endCase") == null) {
                if (form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.BO_TRI.getKey())) {
                    errors.rejectValue("endCase", "endCase", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là Bỏ trị");
                }
                if (form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.TU_VONG.getKey())) {
                    errors.rejectValue("endCase", "endCase", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là Tử vong");
                }
                if (form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.MAT_DAU.getKey())) {
                    errors.rejectValue("endCase", "endCase", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là Mất dấu");
                }
            }
            
            // Cơ sở chuyển đi
            if (StringUtils.isNotEmpty(form.getTransferSiteID()) && form.getCurrentSiteID() == Long.parseLong(form.getTransferSiteID())) {
                errors.rejectValue("transferSiteID", "transferSiteID", "Không thể chuyển gửi tới cơ sở hiện tại");
            }

            if (form.getTransferSiteID() == null && ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase())) {
                errors.rejectValue("transferSiteID", "transferSiteID", "Cơ sở chuyển đi không được để trống");
            }
            
            // Cơ sở khác
            if (StringUtils.isEmpty(form.getTransferSiteName()) && form.getTransferSiteID() != null && form.getTransferSiteID().equals("-1")) {
                errors.rejectValue("transferSiteName", "transferSiteName", "Cơ sở chuyển đi không được để trống");
            }
            
            // Lý do chuyển
            if (StringUtils.isNotEmpty(form.getTransferCase()) && form.getTransferCase().length() > 200) {
                errors.rejectValue("transferCase", "transferCase", "Lý do chuyển không được quá 200 ký tự");
            }
            
            if ("-1".equals(form.getTransferSiteID()) && StringUtils.isNotEmpty(form.getTransferSiteName()) && form.getTransferSiteName().length() > 255) {
                errors.rejectValue("transferSiteName", "transferSiteName", "Cơ sở chuyển đi không được quá 255 ký tự");
            }
            
            //Ngày biến động
            if (errors.getFieldError("treatmentStageTime") == null) {
                if (TextUtils.convertDate(treatmentStageTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentStageTime, FORMATDATE)).compareTo(getDateWithoutTime(currentDate)) > 0) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime", "Ngày biến động không được lớn hơn ngày hiện tại");
                }
            }

            if (errors.getFieldError("treatmentStageTime") == null && StringUtils.isEmpty(treatmentStageTime)) {
                errors.rejectValue("treatmentStageTime", "treatmentStageTime", "Ngày biến động không được để trống");
            }
            
            if (StringUtils.isNotEmpty(form.getTreatmentStageTime()) && errors.getFieldError("treatmentStageTime") == null) {
                if (TextUtils.convertDate(confirmTime, FORMATDATE) != null && TextUtils.convertDate(treatmentStageTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentStageTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(confirmTime, FORMATDATE))) < 0) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime", String.format("Ngày biến động không được nhỏ hơn Ngày XN khẳng định ( %s )", confirmTime));
                }
            }

            treatmentStageTime = StringUtils.isNotEmpty(form.getTreatmentStageTime()) ? form.getTreatmentStageTime().replace("/", "") : form.getTreatmentStageTime();
            treatmentStageTime = insertString(treatmentStageTime, "/");
            if (StringUtils.isNotEmpty(form.getTreatmentStageTime()) && !isThisDateValid(treatmentStageTime)) {
                errors.rejectValue("treatmentStageTime", "treatmentStageTime", "Ngày biến động không hợp lệ");
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(OpcStageEndValidate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Convert sang ngày không có thời gian
     * 
     * @param date
     * @return
     * @throws ParseException 
     */
    public static Date getDateWithoutTime(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
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
    
    /**
     * Chèn chuôi
     * 
     * @param originalString
     * @param stringToBeInserted
     * @param index
     * @return 
     */
    private String insertString(
            String originalString,
            String stringToBeInserted) {

        if (StringUtils.isEmpty(originalString)) {
            return null;
        }
        
        // Create a new string 
        String separeateDate = originalString.substring(0, 1 + 1)
                + stringToBeInserted
                + originalString.substring(1 + 1);
        
        String separeateMonth = separeateDate.substring(0, 4 + 1)
                + stringToBeInserted
                + separeateDate.substring(4 + 1);
        
        // return the modified String 
        return separeateMonth;
    }
}

package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.OpcStageEntity;
import static com.gms.entity.validate.OpcArvVNPTValidate.FORMATDATE;
import com.gms.service.OpcArvService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author pdThaNG
 */
@Component
public class OpcStageVNPTValidate {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    @Autowired
    private OpcArvService opcArvService;

    private static final String PHONE_NUMBER = "(^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$)|(02[0-9])+([0-9]{8}$)";
    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/]+[^-]$)";

    public HashMap<String, String> validate(OpcStageEntity form, Date confirmTime, boolean isPrep) {
        HashMap<String, String> errors = new HashMap<>();
        if (isPrep) {
            return errors;
        }
        try {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
            Date minDate = sdfrmt.parse("01/01/1900");

            if (form.getRegistrationTime() == null) {
                errors.put("stage." + "registrationTime", "Ngày đăng ký không được để trống");

            }
            dateCompare(form.getRegistrationTime(), now, "registrationTime", "Ngày đăng ký không được lớn hơn ngày hiện tại", errors, FORMATDATE);

            if (form.getEndTime() != null && form.getRegistrationTime() != null && form.getRegistrationTime().compareTo(form.getEndTime()) > 0) {
                errors.put("stage." + "endTime", "Ngày kết thúc không được nhỏ hơn ngày đăng ký");
            }
            if (form.getRegistrationTime() != null && form.getRegistrationTime().compareTo(confirmTime) < 0) {
                errors.put("stage." + "registrationTime", String.format("Ngày đăng ký không được nhỏ hơn ngày XN khẳng định (%s)", confirmTime));
            }
            if (form.getRegistrationTime() != null && form.getTreatmentTime() != null && form.getTreatmentTime().compareTo(form.getRegistrationTime()) < 0) {
                errors.put("stage." + "treatmentTime", "Ngày điều trị ARV không được nhỏ hơn ngày đăng ký");
            }
            if (form.getRegistrationType() == null) {
                errors.put("stage." + "registrationType", "Loại đăng ký không được để trống");
            }

//            if (form.getRegistrationType().equals(RegistrationTypeEnum.TRANSFER.getKey()) && (form.getSourceSiteID() == null || form.getSourceSiteID() == 0)) {
//                errors.put("stage." + "sourceSiteID", "Cơ sở chuyển gửi không được để trống");
//            }
            if (form.getEndTime() != null && StringUtils.isEmpty(form.getEndCase())) {
                errors.put("stage." + "endCase", "Lý do kết thúc không được để trống");
            }
            if (!StringUtils.isEmpty(form.getEndCase()) && form.getEndTime() == null) {
                errors.put("stage." + "endTime", "Ngày kết thúc không được để trống");
            }
            dateCompare(form.getEndTime(), now, "endTime", "Ngày kết thúc không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            if (form.getTreatmentTime() != null && form.getEndTime() != null && form.getEndTime().compareTo(form.getTreatmentTime()) < 0) {
                errors.put("stage." + "endTime", "Ngày kết thúc không được nhỏ hơn ngày điều trị ARV");
            }

        } catch (Exception e) {
            errors.put("stage." + "VALIDATE_ERROR", "Lỗi validate");
        }

        return errors;
    }
    // Check length for input string

    private void validateLength(String inputValue, int length, String id, String message, HashMap<String, String> errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.put("stage." + id, message);
        }
    }

    private boolean dateCompare(Date inputForm, Date inputTo, String id, String message, HashMap<String, String> errors, String formatDate) {
        if (inputForm == null || inputTo == null) {
            return false;
        }
        if (inputTo.compareTo(inputForm) < 0) {
            errors.put("stage." + id, message);
        }
        return true;
    }

}

package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.form.opc_arv.OpcStageForm;
import com.gms.entity.form.opc_arv.OpcViralLoadForm;
import static com.gms.entity.validate.OpcStageValidate.FORMATDATE;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class OpcViralValidate implements Validator {

    protected static final String FORMATDATE = "dd/MM/yyyy";

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcViralLoadForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OpcViralLoadForm form = (OpcViralLoadForm) o;

        Date currentDate = new Date();

        if(StringUtils.isNotEmpty(form.getResultNumber()) && form.getResultNumber().length() > 50){
            errors.rejectValue("resultNumber", "resultNumber", "Nồng độ virus không được quá 50 ký tự");
        }
        
        if("-1".equals(form.getTestSiteID()) && StringUtils.isNotEmpty(form.getTestSiteName()) && form.getTestSiteName().length() > 255){
            errors.rejectValue("testSiteName", "testSiteName", "Nơi xét nghiệm không được quá 255 ký tự");
        }
        
        String sampleTime = StringUtils.isNotEmpty(form.getSampleTime()) ? form.getSampleTime().replace("/", "") : form.getSampleTime();
        sampleTime = insertString(sampleTime, "/");
        if (StringUtils.isNotEmpty(form.getSampleTime()) && !isThisDateValid(sampleTime)) {
            errors.rejectValue("sampleTime", "sampleTime", "Ngày lấy mẫu không hợp lệ");
        }

        if (StringUtils.isEmpty(form.getStageID())) {
            errors.rejectValue("stageID", "stageID", "Giai đoạn điều trị không được để trống");
        }

        if (StringUtils.isEmpty(form.getTestTime())) {
            errors.rejectValue("testTime", "testTime", "Ngày xét nghiệm không được để trống");
        }
        String testTime = StringUtils.isNotEmpty(form.getTestTime()) ? form.getTestTime().replace("/", "") : form.getTestTime();
        testTime = insertString(testTime, "/");
        if (errors.getFieldError("testTime") == null && StringUtils.isNotEmpty(form.getTestTime()) && !isThisDateValid(testTime)) {
            errors.rejectValue("testTime", "testTime", "Ngày xét nghiệm không hợp lệ");
        }

        // Ngày nhận kết quả
        String resultTime = StringUtils.isNotEmpty(form.getResultTime()) ? form.getResultTime().replace("/", "") : form.getResultTime();
        resultTime = insertString(resultTime, "/");
        if (errors.getFieldError("resultTime") == null && StringUtils.isNotEmpty(form.getResultTime()) && !isThisDateValid(resultTime)) {
            errors.rejectValue("resultTime", "resultTime", "Ngày nhận kết quả không hợp lệ");
        }

        // Ngày hẹn xét nghiệm
        String retryTime = StringUtils.isNotEmpty(form.getRetryTime()) ? form.getRetryTime().replace("/", "") : form.getRetryTime();
        retryTime = insertString(retryTime, "/");
        if (errors.getFieldError("retryTime") == null && StringUtils.isNotEmpty(form.getRetryTime()) && !isThisDateValid(retryTime)) {
            errors.rejectValue("retryTime", "retryTime", "Ngày hẹn xét nghiệm không hợp lệ");
        }

        if (form.getCauses() == null || form.getCauses().isEmpty()) {
            errors.rejectValue("causes", "causes", "Lý do xét nghiệm không được để trống");
        }

        if (StringUtils.isEmpty(form.getTestSiteID())) {
            errors.rejectValue("testSiteID", "testSiteID", "Nơi xét nghiệm không được để trống");
        }

        if (StringUtils.isEmpty(form.getTestSiteName()) && StringUtils.isNotEmpty(form.getTestSiteID()) && form.getTestSiteID().equals("-1")) {
            errors.rejectValue("testSiteName", "testSiteName", "Nơi xét nghiệm không được để trống");
        }

//        if (StringUtils.isEmpty(form.getResult())) {
//            errors.rejectValue("result", "result", "Kết quả xét nghiệm không được để trống");
//        }

        if (StringUtils.isNotEmpty(form.getNote())) {
            validateLength(form.getNote(), 500, "note", "Ghi chú không được quá 500 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getResult())) {
            validateLength(form.getResult(), 50, "result", "Kết quả không được quá 50 ký tự", errors);
        }

        try {
            if (errors.getFieldError("sampleTime") == null) {
                if (TextUtils.convertDate(sampleTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(sampleTime, FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("sampleTime", "sampleTime", "Ngày lấy mẫu không được lớn hơn ngày hiện tại");
                }
            }

            if (errors.getFieldError("testTime") == null) {
                if (getDateWithoutTime(TextUtils.convertDate(testTime, FORMATDATE)) != null && getDateWithoutTime(TextUtils.convertDate(testTime, FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("testTime", "testTime", "Ngày xét nghiệm không được lớn hơn ngày hiện tại");
                }
            }

            if (errors.getFieldError("testTime") == null) {
                if (TextUtils.convertDate(sampleTime, FORMATDATE) != null && TextUtils.convertDate(testTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(testTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(sampleTime, FORMATDATE))) < 0) {
                    errors.rejectValue("testTime", "testTime", "Ngày xét nghiệm không được nhỏ hơn ngày lấy mẫu");
                }
            }

            if (errors.getFieldError("testTime") == null && form.getFirstViralLoadTime() != null) {
                if (TextUtils.convertDate(testTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(testTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(form.getFirstViralLoadTime(), FORMATDATE))) < 0) {
                    errors.rejectValue("testTime", "testTime", "Ngày xét nghiệm không được nhỏ hơn ngày xét nghiệm đầu tiên (" + form.getFirstViralLoadTime() +")");
                }
            }

            if (errors.getFieldError("resultTime") == null) {
                if (TextUtils.convertDate(resultTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(resultTime, FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("resultTime", "resultTime", "Ngày nhận kết quả không được lớn hơn ngày hiện tại");
                }
            }

            if (errors.getFieldError("resultTime") == null) {
                if (TextUtils.convertDate(testTime, FORMATDATE) != null && TextUtils.convertDate(resultTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(resultTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(testTime, FORMATDATE))) < 0) {
                    errors.rejectValue("resultTime", "resultTime", "Ngày nhận kết quả không được nhỏ hơn ngày xét nghiệm");
                }
            }

            if (errors.getFieldError("retryTime") == null) {
                if (TextUtils.convertDate(testTime, FORMATDATE) != null && TextUtils.convertDate(retryTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(retryTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(testTime, FORMATDATE))) < 0) {
                    errors.rejectValue("retryTime", "retryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn ngày xét nghiệm");
                }
            }

            if (errors.getFieldError("retryTime") == null) {
                if (TextUtils.convertDate(resultTime, FORMATDATE) != null && TextUtils.convertDate(retryTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(retryTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(resultTime, FORMATDATE))) < 0) {
                    errors.rejectValue("retryTime", "retryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn ngày nhận kết quả");
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(OpcViralValidate.class.getName()).log(Level.SEVERE, null, ex);
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

        if (StringUtils.isBlank(dateToValidate)) {
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
        Date resultDate = TextUtils.convertDate(dateToValidate, FORMATDATE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(resultDate);
        int year = cal.get(Calendar.YEAR);

        return Pattern.matches("[0-9]{1,2}(/|-)[0-9]{1,2}(/|-)[0-9]{4}", dateToValidate) && year >= 1900;
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

    // Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, Errors errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.rejectValue(id, id , message);
        }
    }
}

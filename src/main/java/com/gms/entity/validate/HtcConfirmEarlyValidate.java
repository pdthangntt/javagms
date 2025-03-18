package com.gms.entity.validate;

import com.gms.entity.form.HtcConfirmEarlyHivForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Custom validate for early
 *
 * @author pdThang
 */
@Component
public class HtcConfirmEarlyValidate implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validate(Object o, Errors errors) {

        // Validate existed code
        HtcConfirmEarlyHivForm form = (HtcConfirmEarlyHivForm) o;

        if (StringUtils.isNotEmpty(form.getEarlyHivDate()) && StringUtils.isEmpty(form.getEarlyHiv()) && errors.getFieldError("earlyHiv") == null) {
            errors.rejectValue("earlyHiv", "earlyHiv.error.message", "KQXN nhiễm mới không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getVirusLoadDate()) && StringUtils.isEmpty(form.getVirusLoad()) && errors.getFieldError("virusLoad") == null) {
            errors.rejectValue("virusLoad", "virusLoad.error.message", "KQXN tải lượng virus không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getVirusLoadNumber()) && errors.getFieldError("virusLoadNumber") == null) {
            if (form.getVirusLoadNumber().length() > 9) {
                errors.rejectValue("virusLoadNumber", "virusLoadNumber.error.message", "Nồng độ virus không được quá 9 chữ số");
            }
        }
        if (StringUtils.isNotEmpty(form.getVirusLoadNumber()) && errors.getFieldError("virusLoadNumber") == null) {
            try {
                Long.valueOf(form.getVirusLoadNumber());
                if (Integer.valueOf(form.getVirusLoadNumber()) < 0) {
                    errors.rejectValue("virusLoadNumber", "virusLoadNumber.error.message", "Nồng độ virus phải là số nguyên dương");
                }
            } catch (Exception e) {
                errors.rejectValue("virusLoadNumber", "virusLoadNumber.error.message", "Nồng độ virus phải là số nguyên dương");
            }

        }

        try {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            // Validate ngày xét nghiệm nhiễm mới HIV, và ngày xét nghiệm tải lượng virus
            if (StringUtils.isNotEmpty(form.getEarlyHivDate()) && checkDateFormat(form.getEarlyHivDate(), "earlyHivDate", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date earlyHivDate = sdfrmt.parse(form.getEarlyHivDate());
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (earlyHivDate.compareTo(new Date()) > 0 && errors.getFieldError("earlyHivDate") == null) {
                    errors.rejectValue("earlyHivDate", "earlyHivDate.error.message", "Ngày XN nhiễm mới HIV không được lớn hơn ngày hiện tại");
                }
                
                if (StringUtils.isNotEmpty(form.getConfirmTime()) && earlyHivDate.compareTo(confirmTime) < 0 && errors.getFieldError("earlyHivDate") == null) {
                    errors.rejectValue("earlyHivDate", "earlyHivDate.error.message", "Ngày XN nhiễm mới HIV không được nhỏ hơn ngày XN khẳng định");
                }
            }
            // Validate ngày xét nghiệm nhiễm mới HIV, và ngày xét nghiệm tải lượng virus
            if (StringUtils.isNotEmpty(form.getVirusLoadDate()) && checkDateFormat(form.getVirusLoadDate(), "virusLoadDate", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date virusLoadDate = sdfrmt.parse(form.getVirusLoadDate());
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (virusLoadDate.compareTo(new Date()) > 0 && errors.getFieldError("virusLoadDate") == null) {
                    errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng vi rút không được lớn hơn ngày hiện tại");
                }
//                
                if (StringUtils.isNotEmpty(form.getConfirmTime()) && virusLoadDate.compareTo(confirmTime) < 0 && errors.getFieldError("virusLoadDate") == null) {
                    errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng vi rút không được nhỏ hơn ngày XN khẳng định");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, Errors errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.rejectValue(id, id + ".error.message", message);
        }
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

        if (StringUtils.isEmpty(inputValue)) {
            return false;
        }
        sdfrmt.setLenient(false);
        try {
            sdfrmt.parse(inputValue);
        } catch (ParseException ex) {
            errors.rejectValue(id, id + ".error.message", message);
            return false;
        }
        return true;
    }

    // Check null or empty 
    private void validateNull(String inputValue, String id, String message, Errors errors) {
        if (StringUtils.isEmpty(inputValue) || inputValue == null) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }
}

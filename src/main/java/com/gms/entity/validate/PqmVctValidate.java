package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.EarlyHivResultEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.form.PqmVctForm;
import com.gms.service.PacPatientService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author pdThang
 */
@Component
public class PqmVctValidate implements Validator {
private static final String PHONE_NUMBER = "(^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$)|(02[4|5|7|2|6|8|9])+([0-9]{8}$)";
    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/\\*]+[^-]$)";
       
    @Autowired
    private PacPatientService pacPatientService;

    @Override
    public boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validate(Object o, Errors errors) {
        PqmVctForm form = (PqmVctForm) o;

        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        
        if (StringUtils.isNotEmpty(form.getEarlyHivDate())) {
            checkDateFormat(form.getEarlyHivDate(), "earlyHivDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
        }
        if (StringUtils.isNotEmpty(form.getRegisterTherapyTime())) {
            checkDateFormat(form.getRegisterTherapyTime(), "registerTherapyTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
        }
        if (StringUtils.isNotEmpty(form.getExchangeTime())) {
            checkDateFormat(form.getExchangeTime(), "exchangeTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
        }
        if (StringUtils.isNotEmpty(form.getRegisteredTherapySite()) && errors.getFieldError("registeredTherapySite") == null) {
            validateLength(form.getRegisteredTherapySite(), 200, "registeredTherapySite", "Tên cơ sở đăng ký điều trị không dài quá 200 ký tự", errors);
        }
        if (StringUtils.isNotEmpty(form.getTherapyNo()) && errors.getFieldError("therapyNo") == null) {
            validateLength(form.getTherapyNo(), 100, "therapyNo", "Mã số điều trị không dài quá 100 ký tự", errors);
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
    private void checkDateFormat(String inputValue, String id, String message, Errors errors, SimpleDateFormat sdfrmt) {

        if (StringUtils.isEmpty(inputValue)) {
            return;
        }
        sdfrmt.setLenient(false);
        try {
            sdfrmt.parse(inputValue);
        } catch (ParseException ex) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }

    // Check null or empty 
    private void validateNull(String inputValue, String id, String message, Errors errors) {
        if (StringUtils.isEmpty(inputValue) || inputValue == null) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }

    // Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, Errors errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }

}

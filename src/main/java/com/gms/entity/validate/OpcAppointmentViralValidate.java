package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.form.opc_arv.AppointmentViralForm;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author vvthanh
 */
@Component
public class OpcAppointmentViralValidate implements Validator {

    protected static final String FORMATDATE = "ddMMyyyy";
    protected static final String FORMATDATESLASH = "dd/MM/yyyy";
    @Autowired
    private SiteService siteService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(AppointmentViralForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        String currentDate = TextUtils.formatDate(new Date(), "ddMMyyyy");
        String minDate = "01011900";
        AppointmentViralForm form = (AppointmentViralForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        if(StringUtils.isNotEmpty(form.getNote()) && form.getNote().length() > 500){
            errors.rejectValue("note", "note", "Ghi chú không được quá 500 ký tự");
        }
        if (StringUtils.isEmpty(form.getViralLoadRetryTime())) {
            errors.rejectValue("viralLoadRetryTime", "viralLoadRetryTime", "Ngày hẹn xét nghiệm không được để trống");
        }
        if (form.getViralLoadCauses() == null || form.getViralLoadCauses().isEmpty()) {
            errors.rejectValue("viralLoadCauses", "viralLoadCauses", "Lý do xét nghiệm không được để trống");
        }
        if (!StringUtils.isEmpty(form.getViralLoadRetryTime())) {
            checkDateFormat(form.getViralLoadRetryTime(), "viralLoadRetryTime", "Ngày hẹn xét nghiệm không hợp lệ", errors, sdfrmt);
            dateCompare(currentDate, form.getViralLoadRetryTime(), "viralLoadRetryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn ngày hiện tại", errors, FORMATDATE);
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
        //Hàm này chỉ nhận định dạng dd/MM/yyyy
        if (StringUtils.isEmpty(inputValue)) {
            return false;
        }
        inputValue = inputValue.substring(0, 2) + "/" + inputValue.substring(2, 4) + "/" + inputValue.substring(4, inputValue.length());

        sdfrmt.setLenient(false);
        try {
            sdfrmt.parse(inputValue);
        } catch (ParseException ex) {
            if (errors.getFieldError(id) == null) {
                errors.rejectValue(id, id, message);
            }
            return false;
        }
        return true;
    }

    /**
     * compedate
     *
     * @param inputValue
     * @param id
     * @param message
     * @param errors
     * @param sdfrmt
     */
    private boolean dateCompare(String inputForm, String inputTo, String id, String message, Errors errors, String formatDate) {

        if (StringUtils.isEmpty(inputForm) || StringUtils.isEmpty(inputTo) || errors.getFieldError(id) != null) {
            return false;
        }
        Date from = TextUtils.convertDate(inputForm, formatDate);
        Date to = TextUtils.convertDate(inputTo, formatDate);
        if (errors.getFieldError(id) == null) {
            if (to.compareTo(from) < 0) {
                errors.rejectValue(id, id, message);
            }
        }
        return true;
    }
}

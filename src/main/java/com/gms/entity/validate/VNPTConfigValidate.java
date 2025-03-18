package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.EarlyHivResultEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.form.VNPTConfigForm;
import com.gms.service.PacPatientService;
import com.gms.service.VNPTConfigService;
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
public class VNPTConfigValidate implements Validator {

    @Autowired
    private VNPTConfigService configService;

    @Override
    public boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validate(Object o, Errors errors) {
        VNPTConfigForm form = (VNPTConfigForm) o;

        if (form.getCreateAt() == null) {
            validateNull(form.getID(), "ID", "Cơ sở không được để trống", errors);
        }
        validateNull(form.getVpntSiteID(), "vpntSiteID", "Mã cơ sở VNPT không được để trống", errors);
        validateNull(form.getActive(), "active", "Trạng thái hoạt động không được để trống", errors);

        if (!StringUtils.isEmpty(form.getID()) && configService.existID(Long.valueOf(form.getID())) && form.getCreateAt() == null
                && errors.getFieldError("ID") == null) {
            errors.rejectValue("ID", "ID.error.message", "Cấu hình cơ sở đã tồn tại.");
        }
        if (!StringUtils.isEmpty(form.getVpntSiteID()) && configService.existVpntSiteID(form.getVpntSiteID()) && form.getCreateAt() == null
                && errors.getFieldError("vpntSiteID") == null) {
            errors.rejectValue("vpntSiteID", "vpntSiteID.error.message", "Mã cơ sở VNPT đã tồn tại.");
        }
        if (form.getIDcheck() != null && !form.getIDcheck().equals(form.getVpntSiteID())) {
            if (!StringUtils.isEmpty(form.getVpntSiteID()) && configService.existVpntSiteID(form.getVpntSiteID()) && form.getCreateAt() != null
                    && errors.getFieldError("vpntSiteID") == null) {
                errors.rejectValue("vpntSiteID", "vpntSiteID.error.message", "Mã cơ sở VNPT đã tồn tại.");
            }
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

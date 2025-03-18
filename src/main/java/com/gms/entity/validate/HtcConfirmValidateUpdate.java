package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.HtcConfirmForm;
import com.gms.service.DistrictService;
import com.gms.service.HtcConfirmService;
import com.gms.service.ParameterService;
import com.gms.service.ProvinceService;
import com.gms.service.WardService;
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
 * Custom validate for HtcConfirmUpdate
 *
 * @author TrangBN
 */
@Component
public class HtcConfirmValidateUpdate implements Validator {

    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private WardService wardService;
    @Autowired
    private DistrictService districtService;

    private static final String VALIDATE_YEAR = "^\\d{4}$";

    @Override
    public boolean supports(Class<?> type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validate(Object o, Errors errors) {

        // Validate existed code
        HtcConfirmForm form = (HtcConfirmForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        try {
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

}

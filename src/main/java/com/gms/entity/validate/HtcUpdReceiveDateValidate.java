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
public class HtcUpdReceiveDateValidate  implements Validator {
    protected static final String FORMATDATE = "dd/MM/yyyy";
    
    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcTransferTreatmentForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HtcVisitForm form = (HtcVisitForm) o;
        
        // Ngày khách hàng nhận kết quả
        if (StringUtils.isEmpty(form.getResultsPatienTime()) && errors.getFieldError("resultsPatienTime") == null) {
            errors.rejectValue("resultsPatienTime", "resultsPatienTime", "Ngày KH nhận kết quả không không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getResultsPatienTime()) && !isThisDateValid(form.getResultsPatienTime()) && errors.getFieldError("resultsPatienTime") == null) {
            errors.rejectValue("resultsPatienTime", "resultsPatienTime", "Ngày KH nhận kết quả không hợp lệ");
        }
        
        if (StringUtils.isNotEmpty(form.getResultsPatienTime()) && errors.getFieldError("resultsPatienTime") == null) {
            Date resultsPatienTime = TextUtils.convertDate(form.getResultsPatienTime(), FORMATDATE);
            if (resultsPatienTime.compareTo(new Date()) > 0) {
                errors.rejectValue("resultsPatienTime", "resultsPatienTime", "Ngày KH nhận kết quả không được lớn hơn ngày hiện tại");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getResultsPatienTime()) && StringUtils.isNotEmpty(form.getResultsSiteTime()) && errors.getFieldError("resultsPatienTime") == null) {
            Date resultsSiteTime = TextUtils.convertDate(form.getResultsSiteTime(), FORMATDATE);
            Date resultsPatienTime = TextUtils.convertDate(form.getResultsPatienTime(), FORMATDATE);
            if (resultsPatienTime.compareTo(resultsSiteTime) < 0) {
                errors.rejectValue("resultsPatienTime", "resultsPatienTime", "Ngày KH nhận kết quả không được nhỏ hơn ngày cơ sở nhận kết quả khẳng định (" + form.getResultsSiteTime() + ")");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeServiceConfirm()) && form.getExchangeServiceConfirm().equals("5") && StringUtils.isEmpty(form.getExchangeServiceNameConfirm()) && errors.getFieldError("exchangeServiceNameConfirm") == null) {
            errors.rejectValue("exchangeServiceNameConfirm", "exchangeServiceNameConfirm", "Dịch vụ tư vấn chuyển gửi không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getArvExchangeResult()) && form.getArvExchangeResult().equals("1") && StringUtils.isEmpty(form.getExchangeConsultTime()) && errors.getFieldError("exchangeConsultTime") == null) {
            errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && !isThisDateValid(form.getExchangeConsultTime()) && errors.getFieldError("exchangeConsultTime") == null) {
            errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không hợp lệ");
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && errors.getFieldError("exchangeConsultTime") == null) {
            Date resultsPatienTime = TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE);
            if (resultsPatienTime.compareTo(new Date()) > 0) {
                errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không được lớn hơn ngày hiện tại");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && StringUtils.isNotEmpty(form.getResultsSiteTime()) && errors.getFieldError("exchangeConsultTime") == null) {
            Date resultsSiteTime = TextUtils.convertDate(form.getResultsSiteTime(), FORMATDATE);
            Date exchangeConsultTime = TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE);
            if (exchangeConsultTime.compareTo(resultsSiteTime) < 0) {
                errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không được nhỏ hơn ngày cơ sở nhận kết quả khẳng định");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && StringUtils.isEmpty(form.getArvExchangeResult()) && errors.getFieldError("arvExchangeResult") == null) {
            errors.rejectValue("arvExchangeResult", "arvExchangeResult", "Kết quả tư vấn chuyển gửi điều trị ARV không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getArvExchangeResult()) && form.getArvExchangeResult().equals("1") && StringUtils.isEmpty(form.getExchangeTime()) && errors.getFieldError("exchangeTime") == null) {
            errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && !isThisDateValid(form.getExchangeTime()) && errors.getFieldError("exchangeTime") == null) {
            errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không hợp lệ");
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && errors.getFieldError("exchangeTime") == null) {
            Date exchangeTime = TextUtils.convertDate(form.getExchangeTime(), FORMATDATE);
            if (exchangeTime.compareTo(new Date()) > 0) {
                errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không được lớn hơn ngày hiện tại");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && StringUtils.isNotEmpty(form.getExchangeConsultTime()) && errors.getFieldError("exchangeTime") == null) {
            Date exchangeConsultTime = TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE);
            Date exchangeTime = TextUtils.convertDate(form.getExchangeTime(), FORMATDATE);
            if (exchangeTime.compareTo(exchangeConsultTime) < 0) {
                errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không được nhỏ hơn ngày tư vấn chuyển gửi điều trị ARV");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && StringUtils.isEmpty(form.getPermanentProvinceID()) && errors.getFieldError("permanentProvinceID") == null) {
            errors.rejectValue("permanentProvinceID", "permanentProvinceID", "Tỉnh/Thành phố không được trống");
        }
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && StringUtils.isEmpty(form.getPermanentDistrictID()) && errors.getFieldError("permanentDistrictID") == null) {
            errors.rejectValue("permanentDistrictID", "permanentDistrictID", "Quận/Huyện không được trống");
        }
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && StringUtils.isEmpty(form.getArrivalSiteID()) && errors.getFieldError("arrivalSiteID") == null) {
            errors.rejectValue("arrivalSiteID", "arrivalSiteID", "Cơ sở điều trị chuyển gửi không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getArrivalSiteID()) && StringUtils.isEmpty(form.getArrivalSite()) && errors.getFieldError("arrivalSite") == null) {
            errors.rejectValue("arrivalSite", "arrivalSite", "Cơ sở điều trị chuyển gửi không được để trống");
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

package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.form.htc.HtcTransferTreatmentForm;
import com.gms.entity.form.opc_arv.OpcStageForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class HtcTransferTreatmentValidate implements Validator {
    protected static final String FORMATDATE = "dd/MM/yyyy";
    
    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcTransferTreatmentForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HtcTransferTreatmentForm form = (HtcTransferTreatmentForm) o;
        Date currentDate = TextUtils.convertDate(TextUtils.formatDate(new Date(), FORMATDATE), FORMATDATE);
        
        if(StringUtils.isEmpty(form.getExchangeConsultTime()) || "dd/mm/yyyy".equals(form.getExchangeConsultTime())){
            errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không được để trống");
        }
        
        if(StringUtils.isEmpty(form.getExchangeTime()) || "dd/mm/yyyy".equals(form.getExchangeTime())){
            errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không được để trống");
        }
        
        if(StringUtils.isNotEmpty(form.getExchangeConsultTime()) && !"dd/mm/yyyy".equals(form.getExchangeConsultTime()) && !isThisDateValid(form.getExchangeConsultTime())){
            errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không hợp lệ");
        }
        
        if(StringUtils.isNotEmpty(form.getExchangeTime()) && !"dd/mm/yyyy".equals(form.getExchangeTime()) && !isThisDateValid(form.getExchangeTime())){
            errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không hợp lệ");
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && isThisDateValid(form.getExchangeConsultTime()) && errors.getFieldError("exchangeConsultTime") == null) {
            Date exchangeConsultTime = TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE);
            if ((exchangeConsultTime).compareTo(currentDate) > 0) {
                errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không được lớn hơn ngày hiện tại");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && isThisDateValid(form.getExchangeTime()) && errors.getFieldError("exchangeTime") == null) {
            Date exchangeTime = TextUtils.convertDate(form.getExchangeTime(), FORMATDATE);
            if ((exchangeTime).compareTo(currentDate) > 0) {
                errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không được lớn hơn ngày hiện tại");
            }
        }
        
//        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && isThisDateValid(form.getExchangeConsultTime()) && errors.getFieldError("exchangeConsultTime") == null && 
//                StringUtils.isNotEmpty(form.getResultsPatienTime()) && isThisDateValid(form.getResultsPatienTime())) {
//            Date exchangeConsultTime = TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE);
//            Date resultsPatienTime = TextUtils.convertDate(form.getResultsPatienTime(), FORMATDATE);
//            if ((exchangeConsultTime).compareTo(resultsPatienTime) < 0) {
//                errors.rejectValue("exchangeConsultTime", "exchangeConsultTime", "Ngày tư vấn chuyển gửi điều trị ARV không được trước ngày " + form.getResultsPatienTime());
//            }
//        }
        
        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && isThisDateValid(form.getExchangeConsultTime()) && errors.getFieldError("exchangeTime") == null && 
                StringUtils.isNotEmpty(form.getExchangeTime()) && isThisDateValid(form.getExchangeTime())) {
            Date exchangeConsultTime = TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE);
            Date exchangeTime = TextUtils.convertDate(form.getExchangeTime(), FORMATDATE);
            if ((exchangeConsultTime).compareTo(exchangeTime) > 0) {
                errors.rejectValue("exchangeTime", "exchangeTime", "Ngày chuyển gửi không được trước ngày tư vấn chuyển gửi điều trị ARV");
            }
        }
        
        if(StringUtils.isEmpty(form.getPermanentProvinceID())){
            errors.rejectValue("permanentProvinceID", "permanentProvinceID", "Tỉnh/Thành phố không được để trống");
        }
        
        if(StringUtils.isEmpty(form.getPermanentDistrictID())){
            errors.rejectValue("permanentDistrictID", "permanentDistrictID", "Quận/Huyện không được để trống");
        }
        
        if(StringUtils.isEmpty(form.getArrivalSiteID())){
            errors.rejectValue("arrivalSiteID", "arrivalSiteID", "Cơ sở điều trị chuyển gửi không được để trống");
        }
        
        if(StringUtils.isEmpty(form.getArrivalSite()) && "-1".equals(form.getArrivalSiteID())){
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

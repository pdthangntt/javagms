package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.form.opc_arv.OpcTestForm;
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
public class OpcTestValidate implements Validator {

    protected static final String FORMATDATE = "ddMMyyyy";
    protected static final String FORMATDATESLASH = "dd/MM/yyyy";
    @Autowired
    private SiteService siteService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcTestForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        String currentDate = TextUtils.formatDate(new Date(), "ddMMyyyy");
        String minDate = "01011900";
        OpcTestForm form = (OpcTestForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        //Bổ sung ngày 01/06/2020
        if(StringUtils.isNotEmpty(form.getCd4Result())){
            try{
                if(Math.signum(Double.parseDouble(form.getCd4Result())) == -1.0){
                    errors.rejectValue("cd4Result", "cd4Result", "Kết quả xét nghiệm CD4 phải là số và không được âm");
                }
            } catch (NumberFormatException ex){
                errors.rejectValue("cd4Result", "cd4Result", "Kết quả xét nghiệm CD4 phải là số và không được âm");
            }
        }
        
        if(form.getLao().equals("1") && StringUtils.isEmpty(form.getLaoResult())){
            errors.rejectValue("laoResult", "laoResult", "Kết quả sàng lọc Lao không được để trống");
        }
        
        if(StringUtils.isEmpty(form.getLaoStartTime()) && "1".equals(form.getLaoTreatment()) ){
            if(errors.getFieldError("laoStartTime") == null){
                errors.rejectValue("laoStartTime", "laoStartTime", "Ngày bắt đầu điều trị Lao không được để trống");
            }
        }
        checkDateFormat(form.getLaoStartTime(), "laoStartTime", "Ngày bắt đầu điều trị Lao không hợp lệ", errors, sdfrmt);
        checkDateFormat(form.getLaoEndTime(), "laoEndTime", "Ngày kết thúc điều trị Lao không hợp lệ ", errors, sdfrmt);
        dateCompare(form.getLaoStartTime(), currentDate, "laoStartTime", "Ngày bắt đầu điều trị Lao không được lớn hơn ngày hiện tại", errors, FORMATDATE);
        dateCompare(form.getLaoEndTime(), currentDate, "laoEndTime", "Ngày kết thúc điều trị Lao không được lớn hơn ngày hiện tại ", errors, FORMATDATE);
        dateCompare(form.getLaoStartTime(), form.getLaoEndTime(), "laoEndTime", "Ngày kết thúc điều trị Lao không được nhỏ hơn ngày bắt đầu điều trị Lao", errors, FORMATDATE);
            
        //
        if (form.getLao() != null && form.getLao().equals("1")) {
            if (StringUtils.isEmpty(form.getLaoTestTime())) {
                errors.rejectValue("laoTestTime", "laoTestTime", "Ngày sàng lọc lao không được để trống");
            }
            checkDateFormat(form.getLaoTestTime(), "laoTestTime", "Ngày sàng lọc lao không hợp lệ", errors, sdfrmt);
            dateCompare(form.getLaoTestTime(), currentDate, "laoTestTime", "Ngày sàng lọc lao không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getLaoTestTime(), "laoTestTime", "Ngày sàng lọc lao không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (form.getInh() != null && form.getInh().equals("1")) {
            if (StringUtils.isEmpty(form.getInhFromTime())) {
                errors.rejectValue("inhFromTime", "inhFromTime", "Ngày bắt đầu INH không được để trống");
            }
            checkDateFormat(form.getInhFromTime(), "inhFromTime", "Ngày bắt đầu INH không hợp lệ", errors, sdfrmt);
            dateCompare(form.getInhFromTime(), currentDate, "inhFromTime", "Ngày bắt đầu INH không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getInhFromTime(), "inhFromTime", "Ngày bắt đầu INH không được nhỏ hơn 01/01/1900", errors, FORMATDATE);

            checkDateFormat(form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không hợp lệ", errors, sdfrmt);
            dateCompare(form.getInhToTime(), currentDate, "inhToTime", "Ngày kết thúc INH không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getInhFromTime(), form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không được nhỏ hơn Ngày bắt đầu INH", errors, FORMATDATE);
            dateCompare(minDate, form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (form.getNtch() != null && form.getNtch().equals("1")) {
            if (StringUtils.isEmpty(form.getCotrimoxazoleFromTime())) {
                errors.rejectValue("cotrimoxazoleFromTime", "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không được để trống");
            }
            checkDateFormat(form.getCotrimoxazoleFromTime(), "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không hợp lệ", errors, sdfrmt);
            dateCompare(form.getCotrimoxazoleFromTime(), currentDate, "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getCotrimoxazoleFromTime(), "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không được nhỏ hơn 01/01/1900", errors, FORMATDATE);

            checkDateFormat(form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không hợp lệ", errors, sdfrmt);
            dateCompare(form.getCotrimoxazoleToTime(), currentDate, "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getCotrimoxazoleFromTime(), form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không được nhỏ hơn Ngày bắt đầu Cotrimoxazole", errors, FORMATDATE);
            dateCompare(minDate, form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (StringUtils.isNotEmpty(form.getCd4SampleTime())) {
            checkDateFormat(form.getCd4SampleTime(), "cd4SampleTime", "Ngày lấy mẫu không hợp lệ", errors, sdfrmt);
            dateCompare(form.getCd4SampleTime(), currentDate, "cd4SampleTime", "Ngày lấy mẫu không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getCd4SampleTime(), "cd4SampleTime", "Ngày lấy mẫu không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (StringUtils.isNotEmpty(form.getCd4TestTime())) {
            checkDateFormat(form.getCd4TestTime(), "cd4TestTime", "Ngày xét nghiệm không hợp lệ", errors, sdfrmt);
            dateCompare(form.getCd4TestTime(), currentDate, "cd4TestTime", "Ngày xét nghiệm không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getCd4TestTime(), "cd4TestTime", "Ngày xét nghiệm không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
            dateCompare(form.getCd4SampleTime(), form.getCd4TestTime(), "cd4TestTime", "Ngày xét nghiệm không được nhỏ hơn ngày lấy mẫu", errors, FORMATDATE);
            
            if (StringUtils.isNotEmpty(form.getFirstCd4Time()) && errors.getFieldError("cd4TestTime") == null) {
                dateCompare(TextUtils.formatDate(FORMATDATESLASH, FORMATDATE, form.getFirstCd4Time()), form.getCd4TestTime() , "cd4TestTime", String.format("Ngày xét nghiệm không được nhỏ hơn %s", form.getFirstCd4Time()), errors, FORMATDATE);    
            }
        }
        if (form.getCd4TestSiteID() != null && form.getCd4TestSiteID().equals("-1")) {
            if (StringUtils.isEmpty(form.getCd4TestSiteName())) {
                errors.rejectValue("cd4TestSiteName", "cd4TestSiteName", "Nơi xét nghiệm không được để trống");
            }
        }
        if (StringUtils.isNotEmpty(form.getCd4ResultTime())) {
            checkDateFormat(form.getCd4ResultTime(), "cd4ResultTime", "Ngày nhận kết quả không hợp lệ", errors, sdfrmt);
            dateCompare(form.getCd4ResultTime(), currentDate, "cd4ResultTime", "Ngày nhận kết quả không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getCd4ResultTime(), "cd4ResultTime", "Ngày nhận kết quả không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
            dateCompare(form.getCd4TestTime(), form.getCd4ResultTime(), "cd4ResultTime", "Ngày nhận kết quả không được nhỏ hơn ngày xét nghiệm", errors, FORMATDATE);
        }
        if (StringUtils.isNotEmpty(form.getCd4RetryTime())) {
            checkDateFormat(form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không hợp lệ", errors, sdfrmt);
//            dateCompare(form.getCd4RetryTime(), currentDate, "cd4RetryTime", "Ngày hẹn xét nghiệm không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
            dateCompare(form.getCd4TestTime(), form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn ngày xét nghiệm", errors, FORMATDATE);
            dateCompare(form.getCd4ResultTime(), form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn ngày nhận kết quả", errors, FORMATDATE);
        }
        if (form.getHbv() != null && form.getHbv().equals("1")) {
            checkDateFormat(form.getHbvTime(), "hbvTime", "Ngày xét nghiệm HBV không hợp lệ", errors, sdfrmt);
            dateCompare(form.getHbvTime(), currentDate, "hbvTime", "Ngày xét nghiệm HBV không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getHbvTime(), "hbvTime", "Ngày xét nghiệm HBV không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (form.getHcv() != null && form.getHcv().equals("1")) {
            checkDateFormat(form.getHcvTime(), "hcvTime", "Ngày xét nghiệm HCV không hợp lệ", errors, sdfrmt);
            dateCompare(form.getHcvTime(), currentDate, "hcvTime", "Ngày xét nghiệm HCV không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDate, form.getHcvTime(), "hcvTime", "Ngày xét nghiệm HCV không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
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

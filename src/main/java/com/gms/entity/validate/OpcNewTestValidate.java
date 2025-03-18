package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.form.opc_arv.OpcTestForm;
import com.gms.service.OpcArvService;
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
 * @author pdThang
 */
@Component
public class OpcNewTestValidate implements Validator {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    @Autowired
    private OpcArvService opcArvService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcTestForm.class);
    }

    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-]+[^-]$)";

    @Override
    public void validate(Object o, Errors errors) {
        OpcTestForm form = (OpcTestForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = TextUtils.formatDate(new Date(), "dd/MM/yyyy");
        String minDateString = "01/01/1900";
        if (StringUtils.isNotEmpty(form.getNote()) && form.getNote().length() > 500) {
            errors.rejectValue("note", "note.error.message", "Ghi chú không được quá 500 ký tự");
        }

        if ("1".equals(form.getHcv()) && StringUtils.isNotEmpty(form.getHcvCase()) && form.getHcvCase().length() > 255) {
            errors.rejectValue("hcvCase", "hcvCase.error.message", "Lý do XN HCV không được quá 255 ký tự");
        }

        if ("1".equals(form.getHbv()) && StringUtils.isNotEmpty(form.getHbvCase()) && form.getHbvCase().length() > 255) {
            errors.rejectValue("hbvCase", "hbvCase.error.message", "Lý do XN HBV không được quá 255 ký tự");
        }

        if (StringUtils.isNotEmpty(form.getCd4Result()) && form.getCd4Result().length() > 4) {
            errors.rejectValue("cd4Result", "cd4Result.error.message", "Kết quả xét nghiệm không được quá 4 ký tự");
        }

        if ("-1".equals(form.getCd4TestSiteID()) && StringUtils.isNotEmpty(form.getCd4TestSiteName()) && form.getCd4TestSiteName().length() > 255) {
            errors.rejectValue("cd4TestSiteName", "cd4TestSiteName.error.message", "Nơi xét nghiệm không được quá 255 ký tự");
        }

        if ("-1".equals(form.getCd4TestSiteID()) && StringUtils.isNotEmpty(form.getCd4TestSiteName()) && form.getCd4TestSiteName().length() > 255) {
            errors.rejectValue("cd4TestSiteName", "cd4TestSiteName.error.message", "Nơi xét nghiệm không được quá 255 ký tự");
        }

        if ("1".equals(form.getLao()) && StringUtils.isNotEmpty(form.getLaoOtherSymptom()) && form.getLaoOtherSymptom().length() > 255) {
            errors.rejectValue("laoOtherSymptom", "laoOtherSymptom.error.message", "Triệu chứng khác không được quá 255 ký tự");
        }

        if ("1".equals(form.getNtch()) && StringUtils.isNotEmpty(form.getNtchOtherSymptom()) && form.getNtchOtherSymptom().length() > 255) {
            errors.rejectValue("ntchOtherSymptom", "ntchOtherSymptom.error.message", "Triệu chứng khác không được quá 255 ký tự");
        }

        if (StringUtils.isEmpty(form.getStageID())) {
            errors.rejectValue("stageID", "stageID.error.message", "Giai đoạn điều trị không được để trống");
        }
        if (StringUtils.isEmpty(form.getStatusOfTreatmentID())) {
            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị không được để trống");
        }
        //Bổ sung ngày 01/06/2020
        if (StringUtils.isNotEmpty(form.getCd4Result()) && errors.getFieldError("cd4Result") == null) {
            try {
                if (Math.signum(Double.parseDouble(form.getCd4Result())) == -1.0) {
                    errors.rejectValue("cd4Result", "cd4Result.error.message", "Kết quả xét nghiệm CD4 phải là số và không được âm");
                }
            } catch (NumberFormatException ex) {
                errors.rejectValue("cd4Result", "cd4Result.error.message", "Kết quả xét nghiệm CD4 phải là số và không được âm");
            }
        }

//        if (form.getLao().equals("1") && StringUtils.isEmpty(form.getLaoResult())) {
//            errors.rejectValue("laoResult", "laoResult.error.message", "Kết quả sàng lọc Lao không được để trống");
//        }
        if (StringUtils.isEmpty(form.getLaoStartTime()) && "1".equals(form.getLaoTreatment())) {
            if (errors.getFieldError("laoStartTime") == null) {
                errors.rejectValue("laoStartTime", "laoStartTime.error.message", "Ngày bắt đầu điều trị Lao không được để trống");
            }
        }
        checkDateFormat(form.getLaoStartTime(), "laoStartTime", "Ngày bắt đầu điều trị Lao không hợp lệ", errors, sdfrmt);
        checkDateFormat(form.getLaoEndTime(), "laoEndTime", "Ngày kết thúc điều trị Lao không hợp lệ ", errors, sdfrmt);
        checkDateFormat(form.getLaoTestDate(), "laoTestDate", "Ngày xét nghiệm Lao không hợp lệ ", errors, sdfrmt);
        dateCompare(form.getLaoTestTime(), form.getLaoTestDate(), "laoTestDate", "Ngày xét nghiệm Lao phải lớn hơn ngày sàng lọc Lao", errors, FORMATDATE);
        dateCompare(form.getLaoStartTime(), currentDate, "laoStartTime", "Ngày bắt đầu điều trị Lao không được lớn hơn ngày hiện tại", errors, FORMATDATE);
        dateCompare(form.getLaoEndTime(), currentDate, "laoEndTime", "Ngày kết thúc điều trị Lao không được lớn hơn ngày hiện tại ", errors, FORMATDATE);
        dateCompare(form.getLaoStartTime(), form.getLaoEndTime(), "laoEndTime", "Ngày kết thúc điều trị Lao không được nhỏ hơn ngày bắt đầu điều trị Lao", errors, FORMATDATE);

        if (StringUtils.isNotBlank(form.getCotrimoxazoleOtherEndCause()) && form.getCotrimoxazoleOtherEndCause().length() > 255) {
            errors.rejectValue("cotrimoxazoleOtherEndCause", "cotrimoxazoleOtherEndCause.error.message", "Lý do kết thúc không được vượt quá 255 ký tự");
        }

        //
        if (form.getLao() != null && form.getLao().equals("1")) {
            if (StringUtils.isEmpty(form.getLaoTestTime())) {
                errors.rejectValue("laoTestTime", "laoTestTime", "Ngày sàng lọc lao không được để trống");
            }
            checkDateFormat(form.getLaoTestTime(), "laoTestTime", "Ngày sàng lọc lao không hợp lệ", errors, sdfrmt);
            dateCompare(form.getLaoTestTime(), currentDate, "laoTestTime", "Ngày sàng lọc lao không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDateString, form.getLaoTestTime(), "laoTestTime", "Ngày sàng lọc lao không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (form.getInh() != null && form.getInh().equals("1")) {
            if (StringUtils.isEmpty(form.getInhFromTime())) {
                errors.rejectValue("inhFromTime", "inhFromTime", "Ngày bắt đầu INH không được để trống");
            }
            checkDateFormat(form.getInhFromTime(), "inhFromTime", "Ngày bắt đầu INH không hợp lệ", errors, sdfrmt);
            dateCompare(form.getInhFromTime(), currentDate, "inhFromTime", "Ngày bắt đầu INH không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDateString, form.getInhFromTime(), "inhFromTime", "Ngày bắt đầu INH không được nhỏ hơn 01/01/1900", errors, FORMATDATE);

            checkDateFormat(form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không hợp lệ", errors, sdfrmt);
            dateCompare(form.getInhToTime(), currentDate, "inhToTime", "Ngày kết thúc INH không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getInhFromTime(), form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không được nhỏ hơn Ngày bắt đầu INH", errors, FORMATDATE);
            dateCompare(minDateString, form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
//            if (StringUtils.isEmpty(form.getCotrimoxazoleFromTime())) {
//                errors.rejectValue("cotrimoxazoleFromTime", "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không được để trống");
//            }
        checkDateFormat(form.getCotrimoxazoleFromTime(), "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không hợp lệ", errors, sdfrmt);
        dateCompare(form.getCotrimoxazoleFromTime(), currentDate, "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không được lớn hơn ngày hiện tại", errors, FORMATDATE);
        dateCompare(minDateString, form.getCotrimoxazoleFromTime(), "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không được nhỏ hơn 01/01/1900", errors, FORMATDATE);

        checkDateFormat(form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không hợp lệ", errors, sdfrmt);
        dateCompare(form.getCotrimoxazoleToTime(), currentDate, "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không được lớn hơn ngày hiện tại", errors, FORMATDATE);
        dateCompare(form.getCotrimoxazoleFromTime(), form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không được nhỏ hơn Ngày bắt đầu Cotrimoxazole", errors, FORMATDATE);
        dateCompare(minDateString, form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        if (StringUtils.isNotEmpty(form.getCd4SampleTime())) {
            checkDateFormat(form.getCd4SampleTime(), "cd4SampleTime", "Ngày lấy mẫu không hợp lệ", errors, sdfrmt);
            dateCompare(form.getCd4SampleTime(), currentDate, "cd4SampleTime", "Ngày lấy mẫu không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDateString, form.getCd4SampleTime(), "cd4SampleTime", "Ngày lấy mẫu không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (StringUtils.isNotEmpty(form.getCd4TestTime())) {
            checkDateFormat(form.getCd4TestTime(), "cd4TestTime", "Ngày xét nghiệm không hợp lệ", errors, sdfrmt);
            dateCompare(form.getCd4TestTime(), currentDate, "cd4TestTime", "Ngày xét nghiệm không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDateString, form.getCd4TestTime(), "cd4TestTime", "Ngày xét nghiệm không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
            dateCompare(form.getCd4SampleTime(), form.getCd4TestTime(), "cd4TestTime", "Ngày xét nghiệm không được nhỏ hơn ngày lấy mẫu", errors, FORMATDATE);

            if (StringUtils.isNotEmpty(form.getFirstCd4Time()) && errors.getFieldError("cd4TestTime") == null) {
                dateCompare(form.getFirstCd4Time(), form.getCd4TestTime(), "cd4TestTime", String.format("Ngày xét nghiệm không được nhỏ hơn ngày xét nghiệm đầu tiên (%s)", form.getFirstCd4Time()), errors, FORMATDATE);
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
            dateCompare(minDateString, form.getCd4ResultTime(), "cd4ResultTime", "Ngày nhận kết quả không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
            dateCompare(form.getCd4TestTime(), form.getCd4ResultTime(), "cd4ResultTime", "Ngày nhận kết quả không được nhỏ hơn ngày xét nghiệm", errors, FORMATDATE);
        }
        if (StringUtils.isNotEmpty(form.getCd4RetryTime())) {
            checkDateFormat(form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không hợp lệ", errors, sdfrmt);
//            dateCompare(form.getCd4RetryTime(), currentDate, "cd4RetryTime", "Ngày hẹn xét nghiệm không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDateString, form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
            dateCompare(form.getCd4TestTime(), form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn ngày xét nghiệm", errors, FORMATDATE);
            dateCompare(form.getCd4ResultTime(), form.getCd4RetryTime(), "cd4RetryTime", "Ngày hẹn xét nghiệm không được nhỏ hơn ngày nhận kết quả", errors, FORMATDATE);
        }
        if (form.getHbv() != null && form.getHbv().equals("1")) {
            checkDateFormat(form.getHbvTime(), "hbvTime", "Ngày xét nghiệm HBV không hợp lệ", errors, sdfrmt);
            dateCompare(form.getHbvTime(), currentDate, "hbvTime", "Ngày xét nghiệm HBV không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDateString, form.getHbvTime(), "hbvTime", "Ngày xét nghiệm HBV không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
        }
        if (form.getHcv() != null && form.getHcv().equals("1")) {
            checkDateFormat(form.getHcvTime(), "hcvTime", "Ngày xét nghiệm HCV không hợp lệ", errors, sdfrmt);
            dateCompare(form.getHcvTime(), currentDate, "hcvTime", "Ngày xét nghiệm HCV không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(minDateString, form.getHcvTime(), "hcvTime", "Ngày xét nghiệm HCV không được nhỏ hơn 01/01/1900", errors, FORMATDATE);
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
            if (errors.getFieldError(id) == null) {
                errors.rejectValue(id, id + ".error.message", message);
            }
            return false;
        }
        return true;
    }

    // Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, Errors errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.rejectValue(id, id + ".error.message", message);
        }
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
                errors.rejectValue(id, id + ".error.message", message);
            }
        }
        return true;
    }
}

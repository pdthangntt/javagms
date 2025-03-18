package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.form.opc_arv.OpcArvUpdateForm;
import com.gms.service.OpcArvService;
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
 * @author DSNAnh
 */
@Component
public class OpcArvUpdateValidate implements Validator {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    @Autowired
    private OpcArvService opcArvService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcArvUpdateForm.class);
    }

    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/\\*]+[^-]$)";

    @Override
    public void validate(Object o, Errors errors) {
        OpcArvUpdateForm form = (OpcArvUpdateForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = TextUtils.formatDate(new Date(), "dd/MM/yyyy");
        String minDateString = "01/01/1900";
        
        if (StringUtils.isNotEmpty(form.getFirstCd4Result()) && errors.getFieldError("firstCd4Result") == null) {
            validateLength(form.getFirstCd4Result(), 4, "firstCd4Result", "Kết quả XN CD4 đầu tiên không được quá 4 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getPatientPhone()) && errors.getFieldError("patientPhone") == null){
            validateLength(form.getPatientPhone(), 12, "patientPhone", "Số điện thoại không quá 12 ký tự", errors);
        }
        if(StringUtils.isNotEmpty(form.getSupporterPhone()) && errors.getFieldError("supporterPhone") == null){
            validateLength(form.getSupporterPhone(), 12, "supporterPhone", "Số điện thoại không quá 12 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getNote()) && errors.getFieldError("note") == null) {
            validateLength(form.getNote(), 500, "note", "Ghi chú không được quá 500 ký tự", errors);
        }
        
        if ("-1".equals(form.getConfirmSiteID()) && StringUtils.isNotEmpty(form.getConfirmSiteName()) && errors.getFieldError("confirmSiteName") == null) {
            validateLength(form.getConfirmSiteName(), 255, "confirmSiteName", "Nơi XN khẳng định không được quá 255 ký tự", errors);
        }
        
        if (!form.getCurrentCode().equals(form.getCode())) {
            if (StringUtils.isNotEmpty(form.getCode()) && StringUtils.isNotEmpty(form.getCurrentSiteID())) {
                if (opcArvService.checkExistBySiteIDAndCode(Long.parseLong(form.getCurrentSiteID()), form.getCode()) && errors.getFieldError("code") == null) {
                    errors.rejectValue("code", "code.error.message", "Mã bệnh án đã tồn tại");
                }
            }
        }

//        if (StringUtils.isEmpty(form.getStatusOfTreatmentID())) {
//            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị không được để trống");
//        }
        if (StringUtils.isNotEmpty(form.getPermanentAddress()) && errors.getFieldError("permanentAddress") == null) {
            validateLength(form.getPermanentAddress(), 500, "permanentAddress", "Số nhà không quá 500 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getCurrentAddress()) && errors.getFieldError("currentAddress") == null) {
            validateLength(form.getCurrentAddress(), 500, "currentAddress", "Số nhà không quá 500 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getInsuranceNo()) && errors.getFieldError("insuranceNo") == null) {
            validateLength(form.getInsuranceNo(), 50, "insuranceNo", "Số thẻ BHYT không quá 50 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getInsuranceSite()) && errors.getFieldError("insuranceSite") == null) {
            validateLength(form.getInsuranceSite(), 255, "insuranceSite", "Nơi đăng ký KCB ban đầu không quá 255 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getFullName()) && errors.getFieldError("fullName") == null) {
            validateLength(form.getFullName(), 100, "fullName", "Họ và tên không quá 100 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getSupporterName()) && errors.getFieldError("supporterName") == null) {
            validateLength(form.getSupporterName(), 100, "supporterName", "Họ tên người hỗ trợ không được quá 100 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getInsuranceNo()) && errors.getFieldError("insuranceNo") == null && !form.getInsuranceNo().matches(RegexpEnum.HI_CHAR)) {
            errors.rejectValue("insuranceNo", "insuranceNo.error.message", "Số thẻ BHYT chỉ chứa kí tự chữ và số dấu - và _");
        }

        if (StringUtils.isNotEmpty(form.getCode()) && errors.getFieldError("code") == null && !TextUtils.removeDiacritical(form.getCode().trim()).matches(CODE_REGEX)) {
            errors.rejectValue("code", "code.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }

        if (StringUtils.isNotEmpty(form.getConfirmCode()) && errors.getFieldError("confirmCode") == null && !TextUtils.removeDiacritical(form.getConfirmCode().trim()).matches(RegexpEnum.CODE_CONFIRM)) {
            errors.rejectValue("confirmCode", "confirmCode.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }


        if (errors.getFieldError("fullName") == null && StringUtils.isNotEmpty(form.getFullName())
                && !TextUtils.removeDiacritical(form.getFullName().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.rejectValue("fullName", "fullName.error.message", "Họ và tên chỉ chứa kí tự chữ cái");
        }

        if (errors.getFieldError("supporterName") == null && StringUtils.isNotEmpty(form.getSupporterName())
                && !TextUtils.removeDiacritical(form.getSupporterName().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.rejectValue("supporterName", "supporterName.error.message", "Họ tên người hỗ trợ chỉ chứa kí tự chữ cái");
        }

        if (StringUtils.isNotEmpty(form.getIdentityCard()) && !TextUtils.removeDiacritical(form.getIdentityCard().trim()).matches(RegexpEnum.CMND)
                && errors.getFieldError("identityCard") == null) {
            errors.rejectValue("identityCard", "identityCard.error.message", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt/chữ tiếng việt, số âm, số thập phân");
        }
        if (StringUtils.isNotEmpty(form.getIdentityCard()) && errors.getFieldError("identityCard") == null && form.getIdentityCard().length() > 100) {
            errors.rejectValue("identityCard", "identityCard.error.message", "Số CMND không được quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getConfirmCode()) && errors.getFieldError("confirmCode") == null && form.getConfirmCode().length() > 100) {
            errors.rejectValue("confirmCode", "confirmCode.error.message", "Mã XN khẳng định không được quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getInsuranceNo()) && errors.getFieldError("insuranceNo") == null && form.getInsuranceNo().length() > 50) {
            errors.rejectValue("insuranceNo", "insuranceNo.error.message", "Số thẻ BHYT không quá 50 ký tự");
        }
        try {
            Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
            Date minDate = sdfrmt.parse("01/01/1900");
            if ((StringUtils.isEmpty(form.getConfirmTime()) || "dd/mm/yyyy".equals(form.getConfirmTime())) && !form.getIsOtherSite()) {
                if (errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được để trống");
                }
            }
            if ((StringUtils.isNotEmpty(form.getConfirmTime()) && !"dd/mm/yyyy".equals(form.getConfirmTime())) && !form.getIsOtherSite()) {
                checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt);
            }

            checkDateFormat(form.getPcrOneTime(), "pcrOneTime", "Ngày XN PCR lần 1 không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getPcrTwoTime(), "pcrTwoTime", "Ngày XN PCR lần 2 không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getDob(), "dob", "Ngày tháng năm sinh không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getInsuranceExpiry(), "insuranceExpiry", "Ngày hết hạn thẻ BHYT không hợp lệ", errors, sdfrmt);

            checkDateFormat(form.getFirstTreatmentTime(), "firstTreatmentTime", "Ngày ARV đầu tiên không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getFirstCd4Time(), "firstCd4Time", "Ngày XN CD4 đầu tiên không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getFirstViralLoadTime(), "firstViralLoadTime", "Ngày XN TLVR đầu tiên không hợp lệ", errors, sdfrmt);
            
            checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getPregnantEndDate(), "pregnantEndDate", "Ngày kết thúc thai kỳ không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getJoinBornDate(), "joinBornDate", "Ngày dự sinh không hợp lệ", errors, sdfrmt);
            
            
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getPcrOneTime()) && checkDateFormat(form.getPcrOneTime(), "pcrOneTime", "Ngày XN PCR lần 1 không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date pcrOneTime = sdfrmt.parse(form.getPcrOneTime());
                if (confirmTime.compareTo(pcrOneTime) > 0 && errors.getFieldError("pcrOneTime") == null) {
                    errors.rejectValue("pcrOneTime", "pcrOneTime.error.message", "Ngày XN PCR lần 1 không được nhỏ hơn Ngày XN khẳng định");
                }
                if (now.compareTo(pcrOneTime) < 0 && errors.getFieldError("pcrOneTime") == null) {
                    errors.rejectValue("pcrOneTime", "pcrOneTime.error.message", "Ngày XN PCR lần 1 không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getPcrTwoTime()) && checkDateFormat(form.getPcrTwoTime(), "pcrTwoTime", "Ngày XN PCR lần 2 không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date pcrTwoTime = sdfrmt.parse(form.getPcrTwoTime());
                if (confirmTime.compareTo(pcrTwoTime) > 0 && errors.getFieldError("pcrTwoTime") == null) {
                    errors.rejectValue("pcrTwoTime", "pcrTwoTime.error.message", "Ngày XN PCR lần 2 không được nhỏ hơn Ngày XN khẳng định");
                }
            }

            if (StringUtils.isNotEmpty(form.getPcrOneTime()) && checkDateFormat(form.getPcrOneTime(), "pcrOneTime", "Ngày XN PCR lần 1 không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getPcrTwoTime()) && checkDateFormat(form.getPcrTwoTime(), "pcrTwoTime", "Ngày XN PCR lần 2 không hợp lệ", errors, sdfrmt)) {
                Date pcrOneTime = sdfrmt.parse(form.getPcrOneTime());
                Date pcrTwoTime = sdfrmt.parse(form.getPcrTwoTime());
                if (pcrOneTime.compareTo(pcrTwoTime) > 0 && errors.getFieldError("pcrTwoTime") == null) {
                    errors.rejectValue("pcrTwoTime", "pcrTwoTime.error.message", "Ngày XN PCR lần 2 không được nhỏ hơn Ngày XN PCR lần 1");
                }
                if (now.compareTo(pcrTwoTime) < 0 && errors.getFieldError("pcrTwoTime") == null) {
                    errors.rejectValue("pcrTwoTime", "pcrTwoTime.error.message", "Ngày XN PCR lần 2 không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getDob()) && checkDateFormat(form.getDob(), "dob", "Ngày tháng năm sinh không hợp lệ", errors, sdfrmt)) {
                Date dob = sdfrmt.parse(form.getDob());
                if (now.compareTo(dob) < 0 && errors.getFieldError("dob") == null) {
                    errors.rejectValue("dob", "dob.error.message", "Ngày tháng năm sinh không được lớn hơn ngày hiện tại");
                }
                if (minDate.compareTo(dob) > 0 && errors.getFieldError("dob") == null) {
                    errors.rejectValue("dob", "dob.error.message", "Ngày tháng năm sinh không được nhỏ hơn 01/01/1900");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (now.compareTo(confirmTime) < 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày hiện tại");
                }
                if (minDate.compareTo(confirmTime) > 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được nhỏ hơn 01/01/1900");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getRegistrationTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (registrationTime.compareTo(confirmTime) < 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày đăng ký");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTreatmentTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                if (treatmentTime.compareTo(confirmTime) < 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày điều trị ARV");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getFirstTreatmentTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date firstTreatmentTime = sdfrmt.parse(form.getFirstTreatmentTime());
                if (firstTreatmentTime.compareTo(confirmTime) < 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày ARV đầu tiên");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getEndTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date endTime = sdfrmt.parse(form.getEndTime());
                if (endTime.compareTo(confirmTime) < 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày kết thúc");
                }
            }
            dateCompare(form.getFirstTreatmentTime(), currentDate, "firstTreatmentTime", "Ngày ARV không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getConfirmTime(), form.getFirstTreatmentTime(), "firstTreatmentTime", "Ngày ARV đầu tiên không được nhỏ hơn Ngày XN khẳng định", errors, FORMATDATE);
            dateCompare(form.getFirstTreatmentTime(), form.getTreamentTimeValidate(), "firstTreatmentTime", "Ngày ARV đầu tiên phải nhỏ hơn ngày điều trị ARV (" + form.getTreamentTimeValidate() + ")", errors, FORMATDATE);

            checkDateFormat(form.getFirstCd4Time(), "firstCd4Time", "Ngày XN CD4 đầu tiên không hợp lệ", errors, sdfrmt);
            dateCompare(form.getFirstCd4Time(), currentDate, "firstCd4Time", "Ngày XN CD4 đầu tiên không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getFirstCd4Time(), form.getCd4Time(), "firstCd4Time", "Ngày XN CD4 đầu tiên phải nhỏ hơn ngày XN CD4 (" + form.getCd4Time() + ")", errors, FORMATDATE);

            checkDateFormat(form.getFirstViralLoadTime(), "firstViralLoadTime", "Ngày XN TLVR đầu tiên không hợp lệ", errors, sdfrmt);
            dateCompare(form.getFirstViralLoadTime(), currentDate, "firstViralLoadTime", "Ngày XN TLVR đầu tiên không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getFirstViralLoadTime(), form.getViralTimeValidate(), "firstViralLoadTime", "Ngày XN TLVR đầu tiên phải nhỏ hơn ngày XN TLVR (" + form.getViralTimeValidate() + ")", errors, FORMATDATE);

            checkDateFormat(form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không hợp lệ", errors, sdfrmt);
            dateCompare(form.getTranferToTime(), currentDate, "tranferToTime", "Ngày tiếp nhận không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getRegistrationTime(), form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không được nhỏ hơn ngày đăng ký (" + form.getRegistrationTime() +")", errors, FORMATDATE);

            checkDateFormat(form.getDateOfArrival(), "dateOfArrival", "Ngày chuyển gửi không hợp lệ", errors, sdfrmt);
            dateCompare(form.getDateOfArrival(), currentDate, "dateOfArrival", "Ngày chuyển gửi không được lớn hơn ngày hiện tại", errors, FORMATDATE);

            checkDateFormat(form.getFeedbackResultsReceivedTime(), "feedbackResultsReceivedTime", " Ngày phản hồi không hợp lệ", errors, sdfrmt);
            dateCompare(form.getFeedbackResultsReceivedTime(), currentDate, "feedbackResultsReceivedTime", " Ngày phản hồi không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getTranferToTime(), form.getFeedbackResultsReceivedTime(), "feedbackResultsReceivedTime", "Ngày phản hồi không được nhỏ hơn ngày tiếp nhận", errors, FORMATDATE);
            
            checkDateFormat(form.getTranferToTimeOpc(), "tranferToTimeOpc", " Ngày cs chuyến đi tiếp nhận không hợp lệ", errors, sdfrmt);
            dateCompare(form.getTranferToTimeOpc(), currentDate, "tranferToTimeOpc", " Ngày cs chuyến đi tiếp nhận không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getTranferFromTime(), form.getTranferToTimeOpc(), "tranferToTimeOpc", "Ngày cs chuyến đi tiếp nhận không được nhỏ hơn ngày chuyển đi", errors, FORMATDATE);
            
            checkDateFormat(form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", " Ngày cs chuyển đi phản hồi không hợp lệ", errors, sdfrmt);
            dateCompare(form.getFeedbackResultsReceivedTimeOpc(), currentDate, "feedbackResultsReceivedTimeOpc", " Ngày cs chuyển đi phản hồi không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getTranferFromTime(), form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không được nhỏ hơn ngày chuyển đi", errors, FORMATDATE);
            dateCompare(form.getTranferToTimeOpc(), form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không được nhỏ hơn ngày cs chuyển đi tiếp nhận", errors, FORMATDATE);
            
            
            dateCompare(form.getPregnantStartDate(), currentDate, "pregnantStartDate", " Ngày bắt đầu thai kỳ không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getPregnantStartDate(), form.getPregnantEndDate(), "pregnantEndDate", "Ngày kết thúc thai kỳ không được nhỏ hơn ngày bắt đầu thai kỳ", errors, FORMATDATE);
            dateCompare(form.getPregnantStartDate(), form.getJoinBornDate(), "joinBornDate", "Ngày dự sinh không được nhỏ hơn ngày bắt đầu thai kỳ", errors, FORMATDATE);
 
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

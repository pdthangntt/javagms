package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.service.OpcArvService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author pdThaNG
 */
@Component
public class OpcArvVNPTValidate {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    @Autowired
    private OpcArvService opcArvService;

    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/]+[^-]$)";

    public HashMap<String, String> validate(OpcArvEntity form, boolean isPrep) {

        boolean validate = true;
        HashMap<String, String> errors = new HashMap<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        if (isPrep) {
            return errors;
        }

        if (StringUtils.isNotEmpty(form.getFirstCd4Result())) {
            validateLength(form.getFirstCd4Result(), 4, "firstCd4Result", "Kết quả XN CD4 đầu tiên không được quá 4 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getNote())) {
            validateLength(form.getNote(), 500, "note", "Ghi chú không được quá 500 ký tự", errors);
        }

        if ("-1".equals(form.getPatient().getConfirmSiteID()) && StringUtils.isNotEmpty(form.getPatient().getConfirmSiteName())) {
            validateLength(form.getPatient().getConfirmSiteName(), 255, "confirmSiteName", "Nơi XN khẳng định không được quá 255 ký tự", errors);
        }

//        if (!form.getCurrentCode().equals(form.getCode())) {
//            if (StringUtils.isNotEmpty(form.getCode()) && StringUtils.isNotEmpty(form.getCurrentSiteID())) {
//                if (opcArvService.checkExistBySiteIDAndCode(Long.parseLong(form.getCurrentSiteID()), form.getCode()) && errors.get("code") == null) {
//                    errors.put("arv." + "code", "code.error.message", "Mã bệnh án đã tồn tại");
//                }
//            }
//        }
        if (StringUtils.isEmpty(form.getStatusOfTreatmentID())) {
            errors.put("arv." + "statusOfTreatmentID", "Trạng thái điều trị không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getPermanentAddress())) {
            validateLength(form.getPermanentAddress(), 500, "permanentAddress", "Số nhà không quá 500 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getCurrentAddress())) {
            validateLength(form.getCurrentAddress(), 500, "currentAddress", "Số nhà không quá 500 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getInsuranceNo())) {
            validateLength(form.getInsuranceNo(), 50, "insuranceNo", "Số thẻ BHYT không quá 50 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getInsuranceSite())) {
            validateLength(form.getInsuranceSite(), 255, "insuranceSite", "Nơi đăng ký KCB ban đầu không quá 255 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getPatient().getFullName())) {
            validateLength(form.getPatient().getFullName(), 100, "fullName", "Họ và tên không quá 100 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getSupporterName())) {
            validateLength(form.getSupporterName(), 100, "supporterName", "Họ tên người hỗ trợ không được quá 100 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getInsuranceNo()) && !form.getInsuranceNo().matches(RegexpEnum.HI_CHAR)) {
            errors.put("arv." + "insuranceNo", "Số thẻ BHYT chỉ chứa kí tự chữ và số dấu - và _");
        }

//        if (StringUtils.isNotEmpty(form.getCode()) && !TextUtils.removeDiacritical(form.getCode().trim()).matches(CODE_REGEX)) {
//            errors.put("arv." + "code", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
//        }
//        if (StringUtils.isNotEmpty(form.getPatient().getConfirmCode()) && !TextUtils.removeDiacritical(form.getPatient().getConfirmCode().trim()).matches(RegexpEnum.CODE_CONFIRM)) {
//            errors.put("arv." + "confirmCode", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
//        }
//        // Validate patient phone number
//        if (StringUtils.isNotEmpty(form.getPatientPhone())) {
//            form.setPatientPhone(form.getPatientPhone().replaceAll("\\s", ""));
//            if (!form.getPatientPhone().matches(PHONE_NUMBER)) {
//                errors.put("arv." + "patientPhone", "SĐT bệnh nhân không hợp lệ");
//            }
//        }
//
//        if (StringUtils.isNotEmpty(form.getSupporterPhone())) {
//            form.setSupporterPhone(form.getSupporterPhone().replaceAll("\\s", ""));
//            if (!form.getSupporterPhone().matches(PHONE_NUMBER)) {
//                errors.put("arv." + "supporterPhone", "SĐT người hỗ trợ không hợp lệ");
//            }
//        }
//        if (StringUtils.isNotEmpty(form.getPatient().getFullName())
//                && !TextUtils.removeDiacritical(form.getPatient().getFullName().trim()).matches(RegexpEnum.VN_CHAR)) {
//            errors.put("arv." + "fullName", "Họ và tên chỉ chứa kí tự chữ cái");
//        }
        if (StringUtils.isNotEmpty(form.getSupporterName())
                && !TextUtils.removeDiacritical(form.getSupporterName().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.put("arv." + "supporterName", "Họ tên người hỗ trợ chỉ chứa kí tự chữ cái");
        }

        if (StringUtils.isNotEmpty(form.getPatient().getIdentityCard()) && !TextUtils.removeDiacritical(form.getPatient().getIdentityCard().trim()).matches(RegexpEnum.CMND)) {
            errors.put("arv." + "identityCard", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt/chữ tiếng việt, số âm, số thập phân");
        }
        try {
            Date now = new Date();
            Date minDate = sdfrmt.parse("01/01/1900");
            if (!(form.getCode().substring(form.getCode().length() - 1).equals("P") || form.getCode().substring(form.getCode().length() - 1).equals("p"))) {
                if (form.getPatient().getConfirmTime() == null) {
                    errors.put("arv." + "confirmTime", "Ngày XN khẳng định không được để trống");
                }
            }

            if (form.getPcrOneTime() != null && now.compareTo(form.getPcrOneTime()) < 0) {
                errors.put("arv." + "pcrOneTime", "Ngày XN PCR lần 1 không được lớn hơn ngày hiện tại");
            }

            if (form.getPcrOneTime() != null && form.getPcrTwoTime() != null && form.getPcrOneTime().compareTo(form.getPcrTwoTime()) > 0) {
                errors.put("arv." + "pcrTwoTime", "Ngày XN PCR lần 2 không được nhỏ hơn Ngày XN PCR lần 1");
            }
            if (form.getPcrTwoTime() != null && now.compareTo(form.getPcrTwoTime()) < 0) {
                errors.put("arv." + "pcrTwoTime", "Ngày XN PCR lần 2 không được lớn hơn ngày hiện tại");
            }

            if (form.getPatient().getDob() != null && now.compareTo(form.getPatient().getDob()) < 0) {
                errors.put("arv." + "dob", "Ngày tháng năm sinh không được lớn hơn ngày hiện tại");
            }
            if (form.getPatient().getDob() != null && minDate.compareTo(form.getPatient().getDob()) > 0) {
                errors.put("arv." + "dob", "Ngày tháng năm sinh không được nhỏ hơn 01/01/1900");
            }
            if (form.getPatient().getConfirmTime() != null && validate) {
                if (form.getPcrOneTime() != null && form.getPatient().getConfirmTime().compareTo(form.getPcrOneTime()) > 0) {
                    errors.put("arv." + "pcrOneTime", "Ngày XN PCR lần 1 không được nhỏ hơn Ngày XN khẳng định");
                }
                if (form.getPcrTwoTime() != null && form.getPatient().getConfirmTime().compareTo(form.getPcrTwoTime()) > 0) {
                    errors.put("arv." + "pcrTwoTime", "Ngày XN PCR lần 2 không được nhỏ hơn Ngày XN khẳng định");
                }
                if (now.compareTo(form.getPatient().getConfirmTime()) < 0) {
                    errors.put("arv." + "confirmTime", "Ngày XN khẳng định không được lớn hơn ngày hiện tại");
                }
                if (minDate.compareTo(form.getPatient().getConfirmTime()) > 0) {
                    errors.put("arv." + "confirmTime", "Ngày XN khẳng định không được nhỏ hơn 01/01/1900");
                }
                if (form.getRegistrationTime() != null && form.getRegistrationTime().compareTo(form.getPatient().getConfirmTime()) < 0) {
                    errors.put("arv." + "confirmTime", "Ngày XN khẳng định không được lớn hơn ngày đăng ký");
                }
                if (form.getTreatmentTime() != null && form.getTreatmentTime().compareTo(form.getPatient().getConfirmTime()) < 0) {
                    errors.put("arv." + "confirmTime", "Ngày XN khẳng định không được lớn hơn ngày điều trị ARV");
                }
                if (form.getFirstTreatmentTime() != null && form.getFirstTreatmentTime().compareTo(form.getPatient().getConfirmTime()) < 0) {
                    errors.put("arv." + "confirmTime", "Ngày XN khẳng định không được lớn hơn ngày ARV đầu tiên");
                }
                if (form.getEndTime().compareTo(form.getPatient().getConfirmTime()) < 0) {
                    errors.put("arv." + "confirmTime", "Ngày XN khẳng định không được lớn hơn ngày kết thúc");
                }

            }

            if (validate) {
                if (form.getRegistrationTime() == null) {
                    errors.put("arv." + "registrationTime", "Ngày đăng ký(ngay_khoi_lieu_arv) không được để trống");
                }
                dateCompare(form.getFirstTreatmentTime(), now, "firstTreatmentTime", "Ngày ARV không được lớn hơn ngày hiện tại", errors, FORMATDATE);
                dateCompare(form.getPatient().getConfirmTime(), form.getFirstTreatmentTime(), "firstTreatmentTime", "Ngày ARV đầu tiên không được nhỏ hơn Ngày XN khẳng định", errors, FORMATDATE);
                dateCompare(form.getFirstTreatmentTime(), form.getTreatmentTime(), "firstTreatmentTime", "Ngày ARV đầu tiên phải nhỏ hơn ngày điều trị ARV (" + form.getTreatmentTime() + ")", errors, FORMATDATE);
            }

            dateCompare(form.getFirstCd4Time(), now, "firstCd4Time", "Ngày XN CD4 đầu tiên không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getFirstCd4Time(), form.getCd4Time(), "firstCd4Time", "Ngày XN CD4 đầu tiên phải nhỏ hơn ngày XN CD4 (" + form.getCd4Time() + ")", errors, FORMATDATE);

            dateCompare(form.getFirstViralLoadTime(), now, "firstViralLoadTime", "Ngày XN TLVR đầu tiên không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getFirstViralLoadTime(), form.getViralLoadTime(), "firstViralLoadTime", "Ngày XN TLVR đầu tiên phải nhỏ hơn ngày XN TLVR (" + form.getViralLoadTime() + ")", errors, FORMATDATE);

            dateCompare(form.getTranferToTime(), now, "tranferToTime", "Ngày tiếp nhận không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            if (validate) {
                dateCompare(form.getRegistrationTime(), form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không được nhỏ hơn ngày đăng ký (" + form.getRegistrationTime() + ")", errors, FORMATDATE);
            }
            dateCompare(form.getDateOfArrival(), now, "dateOfArrival", "Ngày chuyển gửi không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getDateOfArrival(), form.getTranferToTime(), "dateOfArrival", "Ngày chuyển gửi không được lớn hơn ngày tiếp nhận", errors, FORMATDATE);

            dateCompare(form.getFeedbackResultsReceivedTime(), now, "feedbackResultsReceivedTime", " Ngày phản hồi không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getTranferToTime(), form.getFeedbackResultsReceivedTime(), "feedbackResultsReceivedTime", "Ngày phản hồi không được nhỏ hơn ngày tiếp nhận", errors, FORMATDATE);

            dateCompare(form.getTranferToTimeOpc(), now, "tranferToTimeOpc", " Ngày cs chuyến đi tiếp nhận không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getTranferFromTime(), form.getTranferToTimeOpc(), "tranferToTimeOpc", "Ngày cs chuyến đi tiếp nhận không được nhỏ hơn ngày chuyển đi", errors, FORMATDATE);

            dateCompare(form.getFeedbackResultsReceivedTimeOpc(), now, "feedbackResultsReceivedTimeOpc", " Ngày cs chuyển đi phản hồi không được lớn hơn ngày hiện tại", errors, FORMATDATE);
            dateCompare(form.getTranferFromTime(), form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không được nhỏ hơn ngày chuyển đi", errors, FORMATDATE);
            dateCompare(form.getTranferToTimeOpc(), form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không được nhỏ hơn ngày cs chuyển đi tiếp nhận", errors, FORMATDATE);

        } catch (Exception e) {
        }
        return errors;
    }

// Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, HashMap<String, String> errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.put("arv." + id, message);
        }
    }

    private boolean dateCompare(Date inputForm, Date inputTo, String id, String message, HashMap<String, String> errors, String formatDate) {
        if (inputForm == null || inputTo == null) {
            return false;
        }
        if (inputTo.compareTo(inputForm) < 0) {
            errors.put("arv." + id, message);
        }
        return true;
    }

}

package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.AnswerEnum;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.opc_arv.OpcArvNewForm;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.ParameterService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class OpcArvNewValidate implements Validator {

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    ParameterService parameterService;

    public static final String FORMATDATE = "dd/MM/yyyy";

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcArvNewForm.class);
    }

    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/\\*]+[^-]$)";

    @Override
    public void validate(Object o, Errors errors) {
        OpcArvNewForm form = (OpcArvNewForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        List<ParameterEntity> list = parameterService.getStatusOfTreatment();
        HashMap<String, String> statusOfTreatments = new HashMap<>();
        for (ParameterEntity entity : list) {
            statusOfTreatments.put(entity.getCode(), entity.getValue());
        }
        
        if(StringUtils.isNotEmpty(form.getPatientPhone()) && errors.getFieldError("patientPhone") == null){
            validateLength(form.getPatientPhone(), 12, "patientPhone", "Số điện thoại không quá 12 ký tự", errors);
        }
        if(StringUtils.isNotEmpty(form.getSupporterPhone()) && errors.getFieldError("supporterPhone") == null){
            validateLength(form.getSupporterPhone(), 12, "supporterPhone", "Số điện thoại không quá 12 ký tự", errors);
        }
        if(StringUtils.isNotEmpty(form.getCd4Result()) && errors.getFieldError("cd4Result") == null){
            validateLength(form.getCd4Result(), 4, "cd4Result", "Kết quả XN gần nhất không quá 4 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getFirstCd4Result()) && errors.getFieldError("firstCd4Result") == null){
            validateLength(form.getFirstCd4Result(), 4, "firstCd4Result", "Kết quả XN đầu tiên không quá 4 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getCd4()) && errors.getFieldError("cd4") == null){
            validateLength(form.getCd4(), 4, "cd4", "CD4 hoặc CD4% không quá 4 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getViralLoadResultNumber()) && errors.getFieldError("viralLoadResultNumber") == null){
            validateLength(form.getViralLoadResultNumber(), 50, "viralLoadResultNumber", "Nồng độ virus không quá 50 ký tự", errors);
        }
        
        if("-1".equals(form.getTransferSiteID()) && StringUtils.isNotEmpty(form.getTransferSiteName()) && errors.getFieldError("transferSiteName") == null){
            validateLength(form.getTransferSiteName(), 255, "transferSiteName", "Cơ sở chuyển đi không quá 255 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getDaysReceived()) && errors.getFieldError("daysReceived") == null){
            validateLength(form.getDaysReceived(), 3, "daysReceived", "Số ngày nhận thuốc không quá 3 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getNote()) && errors.getFieldError("note") == null){
            validateLength(form.getNote(), 500, "note", "Ghi chú không quá 500 ký tự", errors);
        } 
        
        if(ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase()) && StringUtils.isNotEmpty(form.getTransferCase()) && errors.getFieldError("transferCase") == null){
            validateLength(form.getTransferCase(), 200, "transferCase", "Lý do chuyển không quá 200 ký tự", errors);
        } 
        
        if(StringUtils.isNotEmpty(form.getExaminationNote()) && errors.getFieldError("examinationNote") == null){
            validateLength(form.getExaminationNote(), 255, "examinationNote", "Các vấn đề khác không quá 255 ký tự", errors);
        }        
                
        
        if("1".equals(form.getNtch()) && StringUtils.isNotEmpty(form.getNtchOtherSymptom())  && errors.getFieldError("ntchOtherSymptom") == null){
            validateLength(form.getNtchOtherSymptom(), 255, "ntchOtherSymptom", "Triệu chứng khác không quá 255 ký tự", errors);
        }
        
        if("1".equals(form.getLao()) && StringUtils.isNotEmpty(form.getLaoOtherSymptom())  && errors.getFieldError("laoOtherSymptom") == null){
            validateLength(form.getLaoOtherSymptom(), 255, "laoOtherSymptom", "Triệu chứng khác không quá 255 ký tự", errors);
        }
        
        if(RegistrationTypeEnum.TRANSFER.getKey().equals(form.getRegistrationType()) && StringUtils.isNotEmpty(form.getSourceCode())  && errors.getFieldError("sourceCode") == null){
            validateLength(form.getSourceCode(), 50, "sourceCode", "Mã BA chuyển đến không quá 50 ký tự", errors);
        }
        
        if("-1".equals(form.getSourceSiteID()) && StringUtils.isNotEmpty(form.getSourceSiteName()) && errors.getFieldError("sourceSiteName") == null){
            validateLength(form.getSourceSiteName(), 255, "sourceSiteName", "Cơ sở chuyển gửi không quá 255 ký tự", errors);
        }
        
        if("-1".equals(form.getConfirmSiteID()) && StringUtils.isNotEmpty(form.getConfirmSiteName()) && errors.getFieldError("confirmSiteName") == null){
            validateLength(form.getConfirmSiteName(), 255, "confirmSiteName", "Nơi XN khẳng định không quá 255 ký tự", errors);
        }
        
        
        if (("1".equals(form.getRegistrationType()) || "3".equals(form.getRegistrationType())) && StringUtils.isNotEmpty(form.getSourceSiteID()) && StringUtils.isEmpty(form.getTranferToTime())) {
            errors.rejectValue("tranferToTime", "tranferToTime.error.message", "Ngày tiếp nhận không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getTransferSiteID()) && form.getTransferSiteID().equals(form.getCurrentSiteID())) {
            errors.rejectValue("transferSiteID", "transferSiteID.error.message", "Cơ sở chuyển đi phải khác cơ sở điều trị hiện tại");
        }

        if(StringUtils.isNotEmpty(form.getPatientWeight()) && form.getPatientWeight().length() > 5){
            validateLength(form.getPatientWeight(), 5, "patientWeight", "Cân nặng không được quá 5 ký tự", errors);
        }
        
        if(StringUtils.isNotEmpty(form.getPatientHeight()) && form.getPatientHeight().length() > 3){
            validateLength(form.getPatientHeight(), 3, "patientHeight", "Chiều cao không được quá 3 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getPatientWeight()) && errors.getFieldError("patientWeight") == null) {
            try {
                if (Math.signum(Double.parseDouble(form.getPatientWeight())) == -1.0) {
                    errors.rejectValue("patientWeight", "patientWeight.error.message", "Cân nặng phải là số dương");
                }
            } catch (NumberFormatException ex) {
                errors.rejectValue("patientWeight", "patientWeight.error.message", "Cân nặng không hợp lệ");
            }
        }

        if (StringUtils.isNotEmpty(form.getPatientHeight()) && errors.getFieldError("patientHeight") == null) {
            try {
                if (Math.signum(Double.parseDouble(form.getPatientHeight())) == -1.0) {
                    errors.rejectValue("patientHeight", "patientHeight.error.message", "Chiều cao phải là số dương");
                }
            } catch (NumberFormatException ex) {
                errors.rejectValue("patientHeight", "patientHeight.error.message", "Chiều cao không hợp lệ");
            }
        }

        if (StringUtils.isNotEmpty(form.getCode()) && StringUtils.isNotEmpty(form.getTherapySiteID()) ) {
            if (opcArvService.checkExistBySiteIDAndCode(Long.parseLong(form.getTherapySiteID()), form.getCode()) && errors.getFieldError("code") == null) {
                errors.rejectValue("code", "code.error.message", "Mã bệnh án đã tồn tại");
            }
        }

        if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && StringUtils.isEmpty(form.getEndCase())) {
            if (StatusOfTreatmentEnum.BO_TRI.getKey().equals(form.getStatusOfTreatmentID())
                    || StatusOfTreatmentEnum.MAT_DAU.getKey().equals(form.getStatusOfTreatmentID())
                    || StatusOfTreatmentEnum.TU_VONG.getKey().equals(form.getStatusOfTreatmentID())) {
                errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc không được để trống");
            }
        }

        if ((StatusOfTreatmentEnum.BO_TRI.getKey().equals(form.getStatusOfTreatmentID())
                || StatusOfTreatmentEnum.MAT_DAU.getKey().equals(form.getStatusOfTreatmentID())
                || StatusOfTreatmentEnum.TU_VONG.getKey().equals(form.getStatusOfTreatmentID())) && StringUtils.isNotEmpty(form.getEndCase())) {
            if (StatusOfTreatmentEnum.BO_TRI.getKey().equals(form.getStatusOfTreatmentID()) && !ArvEndCaseEnum.CANCELLED.getKey().equals(form.getEndCase()) && errors.getFieldError("endCase") == null) {
                errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là Bỏ trị");
            }
            if (StatusOfTreatmentEnum.MAT_DAU.getKey().equals(form.getStatusOfTreatmentID()) && !ArvEndCaseEnum.LOSE_TRACK.getKey().equals(form.getEndCase()) && errors.getFieldError("endCase") == null) {
                errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là Mất dấu");
            }
            if (StatusOfTreatmentEnum.TU_VONG.getKey().equals(form.getStatusOfTreatmentID()) && !ArvEndCaseEnum.DEAD.getKey().equals(form.getEndCase()) && errors.getFieldError("endCase") == null) {
                errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là Tử vong");
            }
            if ((!"2".equals(form.getStatusOfTreatmentID()) && !"3".equals(form.getStatusOfTreatmentID()))
                    && ("3".equals(form.getEndCase()) || "5".equals(form.getEndCase())) && errors.getFieldError("endCase") == null) {
                String message = statusOfTreatments.get(form.getStatusOfTreatmentID());
                errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là " + message);
            }

        }

        if ("0".equals(form.getStatusOfTreatmentID()) && StringUtils.isNotEmpty(form.getEndCase()) && errors.getFieldError("statusOfTreatmentID") == null) {
            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị không phù hợp với Lý do kết thúc");
        }

        if ((StatusOfTreatmentEnum.DS_CHO_DIEU_TRI.getKey().equals(form.getStatusOfTreatmentID()) || StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey().equals(form.getStatusOfTreatmentID()))
                && StringUtils.isNotEmpty(form.getEndCase()) && !ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase()) && !ArvEndCaseEnum.END.getKey().equals(form.getEndCase())) {
            String endCase = ArvEndCaseEnum.CANCELLED.getKey().equals(form.getEndCase()) ? ArvEndCaseEnum.CANCELLED.getLabel() : ArvEndCaseEnum.DEAD.getKey().equals(form.getEndCase()) ? ArvEndCaseEnum.DEAD.getLabel() : ArvEndCaseEnum.LOSE_TRACK.getKey().equals(form.getEndCase()) ? ArvEndCaseEnum.LOSE_TRACK.getLabel() : "";
            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị phải tương ứng với Lý do kết thúc là " + endCase);
        }

        if (StringUtils.isEmpty(form.getCode()) && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã bệnh án không được để trống");
        }

        
        if (form.getCotrimoxazoleEndCauses() != null && form.getCotrimoxazoleEndCauses().size() > 0 && form.getCotrimoxazoleEndCauses().contains("-1") && 
                StringUtils.isNotEmpty(form.getCotrimoxazoleOtherEndCause()) && errors.getFieldError("cotrimoxazoleOtherEndCause") == null) {
            validateLength(form.getCotrimoxazoleOtherEndCause(), 255, "cotrimoxazoleOtherEndCause", "Lý do kết thúc không được vượt quá 255 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getCode()) && errors.getFieldError("code") == null) {
            validateLength(form.getCode(), 50, "code", "Mã bệnh án không quá 50 ký tự", errors);
        }

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
            validateLength(form.getFullName(), 100, "fullName", "Họ và tên không được quá 100 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getSupporterName()) && errors.getFieldError("supporterName") == null) {
            validateLength(form.getSupporterName(), 100, "supporterName", "Họ tên người hỗ trợ không được quá 100 ký tự", errors);
        }

        if (StringUtils.isNotEmpty(form.getInsuranceNo()) && errors.getFieldError("insuranceNo") == null && !form.getInsuranceNo().matches(RegexpEnum.HI_CHAR)) {
            errors.rejectValue("insuranceNo", "insuranceNo.error.message", "Số thẻ BHYT chỉ chứa kí tự chữ và số dấu - và _");
        }

        if (StringUtils.isNotEmpty(form.getCode()) && errors.getFieldError("code") == null && !TextUtils.removeDiacritical(form.getCode().trim()).matches(CODE_REGEX)) {
            errors.rejectValue("code", "code.error.message", "Mã không đúng định dạng, chỉ bao gồm số, chữ, \"-\", \"*\" và \"/\"");
        }

        if (StringUtils.isNotEmpty(form.getConfirmCode()) && errors.getFieldError("confirmCode") == null && !TextUtils.removeDiacritical(form.getConfirmCode().trim()).matches(RegexpEnum.CODE_CONFIRM)) {
            errors.rejectValue("confirmCode", "confirmCode.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }

        if (StringUtils.isEmpty(form.getArvID()) && StringUtils.isNotEmpty(form.getSourceCode()) && errors.getFieldError("sourceCode") == null && !TextUtils.removeDiacritical(form.getSourceCode().trim()).matches(CODE_REGEX)
                && "3".equals(form.getRegistrationType())) {
            errors.rejectValue("sourceCode", "sourceCode.error.message", "Mã không đúng định dạng, chỉ bao gồm số, chữ, \"-\", \"*\" và \"/\"");
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
        try {
            Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
            Date minDate = sdfrmt.parse("01/01/1900");

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (minDate.compareTo(confirmTime) > 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được nhỏ hơn 01/01/1900");
                }
            }

            if ((StringUtils.isEmpty(form.getEndTime()) || "dd/mm/yyyy".equals(form.getEndTime()))
                    && StringUtils.isNotEmpty(form.getEndCase()) && errors.getFieldError("endTime") == null) {
                errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được để trống");
            }

            if (StringUtils.isEmpty(form.getEndCase()) && (StringUtils.isNotEmpty(form.getEndTime())
                    || "dd/mm/yyyy".equals(form.getEndTime())
                    || form.getEndTime().contains("d")
                    || form.getEndTime().contains("m")
                    || form.getEndTime().contains("y")) && errors.getFieldError("endCase") == null) {
                errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc không được để trống");
            }

            
            if (StringUtils.isEmpty(form.getConfirmTime()) || "dd/mm/yyyy".equals(form.getConfirmTime())) {
                if (errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được để trống");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && !"dd/mm/yyyy".equals(form.getConfirmTime())) {
                checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt);
            }
            if (StringUtils.isEmpty(form.getRegistrationTime()) || "dd/mm/yyyy".equals(form.getRegistrationTime())) {
                if (errors.getFieldError("registrationTime") == null) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", "Ngày đăng ký không được để trống");
                }
            }

            if (StringUtils.isNotEmpty(form.getRegistrationTime()) && !"dd/mm/yyyy".equals(form.getRegistrationTime())) {
                checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt);
            }
            checkDateFormat(form.getPcrOneTime(), "pcrOneTime", "Ngày XN PCR lần 1 không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getPcrTwoTime(), "pcrTwoTime", "Ngày XN PCR lần 2 không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getLaoTestTime(), "laoTestTime", "Ngày sàng lọc không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getInhFromTime(), "inhFromTime", "Ngày bắt đầu INH không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getCotrimoxazoleFromTime(), "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám gần nhất không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getAppointmentTime(), "appointmentTime", "Ngày hẹn khám không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getFirstCd4Time(), "firstCd4Time", "Ngày XN đầu tiên không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getCd4Time(), "cd4Time", "Ngày XN gần nhất không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getFirstViralLoadTime(), "firstViralLoadTime", "Ngày XN đầu tiên không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getViralLoadTime(), "viralLoadTime", "Ngày XN gần nhất không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getHbvTime(), "hbvTime", "Ngày xét nghiệm HBV không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getHcvTime(), "hcvTime", "Ngày xét nghiệm HCV không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getFirstTreatmentTime(), "firstTreatmentTime", "Ngày ARV đầu tiên không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getEndTime(), "endTime", "Ngày kết thúc không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getTreatmentStageTime(), "treatmentStageTime", "Ngày biến động không hợp lệ", errors, sdfrmt);

            checkDateFormat(form.getDob(), "dob", "Ngày sinh không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getInsuranceExpiry(), "insuranceExpiry", "Ngày hết hạn thẻ BHYT không hợp lệ", errors, sdfrmt);

            checkDateFormat(form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getDateOfArrival(), "dateOfArrival", "Ngày chuyển gửi theo phiếu không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getTranferToTimeOpc(), "tranferToTimeOpc", "Ngày cs chuyến đi tiếp nhận không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getTranferFromTime(), "tranferFromTime", "Ngày chuyển đi không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getFeedbackResultsReceivedTime(), "feedbackResultsReceivedTime", "Ngày phản hồi không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getLaoStartTime(), "laoStartTime", "Ngày bắt đầu điều trị Lao không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getLaoEndTime(), "laoEndTime", "Ngày kết thúc điều trị Lao không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getQualifiedTreatmentTime(), "qualifiedTreatmentTime", "Ngày đủ tiêu chuẩn điều trị không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getPregnantEndDate(), "pregnantEndDate", "Ngày kết thúc thai kỳ không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getJoinBornDate(), "joinBornDate", "Ngày dự sinh không hợp lệ", errors, sdfrmt);
            checkDateFormat(form.getLaoTestDate(), "laoTestDate", "Ngày xét nghiệm Lao không hợp lệ", errors, sdfrmt);
            
            if (StringUtils.isNotEmpty(form.getFirstCd4Time()) && checkDateFormat(form.getFirstCd4Time(), "firstCd4Time", "Ngày XN đầu tiên không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getCd4Time()) && checkDateFormat(form.getCd4Time(), "cd4Time", "Ngày XN gần nhất không hợp lệ", errors, sdfrmt)) {
                Date firstCd4Time = sdfrmt.parse(form.getFirstCd4Time());
                Date cd4Time = sdfrmt.parse(form.getCd4Time());
                if (firstCd4Time.compareTo(cd4Time) > 0 && errors.getFieldError("cd4Time") == null) {
                    errors.rejectValue("cd4Time", "cd4Time.error.message", "Ngày XN gần nhất không được nhỏ hơn ngày XN đầu tiên");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getLaoTestDate()) && checkDateFormat(form.getLaoTestDate(), "laoTestDate", "Ngày xét nghiệm Lao không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLaoTestTime()) && checkDateFormat(form.getLaoTestTime(), "laoTestTime", "Ngày sàng lọc không hợp lệ", errors, sdfrmt)) {
                Date laoTestDate = sdfrmt.parse(form.getLaoTestDate());
                Date laoTestTime = sdfrmt.parse(form.getLaoTestTime());
                if (laoTestDate.compareTo(laoTestTime) < 0 && errors.getFieldError("laoTestDate") == null) {
                    errors.rejectValue("laoTestDate", "laoTestDate.error.message", "Ngày xét nghiệm Lao phải lớn hơn ngày sàng lọc Lao");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getJoinBornDate()) && checkDateFormat(form.getJoinBornDate(), "joinBornDate", "Ngày dự sinh không hợp lệ", errors, sdfrmt)) {
                Date pregnantStartDate = sdfrmt.parse(form.getPregnantStartDate());
                Date joinBornDate = sdfrmt.parse(form.getJoinBornDate());
                if (joinBornDate.compareTo(pregnantStartDate) < 0 && errors.getFieldError("joinBornDate") == null) {
                    errors.rejectValue("joinBornDate", "joinBornDate.error.message", "Ngày dự sinh không được nhỏ hơn ngày bắt đầu thai kỳ");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getPregnantEndDate()) && checkDateFormat(form.getPregnantEndDate(), "pregnantEndDate", "Ngày kết thúc thai kỳ không hợp lệ", errors, sdfrmt)) {
                Date pregnantStartDate = sdfrmt.parse(form.getPregnantStartDate());
                Date pregnantEndDate = sdfrmt.parse(form.getPregnantEndDate());
                if (pregnantEndDate.compareTo(pregnantStartDate) < 0 && errors.getFieldError("pregnantEndDate") == null) {
                    errors.rejectValue("pregnantEndDate", "pregnantEndDate.error.message", "Ngày kết thúc thai kỳ không được nhỏ hơn ngày bắt đầu thai kỳ");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && checkDateFormat(form.getPregnantStartDate(), "pregnantStartDate", "Ngày bắt đầu thai kỳ không hợp lệ", errors, sdfrmt)) {
                Date pregnantStartDate = sdfrmt.parse(form.getPregnantStartDate());
                if (now.compareTo(pregnantStartDate) < 0 && errors.getFieldError("pregnantStartDate") == null) {
                    errors.rejectValue("pregnantStartDate", "pregnantStartDate.error.message", "Ngày bắt đầu thai kỳ không được lớn hơn ngày hiện tại");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getAppointmentTime()) && checkDateFormat(form.getAppointmentTime(), "appointmentTime", "Ngày hẹn khám không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám gần nhất không hợp lệ", errors, sdfrmt)) {
                Date appointmentTime = sdfrmt.parse(form.getAppointmentTime());
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                if (appointmentTime.compareTo(lastExaminationTime) < 0 && errors.getFieldError("appointmentTime") == null) {
                    errors.rejectValue("appointmentTime", "appointmentTime.error.message", "Ngày hẹn khám phải lớn hơn ngày khám gần nhất");
                }
            }
            
            
            if (StringUtils.isNotEmpty(form.getReceivedWardDate()) && checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt)) {
                Date receivedWardDate = sdfrmt.parse(form.getReceivedWardDate());
                if (now.compareTo(receivedWardDate) < 0 && errors.getFieldError("receivedWardDate") == null) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate.error.message", "Ngày nhận thuốc tại xã không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getTreatmentTime()) && checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getReceivedWardDate()) && checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt)) {
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                Date receivedWardDate = sdfrmt.parse(form.getReceivedWardDate());
                if (receivedWardDate.compareTo(treatmentTime) < 0 && errors.getFieldError("receivedWardDate") == null) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate.error.message", "Ngày nhận thuốc tại xã không được nhỏ hơn ngày điều trị ARV");
                }
            }

            if (StringUtils.isNotEmpty(form.getRegistrationTime()) && checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getReceivedWardDate()) && checkDateFormat(form.getReceivedWardDate(), "receivedWardDate", "Ngày nhận thuốc tại xã không hợp lệ", errors, sdfrmt)) {
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                Date receivedWardDate = sdfrmt.parse(form.getReceivedWardDate());
                if (receivedWardDate.compareTo(registrationTime) < 0 && errors.getFieldError("receivedWardDate") == null) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate.error.message", "Ngày nhận thuốc tại xã không được nhỏ hơn Ngày đăng ký");
                }
            }

            if (StringUtils.isNotEmpty(form.getQualifiedTreatmentTime()) && checkDateFormat(form.getQualifiedTreatmentTime(), "qualifiedTreatmentTime", "Ngày đủ tiêu chuẩn điều trị không hợp lệ", errors, sdfrmt)) {
                Date qualifiedTreatmentTime = sdfrmt.parse(form.getQualifiedTreatmentTime());
                if (now.compareTo(qualifiedTreatmentTime) < 0 && errors.getFieldError("qualifiedTreatmentTime") == null) {
                    errors.rejectValue("qualifiedTreatmentTime", "qualifiedTreatmentTime.error.message", "Ngày đủ tiêu chuẩn điều trị không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getQualifiedTreatmentTime()) && checkDateFormat(form.getQualifiedTreatmentTime(), "qualifiedTreatmentTime", "Ngày đủ tiêu chuẩn điều trị không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date qualifiedTreatmentTime = sdfrmt.parse(form.getQualifiedTreatmentTime());
                if (qualifiedTreatmentTime.compareTo(confirmTime) < 0 && errors.getFieldError("qualifiedTreatmentTime") == null) {
                    errors.rejectValue("qualifiedTreatmentTime", "qualifiedTreatmentTime.error.message", "Ngày đủ tiêu chuẩn điều trị không được nhỏ hơn ngày XN khẳng định");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getTreatmentTime()) && checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getQualifiedTreatmentTime()) && checkDateFormat(form.getQualifiedTreatmentTime(), "qualifiedTreatmentTime", "Ngày đủ tiêu chuẩn điều trị không hợp lệ", errors, sdfrmt)) {
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                Date qualifiedTreatmentTime = sdfrmt.parse(form.getQualifiedTreatmentTime());
                if (qualifiedTreatmentTime.compareTo(treatmentTime) > 0 && errors.getFieldError("qualifiedTreatmentTime") == null) {
                    errors.rejectValue("qualifiedTreatmentTime", "qualifiedTreatmentTime.error.message", "Ngày đủ tiêu chuẩn điều trị không được lớn hơn ngày điều trị ARV");
                }
            }

            if (StringUtils.isNotEmpty(form.getLaoEndTime()) && checkDateFormat(form.getLaoEndTime(), "laoEndTime", "Ngày kết thúc điều trị Lao không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLaoStartTime()) && checkDateFormat(form.getLaoStartTime(), "laoStartTime", "Ngày bắt đầu điều trị Lao không hợp lệ", errors, sdfrmt)) {
                Date laoEndTime = sdfrmt.parse(form.getLaoEndTime());
                Date laoStartTime = sdfrmt.parse(form.getLaoStartTime());
                if (laoStartTime.compareTo(laoEndTime) > 0 && errors.getFieldError("laoEndTime") == null) {
                    errors.rejectValue("laoEndTime", "laoEndTime.error.message", "Ngày kết thúc điều trị Lao không được nhỏ hơn ngày bắt đầu điều trị Lao");
                }
            }

            if (StringUtils.isNotEmpty(form.getLaoEndTime()) && checkDateFormat(form.getLaoEndTime(), "laoEndTime", "Ngày kết thúc điều trị Lao không hợp lệ", errors, sdfrmt)) {
                Date laoEndTime = sdfrmt.parse(form.getLaoEndTime());
                if (now.compareTo(laoEndTime) < 0 && errors.getFieldError("laoEndTime") == null) {
                    errors.rejectValue("laoEndTime", "laoEndTime.error.message", "Ngày kết thúc điều trị Lao không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getLaoStartTime()) && checkDateFormat(form.getLaoStartTime(), "laoStartTime", "Ngày bắt đầu điều trị Lao không hợp lệ", errors, sdfrmt)) {
                Date laoStartTime = sdfrmt.parse(form.getLaoStartTime());
                if (now.compareTo(laoStartTime) < 0 && errors.getFieldError("laoStartTime") == null) {
                    errors.rejectValue("laoStartTime", "laoStartTime.error.message", "Ngày bắt đầu điều trị Lao không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getPcrOneTime()) && checkDateFormat(form.getPcrOneTime(), "pcrOneTime", "Ngày XN PCR lần 1 không hợp lệ", errors, sdfrmt)) {
                Date pcrOneTime = sdfrmt.parse(form.getPcrOneTime());
                if (now.compareTo(pcrOneTime) < 0 && errors.getFieldError("pcrOneTime") == null) {
                    errors.rejectValue("pcrOneTime", "pcrOneTime.error.message", "Ngày XN PCR lần 1 không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getTranferToTime()) && checkDateFormat(form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không hợp lệ", errors, sdfrmt)) {
                Date tranferToTime = sdfrmt.parse(form.getTranferToTime());
                if (now.compareTo(tranferToTime) < 0 && errors.getFieldError("tranferToTime") == null) {
                    errors.rejectValue("tranferToTime", "tranferToTime.error.message", "Ngày tiếp nhận không được lớn hơn ngày hiện tại");
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

            if (StringUtils.isNotEmpty(form.getEndTime()) && checkDateFormat(form.getEndTime(), "endTime", "Ngày kết thúc không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTranferFromTime()) && checkDateFormat(form.getTranferFromTime(), "tranferFromTime", "Ngày chuyển đi không hợp lệ", errors, sdfrmt)) {
                Date endTime = sdfrmt.parse(form.getEndTime());
                Date tranferFromTime = sdfrmt.parse(form.getTranferFromTime());
                if (tranferFromTime.compareTo(endTime) != 0 && errors.getFieldError("tranferFromTime") == null) {
                    errors.rejectValue("tranferFromTime", "tranferFromTime.error.message", "Ngày chuyển đi phải bằng ngày kết thúc");
                }
            }

            if (StringUtils.isNotEmpty(form.getLaoTestTime()) && checkDateFormat(form.getLaoTestTime(), "laoTestTime", "Ngày sàng lọc không hợp lệ", errors, sdfrmt)) {
                Date laoTestTime = sdfrmt.parse(form.getLaoTestTime());
                if (now.compareTo(laoTestTime) < 0 && errors.getFieldError("laoTestTime") == null) {
                    errors.rejectValue("laoTestTime", "laoTestTime.error.message", "Ngày sàng lọc không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isEmpty(form.getInhFromTime()) && "1".equals(form.getInh())) {
                if (errors.getFieldError("inhFromTime") == null) {
                    errors.rejectValue("inhFromTime", "inhFromTime.error.message", "Ngày bắt đầu INH không được để trống");
                }
            }
            
            if (StringUtils.isEmpty(form.getTreatmentStageTime())) {
                if (errors.getFieldError("treatmentStageTime") == null) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime.error.message", "Ngày biến động không được để trống");
                }
            }

            if (StringUtils.isEmpty(form.getLaoStartTime()) && "1".equals(form.getLaoTreatment())) {
                if (errors.getFieldError("laoStartTime") == null) {
                    errors.rejectValue("laoStartTime", "laoStartTime.error.message", "Ngày bắt đầu điều trị Lao không được để trống");
                }
            }

            if (StringUtils.isEmpty(form.getLaoTestTime()) && "1".equals(form.getLao())) {
                if (errors.getFieldError("laoTestTime") == null) {
                    errors.rejectValue("laoTestTime", "laoTestTime.error.message", "Ngày sàng lọc không được để trống");
                }
            }

            if (StringUtils.isEmpty(form.getLaoTestTime()) && "1".equals(form.getLao())) {
                if (errors.getFieldError("laoTestTime") == null) {
                    errors.rejectValue("laoTestTime", "laoTestTime.error.message", "Ngày sàng lọc không được để trống");
                }
            }


            if (StringUtils.isEmpty(form.getTreatmentTime()) && StringUtils.isNotEmpty(form.getStatusOfTreatmentID())
                    && StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey().equals(form.getStatusOfTreatmentID())) {
                if (errors.getFieldError("treatmentTime") == null) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được để trống");
                }
            }

            if (StringUtils.isNotEmpty(form.getInhFromTime()) && checkDateFormat(form.getInhFromTime(), "inhFromTime", "Ngày bắt đầu INH không hợp lệ", errors, sdfrmt)) {
                Date inhFromTime = sdfrmt.parse(form.getInhFromTime());
                if (now.compareTo(inhFromTime) < 0 && errors.getFieldError("inhFromTime") == null) {
                    errors.rejectValue("inhFromTime", "inhFromTime.error.message", "Ngày bắt đầu INH không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getInhToTime()) && checkDateFormat(form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không hợp lệ", errors, sdfrmt)) {
                Date inhToTime = sdfrmt.parse(form.getInhToTime());
                if (now.compareTo(inhToTime) < 0 && errors.getFieldError("inhToTime") == null) {
                    errors.rejectValue("inhToTime", "inhToTime.error.message", "Ngày kết thúc INH không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getInhFromTime()) && checkDateFormat(form.getInhFromTime(), "inhFromTime", "Ngày bắt đầu INH không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getInhToTime()) && checkDateFormat(form.getInhToTime(), "inhToTime", "Ngày kết thúc INH không hợp lệ", errors, sdfrmt)) {
                Date inhFromTime = sdfrmt.parse(form.getInhFromTime());
                Date inhToTime = sdfrmt.parse(form.getInhToTime());
                if (inhFromTime.compareTo(inhToTime) > 0 && errors.getFieldError("inhToTime") == null) {
                    errors.rejectValue("inhToTime", "inhToTime.error.message", "Ngày kết thúc INH không được nhỏ hơn Ngày bắt đầu INH");
                }
            }

            if (StringUtils.isEmpty(form.getHtcID()) && StringUtils.isEmpty(form.getArvID())
                    && StringUtils.isNotEmpty(form.getTranferToTime()) && checkDateFormat(form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getDateOfArrival()) && checkDateFormat(form.getDateOfArrival(), "dateOfArrival", "Ngày chuyển gửi không hợp lệ", errors, sdfrmt)) {
                Date tranferToTime = sdfrmt.parse(form.getTranferToTime());
                Date dateOfArrival = sdfrmt.parse(form.getDateOfArrival());
                if (tranferToTime.compareTo(dateOfArrival) < 0 && errors.getFieldError("dateOfArrival") == null) {
                    errors.rejectValue("dateOfArrival", "dateOfArrival.error.message", "Ngày chuyển gửi không được lớn hơn ngày tiếp nhận");
                }
            }

            if (StringUtils.isNotEmpty(form.getCotrimoxazoleFromTime()) && checkDateFormat(form.getCotrimoxazoleFromTime(), "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không hợp lệ", errors, sdfrmt)) {
                Date cotrimoxazoleFromTime = sdfrmt.parse(form.getCotrimoxazoleFromTime());
                if (now.compareTo(cotrimoxazoleFromTime) < 0 && errors.getFieldError("cotrimoxazoleFromTime") == null) {
                    errors.rejectValue("cotrimoxazoleFromTime", "cotrimoxazoleFromTime.error.message", "Ngày bắt đầu Cotrimoxazole không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getFeedbackResultsReceivedTime()) && checkDateFormat(form.getFeedbackResultsReceivedTime(), "feedbackResultsReceivedTime", "Ngày phản hồi không hợp lệ", errors, sdfrmt)) {
                Date feedbackResultsReceivedTime = sdfrmt.parse(form.getFeedbackResultsReceivedTime());
                if (now.compareTo(feedbackResultsReceivedTime) < 0 && errors.getFieldError("feedbackResultsReceivedTime") == null) {
                    errors.rejectValue("feedbackResultsReceivedTime", "feedbackResultsReceivedTime.error.message", "Ngày phản hồi không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getCotrimoxazoleToTime()) && checkDateFormat(form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không hợp lệ", errors, sdfrmt)) {
                Date cotrimoxazoleToTime = sdfrmt.parse(form.getCotrimoxazoleToTime());
                if (now.compareTo(cotrimoxazoleToTime) < 0 && errors.getFieldError("cotrimoxazoleToTime") == null) {
                    errors.rejectValue("cotrimoxazoleToTime", "cotrimoxazoleToTime.error.message", "Ngày kết thúc Cotrimoxazole không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getCotrimoxazoleFromTime()) && checkDateFormat(form.getCotrimoxazoleFromTime(), "cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getCotrimoxazoleToTime()) && checkDateFormat(form.getCotrimoxazoleToTime(), "cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole không hợp lệ", errors, sdfrmt)) {
                Date cotrimoxazoleFromTime = sdfrmt.parse(form.getCotrimoxazoleFromTime());
                Date cotrimoxazoleToTime = sdfrmt.parse(form.getCotrimoxazoleToTime());
                if (cotrimoxazoleFromTime.compareTo(cotrimoxazoleToTime) > 0 && errors.getFieldError("cotrimoxazoleToTime") == null) {
                    errors.rejectValue("cotrimoxazoleToTime", "cotrimoxazoleToTime.error.message", "Ngày kết thúc Cotrimoxazole không được nhỏ hơn Ngày bắt đầu Cotrimoxazole");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTreatmentTime()) && checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                if (confirmTime.compareTo(treatmentTime) > 0 && errors.getFieldError("treatmentTime") == null) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được nhỏ hơn Ngày XN khẳng định");
                }
                if (now.compareTo(treatmentTime) < 0 && errors.getFieldError("treatmentTime") == null) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám gần nhất không hợp lệ", errors, sdfrmt)) {
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                if (now.compareTo(lastExaminationTime) < 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getFirstCd4Time()) && checkDateFormat(form.getFirstCd4Time(), "firstCd4Time", "Ngày XN đầu tiên không hợp lệ", errors, sdfrmt)) {
                Date firstCd4Time = sdfrmt.parse(form.getFirstCd4Time());
                if (now.compareTo(firstCd4Time) < 0 && errors.getFieldError("firstCd4Time") == null) {
                    errors.rejectValue("firstCd4Time", "firstCd4Time.error.message", "Ngày XN đầu tiên không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getEndTime()) && checkDateFormat(form.getEndTime(), "endTime", "Ngày kết thúc không hợp lệ", errors, sdfrmt)) {
                Date endTime = sdfrmt.parse(form.getEndTime());
                if (now.compareTo(endTime) < 0 && errors.getFieldError("endTime") == null) {
                    errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getCd4Time()) && checkDateFormat(form.getCd4Time(), "cd4Time", "Ngày XN gần nhất không hợp lệ", errors, sdfrmt)) {
                Date cd4Time = sdfrmt.parse(form.getCd4Time());
                if (now.compareTo(cd4Time) < 0 && errors.getFieldError("cd4Time") == null) {
                    errors.rejectValue("cd4Time", "cd4Time.error.message", "Ngày XN gần nhất không được lớn hơn ngày hiện tại");
                }
                if (now.compareTo(cd4Time) < 0 && errors.getFieldError("cd4Time") == null) {
                    errors.rejectValue("cd4Time", "cd4Time.error.message", "Ngày XN gần nhất không được lớn hơn ngày hiện tại");
                }
            }


            if (StringUtils.isNotEmpty(form.getFirstViralLoadTime()) && checkDateFormat(form.getFirstViralLoadTime(), "firstViralLoadTime", "Ngày XN đầu tiên không hợp lệ", errors, sdfrmt)) {
                Date firstViralLoadTime = sdfrmt.parse(form.getFirstViralLoadTime());
                if (now.compareTo(firstViralLoadTime) < 0 && errors.getFieldError("firstViralLoadTime") == null) {
                    errors.rejectValue("firstViralLoadTime", "firstViralLoadTime.error.message", "Ngày XN đầu tiên không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getFirstViralLoadTime()) && checkDateFormat(form.getFirstViralLoadTime(), "firstViralLoadTime", "Ngày XN đầu tiên không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getViralLoadTime()) && checkDateFormat(form.getViralLoadTime(), "viralLoadTime", "Ngày XN gần nhất không hợp lệ", errors, sdfrmt)) {
                Date firstViralLoadTime = sdfrmt.parse(form.getFirstViralLoadTime());
                Date viralLoadTime = sdfrmt.parse(form.getViralLoadTime());
                if (firstViralLoadTime.compareTo(viralLoadTime) > 0 && errors.getFieldError("viralLoadTime") == null) {
                    errors.rejectValue("viralLoadTime", "viralLoadTime.error.message", "Ngày XN gần nhất không được nhỏ hơn Ngày XN đầu tiên");
                }
            }

            if (StringUtils.isNotEmpty(form.getViralLoadTime()) && checkDateFormat(form.getViralLoadTime(), "viralLoadTime", "Ngày XN gần nhất không hợp lệ", errors, sdfrmt)) {
                Date viralLoadTime = sdfrmt.parse(form.getViralLoadTime());
                if (now.compareTo(viralLoadTime) < 0 && errors.getFieldError("viralLoadTime") == null) {
                    errors.rejectValue("viralLoadTime", "viralLoadTime.error.message", "Ngày XN gần nhất không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getHbvTime()) && checkDateFormat(form.getHbvTime(), "hbvTime", "Ngày xét nghiệm HBV không hợp lệ", errors, sdfrmt)) {
                Date hbvTime = sdfrmt.parse(form.getHbvTime());
                if (now.compareTo(hbvTime) < 0 && errors.getFieldError("hbvTime") == null) {
                    errors.rejectValue("hbvTime", "hbvTime.error.message", "Ngày xét nghiệm HBV không được lớn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getHcvTime()) && checkDateFormat(form.getHcvTime(), "hcvTime", "Ngày xét nghiệm HCV không hợp lệ", errors, sdfrmt)) {
                Date hcvTime = sdfrmt.parse(form.getHcvTime());
                if (now.compareTo(hcvTime) < 0 && errors.getFieldError("hcvTime") == null) {
                    errors.rejectValue("hcvTime", "hcvTime.error.message", "Ngày xét nghiệm HCV không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getTreatmentTime()) && checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getEndTime()) && checkDateFormat(form.getEndTime(), "endTime", "Ngày kết thúc không hợp lệ", errors, sdfrmt)) {
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                Date endTime = sdfrmt.parse(form.getEndTime());
                if (treatmentTime.compareTo(endTime) > 0 && errors.getFieldError("endTime") == null) {
                    errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được nhỏ hơn Ngày điều trị ARV");
                }
            }

            if (StringUtils.isNotEmpty(form.getTreatmentTime()) && checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám gần nhất không hợp lệ", errors, sdfrmt)) {
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                if (treatmentTime.compareTo(lastExaminationTime) > 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được nhỏ hơn ngày điều trị ARV");
                }
            }

            if (StringUtils.isNotEmpty(form.getEndTime()) && checkDateFormat(form.getEndTime(), "endTime", "Ngày kết thúc không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám gần nhất không hợp lệ", errors, sdfrmt)) {
                Date endTime = sdfrmt.parse(form.getEndTime());
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                if (endTime.compareTo(lastExaminationTime) < 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được lớn hơn ngày kết thúc");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTreatmentStageTime()) && checkDateFormat(form.getTreatmentStageTime(), "treatmentStageTime", "Ngày biến động không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date treatmentStageTime = sdfrmt.parse(form.getTreatmentStageTime());
                if (confirmTime.compareTo(treatmentStageTime) > 0 && errors.getFieldError("treatmentStageTime") == null) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime.error.message", "Ngày biến động không được nhỏ hơn Ngày XN khẳng định");
                }
                if (now.compareTo(treatmentStageTime) < 0 && errors.getFieldError("treatmentStageTime") == null) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime.error.message", "Ngày biến động không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getRegistrationTime()) && checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (confirmTime.compareTo(registrationTime) > 0 && errors.getFieldError("registrationTime") == null) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", "Ngày đăng ký không được nhỏ hơn Ngày XN khẳng định");
                }
                if (now.compareTo(registrationTime) < 0 && errors.getFieldError("registrationTime") == null) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", "Ngày đăng ký không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getFirstTreatmentTime()) && checkDateFormat(form.getFirstTreatmentTime(), "firstTreatmentTime", "Ngày ARV đầu tiên không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date firstTreatmentTime = sdfrmt.parse(form.getFirstTreatmentTime());
                if (confirmTime.compareTo(firstTreatmentTime) > 0 && errors.getFieldError("firstTreatmentTime") == null) {
                    errors.rejectValue("firstTreatmentTime", "firstTreatmentTime.error.message", "Ngày ARV đầu tiên không được nhỏ hơn Ngày XN khẳng định");
                }
                if (now.compareTo(firstTreatmentTime) < 0 && errors.getFieldError("firstTreatmentTime") == null) {
                    errors.rejectValue("firstTreatmentTime", "firstTreatmentTime.error.message", "Ngày ARV đầu tiên không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getDob()) && checkDateFormat(form.getDob(), "dob", "Ngày sinh không hợp lệ", errors, sdfrmt)) {
                Date dob = sdfrmt.parse(form.getDob());
                if (now.compareTo(dob) < 0 && errors.getFieldError("dob") == null) {
                    errors.rejectValue("dob", "dob.error.message", "Ngày sinh không được lớn hơn ngày hiện tại");
                }
                if (minDate.compareTo(dob) > 0 && errors.getFieldError("dob") == null) {
                    errors.rejectValue("dob", "dob.error.message", "Ngày sinh không được nhỏ hơn 01/01/1900");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định không hợp lệ", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (now.compareTo(confirmTime) < 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getDateOfArrival()) && checkDateFormat(form.getDateOfArrival(), "dateOfArrival", "Ngày chuyển gửi không hợp lệ", errors, sdfrmt)) {
                Date dateOfArrival = sdfrmt.parse(form.getDateOfArrival());
                if (now.compareTo(dateOfArrival) < 0 && errors.getFieldError("dateOfArrival") == null) {
                    errors.rejectValue("dateOfArrival", "dateOfArrival.error.message", "Ngày chuyển gửi không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getTranferFromTime()) && checkDateFormat(form.getTranferFromTime(), "tranferFromTime", "Ngày chuyển đi không hợp lệ", errors, sdfrmt)) {
                Date tranferFromTime = sdfrmt.parse(form.getTranferFromTime());
                if (now.compareTo(tranferFromTime) < 0 && errors.getFieldError("tranferFromTime") == null) {
                    errors.rejectValue("tranferFromTime", "tranferFromTime.error.message", "Ngày chuyển đi không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getTranferToTimeOpc()) && checkDateFormat(form.getTranferToTimeOpc(), "tranferToTimeOpc", "Ngày cs chuyển đi tiếp nhận không hợp lệ", errors, sdfrmt)) {
                Date tranferToTimeOpc = sdfrmt.parse(form.getTranferToTimeOpc());
                if (now.compareTo(tranferToTimeOpc) < 0 && errors.getFieldError("tranferToTimeOpc") == null) {
                    errors.rejectValue("tranferToTimeOpc", "tranferToTimeOpc.error.message", "Ngày cs chuyển đi tiếp nhận không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getFeedbackResultsReceivedTimeOpc()) && checkDateFormat(form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không hợp lệ", errors, sdfrmt)) {
                Date feedbackResultsReceivedTimeOpc = sdfrmt.parse(form.getFeedbackResultsReceivedTimeOpc());
                if (now.compareTo(feedbackResultsReceivedTimeOpc) < 0 && errors.getFieldError("feedbackResultsReceivedTimeOpc") == null) {
                    errors.rejectValue("feedbackResultsReceivedTimeOpc", "feedbackResultsReceivedTimeOpc.error.message", "Ngày cs chuyển đi phản hồi không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getTreatmentTime()) && checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getFirstTreatmentTime()) && checkDateFormat(form.getFirstTreatmentTime(), "firstTreatmentTime", "Ngày ARV đầu tiên không hợp lệ", errors, sdfrmt)) {
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                Date firstTreatmentTime = sdfrmt.parse(form.getFirstTreatmentTime());
                if (treatmentTime.compareTo(firstTreatmentTime) < 0 && errors.getFieldError("treatmentTime") == null) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được nhỏ hơn Ngày ARV đầu tiên");
                }
            }

            if (StringUtils.isNotEmpty(form.getRegistrationTime()) && checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getEndTime()) && checkDateFormat(form.getEndTime(), "endTime", "Ngày kết thúc không hợp lệ", errors, sdfrmt)) {
                Date endTime = sdfrmt.parse(form.getEndTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (endTime.compareTo(registrationTime) < 0 && errors.getFieldError("endTime") == null) {
                    errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được nhỏ hơn Ngày đăng ký");
                }
            }

            if (StringUtils.isNotEmpty(form.getRegistrationTime()) && checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám gần nhất không hợp lệ", errors, sdfrmt)) {
                Date lastExaminationTime = sdfrmt.parse(form.getLastExaminationTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (lastExaminationTime.compareTo(registrationTime) < 0 && errors.getFieldError("lastExaminationTime") == null) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được nhỏ hơn ngày đăng ký");
                }
            }

            if (StringUtils.isNotEmpty(form.getTranferFromTime()) && checkDateFormat(form.getTranferFromTime(), "tranferFromTime", "Ngày chuyển đi không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTranferToTimeOpc()) && checkDateFormat(form.getTranferToTimeOpc(), "tranferToTimeOpc", "Ngày cs chuyển đi tiếp nhận không hợp lệ", errors, sdfrmt)) {
                Date tranferFromTime = sdfrmt.parse(form.getTranferFromTime());
                Date tranferToTimeOpc = sdfrmt.parse(form.getTranferToTimeOpc());
                if (tranferToTimeOpc.compareTo(tranferFromTime) < 0 && errors.getFieldError("tranferToTimeOpc") == null) {
                    errors.rejectValue("tranferToTimeOpc", "tranferToTimeOpc.error.message", "Ngày cs chuyển đi tiếp nhận không được nhỏ hơn ngày chuyển đi");
                }
            }

            if (StringUtils.isNotEmpty(form.getTranferFromTime()) && checkDateFormat(form.getTranferFromTime(), "tranferFromTime", "Ngày chuyển đi không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getFeedbackResultsReceivedTimeOpc()) && checkDateFormat(form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không hợp lệ", errors, sdfrmt)) {
                Date tranferFromTime = sdfrmt.parse(form.getTranferFromTime());
                Date feedbackResultsReceivedTimeOpc = sdfrmt.parse(form.getFeedbackResultsReceivedTimeOpc());
                if (feedbackResultsReceivedTimeOpc.compareTo(tranferFromTime) < 0 && errors.getFieldError("feedbackResultsReceivedTimeOpc") == null) {
                    errors.rejectValue("feedbackResultsReceivedTimeOpc", "feedbackResultsReceivedTimeOpc.error.message", "Ngày cs chuyển đi phản hồi không được nhỏ hơn ngày chuyển đi");
                }
            }

            if (StringUtils.isNotEmpty(form.getTranferToTimeOpc()) && checkDateFormat(form.getTranferToTimeOpc(), "tranferToTimeOpc", "Ngày cs chuyển đi tiếp nhận không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getFeedbackResultsReceivedTimeOpc()) && checkDateFormat(form.getFeedbackResultsReceivedTimeOpc(), "feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi không hợp lệ", errors, sdfrmt)) {
                Date tranferToTimeOpc = sdfrmt.parse(form.getTranferToTimeOpc());
                Date feedbackResultsReceivedTimeOpc = sdfrmt.parse(form.getFeedbackResultsReceivedTimeOpc());
                if (feedbackResultsReceivedTimeOpc.compareTo(tranferToTimeOpc) < 0 && errors.getFieldError("feedbackResultsReceivedTimeOpc") == null) {
                    errors.rejectValue("feedbackResultsReceivedTimeOpc", "feedbackResultsReceivedTimeOpc.error.message", "Ngày cs chuyển đi phản hồi không được nhỏ hơn ngày cs chuyển đi tiếp nhận");
                }
            }

            if (StringUtils.isNotEmpty(form.getRegistrationTime()) && checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTranferFromTime()) && checkDateFormat(form.getTranferFromTime(), "tranferFromTime", "Ngày chuyển đi không hợp lệ", errors, sdfrmt)) {
                Date tranferFromTime = sdfrmt.parse(form.getTranferFromTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (tranferFromTime.compareTo(registrationTime) < 0 && errors.getFieldError("tranferFromTime") == null) {
                    errors.rejectValue("tranferFromTime", "tranferFromTime.error.message", "Ngày chuyển đi không được nhỏ hơn ngày đăng ký");
                }
            }

            if (StringUtils.isNotEmpty(form.getRegistrationTime()) && checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTranferToTime()) && checkDateFormat(form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không hợp lệ", errors, sdfrmt)) {
                Date tranferToTime = sdfrmt.parse(form.getTranferToTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (tranferToTime.compareTo(registrationTime) < 0 && errors.getFieldError("tranferToTime") == null) {
                    errors.rejectValue("tranferToTime", "tranferToTime.error.message", "Ngày tiếp nhận không được nhỏ hơn ngày đăng ký");
                }
            }

            if ((StringUtils.isNotEmpty(form.getHtcID()) || StringUtils.isNotEmpty(form.getArvID())) && StringUtils.isNotEmpty(form.getDateOfArrival()) && checkDateFormat(form.getDateOfArrival(), "dateOfArrival", "Ngày chuyển gửi theo phiếu không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTranferToTime()) && checkDateFormat(form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không hợp lệ", errors, sdfrmt)) {
                Date tranferToTime = sdfrmt.parse(form.getTranferToTime());
                Date dateOfArrival = sdfrmt.parse(form.getDateOfArrival());
                if (tranferToTime.compareTo(dateOfArrival) < 0 && errors.getFieldError("tranferToTime") == null) {
                    errors.rejectValue("tranferToTime", "tranferToTime.error.message", "Ngày tiếp nhận không được nhỏ hơn ngày chuyển gửi theo phiếu");
                }
            }

            if (StringUtils.isNotEmpty(form.getFeedbackResultsReceivedTime()) && checkDateFormat(form.getFeedbackResultsReceivedTime(), "feedbackResultsReceivedTime", "Ngày phản hồi không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getTranferToTime()) && checkDateFormat(form.getTranferToTime(), "tranferToTime", "Ngày tiếp nhận không hợp lệ", errors, sdfrmt)) {
                Date tranferToTime = sdfrmt.parse(form.getTranferToTime());
                Date feedbackResultsReceivedTime = sdfrmt.parse(form.getFeedbackResultsReceivedTime());
                if (tranferToTime.compareTo(feedbackResultsReceivedTime) > 0 && errors.getFieldError("feedbackResultsReceivedTime") == null) {
                    errors.rejectValue("feedbackResultsReceivedTime", "feedbackResultsReceivedTime.error.message", "Ngày phản hồi không được nhỏ hơn ngày tiếp nhận");
                }
            }

            if (StringUtils.isNotEmpty(form.getTreatmentTime()) && checkDateFormat(form.getTreatmentTime(), "treatmentTime", "Ngày điều trị ARV không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getRegistrationTime()) && checkDateFormat(form.getRegistrationTime(), "registrationTime", "Ngày đăng ký không hợp lệ", errors, sdfrmt)) {
                Date treatmentTime = sdfrmt.parse(form.getTreatmentTime());
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (treatmentTime.compareTo(registrationTime) < 0 && errors.getFieldError("treatmentTime") == null) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được nhỏ hơn ngày đăng ký");
                }
            }

        } catch (ParseException e) {
        }

        OpcStageEntity stageEntity = new OpcStageEntity();
        stageEntity.setTreatmentTime(TextUtils.convertDate(form.getTreatmentTime(), FORMATDATE));
        stageEntity.setEndTime(TextUtils.convertDate(form.getEndTime(), FORMATDATE));
        stageEntity.setPatientID(form.getPatientID());
        stageEntity.setRegistrationTime(TextUtils.convertDate(form.getRegistrationTime(), FORMATDATE));

        try {
            if (!opcStageService.validateStage(stageEntity)) {
                if (StringUtils.isNotEmpty(form.getRegistrationTime()) && errors.getFieldError("registrationTime") == null) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", "Giai đoạn điều trị không hợp lệ. Vui lòng kiểm tra lại ngày đăng ký");
                }
                if (StringUtils.isNotEmpty(form.getEndTime()) && errors.getFieldError("endTime") == null) {
                    errors.rejectValue("endTime", "endTime.error.message", "Giai đoạn điều trị không hợp lệ. Vui lòng kiểm tra lại ngày kết thúc điều trị");
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(OpcArvNewValidate.class.getName()).log(Level.SEVERE, null, ex);
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
}

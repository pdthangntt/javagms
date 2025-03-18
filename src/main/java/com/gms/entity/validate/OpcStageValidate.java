package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.TreatmentStageEnum;
import com.gms.entity.form.opc_arv.OpcStageForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author TrangBN
 */
@Component
public class OpcStageValidate implements Validator {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/\\*]+[^-]$)";

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(OpcStageForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OpcStageForm form = (OpcStageForm) o;
        Date currentDate = new Date();
        
        Map<String, String> treatmentStatus = new HashMap<>();
        for (StatusOfTreatmentEnum value : StatusOfTreatmentEnum.values()) {
            treatmentStatus.put(value.getKey(), value.getLabel());
        }
        
        if(StringUtils.isNotEmpty(form.getNote()) && errors.getFieldError("note") == null){
            if(form.getNote().length() > 500){
                errors.rejectValue("note", "note.error.message", "Ghi chú không được quá 500 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getTransferCase()) && errors.getFieldError("transferCase") == null && "-1".equals(form.getTransferSiteID()) ){
            if(form.getTransferCase().length() > 255){
                errors.rejectValue("transferCase", "transferCase.error.message", "Lý do chuyển không được quá 255 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getTransferSiteName()) && errors.getFieldError("transferSiteName") == null && "-1".equals(form.getTransferSiteID()) ){
            if(form.getTransferSiteName().length() > 255){
                errors.rejectValue("transferSiteName", "transferSiteName.error.message", "Cơ sở chuyển đi không được quá 255 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getExaminationNote()) && errors.getFieldError("examinationNote") == null){
            if(form.getExaminationNote().length() > 255){
                errors.rejectValue("examinationNote", "examinationNote.error.message", "Các vấn đề khác không được quá 255 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getDaysReceived()) && errors.getFieldError("daysReceived") == null){
            if(form.getDaysReceived().length() > 3){
                errors.rejectValue("daysReceived", "daysReceived.error.message", "Số ngày nhận thuốc không quá 3 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getCd4()) && errors.getFieldError("cd4") == null){
            if(form.getCd4().length() > 4){
                errors.rejectValue("cd4", "cd4.error.message", "CD4 hoặc CD4% không được quá 4 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getSourceSiteName()) && "-1".equals(form.getSourceSiteID())){
            if(form.getSourceSiteName().length() > 255){
                errors.rejectValue("sourceSiteName", "sourceSiteName.error.message", "Cơ sở chuyển gửi không được quá 255 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getSourceCode()) && RegistrationTypeEnum.TRANSFER.getKey().equals(form.getRegistrationType())){
            if(form.getSourceCode().length() > 50){
                errors.rejectValue("sourceCode", "sourceCode.error.message", "Mã BA chuyển đến không được quá 50 ký tự");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getCausesChange()) && 
                form.getOldTreatmentRegimenID() != null && form.getOldTreatmentRegimenStage() != null && 
                form.getTreatmentRegimenID() != null && form.getTreatmentRegimenStage() != null &&
                (!form.getOldTreatmentRegimenID().equals(form.getTreatmentRegimenID()) || !form.getOldTreatmentRegimenStage().equals(form.getTreatmentRegimenStage()))){
            if(form.getCausesChange().length() > 255){
                errors.rejectValue("causesChange", "causesChange.error.message", "Lý do thay đổi bậc phác đồ/phác đồ không được quá 255 ký tự");
            }
        }
        
        String registrationTime = StringUtils.isNotEmpty(form.getRegistrationTime()) ? form.getRegistrationTime().replace("/", "") : form.getRegistrationTime();
        registrationTime = insertString(registrationTime, "/");
        if (StringUtils.isNotEmpty(form.getRegistrationTime()) && !isThisDateValid(registrationTime)) {
            errors.rejectValue("registrationTime", "registrationTime.error.message", "Ngày đăng ký không hợp lệ");
        }
        if (StringUtils.isNotEmpty(form.getRegimenStageDate()) && !isThisDateValid(form.getRegimenStageDate())) {
            errors.rejectValue("regimenStageDate", "regimenStageDate.error.message", "Ngày thay đổi bậc phác đồ không hợp lệ");
        }
        if (StringUtils.isNotEmpty(form.getRegimenDate()) && !isThisDateValid(form.getRegimenDate())) {
            errors.rejectValue("regimenDate", "regimenDate.error.message", "Ngày thay đổi phác đồ không hợp lệ");
        }
        if (StringUtils.isNotEmpty(form.getSupplyMedicinceDate()) && !isThisDateValid(form.getSupplyMedicinceDate())) {
            errors.rejectValue("supplyMedicinceDate", "supplyMedicinceDate.error.message", "Ngày đầu tiên cấp thuốc nhiều tháng không hợp lệ");
        }
        
        if (StringUtils.isNotEmpty(form.getQualifiedTreatmentTime()) && !isThisDateValid(form.getQualifiedTreatmentTime())) {
            errors.rejectValue("qualifiedTreatmentTime", "qualifiedTreatmentTime.error.message", "Ngày đủ tiêu chuẩn điều trị không hợp lệ");
        }
        
        if (StringUtils.isNotEmpty(form.getReceivedWardDate()) && !isThisDateValid(form.getReceivedWardDate())) {
            errors.rejectValue("receivedWardDate", "receivedWardDate.error.message", "Ngày nhận thuốc tại xã không hợp lệ");
        }
        if(StringUtils.isNotEmpty(form.getBaseTreatmentRegimenStage()) && StringUtils.isNotEmpty(form.getTreatmentRegimenStage()) && errors.getFieldError("regimenStageDate") == null &&
                !form.getBaseTreatmentRegimenStage().equals(form.getTreatmentRegimenStage()) && StringUtils.isEmpty(form.getRegimenStageDate())){
            errors.rejectValue("regimenStageDate", "regimenStageDate.error.message", "Ngày thay đổi bậc phác đồ không được để trống");
        }

        if(StringUtils.isNotEmpty(form.getBaseTreatmentRegimenID()) && StringUtils.isNotEmpty(form.getTreatmentRegimenID())  && errors.getFieldError("regimenDate") == null &&
                !form.getBaseTreatmentRegimenID().equals(form.getTreatmentRegimenID()) && StringUtils.isEmpty(form.getRegimenDate())){
            errors.rejectValue("regimenDate", "regimenDate.error.message", "Ngày thay đổi phác đồ không được để trống");
        }
        String confirmTime = StringUtils.isNotEmpty(form.getConfirmTime()) ? form.getConfirmTime().replace("/", "") : form.getConfirmTime();
        confirmTime = TextUtils.formatDate("ddMMyyyy", FORMATDATE, confirmTime);
        
        String firstTreatmentTime = StringUtils.isNotEmpty(form.getFirstTreatmentTime()) ? form.getFirstTreatmentTime().replace("/", "") : form.getFirstTreatmentTime();
        firstTreatmentTime = TextUtils.formatDate("ddMMyyyy", FORMATDATE, firstTreatmentTime);
        
        String treatmentTime = StringUtils.isNotEmpty(form.getTreatmentTime()) ? form.getTreatmentTime().replace("/", "") : form.getTreatmentTime();
        treatmentTime = insertString(treatmentTime, "/");
        if (StringUtils.isNotEmpty(form.getTreatmentTime()) && !isThisDateValid(treatmentTime) && !StatusOfTreatmentEnum.DS_CHO_DIEU_TRI.getKey().equals(form.getStatusOfTreatmentID())) {
            errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không hợp lệ");
        }
        
        String lastExaminationTime = StringUtils.isNotEmpty(form.getLastExaminationTime()) ? form.getLastExaminationTime().replace("/", "") : form.getLastExaminationTime();
        lastExaminationTime = insertString(lastExaminationTime, "/");
        if (StringUtils.isNotEmpty(form.getLastExaminationTime()) && !isThisDateValid(lastExaminationTime)) {
            errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không hợp lệ");
        }
        
        String appointmentTime = StringUtils.isNotEmpty(form.getAppointmentTime()) ? form.getAppointmentTime().replace("/", "") : form.getAppointmentTime();
        appointmentTime = insertString(appointmentTime, "/");
        if (StringUtils.isNotEmpty(form.getAppointmentTime()) && !isThisDateValid(appointmentTime)) {
            errors.rejectValue("appointmentTime", "appointmentTime.error.message", "Ngày hẹn khám không hợp lệ");
        }
        
        String endTime = StringUtils.isNotEmpty(form.getEndTime()) ? form.getEndTime().replace("/", "") : form.getEndTime();
        endTime = insertString(endTime, "/");
        if (StringUtils.isNotEmpty(form.getEndTime()) && !isThisDateValid(endTime)) {
            errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không hợp lệ");
        }
        
        String treatmentStageTime = StringUtils.isNotEmpty(form.getTreatmentStageTime()) ? form.getTreatmentStageTime().replace("/", "") : form.getTreatmentStageTime();
        treatmentStageTime = insertString(treatmentStageTime, "/");
        if (StringUtils.isNotEmpty(form.getTreatmentStageTime()) && !isThisDateValid(treatmentStageTime)) {
            errors.rejectValue("treatmentStageTime", "treatmentStageTime.error.message", "Ngày biến động không hợp lệ");
        }
        if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && !isThisDateValid(form.getPregnantStartDate())) {
            errors.rejectValue("pregnantStartDate", "pregnantStartDate.error.message", "Ngày bắt đầu thai kỳ không hợp lệ");
        }
        if (StringUtils.isNotEmpty(form.getPregnantEndDate()) && !isThisDateValid(form.getPregnantEndDate())) {
            errors.rejectValue("pregnantEndDate", "pregnantEndDate.error.message", "Ngày kết thúc thai kỳ không hợp lệ");
        }
        
        if (StringUtils.isEmpty(form.getSiteID())) {
            errors.rejectValue("siteID", "siteID.error.message", "Cơ sở điều trị không được để trống");
        }
        
        if (StringUtils.isEmpty(form.getRegistrationTime()) && errors.getFieldError("registrationTime") == null) {
            errors.rejectValue("registrationTime", "registrationTime.error.message", "Ngày đăng ký không được để trống");
        }
        
        if (StringUtils.isEmpty(form.getStatusOfTreatmentID())) {
            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())
                && StringUtils.isNotEmpty(form.getEndCase())) {
            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị không phù hợp với Lý do kết thúc");
        }
        
        if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && errors.getFieldError("statusOfTreatmentID") == null
                && (form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DS_CHO_DIEU_TRI.getKey()) || form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey()))
                && StringUtils.isNotEmpty(form.getEndCase()) && !form.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey()) && !form.getEndCase().equals(ArvEndCaseEnum.END.getKey())) {
            String endName = "";
            for (ArvEndCaseEnum value : ArvEndCaseEnum.values()) {
                if (value.getKey().equals(form.getEndCase())) {
                    endName = value.getLabel();
                    break;
                }
            }
            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị phải tương ứng với Lý do kết thúc là " + endName);
        }
        
        if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && errors.getFieldError("endCase") == null
                && StringUtils.isNotEmpty(form.getEndCase())
                && ((form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.BO_TRI.getKey()) && !form.getEndCase().equals(ArvEndCaseEnum.CANCELLED.getKey())) || 
                (form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.TU_VONG.getKey()) && !form.getEndCase().equals(ArvEndCaseEnum.DEAD.getKey())) || 
                (form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.MAT_DAU.getKey())) && !form.getEndCase().equals(ArvEndCaseEnum.LOSE_TRACK.getKey())) ) {
            String stageName = "";
            for (StatusOfTreatmentEnum value : StatusOfTreatmentEnum.values()) {
                if (value.getKey().equals(form.getStatusOfTreatmentID())) {
                    stageName = value.getLabel();
                    break;
                }
            }
            errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là " + stageName);
        }
        
        if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && errors.getFieldError("statusOfTreatmentID") == null &&
               StringUtils.isNotEmpty(form.getEndCase()) && 
               form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())) {
            errors.rejectValue("statusOfTreatmentID", "statusOfTreatmentID.error.message", "Trạng thái điều trị không phù hợp với Lý do kết thúc");
        }
        
        if (StringUtils.isEmpty(form.getRegistrationType())) {
            errors.rejectValue("registrationType", "registrationType.error.message", "Loại đăng ký không được để trống");
        }
        
//        if (StringUtils.isNotEmpty(form.getRegistrationType()) && form.getRegistrationType().equals(RegistrationTypeEnum.TRANSFER.getKey()) && StringUtils.isEmpty(form.getSourceSiteID())) {
//            errors.rejectValue("sourceSiteID", "sourceSiteID.error.message", "Cơ sở chuyển gửi không được để trống");
//        }
        
        if (StringUtils.isNotEmpty(form.getSourceCode()) && errors.getFieldError("sourceCode") == null && !TextUtils.removeDiacritical(form.getSourceCode().trim()).matches(CODE_REGEX)) {
            errors.rejectValue("sourceCode", "sourceCode.error.message", "Mã không đúng định dạng, chỉ bao gồm số, chữ, \"-\", \"*\" và \"/\"");
        }
        
//        if (StringUtils.isEmpty(form.getSourceSiteName()) && StringUtils.isNotEmpty(form.getSourceSiteID()) && form.getSourceSiteID().equals("-1")) {
//            errors.rejectValue("sourceSiteName", "sourceSiteName.error.message", "Cơ sở chuyển gửi không được để trống");
//        }
        
        if (StringUtils.isEmpty(form.getTreatmentTime()) && StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && errors.getFieldError("treatmentTime") == null
                && form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) {
            errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được để trống");
        }
        
//        if (StringUtils.isEmpty(form.getTreatmentRegimenStage()) && StringUtils.isNotEmpty(form.getStatusOfTreatmentID())
//                && form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) {
//            errors.rejectValue("treatmentRegimenStage", "treatmentRegimenStage.error.message", "Bậc phác đồ hiện tại không được để trống");
//        }
        
//        if (StringUtils.isEmpty(form.getTreatmentRegimenID()) && StringUtils.isNotEmpty(form.getStatusOfTreatmentID())
//                && form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) {
//            errors.rejectValue("treatmentRegimenID", "treatmentRegimenID.error.message", "Phác đồ hiện tại không được để trống");
//        }
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            if (StringUtils.isNotEmpty(form.getAppointmentTime()) && checkDateFormat(form.getAppointmentTime(), "appointmentTime", "Ngày hẹn khám không hợp lệ", errors, sdfrmt)
                    && StringUtils.isNotEmpty(form.getLastExaminationTime()) && checkDateFormat(form.getLastExaminationTime(), "lastExaminationTime", "Ngày khám gần nhất không hợp lệ", errors, sdfrmt)) {
                Date appointmentTimeValidate = sdfrmt.parse(form.getAppointmentTime());
                Date lastExaminationTimeValidate = sdfrmt.parse(form.getLastExaminationTime());
                if (appointmentTimeValidate.compareTo(lastExaminationTimeValidate) < 0 && errors.getFieldError("appointmentTime") == null) {
                    errors.rejectValue("appointmentTime", "appointmentTime.error.message", "Ngày hẹn khám phải lớn hơn ngày khám gần nhất");
                }
            }
            
            if (errors.getFieldError("registrationTime") == null) {
                if (getDateWithoutTime(TextUtils.convertDate(registrationTime, FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", "Ngày đăng ký không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("receivedWardDate") == null) {
                if (StringUtils.isNotEmpty(form.getReceivedWardDate()) && getDateWithoutTime(TextUtils.convertDate(form.getReceivedWardDate(), FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate.error.message", "Ngày nhận thuốc tại xã không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("pregnantStartDate") == null) {
                if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && getDateWithoutTime(TextUtils.convertDate(form.getPregnantStartDate(), FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("pregnantStartDate", "pregnantStartDate.error.message", "Ngày bắt đầu thai kỳ không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("pregnantEndDate") == null) {
                if (StringUtils.isNotEmpty(form.getPregnantStartDate()) && StringUtils.isNotEmpty(form.getPregnantEndDate()) && getDateWithoutTime(TextUtils.convertDate(form.getPregnantStartDate(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(form.getPregnantEndDate(), FORMATDATE))) > 0) {
                    errors.rejectValue("pregnantEndDate", "pregnantEndDate.error.message", "Ngày kết thúc thai kỳ không được nhỏ hơn ngày bắt đầu thai kỳ");
                }
            }
            
            if (errors.getFieldError("supplyMedicinceDate") == null) {
                if ( StringUtils.isNotEmpty(form.getSupplyMedicinceDate()) && getDateWithoutTime(TextUtils.convertDate(form.getSupplyMedicinceDate(), FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("supplyMedicinceDate", "supplyMedicinceDate.error.message", "Ngày đầu tiên cấp thuốc nhiều tháng không được lớn hơn ngày hiện tại ");
                }
            }
            if (errors.getFieldError("registrationTime") == null) {
                if (TextUtils.convertDate(confirmTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(registrationTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(confirmTime, FORMATDATE))) < 0) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", String.format("Ngày đăng ký không được nhỏ hơn ngày XN khẳng định (%s)", confirmTime));
                }
            }
            if (errors.getFieldError("qualifiedTreatmentTime") == null) {
                if (StringUtils.isNotEmpty(form.getQualifiedTreatmentTime()) && getDateWithoutTime(TextUtils.convertDate(form.getQualifiedTreatmentTime(), FORMATDATE)).compareTo(currentDate) > 0) {
                    errors.rejectValue("qualifiedTreatmentTime", "qualifiedTreatmentTime.error.message", "Ngày đủ tiêu chuẩn điều trị không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("qualifiedTreatmentTime") == null) {
                if (StringUtils.isNotEmpty(form.getQualifiedTreatmentTime()) && TextUtils.convertDate(confirmTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getQualifiedTreatmentTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(confirmTime, FORMATDATE))) < 0) {
                    errors.rejectValue("qualifiedTreatmentTime", "qualifiedTreatmentTime.error.message", String.format("Ngày đủ tiêu chuẩn điều trị không được nhỏ hơn ngày XN khẳng định (%s)", confirmTime));
                }
            }
            if (errors.getFieldError("qualifiedTreatmentTime") == null) {
                if (StringUtils.isNotEmpty(form.getQualifiedTreatmentTime()) && TextUtils.convertDate(treatmentTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getQualifiedTreatmentTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) > 0) {
                    errors.rejectValue("qualifiedTreatmentTime", "qualifiedTreatmentTime.error.message", "Ngày đủ tiêu chuẩn điều trị không được lớn hơn ngày điều trị ARV");
                }
            }
            if (errors.getFieldError("treatmentTime") == null) {
                if (TextUtils.convertDate(treatmentTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE)).compareTo(getDateWithoutTime(currentDate)) > 0) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("treatmentTime") == null) {
                if (TextUtils.convertDate(confirmTime, FORMATDATE) != null && TextUtils.convertDate(treatmentTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(confirmTime, FORMATDATE))) < 0) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", String.format("Ngày điều trị ARV không được nhỏ hơn ngày XN khẳng định (%s)", confirmTime) );
                }
            }
            if (errors.getFieldError("treatmentTime") == null) {
                if (TextUtils.convertDate(firstTreatmentTime, FORMATDATE) != null && TextUtils.convertDate(treatmentTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(firstTreatmentTime, FORMATDATE))) < 0) {
                    errors.rejectValue("treatmentTime", "treatmentTime.error.message", String.format("Ngày điều trị ARV không được nhỏ hơn ngày ARV đầu tiên (%s)", firstTreatmentTime));
                }
            }
            if (errors.getFieldError("lastExaminationTime") == null) {
                if (TextUtils.convertDate(lastExaminationTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(lastExaminationTime, FORMATDATE)).compareTo(getDateWithoutTime(currentDate)) > 0) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("lastExaminationTime") == null) {
                if (TextUtils.convertDate(form.getRegistrationTime(), FORMATDATE) != null && TextUtils.convertDate(lastExaminationTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(lastExaminationTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(form.getRegistrationTime(), FORMATDATE))) < 0) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được nhỏ hơn ngày đăng ký");
                }
            }
            if (errors.getFieldError("supplyMedicinceDate") == null) {
                if (TextUtils.convertDate(form.getRegistrationTime(), FORMATDATE) != null && TextUtils.convertDate(form.getSupplyMedicinceDate(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getSupplyMedicinceDate(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(form.getRegistrationTime(), FORMATDATE))) < 0) {
                    errors.rejectValue("supplyMedicinceDate", "supplyMedicinceDate.error.message", "Ngày đầu tiên cấp thuốc nhiều tháng không được nhỏ hơn ngày đăng ký");
                }
            }
            if (errors.getFieldError("receivedWardDate") == null) {
                if (TextUtils.convertDate(form.getRegistrationTime(), FORMATDATE) != null && TextUtils.convertDate(form.getReceivedWardDate(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getReceivedWardDate(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(form.getRegistrationTime(), FORMATDATE))) < 0) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate.error.message", "Ngày nhận thuốc tại xã không được nhỏ hơn ngày đăng ký");
                }
            }
            
            if (errors.getFieldError("lastExaminationTime") == null) {
                if (TextUtils.convertDate(treatmentTime, FORMATDATE) != null && TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được nhỏ hơn ngày điều trị ARV");
                }
            }
            if (errors.getFieldError("supplyMedicinceDate") == null) {
                if (TextUtils.convertDate(treatmentTime, FORMATDATE) != null && TextUtils.convertDate(form.getSupplyMedicinceDate(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getSupplyMedicinceDate(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
                    errors.rejectValue("supplyMedicinceDate", "supplyMedicinceDate.error.message", "Ngày đầu tiên cấp thuốc nhiều tháng không được nhỏ hơn ngày điều trị ARV");
                }
            }
            if (errors.getFieldError("receivedWardDate") == null) {
                if (TextUtils.convertDate(treatmentTime, FORMATDATE) != null && TextUtils.convertDate(form.getReceivedWardDate(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getReceivedWardDate(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
                    errors.rejectValue("receivedWardDate", "receivedWardDate.error.message", "Ngày nhận thuốc tại xã không được nhỏ hơn ngày điều trị ARV");
                }
            }
            
            if (errors.getFieldError("lastExaminationTime") == null) {
                if (StringUtils.isNotEmpty(endTime) && StringUtils.isNotEmpty(form.getLastExaminationTime()) &&  TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE))) > 0) {
                    errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được lớn hơn ngày kết thúc");
                }
            }
            
            if (errors.getFieldError("endTime") == null) {
                if (TextUtils.convertDate(endTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(currentDate)) > 0) {
                    errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("endTime") == null) {
                if (TextUtils.convertDate(treatmentTime, FORMATDATE) != null && TextUtils.convertDate(endTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
                    errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được nhỏ hơn ngày điều trị ARV");
                }
            }
            // Validate ngày biến dộng không null
            if (errors.getFieldError("treatmentStageTime") == null) {
                if (TextUtils.convertDate(treatmentStageTime, FORMATDATE) == null || StringUtils.isEmpty(treatmentStageTime) ) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime.error.message", "Ngày biến động không được để trống");
                }
            }
            if (errors.getFieldError("treatmentStageTime") == null) {
                if (TextUtils.convertDate(treatmentStageTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentStageTime, FORMATDATE)).compareTo(getDateWithoutTime(currentDate)) > 0) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime.error.message", "Ngày biến động không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("treatmentStageTime") == null) {
                if (TextUtils.convertDate(confirmTime, FORMATDATE) != null && TextUtils.convertDate(treatmentStageTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentStageTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(confirmTime, FORMATDATE))) < 0) {
                    errors.rejectValue("treatmentStageTime", "treatmentStageTime.error.message", String.format("Ngày biến động không được nhỏ hơn ngày XN khẳng định (%s)", confirmTime));
                }
            }
            
            if (errors.getFieldError("treatmentTime") == null && TextUtils.convertDate(registrationTime, FORMATDATE) != null && TextUtils.convertDate(treatmentTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(registrationTime, FORMATDATE))) < 0) {
                errors.rejectValue("treatmentTime", "treatmentTime.error.message", "Ngày điều trị ARV không được nhỏ hơn ngày đăng ký");
            }
            
            if (errors.getFieldError("lastExaminationTime") == null && TextUtils.convertDate(registrationTime, FORMATDATE) != null && StringUtils.isNotEmpty(form.getLastExaminationTime()) && TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(registrationTime, FORMATDATE))) < 0) {
                errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được nhỏ hơn ngày đăng ký");
            }
//            if (errors.getFieldError("lastExaminationTime") == null && TextUtils.convertDate(treatmentTime, FORMATDATE) != null && StringUtils.isNotEmpty(form.getLastExaminationTime()) && TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
//                errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được nhỏ hơn ngày điều trị ARV");
//            }
            if (errors.getFieldError("lastExaminationTime") == null && TextUtils.convertDate(endTime, FORMATDATE) != null && StringUtils.isNotEmpty(form.getLastExaminationTime()) && TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(form.getLastExaminationTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE))) > 0) {
                errors.rejectValue("lastExaminationTime", "lastExaminationTime.error.message", "Ngày khám gần nhất không được lớn hơn ngày kết thúc");
            }
            
            if (errors.getFieldError("endTime") == null && TextUtils.convertDate(endTime, FORMATDATE) != null && TextUtils.convertDate(treatmentTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(treatmentTime, FORMATDATE))) < 0) {
                errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được nhỏ hơn ngày điều trị ARV");
            }
            
            if (errors.getFieldError("endTime") == null && TextUtils.convertDate(endTime, FORMATDATE) != null && TextUtils.convertDate(registrationTime, FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(endTime, FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(registrationTime, FORMATDATE))) < 0) {
                errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được nhỏ hơn ngày đăng ký");
            }
            
            if (StringUtils.isNotEmpty(form.getBirthPlanDate()) && !isThisDateValid(form.getBirthPlanDate())) {
                errors.rejectValue("birthPlanDate", "birthPlanDate.error.message", "Ngày dự sinh không hợp lệ");
            }
            if (errors.getFieldError("birthPlanDate") == null) {
                if (StringUtils.isNotEmpty(form.getBirthPlanDate()) && StringUtils.isNotEmpty(form.getPregnantStartDate()) && getDateWithoutTime(TextUtils.convertDate(form.getBirthPlanDate(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(form.getPregnantStartDate(), FORMATDATE))) < 0) {
                    errors.rejectValue("birthPlanDate", "birthPlanDate.error.message", "Ngày dự sinh không được nhỏ hơn ngày bắt đầu thai kỳ");
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(OpcStageValidate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (StringUtils.isNotEmpty(form.getDaysReceived())  && errors.getFieldError("daysReceived") == null) {
            try {
                if (Integer.parseInt(form.getDaysReceived()) < 0) {
                    errors.rejectValue("daysReceived", "daysReceived.error.message", "Số ngày nhận thuốc không hợp lệ");
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("daysReceived", "daysReceived.error.message", "Số ngày nhận thuốc không hợp lệ");
            }
        }
        
        if (errors.getFieldError("endTime") == null && StringUtils.isNotEmpty(form.getEndCase()) && StringUtils.isEmpty(form.getEndTime())) {
            errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getEndTime()) && StringUtils.isEmpty(form.getEndCase())) {
            errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && (form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.BO_TRI.getKey()  ) 
                || form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.TU_VONG.getKey()) || form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.MAT_DAU.getKey())) 
                && StringUtils.isEmpty(form.getEndCase()) && errors.getFieldError("endCase") == null ) {
            errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getEndCase()) && form.getEndCase().equals(TreatmentStageEnum.MOVED_AWAY.getKey()) && errors.getFieldError("endCase") == null) {
            if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && !form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey()) && !form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DS_CHO_DIEU_TRI.getKey())) {
                errors.rejectValue("endCase", "endCase.error.message", "Bệnh nhân có trạng thái điều trị là \"" + treatmentStatus.get(form.getStatusOfTreatmentID()) + "\" không được chuyển gửi sang cơ sở khác");
            }
        }
        
//        if (StringUtils.isNotEmpty(form.getStatusOfTreatmentID()) && errors.getFieldError("endCase") == null &&
//               StringUtils.isNotEmpty(form.getEndCase()) && (ArvEndCaseEnum.END.getKey().equals(form.getEndCase()) || ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase())) && 
//               !form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey()) && !form.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DS_CHO_DIEU_TRI.getKey()) ) {
//            Map<String, String> treatment = new HashMap<>();
//            for (StatusOfTreatmentEnum stt : StatusOfTreatmentEnum.values()) {
//                treatment.put(stt.getKey(), stt.getLabel());
//            }
//            errors.rejectValue("endCase", "endCase.error.message", "Lý do kết thúc phải tương ứng với Trạng thái điều trị là " + (treatment.get(form.getStatusOfTreatmentID())) );
//        }
        
        if (StringUtils.isEmpty(form.getTransferSiteID()) && ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase())) {
            errors.rejectValue("transferSiteID", "transferSiteID.error.message", "Cơ sở chuyển đi không được để trống");
        }
        
        if (StringUtils.isEmpty(form.getTransferSiteName()) && form.getTransferSiteID() != null && form.getTransferSiteID().equals("-1")) {
            errors.rejectValue("transferSiteName", "transferSiteName.error.message", "Cơ sở chuyển đi không được để trống");
        }
        
        if (StringUtils.isNotEmpty(form.getTransferSiteID()) && form.getCurrentSiteID() == Long.parseLong(form.getTransferSiteID())) {
            errors.rejectValue("transferSiteID", "transferSiteID.error.message", "Không thể chuyển gửi tới cơ sở hiện tại");
        }
    }

    /**
     * Convert sang ngày không có thời gian
     * 
     * @param date
     * @return
     * @throws ParseException 
     */
    public static Date getDateWithoutTime(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
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
    
    /**
     * Chèn chuôi
     * 
     * @param originalString
     * @param stringToBeInserted
     * @param index
     * @return 
     */
    private String insertString(
            String originalString,
            String stringToBeInserted) {

        if (StringUtils.isEmpty(originalString)) {
            return null;
        }
        
        // Create a new string 
        String separeateDate = originalString.substring(0, 1 + 1)
                + stringToBeInserted
                + originalString.substring(1 + 1);
        
        String separeateMonth = separeateDate.substring(0, 4 + 1)
                + stringToBeInserted
                + separeateDate.substring(4 + 1);
        
        // return the modified String 
        return separeateMonth;
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
}

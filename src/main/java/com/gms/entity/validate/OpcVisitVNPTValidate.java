package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.OpcVisitEntity;
import static com.gms.entity.validate.OpcArvVNPTValidate.FORMATDATE;
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
public class OpcVisitVNPTValidate {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    @Autowired
    private OpcArvService opcArvService;

    private static final String PHONE_NUMBER = "(^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$)|(02[0-9])+([0-9]{8}$)";
    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/]+[^-]$)";

    public HashMap<String, String> validate(OpcVisitEntity form, Date treatmentTime, Date registrationTime, Date endTime, String statusOfTreatmentID, boolean isPrep) {

        HashMap<String, String> errors = new HashMap<>();
        if (isPrep) {
            return errors;
        }
        try {
            Date now = new Date();
            try {
                if (form.getPatientWeight() > 9999) {
                    errors.put("visit." + "patientWeight", "Cân nặng không được quá 5 ký tự");
                }
                if (form.getPatientWeight() > 9999) {
                    errors.put("visit." + "patientWeight", "Cân nặng phải là số dương");
                }

            } catch (Exception e) {
                errors.put("visit." + "patientWeight", "Cân nặng không hợp lệ");
            }
            try {

                if (form.getPatientHeight() > 999) {
                    errors.put("visit." + "patientHeight", "Chiều cao phải là số dương");
                }
                if (form.getPatientHeight() > 999) {
                    errors.put("visit." + "patientHeight", "Chiều cao không được quá 3 ký tự");
                }
            } catch (Exception e) {
                errors.put("visit." + "patientHeight", "Chiều cao không hợp lệ");
            }

            validateLength(form.getMsg(), 255, "msg", "Lời dặn đơn thuốc không được quá 255 ký tự", errors);
            validateLength(form.getDiagnostic(), 255, "diagnostic", "Chẩn đoán không được quá 255 ký tự", errors);
            validateLength(form.getClinical(), 220, "clinical", "Lâm sàng không được quá 220 ký tự", errors);
            validateLength(form.getCircuit(), 3, "circuit", "Mạch không được quá 3 ký tự", errors);
            validateLength(form.getBodyTemperature(), 5, "bodyTemperature", "Thân nhiệt không được quá 5 ký tự", errors);
            validateLength(form.getBloodPressure(), 8, "bloodPressure", "Huyết áp không được quá 8 ký tự", errors);

            if (form.getStageID() == null) {
                errors.put("visit." + "stageID", "Giai đoạn điều trị không được để trống");
            }

            if (StringUtils.isEmpty(form.getInsurance())) {
                errors.put("visit." + "insurance", "BN có thẻ BHYT không được để trống");
            }
            try {
                if (form.getDaysReceived() < 0) {
                    errors.put("visit." + "daysReceived", "Số ngày nhận thuốc phải dương");
                }
            } catch (NumberFormatException ex) {
                errors.put("visit." + "daysReceived", "Số ngày nhận thuốc không hợp lệ");
            }

            if (StringUtils.isNotEmpty(form.getInsuranceSite())) {
                validateLength(form.getInsuranceSite(), 255, "insuranceSite", "Nơi ĐK KCB ban đầu không quá 255 ký tự", errors);
            }

            if (StringUtils.isNotEmpty(form.getInsuranceNo())) {
                validateLength(form.getInsuranceNo(), 50, "insuranceNo", "Số thẻ BHYT không quá 50 ký tự", errors);
            }

            if (form.getAppointmentTime() != null && form.getExaminationTime() != null && form.getAppointmentTime().compareTo(form.getExaminationTime()) < 0) {
                errors.put("visit." + "appointmentTime", "Ngày hẹn khám phải lớn hơn ngày khám");
            }
            if (form.getAppointmentTime() != null && form.getExaminationTime() != null && form.getAppointmentTime().compareTo(form.getExaminationTime()) < 0) {
                errors.put("visit." + "appointmentTime", "Ngày hẹn khám phải lớn hơn ngày khám");
            }

            if (form.getPregnantStartDate() != null && now.compareTo(form.getPregnantStartDate()) < 0) {
                errors.put("visit." + "pregnantStartDate", "Ngày bắt đầu thai kỳ không được lớn hơn ngày hiện tại");
            }

            if (form.getPregnantStartDate() != null && form.getPregnantEndDate() != null && form.getPregnantStartDate().compareTo(form.getPregnantEndDate()) > 0) {
                errors.put("visit." + "pregnantEndDate", "Ngày kết thúc thai kỳ không được nhỏ hơn ngày bắt đầu thai kỳ");
            }
            if (treatmentTime != null && form.getRegimenDate() != null && treatmentTime.compareTo(form.getRegimenDate()) < 0) {
                errors.put("visit." + "regimenDate", "Ngày thay đổi phác đồ không được lớn hơn ngày điều trị ARV (" + TextUtils.formatDate(treatmentTime, FORMATDATE) + ")");
            }

            if (form.getReceivedWardDate() != null && now.compareTo(form.getReceivedWardDate()) < 0) {
                errors.put("visit." + "receivedWardDate", "Ngày nhận thuốc tại xã không được lớn hơn ngày hiện tại ");
            }
            if (form.getReceivedWardDate() != null && treatmentTime != null && treatmentTime.compareTo(form.getReceivedWardDate()) > 0) {
                errors.put("visit." + "receivedWardDate", "Ngày nhận thuốc tại xã không được nhỏ hơn ngày điều trị ARV (" + TextUtils.formatDate(treatmentTime, FORMATDATE) + ")");
            }

            if (form.getReceivedWardDate() != null && registrationTime != null && registrationTime.compareTo(form.getReceivedWardDate()) > 0) {
                errors.put("visit." + "receivedWardDate", "Ngày nhận thuốc tại xã không được nhỏ hơn ngày đăng ký (" + TextUtils.formatDate(registrationTime, FORMATDATE) + ")");
            }

            if (form.getSupplyMedicinceDate() != null && now.compareTo(form.getSupplyMedicinceDate()) < 0) {
                errors.put("visit." + "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không được lớn hơn ngày hiện tại ");
            }

            if (form.getSupplyMedicinceDate() != null && treatmentTime != null && treatmentTime.compareTo(form.getSupplyMedicinceDate()) > 0) {
                errors.put("visit." + "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không được nhỏ hơn ngày điều trị ARV (" + TextUtils.formatDate(treatmentTime, FORMATDATE) + ")");
            }

            if (form.getSupplyMedicinceDate() != null && registrationTime != null && registrationTime.compareTo(form.getSupplyMedicinceDate()) > 0) {
                errors.put("visit." + "supplyMedicinceDate", "Ngày đầu tiên cấp thuốc nhiều tháng không được nhỏ hơn ngày đăng ký (" + TextUtils.formatDate(registrationTime, FORMATDATE) + ")");
            }
            if (form.getExaminationTime() != null && now.compareTo(form.getExaminationTime()) < 0) {
                errors.put("visit." + "examinationTime", "Ngày khám  không được lớn hơn ngày hiện tại");
            }

            if (form.getExaminationTime() == null) {
                errors.put("visit." + "examinationTime", "Ngày khám  không được để trống");
            }

            if (treatmentTime != null && form.getExaminationTime() != null && treatmentTime.compareTo(form.getExaminationTime()) > 0) {
                errors.put("visit." + "examinationTime", "Ngày khám không được nhỏ hơn ngày điều trị ARV (" + TextUtils.formatDate(treatmentTime, FORMATDATE) + ")");
            }

            if (registrationTime != null && form.getExaminationTime() != null && registrationTime.compareTo(form.getExaminationTime()) > 0) {
                errors.put("visit." + "examinationTime", "Ngày khám không được nhỏ hơn ngày đăng ký (" + TextUtils.formatDate(registrationTime, FORMATDATE) + ")");
            }

            if (endTime != null && form.getExaminationTime() != null && endTime.compareTo(form.getExaminationTime()) < 0) {
                errors.put("visit." + "examinationTime", "Ngày khám không được lớn hơn ngày kết thúc (" + endTime + ")");
            }

//            if (StringUtils.isNotEmpty(statusOfTreatmentID) && StringUtils.isEmpty(form.getTreatmentRegimenID()) && statusOfTreatmentID.equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) {
//                errors.put("visit." + "treatmentRegimenID", "Phác đồ hiện tại không được để trống");
//            }
//
//            if (StringUtils.isNotEmpty(statusOfTreatmentID) && StringUtils.isEmpty(form.getTreatmentRegimenStage()) && statusOfTreatmentID.equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) {
//                errors.put("visit." + "treatmentRegimenStage", "Bậc phác đồ hiện tại không được để trống");
//            }
        } catch (Exception e) {
            errors.put("visit." + "VALIDATE_ERROR", "Lỗi validate");
            e.printStackTrace();
        }

        return errors;
    }
    // Check length for input string

    private void validateLength(String inputValue, int length, String id, String message, HashMap<String, String> errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.put("visit." + id, message);
        }
    }

    private boolean dateCompare(Date inputForm, Date inputTo, String id, String message, HashMap<String, String> errors, String formatDate) {
        if (inputForm == null || inputTo == null) {
            return false;
        }
        if (inputTo.compareTo(inputForm) < 0) {
            errors.put("visit." + id, message);
        }
        return true;
    }

}

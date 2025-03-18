package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.EarlyHivResultEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.form.PacPatientForm;
import com.gms.service.PacPatientService;
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
 * @author pdThang
 */
@Component
public class PacPatientValidate implements Validator {

    private static final String PHONE_NUMBER = "(^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$)|(02[4|5|7|2|6|8|9])+([0-9]{8}$)";
    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/\\*]+[^-]$)";

    @Autowired
    private PacPatientService pacPatientService;

    @Override
    public boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validate(Object o, Errors errors) {
        PacPatientForm form = (PacPatientForm) o;

        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        if (StringUtils.isNotEmpty(form.getOpcCode()) && errors.getFieldError("opcCode") == null) {
            validateLength(form.getOpcCode(), 50, "opcCode", "Mã bệnh án không quá 50 ký tự", errors);
        }
        if (StringUtils.isNotEmpty(form.getOpcCode()) && errors.getFieldError("opcCode") == null && !TextUtils.removeDiacritical(form.getOpcCode().trim()).matches(CODE_REGEX)) {
            errors.rejectValue("opcCode", "opcCode.error.message", "Mã không đúng định dạng, chỉ bao gồm số, chữ, \"-\", \"*\" và \"/\"");
        }

        // Validate patient phone number
        if (StringUtils.isNotEmpty(form.getPatientPhone())) {
            form.setPatientPhone(form.getPatientPhone().replaceAll("\\s", ""));
            if (!form.getPatientPhone().matches(PHONE_NUMBER)) {
                errors.rejectValue("patientPhone", "patientPhone.error.message", "Số điện thoại không hợp lệ");
            }
        }

        // Kiểm tra định ký tự đặc biêt của tên khách hàng
        if (StringUtils.isNotBlank(form.getFullname()) && !TextUtils.removeDiacritical(form.getFullname().trim()).matches(RegexpEnum.VN_CHAR)
                && errors.getFieldError("fullname") == null) {
            errors.rejectValue("fullname", "fullname.error.message", "Họ và tên chỉ chứa các kí tự chữ cái");
        }

        // Validate number format for year of birth
        if (StringUtils.isNotEmpty(form.getYearOfBirth()) && errors.getFieldError("yearOfBirth") == null) {
            if (!form.getYearOfBirth().matches(RegexpEnum.YYYY)) {
                errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }
        // Valdate year of birth in range (1900 - current year)
        if (StringUtils.isNotEmpty(form.getYearOfBirth()) && errors.getFieldError("yearOfBirth") == null) {
            if (Integer.parseInt(form.getYearOfBirth()) < 1900 || Integer.parseInt(form.getYearOfBirth()) > (Calendar.getInstance().get(Calendar.YEAR))) {
                errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }

        // Validate ký tự đặc biệt C2. Mã XN khẳng định:
        if (StringUtils.isNotEmpty(form.getConfirmCode()) && !TextUtils.removeDiacritical(form.getConfirmCode().trim()).matches(RegexpEnum.CODE_CONFIRM) && errors.getFieldError("confirmCode") == null) {
            errors.rejectValue("confirmCode", "confirmCode.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }

        if (StringUtils.isEmpty(form.getConfirmTime())) {
            errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được để trống");
        }

        if (StringUtils.isEmpty(form.getChangeTreatmentDate()) && StringUtils.isNotEmpty(form.getStatusOfChangeTreatmentID())) {
            errors.rejectValue("changeTreatmentDate", "changeTreatmentDate.error.message", "Ngày biến động điều trị không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getEarlyHivTime()) && StringUtils.isEmpty(form.getEarlyHiv()) && errors.getFieldError("earlyHiv") == null) {
            errors.rejectValue("earlyHiv", "earlyHiv.error.message", "KQXN nhiễm mới không được để trống");
        }
        if (StringUtils.isEmpty(form.getEarlyHivTime()) && StringUtils.isNotEmpty(form.getEarlyHiv()) && errors.getFieldError("earlyHivTime") == null) {
            errors.rejectValue("earlyHivTime", "earlyHivTime.error.message", "Ngày XN nhiễm mới không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getVirusLoadConfirmDate()) && StringUtils.isEmpty(form.getVirusLoadConfirm()) && errors.getFieldError("virusLoadConfirm") == null) {
            errors.rejectValue("virusLoadConfirm", "virusLoadConfirm.error.message", "KQXN tải lượng virus không được để trống");
        }
        if (StringUtils.isEmpty(form.getVirusLoadConfirmDate()) && StringUtils.isNotEmpty(form.getVirusLoadConfirm()) && errors.getFieldError("virusLoadConfirmDate") == null) {
            errors.rejectValue("virusLoadConfirmDate", "virusLoadConfirmDate.error.message", "Ngày XN tải lượng virus không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getVirusLoadNumber()) && errors.getFieldError("virusLoadNumber") == null) {
            if (form.getVirusLoadNumber().length() > 9) {
                errors.rejectValue("virusLoadNumber", "virusLoadNumber.error.message", "Nồng độ virus không được quá 9 chữ số");
            }
        }
        if (StringUtils.isNotEmpty(form.getVirusLoadNumber()) && errors.getFieldError("virusLoadNumber") == null) {
            try {
                Integer.valueOf(form.getVirusLoadNumber());
                if (Integer.valueOf(form.getVirusLoadNumber()) < 0) {
                    errors.rejectValue("virusLoadNumber", "virusLoadNumber.error.message", "Nồng độ virus phải là số nguyên dương");
                }
            } catch (Exception e) {
                errors.rejectValue("virusLoadNumber", "virusLoadNumber.error.message", "Nồng độ virus phải là số nguyên dương");
            }

        }

        try {
            if (errors.getFieldError("changeTreatmentDate") == null
                    && StringUtils.isNotEmpty(form.getChangeTreatmentDate()) && StringUtils.isNotEmpty(form.getStatusOfChangeTreatmentID())) {
                checkDateFormat(form.getChangeTreatmentDate(), "changeTreatmentDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
        } catch (Exception e) {
        }

        if (StringUtils.isNotEmpty(form.getConfirmCode()) && StringUtils.isEmpty(form.getSiteConfirmID())) {
            errors.rejectValue("siteConfirmID", "siteConfirmID.error.message", "Tên cơ sở XN khẳng định không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getConfirmCode()) && StringUtils.isEmpty(form.getBloodBaseID())) {
            errors.rejectValue("bloodBaseID", "bloodBaseID.error.message", "Nơi lấy mẫu XN không được để trống");
        }

        // Ngày xét nghiệm
        if (StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
            checkDateFormat(form.getConfirmTime(), "confirmTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
        }
        try {
            if (errors.getFieldError("confirmTime") == null
                    && StringUtils.isNotEmpty(form.getConfirmTime())) {
                Date today = new Date();
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (confirmTime.compareTo(today) > 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày hiện tại");
                }
            }
        } catch (Exception e) {
        }
        try {
            if (errors.getFieldError("changeTreatmentDate") == null
                    && StringUtils.isNotEmpty(form.getChangeTreatmentDate())) {
                Date today = new Date();
                Date changeTreatmentDate = sdfrmt.parse(form.getChangeTreatmentDate());
                if (changeTreatmentDate.compareTo(today) > 0) {
                    errors.rejectValue("changeTreatmentDate", "changeTreatmentDate.error.message", "Ngày biến động điều trị không được lớn hơn ngày hiện tại");
                }
            }
        } catch (Exception e) {
        }
        try {
            if (errors.getFieldError("confirmTime") == null
                    && StringUtils.isNotEmpty(form.getConfirmTime())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (confirmTime.compareTo(year1000) < 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
        } catch (Exception e) {
        }

        //Ngày bắt đầu điều trị
        if (StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
            checkDateFormat(form.getStartTreatmentTime(), "startTreatmentTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
        }
        try {
            if (errors.getFieldError("startTreatmentTime") == null
                    && StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date today = new Date();
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());
                if (startTreatmentTime.compareTo(today) > 0) {
                    errors.rejectValue("startTreatmentTime", "startTreatmentTime.error.message", "Ngày bắt đầu điều trị không được lớn hơn ngày hiện tại");
                }
            }
        } catch (Exception e) {
        }
        try {
            if (errors.getFieldError("startTreatmentTime") == null
                    && StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());
                if (startTreatmentTime.compareTo(year1000) < 0) {
                    errors.rejectValue("startTreatmentTime", "startTreatmentTime.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
        } catch (Exception e) {
        }

        // Validate ngày Bắt đầu điều trị > Ngày Xn khẳng định
        try {
            if (StringUtils.isNotEmpty(form.getStartTreatmentTime()) && StringUtils.isNotEmpty(form.getConfirmTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date startTreatmen = sdfrmt.parse(form.getStartTreatmentTime());

                if (confirmTime.compareTo(startTreatmen) > 0) {
                    errors.rejectValue("startTreatmentTime", "startTreatmentTime.error.message", "Ngày bắt đầu điều trị không được nhỏ hơn Ngày XN khẳng định");
                }
            }

        } catch (ParseException ex) {
        }

        //Ngày tử vong
        if (StringUtils.isNotEmpty(form.getDeathTime())) {
            checkDateFormat(form.getDeathTime(), "deathTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
        }
        try {
            if (errors.getFieldError("deathTime") == null
                    && StringUtils.isNotEmpty(form.getDeathTime())) {
                Date today = new Date();
                Date deathTime = sdfrmt.parse(form.getDeathTime());
                if (deathTime.compareTo(today) > 0) {
                    errors.rejectValue("deathTime", "deathTime.error.message", "Ngày tử vong không được lớn hơn ngày hiện tại");
                }
            }
        } catch (Exception e) {
        }
        try {
            if (errors.getFieldError("deathTime") == null
                    && StringUtils.isNotEmpty(form.getDeathTime())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date deathTime = sdfrmt.parse(form.getDeathTime());
                if (deathTime.compareTo(year1000) < 0) {
                    errors.rejectValue("deathTime", "deathTime.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
        } catch (Exception e) {
        }

        // Validate ngày tử vong > Ngày Xn khẳng định
//        try {
//            if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isNotEmpty(form.getConfirmTime())) {
//                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
//                Date deadTime = sdfrmt.parse(form.getDeathTime());
//
//                if (deadTime.compareTo(confirmTime) < 0) {
//                    errors.rejectValue("deathTime", "deathTime.error.message", "Ngày tử vong không được nhỏ hơn ngày XN khẳng định");
//                }
//            }
        if (StringUtils.isNotEmpty(form.getDeathTime()) && (form.getCauseOfDeath() == null || form.getCauseOfDeath().isEmpty())) {
            errors.rejectValue("causeOfDeath", "causeOfDeath.error.message", "Nguyên nhân tử vong không được để trống");
        } else if (StringUtils.isNotEmpty(form.getRequestDeathTime()) && (form.getCauseOfDeath() == null || form.getCauseOfDeath().isEmpty())) {
            errors.rejectValue("causeOfDeath", "causeOfDeath.error.message", "Nguyên nhân tử vong không được để trống");
        }

        if ((StringUtils.isNotEmpty(form.getDeathTime()) || (form.getCauseOfDeath() != null && form.getCauseOfDeath().size() > 0)) && (form.getRequestDeathTime() == null || form.getRequestDeathTime().equals(""))) {
            errors.rejectValue("requestDeathTime", "requestDeathTime.error.message", "Ngày báo tử vong không được để trống");
        }

        if ((form.getCauseOfDeath() != null && form.getCauseOfDeath().size() > 0) && StringUtils.isEmpty(form.getDeathTime())) {
            errors.rejectValue("deathTime", "deathTime.error.message", "Ngày tử vong không được để trống");
        } else if (StringUtils.isNotEmpty(form.getRequestDeathTime()) && StringUtils.isEmpty(form.getDeathTime())) {
            errors.rejectValue("deathTime", "deathTime.error.message", "Ngày tử vong không được để trống");
        }

//        } catch (ParseException ex) {
//        }
//        if (StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey().equals(form.getStatusOfTreatmentID())) {
//            validateNull(form.getStartTreatmentTime(), "startTreatmentTime", "Ngày bắt đầu điều trị không được để trống", errors);
//            validateNull(form.getSiteTreatmentFacilityID(), "siteTreatmentFacilityID", "Nơi điều trị không được để trống", errors);
//            validateNull(form.getTreatmentRegimenID(), "treatmentRegimenID", "Phác đồ điều trị không được để trống", errors);
//        }
        // Validate địa chỉ cư trú hiện tại
        if (form.getIsDisplayCopy() != null && !form.getIsDisplayCopy()) {
            validateNull(form.getCurrentWardID(), "currentWardID", "Phường/xã hiện cư trú không được để trống", errors);
            validateNull(form.getCurrentProvinceID(), "currentProvinceID", "Tỉnh/Thành hiện cư trú không được để trống", errors);
            validateNull(form.getCurrentDistrictID(), "currentDistrictID", "Quận/Huyện hiện cư trú không được để trống", errors);
        }

        if (StringUtils.isNotEmpty(form.getIdentityCard()) && !TextUtils.removeDiacritical(form.getIdentityCard().trim()).matches(RegexpEnum.CMND)
                && errors.getFieldError("identityCard") == null) {
            errors.rejectValue("identityCard", "identityCard.error.message", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt");
        }

        if (StringUtils.isNotEmpty(form.getHealthInsuranceNo()) && !TextUtils.removeDiacritical(form.getHealthInsuranceNo().trim()).matches(RegexpEnum.HI_CHAR)
                && errors.getFieldError("healthInsuranceNo") == null) {
            errors.rejectValue("healthInsuranceNo", "healthInsuranceNo.error.message", "Số thẻ BHYT không chứa ký tự đặc biệt");
        }

        // Validate length patient_id CMND
        validateLength(form.getIdentityCard(), 50, "identityCard", "Số CMND không được quá 50 ký tự", errors);

        //Ngày XN nhiễm mới
        try {
            if (StringUtils.isNotEmpty(form.getEarlyHivTime())) {
                checkDateFormat(form.getEarlyHivTime(), "earlyHivTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("earlyHivTime") == null
                    && StringUtils.isNotEmpty(form.getEarlyHivTime())) {
                Date today = new Date();
                Date earlyHivTime = sdfrmt.parse(form.getEarlyHivTime());
                if (earlyHivTime.compareTo(today) > 0) {
                    errors.rejectValue("earlyHivTime", "earlyHivTime.error.message", "Ngày XN nhiễm mới không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("earlyHivTime") == null
                    && StringUtils.isNotEmpty(form.getEarlyHivTime())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date earlyHivTime = sdfrmt.parse(form.getEarlyHivTime());
                if (earlyHivTime.compareTo(year1000) < 0) {
                    errors.rejectValue("earlyHivTime", "earlyHivTime.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
//            if (StringUtils.isNotBlank(form.getEarlyHiv()) && StringUtils.isEmpty(form.getEarlyHivTime()) && !EarlyHivResultEnum.NO_TEST.getKey().equals(form.getEarlyHiv())) {
//                errors.rejectValue("earlyHivTime", "earlyHivTime.error.message", "Ngày XN nhiễm mới không được để trống");
//            }
        } catch (Exception e) {
        }

        //Ngày XN tải lượng virus confirm
        try {
            if (StringUtils.isNotEmpty(form.getVirusLoadConfirmDate())) {
                checkDateFormat(form.getVirusLoadConfirmDate(), "virusLoadConfirmDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("virusLoadConfirmDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadConfirmDate())) {
                Date today = new Date();
                Date virusLoadConfirmDate = sdfrmt.parse(form.getVirusLoadConfirmDate());
                if (virusLoadConfirmDate.compareTo(today) > 0) {
                    errors.rejectValue("virusLoadConfirmDate", "virusLoadConfirmDate.error.message", "Ngày XN tải lượng virus không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("virusLoadConfirmDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadConfirmDate())) {
                Date confirm = sdfrmt.parse(form.getConfirmTime());
                Date virusLoadConfirmDate = sdfrmt.parse(form.getVirusLoadConfirmDate());
                if (virusLoadConfirmDate.compareTo(confirm) < 0) {
                    errors.rejectValue("virusLoadConfirmDate", "virusLoadConfirmDate.error.message", "Ngày XN tải lượng virus không được nhỏ hơn Ngày XN khẳng định");
                }
            }
            if (errors.getFieldError("earlyHivTime") == null
                    && StringUtils.isNotEmpty(form.getEarlyHivTime())) {
                Date confirm = sdfrmt.parse(form.getConfirmTime());
                Date earlyHivTime = sdfrmt.parse(form.getEarlyHivTime());
                if (earlyHivTime.compareTo(confirm) < 0) {
                    errors.rejectValue("earlyHivTime", "earlyHivTime.error.message", "Ngày XN nhiễm mới HIV không được nhỏ hơn Ngày XN khẳng định");
                }
            }
            if (errors.getFieldError("virusLoadConfirmDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadConfirmDate())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date virusLoadConfirmDate = sdfrmt.parse(form.getVirusLoadConfirmDate());
                if (virusLoadConfirmDate.compareTo(year1000) < 0) {
                    errors.rejectValue("virusLoadConfirmDate", "virusLoadConfirmDate.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
//            if (StringUtils.isNotBlank(form.getVirusLoadConfirm()) && !form.getVirusLoadConfirm().equals("5") && StringUtils.isEmpty(form.getVirusLoadConfirmDate())) {
//                errors.rejectValue("virusLoadConfirmDate", "virusLoadConfirmDate.error.message", "Ngày XN tải lượng virus không được để trống");
//            }
        } catch (Exception e) {
        }

        //Ngày XN CD4
        try {
            if (StringUtils.isNotEmpty(form.getCdFourResultDate())) {
                checkDateFormat(form.getCdFourResultDate(), "cdFourResultDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("cdFourResultDate") == null
                    && StringUtils.isNotEmpty(form.getCdFourResultDate())) {
                Date today = new Date();
                Date cdFourResultDate = sdfrmt.parse(form.getCdFourResultDate());
                if (cdFourResultDate.compareTo(today) > 0) {
                    errors.rejectValue("cdFourResultDate", "cdFourResultDate.error.message", "Ngày XN CD4 không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("cdFourResultDate") == null
                    && StringUtils.isNotEmpty(form.getCdFourResultDate())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date cdFourResultDate = sdfrmt.parse(form.getCdFourResultDate());
                if (cdFourResultDate.compareTo(year1000) < 0) {
                    errors.rejectValue("cdFourResultDate", "cdFourResultDate.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
            if (StringUtils.isNotBlank(form.getCdFourResult()) && StringUtils.isEmpty(form.getCdFourResultDate())) {
                errors.rejectValue("cdFourResultDate", "cdFourResultDate.error.message", "Ngày XN CD4 không được để trống");
            }
        } catch (Exception e) {
        }

        //Ngày XN tải lượng CD4 lần gần nhất
        try {
            if (StringUtils.isNotEmpty(form.getCdFourResultLastestDate())) {
                checkDateFormat(form.getCdFourResultLastestDate(), "cdFourResultLastestDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("cdFourResultLastestDate") == null
                    && StringUtils.isNotEmpty(form.getCdFourResultLastestDate())) {
                Date today = new Date();
                Date virusLoadArvLastestDate = sdfrmt.parse(form.getCdFourResultLastestDate());
                if (virusLoadArvLastestDate.compareTo(today) > 0) {
                    errors.rejectValue("cdFourResultLastestDate", "cdFourResultLastestDate.error.message", "Ngày XN CD4 gần đây nhất không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("cdFourResultLastestDate") == null
                    && StringUtils.isNotEmpty(form.getCdFourResultDate()) && StringUtils.isNotEmpty(form.getCdFourResultLastestDate())) {
                Date cdFourResultDate = sdfrmt.parse(form.getCdFourResultDate());
                Date cdFourResultLastestDate = sdfrmt.parse(form.getCdFourResultLastestDate());
                if (cdFourResultDate.compareTo(cdFourResultLastestDate) > 0) {
                    errors.rejectValue("cdFourResultLastestDate", "cdFourResultLastestDate.error.message", "Ngày XN CD4 gần đây nhất không được nhỏ hơn ngày XN CD4 lần đầu tiên");
                }
            }
            if (errors.getFieldError("cdFourResultLastestDate") == null
                    && StringUtils.isNotEmpty(form.getCdFourResultLastestDate())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date cdFourResultLastestDate = sdfrmt.parse(form.getCdFourResultLastestDate());
                if (cdFourResultLastestDate.compareTo(year1000) < 0) {
                    errors.rejectValue("cdFourResultLastestDate", "cdFourResultLastestDate.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }

        } catch (Exception e) {
        }

        //Ngày XN tải lượng virus arrv
        try {
            if (StringUtils.isNotEmpty(form.getVirusLoadArvDate())) {
                checkDateFormat(form.getVirusLoadArvDate(), "virusLoadArvDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("virusLoadArvDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadArvDate())) {
                Date today = new Date();
                Date virusLoadArvDate = sdfrmt.parse(form.getVirusLoadArvDate());
                if (virusLoadArvDate.compareTo(today) > 0) {
                    errors.rejectValue("virusLoadArvDate", "virusLoadArvDate.error.message", "Ngày XN tải lượng virus không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("virusLoadArvDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadArvDate())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date virusLoadArvDate = sdfrmt.parse(form.getVirusLoadArvDate());
                if (virusLoadArvDate.compareTo(year1000) < 0) {
                    errors.rejectValue("virusLoadArvDate", "virusLoadArvDate.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
            if (StringUtils.isNotBlank(form.getVirusLoadArv()) && StringUtils.isEmpty(form.getVirusLoadArvDate()) && !"5".equals(form.getVirusLoadArv())) {
                errors.rejectValue("virusLoadArvDate", "virusLoadArvDate.error.message", "Ngày XN tải lượng virus lần đầu không được để trống");
            }
        } catch (Exception e) {
        }

        //Ngày XN tải lượng virus arv lần gần nhất
        try {
            if (StringUtils.isNotEmpty(form.getVirusLoadArvLastestDate())) {
                checkDateFormat(form.getVirusLoadArvDate(), "virusLoadArvLastestDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("virusLoadArvLastestDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadArvDate())) {
                Date today = new Date();
                Date virusLoadArvDate = sdfrmt.parse(form.getVirusLoadArvDate());
                if (virusLoadArvDate.compareTo(today) > 0) {
                    errors.rejectValue("virusLoadArvLastestDate", "virusLoadArvLastestDate.error.message", "Ngày XN tải lượng virus lần gần đây nhất không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("virusLoadArvLastestDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadArvDate()) && StringUtils.isNotEmpty(form.getVirusLoadArvLastestDate())) {
                Date virusLoadArvDate = sdfrmt.parse(form.getVirusLoadArvDate());
                Date virusLoadArvLatestDate = sdfrmt.parse(form.getVirusLoadArvLastestDate());
                if (virusLoadArvDate.compareTo(virusLoadArvLatestDate) > 0) {
                    errors.rejectValue("virusLoadArvLastestDate", "virusLoadArvLastestDate.error.message", "Ngày XN tải lượng virus lần gần đây nhất không được nhỏ hơn ngày XN tải lượng virus lần đầu tiên");
                }
            }
            if (errors.getFieldError("virusLoadArvLastestDate") == null
                    && StringUtils.isNotEmpty(form.getVirusLoadArvLastestDate())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date virusLoadArvLastestDate = sdfrmt.parse(form.getVirusLoadArvLastestDate());
                if (virusLoadArvLastestDate.compareTo(year1000) < 0) {
                    errors.rejectValue("virusLoadArvLastestDate", "virusLoadArvLastestDate.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }

        } catch (Exception e) {
        }

        //Ngày chuẩn đoán
        try {
            if (StringUtils.isNotEmpty(form.getClinicalStageDate())) {
                checkDateFormat(form.getClinicalStageDate(), "clinicalStageDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("clinicalStageDate") == null
                    && StringUtils.isNotEmpty(form.getClinicalStageDate())) {
                Date today = new Date();
                Date clinicalStageDate = sdfrmt.parse(form.getClinicalStageDate());
                if (clinicalStageDate.compareTo(today) > 0) {
                    errors.rejectValue("clinicalStageDate", "clinicalStageDate.error.message", "Ngày chẩn đoán không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("clinicalStageDate") == null
                    && StringUtils.isNotEmpty(form.getClinicalStageDate())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date clinicalStageDate = sdfrmt.parse(form.getClinicalStageDate());
                if (clinicalStageDate.compareTo(year1000) < 0) {
                    errors.rejectValue("clinicalStageDate", "clinicalStageDate.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
            if (StringUtils.isNotBlank(form.getClinicalStage()) && StringUtils.isEmpty(form.getClinicalStageDate())) {
                errors.rejectValue("clinicalStageDate", "clinicalStageDate.error.message", "Ngày chuẩn đoán không được để trống");
            }
        } catch (Exception e) {
        }

        //Ngày chẩn đoán AIDS
        try {
            if (StringUtils.isNotBlank(form.getAidsStatus()) && StringUtils.isNotEmpty(form.getAidsStatusDate())) {
                checkDateFormat(form.getAidsStatusDate(), "aidsStatusDate", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("aidsStatusDate") == null
                    && StringUtils.isNotEmpty(form.getAidsStatusDate())) {
                Date today = new Date();
                Date aidsStatusDate = sdfrmt.parse(form.getAidsStatusDate());
                if (aidsStatusDate.compareTo(today) > 0) {
                    errors.rejectValue("aidsStatusDate", "aidsStatusDate.error.message", "Ngày chẩn đoán AIDS không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("aidsStatusDate") == null
                    && StringUtils.isNotEmpty(form.getAidsStatusDate())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date aidsStatusDate = sdfrmt.parse(form.getAidsStatusDate());
                if (aidsStatusDate.compareTo(year1000) < 0) {
                    errors.rejectValue("aidsStatusDate", "aidsStatusDate.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
            if ("2".equals(form.getAidsStatus()) && StringUtils.isEmpty(form.getAidsStatusDate())) {
                errors.rejectValue("aidsStatusDate", "aidsStatusDate.error.message", "Ngày chẩn đoán AIDS không được để trống");
            }
        } catch (Exception e) {
        }
        //Ngày hết hạn bhyt
        try {
            if (StringUtils.isNotBlank(form.getInsuranceExpiry()) && StringUtils.isNotEmpty(form.getInsuranceExpiry())) {
                checkDateFormat(form.getInsuranceExpiry(), "insuranceExpiry", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("insuranceExpiry") == null
                    && StringUtils.isNotEmpty(form.getInsuranceExpiry())) {
                Date today = new Date();
                Date insuranceExpiry = sdfrmt.parse(form.getInsuranceExpiry());
                if (insuranceExpiry.compareTo(today) < 0) {
                    errors.rejectValue("insuranceExpiry", "insuranceExpiry.error.message", "Ngày hết hạn bhyt phải lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("insuranceExpiry") == null
                    && StringUtils.isNotEmpty(form.getInsuranceExpiry())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date insuranceExpiry = sdfrmt.parse(form.getInsuranceExpiry());
                if (insuranceExpiry.compareTo(year1000) < 0) {
                    errors.rejectValue("insuranceExpiry", "insuranceExpiry.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
        } catch (Exception e) {
        }
        //Ngày ARV đầu tiên
        try {
            if (StringUtils.isNotBlank(form.getFirstTreatmentTime()) && StringUtils.isNotEmpty(form.getFirstTreatmentTime())) {
                checkDateFormat(form.getFirstTreatmentTime(), "firstTreatmentTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("firstTreatmentTime") == null
                    && StringUtils.isNotEmpty(form.getFirstTreatmentTime())) {
                Date today = new Date();
                Date firstTreatmentTime = sdfrmt.parse(form.getFirstTreatmentTime());
                if (firstTreatmentTime.compareTo(today) > 0) {
                    errors.rejectValue("firstTreatmentTime", "firstTreatmentTime.error.message", "Ngày ARV không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("firstTreatmentTime") == null
                    && StringUtils.isNotEmpty(form.getFirstTreatmentTime())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date firstTreatmentTime = sdfrmt.parse(form.getFirstTreatmentTime());
                if (firstTreatmentTime.compareTo(year1000) < 0) {
                    errors.rejectValue("firstTreatmentTime", "firstTreatmentTime.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
        } catch (Exception e) {
        }
        //Ngày đăng ký
        try {
            if (StringUtils.isNotBlank(form.getRegistrationTime()) && StringUtils.isNotEmpty(form.getRegistrationTime())) {
                checkDateFormat(form.getRegistrationTime(), "registrationTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("registrationTime") == null
                    && StringUtils.isNotEmpty(form.getRegistrationTime())) {
                Date today = new Date();
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (registrationTime.compareTo(today) > 0) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", "Ngày đăng ký không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("registrationTime") == null
                    && StringUtils.isNotEmpty(form.getRegistrationTime())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date registrationTime = sdfrmt.parse(form.getRegistrationTime());
                if (registrationTime.compareTo(year1000) < 0) {
                    errors.rejectValue("registrationTime", "registrationTime.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
        } catch (Exception e) {
        }
        //Ngày kết thúc
        try {
            if (StringUtils.isNotBlank(form.getEndTime()) && StringUtils.isNotEmpty(form.getEndTime())) {
                checkDateFormat(form.getEndTime(), "endTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
            }
            if (errors.getFieldError("endTime") == null
                    && StringUtils.isNotEmpty(form.getEndTime())) {
                Date today = new Date();
                Date endTime = sdfrmt.parse(form.getEndTime());
                if (endTime.compareTo(today) > 0) {
                    errors.rejectValue("endTime", "endTime.error.message", "Ngày kết thúc không được lớn hơn ngày hiện tại");
                }
            }
            if (errors.getFieldError("endTime") == null
                    && StringUtils.isNotEmpty(form.getEndTime())) {
                Date year1000 = TextUtils.convertDate("01/01/1000", "dd/MM/yyyy");
                Date endTime = sdfrmt.parse(form.getEndTime());
                if (endTime.compareTo(year1000) < 0) {
                    errors.rejectValue("endTime", "endTime.error.message", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
        } catch (Exception e) {
        }
        // Valdate year of birth in range (1900 - current year)
        if (StringUtils.isNotEmpty(form.getCdFourResult()) && errors.getFieldError("cdFourResult") == null) {
            if (!form.getCdFourResult().matches(RegexpEnum.NUMBER)) {
                errors.rejectValue("cdFourResult", "cdFourResult.error.message", "Kết quả XN C4 phải là số và không được âm");
            }
        }

        if (StringUtils.isNotEmpty(form.getCdFourResultCurrent()) && errors.getFieldError("cdFourResultCurrent") == null) {
            if (!form.getCdFourResultCurrent().matches(RegexpEnum.NUMBER)) {
                errors.rejectValue("cdFourResultCurrent", "cdFourResultCurrent.error.message", "Kết quả XN CD4 hiện tại phải là số và không được âm");
            }
        }

        // Ràng buộc ngày và kết quả
        if (StringUtils.isEmpty(form.getCdFourResultCurrent()) && errors.getFieldError("cdFourResultCurrent") == null && StringUtils.isNotEmpty(form.getCdFourResultLastestDate())) {
            errors.rejectValue("cdFourResultCurrent", "cdFourResultCurrent.error.message", "Kết quả XN CD4 hiện tại không được trống");
        }

        if (StringUtils.isEmpty(form.getCdFourResultLastestDate()) && errors.getFieldError("cdFourResultLastestDate") == null && StringUtils.isNotEmpty(form.getCdFourResultCurrent())) {
            errors.rejectValue("cdFourResultLastestDate", "cdFourResultLastestDate.error.message", "Ngày XN CD4 lần gần đây nhất không được trống");
        }

        if (StringUtils.isEmpty(form.getVirusLoadArvCurrent()) && errors.getFieldError("virusLoadArvCurrent") == null && StringUtils.isNotEmpty(form.getVirusLoadArvLastestDate())) {
            errors.rejectValue("virusLoadArvCurrent", "virusLoadArvCurrent.error.message", "Kết quả XN tải lượng virus hiện tại không được trống");
        }

        if (StringUtils.isEmpty(form.getVirusLoadArvLastestDate()) && errors.getFieldError("virusLoadArvLastestDate") == null && StringUtils.isNotEmpty(form.getVirusLoadArvCurrent()) && !"5".equals(form.getVirusLoadArvCurrent())) {
            errors.rejectValue("virusLoadArvLastestDate", "virusLoadArvLastestDate.error.message", "Ngày XN tải lượng virus lần gần đây nhất không được trống");
        }

        // Kết quả XN tải lượng virus
        if (StringUtils.isNotEmpty(form.getVirusLoadConfirmDate()) && StringUtils.isEmpty(form.getVirusLoadConfirm()) && errors.getFieldError("virusLoadConfirm") == null) {
            errors.rejectValue("virusLoadConfirm", "virusLoadConfirm.error.message", "Kết quả XN tải lượng virus không được để trống");
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
    private void checkDateFormat(String inputValue, String id, String message, Errors errors, SimpleDateFormat sdfrmt) {

        if (StringUtils.isEmpty(inputValue)) {
            return;
        }
        sdfrmt.setLenient(false);
        try {
            sdfrmt.parse(inputValue);
        } catch (ParseException ex) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }

    // Check null or empty 
    private void validateNull(String inputValue, String id, String message, Errors errors) {
        if (StringUtils.isEmpty(inputValue) || inputValue == null) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }

    // Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, Errors errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }

}

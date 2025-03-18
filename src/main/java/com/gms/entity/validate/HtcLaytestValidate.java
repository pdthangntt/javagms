package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.AnswerEnum;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.DecisionAnswerEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.HtcLaytestAgencyEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.HtcLaytestForm;
import com.gms.service.DistrictService;
import com.gms.service.HtcLaytestService;
import com.gms.service.ParameterService;
import com.gms.service.ProvinceService;
import com.gms.service.WardService;
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
 * Custom validate for HtcConfirm
 *
 * @author TrangBN
 */
@Component
public class HtcLaytestValidate implements Validator {

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private WardService wardService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private HtcLaytestService htcLaytestService;
    
    private static final String PHONE_NUMBER = "(^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$)|(02[4|5|7|2|6|8|9])+([0-9]{8}$)";

    @Override
    public boolean supports(Class<?> type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validate(Object o, Errors errors) {

        // Validate existed code
        HtcLaytestForm form = (HtcLaytestForm) o;

        // Kiểm tra phần mã tự tăng
        if (form.getID() == null && StringUtils.isNotEmpty(form.getCode())
                && StringUtils.containsIgnoreCase(form.getCode(), "-") 
                && errors.getFieldError("code") == null) {
            String[] split = form.getCode().split("-", -1);
            try {
                Long.parseLong(split[split.length - 1]);
            } catch (Exception e) {
                errors.rejectValue("code", "code.error.message", "Mã KH không đúng định dạng");
            }
        }
        
        if (errors.getFieldError("permanentAddress") == null) {
            validateLength(form.getPermanentAddress(), 500, "permanentAddress", "Số nhà không được quá 500 ký tự", errors);
        }
        
        if (errors.getFieldError("permanentAddressGroup") == null) {
            validateLength(form.getPermanentAddressGroup(), 500, "permanentAddressGroup", "Đường phố không được quá 500 ký tự", errors);
        }
        
        if (errors.getFieldError("permanentAddressStreet") == null) {
            validateLength(form.getPermanentAddressStreet(), 500, "permanentAddressStreet", "Tổ/ấp/Khu phố không được quá 500 ký tự", errors);
        }
        if (form.getIsDisplayCopy() != null && !form.getIsDisplayCopy()) {
            if (errors.getFieldError("currentAddress") == null) {
                validateLength(form.getCurrentAddress(), 500, "currentAddress", "Số nhà không được quá 500 ký tự", errors);
            }

            if (errors.getFieldError("currentAddressGroup") == null) {
                validateLength(form.getCurrentAddressGroup(), 500, "currentAddressGroup", "Đường phố không được quá 500 ký tự", errors);
            }

            if (errors.getFieldError("currentAddressStreet") == null) {
                validateLength(form.getCurrentAddressStreet(), 500, "currentAddressStreet", "Tổ/ấp/Khu phố không được quá 500 ký tự", errors);
            }
        }
        if (StringUtils.isEmpty(form.getCode()) && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã xét nghiệm không được để trống");
        }
        
        // Check tồn tại mã của KH tại cơ sở xác định trường hợp thêm mới khẳng định
        if (form.getID() == null && htcLaytestService.existCodeAndSiteID(form.getCode(), form.getSiteID())
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã khách hàng đã tồn tại.");
        }
        
        if (StringUtils.isNotEmpty(form.getHasTestBefore()) && String.valueOf(BooleanEnum.TRUE.getKey()).equals(form.getHasTestBefore()) 
                && StringUtils.isEmpty(form.getMostRecentTest())) {
            errors.rejectValue("mostRecentTest", "mostRecentTest.error.message", "Lần XN gần nhất không được để trống");
        }
        
        // Validate định dạng mã xét nghiệm khẳng định
        if (StringUtils.isNotEmpty(form.getCode()) && !TextUtils.removeDiacritical(form.getCode().trim()).matches(RegexpEnum.CODE)
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã không đúng định dạng (Không chứa tự đặc biệt, chỉ chứa số, chữ và dấu -");
        }
        // Validate length mã xét nghiệm không chuyên
        if (!form.getCode().isEmpty() && errors.getFieldError("code") == null) {
            validateLength(form.getCode(), 50, "code", "Mã XN sàng lọc không được quá 50 ký tự", errors);
        }
        
        if (StringUtils.isNotBlank(form.getCode()) && errors.getFieldError("code") == null) {
            validateLength(form.getCode(), 50, "code", "Mã XN khẳng định không được quá 50 ký tự", errors);
        }

        // Validate độ dài tên của KH
        if (errors.getFieldError("patientName") == null) {
            validateLength(form.getPatientName(), 100, "patientName", "Họ tên không được quá 100 ký tự", errors);
        }

        // Validate kí tự đặc biệt của tên KH
        if (errors.getFieldError("patientName") == null && StringUtils.isNotEmpty(form.getPatientName())
                && !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.rejectValue("patientName", "patientName.error.message", "Họ tên chỉ chứa kí tự chữ cái");
        }

        // Valdate định dạng số
        if (StringUtils.isNotEmpty(form.getYearOfBirth()) && errors.getFieldError("yearOfBirth") == null) {
            if (!form.getYearOfBirth().matches(RegexpEnum.YYYY)) {
                errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }
        
        // Valdate year of birth in range (1900 - current year)
        if (StringUtils.isNotEmpty(form.getYearOfBirth()) && errors.getFieldError("yearOfBirth") == null) {
            if (Integer.valueOf(form.getYearOfBirth()) < 1900 || Integer.valueOf(form.getYearOfBirth()) > (Calendar.getInstance().get(Calendar.YEAR))) {
                errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }

        // Validate available genders
        if (StringUtils.isNotEmpty(form.getGenderID()) && !parameterService.fieldParameterExists(form.getGenderID(),
                ParameterEntity.GENDER, false, "")) {
            errors.rejectValue("genderID", "genderID.error.message", "Giới tính không tồn tại");
        }

        // Validate patient phone number
        if (StringUtils.isNotEmpty(form.getPatientPhone())) {
            form.setPatientPhone(form.getPatientPhone().replaceAll("\\s", ""));
            if (!form.getPatientPhone().matches(PHONE_NUMBER)) {
                errors.rejectValue("patientPhone", "patientPhone.error.message", "Số điện thoại không hợp lệ");
            }
        }
        
        // Validate length patient_id CMND
        validateLength(form.getPatientID(), 50, "patientID", "Số CMND không được quá 50 ký tự", errors);
        // Validate kí tự CMND
        if (StringUtils.isNotEmpty(form.getPatientID()) && !TextUtils.removeDiacritical(form.getPatientID().trim()).matches(RegexpEnum.VN_CHAR)
                && errors.getFieldError("patientID") == null) {
            errors.rejectValue("patientID", "patientID.error.message", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt/chữ tiếng việt, số âm, số thập phân");
        }
        // Validate nhóm đối tượng
        if (StringUtils.isNotEmpty(form.getObjectGroupID()) && !parameterService.fieldParameterExists(form.getObjectGroupID(),
                ParameterEntity.TEST_OBJECT_GROUP, false, "")) {
            errors.rejectValue("objectGroupID", "objectGroupID.error.message", "Nhóm đối tượng không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getPermanentProvinceID())
                && !provinceService.existsProvinceByID(form.getPermanentProvinceID())) {
            errors.rejectValue("permanentProvinceID", "permanentProvinceID.error.message", "Tỉnh thành không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getPermanentDistrictID())
                && !districtService.existsDistrictByID(form.getPermanentDistrictID(), form.getPermanentProvinceID())) {
            errors.rejectValue("permanentDistrictID", "permanentDistrictID.error.message", "Quận huyện không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getPermanentWardID())
                && !wardService.existsWardByID(form.getPermanentWardID(), form.getPermanentDistrictID())) {
            errors.rejectValue("permanentWardID", "permanentWardID.error.message", "Xã phường không tồn tại");
        }
        
        if (StringUtils.isNotEmpty(form.getCurrentProvinceID())
                && !provinceService.existsProvinceByID(form.getCurrentProvinceID())) {
            errors.rejectValue("currentProvinceID", "currentProvinceID.error.message", "Tỉnh thành không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getCurrentDistrictID())
                && !districtService.existsDistrictByID(form.getCurrentDistrictID(), form.getCurrentProvinceID())) {
            errors.rejectValue("currentDistrictID", "currentDistrictID.error.message", "Quận huyện không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getCurrentWardID())
                && !wardService.existsWardByID(form.getCurrentWardID(), form.getCurrentDistrictID())) {
            errors.rejectValue("currentWardID", "currentWardID.error.message", "Xã phường không tồn tại");
        }

        if (StringUtils.isEmpty(form.getAdvisoryeTime()) && errors.getFieldError("advisoryeTime") == null) {
            errors.rejectValue("advisoryeTime", "advisoryeTime.error.message", "Ngày xét nghiệm không được để trống");
        }
        
        if (StringUtils.isEmpty(form.getTestResultsID()) && errors.getFieldError("testResultsID") == null) {
            errors.rejectValue("testResultsID", "testResultsID.error.message", "Kết quả xét nghiệm không được để trống");
        }        
        
        // Validate ngày gửi mẫu tại cơ sở sàng lọc nhỏ hơn ngày hiện tại
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (StringUtils.isNotEmpty(form.getSampleSentDate()) && checkDateFormat(form.getSampleSentDate(), "sampleSentDate", "Ngày gửi mẫu phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date sentDate = sdfrmt.parse(form.getSampleSentDate());
                if (sentDate.compareTo(new Date()) > 0 && errors.getFieldError("sampleSentDate") == null) {
                    errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu không được lớn hơn ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getAdvisoryeTime()) && errors.getFieldError("advisoryeTime") == null) {
                checkDateFormat(form.getAdvisoryeTime(), "advisoryeTime", "Ngày xét nghiệm phải đúng định dạng ngày/tháng/năm", errors, sdfrmt);
            }
            
            Date advisoryDate = sdfrmt.parse(form.getAdvisoryeTime());
            if (StringUtils.isNotEmpty(form.getAdvisoryeTime()) && advisoryDate.compareTo(new Date()) > 0 && errors.getFieldError("advisoryeTime") == null) {
                errors.rejectValue("advisoryeTime", "advisoryeTime.error.message", "Ngày xét nghiệm không được lớn hơn ngày hiện tại");
            }
        } catch (Exception e) {
        }

        // Điều kiện bắt buộc nhập có kết quả xét nghiệm và đồng ý xedst nghiệm 
        if(StringUtils.isNotEmpty(form.getTestResultsID()) && StringUtils.isNotEmpty(form.getIsAgreeTest()) 
                && TestResultEnum.CO_PHAN_UNG.getKey().equals(form.getTestResultsID())
                && DecisionAnswerEnum.TRUE.getKey().toString().equals(form.getIsAgreeTest())) {
            
            if (StringUtils.isEmpty(form.getPatientName()) && errors.getFieldError("patientName") == null) {
                errors.rejectValue("patientName", "patientName.error.message", "Họ tên khách hàng không được để trống");
            }
            if (form.getYearOfBirth() == null && errors.getFieldError("yearOfBirth") == null) {
                errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh không được để trống");
            }
//            if (StringUtils.isEmpty(form.getPermanentAddress()) && errors.getFieldError("permanentAddress") == null) {
//                errors.rejectValue("permanentAddress", "permanentAddress.error.message", "Địa chỉ thường trú không được để trống");
//            }
            if (StringUtils.isEmpty(form.getPermanentProvinceID()) && errors.getFieldError("permanentProvinceID") == null) {
                errors.rejectValue("permanentProvinceID", "permanentProvinceID.error.message", "Tỉnh/Thành phố không được để trống");
            }
            if (StringUtils.isEmpty(form.getPermanentDistrictID()) && errors.getFieldError("permanentDistrictID") == null) {
                errors.rejectValue("permanentDistrictID", "permanentDistrictID.error.message", "Quận/Huyện không được để trống");
            }
            if (StringUtils.isEmpty(form.getPermanentWardID()) && errors.getFieldError("permanentWardID") == null) {
                errors.rejectValue("permanentWardID", "permanentWardID.error.message", "Phường/xã không được để trống");
            }
        
        }
        
        // Validate câu trả lời
        if (StringUtils.isNotEmpty(form.getTestResultsID()) && TestResultEnum.CO_PHAN_UNG.getKey().equals(form.getTestResultsID()) 
                && StringUtils.isEmpty(form.getIsAgreeTest()) 
                && errors.getFieldError("isAgreeTest") == null) {
            errors.rejectValue("isAgreeTest", "isAgreeTest.error.message", "Câu trả lời không được để trống");
        }
        
        // Validate available screening test result
        if (StringUtils.isNotEmpty(form.getTestResultsID()) && !parameterService.fieldParameterExists(form.getTestResultsID(),
                ParameterEntity.TEST_RESULTS, false, "")) {
            errors.rejectValue("testResultsID", "testResultsID.error.message", "Kết quả XN sàng lọc không tồn tại");
        }

        // Validate ngày tiếp nhận ( định dang dd/mm/yyyy, hiện tại >= ngày tiếp nhận >= ngày nhận mẫu)
        try {
            if (StringUtils.isNotEmpty(form.getAcceptDate()) && checkDateFormat(form.getAcceptDate(), "acceptDate", "Ngày tiếp nhận phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date acceptDate = sdfrmt.parse(form.getAcceptDate());
                if (acceptDate.compareTo(new Date()) > 0 && errors.getFieldError("acceptDate") == null) {
                    errors.rejectValue("acceptDate", "acceptDate.error.message", "Ngày tiếp nhận không được lớn hơn ngày hiện tại");
                }
            }
            
             Date advisoryeTime;
             Date confirmTime;
             Date exchangeTime;
             Date registerTherapyTime;
            
            // Validate advisoryeTime <= confirmTime
            if (StringUtils.isNotEmpty(form.getAdvisoryeTime()) && StringUtils.isNotEmpty(form.getConfirmTime()) &&
                    errors.getFieldError("confirmTime") == null) {
                advisoryeTime = sdfrmt.parse(form.getAdvisoryeTime());
                confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (advisoryeTime.compareTo(confirmTime) > 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được nhỏ hơn ngày XN tại cộng đồng");
                }
            }
            
            // Ngày xét nghiệm khẳng định không lơn hơn ngày hiện tại
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (confirmTime.compareTo(new Date()) > 0 && errors.getFieldError("confirmTime") == null) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày hiện tại");
                }
            }
            
            // Validate confirmTime <= exchangeTime
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getExchangeTime()) &&
                    errors.getFieldError("exchangeTime") == null) {
                confirmTime = sdfrmt.parse(form.getConfirmTime());
                exchangeTime = sdfrmt.parse(form.getExchangeTime());
                if (confirmTime.compareTo(exchangeTime) > 0) {
                    errors.rejectValue("exchangeTime", "exchangeTime.error.message", "Ngày chuyển gửi không được nhỏ hơn ngày XN khẳng định");
                }
            }
            
            // Ngày chuyển gửi không lơn hơn ngày hiện tại
            if (StringUtils.isNotEmpty(form.getExchangeTime()) && checkDateFormat(form.getExchangeTime(), "exchangeTime", "Ngày chuyển gửi phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                exchangeTime = sdfrmt.parse(form.getExchangeTime());
                if (exchangeTime.compareTo(new Date()) > 0 && errors.getFieldError("exchangeTime") == null) {
                    errors.rejectValue("exchangeTime", "exchangeTime.error.message", "Ngày chuyển gửi không được lớn hơn ngày hiện tại");
                }
            }
            
            // Validate exchangeTime <= registerTherapyTime
            if (StringUtils.isNotEmpty(form.getRegisterTherapyTime()) && StringUtils.isNotEmpty(form.getExchangeTime()) &&
                    errors.getFieldError("registerTherapyTime") == null) {
                registerTherapyTime = sdfrmt.parse(form.getRegisterTherapyTime());
                exchangeTime = sdfrmt.parse(form.getExchangeTime());
                if (exchangeTime.compareTo(registerTherapyTime) > 0) {
                    errors.rejectValue("registerTherapyTime", "registerTherapyTime.error.message", "Ngày đăng ký điều trị không được nhỏ hơn ngày chuyển gửi");
                }
            }
            
            // Ngày chuyển gửi không lơn hơn ngày hiện tại
            if (StringUtils.isNotEmpty(form.getRegisterTherapyTime()) && checkDateFormat(form.getRegisterTherapyTime(), "registerTherapyTime", "Ngày đăng ký điều trị phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                registerTherapyTime = sdfrmt.parse(form.getRegisterTherapyTime());
                if (registerTherapyTime.compareTo(new Date()) > 0 && errors.getFieldError("registerTherapyTime") == null) {
                    errors.rejectValue("registerTherapyTime", "registerTherapyTime.error.message", "Ngày đăng ký điều trị không được lớn hơn ngày hiện tại");
                }
            }
            
            
        } catch (Exception e) {
        }
        
        // Validate ràng buộc kết quả xét nghiêm và vạch chứng vach chứng kết quả
        if(StringUtils.isNotEmpty(form.getControlLine()) && StringUtils.isNotEmpty(form.getTestLine()) &&
               AnswerEnum.YES.getKey().toString().equals(form.getControlLine()) &&
                AnswerEnum.YES.getKey().toString().equals(form.getTestLine()) &&
                !TestResultEnum.CO_PHAN_UNG.getKey().equals(form.getTestResultsID())) {
            errors.rejectValue("testResultsID", "testResultsID.error.message", "Kiểm tra lại kết quả xét nghiệm");
        }
        
        // Validate thông tin bạn tình bạn chích được giới thiệu
        if (form.getLaytestAgencies() != null && form.getLaytestAgencies().size() > 0) {
            for (HtcLaytestAgencyEntity laytestAgency : form.getLaytestAgencies()) {
                validateAgencies(laytestAgency, errors);
            }
        }
    }
    
    // Check length for input string
    private void validateLength(String inputValue, int length, String id, String message, Errors errors) {
        if (StringUtils.isNotEmpty(inputValue) && inputValue.length() > length) {
            errors.rejectValue(id, id + ".error.message", message);
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
            errors.rejectValue(id, id + ".error.message", message);
            return false;
        }
        return true;
    }
    
    /**
     * Validate thông tin bạn tình bạn chích được giới thiệu
     * 
     * @param entity 
     */
    private void validateAgencies(HtcLaytestAgencyEntity entity, Errors errors) {
        
        // Validate length mã xét nghiệm không chuyên
        if (StringUtils.isNotEmpty(entity.getFullname()) && errors.getFieldError("fullname") == null) {
            validateLength(entity.getFullname(), 100, "laytestAgencies", "Họ tên không được quá 100 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(entity.getFullname()) && errors.getFieldError("fullname") == null && !TextUtils.removeDiacritical(entity.getFullname().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.rejectValue("laytestAgencies", "laytestAgencies.error.message", "Họ tên không được chứa ký tự đặc biệt");
        }
        
        if (StringUtils.isNotEmpty(entity.getAddress()) && errors.getFieldError("address") == null) {
            validateLength(entity.getAddress(), 500, "laytestAgencies", "Địa chỉ không được quá 500 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(entity.getAddress()) && errors.getFieldError("address") == null && !TextUtils.removeDiacritical(entity.getAddress().trim()).matches(RegexpEnum.ADDRESS)) {
            errors.rejectValue("laytestAgencies", "laytestAgencies.error.message", "Địa chỉ không được chứa ký tự đặc biệt");
        }
        
        if (StringUtils.isNotEmpty(entity.getPhone()) && errors.getFieldError("phone") == null) {
            validateLength(entity.getPhone(), 50, "laytestAgencies", "Số điện thoại không dược quá 50 ký tự ", errors);
        }
        
        if (StringUtils.isNotEmpty(entity.getPhone()) && errors.getFieldError("phone") == null && 
                !entity.getPhone().matches(RegexpEnum.NUMBER_ONLY)) {
            errors.rejectValue("laytestAgencies", "laytestAgencies.error.message", "Số điện thoại phải là số");
        }
        
        if (StringUtils.isNotEmpty(entity.getAlertType()) && errors.getFieldError("alertType") == null ) {
            validateLength(entity.getAlertType(), 50, "laytestAgencies", "Hình thức thông báo không dược quá 50 ký tự ", errors);
        }
        
        if (StringUtils.isNotEmpty(entity.getAlertType()) && errors.getFieldError("alertType") == null && 
                !entity.getAlertType().matches(RegexpEnum.NUMBER_ONLY)) {
            errors.rejectValue("laytestAgencies", "laytestAgencies.error.message", "Sai hình thức thông báo");
        }
        
        if (StringUtils.isNotEmpty(entity.getIsAgreePreTest()) && errors.getFieldError("isAgreePreTest") == null ) {
            validateLength(entity.getIsAgreePreTest(), 50, "laytestAgencies", "Trạng thái xét nghiệm không dược quá 50 ký tự ", errors);
        }
        
        if (StringUtils.isNotEmpty(entity.getIsAgreePreTest()) && errors.getFieldError("isAgreePreTest") == null && 
                !entity.getIsAgreePreTest().matches(RegexpEnum.NUMBER_ONLY)) {
            errors.rejectValue("laytestAgencies", "laytestAgencies.error.message", "Sai hình tình trạng xét nghiệm");
        }
        
    }
}

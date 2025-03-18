package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.HtcConfirmForm;
import com.gms.entity.form.HtcConfirmImportForm;
import com.gms.service.DistrictService;
import com.gms.service.HtcConfirmService;
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
 *
 * @author NamAnh_HaUI
 */
@Component
public class HtcConfirmImportValidate implements Validator {

    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private WardService wardService;
    @Autowired
    private DistrictService districtService;
    
    private static final String VALIDATE_YEAR = "^\\d{4}$";

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcConfirmImportForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        
        // Validate existed code
        HtcConfirmImportForm form = (HtcConfirmImportForm) o;
        validateLength(form.getAddress(), 500, "address", "Số nhà không được quá 500 ký tự", errors);
        validateLength(form.getPermanentAddressGroup(), 500, "permanentAddressGroup", "Đường phố không được quá 500 ký tự", errors);
        validateLength(form.getPermanentAddressStreet(), 500, "permanentAddressStreet", "Tổ/ấp/Khu phố không được quá 500 ký tự", errors);
        
        if (StringUtils.isNotEmpty(form.getInsurance()) && "1".equals(form.getInsurance()) && !"".equals(form.getInsurance())
                && StringUtils.isNotEmpty(form.getInsuranceNo()) && errors.getFieldError("insuranceNo") == null && !form.getInsuranceNo().matches(RegexpEnum.HI_CHAR)) {
            errors.rejectValue("insuranceNo", "insuranceNo.error.message", "Số thẻ BHYT chỉ chứa kí tự chữ và số dấu - và _");
        }
        
        if (StringUtils.isNotEmpty(form.getCurrentAddress())) {
            validateLength(form.getCurrentAddress(), 500, "currentAddress", "Số nhà không được quá 500 ký tự", errors);
        }
        if (StringUtils.isNotEmpty(form.getCurrentAddressGroup())) {
            validateLength(form.getCurrentAddressGroup(), 500, "currentAddressGroup", "Đường phố không được quá 500 ký tự", errors);
        }
        if (StringUtils.isNotEmpty(form.getCurrentAddressStreet())) {
            validateLength(form.getCurrentAddressStreet(), 500, "currentAddressStreet", "Tổ/ấp/Khu phố không được quá 500 ký tự", errors);
        }
        
        if (StringUtils.isEmpty(form.getSampleReceiveTime()) && errors.getFieldError("sampleReceiveTime") == null) {
            errors.rejectValue("sampleReceiveTime", "sampleReceiveTime.error.message", "Ngày nhận mẫu không được để trống");
        }
        
        // Validate thông tin BHYT
//        if (StringUtils.isEmpty(form.getInsurance()) && errors.getFieldError("insurance") == null) {
//            errors.rejectValue("insurance", "insurance.error.message", "Thẻ BHYT không được để trống");
//        }
        
        if (StringUtils.isNotEmpty(form.getInsuranceNo()) && errors.getFieldError("insuranceNo") == null) {
            validateLength(form.getInsuranceNo(), 50, "insuranceNo", "Số thẻ BHYT không được quá 50 ký tự", errors);
        }

        // Ngày cơ sở gửi mẫu lên cơ sở khẳng định
        if (StringUtils.isEmpty(form.getSourceSampleDate()) && errors.getFieldError("sourceSampleDate") == null) {
            errors.rejectValue("sourceSampleDate", "sourceSampleDate.error.message", "Ngày gửi mẫu không được để trống");
        }
        
        // Kết quả XN khẳng định
        if (StringUtils.isEmpty(form.getTestResultsID()) && errors.getFieldError("testResultsID") == null) {
            errors.rejectValue("testResultsID", "testResultsID.error.message", "Kết quả XN sàng lọc không được để  trống");
        }
        
        // Ngày lấy mẫu không được để trống
        if (StringUtils.isEmpty(form.getSourceReceiveSampleTime()) && errors.getFieldError("sourceReceiveSampleTime") == null) {
            errors.rejectValue("sourceReceiveSampleTime", "sourceReceiveSampleTime.error.message", "Ngày lấy mẫu không được để trống");
        }
        
        // Mã khách hàng XN sàng lọc không được để trống
        if (StringUtils.isEmpty(form.getSourceID()) && errors.getFieldError("sourceID") == null) {
            errors.rejectValue("sourceID", "sourceID.error.message", "Mã khách hàng XN sàng lọc không được để trống");
        }
        
        // Đối tượng không được để trống
        if (StringUtils.isEmpty(form.getObjectGroupID()) && errors.getFieldError("objectGroupID") == null) {
            errors.rejectValue("objectGroupID", "objectGroupID.error.message", "Đối tượng không được để trống");
        }
        
        // Tỉnh/Thành phố không được để trống
        if (StringUtils.isEmpty(form.getProvinceID()) && errors.getFieldError("provinceID") == null) {
            errors.rejectValue("provinceID", "provinceID.error.message", "Tỉnh/Thành phố không được để trống");
        }
        
        // Quận/Huyện không được để trống
        if (StringUtils.isEmpty(form.getDistrictID()) && errors.getFieldError("districtID") == null) {
            errors.rejectValue("districtID", "districtID.error.message", "Quận/Huyện không được để trống");
        }
        
        // Xã/Phường không được để trống
        if (StringUtils.isEmpty(form.getWardID()) && errors.getFieldError("wardID") == null) {
            errors.rejectValue("wardID", "wardID.error.message", "Xã/Phường không được để trống");
        }
        
        // Giới tính không được để trống
        if (StringUtils.isEmpty(form.getGenderID()) && errors.getFieldError("genderID") == null) {
            errors.rejectValue("genderID", "genderID.error.message", "Giới tính không được để trống");
        }
        
        // Cán bộ XN khẳng định không được quá 100 ký tự
        if (StringUtils.isNotEmpty(form.getTestStaffId()) && errors.getFieldError("testStaffId") == null) {
            validateLength(form.getTestStaffId(), 100, "testStaffId", "Cán bộ XN khẳng định không được quá 100 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getResultsID()) && errors.getFieldError("testStaffId") == null) {
            validateNull(form.getTestStaffId(), "testStaffId", "Cán bộ XN khẳng định không được để trống", errors);
        }
        
        // Họ tên không được để trống
        if (StringUtils.isEmpty(form.getFullname()) && errors.getFieldError("fullname") == null) {
            errors.rejectValue("fullname", "fullname.error.message", "Họ tên không được để trống");
        }
        
        if ((StringUtils.isNotEmpty(form.getBioNameResult1()) || StringUtils.isNotEmpty(form.getFirstBioDate()))
                && StringUtils.isEmpty(form.getBioName1()) && errors.getFieldError("bioName1") == null) {
            errors.rejectValue("bioName1", "bioName1.error.message", "Tên SP1 không được để trống");
        }

        if ((StringUtils.isNotEmpty(form.getBioNameResult2()) || StringUtils.isNotEmpty(form.getSecondBioDate()))
                && StringUtils.isEmpty(form.getBioName2()) && errors.getFieldError("bioName2") == null) {
            errors.rejectValue("bioName2", "bioName2.error.message", "Tên SP2 không được để trống");
        }

        if ((StringUtils.isNotEmpty(form.getBioNameResult3())|| StringUtils.isNotEmpty(form.getThirdBioDate()))
                && StringUtils.isEmpty(form.getBioName3()) && errors.getFieldError("bioName3") == null) {
            errors.rejectValue("bioName3", "bioName3.error.message", "Tên SP3 không được để trống");
        }

        if (StringUtils.isEmpty(form.getBioNameResult1())
                && StringUtils.isNotEmpty(form.getBioName1()) && errors.getFieldError("bioNameResult1") == null) {
            errors.rejectValue("bioNameResult1", "bioNameResult1.error.message", "Kết quả XN sinh phẩm 1 không được để trống");
        }

        if (StringUtils.isEmpty(form.getBioNameResult2())
                && StringUtils.isNotEmpty(form.getBioName2()) && errors.getFieldError("bioNameResult2") == null) {
            errors.rejectValue("bioNameResult2", "bioNameResult2.error.message", "Kết quả XN sinh phẩm 2 không được để trống");
        }

        if (StringUtils.isEmpty(form.getBioNameResult3())
                && StringUtils.isNotEmpty(form.getBioName3()) && errors.getFieldError("bioNameResult3") == null) {
            errors.rejectValue("bioNameResult3", "bioNameResult3.error.message", "Kết quả XN sinh phẩm 3 không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getBioName1()) && StringUtils.isNotEmpty(form.getBioName2()) && StringUtils.isNotEmpty(form.getBioName3())
                && errors.getFieldError("bioName1") == null && errors.getFieldError("bioName2") == null && errors.getFieldError("bioName3") == null) {
            if (form.getBioName1().equals(form.getBioName2())) {
                errors.rejectValue("bioName1", "bioName1.error.message", "Tên sinh phẩm không được trùng");
                errors.rejectValue("bioName2", "bioName2.error.message", "Tên sinh phẩm không được trùng");
            }
            if (form.getBioName2().equals(form.getBioName3())) {
                errors.rejectValue("bioName2", "bioName2.error.message", "Tên sinh phẩm không được trùng");
                errors.rejectValue("bioName3", "bioName3.error.message", "Tên sinh phẩm không được trùng");
            }
            if (form.getBioName3().equals(form.getBioName1())) {
                errors.rejectValue("bioName1", "bioName1.error.message", "Tên sinh phẩm không được trùng");
                errors.rejectValue("bioName3", "bioName3.error.message", "Tên sinh phẩm không được trùng");
            }
            if (form.getBioName1().equals(form.getBioName2()) && form.getBioName2().equals(form.getBioName3()) && form.getBioName3().equals(form.getBioName1())) {
                errors.rejectValue("bioName1", "bioName1.error.message", "Tên sinh phẩm không được trùng");
                errors.rejectValue("bioName2", "bioName2.error.message", "Tên sinh phẩm không được trùng");
                errors.rejectValue("bioName3", "bioName3.error.message", "Tên sinh phẩm không được trùng");
            }
        }

        if ("1".equals(form.getResultsID()) && !("0".equals(form.getBioNameResult1()) || "0".equals(form.getBioNameResult2()) || "0".equals(form.getBioNameResult3()))) {
            errors.rejectValue("resultsID", "resultsID.error.message", "1 trong 3 kết quả XN sinh phẩm phải âm tính");
        }
        if ("2".equals(form.getResultsID())
                && errors.getFieldError("bioNameResult1") == null && errors.getFieldError("bioNameResult2") == null && errors.getFieldError("bioNameResult3") == null) {
            if (!"1".equals(form.getBioNameResult1())) {
                errors.rejectValue("bioNameResult1", "bioNameResult1.error.message", "Kết quả XN sinh phẩm phải là dương tính");
            }
            if (!"1".equals(form.getBioNameResult2())) {
                errors.rejectValue("bioNameResult2", "bioNameResult2.error.message", "Kết quả XN sinh phẩm phải là dương tính");
            }
            if (!"1".equals(form.getBioNameResult3())) {
                errors.rejectValue("bioNameResult3", "bioNameResult3.error.message", "Kết quả XN sinh phẩm phải là dương tính");
            }
        }

        if ("".equals(form.getResultsID()) && !("".equals(form.getBioNameResult1()) && "".equals(form.getBioNameResult2()) && "".equals(form.getBioNameResult3()))) {
            if ("1".equals(form.getBioNameResult1()) && "1".equals(form.getBioNameResult2()) && "1".equals(form.getBioNameResult3())) {
                errors.rejectValue("resultsID", "resultsID.error.message", "Kết quả XN khẳng định phải là dương tính");
            }
        }

        // Check tồn tại mã của KH tại cơ sở xác định trường hợp thêm mới khẳng định
        if (form.getID() == null && htcConfirmService.existCodes(form.getCode())
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã xét nghiệm đã tồn tại.");
        }
        
        if (form.getID() != null && !form.getSourceServiceID().equals(ServiceEnum.HTC_CONFIRM.getKey()) && errors.getFieldError("code") == null) {
            HtcConfirmEntity confirm = htcConfirmService.findOne(form.getID());
            if (confirm != null && confirm.getCode()!= null && !confirm.getCode().equals(form.getCode()) && htcConfirmService.existCodes(form.getCode())) {
                errors.rejectValue("code", "code.error.message", "Mã xét nghiệm đã tồn tại.");
            }
        }

        // Validate định dạng mã xét nghiệm khẳng định
        if (StringUtils.isNotEmpty(form.getCode()) && !TextUtils.removeDiacritical(form.getCode().trim()).matches(RegexpEnum.CODE_CONFIRM)
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\" )");
        }

        if (StringUtils.isNotEmpty(form.getSourceID()) && !TextUtils.removeDiacritical(form.getSourceID().trim()).matches(RegexpEnum.CODE)
                && errors.getFieldError("sourceID") == null) {
            errors.rejectValue("sourceID", "sourceID.error.message", "Mã không đúng định dạng (Không chứa tự đặc biệt, chỉ chứa số, chữ và dấu - )");
        }

        // Validate length mã xét nghiệm sàng lọc
        if (StringUtils.isNotEmpty(form.getSourceID()) && errors.getFieldError("sourceID") == null) {
            validateLength(form.getSourceID(), 50, "sourceID", "Mã XN sàng lọc không được quá 50 ký tự", errors);
        }

        if (StringUtils.isNotBlank(form.getCode()) && errors.getFieldError("code") == null) {
            validateLength(form.getCode(), 50, "code", "Mã XN khẳng định không được quá 50 ký tự", errors);
        }

        if (form.getSourceSiteID() == null && errors.getFieldError("sourceSiteID") == null) {
            errors.rejectValue("sourceSiteID", "sourceSiteID.error.message", "Tên cơ sở gửi mẫu không được để trống");
        }

        //Validate bat buoc nhap khi ket qua xet nghiem khang dinh khac null
        if (StringUtils.isNotBlank(form.getResultsID()) && StringUtils.isBlank(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
            errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày xét nghiệm khẳng định không được để trống");
        }
        if (StringUtils.isBlank(form.getResultsID()) && StringUtils.isNotBlank(form.getConfirmTime()) && errors.getFieldError("resultsID") == null) {
            errors.rejectValue("resultsID", "resultsID.error.message", "Kết quả xét nghiệm khẳng định không được để trống");
        }
        if (StringUtils.isNotBlank(form.getResultsID()) && StringUtils.isNotBlank(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
            if (TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getConfirmTime()).charAt(0) == '0') {
                errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không đúng định dạng");
            }
        }
        // Validate độ dài tên của KH
        if (errors.getFieldError("fullname") == null) {
            validateLength(form.getFullname(), 100, "fullname", "Họ tên không được quá 100 ký tự", errors);
        }

        // Validate kí tự đặc biệt của tên KH
        if (errors.getFieldError("fullname") == null && StringUtils.isNotEmpty(form.getFullname())
                && !TextUtils.removeDiacritical(form.getFullname().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.rejectValue("fullname", "fullname.error.message", "Họ tên chỉ chứa kí tự chữ cái");
        }

        // Validate code trong trường hợp thêm mới khách hàng và cập nhật khách hàng từ phía sàng lọc
        if ((form.getID() == null && StringUtils.isEmpty(form.getCode()))
                || (form.getID() != null && ServiceEnum.HTC.getKey().equals(form.getSourceServiceID())
                && StringUtils.isEmpty(form.getCode()))) {
            errors.rejectValue("code", "code.error.message", "Mã xét nghiệm không được để trống");
        }

        // Validate sourceSiteID in case add new customer DSNAnh
        if (form.getSourceSiteID() == null && StringUtils.isEmpty(String.valueOf(form.getSourceSiteID()))) {
            errors.rejectValue("sourceSiteID", "sourceSiteID.error.message", "Tên cơ sở gửi mẫu không được để trống");
        }

        // Validate number format for year of birth
        if (StringUtils.isNotEmpty(form.getYear()) && errors.getFieldError("year") == null) {
            if (!form.getYear().matches(VALIDATE_YEAR)) {
                errors.rejectValue("year", "year.error.message", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }
        // Valdate year of birth in range (1900 - current year)
        if (StringUtils.isNotEmpty(form.getYear()) && errors.getFieldError("year") == null) {
            if (Integer.parseInt(form.getYear()) < 1900 || Integer.parseInt(form.getYear()) > (Calendar.getInstance().get(Calendar.YEAR))) {
                errors.rejectValue("year", "year.error.message", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }

        // Validate available genders
        if (StringUtils.isNotEmpty(form.getGenderID()) && !parameterService.fieldParameterExists(form.getGenderID(),
                ParameterEntity.GENDER, false, "")) {
            errors.rejectValue("genderID", "genderID.error.message", "Giới tính không tồn tại");
        }

        // Validate length patient_id CMND
        validateLength(form.getPatientID(), 50, "patientID", "Số CMND không được quá 50 ký tự", errors);
        
        // Validate kí tự CMND
        if (StringUtils.isNotEmpty(form.getPatientID()) && !TextUtils.removeDiacritical(form.getPatientID().trim()).matches(RegexpEnum.CMND)
                && errors.getFieldError("patientID") == null) {
            errors.rejectValue("patientID", "patientID.error.message", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt/chữ tiếng việt, số âm, số thập phân");
        }
        // Validate nhóm đối tượng
        if (StringUtils.isNotEmpty(form.getObjectGroupID()) && !parameterService.fieldParameterExists(form.getObjectGroupID(),
                ParameterEntity.TEST_OBJECT_GROUP, false, "")) {
            errors.rejectValue("objectGroupID", "objectGroupID.error.message", "Nhóm đối tượng không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getProvinceID())
                && !provinceService.existsProvinceByID(form.getProvinceID())) {
            errors.rejectValue("provinceID", "provinceID.error.message", "Tỉnh thành không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getDistrictID())
                && !districtService.existsDistrictByID(form.getDistrictID(), form.getProvinceID())) {
            errors.rejectValue("districtID", "districtID.error.message", "Quận huyện không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getWardID())
                && !wardService.existsWardByID(form.getWardID(), form.getDistrictID())) {
            errors.rejectValue("wardID", "wardID.error.message", "Xã phường không tồn tại");
        }

        // Validate ngày nhận mẫu tại cơ sở sàng lọc nhỏ hơn ngày hiện tại
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (StringUtils.isNotEmpty(form.getSourceReceiveSampleTime()) && checkDateFormat(form.getSourceReceiveSampleTime(), "sourceReceiveSampleTime", "Ngày lấy mẫu phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date sourceReceiveSampleTime = sdfrmt.parse(form.getSourceReceiveSampleTime());
                if (sourceReceiveSampleTime.compareTo(new Date()) > 0 && errors.getFieldError("sourceReceiveSampleTime") == null) {
                    errors.rejectValue("sourceReceiveSampleTime", "sourceReceiveSampleTime.error.message", "Ngày lấy mẫu không được lớn hơn ngày hiện tại");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getSourceReceiveSampleTime()) && checkDateFormat(form.getSourceReceiveSampleTime(), "sourceReceiveSampleTime", "Ngày lấy mẫu phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date sourceReceiveSampleTime = sdfrmt.parse(form.getSourceReceiveSampleTime());
                Date sourceSampleDate = sdfrmt.parse(form.getSourceSampleDate());
                if (sourceReceiveSampleTime.compareTo(sourceSampleDate) > 0 && errors.getFieldError("sourceReceiveSampleTime") == null) {
                    errors.rejectValue("sourceReceiveSampleTime", "sourceReceiveSampleTime.error.message", "Ngày lấy mẫu không được lớn hơn ngày gửi mẫu");
                }
            }

        } catch (Exception e) {
        }

        // Validate available screening test result
        if (StringUtils.isNotEmpty(form.getTestResultsID()) && !parameterService.fieldParameterExists(form.getTestResultsID(),
                ParameterEntity.TEST_RESULTS, false, "")) {
            errors.rejectValue("testResultsID", "testResultsID.error.message", "Kết quả XN sàng lọc không tồn tại");
        }

        // Validate available sample quality 
        if (StringUtils.isNotEmpty(form.getSampleQuality()) && !parameterService.fieldParameterExists(form.getSampleQuality(),
                ParameterEntity.SAMPLE_QUALITY, false, "")) {
            errors.rejectValue("sampleQuality", "sampleQuality.error.message", "Chất lượng mẫu không tồn tại");
        }

        // Validate ngày gửi mẫu ( định dang dd/mm/yyyy, hiện tại > ngày gửi mẫu > ngày lấy mẫu)
        try {
            if (StringUtils.isNotEmpty(form.getSourceSampleDate()) && checkDateFormat(form.getSourceSampleDate(), "sourceSampleDate", "Ngày lấy mẫu phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date sourceSampleDate = sdfrmt.parse(form.getSourceSampleDate());
                Date sourceReceiveSampleTime = StringUtils.isNotEmpty(form.getSourceReceiveSampleTime()) ? sdfrmt.parse(form.getSourceReceiveSampleTime()) : null;
                if (sourceSampleDate.compareTo(new Date()) > 0 && errors.getFieldError("sourceSampleDate") == null) {
                    errors.rejectValue("sourceSampleDate", "sourceSampleDate.error.message", "Ngày gửi mẫu không được lớn hơn ngày hiện tại");
                }
                if (StringUtils.isNotEmpty(form.getSourceReceiveSampleTime()) && sourceSampleDate.compareTo(sourceReceiveSampleTime) < 0 && errors.getFieldError("sourceSampleDate") == null) {
                    errors.rejectValue("sourceSampleDate", "sourceSampleDate.error.message", "Ngày gửi mẫu không được nhỏ hơn ngày lấy mẫu");
                }
            }

        } catch (Exception e) {
        }

        // Validate ngày nhận mẫu ( định dang dd/mm/yyyy, hiện tại > ngày nhận mẫu > ngày gửi mẫu)
        try {
            if (StringUtils.isNotEmpty(form.getSampleReceiveTime()) && checkDateFormat(form.getSampleReceiveTime(), "sampleReceiveTime", "Ngày nhận mẫu phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date sampleReceiveTime = sdfrmt.parse(form.getSampleReceiveTime());
                Date sourceSampleDate = StringUtils.isNotEmpty(form.getSourceSampleDate()) ? sdfrmt.parse(form.getSourceSampleDate()) : null;
                if (sampleReceiveTime.compareTo(new Date()) > 0 && errors.getFieldError("sampleReceiveTime") == null) {
                    errors.rejectValue("sampleReceiveTime", "sampleReceiveTime.error.message", "Ngày nhận mẫu không được lớn hơn ngày hiện tại");
                }
                if (StringUtils.isNotEmpty(form.getSourceSampleDate()) && sampleReceiveTime.compareTo(sourceSampleDate) < 0 && errors.getFieldError("sampleReceiveTime") == null) {
                    errors.rejectValue("sampleReceiveTime", "sampleReceiveTime.error.message", "Ngày nhận mẫu không được nhỏ hơn ngày gửi mẫu");
                }
            }
        } catch (Exception e) {
        }
        // Validate Ngày nhận mẫu <= Ngày tiếp nhận:
//        try {
//            if (StringUtils.isNotEmpty(form.getSampleReceiveTime()) && checkDateFormat(form.getSampleReceiveTime(), "sampleReceiveTime", "Ngày nhận mẫu phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
//                Date sampleReceiveTime = sdfrmt.parse(form.getSampleReceiveTime());
//                Date sourceSampleDate = StringUtils.isNotEmpty(form.getSourceSampleDate()) ? sdfrmt.parse(form.getSourceSampleDate()) : null;
//                if (sampleReceiveTime.compareTo(new Date()) > 0 && errors.getFieldError("sampleReceiveTime") == null) {
//                    errors.rejectValue("sampleReceiveTime", "sampleReceiveTime.error.message", "Ngày nhận mẫu không được lớn hơn ngày hiện tại");
//                }
//                if (StringUtils.isNotEmpty(form.getSourceSampleDate()) && sampleReceiveTime.compareTo(sourceSampleDate) < 0 && errors.getFieldError("sampleReceiveTime") == null) {
//                    errors.rejectValue("sampleReceiveTime", "sampleReceiveTime.error.message", "Ngày nhận mẫu không được nhỏ hơn ngày gửi mẫu");
//                }
//            }
//        } catch (Exception e) {
//        }
        // Validate ngày tiếp nhận ( định dang dd/mm/yyyy, hiện tại >= ngày tiếp nhận >= ngày nhận mẫu)
        try {
            if (StringUtils.isNotEmpty(form.getAcceptDate()) && checkDateFormat(form.getAcceptDate(), "acceptDate", "Ngày tiếp nhận phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date acceptDate = sdfrmt.parse(form.getAcceptDate());
                Date sampleReceiveTime = StringUtils.isNotEmpty(form.getSampleReceiveTime()) ? sdfrmt.parse(form.getSampleReceiveTime()) : null;
                if (acceptDate.compareTo(new Date()) > 0 && errors.getFieldError("acceptDate") == null) {
                    errors.rejectValue("acceptDate", "acceptDate.error.message", "Ngày tiếp nhận không được lớn hơn ngày hiện tại");
                }
                if (StringUtils.isNotEmpty(form.getSampleReceiveTime()) && acceptDate.compareTo(sampleReceiveTime) < 0 && errors.getFieldError("acceptDate") == null) {
                    errors.rejectValue("acceptDate", "acceptDate.error.message", "Ngày tiếp nhận không được nhỏ hơn ngày nhận mẫu");
                }
            }

        } catch (Exception e) {
        }

//        if (StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("acceptDate") == null) {
//            validateNull(form.getAcceptDate(), "acceptDate", "Ngày tiếp nhận không được để trống", errors);
//        }
//
        validateLength(form.getBioName1(), 100, "bioName1", "Tên sinh phẩm không được quá 100 ký tự", errors);
        validateLength(form.getBioName2(), 100, "bioName2", "Tên sinh phẩm không được quá 100 ký tự", errors);
        validateLength(form.getBioName3(), 100, "bioName3", "Tên sinh phẩm không được quá 100 ký tự", errors);

        // Validate available sp1
        if (StringUtils.isNotEmpty(form.getBioNameResult1()) && !parameterService.fieldParameterExists(form.getBioNameResult1(),
                ParameterEntity.BIO_NAME_RESULT, false, "")) {
            errors.rejectValue("bioNameResult1", "bioNameResult1.error.message", "Kết quả không tồn tại");
        }
        // Validate available sp2
        if (StringUtils.isNotEmpty(form.getBioNameResult2()) && !parameterService.fieldParameterExists(form.getBioNameResult2(),
                ParameterEntity.BIO_NAME_RESULT, false, "")) {
            errors.rejectValue("bioNameResult2", "bioNameResult2.error.message", "Kết quả không tồn tại");
        }
        // Validate available sp1
        if (StringUtils.isNotEmpty(form.getBioNameResult3()) && !parameterService.fieldParameterExists(form.getBioNameResult3(),
                ParameterEntity.BIO_NAME_RESULT, false, "")) {
            errors.rejectValue("bioNameResult3", "bioNameResult3.error.message", "Kết quả không tồn tại");
        }

        // Validate length other technical
        validateLength(form.getOtherTechnical(), 500, "otherTechnical", "Tên kỹ thuật khác không được quá 500 ký tự", errors);

        // Validate available confirm test result
        if (StringUtils.isNotEmpty(form.getResultsID()) && !parameterService.fieldParameterExists(form.getResultsID(),
                ParameterEntity.TEST_RESULTS_CONFIRM, false, "")) {
            errors.rejectValue("resultsID", "resultsID.error.message", "Kết quả XN khẳng định không tồn tại");
        }

        // Validate ngày XN khẳng định
        try {
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && checkDateFormat(form.getConfirmTime(), "confirmTime", "Ngày XN khẳng định phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date sampleReceiveTime = StringUtils.isNotEmpty(form.getSampleReceiveTime()) ? sdfrmt.parse(form.getSampleReceiveTime()) : null;
                Date resultsReturnTime = StringUtils.isNotEmpty(form.getResultsReturnTime()) ? sdfrmt.parse(form.getResultsReturnTime()) : null;
                if (confirmTime.compareTo(new Date()) > 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn ngày hiện tại");
                }
                if (StringUtils.isNotEmpty(form.getSampleReceiveTime()) && confirmTime.compareTo(sampleReceiveTime) < 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được nhỏ hơn ngày nhận mẫu");
                }
                if (StringUtils.isNotEmpty(form.getResultsReturnTime()) && confirmTime.compareTo(resultsReturnTime) > 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được lớn hơn Ngày trả kết quả XN khẳng định");
                }

            }
        } catch (Exception e) {
        }

        // Kết quả XN khẳng định không được để trống" (nếu người dùng đã nhập Ngày XN khẳng định)
        if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isEmpty(form.getResultsID()) && errors.getFieldError("resultsID") == null) {
            errors.rejectValue("resultsID", "resultsID.error.message", "Kết quả XN khẳng định không được để trống");
        }

        // Validate ngày trả kết quả XN khẳng định 
        try {
            if (StringUtils.isNotEmpty(form.getResultsReturnTime()) && checkDateFormat(form.getResultsReturnTime(), "resultsReturnTime", "Ngày trả kết quả XN khẳng định phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date confirmTime = StringUtils.isNotEmpty(form.getConfirmTime()) ? sdfrmt.parse(form.getConfirmTime()) : null;
                Date resultsReturnTime = sdfrmt.parse(form.getResultsReturnTime());
                if (resultsReturnTime.compareTo(new Date()) > 0) {
                    errors.rejectValue("resultsReturnTime", "resultsReturnTime.error.message", "Ngày trả kết quả XN khẳng định không được lớn hơn ngày hiện tại");
                }
                if (resultsReturnTime.compareTo(confirmTime) < 0) {
                    errors.rejectValue("resultsReturnTime", "resultsReturnTime.error.message", "Ngày trả kết quả XN khẳng định không được nhỏ hơn Ngày XN khẳng định");
                }
            }
        } catch (Exception e) {
        }
        
        // Validate mã lưu mẫu
        if (StringUtils.isNotEmpty(form.getSampleSaveCode()) && errors.getFieldError("sampleSaveCode") == null) {
            validateLength(form.getSampleSaveCode(), 20, "sampleSaveCode", "Mã số lưu mẫu không được quá 20 ký tự", errors);
        }
        
        // Validate định dạng mã lưu mẫu
        if (StringUtils.isNotEmpty(form.getSampleSaveCode()) && !TextUtils.removeDiacritical(form.getSampleSaveCode().trim()).matches(RegexpEnum.SAMPLE_SAVE_CODE)
                && errors.getFieldError("sampleSaveCode") == null) {
            errors.rejectValue("sampleSaveCode", "sampleSaveCode.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }
        
        
        try {
            // Validate ngày làm mẫu sinh phẩm xét nghiệm
            Date sampleReceiveDate = StringUtils.isNotEmpty(form.getSampleReceiveTime()) ? sdfrmt.parse(form.getSampleReceiveTime()) : null;
            if (StringUtils.isNotEmpty(form.getFirstBioDate()) && checkDateFormat(form.getFirstBioDate(), "firstBioDate", "Ngày làm XN phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date firstBioDate = sdfrmt.parse(form.getFirstBioDate());
                
                if (firstBioDate.compareTo(new Date()) > 0 && errors.getFieldError("firstBioDate") == null) {
                    errors.rejectValue("firstBioDate", "firstBioDate.error.message", "Ngày làm XN không được lớn hơn ngày hiện tại");
                }
                if (StringUtils.isNotEmpty(form.getSampleReceiveTime()) && firstBioDate.compareTo(sampleReceiveDate) < 0 && errors.getFieldError("firstBioDate") == null) {
                    errors.rejectValue("firstBioDate", "firstBioDate.error.message", "Ngày làm XN không được nhỏ hơn ngày nhận mẫu");
                }
            }
            if (StringUtils.isNotEmpty(form.getSecondBioDate()) && checkDateFormat(form.getSecondBioDate(), "secondBioDate", "Ngày làm XN phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date secondBioDate = sdfrmt.parse(form.getSecondBioDate());
                if (secondBioDate.compareTo(new Date()) > 0 && errors.getFieldError("secondBioDate") == null) {
                    errors.rejectValue("secondBioDate", "secondBioDate.error.message", "Ngày làm XN không được lớn hơn ngày hiện tại");
                }
                if (StringUtils.isNotEmpty(form.getSampleReceiveTime()) && secondBioDate.compareTo(sampleReceiveDate) < 0 && errors.getFieldError("secondBioDate") == null) {
                    errors.rejectValue("secondBioDate", "secondBioDate.error.message", "Ngày làm XN không được nhỏ hơn ngày nhận mẫu");
                }
            }
            if (StringUtils.isNotEmpty(form.getThirdBioDate()) && checkDateFormat(form.getThirdBioDate(), "thirdBioDate", "Ngày làm XN phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date thirdBioDate = sdfrmt.parse(form.getThirdBioDate());
                if (thirdBioDate.compareTo(new Date()) > 0 && errors.getFieldError("thirdBioDate") == null) {
                    errors.rejectValue("thirdBioDate", "thirdBioDate.error.message", "Ngày làm XN không được lớn hơn ngày hiện tại");
                }
                if (StringUtils.isNotEmpty(form.getSampleReceiveTime()) && thirdBioDate.compareTo(sampleReceiveDate) < 0 && errors.getFieldError("thirdBioDate") == null) {
                    errors.rejectValue("thirdBioDate", "thirdBioDate.error.message", "Ngày làm XN không được nhỏ hơn ngày nhận mẫu");
                }
            }
            
            // Validate ngày xét nghiệm nhiễm mới HIV, và ngày xét nghiệm tải lượng virus
            if (StringUtils.isNotEmpty(form.getVirusLoadDate()) && checkDateFormat(form.getVirusLoadDate(), "virusLoadDate", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date virusLoadDate = sdfrmt.parse(form.getVirusLoadDate());
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (virusLoadDate.compareTo(new Date()) > 0 && errors.getFieldError("virusLoadDate") == null) {
                    errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng vi rút không được lớn hơn ngày hiện tại");
                }
                
                if (StringUtils.isNotEmpty(form.getConfirmTime()) && virusLoadDate.compareTo(confirmTime) < 0 && errors.getFieldError("virusLoadDate") == null) {
                    errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng vi rút không được nhỏ hơn ngày XN khẳng định");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getEarlyHivDate()) && checkDateFormat(form.getEarlyHivDate(), "earlyHivDate", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt)) {
                Date earlyHivDate = sdfrmt.parse(form.getEarlyHivDate());
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (earlyHivDate.compareTo(new Date()) > 0 && errors.getFieldError("earlyHivDate") == null) {
                    errors.rejectValue("earlyHivDate", "earlyHivDate.error.message", "Ngày XN nhiễm mới HIV không được lớn hơn ngày hiện tại");
                }
                
                if (StringUtils.isNotEmpty(form.getConfirmTime()) && earlyHivDate.compareTo(confirmTime) < 0 && errors.getFieldError("earlyHivDate") == null) {
                    errors.rejectValue("earlyHivDate", "earlyHivDate.error.message", "Ngày XN nhiễm mới HIV không được nhỏ hơn ngày XN khẳng định");
                }
            }
            
        } catch (Exception e) {
        }
        
        if (StringUtils.isNotEmpty(form.getSampleSentSource()) && !TextUtils.removeDiacritical(form.getSampleSentSource().trim()).matches(RegexpEnum.ADDRESS)
                && errors.getFieldError("sampleSentSource") == null) {
            errors.rejectValue("sampleSentSource", "sampleSentSource.error.message", "Nơi gửi bệnh phẩm không được chứa ký tự đặc biệt");
        }
        
        // Validae length serviceID
        if (StringUtils.isNotEmpty(form.getServiceID()) && errors.getFieldError("serviceID") == null) {
            validateLength(form.getServiceID(), 50, "serviceID", "Loại dịch vụ không được quá 50 ký tự", errors);
        }
        
        // Validate ràng buộc kết quả khẳng định với các trường khác
        if (ConfirmTestResultEnum.DUONG_TINH.equals(form.getResultsID())) {
            if (StringUtils.isEmpty(form.getYear()) && errors.getFieldError("year") == null) {
                errors.rejectValue("year", "year.error.message", "Năm sinh không được để trống");
            }
            validateNull(form.getCurrentProvinceID(), "currentProvinceID", "Tỉnh thành không được để trống", errors);
            validateNull(form.getCurrentDistrictID(), "currentDistrictID", "Quận huyện không được để trống", errors);
            validateNull(form.getCurrentWardID(), "currentWardID", "Phường xã không được để trống", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getEarlyHiv()) && errors.getFieldError("earlyHivDate") == null) {
            validateNull(form.getEarlyHivDate(), "earlyHivDate", "Ngày XN nhiễm mới HIV không được để trống", errors);
        }
        if (StringUtils.isNotEmpty(form.getEarlyHivDate()) && errors.getFieldError("earlyHiv") == null) {
            validateNull(form.getEarlyHiv(), "earlyHiv", "KQXN nhiễm mới HIV không được để trống", errors);
        }
        if (StringUtils.isNotEmpty(form.getVirusLoad()) && errors.getFieldError("virusLoadDate") == null) {
            validateNull(form.getVirusLoadDate(), "virusLoadDate", "Ngày XN tải lượng vi rút không được để trống", errors);
        }
        if (StringUtils.isNotEmpty(form.getVirusLoadDate()) && errors.getFieldError("virusLoad") == null) {
            validateNull(form.getVirusLoad(), "virusLoad", "KQXN tải lượng vi rút không được để trống", errors);
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

    // Check null or empty 
    private void validateNull(String inputValue, String id, String message, Errors errors) {
        if (StringUtils.isEmpty(inputValue) || inputValue == null) {
            errors.rejectValue(id, id + ".error.message", message);
        }
    }

}

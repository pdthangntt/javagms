package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.HtcVisitForm;
import com.gms.service.DistrictService;
import com.gms.service.HtcVisitService;
import com.gms.service.ParameterService;
import com.gms.service.ProvinceService;
import com.gms.service.WardService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Custom validate HtcForm
 *
 * @author TrangBN
 */
@Component
public class HtcVisitValidate implements Validator {

    @Autowired
    private HtcVisitService htcVisitService;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcVisitForm.class);
    }

    private static final String PHONE_NUMBER = "(^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$)|(02[4|5|7|2|6|8|9])+([0-9]{8}$)";
    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-]+[^-]$)";

    @Override
    public void validate(Object o, Errors errors) {

        final String REACTIVE_TEST_RESULT = "2";
        final String CONTINUE_TEST = "true";
        final String ACCEPT_EXCHANGE = "1";
        final String PARTNER_CODE = "2";
        final String APPROACH_NO = "1";
        final String AGREE_PRE_TEST = "1";

        HtcVisitForm form = (HtcVisitForm) o;

        HashMap<String, String> siteConfig = new HashMap<>();
        siteConfig = parameterService.getSiteConfig(form.getCurrentSiteID());

        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        validateLength(form.getPermanentAddress(), 500, "permanentAddress", "Số nhà không được quá 500 ký tự", errors);

        validateLength(form.getCode(), 18, "code", "Mã khách hàng không được quá 18 ký tự.", errors);

        validateLength(form.getPermanentAddressGroup(), 500, "permanentAddressGroup", "Đường phố không được quá 500 ký tự", errors);

        validateLength(form.getPermanentAddressStreet(), 500, "permanentAddressStreet", "Tổ/ấp/Khu phố không được quá 500 ký tự", errors);

        // Validate bắt buộc nếu kết quả xét nghiệm có phản ứng
        if (REACTIVE_TEST_RESULT.equals(form.getTestResultsID())) {

            
            validateNull(form.getJobID(), "jobID", "Nghề nghiệp không được để trống", errors);
            
            if (form.getRiskBehaviorID() == null || form.getRiskBehaviorID().isEmpty()) {
                errors.rejectValue("riskBehaviorID", "riskBehaviorID.error.message", "Hành vi nguy cơ không được để trống");
            }
            
            validateNull(form.getModeOfTransmission(), "modeOfTransmission", "Đường lấy truyền không được để trống", errors);
            
            // Validate Họ tên
            validateNull(form.getPatientName(), "patientName", "Họ tên không được để trống", errors);

            // Validate A7. Đối chiếu CMND/Giấy tờ khác
            validateNull(form.getPatientIDAuthen(), "patientIDAuthen", "Câu trả lời không được để trống", errors);

            // Validate dân tộc
            validateNull(form.getRaceID(), "raceID", "Dân tộc không được để trống", errors);

            // Validate year of birth
            validateNull(form.getYearOfBirth(), "yearOfBirth", "Năm sinh không được để trống", errors);

            // Validate địa chỉ thường trú
//            validateNull(form.getPermanentAddress(), "permanentAddress", "Địa chỉ thường trú không được để trống", errors);
            validateNull(form.getPermanentProvinceID(), "permanentProvinceID", "Tỉnh thành không được để trống", errors);
            validateNull(form.getPermanentDistrictID(), "permanentDistrictID", "Quận huyện không được để trống", errors);
            validateNull(form.getPermanentWardID(), "permanentWardID", "Phường xã không được để trống", errors);

            // Validate địa chỉ cư trú hiện tại
            if (form.getIsDisplayCopy() != null && !form.getIsDisplayCopy()) {
//                validateNull(form.getCurrentAddress(), "currentAddress", "Địa chỉ tạm trú không được thiếu", errors);
                validateNull(form.getCurrentProvinceID(), "currentProvinceID", "Tỉnh thành không được để trống", errors);
                validateNull(form.getCurrentDistrictID(), "currentDistrictID", "Quận huyện không được để trống", errors);
                validateNull(form.getCurrentWardID(), "currentWardID", "Phường xã không được để trống", errors);
            }

        }

        // Validate CMND 
        if (StringUtils.isEmpty(form.getPatientID()) && StringUtils.isNotEmpty(siteConfig.get(SiteConfigEnum.HTC_RQ_ID.getKey()))
                && siteConfig.get(SiteConfigEnum.HTC_RQ_ID.getKey()).equals("1")
                && StringUtils.isNotEmpty(form.getTestResultsID())
                && form.getTestResultsID().equals(TestResultEnum.CO_PHAN_UNG.getKey())
                && errors.getFieldError("patientID") == null) {
            errors.rejectValue("patientID", "patientID.error.message", "Số CMND/Giấy tờ khác không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getPatientID()) && !TextUtils.removeDiacritical(form.getPatientID().trim()).matches(RegexpEnum.CMND)
                && errors.getFieldError("patientID") == null) {
            errors.rejectValue("patientID", "patientID.error.message", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt/chữ tiếng việt, số âm, số thập phân");
        }

        if (StringUtils.isNotEmpty(form.getCode()) && !TextUtils.removeDiacritical(form.getCode().trim()).matches(RegexpEnum.CODE)
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã không đúng định dạng (Không chứa tự đặc biệt, chỉ chứa số, chữ và dấu -");
        }

        // Check not null cho loại dịch vụ 
        validateNull(form.getServiceID(), "serviceID.error.message", "Loại hình dịch vụ không được để trống", errors);

        // Check null cho nhóm đối tượng
        validateNull(form.getObjectGroupID(), "objectGroupID.error.message", "Nhóm đối tượng không được để trống", errors);

        // Check null cho câu trả lời đồng ý XN sàng lọc
//        validateNull(form.getIsAgreePreTest(), "isAgreePreTest.error.message", "Câu trả lời không được để trống", errors);
        // Kiểm tra định ký tự đặc biêt của tên khách hàng
        if (StringUtils.isNotBlank(form.getPatientName()) && !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)
                && errors.getFieldError("patientName") == null) {
            errors.rejectValue("patientName", "patientName.error.message", "Họ và tên chỉ chứa các kí tự chữ cái");
        }

        // Validate gender
        validateNull(form.getGenderID(), "genderID", "Giới tính không được trống", errors);

        // Validate inputted fields
        if (PARTNER_CODE.equals(form.getReferralSource()) && StringUtils.isEmpty(form.getYouInjectCode()) && errors.getFieldError("youInjectCode") == null) {
            validateNull(form.getYouInjectCode(), "youInjectCode", "Mã bạn tình bạn chích không được trống", errors);
        }

        if (PARTNER_CODE.equals(form.getReferralSource()) && StringUtils.isEmpty(form.getYouInjectCode()) && errors.getFieldError("youInjectCode") == null) {
            validateLength(form.getYouInjectCode(), 50, "youInjectCode", "Mã số bạn tình/bạn chích không được quá 50 ký tự", errors);
        }

        if (APPROACH_NO.equals(form.getReferralSource()) && StringUtils.isEmpty(form.getYouInjectCode()) && errors.getFieldError("approacherNo") == null) {
            validateNull(form.getApproacherNo(), "approacherNo", "Mã tiếp cận cộng đồng không được trống", errors);
        }

        if (APPROACH_NO.equals(form.getReferralSource()) && StringUtils.isEmpty(form.getYouInjectCode()) && errors.getFieldError("approacherNo") == null) {
            validateLength(form.getApproacherNo(), 50, "approacherNo", "Mã số tiếp cận cộng đồng không được quá 50 ký tự", errors);
        }

//        if (StringUtils.isNotEmpty(form.getHasHealthInsurance()) && HAS_HEALTH_INSURANCE_NO.equals(form.getHealthInsuranceNo())) {
//            validateNull(form.getHealthInsuranceNo(), "healthInsuranceNo", "Mã số BHYT không được trống", errors);
//        }
        if (StringUtils.isNotEmpty(form.getHasHealthInsurance()) && "1".equals(form.getHasHealthInsurance())
                && errors.getFieldError("healthInsuranceNo") == null) {
            validateLength(form.getHealthInsuranceNo(), 50, "healthInsuranceNo", "Số thẻ BHYT không quá 50 ký tự", errors);
        }
        if (StringUtils.isNotEmpty(form.getHasHealthInsurance()) && "1".equals(form.getHasHealthInsurance()) && !"".equals(form.getHealthInsuranceNo())
                && form.getHealthInsuranceNo() != null && errors.getFieldError("healthInsuranceNo") == null && !form.getHealthInsuranceNo().matches(RegexpEnum.HI_CHAR)) {
            errors.rejectValue("healthInsuranceNo", "healthInsuranceNo.error.message", "Số thẻ BHYT chỉ chứa kí tự chữ và số dấu - và _");
        }

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

        // Validate tồn tại code khi update
        if (form.getID() != null && htcVisitService.checkExistCodeUpdate(form.getCode(), form.getID(), form.getSiteID())
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã khách hàng đã tồn tại.");
        }

        if (form.getID() == null && htcVisitService.existCodeAndSiteID(form.getCode(), form.getSiteID())
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã khách hàng đã tồn tại.");
        }

        if (form.getID() == null && StringUtils.isEmpty(form.getCode()) && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã khách hàng không để trống");
        }

        if (StringUtils.isNotEmpty(form.getCode()) && errors.getFieldError("code") == null && !TextUtils.removeDiacritical(form.getCode().trim()).matches(CODE_REGEX)) {
            errors.rejectValue("code", "code.error.message", "Mã khách hàng không chứa ký tự đặc biệt và bắt đầu bằng dấu cách");
        }

        // Check trùng B4. Mã do CS XN sàng lọc cấp:
        if (form.getID() != null && StringUtils.isNotEmpty(form.getTestNoFixSite()) && !form.getTestNoFixSite().equals(form.getCode())
                && htcVisitService.checkExistCodeUpdate(form.getTestNoFixSite(), form.getID(), form.getSiteID())
                && errors.getFieldError("testNoFixSite") == null) {
            errors.rejectValue("testNoFixSite", "testNoFixSite.error.message", "Mã khách hàng đã tồn tại.");
        }

        if (form.getID() == null && StringUtils.isNotEmpty(form.getTestNoFixSite())
                && !form.getTestNoFixSite().equals(form.getCode())
                && htcVisitService.existCodeAndSiteID(form.getTestNoFixSite(), form.getSiteID())
                && errors.getFieldError("code") == null) {
            errors.rejectValue("testNoFixSite", "testNoFixSite.error.message", "Mã khách hàng đã tồn tại.");
        }

        validateLength(form.getPatientName(), 100, "patientName", "Họ tên không được quá 100 ký tự", errors);

        if (StringUtils.isNotEmpty(form.getRaceID()) && !parameterService.fieldParameterExists(form.getRaceID(),
                ParameterEntity.RACE, false, "") && errors.getFieldError("raceID") == null) {
            errors.rejectValue("raceID", "raceID.error.message", "Dân tộc không tồn tại.");
        }

        if (StringUtils.isNotEmpty(form.getServiceID()) && !parameterService.fieldParameterExists(form.getServiceID(),
                ParameterEntity.SERVICE_TEST, false, "") && errors.getFieldError("serviceID") == null) {
            errors.rejectValue("serviceID", "serviceID.error.message", "Dịch vụ không tồn tại.");
        }

        if (StringUtils.isNotEmpty(form.getGenderID()) && !parameterService.fieldParameterExists(form.getGenderID(),
                ParameterEntity.GENDER, false, "")) {
            errors.rejectValue("genderID", "genderID.error.message", "Giới tính không tồn tại");
        }

        if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && !form.getYearOfBirth().matches("[0-9]{0,4}")) {
            errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh gồm bốn chữ số vd: 1994");
        } else if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && (Integer.parseInt(form.getYearOfBirth()) < 1900
                || Integer.parseInt(form.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR))) {
            errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh hợp lệ từ 1900 - năm hiện tại");
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

        if (form.getIsDisplayCopy() != null && !form.getIsDisplayCopy()) {

            validateLength(form.getCurrentAddress(), 500, "currentAddress", "Số nhà không được quá 500 ký tự", errors);

            validateLength(form.getCurrentAddressGroup(), 500, "currentAddressGroup", "Đường phố không được quá 500 ký tự", errors);

            validateLength(form.getCurrentAddressStreet(), 500, "currentAddressStreet", "Tổ/ấp/Khu phố không được quá 500 ký tự", errors);

            if (StringUtils.isNotEmpty(form.getCurrentProvinceID()) && !provinceService.existsProvinceByID(form.getCurrentProvinceID())) {
                errors.rejectValue("currentProvinceID", "currentProvinceID.error.message", "Tỉnh thành không tồn tại");
            }

            if (StringUtils.isNotEmpty(form.getCurrentDistrictID()) && !districtService.existsDistrictByID(form.getCurrentDistrictID(), form.getCurrentProvinceID())) {
                errors.rejectValue("currentDistrictID", "currentDistrictID.error.message", "Quận huyện không tồn tại");
            }

            if (StringUtils.isNotEmpty(form.getCurrentWardID())
                    && !wardService.existsWardByID(form.getCurrentWardID(), form.getCurrentDistrictID())) {
                errors.rejectValue("currentWardID", "currentWardID.error.message", "Xã phường không tồn tại");
            }
        }

        // Validate XN sàng lọc và các trường liên quan
        if (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && AGREE_PRE_TEST.equals(form.getIsAgreePreTest())) {

//            validateNull(form.getPreTestTime(), "preTestTime", "Ngày xét nghiệm sàng lọc không được trống", errors);
            checkDateFormat(form.getPreTestTime(), "preTestTime", "Nhập đúng định dạng theo dd/mm/yyyy", errors, sdfrmt);

        }

        try {
            // Ngày XN sàng lọc
            if (errors.getFieldError("preTestTime") == null) {
                if (StringUtils.isNotEmpty(form.getPreTestTime())) {
                    Date today = new Date();
                    Date preTestTime = sdfrmt.parse(form.getPreTestTime());
                    if (preTestTime.compareTo(today) > 0) {
                        errors.rejectValue("preTestTime", "preTestTime.error.message", "Không được sau ngày hiện tại");
                    }
                }
            }
        } catch (Exception e) {
        }

        // đồng ý tiếp tục xét nghiệm
        if ((REACTIVE_TEST_RESULT.equals(form.getTestResultsID()) || "3".equals(form.getTestResultsID()) || "4".equals(form.getTestResultsID())) && CONTINUE_TEST.equals(form.getIsAgreeTest())) {

            // Validate tên cơ sở xét nghiệm khẳng định
            validateNull(form.getSiteConfirmTest(), "siteConfirmTest", "Nơi chuyển máu đến để XN khẳng định không được để trống", errors);
            validateLength(form.getSiteConfirmTest(), 220, "siteConfirmTest", "Tên cơ sở xét nghiệm khẳng định không được quá 220 ký tự ", errors);

            // Ngày xét nghiệm khẳng định không null khi có kết quả xn khẳng định
            if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && StringUtils.isEmpty(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
                errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày xét nghiệm khẳng định không được trống");
            }
            if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
                if (TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getConfirmTime()).charAt(0) == '0') {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày xét nghiệm khẳng định không đúng định dạng");
                }
            }

            // Validate required other fields if has value for "Kết quả xét nghiệm khẳng định"
            if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && !parameterService.fieldParameterExists(form.getConfirmResultsID(), ParameterEntity.TEST_RESULTS_CONFIRM, false, "")) {
                errors.rejectValue("confirmResultsID", "confirmResultsID.error.message", "Kết quả xét nghiệm khẳng định không tồn tại");
            }

            // validate chỉ dùng XN nhiễm mới Asante với TVXN cố định và lưu động
            if (StringUtils.isNotEmpty(form.getAsanteTest()) && "KC".equals(form.getServiceID())) {
                errors.rejectValue("asanteTest", "asanteTest.error.message", "Chỉ XN nhiễm mới Asante với TVXN cố định và lưu động");
            }

            // Validate C5 C6 C7
            try {
                // Validate ngày xét nghiệm khẳng định
                if (StringUtils.isNotEmpty(form.getConfirmTime())) {
                    checkDateFormat(form.getConfirmTime(), "confirmTime", "Nhập đúng định dạng theo dd/mm/yyyy", errors, sdfrmt);
                    if (errors.getFieldError("resultsSiteTime") == null) {
                        validateNull(form.getResultsSiteTime(), "resultsSiteTime", "Ngày cơ sở nhận kết quả không được trống", errors);
                    }
                    if (errors.getFieldError("confirmResultsID") == null) {
                        validateNull(form.getConfirmResultsID(), "confirmResultsID", "Kết quả xét nghiệm khẳng định không được thiếu", errors);
                    }
                }

                // Validate ngày cơ sở nhận kết quả khẳng định
                if (StringUtils.isNotEmpty(form.getResultsSiteTime())) {
                    checkDateFormat(form.getResultsSiteTime(), "resultsSiteTime", "Định dạng ngày cơ sở nhận kết quả XN khẳng định không đúng dd/mm/yyyy", errors, sdfrmt);
                }

                Date c5 = null;
                Date c6 = null;
                Date c7 = null;

                // Validate C5 <= C6
                if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getResultsSiteTime())) {
                    c5 = sdfrmt.parse(form.getConfirmTime());
                    c6 = sdfrmt.parse(form.getResultsSiteTime());
                    if (c5.compareTo(c6) > 0) {
                        errors.rejectValue("resultsSiteTime", "resultsSiteTime.error.message", "Ngày cơ sở nhận KQ khẳng định không được nhỏ hơn ngày XN khẳng định");
                    }
                }

                // Ràng buộc C5 khi C6 có giá trị
                if (StringUtils.isEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getResultsSiteTime()) && errors.getFieldError("confirmTime") == null) {
                    validateNull(form.getConfirmTime(), "confirmTime", "Ngày xét nghiệm khẳng định không được trống", errors);
                }

                // Validate C5. Ngày xét nghiệm khẳng định >= B2. Ngày XN sàng lọc
                if (StringUtils.isNotEmpty(form.getPreTestTime()) && StringUtils.isNotEmpty(form.getConfirmTime())) {
                    c5 = sdfrmt.parse(form.getConfirmTime());
                    Date preTestDate = sdfrmt.parse(form.getPreTestTime());
                    if (c5.compareTo(preTestDate) < 0) {
                        errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định không được trước ngày làm XN sàng lọc ");
                    }
                }

                // Validate C7. Ngày khách hàng nhận kết quả khẳng định <= D1.Ngày tư vấn chuyển gửi điều trị ARV
//                if (StringUtils.isNotEmpty(form.getResultsPatienTime()) && StringUtils.isNotEmpty(form.getExchangeConsultTime())) {
//                    c7 = sdfrmt.parse(form.getResultsPatienTime());
//                    Date exchangeDate = sdfrmt.parse(form.getExchangeConsultTime());
//                    if (exchangeDate.compareTo(c7) < 0) {
//                        errors.rejectValue("exchangeConsultTime", "exchangeConsultTime.error.message", "Ngày tư vấn chuyển gửi điều trị ARV không được trước ngày khách hàng nhận kết quả khẳng định");
//                    }
//                }
                if (StringUtils.isEmpty(form.getExchangeConsultTime())
                        && (StringUtils.isNotEmpty(form.getPartnerProvideResult()) || StringUtils.isNotEmpty(form.getArvExchangeResult()))
                        && errors.getFieldError("exchangeConsultTime") == null) {
                    errors.rejectValue("exchangeConsultTime", "exchangeConsultTime.error.message", "Ngày tư vấn chuyển gửi điều trị ARV không được để trống");
                }

                // Validate ràng buộc ngày C6 <= C7
                if (StringUtils.isNotEmpty(form.getResultsPatienTime())) {
                    checkDateFormat(form.getResultsPatienTime(), "resultsPatienTime", "Định dạng ngày khách hàng nhận kết quả xét nghiệm khẳng định không đúng dd/mm/yyyy", errors, sdfrmt);
                    if (errors.getFieldError("resultsSiteTime") == null) {
                        validateNull(form.getResultsSiteTime(), "resultsSiteTime", "Ngày cơ sở nhận kết quả khẳng định không được để trống", errors);
                    }

                    c7 = sdfrmt.parse(form.getResultsPatienTime());
                    c6 = sdfrmt.parse(form.getResultsSiteTime());

                    if (c7.compareTo(c6) < 0) {
                        errors.rejectValue("resultsPatienTime", "resultsPatienTime.error.message", "Ngày khách hàng nhận kết quả khẳng định không được nhỏ hơn ngày cơ sở nhận kết quả khẳng định");
                    }
                }

            } catch (Exception e) {
            }

            // Validate ngày không đươc lớn hơn ngày hiện tại
            try {

                // Ngày xét nghiệm khẳng định
                if (errors.getFieldError("confirmTime") == null) {
                    if (StringUtils.isNotEmpty(form.getConfirmTime())) {
                        Date today = new Date();
                        Date confirmDate = sdfrmt.parse(form.getConfirmTime());
                        if (confirmDate.compareTo(today) > 0) {
                            errors.rejectValue("confirmTime", "confirmTime.error.message", "Không được sau ngày hiện tại");
                        }
                    }
                }

                // Ngày cơ sở nhận kết quả KĐ
                if (errors.getFieldError("resultsSiteTime") == null) {
                    if (StringUtils.isNotEmpty(form.getResultsSiteTime())) {
                        Date today = new Date();
                        Date resultsSiteTime = sdfrmt.parse(form.getResultsSiteTime());
                        if (resultsSiteTime.compareTo(today) > 0) {
                            errors.rejectValue("resultsSiteTime", "resultsSiteTime.error.message", "Không được sau ngày hiện tại");
                        }
                    }
                }

                // Ngày khách hàng nhận kết quả KĐ
                if (errors.getFieldError("resultsPatienTime") == null) {
                    if (StringUtils.isNotEmpty(form.getResultsPatienTime())) {
                        Date today = new Date();
                        Date resultsPatienTime = sdfrmt.parse(form.getResultsPatienTime());
                        if (resultsPatienTime.compareTo(today) > 0) {
                            errors.rejectValue("resultsPatienTime", "resultsPatienTime.error.message", "Không được sau ngày hiện tại");
                        }
                    }
                }

                // Ngày tư vấn chuyển gửi điều trị ARV
                if (errors.getFieldError("exchangeConsultTime") == null) {
                    if (StringUtils.isNotEmpty(form.getExchangeConsultTime())) {
                        Date today = new Date();
                        Date exchangeConsultTime = sdfrmt.parse(form.getExchangeConsultTime());
                        if (exchangeConsultTime.compareTo(today) > 0) {
                            errors.rejectValue("exchangeConsultTime", "exchangeConsultTime.error.message", "Không được sau ngày hiện tại");
                        }
                    }
                }

                // Ngày chuyển gửi
                if (errors.getFieldError("exchangeTime") == null) {
                    if (StringUtils.isNotEmpty(form.getExchangeTime())) {
                        Date today = new Date();
                        Date exchangeTime = sdfrmt.parse(form.getExchangeTime());
                        if (exchangeTime.compareTo(today) > 0) {
                            errors.rejectValue("exchangeTime", "exchangeTime.error.message", "Không được sau ngày hiện tại");
                        }
                    }
                }

                // Validate ngày xét nghiệm nhiễm mới và tải lượng virus
                if (StringUtils.isNotEmpty(form.getEarlyHivDate())) {

                    checkDateFormat(form.getEarlyHivDate(), "earlyHivDate", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt);

                    Date earlyHivDate = sdfrmt.parse(form.getEarlyHivDate());
                    Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                    if (earlyHivDate.compareTo(new Date()) > 0 && errors.getFieldError("earlyHivDate") == null) {
                        errors.rejectValue("earlyHivDate", "earlyHivDate.error.message", "Ngày XN nhiễm mới HIV không được lớn hơn ngày hiện tại");
                    }

                    if (StringUtils.isNotEmpty(form.getConfirmTime()) && earlyHivDate.compareTo(confirmTime) < 0 && errors.getFieldError("earlyHivDate") == null) {
                        errors.rejectValue("earlyHivDate", "earlyHivDate.error.message", "Ngày XN nhiễm mới HIV không được nhỏ hơn ngày XN khẳng định");
                    }
                }

                // Validate ngày xét nghiệm nhiễm mới HIV, và ngày xét nghiệm tải lượng virus
                if (StringUtils.isNotEmpty(form.getVirusLoadDate())) {

                    checkDateFormat(form.getVirusLoadDate(), "virusLoadDate", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt);
                    Date virusLoadDate = sdfrmt.parse(form.getVirusLoadDate());
                    Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                    if (virusLoadDate.compareTo(new Date()) > 0 && errors.getFieldError("virusLoadDate") == null) {
                        errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng vi rút không được lớn hơn ngày hiện tại");
                    }

                    if (StringUtils.isNotEmpty(form.getConfirmTime()) && virusLoadDate.compareTo(confirmTime) < 0 && errors.getFieldError("virusLoadDate") == null) {
                        errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng vi rút không được nhỏ hơn ngày XN khẳng định");
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Validate for chuyển gửi điều trị
            if (StringUtils.isNotEmpty(form.getExchangeConsultTime())) {
                checkDateFormat(form.getExchangeConsultTime(), "exchangeConsultTime", "Định dạng ngày tư vấn không đúng dd/mm/yyyy", errors, sdfrmt);
//                validateNull(form.getPartnerProvideResult(), "partnerProvideResult", "Kết quả tư vấn không được để trống", errors);
                validateNull(form.getArvExchangeResult(), "arvExchangeResult", "Kết quả tư vấn chuyển gửi điều trị ARV không được để trống", errors);
            }

            if (StringUtils.isNotEmpty(form.getExchangeTime())) {
                checkDateFormat(form.getExchangeTime(), "exchangeTime", "Định dạng ngày chuyển gửi không đúng dd/mm/yyyy", errors, sdfrmt);
            }

            if (ACCEPT_EXCHANGE.equals(form.getArvExchangeResult()) && errors.getFieldError("exchangeTime") == null) {
                validateNull(form.getExchangeTime(), "exchangeTime", "Ngày chuyển gửi không được để trống", errors);
                checkDateFormat(form.getExchangeTime(), "exchangeTime", "Định dạng ngày chuyển gửi không đúng dd/mm/yyyy", errors, sdfrmt);
            }

            if (StringUtils.isNotEmpty(form.getExchangeTime()) && StringUtils.isNotEmpty(form.getResultsPatienTime())
                    && StringUtils.isNotEmpty(form.getArvExchangeResult())
                    && "1".equals(form.getArvExchangeResult())) {
                validateNull(form.getExchangeProvinceID(), "exchangeProvinceID", "Tỉnh thành không được để trống", errors);
                validateNull(form.getExchangeDistrictID(), "exchangeDistrictID", "Quận huyện không được để trống", errors);
                validateNull(form.getArrivalSiteID(), "arrivalSiteID", "Tên cơ sở không được để trống", errors);
                if ("-1".equals(form.getArrivalSiteID())) {
                    validateNull(form.getArrivalSite(), "arrivalSite", "Tên cơ sở không được để trống", errors);
                }
                if (errors.getFieldError("arrivalSite") == null) {
                    validateLength(form.getArrivalSite(), 255, "arrivalSite", "Tên cơ sở không được quá 255 ký tự", errors);
                }
            }

            if (StringUtils.isNotEmpty(form.getRegisterTherapyTime()) && StringUtils.isNotEmpty(form.getResultsPatienTime())
                    && StringUtils.isNotEmpty(form.getConfirmResultsID())
                    && "2".equals(form.getConfirmResultsID())) {
                checkDateFormat(form.getRegisterTherapyTime(), "registerTherapyTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
                validateNull(form.getTherapyRegistProvinceID(), "therapyRegistProvinceID", "Tỉnh thành không được để trống", errors);
                validateNull(form.getTherapyRegistDistrictID(), "therapyRegistDistrictID", "Quận huyện không được để trống", errors);
                validateNull(form.getRegisteredTherapySite(), "registeredTherapySite", "Tên cơ sở không được để trống", errors);
                validateNull(form.getTherapyNo(), "therapyNo", "Mã điều trị không được để trống", errors);
                if (errors.getFieldError("arrivalSite") == null) {
                    validateLength(form.getRegisteredTherapySite(), 255, "registeredTherapySite", "Tên cơ sở không được quá 255 ký tự", errors);
                }
                if (errors.getFieldError("therapyNo") == null) {
                    validateLength(form.getTherapyNo(), 255, "therapyNo", "Mã số điều trị không được quá 100 ký tự", errors);
                }
            }
        }

        // Ngày nhận kết quả B6. Ngày nhận kết quả
        try {
            if (errors.getFieldError("resultsTime") == null
                    && StringUtils.isNotEmpty(form.getResultsTime())) {
                Date today = new Date();
                Date resultsTime = sdfrmt.parse(form.getResultsTime());
                if (resultsTime.compareTo(today) > 0) {
                    errors.rejectValue("resultsTime", "resultsTime.error.message", "Không được sau ngày hiện tại");
                }
            }
        } catch (Exception e) {
        }

        // Validate ký tự đặc biệt C2. Mã XN khẳng định:
        if (StringUtils.isNotEmpty(form.getConfirmTestNo()) && !TextUtils.removeDiacritical(form.getConfirmTestNo().trim()).matches(RegexpEnum.CODE_CONFIRM) && errors.getFieldError("confirmTestNo") == null) {
            errors.rejectValue("confirmTestNo", "confirmTestNo.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }

        if (StringUtils.isNotEmpty(form.getStaffKC()) && !TextUtils.removeDiacritical(form.getStaffKC().trim()).matches(RegexpEnum.CODE_CONFIRM) && errors.getFieldError("staffKC") == null) {
            errors.rejectValue("staffKC", "staffKC.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }

        if (StringUtils.isNotEmpty(form.getTestNoFixSite()) && !TextUtils.removeDiacritical(form.getTestNoFixSite().trim()).matches(RegexpEnum.CODE_CONFIRM) && errors.getFieldError("testNoFixSite") == null) {
            errors.rejectValue("testNoFixSite", "testNoFixSite.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }

        // Validate patient phone number
        if (StringUtils.isNotEmpty(form.getPatientPhone())) {
            form.setPatientPhone(form.getPatientPhone().replaceAll("\\s", ""));
            if (!form.getPatientPhone().matches(PHONE_NUMBER)) {
                errors.rejectValue("patientPhone", "patientPhone.error.message", "Số điện thoại không hợp lệ");
            }
        }

//        DSNAnh update 11/09/2019
        // Validate cho dịch vụ tại dịch vụ cơ sở cố định
//        if (StringUtils.isNotEmpty(form.getServiceID()) && FIX_SERVICE_TYPE.equalsIgnoreCase(form.getServiceID())) {
//            validateNull(form.getFixedServiceID(), "fixedServiceID", "Dịch vụ cố định không được để trống", errors);
//        }
        if (StringUtils.isNotBlank(form.getStaffBeforeTestID()) && errors.getFieldError("staffBeforeTestID") == null) {
            validateLength(form.getStaffBeforeTestID(), 255, "staffBeforeTestID", "Tên tư vấn viên không được quá 255 ký tự", errors);
        }

        // Validate nhân viên sau khi xét nghiệm
        if (form.getID() == null && (StringUtils.isNotBlank(form.getResultsTime()) || StringUtils.isNotBlank(form.getResultsPatienTime())) && StringUtils.isBlank(form.getStaffAfterID()) && errors.getFieldError("staffAfterID") == null) {
            errors.rejectValue("staffAfterID", "staffAfterID.error.message", "Nhân viên không được để trống");
        }

        if (StringUtils.isNotBlank(form.getStaffAfterID()) && errors.getFieldError("staffAfterID") == null) {
            validateLength(form.getStaffAfterID(), 255, "staffAfterID", "Tên tư vấn viên không được quá 255 ký tự", errors);
        }

        // Validate câu trả lời đồng ý xét nghiệm khẳng định
        if (REACTIVE_TEST_RESULT.equals(form.getTestResultsID()) && StringUtils.isEmpty(form.getIsAgreeTest())) {
            errors.rejectValue("isAgreeTest", "isAgreeTest.error.message", "Câu trả lời không để được trống");
        }

        // Validate tồn tại cho nghề nghiệp
        if (StringUtils.isNotEmpty(form.getJobID()) && !parameterService.fieldParameterExists(form.getJobID(), ParameterEntity.JOB, false, "")) {
            errors.rejectValue("jobID", "jobID.error.message", "Công việc không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getModeOfTransmission()) && !parameterService.fieldParameterExists(form.getModeOfTransmission(), ParameterEntity.MODE_OF_TRANSMISSION, false, "")) {
            errors.rejectValue("modeOfTransmission", "modeOfTransmission.error.message", "Đường lây không tồn tại");
        }

        // Validate định dạng ngày
        try {

            // D1.1 > D1 (Ngày chuyển gửi sau ngày thực hiện tư vấn chuyển gửi điều trị ARV)
            if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && StringUtils.isNotEmpty(form.getExchangeTime())) {
                Date exchangeConsultTime = sdfrmt.parse(form.getExchangeConsultTime());
                Date exchangeTime = sdfrmt.parse(form.getExchangeTime());
                if (exchangeConsultTime.compareTo(exchangeTime) > 0) {
                    errors.rejectValue("exchangeTime", "exchangeTime.error.message", "Ngày chuyển gửi phải sau ngày tư vấn chuyển gửi điều trị ARV");
                }
            }

            //D3. Ngày đăng ký điều trị > D1.1. Ngày chuyển gửi:
            if (StringUtils.isNotEmpty(form.getRegisterTherapyTime()) && StringUtils.isNotEmpty(form.getExchangeTime())) {
                Date exchangeTime = sdfrmt.parse(form.getExchangeTime());
                Date registerTherapyTime = sdfrmt.parse(form.getRegisterTherapyTime());
                if (exchangeTime.compareTo(registerTherapyTime) > 0) {
                    errors.rejectValue("registerTherapyTime", "registerTherapyTime.error.message", "Ngày đăng ký điều trị phải sau ngày chuyển gửi");
                }
            }

            //Validate Ngày nhận kết quả Xn sàng lọc >= Ngày XN sàng lọc
            if (StringUtils.isNotEmpty(form.getResultsTime()) && StringUtils.isNotEmpty(form.getPreTestTime())) {
                Date resultDate = sdfrmt.parse(form.getResultsTime());
                Date preTest = sdfrmt.parse(form.getPreTestTime());

                if (resultDate.compareTo(preTest) < 0) {
                    errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận KQ phải lớn hơn ngày XN sàng lọc");
                }
            }

            // Validate ngày xn sàng lọc >= ngày tvxn
            if (StringUtils.isNotEmpty(form.getAdvisoryeTime()) && StringUtils.isNotEmpty(form.getPreTestTime())) {
                Date advisoryeTime = sdfrmt.parse(form.getAdvisoryeTime());
                Date preTest = sdfrmt.parse(form.getPreTestTime());

                if (advisoryeTime.compareTo(preTest) > 0) {
                    errors.rejectValue("preTestTime", "preTestTime.error.message", "Ngày XN sàng lọc không được trước ngày tư vấn XN");
                }
            }

            // Validate ngày tư vấn trước xét nghiệm không được là ngày tương lai
            if (StringUtils.isNotEmpty(form.getAdvisoryeTime())) {
                Date today = new Date();
                Date advisoryeDate = sdfrmt.parse(form.getAdvisoryeTime());
                if (advisoryeDate.compareTo(today) > 0) {
                    errors.rejectValue("advisoryeTime", "advisoryeTime.error.message", "Không được sau ngày hiện tại");
                }

            }

        } catch (ParseException ex) {
            Logger.getLogger(HtcVisitValidate.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Check null ngày nhận kết quả
//        if (NON_REACTIVE_TEST.equals(form.getTestResultsID()) && StringUtils.isEmpty(form.getResultsTime())) {
//            errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận kết quả không được để trống");
//        }
//        
//        if (UNKNOWN_PRE_TEST.equals(form.getTestResultsID()) && StringUtils.isEmpty(form.getResultsTime())) {
//            errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận kết quả không được để trống");
//        }
        if (StringUtils.isNotEmpty(form.getResultsTime())) {
            checkDateFormat(form.getResultsTime(), "resultsTime", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt);
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

}

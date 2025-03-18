package com.gms.entity.validate;

import com.gms.entity.form.HtcVisitForm;
import com.gms.service.HtcVisitService;
import com.gms.service.ParameterService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class HtcVisitTransferGsphValidate implements Validator {

    @Autowired
    private HtcVisitService htcVisitService;

    @Autowired
    private ParameterService parameterService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcVisitForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        final String REACTIVE_TEST_RESULT = "2";
        final String CONTINUE_TEST = "true";
        final String ACCEPT_EXCHANGE = "1";

        HtcVisitForm form = (HtcVisitForm) o;

        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        // Validate bắt buộc nếu kết quả xét nghiệm có phản ứng
        if (REACTIVE_TEST_RESULT.equals(form.getTestResultsID())) {

            // Validate địa chỉ cư trú hiện tại
            if (form.getIsDisplayCopy() != null && !form.getIsDisplayCopy()) {
                validateNull(form.getCurrentProvinceID(), "currentProvinceID", "Tỉnh thành không được để trống", errors);
                validateNull(form.getCurrentDistrictID(), "currentDistrictID", "Quận huyện không được để trống", errors);
                validateNull(form.getCurrentWardID(), "currentWardID", "Phường xã không được để trống", errors);
            }
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

        // Validate C6 C7
        try {
            Date c6 = null;
            Date c7 = null;

            // Validate C7. Ngày khách hàng nhận kết quả khẳng định <= D1.Ngày tư vấn chuyển gửi điều trị ARV yess
            if (StringUtils.isEmpty(form.getExchangeConsultTime())
                    && (StringUtils.isNotEmpty(form.getPartnerProvideResult()) || StringUtils.isNotEmpty(form.getArvExchangeResult()))
                    && errors.getFieldError("exchangeConsultTime") == null) {
                errors.rejectValue("exchangeConsultTime", "exchangeConsultTime.error.message", "Ngày tư vấn chuyển gửi điều trị ARV không được để trống");
            }

            // Validate ràng buộc ngày C6 <= C7 yess
            if (StringUtils.isNotEmpty(form.getResultsPatienTime())) {
                checkDateFormat(form.getResultsPatienTime(), "resultsPatienTime", "Định dạng ngày khách hàng nhận kết quả xét nghiệm khẳng định không đúng dd/mm/yyyy", errors, sdfrmt);

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

            // Ngày khách hàng nhận kết quả KĐ yess
            if (errors.getFieldError("resultsPatienTime") == null) {
                if (StringUtils.isNotEmpty(form.getResultsPatienTime())) {
                    Date today = new Date();
                    Date resultsPatienTime = sdfrmt.parse(form.getResultsPatienTime());
                    if (resultsPatienTime.compareTo(today) > 0) {
                        errors.rejectValue("resultsPatienTime", "resultsPatienTime.error.message", "Không được sau ngày hiện tại");
                    }
                }
            }

            // Ngày tư vấn chuyển gửi điều trị ARV yess
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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Validate for chuyển gửi điều trị yess
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

        // Validate ký tự đặc biệt C2. Mã XN khẳng định:
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

            // Validate ngày xn sàng lọc >= ngày tvxn
            if (StringUtils.isNotEmpty(form.getAdvisoryeTime()) && StringUtils.isNotEmpty(form.getPreTestTime())) {
                Date advisoryeTime = sdfrmt.parse(form.getAdvisoryeTime());
                Date preTest = sdfrmt.parse(form.getPreTestTime());

                if (advisoryeTime.compareTo(preTest) > 0) {
                    errors.rejectValue("preTestTime", "preTestTime.error.message", "Ngày XN sàng lọc không được trước ngày tư vấn XN");
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(HtcVisitTransferGsphValidate.class.getName()).log(Level.SEVERE, null, ex);
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

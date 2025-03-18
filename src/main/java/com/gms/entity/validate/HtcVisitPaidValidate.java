package com.gms.entity.validate;

import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.HtcVisitForm;
import com.gms.service.ParameterService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class HtcVisitPaidValidate implements Validator {

    @Autowired
    private ParameterService parameterService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcVisitForm.class);
    }

    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-]+[^-]$)";

    @Override
    public void validate(Object o, Errors errors) {

        final String ACCEPT_EXCHANGE = "1";

        HtcVisitForm form = (HtcVisitForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        // Validate C5 C6 C7
        try {
            // Validate ngày xét nghiệm khẳng định
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("resultsSiteTime") == null) {
                validateNull(form.getResultsSiteTime(), "resultsSiteTime", "Ngày cơ sở nhận kết quả không được trống", errors);
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

            // Validate C7. Ngày khách hàng nhận kết quả khẳng định <= D1.Ngày tư vấn chuyển gửi điều trị ARV
//            if (StringUtils.isNotEmpty(form.getResultsPatienTime()) && StringUtils.isNotEmpty(form.getExchangeConsultTime())) {
//                c7 = sdfrmt.parse(form.getResultsPatienTime());
//                Date exchangeDate = sdfrmt.parse(form.getExchangeConsultTime());
//                if (exchangeDate.compareTo(c7) < 0) {
//                    errors.rejectValue("exchangeConsultTime", "exchangeConsultTime.error.message", "Ngày tư vấn chuyển gửi điều trị ARV không được trước ngày khách hàng nhận kết quả khẳng định");
//                }
//            }

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
            errors.rejectValue("resultsPatienTime", "resultsPatienTime.error.message", "Ngày khách hàng nhận kết quả khẳng định không được nhỏ hơn ngày cơ sở nhận kết quả khẳng định");
        }
        
        try {
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
        } catch (Exception e) {
        }
        
        try {
            // Validate ngày xét nghiệm nhiễm mới HIV, và ngày xét nghiệm tải lượng virus
            if (StringUtils.isNotEmpty(form.getVirusLoadDate())) {

                checkDateFormat(form.getVirusLoadDate(), "virusLoadDate", "Ngày phải đúng định dạng ngày/tháng/năm", errors, sdfrmt);
                Date virusLoadDate = sdfrmt.parse(form.getVirusLoadDate());
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (virusLoadDate.compareTo(new Date()) > 0 && errors.getFieldError("virusLoadDate") == null) {
                    errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng virus không được lớn hơn ngày hiện tại");
                }

                if (StringUtils.isNotEmpty(form.getConfirmTime()) && virusLoadDate.compareTo(confirmTime) < 0 && errors.getFieldError("virusLoadDate") == null) {
                    errors.rejectValue("virusLoadDate", "virusLoadDate.error.message", "Ngày XN tải lượng virus không được nhỏ hơn ngày XN khẳng định");
                }
            }
        } catch (Exception e) {
        }

        // Validate ngày không đươc lớn hơn ngày hiện tại
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

        } catch (Exception e) {
        }

        // Validate for chuyển gửi điều trị
        if (StringUtils.isNotEmpty(form.getExchangeConsultTime())) {
            checkDateFormat(form.getExchangeConsultTime(), "exchangeConsultTime", "Định dạng ngày tư vấn không đúng dd/mm/yyyy", errors, sdfrmt);
//            validateNull(form.getPartnerProvideResult(), "partnerProvideResult", "Kết quả tư vấn không được để trống", errors);
            validateNull(form.getArvExchangeResult(), "arvExchangeResult", "Kết quả tư vấn chuyển gửi điều trị ARV không được để trống", errors);
        }

        if (StringUtils.isNotEmpty(form.getExchangeTime())) {
            checkDateFormat(form.getExchangeTime(), "exchangeTime", "Định dạng ngày chuyển gửi không đúng dd/mm/yyyy", errors, sdfrmt);
        }

        if (ACCEPT_EXCHANGE.equals(form.getArvExchangeResult())) {
            validateNull(form.getExchangeTime(), "exchangeTime", "Ngày chuyển gửi không được để trống", errors);
            checkDateFormat(form.getExchangeTime(), "exchangeTime", "Định dạng ngày không đúng dd/mm/yyyy", errors, sdfrmt);
        }

        if (StringUtils.isNotEmpty(form.getExchangeTime()) && StringUtils.isNotEmpty(form.getResultsPatienTime())
                && StringUtils.isNotEmpty(form.getConfirmResultsID())) {
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
                && StringUtils.isNotEmpty(form.getConfirmResultsID())) {
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
        if (StringUtils.isNotEmpty(form.getConfirmTestNo()) &&  !form.getConfirmTestNo().matches(RegexpEnum.CODE_CONFIRM) 
                && errors.getFieldError("confirmTestNo") == null) {
            errors.rejectValue("confirmTestNo", "confirmTestNo.error.message", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }

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

            //Validate Ngày nhận kết quả Xn sàng lọc >= Ngày XN sàng lọc
            if (StringUtils.isNotEmpty(form.getResultsTime()) && StringUtils.isNotEmpty(form.getPreTestTime())) {
                Date resultDate = sdfrmt.parse(form.getResultsTime());
                Date preTest = sdfrmt.parse(form.getPreTestTime());

                if (resultDate.compareTo(preTest) < 0) {
                    errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận KQ phải lớn hơn ngày XN sàng lọc");
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
            Logger.getLogger(HtcVisitPaidValidate.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Check null ngày nhận kết quả
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

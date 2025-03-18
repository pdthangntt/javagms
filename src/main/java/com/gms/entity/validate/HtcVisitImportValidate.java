package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.form.HtcVisitImportForm;
import com.gms.service.HtcVisitService;
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
 * @author TrangBN
 */
@Component
public class HtcVisitImportValidate implements Validator {

    @Autowired
    private HtcVisitService htcVisitService;
    
    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcVisitImportForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        
        final String REACTIVE_TEST_RESULT = "2";
        
        HtcVisitImportForm form = (HtcVisitImportForm) o;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        
        if (REACTIVE_TEST_RESULT.equals(form.getTestResultsID())) {
            
            // Validate Họ tên
            validateNull(form.getPatientName(), "patientName", "Họ tên không được để trống", errors);
            // Validate ký tự đặc biệt cho họ tên
            if (StringUtils.isNotBlank(form.getPatientName()) && errors.getFieldError("patientName") == null && 
                    !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)) {
                errors.rejectValue("patientName", "patientName.error.message", "Họ và tên chỉ chứa các kí tự chữ cái");
            }
            // Validate gender
            validateNull(form.getGenderID(), "genderID", "Giới tính không được trống", errors);
            // Validate year of birth
            validateNull(form.getYearOfBirth(), "yearOfBirth", "Năm sinh không được để trống", errors);
            // Validate địa chỉ thường trú
            validateNull(form.getPermanentAddress(), "permanentAddress", "Địa chỉ thường trú không được để trống", errors);
            // Validate tỉnh thành thường trú
            validateNull(form.getProvince(), "province", "Tỉnh thành thường trú không được để trống", errors);
            // Validate quận huyện
            validateNull(form.getDistrict(), "district", "Quận huyện thường trú không được để trống", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getPatientName()) && errors.getFieldError("patientName") == null) {
            validateLength(form.getPatientName(), 100, "patientName","Họ và tên khách hàng không được quá 100 ký tự", errors);
        }
        
        validateNull(form.getAdvisoryeTime(), "advisoryeTime", "Ngày tư vấn không được để trống", errors);
        if (StringUtils.isNotBlank(form.getAdvisoryeTime()) && errors.getFieldError("advisoryeTime") == null) {
            if(TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getAdvisoryeTime()).charAt(0) == '0'){
                errors.rejectValue("advisoryeTime", "advisoryeTime.error.message", "Ngày tư vấn trước XN không đúng định dạng");
            }
        }
        if (StringUtils.isNotBlank(form.getResultsTime()) && StringUtils.isBlank(form.getStaffAfterID()) && errors.getFieldError("staffAfterID") == null) {
            errors.rejectValue("staffAfterID", "staffAfterID.error.message", "Chưa nhập tên tư vấn viên sau XN");
        }
        // Validate objectGroupID 
        if(StringUtils.isEmpty(form.getObjectGroupID_1()) 
                && StringUtils.isEmpty(form.getObjectGroupID_2()) 
                && StringUtils.isEmpty(form.getObjectGroupID_3()) 
                && StringUtils.isEmpty(form.getObjectGroupID_4()) 
                && StringUtils.isEmpty(form.getObjectGroupID_5()) 
                && StringUtils.isEmpty(form.getObjectGroupID_6()) 
                && StringUtils.isEmpty(form.getObjectGroupID_7()) 
                && StringUtils.isEmpty(form.getObjectGroupID_8()) 
                ) {
            errors.rejectValue("objectGroupID_1", "objectGroupID_1.error.message", "Chọn một nhóm đối tượng");
        }
        
        // Validate existing code
        if (StringUtils.isNotBlank(form.getCode()) && htcVisitService.existCodeAndSiteID(form.getCode(), form.getSiteID())) {
                errors.rejectValue("code", "code.error.message", "Mã khách hàng đã tồn tại");
        }
        validateNull(form.getCode(), "code", "Mã khách hàng không được để trống", errors);
        
        if (errors.getFieldError("code") == null) {
            validateLength(form.getCode(), 50, "code", "Mã khách hàng không được quá 50 ký tự", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getCode()) && !TextUtils.removeDiacritical(form.getCode().trim()).matches(RegexpEnum.CODE)
                && errors.getFieldError("code") == null) {
            errors.rejectValue("code", "code.error.message", "Mã không đúng định dạng (Không chứa tự đặc biệt, chỉ chứa số, chữ và dấu -");
        }
        
        if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && !form.getYearOfBirth().matches("[0-9]{0,4}")) {
            errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh gồm bốn chữ số vd: 1994");
        } else if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && (Integer.parseInt(form.getYearOfBirth()) < 1900
                || Integer.parseInt(form.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR))) {
            errors.rejectValue("yearOfBirth", "yearOfBirth.error.message", "Năm sinh hợp lệ từ 1900 - năm hiện tại");
        }
        
        validateLength(form.getPermanentAddress(), 500, "permanentAddress", "Địa chỉ thường trú không được quá 500 ký tự", errors);
        
        // Validate ngày tư vấn trước xét nghiệm không được là ngày tương lai
        try {
            Date today = new Date();
            
            if (StringUtils.isNotEmpty(form.getAdvisoryeTime())) {
                Date advisoryeDate = sdfrmt.parse(form.getAdvisoryeTime());
                if (advisoryeDate.compareTo(today) > 0) {
                    errors.rejectValue("advisoryeTime", "advisoryeTime.error.message", "Ngày tư vấn trước XN không được sau ngày hiện tại");
                }

            }
            
            if (StringUtils.isNotEmpty(form.getPreTestTime()) && errors.getFieldError("preTestTime") == null) {
                Date preTestTime = sdfrmt.parse(form.getPreTestTime());
                if (preTestTime.compareTo(today) > 0) {
                    errors.rejectValue("preTestTime", "preTestTime.error.message", "Ngày xét nghiệm sàng lọc không được sau ngày hiện tại");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (confirmTime.compareTo(today) > 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày xét nghiệm khẳng định không được sau ngày hiện tại");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getSampleSentDate()) && errors.getFieldError("sampleSentDate") == null) {
                Date confirmTime = sdfrmt.parse(form.getSampleSentDate());
                if (confirmTime.compareTo(today) > 0) {
                    errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu xét nghiệm không được sau ngày hiện tại");
                }
            }
        } catch (Exception e) {
        }
        
        // Validate giá trị liên quan trường đồng ý xét nghiệm sàng lọc
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không"))) && StringUtils.isNotEmpty(form.getPreTestTime()) && errors.getFieldError("preTestTime") == null) {
            errors.rejectValue("preTestTime", "preTestTime.error.message", "Ngày XN sàng lọc chỉ có giá trị khi đồng ý xét nghiệm sàng lọc");
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không"))) && StringUtils.isNotEmpty(form.getTestResultsID()) && errors.getFieldError("testResultsID") == null) {
            errors.rejectValue("testResultsID", "testResultsID.error.message", "Kết quả XN sàng lọc chỉ có giá trị khi đồng ý xét nghiệm sàng lọc");
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không")))  && StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
            errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày XN khẳng định chỉ có giá trị khi đồng ý xét nghiệm sàng lọc");
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không")))  && StringUtils.isNotEmpty(form.getConfirmResultsID()) && errors.getFieldError("confirmResultsID") == null) {
            errors.rejectValue("confirmResultsID", "confirmResultsID.error.message", "Kết quả XN khẳng định chỉ có giá trị khi đồng ý xét nghiệm sàng lọc");
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không"))) && StringUtils.isNotEmpty(form.getResultsTime()) && errors.getFieldError("resultsTime") == null) {
            errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày trả kết quả chỉ có giá trị khi đồng ý xét nghiệm sàng lọc");
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không"))) && StringUtils.isNotEmpty(form.getSampleSentDate()) && errors.getFieldError("sampleSentDate") == null) {
            errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu chỉ có giá trị khi đồng ý xét nghiệm sàng lọc");
        }
        
        try {
            if (StringUtils.isNotEmpty(form.getResultsTime()) && StringUtils.isNotEmpty(form.getAdvisoryeTime())) {
                Date today = new Date();
                Date resultsTime = sdfrmt.parse(form.getResultsTime());
                Date advisoryeTime = sdfrmt.parse(form.getAdvisoryeTime());
                if (resultsTime.compareTo(today) > 0) {
                    errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận kết quả và tư vấn sau XN không được sau ngày hiện tại");
                }
                if (resultsTime.compareTo(advisoryeTime) < 0) {
                    errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận kết quả và tư vấn sau XN phải sau ngày tư vấn trước XN");
                }
            }
        } catch (Exception e) {
        }
        if (StringUtils.isNotBlank(form.getResultsTime()) && errors.getFieldError("resultsTime") == null) {
            if(TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getResultsTime()).charAt(0) == '0'){
                errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận kết quả và tư vấn sau XN không đúng định dạng");
            }
        }
        if (StringUtils.isNotBlank(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
            if(TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getConfirmTime()).charAt(0) == '0'){
                errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày xét nghiệm khẳng định không đúng định dạng");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getSampleSentDate()) && StringUtils.isEmpty(form.getResultsTime()) && errors.getFieldError("sampleSentDate") == null ) {
            errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu XN chỉ nhập khi có ngày nhận kết quả xét nghiệm");
        }
        if (StringUtils.isNotEmpty(form.getSampleSentDate()) && StringUtils.isNotEmpty(form.getConfirmResultsID())
                && ConfirmTestResultEnum.AM_TINH.equals(form.getConfirmResultsID()) && errors.getFieldError("sampleSentDate") == null ) {
            errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu XN chỉ nhập khi kết quả XNKĐ không âm tính và đồng ý xét nghiệm khẳng định");
        }
        if (StringUtils.isEmpty(form.getSampleSentDate()) && StringUtils.isNotEmpty(form.getConfirmResultsID())
                && ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID()) && errors.getFieldError("sampleSentDate") == null ) {
            errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu XN không được để trống");
        }
        
        if (StringUtils.isNotBlank(form.getSampleSentDate()) && errors.getFieldError("sampleSentDate") == null) {
            if(TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getResultsTime()).charAt(0) == '0'){
                errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu XN không đúng định dạng");
            }
        }
        
        if(StringUtils.isNotEmpty(form.getConfirmResultsID()) && ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID()) &&
               !TestResultEnum.CO_PHAN_UNG.getKey().equals(form.getTestResultsID()) && !TestResultEnum.KHONG_RO.getKey().equals(form.getTestResultsID()) && errors.getFieldError("confirmResultsID") == null){
            errors.rejectValue("confirmResultsID", "confirmResultsID.error.message", "Kết quả XNKĐ chỉ có khi kết quả XNSL là có phản ứng");
        }
        
        if (StringUtils.isNotEmpty(form.getTestResultsID()) && errors.getFieldError("preTestTime") == null) {
            validateNull(form.getPreTestTime(), "preTestTime", "Ngày xét nghiệm sàng lọc không được để trống", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && errors.getFieldError("confirmTime") == null) {
            validateNull(form.getConfirmTime(), "confirmTime", "Ngày xét nghiệm khẳng định không được để trống", errors);
        }
        
        if (StringUtils.isNotEmpty(form.getPreTestTime()) && errors.getFieldError("preTestTime") == null) {
            if(TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getPreTestTime()) == null || TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getPreTestTime()).charAt(0) == '0'){
                errors.rejectValue("preTestTime", "preTestTime.error.message", "Ngày xét nghiệm sàng lọc không đúng định dạng");
            }
        }
        
        if (StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("confirmTime") == null) {
            if(TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getConfirmTime()) == null || TextUtils.formatDate("dd/MM/yyyy", "yyyy", form.getConfirmTime()).charAt(0) == '0'){
                errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày xét nghiệm khẳng định không đúng định dạng");
            }
        }
        
        try {
            
            Date c5 = new Date();
            Date c6 = new Date();
            
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getPreTestTime()) && errors.getFieldError("confirmTime") == null) {
                c5 = sdfrmt.parse(form.getAdvisoryeTime());
                c6 = sdfrmt.parse(form.getPreTestTime());
                if (c5.compareTo(c6) > 0) {
                    errors.rejectValue("preTestTime", "preTestTime.error.message", "Ngày xét nghiệm sàng lọc không được nhỏ hơn ngày tư vấn trước xét nghiệm");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getPreTestTime()) && errors.getFieldError("confirmTime") == null) {
                c5 = sdfrmt.parse(form.getPreTestTime());
                c6 = sdfrmt.parse(form.getConfirmTime());
                if (c5.compareTo(c6) > 0) {
                    errors.rejectValue("confirmTime", "confirmTime.error.message", "Ngày xét nghiệm khẳng định không được nhỏ hơn ngày xét nghiệm sàng lọc");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getSampleSentDate()) && errors.getFieldError("sampleSentDate") == null) {
                c5 = sdfrmt.parse(form.getConfirmTime());
                c6 = sdfrmt.parse(form.getSampleSentDate());
                
                if (c5.compareTo(c6) > 0) {
                    errors.rejectValue("sampleSentDate", "sampleSentDate.error.message", "Ngày gửi mẫu không được nhỏ hơn ngày xét nghiệm khẳng định");
                }
            }
            
            if (StringUtils.isNotEmpty(form.getPreTestTime()) && StringUtils.isNotEmpty(form.getResultsTime()) && errors.getFieldError("resultsTime") == null) {
                c5 = sdfrmt.parse(form.getPreTestTime());
                c6 = sdfrmt.parse(form.getResultsTime());
                
                if (c5.compareTo(c6) > 0) {
                    errors.rejectValue("resultsTime", "resultsTime.error.message", "Ngày nhận kết quả không được nhỏ hơn ngày xét nghiệm sàng lọc");
                }
            }
        } catch (ParseException e) {
        }
        
        // Validate kết quả xét nghiệm khẳng định theo ngày
        if (StringUtils.isNotEmpty(form.getConfirmTime()) && errors.getFieldError("confirmResultsID") == null) {
            validateNull(form.getConfirmResultsID(), "confirmResultsID", "Kết quả xét nghiệm khẳng định không được để trống", errors);
        }
        
        // Validate CMND
        if (StringUtils.isNotEmpty(form.getPatientID()) && !TextUtils.removeDiacritical(form.getPatientID().trim()).matches(RegexpEnum.CMND)
                && errors.getFieldError("patientID") == null) {
            errors.rejectValue("patientID", "patientID.error.message", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt/chữ tiếng việt, số âm, số thập phân");
        }
        
    }
    
    // Check null or empty 
    private void validateNull(String inputValue, String id, String message, Errors errors) {
        if (StringUtils.isEmpty(inputValue)) {
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

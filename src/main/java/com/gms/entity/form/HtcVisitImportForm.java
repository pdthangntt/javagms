package com.gms.entity.form;

import com.gms.components.TextUtils;
import com.gms.components.annotation.ParameterFK;
import com.gms.entity.constant.CdServiceEnum;
import com.gms.entity.constant.HasHealthInsuranceEnum;
import com.gms.entity.constant.TestMethodEnum;
import com.gms.entity.db.*;
import com.gms.service.ParameterService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.validation.constraints.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author TrangBN
 */
public class HtcVisitImportForm implements Serializable {

    private Long ID;
    protected static HashMap<String, String> attributeLabels;

    private final String REGEX_VALIDATE_DATE = "^(?=\\d{2}([-.,\\/])\\d{2}\\1\\d{4}$)(?:0[1-9]|1\\d|[2][0-8]|29(?!.02.(?!(?!(?:[02468][1-35-79]|[13579][0-13-57-9])00)\\d{2}(?:[02468][048]|[13579][26])))|30(?!.02)|31(?=.(?:0[13578]|10|12))).(?:0[1-9]|1[012]).\\d{4}$";

    //----- A. Thông tin khách hàng
    @Pattern(regexp = REGEX_VALIDATE_DATE, message = "Định dạng ngày không đúng dd/mm/yyyy")
    private String advisoryeTime; //Ngày tư vấn xét nghiệm

    private Long siteID;
    private String code;
    private String patientName;

    @NotBlank(message = "Câu trả lời không được để trống")
    private String isAgreePreTest;

    @Size(min = 0, max = 50, message = "CMND/Thẻ căn cước khách hàng có độ dài tối đa 50 kí tự.")
    private String patientID; // CMND
    private String genderID;
    private String yearOfBirth;
    private String permanentAddress; //Địa chỉ thường trú
    private String province;
    private String district;
    private String ward;
    private String hasHealthInsurance;
    private boolean isRemove;

    //----------B. Xét nghiệm sàng lọc
    @ParameterFK(fieldName = "testResultsID", type = ParameterEntity.TEST_RESULTS, mutiple = false, service = ParameterService.class)
    private String testResultsID; //Kết quả xét nghiệm sàng lọc
    @Pattern(regexp = REGEX_VALIDATE_DATE, message = "Định dạng ngày không đúng dd/mm/yyyy")
    private String resultsTime; //Ngày trả kết quả xét nghiệm sàng lọc

    private String isAgreeTest; // Đồng ý tiếp tục xét nghiệm hay không

    @ParameterFK(fieldName = "confirmResultsID", type = ParameterEntity.TEST_RESULTS, mutiple = false,
            service = ParameterService.class, message = "Kết quả xét nghiệm khẳng định không hợp lệ")
    private String confirmResultsID; //Kết quả xét nghiệm khẳng định

    //-----------E. Tư vấn viên
    @NotBlank(message = "Chưa nhập tên nhân viên trước XN")
    private String staffBeforeTestID; //Nhân viên tư vấn trước xét nghiệm

    private String staffAfterID; // Nhân viên tư vấn sau xét nghiệm

    // Các nhóm đối tượng nguy cơ
    private String objectGroupID_1; // Nghiện chích ma túy
    private String objectGroupID_2; // Người hành nghề mại dâm 
    private String objectGroupID_3; // MSM ( nam quan hệ tình dục với nam)
    private String objectGroupID_4; // Vợ/chồng/bạn tình của người nhiễm HIV
    private String objectGroupID_5; // 5.1. Phụ nữ mang thai (Thời kỳ mang thai
    private String objectGroupID_6; // Nhóm nguy cơ: 5.2. Phụ nữ mang thai (Giai đoạn chuyển dạ, đẻ)
    private String objectGroupID_7; // Nhóm nguy cơ: BN Lao
    private String objectGroupID_8; // Nhóm nguy cơ: Các đối tượng khác
    private String objectGroupID_9; // Nhóm nguy cơ: Phạm nhân
    private String sampleSentDate; // Ngày gửi mẫu
    private String preTestTime; // Ngày xét nghiệm sàng lọc
    private String confirmTime; // Ngày xét nghiệm khẳng định
    private String phone; // Số điện thoại
    private String healthInsuranceNo;

    public String getHealthInsuranceNo() {
        return healthInsuranceNo;
    }

    public void setHealthInsuranceNo(String healthInsuranceNo) {
        this.healthInsuranceNo = healthInsuranceNo;
    }

    public String getObjectGroupID_9() {
        return objectGroupID_9;
    }

    public void setObjectGroupID_9(String objectGroupID_9) {
        this.objectGroupID_9 = objectGroupID_9;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public void setHasHealthInsurance(String hasHealthInsurance) {
        this.hasHealthInsurance = hasHealthInsurance;
    }

    public String getObjectGroupID_8() {
        return objectGroupID_8;
    }

    public void setObjectGroupID_8(String objectGroupID_8) {
        this.objectGroupID_8 = objectGroupID_8;
    }

    public String getPreTestTime() {
        return preTestTime;
    }

    public void setPreTestTime(String preTestTime) {
        this.preTestTime = preTestTime;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getSampleSentDate() {
        return sampleSentDate;
    }

    public void setSampleSentDate(String sampleSentDate) {
        this.sampleSentDate = sampleSentDate;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public static HashMap<String, String> getAttributeLabels() {
        return attributeLabels;
    }

    public static void setAttributeLabels(HashMap<String, String> attributeLabels) {
        HtcVisitImportForm.attributeLabels = attributeLabels;
    }

    public String getObjectGroupID_1() {
        return StringUtils.isNotEmpty(objectGroupID_1) ? objectGroupID_1.toLowerCase().trim() : objectGroupID_1;
    }

    public void setObjectGroupID_1(String objectGroupID_1) {
        this.objectGroupID_1 = objectGroupID_1;
    }

    public String getObjectGroupID_2() {
        return StringUtils.isNotEmpty(objectGroupID_2) ? objectGroupID_2.toLowerCase().trim() : objectGroupID_2;
    }

    public void setObjectGroupID_2(String objectGroupID_2) {
        this.objectGroupID_2 = objectGroupID_2;
    }

    public String getObjectGroupID_3() {
        return StringUtils.isNotEmpty(objectGroupID_3) ? objectGroupID_3.toLowerCase().trim() : objectGroupID_3;
    }

    public void setObjectGroupID_3(String objectGroupID_3) {
        this.objectGroupID_3 = objectGroupID_3;
    }

    public String getObjectGroupID_4() {
        return StringUtils.isNotEmpty(objectGroupID_4) ? objectGroupID_4.toLowerCase().trim() : objectGroupID_4;
    }

    public void setObjectGroupID_4(String objectGroupID_4) {
        this.objectGroupID_4 = objectGroupID_4;
    }

    public String getObjectGroupID_5() {
        return StringUtils.isNotEmpty(objectGroupID_5) ? objectGroupID_5.toLowerCase().trim() : objectGroupID_5;
    }

    public void setObjectGroupID_5(String objectGroupID_5) {
        this.objectGroupID_5 = objectGroupID_5;
    }

    public String getObjectGroupID_6() {
        return StringUtils.isNotEmpty(objectGroupID_6) ? objectGroupID_6.toLowerCase().trim() : objectGroupID_6;
    }

    public void setObjectGroupID_6(String objectGroupID_6) {
        this.objectGroupID_6 = objectGroupID_6;
    }

    public String getObjectGroupID_7() {
        return StringUtils.isNotEmpty(objectGroupID_7) ? objectGroupID_7.toLowerCase().trim() : objectGroupID_7;
    }

    public void setObjectGroupID_7(String objectGroupID_7) {
        this.objectGroupID_7 = objectGroupID_7;
    }

    public String getConfirmResultsID() {
        return StringUtils.isNotEmpty(confirmResultsID) ? confirmResultsID.toLowerCase() : confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getCode() {
        return StringUtils.isNotEmpty(code) ? code.trim() : StringUtils.EMPTY;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPatientName() {
        return StringUtils.isNotEmpty(patientName) ? patientName.trim() : StringUtils.EMPTY;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientID() {
        return StringUtils.isNotEmpty(patientID) ? patientID.trim() : StringUtils.EMPTY;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

//    @NotBlank(message = "Giới tính không được trống")
    public String getGenderID() {
        return StringUtils.isNotEmpty(genderID) ? genderID.trim().toLowerCase() : StringUtils.EMPTY;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYearOfBirth() {
        return StringUtils.isNotEmpty(yearOfBirth) ? yearOfBirth.trim() : StringUtils.EMPTY;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPermanentAddress() {
        return StringUtils.isNotEmpty(permanentAddress) ? permanentAddress.trim() : StringUtils.EMPTY;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getTestResultsID() {
        return StringUtils.isNotEmpty(testResultsID) ? testResultsID.trim().toLowerCase() : StringUtils.EMPTY;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(String advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }

    public String getStaffBeforeTestID() {
        return staffBeforeTestID;
    }

    public void setStaffBeforeTestID(String staffBeforeTestID) {
        this.staffBeforeTestID = staffBeforeTestID;
    }

    public String getStaffAfterID() {
        return staffAfterID;
    }

    public void setStaffAfterID(String staffAfterID) {
        this.staffAfterID = staffAfterID;
    }

    public String getResultsTime() {
        return StringUtils.isNotEmpty(resultsTime) ? resultsTime.trim() : StringUtils.EMPTY;
    }

    public void setResultsTime(String resultsTime) {
        this.resultsTime = resultsTime;
    }

    public String getIsAgreePreTest() {
        return StringUtils.isNotEmpty(isAgreePreTest) ? isAgreePreTest.toLowerCase().trim() : isAgreePreTest;
    }

    public void setIsAgreePreTest(String isAgreePreTest) {
        this.isAgreePreTest = isAgreePreTest;
    }

    public String getIsAgreeTest() {
        return StringUtils.isNotEmpty(isAgreeTest) ? isAgreeTest.toLowerCase() : isAgreeTest;
    }

    public void setIsAgreeTest(String isAgreeTest) {
        this.isAgreeTest = isAgreeTest;
    }

    public boolean isIsRemove() {
        return isRemove;
    }

    public void setIsRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }

    /**
     * Set attributes cho form
     */
    public void setAttributeLabels() {
        attributeLabels.put("order", "TT");
        attributeLabels.put("advisoryeTime", "Ngày tư vấn trước XN");
        attributeLabels.put("code", "Mã khách hàng");
        attributeLabels.put("patientName", "Họ tên khách hàng");
        attributeLabels.put("patientID", "CMND (Đối với các KH Dương tính)");
        attributeLabels.put("genderID", "Giới tính");
        attributeLabels.put("yearOfBirth", "Năm sinh");
        attributeLabels.put("permanentAddress", "Địa chỉ");
        attributeLabels.put("isAgreePreTest", "Xét nghiệm");
        attributeLabels.put("objectGroupID_1", "Nhóm nguy cơ: Nghiện chích ma túy (NCMT)");
        attributeLabels.put("objectGroupID_2", "Nhóm nguy cơ: Phụ nữ bán dâm (PNBD)");
        attributeLabels.put("objectGroupID_3", "Nhóm nguy cơ: Nam quan hệ tình dục với nam (MSM)");
        attributeLabels.put("objectGroupID_4", "Nhóm nguy cơ: Vợ/chồng /bạn tình của người nhiễm HIV");
        attributeLabels.put("objectGroupID_5", "Nhóm nguy cơ: Phụ nữ mang thai");
        attributeLabels.put("objectGroupID_6", "Nhóm nguy cơ: BN Lao");
        attributeLabels.put("objectGroupID_7", "Nhóm nguy cơ: Các đối tượng khác");
        attributeLabels.put("testResultsID", "Kết quả xét nghiệm sàng lọc");
        attributeLabels.put("confirmResultsID", "Kết quả xét nghiệm khẳng định");
        attributeLabels.put("resultsTime", "Ngày nhận kết quả và tư vấn sau XN");
        attributeLabels.put("staffBeforeTestID", "Tên tư vấn viên trước XN");
        attributeLabels.put("staffAfterID", "Tên tư vấn viên sau XN");
        attributeLabels.put("healthInsuranceNo", "Mã số thẻ BHYT");
    }

    /**
     * Convert to HtcVisitEntity
     *
     * @param siteConfirmTest
     * @return
     */
    public HtcVisitEntity convertToEntity(String siteConfirmTest) {

        HtcVisitEntity htcVisitEntity = new HtcVisitEntity();
        htcVisitEntity.setServiceID("CD");  // Mặc định chọn dịch vụ cố định
        htcVisitEntity.setCdService(CdServiceEnum.SANG_LOC.getKey()); // Dịch vụ tại cơ sở cố định
        htcVisitEntity.setTestMethod(TestMethodEnum.KHANG_THE.getKey()); // Mặc định phương pháp xét nghiệm kháng thể
        htcVisitEntity.setFixedServiceID("1"); // Mặc định nhận dịch vụ tư vấn xét nghiệm	
        htcVisitEntity.setCode(getCode());
        htcVisitEntity.setPatientName(getPatientName());
        htcVisitEntity.setPatientIDAuthen("2");  // Mặc định không có khả năng xác minh thông tin
        htcVisitEntity.setPermanentAddress(getPermanentAddress());
        htcVisitEntity.setCurrentAddress(getPermanentAddress());
        htcVisitEntity.setGenderID(getGenderID());
        htcVisitEntity.setSiteID(getSiteID());
        htcVisitEntity.setPatientID(getPatientID());
        htcVisitEntity.setIsAgreePreTest(StringUtils.isEmpty(getIsAgreePreTest()) ? null : "có".equals(getIsAgreePreTest()) ? "1" : "0");

        if (StringUtils.isNotBlank(getObjectGroupID_1()) && "có".equals(getObjectGroupID_1().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("1");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_2()) && "có".equals(getObjectGroupID_2().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("2");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_3()) && "có".equals(getObjectGroupID_3().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("3");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_4()) && "có".equals(getObjectGroupID_4().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("4");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_5()) && "có".equals(getObjectGroupID_5().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("5.1");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_6()) && "có".equals(getObjectGroupID_6().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("5.2");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_7()) && "có".equals(getObjectGroupID_7().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("6");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_8()) && "x".equals(getObjectGroupID_8().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("7");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_8()) && "tan binh".equals(TextUtils.removeDiacritical(getObjectGroupID_8().toLowerCase()))) {
            htcVisitEntity.setObjectGroupID("21");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_8()) && "cong an".equals(TextUtils.removeDiacritical(getObjectGroupID_8().toLowerCase()))) {
            htcVisitEntity.setObjectGroupID("20");
        }
        if (StringUtils.isNotBlank(getObjectGroupID_9()) && "có".equals(getObjectGroupID_9().toLowerCase())) {
            htcVisitEntity.setObjectGroupID("19");
        }
        if (StringUtils.isNotEmpty(getTestResultsID())) {
            htcVisitEntity.setIsAgreeTest(true);
        }

        htcVisitEntity.setTestResultsID(getTestResultsID());

        htcVisitEntity.setIsRemove(isIsRemove());

        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            htcVisitEntity.setAdvisoryeTime(StringUtils.isNotEmpty(getAdvisoryeTime()) ? sdfrmt.parse(getAdvisoryeTime()) : null);
            htcVisitEntity.setResultsTime(StringUtils.isNotEmpty(getResultsTime()) ? sdfrmt.parse(getResultsTime()) : null);
            htcVisitEntity.setPreTestTime(StringUtils.isNotEmpty(getPreTestTime()) ? sdfrmt.parse(getPreTestTime()) : null);
            htcVisitEntity.setConfirmTime(StringUtils.isNotEmpty(getConfirmTime()) ? sdfrmt.parse(getConfirmTime()) : null);
            htcVisitEntity.setSampleSentDate(StringUtils.isNotEmpty(getSampleSentDate()) ? sdfrmt.parse(getSampleSentDate()) : null);
        } catch (ParseException e) {
        }

        htcVisitEntity.setConfirmResultsID(getConfirmResultsID());
        htcVisitEntity.setSiteConfirmTest(siteConfirmTest);
        htcVisitEntity.setStaffAfterID(getStaffAfterID());
        htcVisitEntity.setHasHealthInsurance(StringUtils.isNotEmpty(getHealthInsuranceNo()) ? "1" : "0");
        htcVisitEntity.setHealthInsuranceNo(getHealthInsuranceNo());
        htcVisitEntity.setStaffBeforeTestID(getStaffBeforeTestID());
        htcVisitEntity.setYearOfBirth(StringUtils.isNotEmpty(getYearOfBirth()) ? getYearOfBirth() : null);
        htcVisitEntity.setPermanentProvinceID(getProvince());
        htcVisitEntity.setPermanentDistrictID(getDistrict());
        htcVisitEntity.setPermanentWardID(getWard());

        htcVisitEntity.setCurrentProvinceID(htcVisitEntity.getPermanentProvinceID());
        htcVisitEntity.setCurrentDistrictID(htcVisitEntity.getPermanentDistrictID());
        htcVisitEntity.setCurrentWardID(htcVisitEntity.getPermanentWardID());
        htcVisitEntity.setPatientPhone(StringUtils.isNotEmpty(getPhone()) ? getPhone().trim() : null);

        return htcVisitEntity;
    }
}

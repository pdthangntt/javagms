package com.gms.entity.form;

import com.gms.components.TextUtils;
import com.gms.components.annotation.ParameterFK;
import com.gms.entity.constant.ArvExchangeEnum;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.ConfirmTypeEnum;
import com.gms.entity.constant.DecisionAnswerEnum;
import com.gms.entity.constant.HtcConfirmStatusEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.ServiceTestEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.constant.TherapyExchangeStatusEnum;
import com.gms.entity.db.*;
import com.gms.service.ParameterService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.validation.constraints.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author vvthanh
 */
public class HtcVisitForm implements Serializable, Cloneable {

    private Long ID;
    protected static HashMap<String, String> attributeLabels;

    //----- A. Thông tin khách hàng
    @Pattern(regexp = RegexpEnum.DD_MM_YYYY, message = "Định dạng ngày không đúng dd/mm/yyyy")
    private String advisoryeTime; //Ngày tư vấn

    @Size(min = 0, max = 50, message = "Loại hình dịch vụ không được quá 50 ký tự.")
    private String serviceID;

    private String fixedServiceID;

//    @Size(max = 50, message = "Mã khách hàng không được quá 50 ký tự.")
    private String code;

    private String patientName;
    private String patientPhone;
    private String isAgreePreTest;
    private String hasHealthInsurance;
    private String healthInsuranceNo; // Số bảo hiểm y tế
    private String preTestTime;
    private String patientID; // CMND
    private String patientIDAuthen;
    private String raceID;
    private String genderID;
    private String yearOfBirth;
    private String permanentAddress; //Địa chỉ thường trú
    private String permanentAddressStreet;
    private String permanentAddressGroup;
    private String permanentProvinceID;
    private String permanentDistrictID;
    private String permanentWardID;
    private String currentAddress; //Địa chỉ đang cư trú
    private String currentAddressStreet;
    private String currentAddressGroup;
    private String testNoFixSite;
    private String currentProvinceID; // Mã tỉnh đang cư trú
    private String currentDistrictID; // Mã huyện đang cư trú
    private String currentWardID; // Mã phường/ xã đang cư trú

    @ParameterFK(fieldName = "jobID", type = ParameterEntity.JOB, mutiple = false, service = ParameterService.class)
    private String jobID;

    @ParameterFK(fieldName = "objectGroupID", type = ParameterEntity.TEST_OBJECT_GROUP, mutiple = false, service = ParameterService.class)
    private String objectGroupID; //Nhóm đối tượng

    private List<String> riskBehaviorID; //Hành vi nguy cơ lây nhiễm
    private List<String> referralSource; //Nguồn giới thiệu
    private String approacherNo; // Mã số tiếp cận cộng đồng
    private String youInjectCode; //Mã số bạn tình, bạn chích

    //----------B. Xét nghiệm sàng lọc
    @ParameterFK(fieldName = "testResultsID", type = ParameterEntity.TEST_RESULTS, mutiple = false, service = ParameterService.class)
    private String testResultsID; //Kết quả xét nghiệm sàng lọc

    private String resultsTime; //Ngày trả kết quả xét nghiệm sàng lọc
    private String isAgreeTest; // Đồng ý tiếp tục xét nghiệm hay không

    //----------C. Xét nghiệm khẳng định
    private String siteConfirmTest; // Cơ sở xét nghiệm khẳng định
    private String confirmTestNo; // Mã xét nghiệm khẳng định

    @ParameterFK(fieldName = "confirmResultsID", type = ParameterEntity.TEST_RESULTS, mutiple = false,
            service = ParameterService.class, message = "Kết quả xét nghiệm khẳng định không hợp lệ")
    private String confirmResultsID; //Kết quả xét nghiệm khẳng định
    private String modeOfTransmission; // Đường lây nhiễm
    private String resultsSiteTime; //Ngày cơ sở nhận kết quả xn khẳng định
    private String resultsPatienTime; //Ngày xét trả kết quả xét nghiệm khẳng định cho khách hàng
    private String confirmTime; //Ngày xét nghiệm khẳng định

    //-----------D. Chuyển gửi điều trị ARV
    private String exchangeConsultTime; // Ngày thực hiện tư vấn chuyển gửi điều trị
    private String arvExchangeResult; // Kết quả tư vấn chuyển điều trị ARV
    private String exchangeTime; // Ngày chuyển gửi
    private String partnerProvideResult; // Kết quả tư vấn cung cấp thông tin thực hiện xét nghiệm theo dấu bạn tình/bạn chích
    private String arrivalSite; // Tên cơ sở điều trị khách hàng được chuyển đến
    private String arrivalSiteID; //Mã cơ sở
    private String exchangeProvinceID; // Mã tỉnh nơi chuyển gửi đến
    private String exchangeDistrictID; // Mã huyện chuyển gửi đến
    private String registerTherapyTime; // Ngày đăng ký điều trị
    private String registeredTherapySite; // Tên cơ sở mà khách hành đã đăng ký điều trị
    private String therapyRegistProvinceID; // Mã tỉnh đã đăng ký điều trị
    private String therapyRegistDistrictID; // Mã huyện đã đăng ký điều trị
    private String therapyNo; // Mã số điều trị

    //-----------E. Tư vấn viên
    @NotNull(message = "Nhân viên không được để trống")
    private String staffBeforeTestID; //Nhân viên tư vấn trước xét nghiệm
    private String staffAfterID; // Nhân viên tư vấn sau xét nghiệm
    private String isRemove;

    //-------- Other
    private Long siteID;
    private String asanteTest;
    private String confirmTestStatus; // Trạng thái xét nghiệm khẳng định
    private String pacSentDate;   // Trạng thái chuyển gửi GSPH dựa trên ngày này
    private String pacPatientID;  // Id của bênh nhân bên quản lý người nhiễm
    private String dpltmcStatus; // Trạng thái dự phòng láy truyền từ mẹ sang con
    private String therapyExchangeStatus; // Trạng thái chuyển gửi điều trị
    private String opcSentDate;   // Trạng thái chuyển gửi điều trị dựa trên ngày này
    private String opcPatientID;  // Id của bênh nhân bên quản lý điều trị

    // Ngày mẫu máu
    private String sampleSentDate;

    // Continue creating flag
    private String pageRedirect;
    private Boolean isDisplayCopy;

    private String updateConfirmTime;
    private String requestConfirmTime;
    private String earlyHiv;
    private String virusLoad;
    private Long laytestID;
    private String bioName; // Tên sinh phẩm
    private boolean isNew;

    private String earlyHivDate;
    private String virusLoadDate;

    private String pepfarProjectID;

    // Bổ sung sửa theo phiếu SHIFT
    private String cdService; // Dịch vụ tại cơ sở TVXN cố định
    private String testMethod; // Phương pháp XN sàng lọc
    private String resultAnti; // KQXN Kháng nguyên/Kháng thể
    private String confirmType; // Loại hình XN khẳng định
    private String pcrHiv; // Chọn câu trả lời PCR HIV
    private String resultPcrHiv; // Xét nghiệm PCR HIV bổ sung
    private String exchangeService; // Dịch vụ tư vấn chuyển gửi
    private String exchangeServiceConfirm; // Dịch vụ tư vấn chuyển gửi thông tin khẳng định
    private String exchangeServiceName; // Dịch vụ chuyển gửi khác
    private String exchangeServiceNameConfirm; // Dịch vụ chuyển gửi khác thông tin khẳng định
    
    private String causeChange;
    
    private boolean disableEarlyHiv;  // cờ đánh dấu disabled trường earlyHiv
    private boolean disableVirusLoad; // cờ đánh dấu disabled trường virus load
    private Long currentSiteID; // Mã cơ sở đang đăng nhập
    private boolean confirmResutl; // Cờ đánh dấu nhận kết quả từ khẳng định

    private String staffKC; // Mã nhân viên không chuyên
    private String customerType; // Loại khách hàng
    private String laoVariable; // Thể LAO
    private String laoVariableName; // Thể LAO khác
    private String defaultProject;// Dự án mặc định
    
    private String virusLoadNumber; //Kết quả xét nghiệm TLVR - nhập số
    private String earlyBioName; // Tên sinh phẩm nhiễm mới
    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    public String getVirusLoadNumber() {
        return virusLoadNumber;
    }

    public void setVirusLoadNumber(String virusLoadNumber) {
        this.virusLoadNumber = virusLoadNumber;
    }

    public String getEarlyBioName() {
        return earlyBioName;
    }

    public void setEarlyBioName(String earlyBioName) {
        this.earlyBioName = earlyBioName;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public String getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(String defaultProject) {
        this.defaultProject = defaultProject;
    }

    public String getStaffKC() {
        return staffKC;
    }

    public void setStaffKC(String staffKC) {
        this.staffKC = staffKC;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getLaoVariable() {
        return laoVariable;
    }

    public void setLaoVariable(String laoVariable) {
        this.laoVariable = laoVariable;
    }

    public String getLaoVariableName() {
        return laoVariableName;
    }

    public void setLaoVariableName(String laoVariableName) {
        this.laoVariableName = laoVariableName;
    }
    
    public boolean isConfirmResutl() {
        return confirmResutl;
    }

    public void setConfirmResutl(boolean confirmResutl) {
        this.confirmResutl = confirmResutl;
    }
    
    public Long getCurrentSiteID() {
        return currentSiteID;
    }

    public void setCurrentSiteID(Long currentSiteID) {
        this.currentSiteID = currentSiteID;
    }

    public boolean isDisableEarlyHiv() {
        return disableEarlyHiv;
    }

    public void setDisableEarlyHiv(boolean disableEarlyHiv) {
        this.disableEarlyHiv = disableEarlyHiv;
    }

    public boolean isDisableVirusLoad() {
        return disableVirusLoad;
    }

    public void setDisableVirusLoad(boolean disableVirusLoad) {
        this.disableVirusLoad = disableVirusLoad;
    }
    
    public String getCauseChange() {
        return causeChange;
    }

    public void setCauseChange(String causeChange) {
        this.causeChange = causeChange;
    }
    
    public String getExchangeServiceNameConfirm() {
        return exchangeServiceNameConfirm;
    }

    public void setExchangeServiceNameConfirm(String exchangeServiceNameConfirm) {
        this.exchangeServiceNameConfirm = exchangeServiceNameConfirm;
    }
    
    public String getExchangeServiceConfirm() {
        return exchangeServiceConfirm;
    }

    public void setExchangeServiceConfirm(String exchangeServiceConfirm) {
        this.exchangeServiceConfirm = exchangeServiceConfirm;
    }
    
    public String getCdService() {
        return cdService;
    }

    public void setCdService(String cdService) {
        this.cdService = cdService;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getResultAnti() {
        return resultAnti;
    }

    public void setResultAnti(String resultAnti) {
        this.resultAnti = resultAnti;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public String getUpdateConfirmTime() {
        return updateConfirmTime;
    }

    public void setUpdateConfirmTime(String updateConfirmTime) {
        this.updateConfirmTime = updateConfirmTime;
    }

    public String getRequestConfirmTime() {
        return requestConfirmTime;
    }

    public void setRequestConfirmTime(String requestConfirmTime) {
        this.requestConfirmTime = requestConfirmTime;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public String getPcrHiv() {
        return pcrHiv;
    }

    public void setPcrHiv(String pcrHiv) {
        this.pcrHiv = pcrHiv;
    }

    public String getResultPcrHiv() {
        return resultPcrHiv;
    }

    public void setResultPcrHiv(String resultPcrHiv) {
        this.resultPcrHiv = resultPcrHiv;
    }

    public String getExchangeService() {
        return exchangeService;
    }

    public void setExchangeService(String exchangeService) {
        this.exchangeService = exchangeService;
    }

    public String getExchangeServiceName() {
        return exchangeServiceName;
    }

    public void setExchangeServiceName(String exchangeServiceName) {
        this.exchangeServiceName = exchangeServiceName;
    }

    public String getPermanentAddressStreet() {
        return permanentAddressStreet;
    }

    public void setPermanentAddressStreet(String permanentAddressStreet) {
        this.permanentAddressStreet = permanentAddressStreet;
    }

    public String getPermanentAddressGroup() {
        return permanentAddressGroup;
    }

    public void setPermanentAddressGroup(String permanentAddressGroup) {
        this.permanentAddressGroup = permanentAddressGroup;
    }

    public String getCurrentAddressStreet() {
        return currentAddressStreet;
    }

    public void setCurrentAddressStreet(String currentAddressStreet) {
        this.currentAddressStreet = currentAddressStreet;
    }

    public String getCurrentAddressGroup() {
        return currentAddressGroup;
    }

    public void setCurrentAddressGroup(String currentAddressGroup) {
        this.currentAddressGroup = currentAddressGroup;
    }

    public String getArrivalSiteID() {
        return arrivalSiteID;
    }

    public void setArrivalSiteID(String arrivalSiteID) {
        this.arrivalSiteID = arrivalSiteID;
    }

    public String getPepfarProjectID() {
        return pepfarProjectID;
    }

    public void setPepfarProjectID(String pepfarProjectID) {
        this.pepfarProjectID = pepfarProjectID;
    }

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getVirusLoadDate() {
        return virusLoadDate;
    }

    public void setVirusLoadDate(String virusLoadDate) {
        this.virusLoadDate = virusLoadDate;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getBioName() {
        return bioName;
    }

    public void setBioName(String bioName) {
        this.bioName = bioName;
    }

    public String getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(String isRemove) {
        this.isRemove = isRemove;
    }

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getPatientIDAuthen() {
        return patientIDAuthen;
    }

    public void setPatientIDAuthen(String patientIDAuthen) {
        this.patientIDAuthen = patientIDAuthen;
    }

    public static HashMap<String, String> getAttributeLabels() {
        return attributeLabels;
    }

    public static void setAttributeLabels(HashMap<String, String> attributeLabels) {
        HtcVisitForm.attributeLabels = attributeLabels;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
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

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getCode() {
        return StringUtils.isNotEmpty(code) ? code.trim() : StringUtils.EMPTY;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getServiceID() {
        return StringUtils.isNotEmpty(serviceID) ? serviceID.trim() : StringUtils.EMPTY;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getPatientName() {
        return StringUtils.isNotEmpty(patientName) ? patientName.trim() : StringUtils.EMPTY;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return StringUtils.isNotEmpty(patientPhone) ? patientPhone.trim() : StringUtils.EMPTY;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientID() {
        return StringUtils.isNotEmpty(patientID) ? patientID.trim() : StringUtils.EMPTY;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getRaceID() {
        return StringUtils.isNotEmpty(raceID) ? raceID.trim() : StringUtils.EMPTY;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

//    @NotBlank(message = "Giới tính không được trống")
    public String getGenderID() {
        return StringUtils.isNotEmpty(genderID) ? genderID.trim() : StringUtils.EMPTY;
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

    public String getPermanentProvinceID() {
        return StringUtils.isNotEmpty(permanentProvinceID) ? permanentProvinceID.trim() : StringUtils.EMPTY;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getPermanentDistrictID() {
        return StringUtils.isNotEmpty(permanentDistrictID) ? permanentDistrictID.trim() : StringUtils.EMPTY;
    }

    public void setPermanentDistrictID(String permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public String getPermanentWardID() {
        return StringUtils.isNotEmpty(permanentWardID) ? permanentWardID.trim() : StringUtils.EMPTY;
    }

    public void setPermanentWardID(String permanentWardID) {
        this.permanentWardID = permanentWardID;
    }

    public String getJobID() {
        return StringUtils.isNotEmpty(jobID) ? jobID.trim() : StringUtils.EMPTY;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public List<String> getRiskBehaviorID() {
        return riskBehaviorID;
    }

    public void setRiskBehaviorID(List<String> riskBehaviorID) {
        this.riskBehaviorID = riskBehaviorID;
    }

    public String getYouInjectCode() {
        return StringUtils.isNotEmpty(youInjectCode) ? youInjectCode.trim() : StringUtils.EMPTY;
    }

    public void setYouInjectCode(String youInjectCode) {
        this.youInjectCode = youInjectCode;
    }

    public String getTestResultsID() {
        return StringUtils.isNotEmpty(testResultsID) ? testResultsID.trim() : StringUtils.EMPTY;
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

    public String getSiteConfirmTest() {
        return siteConfirmTest;
    }

    public void setSiteConfirmTest(String siteConfirmTest) {
        this.siteConfirmTest = siteConfirmTest;
    }

    public String getFixedServiceID() {
        return StringUtils.isNotEmpty(fixedServiceID) ? fixedServiceID.trim() : StringUtils.EMPTY;
    }

    public void setFixedServiceID(String fixedServiceID) {
        this.fixedServiceID = fixedServiceID;
    }

    public String getModeOfTransmission() {
        return modeOfTransmission;
    }

    public void setModeOfTransmission(String modeOfTransmission) {
        this.modeOfTransmission = modeOfTransmission;
    }

    public String getArvExchangeResult() {
        return StringUtils.isNotEmpty(arvExchangeResult) ? arvExchangeResult.trim() : StringUtils.EMPTY;
    }

    public void setArvExchangeResult(String arvExchangeResult) {
        this.arvExchangeResult = arvExchangeResult;
    }

    public String getPartnerProvideResult() {
        return StringUtils.isNotEmpty(partnerProvideResult) ? partnerProvideResult.trim() : StringUtils.EMPTY;
    }

    public void setPartnerProvideResult(String partnerProvideResult) {
        this.partnerProvideResult = partnerProvideResult;
    }

    public String getCurrentAddress() {
        return StringUtils.isNotEmpty(currentAddress) ? currentAddress.trim() : StringUtils.EMPTY;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentProvinceID() {
        return StringUtils.isNotEmpty(currentProvinceID) ? currentProvinceID.trim() : StringUtils.EMPTY;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getCurrentDistrictID() {
        return StringUtils.isNotEmpty(currentDistrictID) ? currentDistrictID.trim() : StringUtils.EMPTY;
    }

    public void setCurrentDistrictID(String currentDistrictID) {
        this.currentDistrictID = currentDistrictID;
    }

    public String getCurrentWardID() {
        return StringUtils.isNotEmpty(currentWardID) ? currentWardID.trim() : StringUtils.EMPTY;
    }

    public void setCurrentWardID(String currentWardID) {
        this.currentWardID = currentWardID;
    }

    public String getExchangeProvinceID() {
        return StringUtils.isNotEmpty(exchangeProvinceID) ? exchangeProvinceID.trim() : StringUtils.EMPTY;
    }

    public void setExchangeProvinceID(String exchangeProvinceID) {
        this.exchangeProvinceID = exchangeProvinceID;
    }

    public String getExchangeDistrictID() {
        return StringUtils.isNotEmpty(exchangeDistrictID) ? exchangeDistrictID.trim() : StringUtils.EMPTY;
    }

    public void setExchangeDistrictID(String exchangeDistrictID) {
        this.exchangeDistrictID = exchangeDistrictID;
    }

    public String getTherapyRegistProvinceID() {
        return StringUtils.isNotEmpty(therapyRegistProvinceID) ? therapyRegistProvinceID.trim() : StringUtils.EMPTY;
    }

    public void setTherapyRegistProvinceID(String therapyRegistProvinceID) {
        this.therapyRegistProvinceID = therapyRegistProvinceID;
    }

    public String getTherapyRegistDistrictID() {
        return StringUtils.isNotEmpty(therapyRegistDistrictID) ? therapyRegistDistrictID.trim() : StringUtils.EMPTY;
    }

    public void setTherapyRegistDistrictID(String therapyRegistDistrictID) {
        this.therapyRegistDistrictID = therapyRegistDistrictID;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getArrivalSite() {
        return StringUtils.isNotEmpty(arrivalSite) ? arrivalSite.trim() : StringUtils.EMPTY;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public String getRegisteredTherapySite() {
        return StringUtils.isNotEmpty(registeredTherapySite) ? registeredTherapySite.trim() : StringUtils.EMPTY;
    }

    public void setRegisteredTherapySite(String registeredTherapySite) {
        this.registeredTherapySite = registeredTherapySite;
    }

    public String getExchangeConsultTime() {
        return exchangeConsultTime;
    }

    public void setExchangeConsultTime(String exchangeConsultTime) {
        this.exchangeConsultTime = exchangeConsultTime;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getRegisterTherapyTime() {
        return registerTherapyTime;
    }

    public void setRegisterTherapyTime(String registerTherapyTime) {
        this.registerTherapyTime = registerTherapyTime;
    }

    public String getTherapyNo() {
        return therapyNo;
    }

    public void setTherapyNo(String therapyNo) {
        this.therapyNo = therapyNo;
    }

    public String getApproacherNo() {
        return StringUtils.isNotEmpty(approacherNo) ? approacherNo.trim() : StringUtils.EMPTY;
    }

    public void setApproacherNo(String approacherNo) {
        this.approacherNo = approacherNo;
    }

    public List<String> getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(List<String> referralSource) {
        this.referralSource = referralSource;
    }

    public String getResultsTime() {
        return StringUtils.isNotEmpty(resultsTime) ? resultsTime.trim() : StringUtils.EMPTY;
    }

    public void setResultsTime(String resultsTime) {
        this.resultsTime = resultsTime;
    }

    public String getResultsSiteTime() {
        return StringUtils.isNotEmpty(resultsSiteTime) ? resultsSiteTime.trim() : StringUtils.EMPTY;
    }

    public void setResultsSiteTime(String resultsSiteTime) {
        this.resultsSiteTime = resultsSiteTime;
    }

    public String getResultsPatienTime() {
        return StringUtils.isNotEmpty(resultsPatienTime) ? resultsPatienTime.trim() : StringUtils.EMPTY;
    }

    public void setResultsPatienTime(String resultsPatienTime) {
        this.resultsPatienTime = resultsPatienTime;
    }

    public String getConfirmTime() {
        return StringUtils.isNotEmpty(confirmTime) ? confirmTime.trim() : StringUtils.EMPTY;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getIsAgreeTest() {
        return StringUtils.isNotEmpty(isAgreeTest) ? isAgreeTest.trim() : StringUtils.EMPTY;
    }

    public void setIsAgreeTest(String isAgreeTest) {
        this.isAgreeTest = isAgreeTest;
    }

    public Boolean getIsDisplayCopy() {
        return isDisplayCopy;
    }

    public void setIsDisplayCopy(Boolean isDisplayCopy) {
        this.isDisplayCopy = isDisplayCopy;
    }

    public String getPageRedirect() {
        return StringUtils.isNotEmpty(pageRedirect) ? pageRedirect.trim() : StringUtils.EMPTY;
    }

    public void setPageRedirect(String pageRedirect) {
        this.pageRedirect = pageRedirect;
    }

    public String getObjectGroupID() {
        return StringUtils.isNotEmpty(objectGroupID) ? objectGroupID.trim() : StringUtils.EMPTY;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getTestNoFixSite() {
        return StringUtils.isNotEmpty(testNoFixSite) ? testNoFixSite.trim() : StringUtils.EMPTY;
    }

    public void setTestNoFixSite(String testNoFixSite) {
        this.testNoFixSite = testNoFixSite;
    }

    public String getAsanteTest() {
        return StringUtils.isNotEmpty(asanteTest) ? asanteTest.trim() : StringUtils.EMPTY;
    }

    public void setAsanteTest(String asanteTest) {
        this.asanteTest = asanteTest;
    }

    public String getIsAgreePreTest() {
        return StringUtils.isNotEmpty(isAgreePreTest) ? isAgreePreTest.trim() : StringUtils.EMPTY;
    }

    public void setIsAgreePreTest(String isAgreePreTest) {
        this.isAgreePreTest = isAgreePreTest;
    }

    public String getPreTestTime() {
        return StringUtils.isNotEmpty(preTestTime) ? preTestTime.trim() : StringUtils.EMPTY;
    }

    public void setPreTestTime(String preTestTime) {
        this.preTestTime = preTestTime;
    }

    public String getHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public void setHasHealthInsurance(String hasHealthInsurance) {
        this.hasHealthInsurance = hasHealthInsurance;
    }

    public String getHealthInsuranceNo() {
        return StringUtils.isNotEmpty(healthInsuranceNo) ? healthInsuranceNo.trim() : healthInsuranceNo;
    }

    public void setHealthInsuranceNo(String healthInsuranceNo) {
        this.healthInsuranceNo = healthInsuranceNo;
    }

    public String getConfirmTestStatus() {
        return confirmTestStatus;
    }

    public void setConfirmTestStatus(String confirmTestStatus) {
        this.confirmTestStatus = confirmTestStatus;
    }

    public String getDpltmcStatus() {
        return dpltmcStatus;
    }

    public void setDpltmcStatus(String dpltmcStatus) {
        this.dpltmcStatus = dpltmcStatus;
    }

    public String getTherapyExchangeStatus() {
        return therapyExchangeStatus;
    }

    public void setTherapyExchangeStatus(String therapyExchangeStatus) {
        this.therapyExchangeStatus = therapyExchangeStatus;
    }

    public String getSampleSentDate() {
        return sampleSentDate;
    }

    public void setSampleSentDate(String sampleSentDate) {
        this.sampleSentDate = sampleSentDate;
    }

    public Long getLaytestID() {
        return laytestID;
    }

    public void setLaytestID(Long laytestID) {
        this.laytestID = laytestID;
    }

    public String getPacSentDate() {
        return pacSentDate;
    }

    public void setPacSentDate(String pacSentDate) {
        this.pacSentDate = pacSentDate;
    }

    public String getPacPatientID() {
        return pacPatientID;
    }

    public void setPacPatientID(String pacPatientID) {
        this.pacPatientID = pacPatientID;
    }

    public String getOpcSentDate() {
        return opcSentDate;
    }

    public void setOpcSentDate(String opcSentDate) {
        this.opcSentDate = opcSentDate;
    }

    public String getOpcPatientID() {
        return opcPatientID;
    }

    public void setOpcPatientID(String opcPatientID) {
        this.opcPatientID = opcPatientID;
    }

    /**
     * Set dữ liệu từ entity sang form
     *
     * @author TrangBN
     * @param htcVisitEntity
     */
    public void setFromVisit(HtcVisitEntity htcVisitEntity) {
        String formatDate = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
        setID(htcVisitEntity.getID());
        setSiteID(htcVisitEntity.getSiteID());
        if (htcVisitEntity.getAdvisoryeTime() != null) {
            setAdvisoryeTime(TextUtils.formatDate(htcVisitEntity.getAdvisoryeTime(), formatDate));
        }
        if (htcVisitEntity.getRequestConfirmTime()!= null) {
            setRequestConfirmTime(simpleDateFormat.format(htcVisitEntity.getRequestConfirmTime()));
        }
        if (htcVisitEntity.getUpdateConfirmTime()!= null) {
            setUpdateConfirmTime(simpleDateFormat.format(htcVisitEntity.getUpdateConfirmTime()));
        }
        
        setServiceID(htcVisitEntity.getServiceID());
        setCode(htcVisitEntity.getCode());

        setPatientName(htcVisitEntity.getPatientName());
        setPatientPhone(htcVisitEntity.getPatientPhone());
        setPatientID(htcVisitEntity.getPatientID());
        setRaceID(htcVisitEntity.getRaceID());
        setGenderID(htcVisitEntity.getGenderID());
        setYearOfBirth(htcVisitEntity.getYearOfBirth());
        setHasHealthInsurance(htcVisitEntity.getHasHealthInsurance());
        setHealthInsuranceNo(htcVisitEntity.getHealthInsuranceNo());
        setPermanentAddress(htcVisitEntity.getPermanentAddress());
        setPermanentAddressGroup(htcVisitEntity.getPermanentAddressGroup());
        setPermanentAddressStreet(htcVisitEntity.getPermanentAddressStreet());
        setPermanentProvinceID(htcVisitEntity.getPermanentProvinceID());
        setPermanentDistrictID(htcVisitEntity.getPermanentDistrictID());
        setPermanentWardID(htcVisitEntity.getPermanentWardID());
        setCurrentAddress(htcVisitEntity.getCurrentAddress());
        setCurrentAddressGroup(htcVisitEntity.getCurrentAddressGroup());
        setCurrentAddressStreet(htcVisitEntity.getCurrentAddressStreet());
        setTestNoFixSite(htcVisitEntity.getTestNoFixSite());
        setCurrentProvinceID(htcVisitEntity.getCurrentProvinceID());
        setCurrentDistrictID(htcVisitEntity.getCurrentDistrictID());
        setCurrentWardID(htcVisitEntity.getCurrentWardID());
        setJobID(htcVisitEntity.getJobID());
        setObjectGroupID(htcVisitEntity.getObjectGroupID());
        setModeOfTransmission(htcVisitEntity.getModeOfTransmission());
        setRiskBehaviorID(htcVisitEntity.getRiskBehaviorID());
        setReferralSource(htcVisitEntity.getReferralSource());
        setApproacherNo(htcVisitEntity.getApproacherNo());
        setYouInjectCode(htcVisitEntity.getYouInjectCode());
        setTestResultsID(htcVisitEntity.getTestResultsID());
        setIsAgreePreTest(htcVisitEntity.getIsAgreePreTest());
        if (htcVisitEntity.getPreTestTime() != null) {
            setPreTestTime(TextUtils.formatDate(htcVisitEntity.getPreTestTime(), formatDate));
        }
        if (htcVisitEntity.getResultsTime() != null) {
            setResultsTime(simpleDateFormat.format(htcVisitEntity.getResultsTime()));
        }
        if (htcVisitEntity.getIsAgreeTest() != null) {
            setIsAgreeTest(htcVisitEntity.getIsAgreeTest().toString());
        }
        if (htcVisitEntity.getPatientIDAuthen() != null) {
            setPatientIDAuthen(htcVisitEntity.getPatientIDAuthen());
        }
        setSiteConfirmTest(htcVisitEntity.getSiteConfirmTest());
        setConfirmTestNo(htcVisitEntity.getConfirmTestNo());
        setConfirmResultsID(htcVisitEntity.getConfirmResultsID());
        setEarlyHiv(htcVisitEntity.getEarlyHiv());
        setVirusLoad(htcVisitEntity.getVirusLoad());
        setModeOfTransmission(htcVisitEntity.getModeOfTransmission());
        if (htcVisitEntity.getResultsSiteTime() != null) {
            setResultsSiteTime(simpleDateFormat.format(htcVisitEntity.getResultsSiteTime()));
        }
        if (htcVisitEntity.getResultsPatienTime() != null) {
            setResultsPatienTime(simpleDateFormat.format(htcVisitEntity.getResultsPatienTime()));
        }
        if (htcVisitEntity.getConfirmTime() != null) {
            setConfirmTime(simpleDateFormat.format(htcVisitEntity.getConfirmTime()));
        }
        if (htcVisitEntity.getExchangeConsultTime() != null) {
            setExchangeConsultTime(simpleDateFormat.format(htcVisitEntity.getExchangeConsultTime()));
        }
        if (htcVisitEntity.getExchangeTime() != null) {
            setExchangeTime(simpleDateFormat.format(htcVisitEntity.getExchangeTime()));
        }
        if (htcVisitEntity.getRegisterTherapyTime() != null) {
            setRegisterTherapyTime(simpleDateFormat.format(htcVisitEntity.getRegisterTherapyTime()));
        }
        setArvExchangeResult(htcVisitEntity.getArvExchangeResult());
        setPartnerProvideResult(htcVisitEntity.getPartnerProvideResult());
        setArrivalSite(htcVisitEntity.getArrivalSite());
        setArrivalSiteID(String.valueOf(htcVisitEntity.getArrivalSiteID()));
        setExchangeProvinceID(htcVisitEntity.getExchangeProvinceID());
        setExchangeDistrictID(htcVisitEntity.getExchangeDistrictID());
        setRegisteredTherapySite(htcVisitEntity.getRegisteredTherapySite());
        setTherapyRegistProvinceID(htcVisitEntity.getTherapyRegistProvinceID());
        setTherapyRegistDistrictID(htcVisitEntity.getTherapyRegistDistrictID());
        setTherapyNo(htcVisitEntity.getTherapyNo());
        setStaffBeforeTestID(htcVisitEntity.getStaffBeforeTestID());
        setStaffAfterID(htcVisitEntity.getStaffAfterID());
        setAsanteTest(htcVisitEntity.getAsanteTest());
        setSampleSentDate(htcVisitEntity.getSampleSentDate() == null ? null : TextUtils.formatDate(htcVisitEntity.getSampleSentDate(), formatDate));
        if (StringUtils.isNotEmpty(htcVisitEntity.getConfirmTestStatus())) {
            setConfirmTestStatus(htcVisitEntity.getConfirmTestStatus());
        }

        // Set trạng thái chuyển gửi điều trị
        if ("1".equals(this.getArvExchangeResult())
                && StringUtils.isNotBlank(this.getArrivalSite())
                && StringUtils.isNotBlank(this.getExchangeTime())) {
            setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_CHUYEN.getKey()); // Set trạng thái sang đã chuyển
        }

        if (StringUtils.isNotBlank(this.getRegisterTherapyTime())
                && StringUtils.isNotBlank(this.getRegisteredTherapySite())
                && StringUtils.isNotBlank(this.getTherapyNo())) {
            setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey()); // Trạng thái chuyển gửi thành công
        }

        //12/05/2020
        if (htcVisitEntity.getFeedbackReceiveTime() == null && htcVisitEntity.getArrivalTime() != null) {
            setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_TIEP_NHAN.getKey());
        }

        setPacSentDate(htcVisitEntity.getPacSentDate() != null ? TextUtils.formatDate(htcVisitEntity.getPacSentDate(), formatDate) : "");
        setPacPatientID(htcVisitEntity.getPacPatientID() != null ? htcVisitEntity.getPacPatientID().toString() : null);
        setOpcSentDate(htcVisitEntity.getOpcSentDate() != null ? TextUtils.formatDate(htcVisitEntity.getOpcSentDate(), formatDate) : "");
        setOpcPatientID(htcVisitEntity.getOpcPatientID() != null ? htcVisitEntity.getOpcPatientID().toString() : null);
        setBioName(htcVisitEntity.getBioName());
        setEarlyHivDate(htcVisitEntity.getEarlyHivDate() != null ? simpleDateFormat.format(htcVisitEntity.getEarlyHivDate()) : null);
        setVirusLoadDate(htcVisitEntity.getVirusLoadDate() != null ? simpleDateFormat.format(htcVisitEntity.getVirusLoadDate()) : null);
        setPepfarProjectID(htcVisitEntity.getPepfarProjectID());
        
        setCdService(htcVisitEntity.getCdService());
        setTestMethod(htcVisitEntity.getTestMethod());
        setResultAnti(htcVisitEntity.getResultAnti());
        setConfirmType(htcVisitEntity.getConfirmType());
        setPcrHiv(htcVisitEntity.isPcrHiv() ? "1" : "0");
        setResultPcrHiv(htcVisitEntity.getResultPcrHiv());
        
        if (StringUtils.isNotEmpty(htcVisitEntity.getTestResultsID()) && htcVisitEntity.getTestResultsID().equals(TestResultEnum.KHONG_PHAN_UNG.getKey())) {
            setExchangeService(htcVisitEntity.getExchangeService());
            setExchangeServiceName(htcVisitEntity.getExchangeServiceName());
        } else if (StringUtils.isNotEmpty(htcVisitEntity.getTestResultsID())) {
            setExchangeServiceConfirm(htcVisitEntity.getExchangeService());
            setExchangeServiceNameConfirm(htcVisitEntity.getExchangeServiceName());
        }
        
        if (htcVisitEntity.getID() != null && StringUtils.isNotEmpty(htcVisitEntity.getConfirmResultsID()) && htcVisitEntity.getConfirmResultsID().equals(ConfirmTestResultEnum.DUONG_TINH)) {
            setDisableEarlyHiv(StringUtils.isNotEmpty(htcVisitEntity.getEarlyHiv() ) && htcVisitEntity.getEarlyHivDate() != null );
            setDisableVirusLoad(StringUtils.isNotEmpty(htcVisitEntity.getVirusLoad()) && htcVisitEntity.getVirusLoadDate() != null);
        } else if(htcVisitEntity.getID() != null) {
            setDisableEarlyHiv(true);
        }
        
        if (htcVisitEntity.getID() != null && StringUtils.isNotEmpty(htcVisitEntity.getConfirmResultsID()) && !htcVisitEntity.getConfirmResultsID().equals(ConfirmTestResultEnum.DUONG_TINH)) {
            setDisableVirusLoad(StringUtils.isNotEmpty(htcVisitEntity.getVirusLoad()) && htcVisitEntity.getVirusLoadDate() != null);
        }
        setConfirmResutl(htcVisitEntity.isConfirmResutl());
        
        setStaffKC(htcVisitEntity.getStaffKC());
        setCustomerType(htcVisitEntity.getCustomerType());
        setLaoVariable(htcVisitEntity.getLaoVariable());
        setLaoVariableName(htcVisitEntity.getLaoVariableName());
        
        setVirusLoadNumber(htcVisitEntity.getVirusLoadNumber());
        setEarlyBioName(htcVisitEntity.getEarlyBioName());
        setEarlyDiagnose(htcVisitEntity.getEarlyDiagnose());
    }

    /**
     * Set dữ liệu từ entity sang form, trong trường hợp đã trả kết quả khẳng
     * định
     *
     * @author TrangBN
     * @param htcVisitEntity
     */
    public void setFromVisitReceiveResult(HtcVisitEntity htcVisitEntity) {
        String formatDate = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
        setID(htcVisitEntity.getID());
        setSiteID(htcVisitEntity.getSiteID());
        if (htcVisitEntity.getAdvisoryeTime() != null) {
            setAdvisoryeTime(TextUtils.formatDate(htcVisitEntity.getAdvisoryeTime(), formatDate));
        }
        setServiceID(htcVisitEntity.getServiceID());
        setCode(htcVisitEntity.getCode());

        setPatientName(htcVisitEntity.getPatientName());
        setPatientPhone(htcVisitEntity.getPatientPhone());
        setPatientID(htcVisitEntity.getPatientID());
        setRaceID(htcVisitEntity.getRaceID());
        setGenderID(htcVisitEntity.getGenderID());
        setYearOfBirth(htcVisitEntity.getYearOfBirth());
        setHasHealthInsurance(htcVisitEntity.getHasHealthInsurance());
        setHealthInsuranceNo(htcVisitEntity.getHealthInsuranceNo());
        setPermanentAddress(htcVisitEntity.getPermanentAddress());
        setPermanentAddressGroup(htcVisitEntity.getPermanentAddressGroup());
        setPermanentAddressStreet(htcVisitEntity.getPermanentAddressStreet());
        setPermanentProvinceID(htcVisitEntity.getPermanentProvinceID());
        setPermanentDistrictID(htcVisitEntity.getPermanentDistrictID());
        setPermanentWardID(htcVisitEntity.getPermanentWardID());
        setCurrentAddress(htcVisitEntity.getCurrentAddress());
        setCurrentAddressGroup(htcVisitEntity.getCurrentAddressGroup());
        setCurrentAddressStreet(htcVisitEntity.getCurrentAddressStreet());
        setTestNoFixSite(htcVisitEntity.getTestNoFixSite());
        setCurrentProvinceID(htcVisitEntity.getCurrentProvinceID());
        setCurrentDistrictID(htcVisitEntity.getCurrentDistrictID());
        setCurrentWardID(htcVisitEntity.getCurrentWardID());
        setJobID(htcVisitEntity.getJobID());
        setObjectGroupID(htcVisitEntity.getObjectGroupID());
        setModeOfTransmission(htcVisitEntity.getModeOfTransmission());
        setRiskBehaviorID(htcVisitEntity.getRiskBehaviorID());
        setReferralSource(htcVisitEntity.getReferralSource());
        setApproacherNo(htcVisitEntity.getApproacherNo());
        setYouInjectCode(htcVisitEntity.getYouInjectCode());
        setTestResultsID(htcVisitEntity.getTestResultsID());
        setIsAgreeTest(htcVisitEntity.getIsAgreePreTest());
        setIsAgreePreTest(htcVisitEntity.getIsAgreePreTest());
        if (htcVisitEntity.getPreTestTime() != null) {
            setPreTestTime(TextUtils.formatDate(htcVisitEntity.getPreTestTime(), formatDate));
        }
        if (htcVisitEntity.getResultsTime() != null) {
            setResultsTime(simpleDateFormat.format(htcVisitEntity.getResultsTime()));
        }
        if (htcVisitEntity.getIsAgreeTest() != null) {
            setIsAgreeTest(htcVisitEntity.getIsAgreeTest().toString());
        }
        if (htcVisitEntity.getPatientIDAuthen() != null) {
            setPatientIDAuthen(htcVisitEntity.getPatientIDAuthen());
        }
        setSiteConfirmTest(htcVisitEntity.getSiteConfirmTest());
        setConfirmTestNo(htcVisitEntity.getConfirmTestNo());
        setConfirmResultsID(htcVisitEntity.getConfirmResultsID());
        setModeOfTransmission(htcVisitEntity.getModeOfTransmission());

        setPacSentDate(htcVisitEntity.getPacSentDate() != null ? TextUtils.formatDate(htcVisitEntity.getPacSentDate(), formatDate) : "");
        setPacPatientID(htcVisitEntity.getPacPatientID() != null ? htcVisitEntity.getPacPatientID().toString() : null);
        setOpcSentDate(htcVisitEntity.getOpcSentDate() != null ? TextUtils.formatDate(htcVisitEntity.getOpcSentDate(), formatDate) : "");
        setOpcPatientID(htcVisitEntity.getOpcPatientID() != null ? htcVisitEntity.getOpcPatientID().toString() : null);
        setBioName(htcVisitEntity.getBioName());
        if (StringUtils.isNotEmpty(htcVisitEntity.getConfirmTestStatus())) {
            setConfirmTestStatus(htcVisitEntity.getConfirmTestStatus());
        }
        // Sửa theo phiếu SHIFT 20/08/2020
        setCdService(htcVisitEntity.getCdService());
        setTestMethod(htcVisitEntity.getTestMethod());
        setResultAnti(htcVisitEntity.getResultAnti());
        setExchangeService(htcVisitEntity.getExchangeService());
        setExchangeServiceName(htcVisitEntity.getExchangeServiceName());
        setConfirmType(htcVisitEntity.getConfirmType());
        setStaffKC(htcVisitEntity.getStaffKC());
        setCustomerType(htcVisitEntity.getCustomerType());
        setLaoVariable(htcVisitEntity.getLaoVariable());
        setLaoVariableName(htcVisitEntity.getLaoVariableName());
//        setPcrHiv(htcVisitEntity.isPcrHiv() ? "1" : "0");
//        setResultPcrHiv(htcVisitEntity.getResultPcrHiv());
        
    }

    /**
     * Chuyển đổi dữ liệu về entity
     *
     * @auth TrangBN
     * @param htcEntity
     * @param siteEntity
     * @return
     * @throws ParseException
     */
    public HtcVisitEntity getHtcVisit(HtcVisitEntity htcEntity, SiteEntity siteEntity) throws ParseException {
        String formatDate = "dd/MM/yyyy";
        // Default service type
        final String SERVICE_TYPE_DEFAULT = "CD";
        // Kết quả test có phản ứng
        final String REACTIVE_TEST_RESULT = "2";
        // Kết quả test không xác định
        final String UN_SPECIFIED_RESULT = "3";
        // Kết quả tư vấn đồng ý chuyển gửi
        // Có thể BHYT 
        final String NO_HEALTH_INSURANCE = "0";

        // Cập nhật sau khi chuyển sang GSPH, Thông tin từ C6 trở đi 
        if (htcEntity != null && htcEntity.getPacSentDate() != null && htcEntity.getPacPatientID() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
            htcEntity.setResultsPatienTime(StringUtils.isEmpty(getResultsPatienTime()) ? null : simpleDateFormat.parse(getResultsPatienTime()));
            htcEntity.setExchangeConsultTime(StringUtils.isNotEmpty(getExchangeConsultTime()) ? simpleDateFormat.parse(getExchangeConsultTime()) : null);
            htcEntity.setPartnerProvideResult(getPartnerProvideResult());
            htcEntity.setArvExchangeResult(getArvExchangeResult());
            htcEntity.setExchangeTime(StringUtils.isNotEmpty(getExchangeTime()) ? simpleDateFormat.parse(getExchangeTime()) : null);
            htcEntity.setExchangeProvinceID(getExchangeProvinceID());
            htcEntity.setExchangeDistrictID(getExchangeDistrictID());
            htcEntity.setArrivalSiteID(Long.valueOf(getArrivalSiteID() == null || getArrivalSiteID().equals("") ? "-1" : getArrivalSiteID()));
            htcEntity.setArrivalSite(getArrivalSite());
            htcEntity.setRegisterTherapyTime(StringUtils.isNotEmpty(getRegisterTherapyTime()) ? simpleDateFormat.parse(getRegisterTherapyTime()) : null);
            htcEntity.setTherapyRegistProvinceID(getTherapyRegistProvinceID());
            htcEntity.setTherapyRegistDistrictID(getTherapyRegistDistrictID());
            htcEntity.setRegisteredTherapySite(getRegisteredTherapySite());
            htcEntity.setTherapyNo(getTherapyNo());
            htcEntity.setStaffBeforeTestID(TextUtils.toFullname(getStaffBeforeTestID()));
            htcEntity.setStaffAfterID(TextUtils.toFullname(getStaffAfterID()));
            
            if (StringUtils.isNotEmpty(getTestResultsID()) && getTestResultsID().equals("1")) {
                htcEntity.setExchangeService(getExchangeService());
                htcEntity.setExchangeServiceName(getExchangeServiceName());
            } else if (StringUtils.isNotEmpty(getTestResultsID())) {
                htcEntity.setExchangeService(getExchangeServiceConfirm());
                htcEntity.setExchangeServiceName(getExchangeServiceNameConfirm());
            }
            
            if (StringUtils.isEmpty(htcEntity.getExchangeService()) || (StringUtils.isNotEmpty(htcEntity.getExchangeService()) && !htcEntity.getExchangeService().equals("1"))) {
                htcEntity.setTherapyExchangeStatus("0");
            }
            
            return htcEntity;
        }
        
        if (htcEntity == null) {
            htcEntity = new HtcVisitEntity();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
        htcEntity.setID(getID());
        htcEntity.setSiteID(siteEntity.getID());
        htcEntity.setAdvisoryeTime(StringUtils.isNotEmpty(getAdvisoryeTime()) ? simpleDateFormat.parse(getAdvisoryeTime()) : null);
        htcEntity.setServiceID(getServiceID());

        
        htcEntity.setCode(getCode().toUpperCase());
        htcEntity.setPatientName(getPatientName());
        htcEntity.setPatientPhone(getPatientPhone());
        htcEntity.setPatientID(getPatientID());
        htcEntity.setRaceID(getRaceID());
        htcEntity.setGenderID(getGenderID());
        htcEntity.setYearOfBirth(getYearOfBirth());
        htcEntity.setHasHealthInsurance(getHasHealthInsurance());
        htcEntity.setHealthInsuranceNo(StringUtils.isEmpty(getHasHealthInsurance()) || NO_HEALTH_INSURANCE.equals(getHasHealthInsurance()) ? null : getHealthInsuranceNo());
        htcEntity.setPermanentAddress(getPermanentAddress());
        htcEntity.setPermanentAddressGroup(getPermanentAddressGroup());
        htcEntity.setPermanentAddressStreet(getPermanentAddressStreet());
        htcEntity.setPermanentProvinceID(getPermanentProvinceID());
        htcEntity.setPermanentDistrictID(getPermanentDistrictID());
        htcEntity.setPermanentWardID(getPermanentWardID());
        if (getIsDisplayCopy() == null || getIsDisplayCopy()) {
            htcEntity.setCurrentAddress(htcEntity.getPermanentAddress());
            htcEntity.setCurrentAddressGroup(htcEntity.getPermanentAddressGroup());
            htcEntity.setCurrentAddressStreet(htcEntity.getPermanentAddressStreet());
            htcEntity.setCurrentProvinceID(htcEntity.getPermanentProvinceID());
            htcEntity.setCurrentDistrictID(htcEntity.getPermanentDistrictID());
            htcEntity.setCurrentWardID(getPermanentWardID());
        } else {
            htcEntity.setCurrentAddress(getCurrentAddress());
            htcEntity.setCurrentAddressGroup(getCurrentAddressGroup());
            htcEntity.setCurrentAddressStreet(getCurrentAddressStreet());
            htcEntity.setCurrentProvinceID(getCurrentProvinceID());
            htcEntity.setCurrentDistrictID(getCurrentDistrictID());
            htcEntity.setCurrentWardID(getCurrentWardID());
        }

        htcEntity.setJobID(getJobID());
        htcEntity.setObjectGroupID(getObjectGroupID());
        htcEntity.setModeOfTransmission(getModeOfTransmission());
        htcEntity.setBioName(getBioName());

        if (getRiskBehaviorID() != null) {
            htcEntity.setRiskBehaviorID(getRiskBehaviorID());
        } else {
            htcEntity.setRiskBehaviorID(null);
        }
        htcEntity.setModeOfTransmission(getModeOfTransmission());
        htcEntity.setReferralSource(getReferralSource());
        htcEntity.setApproacherNo(getApproacherNo());
        htcEntity.setYouInjectCode(getYouInjectCode());
        htcEntity.setIsAgreePreTest(getIsAgreePreTest());

        if (StringUtils.isNotEmpty(htcEntity.getIsAgreePreTest())) {
            htcEntity.setPreTestTime(StringUtils.isNotEmpty(getPreTestTime()) ? simpleDateFormat.parse(getPreTestTime()) : null);
        }

        htcEntity.setTestResultsID(getTestResultsID());

        if (StringUtils.isNotEmpty(getResultsTime())) {
            htcEntity.setResultsTime(simpleDateFormat.parse(getResultsTime()));
        } else {
            htcEntity.setResultsTime(null);
        }

        if (StringUtils.isNotEmpty(getIsAgreeTest())) {
            htcEntity.setIsAgreeTest(getIsAgreeTest().contains("true"));
        }

        if (StringUtils.isNotEmpty(getPatientIDAuthen())) {
            htcEntity.setPatientIDAuthen(getPatientIDAuthen());
        }

        // Có phản ứng và đồng ý xét nghiệm
        if (htcEntity.getTestResultsID() != null && (REACTIVE_TEST_RESULT.equals(htcEntity.getTestResultsID()) || UN_SPECIFIED_RESULT.equals(htcEntity.getTestResultsID()) || "4".equals(htcEntity.getTestResultsID()))
                && htcEntity.getIsAgreeTest() == true) {

            // Xử lý mã do tên cơ sở xn cố định cấp B2.2
            if (getIsAgreeTest().contains("true")) {
                htcEntity.setTestNoFixSite(StringUtils.isNotEmpty(getTestNoFixSite()) ? getTestNoFixSite().toUpperCase() : getCode().toUpperCase());
            }

            htcEntity.setSiteConfirmTest(getSiteConfirmTest());

            // Xử lý mã xn khẳng định do cơ sở TVXN cố định cấp C2
            if (StringUtils.isNotEmpty(getConfirmTestNo())) {
                htcEntity.setConfirmTestNo(getConfirmTestNo().toUpperCase());
            } else {
                htcEntity.setConfirmTestNo(htcEntity.getTestNoFixSite().toUpperCase());
            }

            htcEntity.setModeOfTransmission(getModeOfTransmission());

            htcEntity.setConfirmTime(StringUtils.isEmpty(getConfirmTime()) ? null : simpleDateFormat.parse(getConfirmTime()));
            htcEntity.setResultsSiteTime(StringUtils.isEmpty(getResultsSiteTime()) ? null : simpleDateFormat.parse(getResultsSiteTime()));
            htcEntity.setResultsPatienTime(StringUtils.isEmpty(getResultsPatienTime()) ? null : simpleDateFormat.parse(getResultsPatienTime()));

            htcEntity.setExchangeConsultTime(StringUtils.isNotEmpty(getExchangeConsultTime()) ? simpleDateFormat.parse(getExchangeConsultTime()) : null);

            htcEntity.setPartnerProvideResult(getPartnerProvideResult());
            htcEntity.setArvExchangeResult(getArvExchangeResult());

            // Set giá trị cho phần chuyển gửi điều trị ARV
            htcEntity.setExchangeProvinceID(getExchangeProvinceID());
            htcEntity.setExchangeDistrictID(getExchangeDistrictID());
            htcEntity.setArrivalSite(getArrivalSite());
            htcEntity.setArrivalSiteID(Long.valueOf(getArrivalSiteID() == null || getArrivalSiteID().equals("") ? "-1" : getArrivalSiteID()));
            htcEntity.setExchangeTime(StringUtils.isNotEmpty(getExchangeTime()) ? simpleDateFormat.parse(getExchangeTime()) : null);
            htcEntity.setRegisteredTherapySite(getRegisteredTherapySite());
            htcEntity.setTherapyRegistProvinceID(getTherapyRegistProvinceID());
            htcEntity.setTherapyRegistDistrictID(getTherapyRegistDistrictID());
            htcEntity.setRegisterTherapyTime(StringUtils.isNotEmpty(getRegisterTherapyTime()) ? simpleDateFormat.parse(getRegisterTherapyTime()) : null);
            htcEntity.setTherapyNo(getTherapyNo());

        }
        htcEntity.setIsRemove(false);
        htcEntity.setStaffBeforeTestID(TextUtils.toFullname(getStaffBeforeTestID()));
        htcEntity.setStaffAfterID(TextUtils.toFullname(getStaffAfterID()));

        if (getID() == null && StringUtils.isNotEmpty(getSiteConfirmTest())
                && String.valueOf(DecisionAnswerEnum.TRUE.getKey()).equals(getIsAgreeTest())) {
            htcEntity.setSampleSentDate(new Date());
        } else {
            htcEntity.setSampleSentDate(StringUtils.isNotEmpty(getSampleSentDate()) ? simpleDateFormat.parse(getSampleSentDate()) : null);
        }

        htcEntity.setConfirmResultsID(this.getConfirmResultsID());

        // Set trạng thái chuyển gửi điều trị
        if ("1".equals(this.getArvExchangeResult())
                && StringUtils.isNotBlank(this.getArrivalSite())
                && StringUtils.isNotBlank(this.getExchangeTime())) {
            htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_CHUYEN.getKey()); // Set trạng thái sang đã chuyển
        }

        if (StringUtils.isNotBlank(this.getRegisterTherapyTime())
                && StringUtils.isNotBlank(this.getRegisteredTherapySite())
                && StringUtils.isNotBlank(this.getTherapyNo())) {
            htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey()); // Trạng thái chuyển gửi thành công
        }

        //12/05/2020
        if (htcEntity.getFeedbackReceiveTime() == null && htcEntity.getArrivalTime() != null) {
            htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_TIEP_NHAN.getKey());
        }

        //  Set giá trị C4.1 -> C4.4
        htcEntity.setEarlyHiv(getEarlyHiv());
        htcEntity.setEarlyHivDate(StringUtils.isNotEmpty(getEarlyHivDate()) ? simpleDateFormat.parse(getEarlyHivDate()) : null);
        htcEntity.setVirusLoad(getVirusLoad());
        htcEntity.setVirusLoadDate(StringUtils.isNotEmpty(getVirusLoadDate()) ? simpleDateFormat.parse(getVirusLoadDate()) : null);
        
        if ((StringUtils.isNotEmpty(getConfirmType()) && getConfirmType().equals(ConfirmTypeEnum.PCR_HIV.getKey()) && StringUtils.isNotEmpty(getConfirmResultsID()) && StringUtils.isNotEmpty(getConfirmResultsID()))
                || (StringUtils.isNotEmpty(getConfirmType()) && getConfirmType().equals(ConfirmTypeEnum.SEROUS.getKey()) && StringUtils.isNotEmpty(getResultPcrHiv()))) {
            if (StringUtils.isEmpty(htcEntity.getVirusLoad()) || htcEntity.getVirusLoadDate() == null) {
                htcEntity.setVirusLoad(getVirusLoad());
                htcEntity.setVirusLoadDate(StringUtils.isNotEmpty(getVirusLoadDate()) ? simpleDateFormat.parse(getVirusLoadDate()) : null);
            }
        }
        
        // Cập nhật trường trạng thái xét nghiệm khẳng định
        htcEntity.setConfirmTestStatus(getConfirmTestStatus());

        if (StringUtils.isNotEmpty(htcEntity.getConfirmTestNo()) && StringUtils.isNotEmpty(htcEntity.getConfirmResultsID())
                && htcEntity.getConfirmTime() != null) {
            htcEntity.setConfirmTestStatus(HtcConfirmStatusEnum.DONE.getKey());

            if (ConfirmTestResultEnum.DUONG_TINH.equals(htcEntity.getConfirmResultsID())) {
                htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUA_CHUYEN.getKey());
            }
        }

        if (ArvExchangeEnum.DONG_Y.getKey().equals(htcEntity.getArvExchangeResult())
                && htcEntity.getExchangeTime() != null && StringUtils.isNotEmpty(htcEntity.getArrivalSite())) {
            htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_CHUYEN.getKey());
        }

        if (htcEntity.getRegisterTherapyTime() != null && StringUtils.isNotEmpty(htcEntity.getRegisteredTherapySite())
                && StringUtils.isNotEmpty(htcEntity.getTherapyNo())) {
            htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey());
        }

        //12/05/2020
        if (htcEntity.getFeedbackReceiveTime() == null && htcEntity.getArrivalTime() != null) {
            htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_TIEP_NHAN.getKey());
        }

        htcEntity.setPepfarProjectID(getPepfarProjectID());

        // Sửa theo mẫu SHIFT  20/0/2020
        if (!getServiceID().equals(ServiceTestEnum.KC.getKey()) && !getServiceID().equals(ServiceTestEnum.CD.getKey())) {
            htcEntity.setCdService(null);
        }
        
        if (getServiceID().equals(ServiceTestEnum.CD.getKey())) {
            htcEntity.setCdService(getCdService());
        }
        
        if (getServiceID().equals(ServiceTestEnum.KC.getKey()) && StringUtils.isNotEmpty(getApproacherNo() )) {
            if (StringUtils.isEmpty(htcEntity.getCdService())) {
                htcEntity.setCdService(getCdService());
            }
        }
        
        htcEntity.setTestMethod(getTestMethod());
        htcEntity.setResultAnti(getResultAnti());
        htcEntity.setConfirmType(getConfirmType());
        htcEntity.setPcrHiv(StringUtils.isNotEmpty(getPcrHiv()) ? getPcrHiv().equals("1") : false);
        htcEntity.setResultPcrHiv(getResultPcrHiv());
        
        if ( StringUtils.isNotEmpty(getTestResultsID()) && getTestResultsID().equals("1") ) {
            htcEntity.setExchangeService(getExchangeService());
            htcEntity.setExchangeServiceName(getExchangeServiceName());
        } else if (StringUtils.isNotEmpty(getTestResultsID())) {
            htcEntity.setExchangeService(getExchangeServiceConfirm());
            htcEntity.setExchangeServiceName(getExchangeServiceNameConfirm());
        }
        
        if (StringUtils.isNotEmpty(htcEntity.getServiceID()) && htcEntity.getServiceID().equals(ServiceTestEnum.KC.getKey())) {
            htcEntity.setStaffKC(getStaffKC());
        }
        
        htcEntity.setLaoVariable(getLaoVariable());
        htcEntity.setLaoVariableName(getLaoVariableName());
        htcEntity.setCustomerType(getCustomerType());
        
        htcEntity.setVirusLoadNumber(getVirusLoadNumber());
        htcEntity.setEarlyBioName(getEarlyBioName());
        htcEntity.setEarlyDiagnose(getEarlyDiagnose());
        
        if (StringUtils.isEmpty(htcEntity.getExchangeService()) || (StringUtils.isNotEmpty(htcEntity.getExchangeService()) && !htcEntity.getExchangeService().equals("1"))) {
            htcEntity.setTherapyExchangeStatus("0");
        }
        
        return htcEntity;
    }

    /**
     * Chuyển HtcLaytestEntity sang HtcVisitEntity
     *
     * @auth TrangBN
     * @param laytestEntity
     * @return
     */
    public HtcVisitEntity getEntityFromLaytest(HtcLaytestEntity laytestEntity) {

        HtcVisitEntity htcEntity = new HtcVisitEntity();
        htcEntity.setCode(laytestEntity.getCode());
        htcEntity.setServiceID(laytestEntity.getServiceID());
        htcEntity.setAdvisoryeTime(laytestEntity.getAdvisoryeTime() == null ? null : laytestEntity.getAdvisoryeTime());
        htcEntity.setPatientName(laytestEntity.getPatientName());
        htcEntity.setPatientPhone(laytestEntity.getPatientPhone());
        htcEntity.setYearOfBirth(laytestEntity.getYearOfBirth() != null ? String.valueOf(laytestEntity.getYearOfBirth()) : null);
        htcEntity.setGenderID(laytestEntity.getGenderID());
        htcEntity.setRaceID(laytestEntity.getRaceID());
        htcEntity.setPatientID(laytestEntity.getPatientID());
        htcEntity.setPermanentAddress(laytestEntity.getPermanentAddress());
        htcEntity.setPermanentAddressGroup(laytestEntity.getPermanentAddressGroup());
        htcEntity.setPermanentAddressStreet(laytestEntity.getPermanentAddressStreet());
        htcEntity.setPermanentProvinceID(laytestEntity.getPermanentProvinceID());
        htcEntity.setPermanentDistrictID(laytestEntity.getPermanentDistrictID());
        htcEntity.setPermanentWardID(laytestEntity.getPermanentWardID());
        htcEntity.setCurrentAddress(laytestEntity.getCurrentAddress());
        htcEntity.setCurrentAddressGroup(laytestEntity.getCurrentAddressGroup());
        htcEntity.setCurrentAddressStreet(laytestEntity.getCurrentAddressStreet());
        htcEntity.setCurrentProvinceID(laytestEntity.getCurrentProvinceID());
        htcEntity.setCurrentDistrictID(laytestEntity.getCurrentDistrictID());
        htcEntity.setCurrentWardID(laytestEntity.getCurrentWardID());
        htcEntity.setJobID(laytestEntity.getJobID());
        htcEntity.setObjectGroupID(laytestEntity.getObjectGroupID());
        htcEntity.setRiskBehaviorID(laytestEntity.getRiskBehaviorID());
        htcEntity.setTestResultsID(laytestEntity.getTestResultsID());
        htcEntity.setIsAgreeTest(laytestEntity.getIsAgreeTest());

        return htcEntity;
    }

    /**
     * Set chir thông tin điều trị từ C6 trở đi 
     * 
     * @param form 
     */
    public void setArvInfo(HtcVisitForm form) {
        this.setResultsPatienTime(form.getResultsPatienTime());
        this.setExchangeServiceConfirm(form.getExchangeServiceConfirm());
        this.setExchangeServiceNameConfirm(form.getExchangeServiceNameConfirm());
        this.setExchangeConsultTime(form.getExchangeConsultTime());
        this.setPartnerProvideResult(form.getPartnerProvideResult());
        this.setArvExchangeResult(form.getArvExchangeResult());
        this.setExchangeTime(form.getExchangeTime());
        this.setExchangeProvinceID(form.getExchangeProvinceID());
        this.setExchangeDistrictID(form.getExchangeDistrictID());
        this.setArrivalSiteID(form.getArrivalSiteID());
        this.setArrivalSite(form.getArrivalSite());
        this.setRegisterTherapyTime(form.getRegisterTherapyTime());
        this.setTherapyRegistProvinceID(form.getTherapyRegistProvinceID());
        this.setTherapyRegistDistrictID(form.getTherapyRegistDistrictID());
        this.setRegisteredTherapySite(form.getRegisteredTherapySite());
        this.setTherapyNo(form.getTherapyNo());
        this.setStaffBeforeTestID(form.getStaffBeforeTestID());
        this.setStaffAfterID(form.getStaffAfterID());
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}

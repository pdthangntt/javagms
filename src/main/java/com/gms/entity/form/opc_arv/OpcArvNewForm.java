package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author DSNAnh
 */
public class OpcArvNewForm {

    private boolean isStageTimeError;
    private String dateBefore18mon;
    private Long ID;
    private String htcSiteID;
    private String sourceID;
    private String htcID;
    private String siteAgency;
    private String siteName;
    private String arvID; //Mã bệnh án
    private String currentSiteID; //Mã cơ sở hiện tại
    private String siteID; //Mã cơ sở quản lý
    private String code; //Mã bệnh nhân
    private String fullName; //Họ và tên
    private String genderID; //Giới tính
    private String dob; //Ngày/tháng/năm sinh
    private String raceID; //Dân tộc
    private String identityCard; //Chứng minh thư
    private String jobID; //Nghề nghiệp
    private String objectGroupID; //Nhóm đối tượng
    private Long patientID; //ID bệnh nhân

    private String insurance; //Có thẻ BHYT ?

    private String insuranceNo; //Số thẻ BHYT
    private String insuranceType; //Loại thanh toán thẻ BHYT - Sử dụng enum
    private String insuranceSite; //Nơi đăng ký kcb ban đầu
    private String insuranceExpiry; //Ngày hết hạn thẻ BHYT
    private String insurancePay; //Tỉ lệ thanh toán thẻ BHYT - Sử dụng enum
    private String patientPhone; //Số điện thoại bệnh nhân
    private String permanentAddress; //Địa chỉ thường trú
    private String permanentProvinceID;
    private String permanentDistrictID;
    private String permanentWardID;
    private String currentAddress; //Địa chỉ cư trú
    private String currentProvinceID;
    private String currentDistrictID;
    private String currentWardID;
    private String currentAddressFull;
    private String permanentAddressFull;
    private Boolean isDisplayCopy;
    private String supporterName; //Họ và tên người hỗ trợ
    private String supporterRelation; //Quan hệ với bệnh nhân
    private String supporterPhone; //Số điện thoại người hỗ trợ
    private String confirmCode; // Mã XN khẳng định
    private String confirmTime; //Ngày xét nghiệm khẳng định
    private String confirmSiteID;
    private String confirmSiteName;
    private String pcrOneTime; //Ngày xn pcr lần 01
    private String pcrOneResult; //Kết quả xn pcr lần 01
    private String pcrTwoTime; //Ngày xn pcr lần 02
    private String pcrTwoResult; //Kết quả xn pcr lần 02
    private String therapySiteID; //Cơ sở điều trị
    private String registrationTime; //NGày đăng ký vào OPC
    private String registrationType; //Loại đăng ký - Sử dụng enum
    private String sourceSiteID; //mã cơ sở nguồn gửi đến
    private String sourceSiteName; //Tên cơ sở nguồn gửi đến
    private String sourceCode; //Mã bệnh án từ cơ sở khác chuyển tới
    private String sourceServiceID; //Mã nguồn dịch vụ
    private String clinicalStage; // Giai đoạn lâm sàng
    private String cd4; //CD4 hoặc cd4%
    private String transferSiteName;
    private boolean isOpcManager;
    private String lao; //Có sàng lọc lao

    private String laoTestTime; //Ngày xét nghiệm lao
    private String laoOtherSymptom; // triệu chứng khác
    private List<String> laoSymptoms; //D3. Các biểu hiện nghi lao

    private String inh; //Điều trị dự phòng INH - sử dụng enum

    private String inhFromTime; //Lao từ ngày
    private String inhToTime; //Lao đến ngày
    private List<String> inhEndCauses; //D8. Lý do kết thúc lao - inh

    private String ntch; //Nhiễm trùng cơ hội - ntch - sử dụng enum

    private String ntchOtherSymptom; // triệu chứng khác ntch
    private List<String> ntchSymptoms; //D10. Các biểu hiện ntch
    private String cotrimoxazoleFromTime; //cotrimoxazole từ ngày
    private String cotrimoxazoleToTime; //cotrimoxazole đến ngày
    private List<String> cotrimoxazoleEndCauses; //Lý do kết thúc cotrimoxazole
    private String statusOfTreatmentID; //Trạng thái điều trị
    private String firstTreatmentTime; //Ngày ARV đầu tiên
    private String firstTreatmentRegimenID; //Phác đồ điều trị đầu tiên - liên kết với treatment-regimen
    private String treatmentTime; //Ngày ARV tại cơ sở OPC
    private String treatmentRegimenStage; //Bậc phác đồ điều trị - sử dụng enum
    private String treatmentRegimenID; //Phác đồ điều trị tại cơ sở OPC
    private String lastExaminationTime; //Ngày khám bệnh gần nhất
    private String medicationAdherence; //Tuân thủ điều trị
    private String daysReceived; //Số ngày nhận thuốc
    private String appointmentTime; // Ngày hẹn khám 
    private String examinationNote; //Các vấn đề trong lần khám gần nhất
    private String firstCd4Time; //Ngày xét nghiệm cd4 đầu tiên
    private String firstCd4Result; //Kết quả xét nghiệm cd4
    private List<String> firstCd4Causes; //F3. Lý do xét nghiệm cd4 đầu tiên
    private String cd4Time; //Ngày xét nghiệm cd4
    private String cd4Result; //Kết quả xét nghiệm cd4 gần nhất
    private List<String> cd4Causes; //F6. Lý do xét nghiệm cd4 gần nhất
    private String firstViralLoadTime; // TLVR - Ngày xét nghiệm đầu tiên
    private String firstViralLoadResult; //Kết quả xét nghiệm TLVR
    private List<String> firstViralLoadCauses; //G3. TLVR - lý do xét nghiệm  đầu tiên
    private String viralLoadTime; // TLVR - Ngày xét nghiệm
    private String viralLoadResult; //Kết quả xét nghiệm TLVR
    private List<String> viralLoadCauses; //G6. TLVR - lý do xét nghiệm

    private String hbv; //Có xn hbv hay không - sử dụng enum

    private String hbvTime; // Ngày xét nghiệm hbv
    private String hbvResult; //Kết quả XN HBV

    private String hcv; //Có xn HCV hay không - sử dụng enum

    private String hcvTime; // Ngày xét nghiệm HCV
    private String hcvResult; //Kết quả XN HCV
    private String endTime; // Ngày kết thúc tại OPC
    private String endCase; //Lý do kết thúc điều trị
    private String transferSiteID; //Cơ sở chuyển đi
    private String transferCase; //Lý do chuyển đi
    private String treatmentStageID; //Trạng thái biến động điều trị
    private String treatmentStageTime; //Ngày biến động điều trị
    private String note; //Ghi chú
    private String tranferToTime;
    private String dateOfArrival;
    private String feedbackResultsReceivedTime;
    private String tranferFromTime;
    private String feedbackResultsReceivedTimeOpc;
    private String viralLoadResultNumber;
    private String tranferToTimeOpc;

    private String registrationStatus; //Tình trạng đăng ký
    private String patientWeight; //Câng nặng
    private String patientHeight; //Chiều cao
    private String laoResult; //Kết quả xét nghiệm Lao
    private String laoTreatment; //Có điều trị lọc lao
    private String laoStartTime; //Ngày bắt đầu điều trị lao
    private String laoEndTime; //Ngày kết thúc điều trị lao
    private String qualifiedTreatmentTime; // Ngày đủ tiêu chuẩn điều trị
    private String receivedWard; //Nhận thuốc tại tuyến xã
    private String route;
    private String permanentAddressStreet; // Đường phố thương trú
    private String permanentAddressGroup; // Tổ ấp khu phố thường trú
    private String currentAddressStreet; // Đường phố hiện tại
    private String currentAddressGroup; // Tổ ấp khu phố hiện tại
    private String receivedWardDate; //Nhận thuốc tại tuyến xã

    private boolean isOtherSite;
    private String pageRedirect;
    private String pregnantStartDate;//Ngày bắt đầu thai kỳ
    private String pregnantEndDate;//Ngày kết thúc thai kỳ
    private String joinBornDate;//Ngày dự sinh
    private String suspiciousSymptoms; // Triệu chứng nghi ngờ
    private String examinationAndTest; //Chuyển gửi khám và xét nghiệm
    private String laoTestDate; // Ngày xét nghiệm Lao
    private String laoTestResults; // Kết quả xét nghiệm Lao
    private String laoDiagnose; // Chẩn đoán loại hình Lao
    private String cotrimoxazoleOtherEndCause; //Lý do kết thúc cotrimexazole khác
    private String passTreatment;
    private String quickByTreatmentTime;
    private String quickByConfirmTime;
    private static final String formatType = "dd/MM/yyyy";

    public String getQuickByTreatmentTime() {
        return quickByTreatmentTime;
    }

    public void setQuickByTreatmentTime(String quickByTreatmentTime) {
        this.quickByTreatmentTime = quickByTreatmentTime;
    }

    public String getQuickByConfirmTime() {
        return quickByConfirmTime;
    }

    public void setQuickByConfirmTime(String quickByConfirmTime) {
        this.quickByConfirmTime = quickByConfirmTime;
    }

    public String getPassTreatment() {
        return passTreatment;
    }

    public void setPassTreatment(String passTreatment) {
        this.passTreatment = passTreatment;
    }

    public String getCotrimoxazoleOtherEndCause() {
        return cotrimoxazoleOtherEndCause;
    }

    public void setCotrimoxazoleOtherEndCause(String cotrimoxazoleOtherEndCause) {
        this.cotrimoxazoleOtherEndCause = cotrimoxazoleOtherEndCause;
    }

    public String getSuspiciousSymptoms() {
        return suspiciousSymptoms;
    }

    public void setSuspiciousSymptoms(String suspiciousSymptoms) {
        this.suspiciousSymptoms = suspiciousSymptoms;
    }

    public String getExaminationAndTest() {
        return examinationAndTest;
    }

    public void setExaminationAndTest(String examinationAndTest) {
        this.examinationAndTest = examinationAndTest;
    }

    public String getLaoTestDate() {
        return laoTestDate;
    }

    public void setLaoTestDate(String laoTestDate) {
        this.laoTestDate = laoTestDate;
    }

    public String getLaoTestResults() {
        return laoTestResults;
    }

    public void setLaoTestResults(String laoTestResults) {
        this.laoTestResults = laoTestResults;
    }

    public String getLaoDiagnose() {
        return laoDiagnose;
    }

    public void setLaoDiagnose(String laoDiagnose) {
        this.laoDiagnose = laoDiagnose;
    }

    public String getPregnantStartDate() {
        return pregnantStartDate;
    }

    public void setPregnantStartDate(String pregnantStartDate) {
        this.pregnantStartDate = pregnantStartDate;
    }

    public String getPregnantEndDate() {
        return pregnantEndDate;
    }

    public void setPregnantEndDate(String pregnantEndDate) {
        this.pregnantEndDate = pregnantEndDate;
    }

    public String getJoinBornDate() {
        return joinBornDate;
    }

    public void setJoinBornDate(String joinBornDate) {
        this.joinBornDate = joinBornDate;
    }

    public boolean isIsStageTimeError() {
        return isStageTimeError;
    }

    public void setIsStageTimeError(boolean isStageTimeError) {
        this.isStageTimeError = isStageTimeError;
    }

    public String getDateBefore18mon() {
        return dateBefore18mon;
    }

    public void setDateBefore18mon(String dateBefore18mon) {
        this.dateBefore18mon = dateBefore18mon;
    }

    public String getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(String receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }

    public String getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(String patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getLaoResult() {
        return laoResult;
    }

    public void setLaoResult(String laoResult) {
        this.laoResult = laoResult;
    }

    public String getLaoTreatment() {
        return laoTreatment;
    }

    public void setLaoTreatment(String laoTreatment) {
        this.laoTreatment = laoTreatment;
    }

    public String getLaoStartTime() {
        return laoStartTime;
    }

    public void setLaoStartTime(String laoStartTime) {
        this.laoStartTime = laoStartTime;
    }

    public String getLaoEndTime() {
        return laoEndTime;
    }

    public void setLaoEndTime(String laoEndTime) {
        this.laoEndTime = laoEndTime;
    }

    public String getQualifiedTreatmentTime() {
        return qualifiedTreatmentTime;
    }

    public void setQualifiedTreatmentTime(String qualifiedTreatmentTime) {
        this.qualifiedTreatmentTime = qualifiedTreatmentTime;
    }

    public String getReceivedWard() {
        return receivedWard;
    }

    public void setReceivedWard(String receivedWard) {
        this.receivedWard = receivedWard;
    }

    public String getTranferToTimeOpc() {
        return tranferToTimeOpc;
    }

    public void setTranferToTimeOpc(String tranferToTimeOpc) {
        this.tranferToTimeOpc = tranferToTimeOpc;
    }

    public String getViralLoadResultNumber() {
        return viralLoadResultNumber;
    }

    public void setViralLoadResultNumber(String viralLoadResultNumber) {
        this.viralLoadResultNumber = viralLoadResultNumber;
    }

    public String getTranferToTime() {
        return tranferToTime;
    }

    public void setTranferToTime(String tranferToTime) {
        this.tranferToTime = tranferToTime;
    }

    public String getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(String dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public String getFeedbackResultsReceivedTime() {
        return feedbackResultsReceivedTime;
    }

    public void setFeedbackResultsReceivedTime(String feedbackResultsReceivedTime) {
        this.feedbackResultsReceivedTime = feedbackResultsReceivedTime;
    }

    public String getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(String tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

    public String getFeedbackResultsReceivedTimeOpc() {
        return feedbackResultsReceivedTimeOpc;
    }

    public void setFeedbackResultsReceivedTimeOpc(String feedbackResultsReceivedTimeOpc) {
        this.feedbackResultsReceivedTimeOpc = feedbackResultsReceivedTimeOpc;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getHtcID() {
        return htcID;
    }

    public void setHtcID(String htcID) {
        this.htcID = htcID;
    }

    public String getHtcSiteID() {
        return htcSiteID;
    }

    public void setHtcSiteID(String htcSiteID) {
        this.htcSiteID = htcSiteID;
    }

    public Boolean getIsDisplayCopy() {
        return isDisplayCopy;
    }

    public void setIsDisplayCopy(Boolean isDisplayCopy) {
        this.isDisplayCopy = isDisplayCopy;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public boolean isIsOpcManager() {
        return isOpcManager;
    }

    public void setIsOpcManager(boolean isOpcManager) {
        this.isOpcManager = isOpcManager;
    }

    public String getCurrentAddressFull() {
        return currentAddressFull;
    }

    public void setCurrentAddressFull(String currentAddressFull) {
        this.currentAddressFull = currentAddressFull;
    }

    public String getPermanentAddressFull() {
        return permanentAddressFull;
    }

    public void setPermanentAddressFull(String permanentAddressFull) {
        this.permanentAddressFull = permanentAddressFull;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPageRedirect() {
        return StringUtils.isNotEmpty(pageRedirect) ? pageRedirect.trim() : StringUtils.EMPTY;
    }

    public void setPageRedirect(String pageRedirect) {
        this.pageRedirect = pageRedirect;
    }

    public String getCurrentSiteID() {
        return currentSiteID;
    }

    public void setCurrentSiteID(String currentSiteID) {
        this.currentSiteID = currentSiteID;
    }

    public boolean isIsOtherSite() {
        return isOtherSite;
    }

    public void setIsOtherSite(boolean isOtherSite) {
        this.isOtherSite = isOtherSite;
    }

    public String getTransferSiteName() {
        return transferSiteName;
    }

    public void setTransferSiteName(String transferSiteName) {
        this.transferSiteName = transferSiteName;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getArvID() {
        return arvID;
    }

    public void setArvID(String arvID) {
        this.arvID = arvID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceSite() {
        return insuranceSite;
    }

    public void setInsuranceSite(String insuranceSite) {
        this.insuranceSite = insuranceSite;
    }

    public String getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(String insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public String getInsurancePay() {
        return insurancePay;
    }

    public void setInsurancePay(String insurancePay) {
        this.insurancePay = insurancePay;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getPermanentDistrictID() {
        return permanentDistrictID;
    }

    public void setPermanentDistrictID(String permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public String getPermanentWardID() {
        return permanentWardID;
    }

    public void setPermanentWardID(String permanentWardID) {
        this.permanentWardID = permanentWardID;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getCurrentDistrictID() {
        return currentDistrictID;
    }

    public void setCurrentDistrictID(String currentDistrictID) {
        this.currentDistrictID = currentDistrictID;
    }

    public String getCurrentWardID() {
        return currentWardID;
    }

    public void setCurrentWardID(String currentWardID) {
        this.currentWardID = currentWardID;
    }

    public String getSupporterName() {
        return supporterName;
    }

    public void setSupporterName(String supporterName) {
        this.supporterName = supporterName;
    }

    public String getSupporterRelation() {
        return supporterRelation;
    }

    public void setSupporterRelation(String supporterRelation) {
        this.supporterRelation = supporterRelation;
    }

    public String getSupporterPhone() {
        return supporterPhone;
    }

    public void setSupporterPhone(String supporterPhone) {
        this.supporterPhone = supporterPhone;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmSiteID() {
        return confirmSiteID;
    }

    public void setConfirmSiteID(String confirmSiteID) {
        this.confirmSiteID = confirmSiteID;
    }

    public String getConfirmSiteName() {
        return confirmSiteName;
    }

    public void setConfirmSiteName(String confirmSiteName) {
        this.confirmSiteName = confirmSiteName;
    }

    public String getPcrOneTime() {
        return pcrOneTime;
    }

    public void setPcrOneTime(String pcrOneTime) {
        this.pcrOneTime = pcrOneTime;
    }

    public String getPcrOneResult() {
        return pcrOneResult;
    }

    public void setPcrOneResult(String pcrOneResult) {
        this.pcrOneResult = pcrOneResult;
    }

    public String getPcrTwoTime() {
        return pcrTwoTime;
    }

    public void setPcrTwoTime(String pcrTwoTime) {
        this.pcrTwoTime = pcrTwoTime;
    }

    public String getPcrTwoResult() {
        return pcrTwoResult;
    }

    public void setPcrTwoResult(String pcrTwoResult) {
        this.pcrTwoResult = pcrTwoResult;
    }

    public String getTherapySiteID() {
        return therapySiteID;
    }

    public void setTherapySiteID(String therapySiteID) {
        this.therapySiteID = therapySiteID;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getSourceSiteName() {
        return sourceSiteName;
    }

    public void setSourceSiteName(String sourceSiteName) {
        this.sourceSiteName = sourceSiteName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getClinicalStage() {
        return clinicalStage;
    }

    public void setClinicalStage(String clinicalStage) {
        this.clinicalStage = clinicalStage;
    }

    public String getCd4() {
        return cd4;
    }

    public void setCd4(String cd4) {
        this.cd4 = cd4;
    }

    public String getLaoTestTime() {
        return laoTestTime;
    }

    public void setLaoTestTime(String laoTestTime) {
        this.laoTestTime = laoTestTime;
    }

    public String getLaoOtherSymptom() {
        return laoOtherSymptom;
    }

    public void setLaoOtherSymptom(String laoOtherSymptom) {
        this.laoOtherSymptom = laoOtherSymptom;
    }

    public List<String> getLaoSymptoms() {
        return laoSymptoms;
    }

    public void setLaoSymptoms(List<String> laoSymptoms) {
        this.laoSymptoms = laoSymptoms;
    }

    public String getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(String inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

    public String getInhToTime() {
        return inhToTime;
    }

    public void setInhToTime(String inhToTime) {
        this.inhToTime = inhToTime;
    }

    public List<String> getInhEndCauses() {
        return inhEndCauses;
    }

    public void setInhEndCauses(List<String> inhEndCauses) {
        this.inhEndCauses = inhEndCauses;
    }

    public String getNtchOtherSymptom() {
        return ntchOtherSymptom;
    }

    public void setNtchOtherSymptom(String ntchOtherSymptom) {
        this.ntchOtherSymptom = ntchOtherSymptom;
    }

    public List<String> getNtchSymptoms() {
        return ntchSymptoms;
    }

    public void setNtchSymptoms(List<String> ntchSymptoms) {
        this.ntchSymptoms = ntchSymptoms;
    }

    public String getCotrimoxazoleFromTime() {
        return cotrimoxazoleFromTime;
    }

    public void setCotrimoxazoleFromTime(String cotrimoxazoleFromTime) {
        this.cotrimoxazoleFromTime = cotrimoxazoleFromTime;
    }

    public String getCotrimoxazoleToTime() {
        return cotrimoxazoleToTime;
    }

    public void setCotrimoxazoleToTime(String cotrimoxazoleToTime) {
        this.cotrimoxazoleToTime = cotrimoxazoleToTime;
    }

    public List<String> getCotrimoxazoleEndCauses() {
        return cotrimoxazoleEndCauses;
    }

    public void setCotrimoxazoleEndCauses(List<String> cotrimoxazoleEndCauses) {
        this.cotrimoxazoleEndCauses = cotrimoxazoleEndCauses;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getFirstTreatmentTime() {
        return firstTreatmentTime;
    }

    public void setFirstTreatmentTime(String firstTreatmentTime) {
        this.firstTreatmentTime = firstTreatmentTime;
    }

    public String getFirstTreatmentRegimenID() {
        return firstTreatmentRegimenID;
    }

    public void setFirstTreatmentRegimenID(String firstTreatmentRegimenID) {
        this.firstTreatmentRegimenID = firstTreatmentRegimenID;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getTreatmentRegimenStage() {
        return treatmentRegimenStage;
    }

    public void setTreatmentRegimenStage(String treatmentRegimenStage) {
        this.treatmentRegimenStage = treatmentRegimenStage;
    }

    public String getTreatmentRegimenID() {
        return treatmentRegimenID;
    }

    public void setTreatmentRegimenID(String treatmentRegimenID) {
        this.treatmentRegimenID = treatmentRegimenID;
    }

    public String getLastExaminationTime() {
        return lastExaminationTime;
    }

    public void setLastExaminationTime(String lastExaminationTime) {
        this.lastExaminationTime = lastExaminationTime;
    }

    public String getMedicationAdherence() {
        return medicationAdherence;
    }

    public void setMedicationAdherence(String medicationAdherence) {
        this.medicationAdherence = medicationAdherence;
    }

    public String getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(String daysReceived) {
        this.daysReceived = daysReceived;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getExaminationNote() {
        return examinationNote;
    }

    public void setExaminationNote(String examinationNote) {
        this.examinationNote = examinationNote;
    }

    public String getFirstCd4Time() {
        return firstCd4Time;
    }

    public void setFirstCd4Time(String firstCd4Time) {
        this.firstCd4Time = firstCd4Time;
    }

    public String getFirstCd4Result() {
        return firstCd4Result;
    }

    public void setFirstCd4Result(String firstCd4Result) {
        this.firstCd4Result = firstCd4Result;
    }

    public List<String> getFirstCd4Causes() {
        return firstCd4Causes;
    }

    public void setFirstCd4Causes(List<String> firstCd4Causes) {
        this.firstCd4Causes = firstCd4Causes;
    }

    public String getCd4Time() {
        return cd4Time;
    }

    public void setCd4Time(String cd4Time) {
        this.cd4Time = cd4Time;
    }

    public String getCd4Result() {
        return cd4Result;
    }

    public void setCd4Result(String cd4Result) {
        this.cd4Result = cd4Result;
    }

    public List<String> getCd4Causes() {
        return cd4Causes;
    }

    public void setCd4Causes(List<String> cd4Causes) {
        this.cd4Causes = cd4Causes;
    }

    public String getFirstViralLoadTime() {
        return firstViralLoadTime;
    }

    public void setFirstViralLoadTime(String firstViralLoadTime) {
        this.firstViralLoadTime = firstViralLoadTime;
    }

    public String getFirstViralLoadResult() {
        return firstViralLoadResult;
    }

    public void setFirstViralLoadResult(String firstViralLoadResult) {
        this.firstViralLoadResult = firstViralLoadResult;
    }

    public List<String> getFirstViralLoadCauses() {
        return firstViralLoadCauses;
    }

    public void setFirstViralLoadCauses(List<String> firstViralLoadCauses) {
        this.firstViralLoadCauses = firstViralLoadCauses;
    }

    public String getViralLoadTime() {
        return viralLoadTime;
    }

    public void setViralLoadTime(String viralLoadTime) {
        this.viralLoadTime = viralLoadTime;
    }

    public String getViralLoadResult() {
        return viralLoadResult;
    }

    public void setViralLoadResult(String viralLoadResult) {
        this.viralLoadResult = viralLoadResult;
    }

    public List<String> getViralLoadCauses() {
        return viralLoadCauses;
    }

    public void setViralLoadCauses(List<String> viralLoadCauses) {
        this.viralLoadCauses = viralLoadCauses;
    }

    public String getHbvTime() {
        return hbvTime;
    }

    public void setHbvTime(String hbvTime) {
        this.hbvTime = hbvTime;
    }

    public String getHbvResult() {
        return hbvResult;
    }

    public void setHbvResult(String hbvResult) {
        this.hbvResult = hbvResult;
    }

    public String getHcvTime() {
        return hcvTime;
    }

    public void setHcvTime(String hcvTime) {
        this.hcvTime = hcvTime;
    }

    public String getHcvResult() {
        return hcvResult;
    }

    public void setHcvResult(String hcvResult) {
        this.hcvResult = hcvResult;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public String getTransferSiteID() {
        return transferSiteID;
    }

    public void setTransferSiteID(String transferSiteID) {
        this.transferSiteID = transferSiteID;
    }

    public String getTransferCase() {
        return transferCase;
    }

    public void setTransferCase(String transferCase) {
        this.transferCase = transferCase;
    }

    public String getTreatmentStageID() {
        return treatmentStageID;
    }

    public void setTreatmentStageID(String treatmentStageID) {
        this.treatmentStageID = treatmentStageID;
    }

    public String getTreatmentStageTime() {
        return treatmentStageTime;
    }

    public void setTreatmentStageTime(String treatmentStageTime) {
        this.treatmentStageTime = treatmentStageTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getLao() {
        return lao;
    }

    public void setLao(String lao) {
        this.lao = lao;
    }

    public String getInh() {
        return inh;
    }

    public void setInh(String inh) {
        this.inh = inh;
    }

    public String getNtch() {
        return ntch;
    }

    public void setNtch(String ntch) {
        this.ntch = ntch;
    }

    public String getHbv() {
        return hbv;
    }

    public void setHbv(String hbv) {
        this.hbv = hbv;
    }

    public String getHcv() {
        return hcv;
    }

    public void setHcv(String hcv) {
        this.hcv = hcv;
    }

    public OpcArvNewForm() {
    }

    public OpcArvNewForm(HtcVisitEntity entity) {
        this.htcID = entity.getID().toString();
        this.sourceServiceID = ServiceEnum.HTC.getKey();
        this.sourceID = entity.getID().toString();
        this.htcSiteID = entity.getSiteID().toString();
        this.fullName = entity.getPatientName();
        this.genderID = entity.getGenderID();
        this.dob = "01/01/" + entity.getYearOfBirth();
        this.identityCard = entity.getPatientID();
        this.raceID = entity.getRaceID();
        this.jobID = entity.getJobID();
        this.objectGroupID = entity.getObjectGroupID();
        this.insurance = entity.getHasHealthInsurance() == null || entity.getHealthInsuranceNo() == null ? "0" : entity.getHasHealthInsurance();
        this.insuranceNo = entity.getHealthInsuranceNo();
        this.patientPhone = entity.getPatientPhone();
        this.permanentAddress = entity.getPermanentAddress();
        this.permanentProvinceID = entity.getPermanentProvinceID();
        this.permanentDistrictID = entity.getPermanentDistrictID();
        this.permanentWardID = entity.getPermanentWardID();
        this.currentAddress = entity.getCurrentAddress();
        this.currentProvinceID = entity.getCurrentProvinceID();
        this.currentDistrictID = entity.getCurrentDistrictID();
        this.currentWardID = entity.getCurrentWardID();
        this.confirmCode = entity.getConfirmTestNo();
        this.confirmTime = TextUtils.formatDate(entity.getConfirmTime(), formatType);
        this.confirmSiteID = entity.getSiteConfirmTest();
        this.sourceSiteID = entity.getArrivalSiteID() != null ? entity.getArrivalSiteID().toString() : null;
        this.registrationTime = TextUtils.formatDate(new Date(), formatType);
        this.registrationType = "1";
        this.tranferToTime = TextUtils.formatDate(new Date(), formatType);
        this.dateOfArrival = TextUtils.formatDate(entity.getExchangeTime(), formatType);
        this.firstViralLoadTime = TextUtils.formatDate(entity.getVirusLoadDate(), formatType);
        this.firstViralLoadResult = entity.getVirusLoad();
        this.permanentAddressGroup = entity.getPermanentAddressGroup();
        this.permanentAddressStreet = entity.getPermanentAddressStreet();
        this.currentAddressGroup = entity.getCurrentAddressGroup();
        this.currentAddressStreet = entity.getCurrentAddressStreet();
    }

    public OpcArvNewForm(OpcArvEntity entity) {
        if (entity != null) {
            this.arvID = entity.getID() == null ? "" : entity.getID().toString();
            this.siteID = entity.getSiteID() == null ? "" : entity.getSiteID().toString();
            this.code = entity.getCode();
            this.currentAddressFull = entity.getCurrentAddressFull();
            this.permanentAddressFull = entity.getPermanentAddressFull();
            if (entity.getPatient() != null) {
                this.fullName = entity.getPatient().getFullName();
                this.genderID = entity.getPatient().getGenderID();
                this.dob = TextUtils.formatDate(entity.getPatient().getDob(), formatType);
                this.raceID = entity.getPatient().getRaceID();
                this.identityCard = entity.getPatient().getIdentityCard();
                this.confirmCode = entity.getPatient().getConfirmCode();
                this.confirmTime = TextUtils.formatDate(entity.getPatient().getConfirmTime(), formatType);
                this.confirmSiteID = entity.getPatient().getConfirmSiteID() == null ? "" : entity.getPatient().getConfirmSiteID().toString();
                this.confirmSiteName = entity.getPatient().getConfirmSiteName();
            }
            this.jobID = entity.getJobID();
            this.objectGroupID = entity.getObjectGroupID();
            this.insurance = entity.getInsurance();
            this.insuranceNo = entity.getInsuranceNo();
            this.insuranceType = entity.getInsuranceType();
            this.insuranceSite = entity.getInsuranceSite();
            this.insuranceExpiry = TextUtils.formatDate(entity.getInsuranceExpiry(), formatType);
            this.insurancePay = entity.getInsurancePay();
            this.patientPhone = entity.getPatientPhone();
            this.permanentAddress = entity.getPermanentAddress();
            this.permanentAddressStreet = entity.getPermanentAddressStreet();
            this.permanentAddressGroup = entity.getPermanentAddressGroup();
            this.permanentProvinceID = entity.getPermanentProvinceID();
            this.permanentDistrictID = entity.getPermanentDistrictID();
            this.permanentWardID = entity.getPermanentWardID();
            this.currentAddress = entity.getCurrentAddress();
            this.currentAddressStreet = entity.getCurrentAddressStreet();
            this.currentAddressGroup = entity.getCurrentAddressGroup();
            this.currentProvinceID = entity.getCurrentProvinceID();
            this.currentDistrictID = entity.getCurrentDistrictID();
            this.currentWardID = entity.getCurrentWardID();
            this.supporterName = entity.getSupporterName();
            this.supporterRelation = entity.getSupporterRelation();
            this.supporterPhone = entity.getSupporterPhone();
            this.pcrOneTime = TextUtils.formatDate(entity.getPcrOneTime(), formatType);
            this.pcrOneResult = entity.getPcrOneResult();
            this.pcrTwoTime = TextUtils.formatDate(entity.getPcrTwoTime(), formatType);
            this.pcrTwoResult = entity.getPcrTwoResult();
            this.therapySiteID = entity.getSiteID() == null ? "" : entity.getSiteID().toString();
            this.registrationTime = TextUtils.formatDate(entity.getRegistrationTime(), formatType);
            this.registrationType = entity.getRegistrationType();
            this.sourceSiteID = entity.getSourceSiteID() == null ? "" : entity.getSourceSiteID().toString();
            this.sourceSiteName = entity.getSourceArvSiteName();
            this.sourceCode = entity.getSourceArvCode();
            this.clinicalStage = entity.getClinicalStage();
            this.cd4 = entity.getCd4();
            this.transferSiteName = entity.getTransferSiteName();
            this.lao = entity.isLao() ? "1" : "0";
            this.laoTestTime = TextUtils.formatDate(entity.getLaoTestTime(), formatType);
            this.laoOtherSymptom = entity.getLaoOtherSymptom();
            this.laoSymptoms = entity.getLaoSymptoms();
            this.inh = entity.isInh() ? "1" : "0";
            this.inhFromTime = TextUtils.formatDate(entity.getInhFromTime(), formatType);
            this.inhToTime = TextUtils.formatDate(entity.getInhToTime(), formatType);
            this.inhEndCauses = entity.getInhEndCauses();
            this.ntch = entity.isNtch() ? "1" : "0";
            this.ntchOtherSymptom = entity.getNtchOtherSymptom();
            this.ntchSymptoms = entity.getNtchSymptoms();
            this.cotrimoxazoleFromTime = TextUtils.formatDate(entity.getCotrimoxazoleFromTime(), formatType);
            this.cotrimoxazoleToTime = TextUtils.formatDate(entity.getCotrimoxazoleToTime(), formatType);
            this.cotrimoxazoleEndCauses = entity.getCotrimoxazoleEndCauses();
            this.statusOfTreatmentID = entity.getStatusOfTreatmentID();
            this.firstTreatmentTime = TextUtils.formatDate(entity.getFirstTreatmentTime(), formatType);
            this.firstTreatmentRegimenID = entity.getFirstTreatmentRegimenID();
            this.treatmentTime = TextUtils.formatDate(entity.getTreatmentTime(), formatType);
            this.treatmentRegimenStage = entity.getTreatmentRegimenStage();
            this.treatmentRegimenID = entity.getTreatmentRegimenID();
            this.lastExaminationTime = TextUtils.formatDate(entity.getLastExaminationTime(), formatType);
            this.medicationAdherence = entity.getMedicationAdherence();
            this.daysReceived = entity.getDaysReceived() == 0 ? "" : "" + entity.getDaysReceived();
            this.appointmentTime = TextUtils.formatDate(entity.getAppointmentTime(), formatType);
            this.examinationNote = entity.getExaminationNote();
            this.firstCd4Time = TextUtils.formatDate(entity.getFirstCd4Time(), formatType);
            this.firstCd4Result = entity.getFirstCd4Result();
            this.firstCd4Causes = entity.getFirstCd4Causes();
            this.cd4Time = TextUtils.formatDate(entity.getCd4Time(), formatType);
            this.cd4Result = entity.getCd4Result();
            this.cd4Causes = entity.getCd4Causes();
            this.firstViralLoadTime = TextUtils.formatDate(entity.getFirstViralLoadTime(), formatType);
            this.firstViralLoadResult = entity.getFirstViralLoadResult();
            this.firstViralLoadCauses = entity.getFirstViralLoadCauses();
            this.viralLoadTime = TextUtils.formatDate(entity.getViralLoadTime(), formatType);
            this.viralLoadResult = entity.getViralLoadResult();
            this.viralLoadCauses = entity.getViralLoadCauses();
            this.hbv = entity.isHbv() ? "1" : "0";
            this.hbvTime = TextUtils.formatDate(entity.getHbvTime(), formatType);
            this.hbvResult = entity.getHbvResult();
            this.hcv = entity.isHcv() ? "1" : "0";
            this.hcvTime = TextUtils.formatDate(entity.getHcvTime(), formatType);
            this.hcvResult = entity.getHcvResult();
            this.endTime = TextUtils.formatDate(entity.getEndTime(), formatType);
            this.endCase = entity.getEndCase();
            this.transferSiteID = entity.getTransferSiteID() == null ? "" : entity.getTransferSiteID().toString();
            this.transferCase = entity.getTransferCase();
            this.treatmentStageID = entity.getTreatmentStageID();
            this.treatmentStageTime = TextUtils.formatDate(entity.getTreatmentStageTime(), formatType);
            this.note = entity.getNote();
            this.route = entity.getRouteID();
            this.receivedWardDate = TextUtils.formatDate(entity.getReceivedWardDate(), formatType);
//            this.isOtherSite = this.currentSiteID.equals(entity.getSiteID().toString());
            this.pregnantStartDate = TextUtils.formatDate(entity.getPregnantStartDate(), formatType);
            this.pregnantEndDate = TextUtils.formatDate(entity.getPregnantEndDate(), formatType);
            this.joinBornDate = TextUtils.formatDate(entity.getJoinBornDate(), formatType);
            this.suspiciousSymptoms = entity.getSuspiciousSymptoms();
            this.examinationAndTest = entity.isExaminationAndTest() ? "1" : "0";
            this.laoTestDate = TextUtils.formatDate(entity.getLaoTestDate(), formatType);
            this.laoResult = entity.getLaoResult();
            this.laoDiagnose = entity.getLaoDiagnose();
            this.cotrimoxazoleOtherEndCause = entity.getCotrimoxazoleOtherEndCause();
            this.passTreatment = entity.isPassTreatment() ? "1" : "0";
            this.quickByTreatmentTime = entity.getQuickByTreatmentTime();
            this.quickByConfirmTime = entity.getQuickByConfirmTime();
        }
    }

    public OpcArvEntity setToEntity(OpcArvEntity opcArvEntity) {
        OpcArvEntity entity = new OpcArvEntity();
        if (opcArvEntity != null) {
            entity = opcArvEntity;
        }
        entity.setSiteID(isOpcManager ? Long.parseLong(therapySiteID) : Long.parseLong(currentSiteID));
        entity.setCode(code.trim().toUpperCase());

        if (StringUtils.isNotEmpty(htcID)) {
            entity.setSourceID(Long.parseLong(htcID));
        }
        if (opcArvEntity == null) {
            entity.setPatient(new OpcPatientEntity());
        }
        entity.getPatient().setSiteID(isOpcManager ? Long.parseLong(therapySiteID) : Long.parseLong(currentSiteID));
        entity.getPatient().setFullName(TextUtils.toFullname(fullName.trim()));
        entity.getPatient().setGenderID(genderID);
        entity.getPatient().setDob(TextUtils.convertDate(dob, formatType));
        entity.getPatient().setRaceID(raceID);
        entity.getPatient().setIdentityCard(identityCard);
        entity.getPatient().setConfirmCode(confirmCode);
        entity.getPatient().setConfirmTime(TextUtils.convertDate(confirmTime, formatType));
        entity.getPatient().setConfirmSiteID(StringUtils.isEmpty(confirmSiteID) ? 0 : Long.parseLong(confirmSiteID));
        entity.getPatient().setConfirmSiteName(confirmSiteName);

        entity.setJobID(jobID);
        entity.setObjectGroupID(objectGroupID);
        entity.setInsurance(getInsurance());
        entity.setInsuranceNo(StringUtils.isNotEmpty(insuranceNo) ? insuranceNo.trim().toUpperCase() : "");
        entity.setInsuranceType(insuranceType);
        entity.setInsuranceSite(insuranceSite);
        entity.setInsuranceExpiry(TextUtils.convertDate(insuranceExpiry, formatType));
        entity.setInsurancePay(insurancePay);
        entity.setPatientPhone(patientPhone);

        entity.setPermanentAddress(permanentAddress);
        entity.setPermanentAddressStreet(permanentAddressStreet);
        entity.setPermanentAddressGroup(permanentAddressGroup);
        entity.setPermanentProvinceID(permanentProvinceID);
        entity.setPermanentDistrictID(permanentDistrictID);
        entity.setPermanentWardID(permanentWardID);
        entity.setCurrentAddress(currentAddress);
        entity.setCurrentAddressStreet(currentAddressStreet);
        entity.setCurrentAddressGroup(currentAddressGroup);
        entity.setCurrentProvinceID(currentProvinceID);
        entity.setCurrentDistrictID(currentDistrictID);
        entity.setCurrentWardID(currentWardID);
        if (getIsDisplayCopy() == null || getIsDisplayCopy()) {
            entity.setCurrentAddress(entity.getPermanentAddress());
            entity.setCurrentAddressGroup(entity.getPermanentAddressGroup());
            entity.setCurrentAddressStreet(entity.getPermanentAddressStreet());
            entity.setCurrentProvinceID(entity.getPermanentProvinceID());
            entity.setCurrentDistrictID(entity.getPermanentDistrictID());
            entity.setCurrentWardID(entity.getPermanentWardID());
        } else {
            entity.setCurrentAddress(getCurrentAddress());
            entity.setCurrentAddressGroup(getCurrentAddressGroup());
            entity.setCurrentAddressStreet(getCurrentAddressStreet());
            entity.setCurrentProvinceID(getCurrentProvinceID());
            entity.setCurrentDistrictID(getCurrentDistrictID());
            entity.setCurrentWardID(getCurrentWardID());
        }
        entity.setSupporterName(StringUtils.isEmpty(supporterName) ? "" : TextUtils.toFullname(supporterName.trim()));
        entity.setSupporterRelation(supporterRelation);
        entity.setSupporterPhone(supporterPhone);
        entity.setPcrOneTime(TextUtils.convertDate(pcrOneTime, formatType));
        entity.setPcrOneResult(pcrOneResult);
        entity.setPcrTwoTime(TextUtils.convertDate(pcrTwoTime, formatType));
        entity.setPcrTwoResult(pcrTwoResult);
        entity.setRegistrationTime(StringUtils.isNotEmpty(getRegistrationTime()) ? (getRegistrationTime().equals(TextUtils.formatDate(new Date(), formatType)) ? new Date() : TextUtils.convertDate(getRegistrationTime(), formatType)) : null);
        entity.setRegistrationType(registrationType);
        entity.setSourceSiteID(StringUtils.isEmpty(sourceSiteID) ? 0 : Long.parseLong(sourceSiteID));
        if (StringUtils.isNotEmpty(entity.getRegistrationType()) && entity.getSourceSiteID() != null && entity.getSourceSiteID() != 0) {
            entity.setSourceServiceID(RegistrationTypeEnum.NEW.getKey().equals(getRegistrationType()) ? ServiceEnum.HTC.getKey() : ServiceEnum.OPC.getKey());
        }
        entity.setSourceArvSiteName(sourceSiteName);
        entity.setSourceArvCode(sourceCode);
        entity.setClinicalStage(clinicalStage);
        entity.setCd4(cd4);
        entity.setTransferSiteName(transferSiteName);
        entity.setLao("1".equals(lao));
        entity.setLaoTestTime(TextUtils.convertDate(laoTestTime, formatType));
        entity.setLaoOtherSymptom(laoOtherSymptom == null ? "" : laoOtherSymptom);
        entity.setLaoSymptoms(laoSymptoms);
        entity.setInh("1".equals(inh));
        entity.setInhFromTime(TextUtils.convertDate(inhFromTime, formatType));
        entity.setInhToTime(TextUtils.convertDate(inhToTime, formatType));
        entity.setInhEndCauses(inhEndCauses);
        entity.setNtch("1".equals(ntch));
        entity.setNtchOtherSymptom(ntchOtherSymptom);
        entity.setNtchSymptoms(ntchSymptoms);
        entity.setCotrimoxazoleFromTime(TextUtils.convertDate(cotrimoxazoleFromTime, formatType));
        entity.setCotrimoxazoleToTime(TextUtils.convertDate(cotrimoxazoleToTime, formatType));
        entity.setCotrimoxazoleEndCauses(cotrimoxazoleEndCauses);
        entity.setStatusOfTreatmentID(statusOfTreatmentID);
        entity.setObjectGroupID(objectGroupID);

        entity.setFirstTreatmentTime(RegistrationTypeEnum.NEW.getKey().equals(registrationType) ? TextUtils.convertDate(treatmentTime, formatType) : TextUtils.convertDate(firstTreatmentTime, formatType));
        entity.setFirstTreatmentRegimenID(RegistrationTypeEnum.NEW.getKey().equals(registrationType) ? treatmentRegimenID : firstTreatmentRegimenID);
        entity.setTreatmentTime(TextUtils.convertDate(treatmentTime, formatType));
        entity.setTreatmentRegimenStage(treatmentRegimenStage);
        entity.setTreatmentRegimenID(treatmentRegimenID);
        entity.setLastExaminationTime(TextUtils.convertDate(lastExaminationTime, formatType));
        entity.setMedicationAdherence(medicationAdherence);
        entity.setDaysReceived(StringUtils.isEmpty(daysReceived) ? 0 : Integer.parseInt(daysReceived));
        entity.setAppointmentTime(TextUtils.convertDate(appointmentTime, formatType));
        entity.setExaminationNote(examinationNote);
        entity.setFirstCd4Time(TextUtils.convertDate(firstCd4Time, formatType));
        entity.setFirstCd4Result(firstCd4Result);
        entity.setFirstCd4Causes(firstCd4Causes);
        entity.setCd4Time(TextUtils.convertDate(cd4Time, formatType));
        entity.setCd4Result(cd4Result);
        entity.setCd4Causes(cd4Causes);
        entity.setFirstViralLoadTime(TextUtils.convertDate(firstViralLoadTime, formatType));
        entity.setFirstViralLoadResult(firstViralLoadResult);
        entity.setFirstViralLoadCauses(firstViralLoadCauses);
        entity.setViralLoadTime(TextUtils.convertDate(viralLoadTime, formatType));
        entity.setViralLoadResult(viralLoadResult);
        entity.setViralLoadCauses(viralLoadCauses);
        entity.setHbv("1".equals(hbv));
        entity.setHbvTime(TextUtils.convertDate(hbvTime, formatType));
        entity.setHbvResult(hbvResult);
        entity.setHcv("1".equals(hcv));
        entity.setHcvTime(TextUtils.convertDate(hcvTime, formatType));
        entity.setHcvResult(hcvResult);
        entity.setEndTime(TextUtils.convertDate(endTime, formatType));
        entity.setEndCase(endCase);
        entity.setTransferSiteID(StringUtils.isEmpty(transferSiteID) ? 0 : Long.parseLong(transferSiteID));
        entity.setTransferCase(transferCase);
        entity.setTreatmentStageID(StringUtils.isNotEmpty(endCase)
                ? ("1".equals(endCase) ? "6" : "2".equals(endCase) ? "7" : "3".equals(endCase) ? "3" : "4".equals(endCase) ? "5" : "5".equals(endCase) ? "8" : "")
                : ("1".equals(registrationType) || "4".equals(registrationType) || "5".equals(registrationType) ? "1" : "2".equals(registrationType) ? "2" : "3".equals(registrationType) ? "4" : ""));
        entity.setTreatmentStageTime(TextUtils.convertDate(treatmentStageTime, formatType));
        entity.setNote(note);

        entity.setFeedbackResultsReceivedTimeOpc(TextUtils.convertDate(feedbackResultsReceivedTimeOpc, formatType));
        entity.setFeedbackResultsReceivedTime(TextUtils.convertDate(feedbackResultsReceivedTime, formatType));
        entity.setTranferFromTime(TextUtils.convertDate(tranferFromTime, formatType));
        entity.setTranferToTime(TextUtils.convertDate(tranferToTime, formatType));
        entity.setDateOfArrival(TextUtils.convertDate(dateOfArrival, formatType));
        entity.setViralLoadResultNumber(viralLoadResultNumber);
        entity.setTranferToTimeOpc(TextUtils.convertDate(tranferToTimeOpc, formatType));

        if (("not_print".equals(pageRedirect) || "printable".equals(pageRedirect)) && StringUtils.isNotEmpty(sourceSiteID)) {
            entity.setTranferToTime(StringUtils.isEmpty(tranferToTime) ? new Date() : TextUtils.convertDate(tranferToTime, formatType));
            entity.setSourceServiceID("1".equals(registrationType) ? ServiceEnum.HTC.getKey() : ServiceEnum.OPC.getKey());
        }
        if ("printable".equals(pageRedirect) && StringUtils.isNotEmpty(sourceSiteID)) {
            entity.setFeedbackResultsReceivedTime(new Date());
        }

        entity.setReceivedWard(receivedWard);
        entity.setQualifiedTreatmentTime(TextUtils.convertDate(qualifiedTreatmentTime, formatType));
        entity.setLaoEndTime(TextUtils.convertDate(laoEndTime, formatType));
        entity.setLaoStartTime(TextUtils.convertDate(laoStartTime, formatType));
        if (laoTreatment != null) {
            entity.setLaoTreatment(laoTreatment.equals("1"));
        }
        entity.setLaoResult(laoResult);
        if (StringUtils.isNotEmpty(patientHeight)) {
            entity.setPatientHeight((float) Double.parseDouble(patientHeight));
        }
        if (StringUtils.isNotEmpty(patientWeight)) {
            entity.setPatientWeight((float) Double.parseDouble(patientWeight));
        }
        entity.setRegistrationStatus(registrationStatus);
        entity.setRouteID(route);
        if (StringUtils.isNotEmpty(endCase) && endCase.equals(ArvEndCaseEnum.DEAD.getKey())) {
            entity.setDeathTime(TextUtils.convertDate(endTime, formatType));
        }
        entity.setReceivedWardDate(TextUtils.convertDate(receivedWardDate, formatType));
        entity.setPregnantStartDate(TextUtils.convertDate(pregnantStartDate, formatType));
        entity.setPregnantEndDate(TextUtils.convertDate(pregnantEndDate, formatType));
        entity.setJoinBornDate(TextUtils.convertDate(joinBornDate, formatType));
        entity.setSuspiciousSymptoms(suspiciousSymptoms);
        if (examinationAndTest != null) {
            entity.setExaminationAndTest(examinationAndTest.equals("1"));
        }
        entity.setLaoTestDate(TextUtils.convertDate(laoTestDate, formatType));
        entity.setLaoDiagnose(laoDiagnose);
        entity.setCotrimoxazoleOtherEndCause(cotrimoxazoleOtherEndCause);
        if (passTreatment != null) {
            entity.setPassTreatment(passTreatment.equals("1"));
        }
        entity.setQuickByTreatmentTime(quickByTreatmentTime);
        entity.setQuickByConfirmTime(quickByConfirmTime);

        return entity;
    }

    public boolean getCollapseD() {
        return StringUtils.isNotEmpty(getLao()) || StringUtils.isNotEmpty(getInh()) || StringUtils.isNotEmpty(getNtch());
    }

    public boolean getCollapseE() {
        return StringUtils.isNotEmpty(getFirstTreatmentTime()) || StringUtils.isNotEmpty(getFirstTreatmentRegimenID()) || StringUtils.isNotEmpty(getTreatmentTime()) || StringUtils.isNotEmpty(getTreatmentRegimenStage()) || StringUtils.isNotEmpty(getTreatmentRegimenID()) || StringUtils.isNotEmpty(getLastExaminationTime())
                || StringUtils.isNotEmpty(getMedicationAdherence()) || StringUtils.isNotEmpty(getDaysReceived()) || StringUtils.isNotEmpty(getAppointmentTime()) || StringUtils.isNotEmpty(getExaminationNote()) || (StringUtils.isNotEmpty(getStatusOfTreatmentID()));
    }

    public boolean getCollapseF() {
        return StringUtils.isNotEmpty(getFirstCd4Time()) || StringUtils.isNotEmpty(getFirstCd4Result()) || getFirstCd4Causes() != null
                || StringUtils.isNotEmpty(getCd4Time()) || StringUtils.isNotEmpty(getCd4Result()) || getCd4Causes() != null;
    }

    public boolean getCollapseG() {
        return StringUtils.isNotEmpty(getFirstViralLoadTime()) || StringUtils.isNotEmpty(getFirstViralLoadResult()) || getFirstViralLoadCauses() != null
                || StringUtils.isNotEmpty(getViralLoadTime()) || StringUtils.isNotEmpty(getViralLoadResult()) || getViralLoadCauses() != null || StringUtils.isNotEmpty(viralLoadResultNumber);
    }

    public boolean getCollapseH() {
        return StringUtils.isNotEmpty(getHbv()) || StringUtils.isNotEmpty(getHcv());
    }

    public boolean getCollapseI() {
        return StringUtils.isNotEmpty(getEndCase()) || StringUtils.isNotEmpty(getTreatmentStageID()) || StringUtils.isNotEmpty(getTreatmentStageTime()) || isStageTimeError;
    }

}

package com.gms.entity.form;

import com.gms.components.TextUtils;
import com.gms.entity.constant.LaoSymtomEnum;
import com.gms.entity.constant.QuickTreatmentEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.SuspiciousSymptomsEnum;
import com.gms.entity.db.BaseEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author pdThang
 */
public class OpcArvImportForm extends BaseEntity {

    private String siteAgency;
    private String siteName;
    private String arvID; //
    private String currentSiteID; //Mã cơ sở hiện tại
    private String siteID; //Mã cơ sở quản lý
    private String code; //Mã bệnh án
    private String fullName; //Họ và tên
    private String genderID; //Giới tính
    private String dob; //Ngày/tháng/năm sinh
    private String raceID; //Dân tộc
    private String identityCard; //Chứng minh thư
    private String jobID; //Nghề nghiệp
    private String objectGroupID; //Nhóm đối tượng

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
    private String clinicalStage; // Giai đoạn lâm sàng
    private String cd4; //CD4 hoặc cd4%
    private String transferSiteName;
    private boolean isOpcManager;
    private String lao; //Có sàng lọc lao

    private String laoTestTime; //Ngày xét nghiệm lao
    private String laoOtherSymptom; // triệu chứng khác
    private String laoSymptoms; //D3. Các biểu hiện nghi lao

    private String inh; //Điều trị dự phòng INH - sử dụng enum

    private String inhFromTime; //Lao từ ngày
    private String inhToTime; //Lao đến ngày
    private String inhEndCauses; //D8. Lý do kết thúc lao - inh

    private String ntch; //Nhiễm trùng cơ hội - ntch - sử dụng enum

    private String ntchOtherSymptom; // triệu chứng khác ntch
    private String ntchSymptoms; //D10. Các biểu hiện ntch
    private String cotrimoxazoleFromTime; //cotrimoxazole từ ngày
    private String cotrimoxazoleToTime; //cotrimoxazole đến ngày
    private String cotrimoxazoleEndCauses; //Lý do kết thúc cotrimoxazole
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
    private String firstCd4Causes; //F3. Lý do xét nghiệm cd4 đầu tiên
    private String cd4Time; //Ngày xét nghiệm cd4
    private String cd4Result; //Kết quả xét nghiệm cd4 gần nhất
    private String cd4Causes; //F6. Lý do xét nghiệm cd4 gần nhất
    private String firstViralLoadTime; // TLVR - Ngày xét nghiệm đầu tiên
    private String firstViralLoadResult; //Kết quả xét nghiệm TLVR
    private String firstViralLoadCauses; //G3. TLVR - lý do xét nghiệm  đầu tiên
    private String viralLoadTime; // TLVR - Ngày xét nghiệm
    private String viralLoadResult; //Kết quả xét nghiệm TLVR
    private String viralLoadCauses; //G6. TLVR - lý do xét nghiệm

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
    private boolean isOtherSite;
    private String pageRedirect;
    private static final String formatType = "dd/MM/yyyy";
    private String tranferToTime;
    private String dateOfArrival;
    private String feedbackResultsReceivedTime;
    private String registrationStatus;
    private String patientWeight;
    private String patientHeight;
    
    
    private String laoResult; //Kết quả xét nghiệm Lao
    private String laoTreatment; //Có điều trị lọc lao
    private String laoStartTime; //Ngày bắt đầu điều trị lao
    private String laoEndTime; //Ngày kết thúc điều trị lao
    
    private String qualifiedTreatmentTime;
    private String receivedWard;
    
    private String tranferFromTime;
    private String tranferToTimeOpc;
    private String feedbackResultsReceivedTimeOpc;
    private String route;
    
    private String permanentAddressStreet; // Đường phố thương trú
    private String permanentAddressGroup; // Tổ ấp khu phố thường trú
    private String currentAddressStreet; // Đường phố hiện tại
    private String currentAddressGroup; // Tổ ấp khu phố hiện tại
    private String receivedWardDate; //Nhận thuốc tại tuyến xã
    private String viralLoadResultNumber;
    
    private String suspiciousSymptoms;
    private String examinationAndTest;
    private String laoTestDate;
    private String laoDiagnose;
    private String cotrimoxazoleOtherEndCause;
    private String passTreatment;

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

    public String getLaoDiagnose() {
        return laoDiagnose;
    }

    public void setLaoDiagnose(String laoDiagnose) {
        this.laoDiagnose = laoDiagnose;
    }

    public String getCotrimoxazoleOtherEndCause() {
        return cotrimoxazoleOtherEndCause;
    }

    public void setCotrimoxazoleOtherEndCause(String cotrimoxazoleOtherEndCause) {
        this.cotrimoxazoleOtherEndCause = cotrimoxazoleOtherEndCause;
    }

    public String getPassTreatment() {
        return passTreatment;
    }

    public void setPassTreatment(String passTreatment) {
        this.passTreatment = passTreatment;
    }

    public String getViralLoadResultNumber() {
        return viralLoadResultNumber;
    }

    public void setViralLoadResultNumber(String viralLoadResultNumber) {
        this.viralLoadResultNumber = viralLoadResultNumber;
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

    public String getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(String receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(String tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

    public String getTranferToTimeOpc() {
        return tranferToTimeOpc;
    }

    public void setTranferToTimeOpc(String tranferToTimeOpc) {
        this.tranferToTimeOpc = tranferToTimeOpc;
    }

    public String getFeedbackResultsReceivedTimeOpc() {
        return feedbackResultsReceivedTimeOpc;
    }

    public void setFeedbackResultsReceivedTimeOpc(String feedbackResultsReceivedTimeOpc) {
        this.feedbackResultsReceivedTimeOpc = feedbackResultsReceivedTimeOpc;
    }

    
    
    public String getReceivedWard() {
        return receivedWard;
    }

    public void setReceivedWard(String receivedWard) {
        this.receivedWard = receivedWard;
    }

    public String getQualifiedTreatmentTime() {
        return qualifiedTreatmentTime;
    }

    public void setQualifiedTreatmentTime(String qualifiedTreatmentTime) {
        this.qualifiedTreatmentTime = qualifiedTreatmentTime;
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

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getFeedbackResultsReceivedTime() {
        return feedbackResultsReceivedTime;
    }

    public void setFeedbackResultsReceivedTime(String feedbackResultsReceivedTime) {
        this.feedbackResultsReceivedTime = feedbackResultsReceivedTime;
    }

    public String getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(String dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public String getTranferToTime() {
        return tranferToTime;
    }

    public void setTranferToTime(String tranferToTime) {
        this.tranferToTime = tranferToTime;
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

    public String getArvID() {
        return arvID;
    }

    public void setArvID(String arvID) {
        this.arvID = arvID;
    }

    public String getCurrentSiteID() {
        return currentSiteID;
    }

    public void setCurrentSiteID(String currentSiteID) {
        this.currentSiteID = currentSiteID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
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

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
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

    public String getTransferSiteName() {
        return transferSiteName;
    }

    public void setTransferSiteName(String transferSiteName) {
        this.transferSiteName = transferSiteName;
    }

    public boolean isIsOpcManager() {
        return isOpcManager;
    }

    public void setIsOpcManager(boolean isOpcManager) {
        this.isOpcManager = isOpcManager;
    }

    public String getLao() {
        return lao;
    }

    public void setLao(String lao) {
        this.lao = lao;
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

    public String getLaoSymptoms() {
        return laoSymptoms;
    }

    public void setLaoSymptoms(String laoSymptoms) {
        this.laoSymptoms = laoSymptoms;
    }

    public String getInh() {
        return inh;
    }

    public void setInh(String inh) {
        this.inh = inh;
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

    public String getInhEndCauses() {
        return inhEndCauses;
    }

    public void setInhEndCauses(String inhEndCauses) {
        this.inhEndCauses = inhEndCauses;
    }

    public String getNtch() {
        return ntch;
    }

    public void setNtch(String ntch) {
        this.ntch = ntch;
    }

    public String getNtchOtherSymptom() {
        return ntchOtherSymptom;
    }

    public void setNtchOtherSymptom(String ntchOtherSymptom) {
        this.ntchOtherSymptom = ntchOtherSymptom;
    }

    public String getNtchSymptoms() {
        return ntchSymptoms;
    }

    public void setNtchSymptoms(String ntchSymptoms) {
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

    public String getCotrimoxazoleEndCauses() {
        return cotrimoxazoleEndCauses;
    }

    public void setCotrimoxazoleEndCauses(String cotrimoxazoleEndCauses) {
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

    public String getFirstCd4Causes() {
        return firstCd4Causes;
    }

    public void setFirstCd4Causes(String firstCd4Causes) {
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

    public String getCd4Causes() {
        return cd4Causes;
    }

    public void setCd4Causes(String cd4Causes) {
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

    public String getFirstViralLoadCauses() {
        return firstViralLoadCauses;
    }

    public void setFirstViralLoadCauses(String firstViralLoadCauses) {
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

    public String getViralLoadCauses() {
        return viralLoadCauses;
    }

    public void setViralLoadCauses(String viralLoadCauses) {
        this.viralLoadCauses = viralLoadCauses;
    }

    public String getHbv() {
        return hbv;
    }

    public void setHbv(String hbv) {
        this.hbv = hbv;
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

    public String getHcv() {
        return hcv;
    }

    public void setHcv(String hcv) {
        this.hcv = hcv;
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

    public boolean isIsOtherSite() {
        return isOtherSite;
    }

    public void setIsOtherSite(boolean isOtherSite) {
        this.isOtherSite = isOtherSite;
    }

    public String getPageRedirect() {
        return pageRedirect;
    }

    public void setPageRedirect(String pageRedirect) {
        this.pageRedirect = pageRedirect;
    }

    @Override
    public void setAttributeLabels() {
        super.setAttributeLabels();
        attributeLabels.put("currentSiteID", "Mã cơ sở hiện tại"); //Mã cơ sở hiện tại
        attributeLabels.put("siteID", "Mã cơ sở quản lý"); //Mã cơ sở quản lý
        attributeLabels.put("code", "Mã bệnh án"); //Mã bệnh nhân
        attributeLabels.put("fullName", "Họ và tên"); //Họ và tên
        attributeLabels.put("genderID", "Giới tính"); //Giới tính
        attributeLabels.put("dob", "Ngày sinh"); //Ngày/tháng/năm sinh
        attributeLabels.put("raceID", "Dân tộc"); //Dân tộc
        attributeLabels.put("identityCard", "Số CMND"); //Chứng minh thư
        attributeLabels.put("jobID", "Nghề nghiệp"); //Nghề nghiệp
        attributeLabels.put("objectGroupID", "Nhóm đối tượng"); //Nhóm đối tượng

        attributeLabels.put("insurance", "BN có thẻ BHYT"); //Có thẻ BHYT ?

        attributeLabels.put("insuranceNo", "Số thẻ BHYT"); //Số thẻ BHYT
        attributeLabels.put("insuranceType", "Loại thẻ BHYT"); //Loại thanh toán thẻ BHYT - Sử dụng enum
        attributeLabels.put("insuranceSite", "Nơi ĐK KCB ban đầu"); //Nơi đăng ký kcb ban đầu
        attributeLabels.put("insuranceExpiry", "Ngày hết hạn thẻ BHYT"); //Ngày hết hạn thẻ BHYT
        attributeLabels.put("insurancePay", "Tỷ lệ thanh toán BHYT (%)"); //Tỉ lệ thanh toán thẻ BHYT - Sử dụng enum
        attributeLabels.put("patientPhone", "SĐT bệnh nhân"); //Số điện thoại bệnh nhân
        attributeLabels.put("permanentAddress", "Số nhà thường trú"); //Địa chỉ thường trú
        attributeLabels.put("permanentProvinceID", "Tỉnh/Thành thường trú");
        attributeLabels.put("permanentDistrictID", "Quận/Huyện thường trú");
        attributeLabels.put("permanentWardID", "Xã/Phường thường trú");
        attributeLabels.put("currentAddress", "Số nhà cư trú"); //Địa chỉ cư trú
        attributeLabels.put("currentProvinceID", "Tỉnh/Thành cư trú");
        attributeLabels.put("currentDistrictID", "Quận/Huyện cư trú");
        attributeLabels.put("currentWardID", "Xã/Phường cư trú");
        attributeLabels.put("supporterName", "Họ tên người hỗ trợ"); //Họ và tên người hỗ trợ
        attributeLabels.put("supporterRelation", "Quan hệ với BN"); //Quan hệ với bệnh nhân
        attributeLabels.put("supporterPhone", "SĐT người hỗ trợ"); //Số điện thoại người hỗ trợ
        attributeLabels.put("confirmCode", "Mã XN khẳng định"); // Mã XN khẳng định
        attributeLabels.put("confirmTime", "Ngày XN khẳng định"); //Ngày xét nghiệm khẳng định
        attributeLabels.put("confirmSiteName", "Nơi XN khẳng định");
        attributeLabels.put("pcrOneTime", "Ngày XN PCR lần 1"); //Ngày xn pcr lần 01
        attributeLabels.put("pcrOneResult", "Kết quả XN PCR lần 1"); //Kết quả xn pcr lần 01
        attributeLabels.put("pcrTwoTime", "Ngày XN PCR lần 2"); //Ngày xn pcr lần 02
        attributeLabels.put("pcrTwoResult", "Kết quả XN PCR lần 2"); //Kết quả xn pcr lần 02
        attributeLabels.put("therapySiteID", "Cơ sở điều trị"); //Cơ sở điều trị
        attributeLabels.put("registrationTime", "Ngày đăng ký"); //NGày đăng ký vào OPC
        attributeLabels.put("registrationType", "Loại đăng ký"); //Loại đăng ký - Sử dụng enum
        attributeLabels.put("sourceSiteName", "Cơ sở chuyển gửi"); //mã cơ sở nguồn gửi đến
        attributeLabels.put("sourceCode", "Mã BA chuyển đến"); //Mã bệnh án từ cơ sở khác chuyển tới
        attributeLabels.put("clinicalStage", "Giai đoạn lâm sàng"); // Giai đoạn lâm sàng
        attributeLabels.put("cd4", "CD4  hoặc CD4%"); //CD4 hoặc cd4%
        attributeLabels.put("tranferToTime", "Ngày tiếp nhận");
        attributeLabels.put("dateOfArrival", "Ngày chuyển gửi theo phiếu");
        attributeLabels.put("feedbackResultsReceivedTime", "Ngày phản hồi");
        attributeLabels.put("registrationStatus", "Tình trạng đăng ký");
        attributeLabels.put("patientWeight", "Cân nặng (kg)");
        attributeLabels.put("patientHeight", "Chiều cao (cm)");
        attributeLabels.put("lao", "Sàng lọc Lao"); //Có sàng lọc lao

        attributeLabels.put("laoTestTime", "Ngày sàng lọc"); //Ngày xét nghiệm lao
        attributeLabels.put("laoOtherSymptom", "Triệu chứng khác"); // triệu chứng khác
        attributeLabels.put("laoSymptoms", "Các biểu hiện nghi Lao"); 
        
        attributeLabels.put("laoResult", "Kết quả xét nghiệm Lao"); 
        attributeLabels.put("laoTreatment", "Điều trị Lao"); 
        attributeLabels.put("laoStartTime", "Ngày bắt đầu điều trị Lao"); 
        attributeLabels.put("laoEndTime", "Ngày kết thúc điều trị Lao"); 
        

        attributeLabels.put("inh", "Điều trị dự phòng INH"); //Điều trị dự phòng INH - sử dụng enum

        attributeLabels.put("inhFromTime", "Ngày bắt đầu INH"); //Lao từ ngày
        attributeLabels.put("inhToTime", "Ngày kết thúc INH"); //Lao đến ngày
        attributeLabels.put("inhEndCauses", "Lý do kết thúc INH"); //D8. Lý do kết thúc lao - inh

        attributeLabels.put("ntch", "Điều trị NTCH"); //Nhiễm trùng cơ hội - ntch - sử dụng enum

        attributeLabels.put("ntchOtherSymptom", "Triệu chứng NTCH khác"); // triệu chứng khác ntch
        attributeLabels.put("ntchSymptoms", "Các biểu hiện NTCH"); //D10. Các biểu hiện ntch
        attributeLabels.put("cotrimoxazoleFromTime", "Ngày bắt đầu Cotrimoxazole"); //cotrimoxazole từ ngày
        attributeLabels.put("cotrimoxazoleToTime", "Ngày kết thúc Cotrimoxazole"); //cotrimoxazole đến ngày
        attributeLabels.put("cotrimoxazoleEndCauses", "Lý do kết thúc NTCH"); //Lý do kết thúc cotrimoxazole
        attributeLabels.put("statusOfTreatmentID", "Trạng thái điều trị"); //Trạng thái điều trị
        attributeLabels.put("firstTreatmentTime", "Ngày ARV đầu tiên"); //Ngày ARV đầu tiên
        attributeLabels.put("firstTreatmentRegimenID", "Phác đồ đầu tiên"); //Phác đồ điều trị đầu tiên - liên kết với treatment-regimen
        
        
//        attributeLabels.put("qualifiedTreatmentTime", "Ngày đủ TC điều trị");
        
        
        attributeLabels.put("treatmentTime", "Ngày điều trị ARV"); //Ngày ARV tại cơ sở OPC
        attributeLabels.put("treatmentRegimenStage", "Bậc phác đồ hiện tại"); //Bậc phác đồ điều trị - sử dụng enum
        attributeLabels.put("treatmentRegimenID", "Phác đồ hiện tại"); //Phác đồ điều trị tại cơ sở OPC
        attributeLabels.put("lastExaminationTime", "Ngày khám gần nhất"); //Ngày khám bệnh gần nhất
        attributeLabels.put("medicationAdherence", "Tuân thủ điều trị"); //Tuân thủ điều trị
        attributeLabels.put("daysReceived", "Số ngày nhận thuốc"); //Số ngày nhận thuốc
        attributeLabels.put("appointmentTime", "Ngày hẹn khám"); // Ngày hẹn khám 
        attributeLabels.put("examinationNote", "Các vấn đề khác"); //Các vấn đề trong lần khám gần nhất
        attributeLabels.put("firstCd4Time", "Ngày XN CD4 đầu tiên"); //Ngày xét nghiệm cd4 đầu tiên
        attributeLabels.put("firstCd4Result", "Kết quả XN CD4 đầu tiên"); //Kết quả xét nghiệm cd4
//        attributeLabels.put("firstCd4Causes", "Lý do XN CD4 đầu tiên"); //F3. Lý do xét nghiệm cd4 đầu tiên
        attributeLabels.put("cd4Time", "Ngày XN CD4 gần nhất"); //Ngày xét nghiệm cd4
        attributeLabels.put("cd4Result", "Kết quả XN CD4 gần nhất"); //Kết quả xét nghiệm cd4 gần nhất
        attributeLabels.put("cd4Causes", "Lý do XN CD4 gần nhất"); //F6. Lý do xét nghiệm cd4 gần nhất
        attributeLabels.put("firstViralLoadTime", "Ngày XN tải lượng đầu tiên"); // TLVR - Ngày xét nghiệm đầu tiên
        attributeLabels.put("firstViralLoadResult", "Kết quả XN tải lượng đầu tiên"); //Kết quả xét nghiệm TLVR
        attributeLabels.put("firstViralLoadCauses", "Lý do XN tải lượng đầu tiên"); //G3. TLVR - lý do xét nghiệm  đầu tiên
        attributeLabels.put("viralLoadTime", "Ngày XN tải lượng gần nhất"); // TLVR - Ngày xét nghiệm
        attributeLabels.put("viralLoadResult", "Kết quả XN tải lượng gần nhất"); //Kết quả xét nghiệm TLVR
        attributeLabels.put("viralLoadCauses", "Lý do XN tải lượng gần nhất"); //G6. TLVR - lý do xét nghiệm

        attributeLabels.put("hbv", "Xét nghiệm HBV"); //Có xn hbv hay không - sử dụng enum

        attributeLabels.put("hbvTime", "Ngày xét nghiệm HBV"); // Ngày xét nghiệm hbv
        attributeLabels.put("hbvResult", "Kết quả XN HBV"); //Kết quả XN HBV

        attributeLabels.put("hcv", "Xét nghiệm HCV"); //Có xn HCV hay không - sử dụng enum

        attributeLabels.put("hcvTime", "Ngày xét nghiệm HCV"); // Ngày xét nghiệm HCV
        attributeLabels.put("hcvResult", "Kết quả XN HCV"); //Kết quả XN HCV
        attributeLabels.put("endTime", "Ngày kết thúc"); // Ngày kết thúc tại OPC
        attributeLabels.put("endCase", "Lý do kết thúc"); //Lý do kết thúc điều trị
        attributeLabels.put("transferSiteName", "Cơ sở chuyển đi"); //Cơ sở chuyển đi
        attributeLabels.put("transferCase", "Lý do chuyển"); //Lý do chuyển đi
//        attributeLabels.put("treatmentStageID", ""); //Trạng thái biến động điều trị
//        attributeLabels.put("treatmentStageTime", ""); //Ngày biến động điều trị
		
        attributeLabels.put("tranferFromTime", "Ngày chuyển đi"); 
        attributeLabels.put("tranferToTimeOpc", "Ngày cs chuyển đi tiếp nhận"); 
        attributeLabels.put("feedbackResultsReceivedTimeOpc", "Ngày cs chuyển đi phản hồi"); 
        
        attributeLabels.put("note", "Ghi chú"); //Ghi chú
        
        attributeLabels.put("route", "Tuyến đăng ký bảo hiểm");
        attributeLabels.put("permanentAddressGroup", "Tổ/ấp/Khu phố thường trú");
        attributeLabels.put("permanentAddressStreet", "Đường phố thường trú (nếu có)");
        attributeLabels.put("currentAddressGroup", "Tổ/ấp/Khu phố cư trú");
        attributeLabels.put("currentAddressStreet", "Đường phố cư trú (nếu có)");
        attributeLabels.put("viralLoadResultNumber", "Nồng độ virus");
        attributeLabels.put("receivedWardDate", "Ngày nhận thuốc tại xã");
        
        attributeLabels.put("suspiciousSymptoms", "Triệu chứng nghi ngờ");
        attributeLabels.put("examinationAndTest", "Chuyển gửi khám và xét nghiệm");
        attributeLabels.put("laoTestDate", "Ngày xét nghiệm Lao");
        attributeLabels.put("laoDiagnose", "Chẩn đoán loại hình Lao");
        attributeLabels.put("cotrimoxazoleOtherEndCause", "Lý do kết thúc khác (nếu có)");
        attributeLabels.put("passTreatment", "Đạt tiêu chuẩn điều trị nhanh");
    }
    
    public OpcArvEntity setToEntity(OpcArvEntity opcArvEntity) {
        OpcArvEntity entity = new OpcArvEntity();
        if(opcArvEntity != null){
            entity = opcArvEntity;
        }
        entity.setSiteID(isOpcManager ? Long.parseLong(therapySiteID) : Long.parseLong(currentSiteID));
        entity.setCode(code.trim().toUpperCase());
//        entity.setSourceServiceID(StringUtils.isNotEmpty(htcID) ? ServiceEnum.HTC.getKey() : "");
//        if(StringUtils.isNotEmpty(htcID)){
//            entity.setSourceID(Long.parseLong(htcID));
//        }
        if(opcArvEntity == null){
            entity.setPatient(new OpcPatientEntity());
        }
        entity.getPatient().setSiteID(isOpcManager ? Long.parseLong(therapySiteID) : Long.parseLong(currentSiteID));
        entity.getPatient().setFullName(TextUtils.toFullname(fullName.trim()));
        entity.getPatient().setGenderID(genderID);
        entity.getPatient().setDob(TextUtils.convertDate(dob, formatType));
        entity.getPatient().setRaceID(raceID);
        entity.getPatient().setIdentityCard(identityCard);
        entity.getPatient().setConfirmCode(StringUtils.isNotEmpty(confirmCode) ? confirmCode.trim().toUpperCase() : "");
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
        entity.setPermanentProvinceID(permanentProvinceID);
        entity.setPermanentDistrictID(permanentDistrictID);
        entity.setPermanentWardID(permanentWardID);
        entity.setCurrentAddress(currentAddress);
        entity.setCurrentProvinceID(currentProvinceID);
        entity.setCurrentDistrictID(currentDistrictID);
        entity.setCurrentWardID(currentWardID);
        entity.setSupporterName(StringUtils.isEmpty(supporterName) ? "" : TextUtils.toFullname(supporterName.trim()));
        entity.setSupporterRelation(supporterRelation);
        entity.setSupporterPhone(supporterPhone);
        entity.setPcrOneTime(TextUtils.convertDate(pcrOneTime, formatType));
        entity.setPcrOneResult(pcrOneResult);
        entity.setPcrTwoTime(TextUtils.convertDate(pcrTwoTime, formatType));
        entity.setPcrTwoResult(pcrTwoResult);
        entity.setRegistrationTime(TextUtils.convertDate(registrationTime, formatType));
        entity.setRegistrationType(registrationType);
        entity.setSourceSiteID(StringUtils.isEmpty(sourceSiteID) ? 0 : Long.valueOf(sourceSiteID));
        entity.setSourceArvSiteName(sourceSiteName);
        entity.setSourceArvCode(sourceCode);
        entity.setClinicalStage(clinicalStage);
        entity.setCd4(cd4);
        entity.setTransferSiteName(transferSiteName);
        entity.setLao("1".equals(lao));
        entity.setLaoTestTime(TextUtils.convertDate(laoTestTime, formatType));
        entity.setLaoOtherSymptom(laoOtherSymptom == null ? "" : laoOtherSymptom);
        List<String> listlaoSymptoms = new ArrayList<>();
        listlaoSymptoms.add(laoSymptoms);
        entity.setLaoSymptoms(listlaoSymptoms);
        entity.setInh("1".equals(inh));
        entity.setInhFromTime(TextUtils.convertDate(inhFromTime, formatType));
        entity.setInhToTime(TextUtils.convertDate(inhToTime, formatType));
        List<String> listinhEndCauses = new ArrayList<>();
        listinhEndCauses.add(inhEndCauses);
        entity.setInhEndCauses(listinhEndCauses);
        entity.setNtch("1".equals(ntch));
        entity.setNtchOtherSymptom(ntchOtherSymptom);
        List<String> listntchSymptoms = new ArrayList<>();
        listntchSymptoms.add(ntchSymptoms);
        entity.setNtchSymptoms(listntchSymptoms);
        entity.setCotrimoxazoleFromTime(TextUtils.convertDate(cotrimoxazoleFromTime, formatType));
        entity.setCotrimoxazoleToTime(TextUtils.convertDate(cotrimoxazoleToTime, formatType));
        List<String> listcotrimoxazoleEndCauses = new ArrayList<>();
        listcotrimoxazoleEndCauses.add(cotrimoxazoleEndCauses);
        entity.setCotrimoxazoleEndCauses(listcotrimoxazoleEndCauses);
        entity.setStatusOfTreatmentID(statusOfTreatmentID);
        entity.setFirstTreatmentTime(TextUtils.convertDate(firstTreatmentTime, formatType));
        entity.setFirstTreatmentRegimenID(firstTreatmentRegimenID);
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
        
        List<String> listfirstCd4Causes = new ArrayList<>();
        listfirstCd4Causes.add(firstCd4Causes);
        entity.setFirstCd4Causes(listfirstCd4Causes);
        
        entity.setCd4Time(TextUtils.convertDate(cd4Time, formatType));
        entity.setCd4Result(cd4Result);
        
        List<String> listcd4Causes = new ArrayList<>();
        listcd4Causes.add(firstCd4Causes);
        entity.setCd4Causes(listcd4Causes);
        
        entity.setFirstViralLoadTime(TextUtils.convertDate(firstViralLoadTime, formatType));
        entity.setFirstViralLoadResult(firstViralLoadResult);
        
        List<String> listfirstViralLoadCauses = new ArrayList<>();
        listfirstViralLoadCauses.add(firstViralLoadCauses);
        entity.setFirstViralLoadCauses(listfirstViralLoadCauses);
        
        entity.setViralLoadTime(TextUtils.convertDate(viralLoadTime, formatType));
        entity.setViralLoadResult(viralLoadResult);
        
        List<String> listviralLoadCauses = new ArrayList<>();
        listviralLoadCauses.add(viralLoadCauses);
        entity.setViralLoadCauses(listviralLoadCauses);
        
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
        entity.setTreatmentStageID(StringUtils.isNotEmpty(endCase) ? 
                ("1".equals(endCase) ? "6" : "2".equals(endCase) ? "7" : "3".equals(endCase) ? "3" : "4".equals(endCase) ? "5" : "5".equals(endCase) ? "8" : "") : 
                ("1".equals(registrationType) || "4".equals(registrationType) || "5".equals(registrationType) ? "1" : "2".equals(registrationType) ? "2" : "3".equals(registrationType) ? "4" : ""));
        entity.setTreatmentStageTime(TextUtils.convertDate(StringUtils.isNotEmpty(endTime) ? endTime : registrationTime, formatType));
        entity.setNote(note);
        
        entity.setFeedbackResultsReceivedTimeOpc(TextUtils.convertDate(feedbackResultsReceivedTimeOpc, formatType));
        entity.setFeedbackResultsReceivedTime(TextUtils.convertDate(feedbackResultsReceivedTime, formatType));
        entity.setTranferFromTime(TextUtils.convertDate(tranferFromTime, formatType));
        entity.setTranferToTime(TextUtils.convertDate(tranferToTime, formatType));
        entity.setDateOfArrival(TextUtils.convertDate(dateOfArrival, formatType));
        entity.setViralLoadResultNumber(viralLoadResultNumber);
        entity.setTranferToTimeOpc(TextUtils.convertDate(tranferToTimeOpc, formatType));
        
        if(("not_print".equals(pageRedirect) || "printable".equals(pageRedirect)) && StringUtils.isNotEmpty(sourceSiteID)){
            entity.setTranferToTime(StringUtils.isEmpty(tranferToTime) ? new Date() : TextUtils.convertDate(tranferToTime, formatType));
            entity.setSourceServiceID("1".equals(registrationType) ? ServiceEnum.HTC.getKey() : ServiceEnum.OPC.getKey());
        }
        if("printable".equals(pageRedirect) && StringUtils.isNotEmpty(sourceSiteID)){
            entity.setFeedbackResultsReceivedTime(new Date());
        }
        
        entity.setReceivedWard(receivedWard);
        entity.setQualifiedTreatmentTime(TextUtils.convertDate(qualifiedTreatmentTime, formatType));
        entity.setLaoEndTime(TextUtils.convertDate(laoEndTime, formatType));
        entity.setLaoStartTime(TextUtils.convertDate(laoStartTime, formatType));
        if(laoTreatment != null){
            entity.setLaoTreatment(laoTreatment.equals("1"));
        }
        entity.setLaoResult(laoResult);
        if(StringUtils.isNotEmpty(patientHeight)){
            entity.setPatientHeight((float) Double.parseDouble(patientHeight));
        }
        if(StringUtils.isNotEmpty(patientWeight)){
            entity.setPatientWeight((float) Double.parseDouble(patientWeight));
        }
        entity.setRegistrationStatus(registrationStatus);
        
        entity.setPermanentAddressGroup(permanentAddressGroup);
        entity.setPermanentAddressStreet(permanentAddressStreet);
        entity.setCurrentAddressGroup(currentAddressGroup);
        entity.setCurrentAddressStreet(currentAddressStreet);
        entity.setReceivedWardDate(TextUtils.convertDate(receivedWardDate, formatType));
        
        entity.setRouteID(route);
        if(lao != null && lao.equals("1")){
            entity.setSuspiciousSymptoms(suspiciousSymptoms);
            if(entity.getLaoSymptoms() != null && (entity.getLaoSymptoms().contains(LaoSymtomEnum.HO.getKey()) || 
                    entity.getLaoSymptoms().contains(LaoSymtomEnum.SOT.getKey()) || entity.getLaoSymptoms().contains(LaoSymtomEnum.SUTCAN.getKey()) || 
                    entity.getLaoSymptoms().contains(LaoSymtomEnum.RAMOHOI.getKey()))){
                entity.setSuspiciousSymptoms(SuspiciousSymptomsEnum.CO_TRIEU_CHUNG.getKey());
            }
            if(entity.getSuspiciousSymptoms() != null && entity.getSuspiciousSymptoms().equals(SuspiciousSymptomsEnum.CO_TRIEU_CHUNG.getKey())){
                if(examinationAndTest != null){
                    entity.setExaminationAndTest(examinationAndTest.equals("1"));
                }
                if(entity.isExaminationAndTest()){
                    entity.setLaoTestDate(TextUtils.convertDate(laoTestDate, formatType));
                    entity.setLaoDiagnose(laoDiagnose);
                    entity.setLaoResult(laoResult);
                }
            }
            
        }
        
        if(entity.getCotrimoxazoleEndCauses() != null && entity.getCotrimoxazoleEndCauses().size() >= 1 && 
                entity.getCotrimoxazoleEndCauses().contains("-1")){
            entity.setCotrimoxazoleOtherEndCause(cotrimoxazoleOtherEndCause);
        }
        if(entity.getStatusOfTreatmentID() != null && entity.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())){
            entity.setPassTreatment(StringUtils.isEmpty(passTreatment) ? false :  passTreatment.equals("1"));
        }
        
        return entity;
    }

}

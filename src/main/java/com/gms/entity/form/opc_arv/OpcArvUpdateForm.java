package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author pdthang
 */
public class OpcArvUpdateForm implements Serializable {

    private String ID;

    private String code; //Mã

    private String sourceServiceID; //Loại nguồn dịch vụ gửi đến

    private String sourceSiteID; //mã cơ sở nguồn gửi đến

    private String sourceArvSiteID; //mã cơ sở ARV chuyển gửi đến

    private String sourceArvSiteName; //Cơ sở arv chuyển gửi

    private String sourceArvCode; //Mã bệnh án từ cơ sở khác chuyển tới

    private String sourceID; //mã code đối tượng được gửi đến

    private String siteID; //Mã cơ sở quản lý

    private String patientID; //FK thông tin cơ bản của bệnh nhân

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

    private String supporterName; //Họ và tên người hỗ trợ

    private String supporterRelation; //Quan hệ với bệnh nhân

    private String supporterPhone; //Số điện thoại người hỗ trợ

    private String registrationTime; //NGày đăng ký vào OPC

    private String registrationType; //Loại đăng ký - Sử dụng enum

    private String clinicalStage; // Giai đoạn lâm sàng

    private String cd4; //CD4 hoặc cd4%

    private boolean lao; //Có sàng lọc lao

    private String laoTestTime; //Ngày xét nghiệm lao

    private String laoOtherSymptom; // triệu chứng khác

    private boolean inh; //Điều trị dự phòng INH - sử dụng enum

    private String inhFromTime; //Lao từ ngày

    private String inhToTime; //Lao đến ngày

    private boolean ntch; //Nhiễm trùng cơ hội - ntch - sử dụng enum

    private String ntchOtherSymptom; // triệu chứng khác ntch

    private String cotrimoxazoleFromTime; //cotrimoxazole từ ngày

    private String cotrimoxazoleToTime; //cotrimoxazole đến ngày
//    private String statusOfTreatmentID; //Trạng thái điều trị

    private String treatmentStageID; //Trạng thái biến động điều trị

    private String treatmentStageTime; //Ngày biến động điều trị

    private String firstTreatmentTime; //Ngày ARV đầu tiên

    private String firstTreatmentRegimenID; //Phác đồ điều trị đầu tiên - liên kết với treatment-regimen

    private String treatmentTime; //Ngày ARV tại cơ sở OPC

    private String treatmentRegimenStage; //Bậc phác đồ điều trị - sử dụng enum

    private String treatmentRegimenID; //Phác đồ điều trị tại cơ sở OPC

    private String lastExaminationTime; //Ngày khám bệnh gần nhất

    private String medicationAdherence; //Tuân thủ điều trị

    private int daysReceived; //Số ngày nhận thuốc

    private String appointmentTime; // Ngày hẹn khám   

    private String examinationNote; //Các vấn đề trong lần khám gần nhất

    private String firstCd4Time; //Ngày xét nghiệm cd4 đầu tiên

    private String firstCd4Result; //Kết quả xét nghiệm cd4

    private String cd4Time; //Ngày xét nghiệm cd4

    private String cd4Result; //Kết quả xét nghiệm cd4 gần nhất

    private String firstViralLoadResult; //Kết quả xét nghiệm TLVR

    private String firstViralLoadTime; // TLVR - Ngày xét nghiệm đầu tiên

    private String viralLoadResult; //Kết quả xét nghiệm TLVR

    private String viralLoadTime; // TLVR - Ngày xét nghiệm

    private boolean hbv; //Có xn hbv hay không - sử dụng enum

    private String hbvTime; // Ngày xét nghiệm hbv

    private String hbvResult; //Kết quả XN HBV

    private String hbvCase; //Lý do XN HBV

    private boolean hcv; //Có xn HCV hay không - sử dụng enum

    private String hcvTime; // Ngày xét nghiệm HCV

    private String hcvResult; //Kết quả XN HCV

    private String hcvCase; //Lý do XN HCV
    private String endTime; // Ngày kết thúc tại OPC

    private String endCase; //Lý do kết thúc điều trị

    private String transferSiteID; //Cơ sở chuyển đi

    private String transferSiteName; //Tên Cơ sở chuyển đi

    private String transferCase; //Lý do chuyển đi

    private String note; //Lý do

    private String createAT;

    private String updateAt;

    private String createdBY;

    private String updatedBY;

    private boolean remove; //Xoá logic

    private boolean removeTranfer; //Xoá logic khi chuyển gửi

    private String remoteAT;

    //Bổ sung PCR
    private String pcrOneTime; //Ngày xn pcr lần 01
    private String pcrOneResult; //Kết quả xn pcr lần 01

    private String pcrTwoTime; //Ngày xn pcr lần 02

    private String pcrTwoResult; //Kết quả xn pcr lần 02

    private List<String> laoSymptoms; //D3. Các biểu hiện nghi lao

    private List<String> inhEndCauses; //D8. Lý do kết thúc lao - inh

    private List<String> ntchSymptoms; //D10. Các biểu hiện ntch

    private List<String> cotrimoxazoleEndCauses; //Lý do kết thúc cotrimoxazole

//    private List<String> firstCd4Causes; //F3. Lý do xét nghiệm cd4 đầu tiên

    private List<String> cd4Causes; //F6. Lý do xét nghiệm cd4 gần nhất

    private List<String> firstViralLoadCauses; //G3. TLVR - lý do xét nghiệm  đầu tiên

    private List<String> viralLoadCauses; //G6. TLVR - lý do xét nghiệm

    private String permanentAddressFull;

    private String currentAddressFull;

    // Phần thông tin bệnh nhân private OpcPatientEntity patient;
    private String fullName; //Họ và tên
    private String genderID; //Giới tính
    private String dob; //Ngày/tháng/năm sinh
    private String raceID; //Dân tộc
    private String identityCard; //Chứng minh thư
    private String confirmCode; // Mã XN khẳng định
    private String confirmTime; //Ngày xét nghiệm khẳng định
    private String confirmSiteID; //Cơ sở xét nghiệm khẳng định - lấy từ tham số nơi xét nghiệm khẳng định giống hivinfo
    private String confirmSiteName; //Hoặc Tên cơ sở xét nghiệm khẳng định
    private String dateBefore18mon; //Hoặc Tên cơ sở xét nghiệm khẳng định
    private String currentSiteID; //site
    private String currentCode; //Mã bệnh án khi chưa đổi
    private String routeID; //Mã bệnh án khi chưa đổi

    //COPY ĐỊA CHỈ
    private Boolean isDisplayCopy;
    private Boolean isOtherSite;

    private String arvID; //ID kết nói vs tab khác

    //Thông tin chuyển đến
    private String tranferToTime; //Ngày tiếp nhận từ cơ sở chuyến đến
    private String dateOfArrival; //Ngày chuyển gửi (theo phiếu) khi lý do đăng ký là chuyển đến
    private String feedbackResultsReceivedTime; //Ngày tiếp nhận từ cơ sở chuyến đến

    //Thông tin chuyển đi
    private String tranferFromTime; //Ngày chuyển đi 
    private String tranferToTimeOpc; //ngày cơ sở chuyển đến tiếp nhận
    private String feedbackResultsReceivedTimeOpc; //Ngày cơ sở chuyến đến phản hồi thông tin

    //validate Ngày đầu tiên
    private String treamentTimeValidate; //
    private String viralTimeValidate; //

    private String qualifiedTreatmentTime; //
    private String receivedWard; //

    private String permanentAddressStreet; // Đường phố thương trú
    private String permanentAddressGroup; // Tổ ấp khu phố thường trú
    private String currentAddressStreet; // Đường phố hiện tại
    private String currentAddressGroup; // Tổ ấp khu phố hiện tại

    private String pregnantStartDate;
    private String pregnantEndDate;
    private String joinBornDate;

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

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
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

    public String getViralTimeValidate() {
        return viralTimeValidate;
    }

    public void setViralTimeValidate(String viralTimeValidate) {
        this.viralTimeValidate = viralTimeValidate;
    }

    public String getTreamentTimeValidate() {
        return treamentTimeValidate;
    }

    public void setTreamentTimeValidate(String treamentTimeValidate) {
        this.treamentTimeValidate = treamentTimeValidate;
    }

    public String getTranferToTime() {
        return tranferToTime;
    }

    public void setTranferToTime(String tranferToTime) {
        this.tranferToTime = tranferToTime;
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

    public String getCurrentCode() {
        return currentCode;
    }

    public void setCurrentCode(String currentCode) {
        this.currentCode = currentCode;
    }

    public String getCurrentSiteID() {
        return currentSiteID;
    }

    public void setCurrentSiteID(String currentSiteID) {
        this.currentSiteID = currentSiteID;
    }

    public String getArvID() {
        return arvID;
    }

    public void setArvID(String arvID) {
        this.arvID = arvID;
    }

    public String getDateBefore18mon() {
        return dateBefore18mon;
    }

    public void setDateBefore18mon(String dateBefore18mon) {
        this.dateBefore18mon = dateBefore18mon;
    }

    public Boolean getIsOtherSite() {
        return isOtherSite;
    }

    public void setIsOtherSite(Boolean isOtherSite) {
        this.isOtherSite = isOtherSite;
    }

    public Boolean getIsDisplayCopy() {
        return isDisplayCopy;
    }

    public void setIsDisplayCopy(Boolean isDisplayCopy) {
        this.isDisplayCopy = isDisplayCopy;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getSourceArvSiteID() {
        return sourceArvSiteID;
    }

    public void setSourceArvSiteID(String sourceArvSiteID) {
        this.sourceArvSiteID = sourceArvSiteID;
    }

    public String getSourceArvSiteName() {
        return sourceArvSiteName;
    }

    public void setSourceArvSiteName(String sourceArvSiteName) {
        this.sourceArvSiteName = sourceArvSiteName;
    }

    public String getSourceArvCode() {
        return sourceArvCode;
    }

    public void setSourceArvCode(String sourceArvCode) {
        this.sourceArvCode = sourceArvCode;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
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

    public boolean isLao() {
        return lao;
    }

    public void setLao(boolean lao) {
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

    public boolean isInh() {
        return inh;
    }

    public void setInh(boolean inh) {
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

    public boolean isNtch() {
        return ntch;
    }

    public void setNtch(boolean ntch) {
        this.ntch = ntch;
    }

    public String getNtchOtherSymptom() {
        return ntchOtherSymptom;
    }

    public void setNtchOtherSymptom(String ntchOtherSymptom) {
        this.ntchOtherSymptom = ntchOtherSymptom;
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

//    public String getStatusOfTreatmentID() {
//        return statusOfTreatmentID;
//    }
//
//    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
//        this.statusOfTreatmentID = statusOfTreatmentID;
//    }

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

    public int getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(int daysReceived) {
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

    public String getFirstViralLoadResult() {
        return firstViralLoadResult;
    }

    public void setFirstViralLoadResult(String firstViralLoadResult) {
        this.firstViralLoadResult = firstViralLoadResult;
    }

    public String getFirstViralLoadTime() {
        return firstViralLoadTime;
    }

    public void setFirstViralLoadTime(String firstViralLoadTime) {
        this.firstViralLoadTime = firstViralLoadTime;
    }

    public String getViralLoadResult() {
        return viralLoadResult;
    }

    public void setViralLoadResult(String viralLoadResult) {
        this.viralLoadResult = viralLoadResult;
    }

    public String getViralLoadTime() {
        return viralLoadTime;
    }

    public void setViralLoadTime(String viralLoadTime) {
        this.viralLoadTime = viralLoadTime;
    }

    public boolean isHbv() {
        return hbv;
    }

    public void setHbv(boolean hbv) {
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

    public String getHbvCase() {
        return hbvCase;
    }

    public void setHbvCase(String hbvCase) {
        this.hbvCase = hbvCase;
    }

    public boolean isHcv() {
        return hcv;
    }

    public void setHcv(boolean hcv) {
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

    public String getHcvCase() {
        return hcvCase;
    }

    public void setHcvCase(String hcvCase) {
        this.hcvCase = hcvCase;
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

    public String getTransferSiteName() {
        return transferSiteName;
    }

    public void setTransferSiteName(String transferSiteName) {
        this.transferSiteName = transferSiteName;
    }

    public String getTransferCase() {
        return transferCase;
    }

    public void setTransferCase(String transferCase) {
        this.transferCase = transferCase;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreateAT() {
        return createAT;
    }

    public void setCreateAT(String createAT) {
        this.createAT = createAT;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(String createdBY) {
        this.createdBY = createdBY;
    }

    public String getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(String updatedBY) {
        this.updatedBY = updatedBY;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isRemoveTranfer() {
        return removeTranfer;
    }

    public void setRemoveTranfer(boolean removeTranfer) {
        this.removeTranfer = removeTranfer;
    }

    public String getRemoteAT() {
        return remoteAT;
    }

    public void setRemoteAT(String remoteAT) {
        this.remoteAT = remoteAT;
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

    public List<String> getLaoSymptoms() {
        return laoSymptoms;
    }

    public void setLaoSymptoms(List<String> laoSymptoms) {
        this.laoSymptoms = laoSymptoms;
    }

    public List<String> getInhEndCauses() {
        return inhEndCauses;
    }

    public void setInhEndCauses(List<String> inhEndCauses) {
        this.inhEndCauses = inhEndCauses;
    }

    public List<String> getNtchSymptoms() {
        return ntchSymptoms;
    }

    public void setNtchSymptoms(List<String> ntchSymptoms) {
        this.ntchSymptoms = ntchSymptoms;
    }

    public List<String> getCotrimoxazoleEndCauses() {
        return cotrimoxazoleEndCauses;
    }

    public void setCotrimoxazoleEndCauses(List<String> cotrimoxazoleEndCauses) {
        this.cotrimoxazoleEndCauses = cotrimoxazoleEndCauses;
    }

//    public List<String> getFirstCd4Causes() {
//        return firstCd4Causes;
//    }
//
//    public void setFirstCd4Causes(List<String> firstCd4Causes) {
//        this.firstCd4Causes = firstCd4Causes;
//    }

    public List<String> getCd4Causes() {
        return cd4Causes;
    }

    public void setCd4Causes(List<String> cd4Causes) {
        this.cd4Causes = cd4Causes;
    }

    public List<String> getFirstViralLoadCauses() {
        return firstViralLoadCauses;
    }

    public void setFirstViralLoadCauses(List<String> firstViralLoadCauses) {
        this.firstViralLoadCauses = firstViralLoadCauses;
    }

    public List<String> getViralLoadCauses() {
        return viralLoadCauses;
    }

    public void setViralLoadCauses(List<String> viralLoadCauses) {
        this.viralLoadCauses = viralLoadCauses;
    }

    public String getPermanentAddressFull() {
        return permanentAddressFull;
    }

    public void setPermanentAddressFull(String permanentAddressFull) {
        this.permanentAddressFull = permanentAddressFull;
    }

    public String getCurrentAddressFull() {
        return currentAddressFull;
    }

    public void setCurrentAddressFull(String currentAddressFull) {
        this.currentAddressFull = currentAddressFull;
    }

    public void setForm(OpcArvEntity entity) {

        String formatDate = "dd/MM/yyyy";

        //Set giá trị cho cho form
        setID(String.valueOf(entity.getID()));
        setArvID(getID());
//        setPatientID(String.valueOf(entity.getPatient()));
        setCode(entity.getCode());
        setFullName(entity.getPatient().getFullName());
        setGenderID(entity.getPatient().getGenderID());
        setDob(TextUtils.formatDate(entity.getPatient().getDob(), formatDate));
        setIdentityCard(entity.getPatient().getIdentityCard());
        setRaceID(entity.getPatient().getRaceID());
        setJobID(entity.getJobID());

        setObjectGroupID(entity.getObjectGroupID());
        setInsurance(entity.getInsurance());
        setInsuranceNo(entity.getInsuranceNo());
        setInsuranceType(entity.getInsuranceType());
        setInsuranceSite(entity.getInsuranceSite());
        setInsuranceExpiry(TextUtils.formatDate(entity.getInsuranceExpiry(), formatDate));
//        setDateBefore18mon(TextUtils.formatDate(date, formatDate));
        setInsurancePay(entity.getInsurancePay());
        setPatientPhone(entity.getPatientPhone());
        //dia chi
        setPermanentAddress(entity.getPermanentAddress());
        setPermanentProvinceID(entity.getPermanentProvinceID());
        setPermanentDistrictID(entity.getPermanentDistrictID());
        setPermanentWardID(entity.getPermanentWardID());
        setCurrentAddress(entity.getCurrentAddress());
        setCurrentProvinceID(entity.getCurrentProvinceID());
        setCurrentDistrictID(entity.getCurrentDistrictID());
        setCurrentWardID(entity.getCurrentWardID());

        setSupporterName(entity.getSupporterName());
        setSupporterRelation(entity.getSupporterRelation());
        setSupporterPhone(entity.getSupporterPhone());

        setConfirmCode(entity.getPatient().getConfirmCode());
        setConfirmTime(TextUtils.formatDate(entity.getPatient().getConfirmTime(), formatDate));
        setConfirmSiteID(String.valueOf(entity.getPatient().getConfirmSiteID()));
        setConfirmSiteName(entity.getPatient().getConfirmSiteName());
        setPcrOneTime(TextUtils.formatDate(entity.getPcrOneTime(), formatDate));
        setPcrOneResult(entity.getPcrOneResult());
        setPcrTwoTime(TextUtils.formatDate(entity.getPcrTwoTime(), formatDate));
        setPcrTwoResult(entity.getPcrTwoResult());
        
        setPregnantStartDate(TextUtils.formatDate(entity.getPregnantStartDate(), formatDate));
        setPregnantEndDate(TextUtils.formatDate(entity.getPregnantEndDate(), formatDate));
        setJoinBornDate(TextUtils.formatDate(entity.getJoinBornDate(), formatDate));

    }
}

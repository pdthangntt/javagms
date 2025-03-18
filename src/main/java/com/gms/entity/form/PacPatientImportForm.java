package com.gms.entity.form;

import com.gms.entity.db.BaseEntity;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author NamAnh_HaUI
 */
public class PacPatientImportForm extends BaseEntity {

    private final String REGEX_VALIDATE_DATE = "^(?=\\d{2}([-.,\\/])\\d{2}\\1\\d{4}$)(?:0[1-9]|1\\d|[2][0-8]|29(?!.02.(?!(?!(?:[02468][1-35-79]|[13579][0-13-57-9])00)\\d{2}(?:[02468][048]|[13579][26])))|30(?!.02)|31(?=.(?:0[13578]|10|12))).(?:0[1-9]|1[012]).\\d{4}$";
    private final String REGEX_ALLOWED_CHAR = "^[A-Z0-9,-]{0,50}$";
    private final String REGEX_YEAR_CHAR = "^(19|20)\\d{2}$";
    private final String REGEX_VN_CHAR = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ"
            + "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ"
            + "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";

//    @NotBlank(message = "Nguồn dịch vụ phát hiện không được để trống")
    private String sourceServiceID; //Loại nguồn dịch vụ gửi đến

    private Long sourceID; //mã code đối tượng được gửi đến

    @NotBlank(message = "Tên người nhiễm không được để trống")
    @Size(max = 50, message = "Tên người nhiễm có độ dài tối đa 100 kí tự.")
    private String fullname;

//    @NotBlank(message = "Dân tộc không được để trống")
    private String raceID; //Dân tộc

    @NotBlank(message = "Giới tính không được để trống")
    private String genderID; //Giới tính

    @NotBlank(message = "Năm sinh không được để trống")
    @Pattern(regexp = REGEX_YEAR_CHAR, message = "Năm sinh không đúng định dạng")
    private String yearOfBirth;

    private String identityCard; //Chứng minh thư nhân dân

    private String healthInsuranceNo; //Mã bảo hiểm y tế

    private String causeOfDeath; //Nguyên nhân tử vong

//    @NotBlank(message = "Nghề nghiệp không được để trống")
    private String jobID; //công việc

    @NotBlank(message = "Nhóm đối tượng không được để trống")
    private String objectGroupID; //Nhóm đối tượng

//    @NotBlank(message = "Hành vi nguy cơ không được để trống")
    private String riskBehaviorID; //Hành vi nguy cơ lây nhiễm

//    @NotBlank(message = "Đường lây nhiễm không được để trống")
    private String modeOfTransmissionID; //Đưòng lây

//    @NotBlank(message = "Kết quả xác minh hiện trạng cư trú không được để trống")
    private String statusOfResidentID; //Hiện trạng cư trú

    private String permanentAddressNo; //Địa chỉ thường trú
    private String permanentAddressGroup; //Tổ
    private String permanentAddressStreet; //Đường phố

    @NotBlank(message = "Tỉnh/Thành phố thường trú không được để trống")
    private String permanentProvinceID;

    @NotBlank(message = "Quận/huyện thường trú không được để trống")
    private String permanentDistrictID;

    @NotBlank(message = "Phường/xã thường trú không được để trống")
    private String permanentWardID;

    private String currentAddressNo; //Địa chỉ cư trú
    private String currentAddressGroup; //Tổ
    private String currentAddressStreet; //Đường phố

    @NotBlank(message = "Tỉnh/Thành phố cư trú không được để trống")
    private String currentProvinceID;

    @NotBlank(message = "Quận/huyện cư trú không được để trống")
    private String currentDistrictID;

    @NotBlank(message = "Phường/xã cư trú không được để trống")
    private String currentWardID;

    private String typeOfPatientID; //Loại bệnh nhân

    @Pattern(regexp = REGEX_VALIDATE_DATE, message = "Định dạng ngày không đúng dd/mm/yyyy")
//    @NotBlank(message = "Ngày XN khẳng định không được để trống")
    private String confirmTime; //Ngày xét nghiệm khẳng định

//    @NotBlank(message = "Tên cơ sở XN khẳng định không được để trống")
    private String siteConfirmID; //Cơ sở xét nghiệm khẳng định

//    @NotBlank(message = "Nơi lấy mẫu XN không được để trống")
    private String bloodBaseID; //Nơi lấy máu

//    @NotBlank(message = "Trạng thái điều trị không được để trống")
    private String statusOfTreatmentID; //Trạng thái điều trị

    @Pattern(regexp = REGEX_VALIDATE_DATE, message = "Định dạng ngày không đúng dd/mm/yyyy")
    private String deathTime; //Ngày tử vong

    private String note; //Ghi chú

    private String tab; // dùng để gán lại tab sau khi cập nhật thông tin bệnh nhân hoặc xem thông tin bệnh nhân

    @Pattern(regexp = REGEX_VALIDATE_DATE, message = "Định dạng ngày không đúng dd/mm/yyyy")
    private String startTreatmentTime; //Ngày bắt đầu điều trị 

    private String siteTreatmentFacilityID; //Cơ sở điều trị - liên kết với treatment-facility

    private String treatmentRegimenID; //Phác đồ điều trị - liên kết với treatment-regimen

    private String symptomID; //Triệu chứng - liên kết với symptom

    //update 04/09
    private String patientPhone;
    private String confirmCode;
    private String earlyHiv;
    private String earlyHivTime;
    private String virusLoadConfirm;
    private String virusLoadConfirmDate;
    private String cdFourResult;
    private String cdFourResultDate;
    private String cdFourResultCurrent;
    private String cdFourResultLastestDate;
    private String virusLoadArv;
    private String virusLoadArvDate;
    private String virusLoadArvCurrent;
    private String virusLoadArvLastestDate;
    private String clinicalStage;
    private String clinicalStageDate;
    private String opcCode;
    private String aidsStatus;
    private String aidsStatusDate;
    private String statusOfChangeTreatmentID;
    private String changeTreatmentDate;
    private String requestDeathTime;

    //end update 04/09

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getEarlyHivTime() {
        return earlyHivTime;
    }

    public void setEarlyHivTime(String earlyHivTime) {
        this.earlyHivTime = earlyHivTime;
    }

    public String getVirusLoadConfirm() {
        return virusLoadConfirm;
    }

    public void setVirusLoadConfirm(String virusLoadConfirm) {
        this.virusLoadConfirm = virusLoadConfirm;
    }

    public String getVirusLoadConfirmDate() {
        return virusLoadConfirmDate;
    }

    public void setVirusLoadConfirmDate(String virusLoadConfirmDate) {
        this.virusLoadConfirmDate = virusLoadConfirmDate;
    }

    public String getCdFourResult() {
        return cdFourResult;
    }

    public void setCdFourResult(String cdFourResult) {
        this.cdFourResult = cdFourResult;
    }

    public String getCdFourResultDate() {
        return cdFourResultDate;
    }

    public void setCdFourResultDate(String cdFourResultDate) {
        this.cdFourResultDate = cdFourResultDate;
    }

    public String getCdFourResultCurrent() {
        return cdFourResultCurrent;
    }

    public void setCdFourResultCurrent(String cdFourResultCurrent) {
        this.cdFourResultCurrent = cdFourResultCurrent;
    }

    public String getCdFourResultLastestDate() {
        return cdFourResultLastestDate;
    }

    public void setCdFourResultLastestDate(String cdFourResultLastestDate) {
        this.cdFourResultLastestDate = cdFourResultLastestDate;
    }

    public String getVirusLoadArv() {
        return virusLoadArv;
    }

    public void setVirusLoadArv(String virusLoadArv) {
        this.virusLoadArv = virusLoadArv;
    }

    public String getVirusLoadArvDate() {
        return virusLoadArvDate;
    }

    public void setVirusLoadArvDate(String virusLoadArvDate) {
        this.virusLoadArvDate = virusLoadArvDate;
    }

    public String getVirusLoadArvCurrent() {
        return virusLoadArvCurrent;
    }

    public void setVirusLoadArvCurrent(String virusLoadArvCurrent) {
        this.virusLoadArvCurrent = virusLoadArvCurrent;
    }

    public String getVirusLoadArvLastestDate() {
        return virusLoadArvLastestDate;
    }

    public void setVirusLoadArvLastestDate(String virusLoadArvLastestDate) {
        this.virusLoadArvLastestDate = virusLoadArvLastestDate;
    }

    public String getClinicalStage() {
        return clinicalStage;
    }

    public void setClinicalStage(String clinicalStage) {
        this.clinicalStage = clinicalStage;
    }

    public String getClinicalStageDate() {
        return clinicalStageDate;
    }

    public void setClinicalStageDate(String clinicalStageDate) {
        this.clinicalStageDate = clinicalStageDate;
    }

    public String getOpcCode() {
        return opcCode;
    }

    public void setOpcCode(String opcCode) {
        this.opcCode = opcCode;
    }

    public String getAidsStatus() {
        return aidsStatus;
    }

    public void setAidsStatus(String aidsStatus) {
        this.aidsStatus = aidsStatus;
    }

    public String getAidsStatusDate() {
        return aidsStatusDate;
    }

    public void setAidsStatusDate(String aidsStatusDate) {
        this.aidsStatusDate = aidsStatusDate;
    }

    public String getStatusOfChangeTreatmentID() {
        return statusOfChangeTreatmentID;
    }

    public void setStatusOfChangeTreatmentID(String statusOfChangeTreatmentID) {
        this.statusOfChangeTreatmentID = statusOfChangeTreatmentID;
    }

    public String getChangeTreatmentDate() {
        return changeTreatmentDate;
    }

    public void setChangeTreatmentDate(String changeTreatmentDate) {
        this.changeTreatmentDate = changeTreatmentDate;
    }

    public String getRequestDeathTime() {
        return requestDeathTime;
    }

    public void setRequestDeathTime(String requestDeathTime) {
        this.requestDeathTime = requestDeathTime;
    }
    
    
    
    
    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getHealthInsuranceNo() {
        return healthInsuranceNo;
    }

    public void setHealthInsuranceNo(String healthInsuranceNo) {
        this.healthInsuranceNo = healthInsuranceNo;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
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

    public String getModeOfTransmissionID() {
        return modeOfTransmissionID;
    }

    public void setModeOfTransmissionID(String modeOfTransmissionID) {
        this.modeOfTransmissionID = modeOfTransmissionID;
    }

    public String getStatusOfResidentID() {
        return statusOfResidentID;
    }

    public void setStatusOfResidentID(String statusOfResidentID) {
        this.statusOfResidentID = statusOfResidentID;
    }

    public String getPermanentAddressNo() {
        return permanentAddressNo;
    }

    public void setPermanentAddressNo(String permanentAddressNo) {
        this.permanentAddressNo = permanentAddressNo;
    }

    public String getPermanentAddressGroup() {
        return permanentAddressGroup;
    }

    public void setPermanentAddressGroup(String permanentAddressGroup) {
        this.permanentAddressGroup = permanentAddressGroup;
    }

    public String getPermanentAddressStreet() {
        return permanentAddressStreet;
    }

    public void setPermanentAddressStreet(String permanentAddressStreet) {
        this.permanentAddressStreet = permanentAddressStreet;
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

    public String getCurrentAddressNo() {
        return currentAddressNo;
    }

    public void setCurrentAddressNo(String currentAddressNo) {
        this.currentAddressNo = currentAddressNo;
    }

    public String getCurrentAddressGroup() {
        return currentAddressGroup;
    }

    public void setCurrentAddressGroup(String currentAddressGroup) {
        this.currentAddressGroup = currentAddressGroup;
    }

    public String getCurrentAddressStreet() {
        return currentAddressStreet;
    }

    public void setCurrentAddressStreet(String currentAddressStreet) {
        this.currentAddressStreet = currentAddressStreet;
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

    public String getTypeOfPatientID() {
        return typeOfPatientID;
    }

    public void setTypeOfPatientID(String typeOfPatientID) {
        this.typeOfPatientID = typeOfPatientID;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getSiteConfirmID() {
        return siteConfirmID;
    }

    public void setSiteConfirmID(String siteConfirmID) {
        this.siteConfirmID = siteConfirmID;
    }

    public String getBloodBaseID() {
        return bloodBaseID;
    }

    public void setBloodBaseID(String bloodBaseID) {
        this.bloodBaseID = bloodBaseID;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(String deathTime) {
        this.deathTime = deathTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getStartTreatmentTime() {
        return startTreatmentTime;
    }

    public void setStartTreatmentTime(String startTreatmentTime) {
        this.startTreatmentTime = startTreatmentTime;
    }

    public String getSiteTreatmentFacilityID() {
        return siteTreatmentFacilityID;
    }

    public void setSiteTreatmentFacilityID(String siteTreatmentFacilityID) {
        this.siteTreatmentFacilityID = siteTreatmentFacilityID;
    }

    public String getTreatmentRegimenID() {
        return treatmentRegimenID;
    }

    public void setTreatmentRegimenID(String treatmentRegimenID) {
        this.treatmentRegimenID = treatmentRegimenID;
    }

    public String getSymptomID() {
        return symptomID;
    }

    public void setSymptomID(String symptomID) {
        this.symptomID = symptomID;
    }

    public String getRiskBehaviorID() {
        return riskBehaviorID;
    }

    public void setRiskBehaviorID(String riskBehaviorID) {
        this.riskBehaviorID = riskBehaviorID;
    }

}

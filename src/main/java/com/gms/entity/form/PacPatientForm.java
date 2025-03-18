package com.gms.entity.form;

import com.gms.components.TextUtils;
import com.gms.components.annotation.ExistFK;
import com.gms.components.annotation.ParameterFK;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author vvthanh
 */
public class PacPatientForm implements Serializable {

    private Long ID;

//    @NotBlank(message = "Nguồn dịch vụ phát hiện không được để trống")
    @ParameterFK(fieldName = "sourceServiceID", type = ParameterEntity.SERVICE, mutiple = false, service = ParameterService.class)
    private String sourceServiceID; //Loại nguồn dịch vụ gửi đến
    private Long sourceID; //mã code đối tượng được gửi đến

    @NotBlank(message = "Tên người nhiễm không được để trống")
    @Size(max = 50, message = "Tên người nhiễm có độ dài tối đa 100 kí tự.")
    private String fullname;

    private String raceID; //Dân tộc

    @NotBlank(message = "Giới tính không được để trống")
    @ParameterFK(fieldName = "genderID", type = ParameterEntity.GENDER, mutiple = false, service = ParameterService.class, message = "Giới tính không tồn tại")
    private String genderID; //Giới tính

    @NotBlank(message = "Năm sinh không được để trống")
    private String yearOfBirth;

    private String identityCard; //Chứng minh thư nhân dân
    private String healthInsuranceNo; //Mã bảo hiểm y tế
    private String hasHealthInsurance;
    ; // Câu hỏi có BHYT hay không
    private List<String> causeOfDeath; //Nguyên nhân tử vong

    private String jobID; //công việc

    @NotBlank(message = "Nhóm đối tượng không được để trống")
    @ParameterFK(fieldName = "objectGroupID", type = ParameterEntity.TEST_OBJECT_GROUP, mutiple = false, service = ParameterService.class)
    private String objectGroupID; //Nhóm đối tượng

    private List<String> riskBehaviorID; //Hành vi nguy cơ lây nhiễm

    private String modeOfTransmissionID; //Đưòng lây

    @ParameterFK(fieldName = "statusOfResidentID", type = ParameterEntity.STATUS_OF_RESIDENT, mutiple = false, service = ParameterService.class)
    private String statusOfResidentID; //Hiện trạng cư trú

    private String permanentAddressNo; //Địa chỉ thường trú
    private String permanentAddressGroup; //Tổ
    private String permanentAddressStreet; //Đường phố

    @NotBlank(message = "Tỉnh/Thành phố thường trú không được để trống")
    @ExistFK(fieldName = "permanentProvinceID", service = LocationsService.class, message = "Tỉnh thành không tồn tại")
    private String permanentProvinceID;

    @NotBlank(message = "Quận/huyện thường trú không được để trống")
    @ExistFK(fieldName = "permanentDistrictID", service = LocationsService.class, message = "Quận huyện không tồn tại")
    private String permanentDistrictID;

    @NotBlank(message = "Phường/xã thường trú không được để trống")
    @ExistFK(fieldName = "permanentWardID", service = LocationsService.class, message = "Xã không tồn tại")
    private String permanentWardID;

    private String currentAddressNo; //Địa chỉ cư trú
    private String currentAddressGroup; //Tổ
    private String currentAddressStreet; //Đường phố
    private String refParentID; //Đường phố

    @ExistFK(fieldName = "currentProvinceID", service = LocationsService.class, message = "Tỉnh thành không tồn tại")
    private String currentProvinceID;

    @ExistFK(fieldName = "currentDistrictID", service = LocationsService.class, message = "Quận huyện không tồn tại")
    private String currentDistrictID;

    @ExistFK(fieldName = "currentWardID", service = LocationsService.class, message = "Xã không tồn tại")
    private String currentWardID;

    @ParameterFK(fieldName = "typeOfPatientID", type = ParameterEntity.TYPE_OF_PATIENT, mutiple = false, service = ParameterService.class)
    private String typeOfPatientID; //Loại bệnh nhân

    private String confirmTime; //Ngày xét nghiệm khẳng định

    @ParameterFK(fieldName = "siteConfirmID", type = ParameterEntity.PLACE_TEST, mutiple = false, service = ParameterService.class)
    private String siteConfirmID; //Cơ sở xét nghiệm khẳng định

    @ParameterFK(fieldName = "bloodBaseID", type = ParameterEntity.BLOOD_BASE, mutiple = false, service = ParameterService.class)
    private String bloodBaseID; //Nơi lấy máu

    @ParameterFK(fieldName = "statusOfTreatmentID", type = ParameterEntity.STATUS_OF_TREATMENT, mutiple = false, service = ParameterService.class)
    private String statusOfTreatmentID; //Trạng thái điều trị

    public String getRefParentID() {
        return refParentID;
    }

    public void setRefParentID(String refParentID) {
        this.refParentID = refParentID;
    }

    private String deathTime; //Ngày tử vong
    private String requestDeathTime; //Ngày báo tử vong
    private String note; //Ghi chú
    private String tab; // dùng để gán lại tab sau khi cập nhật thông tin bệnh nhân hoặc xem thông tin bệnh nhân
    private boolean equalName; // dùng để so sánh tên để bôi màu, chức năng Kiểm tra trùng lắp
    private String startTreatmentTime; //Ngày bắt đầu điều trị 
    private String siteTreatmentFacilityID; //Cơ sở điều trị - liên kết với treatment-facility
    private String treatmentRegimenID; //Phác đồ điều trị - liên kết với treatment-regimen
    private String symptomID; //Triệu chứng - liên kết với symptom
    private String permanentAddressFull;
    private Date acceptTime; //Duyệt sang cần rà soát
    private Date reviewWardTime; //Trạng thái rà soát cấp xã
    private Date reviewProvinceTime;
    private String reviewProvinceTimeForm;

    private String virusLoadConfirm; //Tải lượng virut
    private String virusLoadArv; //Tải lượng virut

    private String earlyHiv;
    private String earlyHivTime;
    private String virusLoadArvDate;
    private String virusLoadConfirmDate;
    private String cdFourResult; //CD4result
    private String cdFourResultDate; // CD4ResultDate
    private String clinicalStage;
    private String clinicalStageDate; // clinicalStageDate
    private String aidsStatus;
    private String aidsStatusDate; // AIDS_STATUS_DATE   
    private String changeTreatmentDate; // Ngày biến động điều trị   
    private String cdFourResultCurrent; //CD4result hiện tại
    private String cdFourResultLastestDate; // CD4ResultLastestDate Ngày XN CD4 ngày gần nhất
    private String virusLoadArvCurrent; //Tải lượng virus ARV hiện tại
    private String virusLoadArvLastestDate; // Ngày XN tải lượng virus gần đây nhất
    private String statusOfChangeTreatmentID; //Trạng thái biến động điều trị
    private String confirmCode; // Mã XN khẳng định
    private String acceptPermanentTime; // //Trạng thái duyệt chuyển sang ngoại tỉnh phát hiện
    private boolean isConnect;

    private String provinceID; //Tỉnh thành quản lý
    private String districtID; //Quận huyện quản lý
    private String wardID; // Xã quản lý

    private String createAT;
    private String updateAt;

    private String patientPhone; //Số điện thoại bệnh nhân
    private String opcCode; // mã bệnh án

    private String virusLoadNumber; //Kết quả xét nghiệm TLVR - nhập số

    private String earlyBioName; // Tên sinh phẩm nhiễm mới

    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    //OTHoa update
    private String insuranceExpiry; //Ngày hết hạn bhyt
    private String firstTreatmentTime; //Ngày ARV đầu tiên
    private String firstTreatmentRegimenId; //Phác đồ điều trị đầu tiên
    private String registrationTime;//Ngày đăng ký
    private String registrationType; // Loại đăng ký
    private String endTime; //Ngày kết thúc
    private String endCase; // Lý do kết thúc

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

    public String getHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public void setHasHealthInsurance(String hasHealthInsurance) {
        this.hasHealthInsurance = hasHealthInsurance;
    }

    public String getOpcCode() {
        return opcCode;
    }

    public void setOpcCode(String opcCode) {
        this.opcCode = opcCode;
    }

    public String getChangeTreatmentDate() {
        return changeTreatmentDate;
    }

    public void setChangeTreatmentDate(String changeTreatmentDate) {
        this.changeTreatmentDate = changeTreatmentDate;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getRequestDeathTime() {
        return requestDeathTime;
    }

    public void setRequestDeathTime(String requestDeathTime) {
        this.requestDeathTime = requestDeathTime;
    }

    public String getReviewProvinceTimeForm() {
        return reviewProvinceTimeForm;
    }

    public void setReviewProvinceTimeForm(String reviewProvinceTimeForm) {
        this.reviewProvinceTimeForm = reviewProvinceTimeForm;
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

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public boolean isIsConnect() {
        return isConnect;
    }

    public void setIsConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public String getAcceptPermanentTime() {
        return acceptPermanentTime;
    }

    public void setAcceptPermanentTime(String acceptPermanentTime) {
        this.acceptPermanentTime = acceptPermanentTime;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getEarlyHivTime() {
        return earlyHivTime;
    }

    public void setEarlyHivTime(String earlyHivTime) {
        this.earlyHivTime = earlyHivTime;
    }

    public String getCdFourResult() {
        return cdFourResult;
    }

    public String getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(String insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public String getFirstTreatmentTime() {
        return firstTreatmentTime;
    }

    public void setFirstTreatmentTime(String firstTreatmentTime) {
        this.firstTreatmentTime = firstTreatmentTime;
    }

    public String getFirstTreatmentRegimenId() {
        return firstTreatmentRegimenId;
    }

    public void setFirstTreatmentRegimenId(String firstTreatmentRegimenId) {
        this.firstTreatmentRegimenId = firstTreatmentRegimenId;
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

    public void setCdFourResult(String cdFourResult) {
        this.cdFourResult = cdFourResult;
    }

    public String getCdFourResultDate() {
        return cdFourResultDate;
    }

    public void setCdFourResultDate(String cdFourResultDate) {
        this.cdFourResultDate = cdFourResultDate;
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

    public String getAidsStatusDate() {
        return aidsStatusDate;
    }

    public void setAidsStatusDate(String aidsStatusDate) {
        this.aidsStatusDate = aidsStatusDate;
    }

    public String getVirusLoadConfirm() {
        return virusLoadConfirm;
    }

    public void setVirusLoadConfirm(String virusLoadConfirm) {
        this.virusLoadConfirm = virusLoadConfirm;
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

    public String getVirusLoadConfirmDate() {
        return virusLoadConfirmDate;
    }

    public void setVirusLoadConfirmDate(String virusLoadConfirmDate) {
        this.virusLoadConfirmDate = virusLoadConfirmDate;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getAidsStatus() {
        return aidsStatus;
    }

    public void setAidsStatus(String aidsStatus) {
        this.aidsStatus = aidsStatus;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getReviewWardTime() {
        return reviewWardTime;
    }

    public void setReviewWardTime(Date reviewWardTime) {
        this.reviewWardTime = reviewWardTime;
    }

    public Date getReviewProvinceTime() {
        return reviewProvinceTime;
    }

    public void setReviewProvinceTime(Date reviewProvinceTime) {
        this.reviewProvinceTime = reviewProvinceTime;
    }

    public String getPermanentAddressFull() {
        return permanentAddressFull;
    }

    public void setPermanentAddressFull(String permanentAddressFull) {
        this.permanentAddressFull = permanentAddressFull;
    }

    public boolean isEqualName() {
        return equalName;
    }

    public void setEqualName(boolean equalName) {
        this.equalName = equalName;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public List<String> getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(List<String> causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
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

    //Trường đánh dấu
    private Boolean isDisplayCopy;

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

    public List<String> getRiskBehaviorID() {
        return riskBehaviorID;
    }

    public void setRiskBehaviorID(List<String> riskBehaviorID) {
        this.riskBehaviorID = riskBehaviorID;
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

    public String getStatusOfChangeTreatmentID() {
        return statusOfChangeTreatmentID;
    }

    public void setStatusOfChangeTreatmentID(String statusOfChangeTreatmentID) {
        this.statusOfChangeTreatmentID = statusOfChangeTreatmentID;
    }

    /**
     * Chuyển dữ liệu từ form sang entity
     *
     * @author pdThang
     * @param pacPatient
     * @return
     * @throws ParseException
     */
    public PacPatientInfoEntity getPacPatient(PacPatientInfoEntity pacPatient) throws Exception {
        if (pacPatient == null) {
            pacPatient = new PacPatientInfoEntity();
        }
        try {
            pacPatient.setYearOfBirth(StringUtils.isNotBlank(getYearOfBirth()) ? Integer.valueOf(getYearOfBirth()) : 0);
        } catch (Exception e) {
        }
        pacPatient.setFullname(getFullname());
        pacPatient.setGenderID(getGenderID());
        pacPatient.setRaceID(getRaceID());
        pacPatient.setJobID(getJobID());
        pacPatient.setHealthInsuranceNo(getHealthInsuranceNo());
        pacPatient.setIdentityCard(getIdentityCard());
        pacPatient.setObjectGroupID(getObjectGroupID());
        pacPatient.setModeOfTransmissionID(getModeOfTransmissionID());
        pacPatient.setRiskBehaviorID(getRiskBehaviorID());
        pacPatient.setCauseOfDeath(getCauseOfDeath());
        pacPatient.setPatientPhone(getPatientPhone());

        pacPatient.setPermanentAddressNo(getPermanentAddressNo());
        pacPatient.setPermanentAddressGroup(getPermanentAddressGroup());
        pacPatient.setPermanentAddressStreet(getPermanentAddressStreet());
        pacPatient.setPermanentProvinceID(getPermanentProvinceID());
        pacPatient.setPermanentDistrictID(getPermanentDistrictID());
        pacPatient.setPermanentWardID(getPermanentWardID());
        pacPatient.setSourceServiceID(getSourceServiceID());
        if (getIsDisplayCopy() == null || getIsDisplayCopy()) {
            pacPatient.setCurrentAddressNo(pacPatient.getPermanentAddressNo());
            pacPatient.setCurrentAddressGroup(pacPatient.getPermanentAddressGroup());
            pacPatient.setCurrentAddressStreet(pacPatient.getPermanentAddressStreet());
            pacPatient.setCurrentProvinceID(pacPatient.getPermanentProvinceID());
            pacPatient.setCurrentDistrictID(pacPatient.getPermanentDistrictID());
            pacPatient.setCurrentWardID(pacPatient.getPermanentWardID());
        } else {
            pacPatient.setCurrentAddressNo(getCurrentAddressNo());
            pacPatient.setCurrentAddressGroup(getCurrentAddressGroup());
            pacPatient.setCurrentAddressStreet(getCurrentAddressStreet());
            pacPatient.setCurrentProvinceID(getCurrentProvinceID());
            pacPatient.setCurrentDistrictID(getCurrentDistrictID());
            pacPatient.setCurrentWardID(getCurrentWardID());
        }

        pacPatient.setStatusOfResidentID(getStatusOfResidentID());
        pacPatient.setConfirmTime(confirmTime == null || confirmTime.equals("") ? null : TextUtils.convertDate(confirmTime, "dd/MM/yyyy"));

        pacPatient.setSiteConfirmID(getSiteConfirmID());
        pacPatient.setBloodBaseID(getBloodBaseID());
        pacPatient.setStatusOfTreatmentID(getStatusOfTreatmentID());
        pacPatient.setStartTreatmentTime(startTreatmentTime == null || startTreatmentTime.equals("") ? null : TextUtils.convertDate(startTreatmentTime, "dd/MM/yyyy"));
        pacPatient.setSiteTreatmentFacilityID(getSiteTreatmentFacilityID());
        pacPatient.setTreatmentRegimenID(getTreatmentRegimenID());
        pacPatient.setSymptomID(getSymptomID());
        pacPatient.setDeathTime(getDeathTime() == null || getDeathTime().equals("") ? null : TextUtils.convertDate(getDeathTime(), "dd/MM/yyyy"));
        pacPatient.setRequestDeathTime(getRequestDeathTime() == null || getRequestDeathTime().equals("") ? null : TextUtils.convertDate(getRequestDeathTime(), "dd/MM/yyyy"));
        pacPatient.setNote(getNote());

        //thêm mới
        pacPatient.setVirusLoadConfirm(getVirusLoadConfirm());
        pacPatient.setVirusLoadArv(getVirusLoadArv());
        pacPatient.setEarlyHiv(getEarlyHiv());
        pacPatient.setEarlyHivTime(getEarlyHivTime() == null || getEarlyHivTime().equals("") ? null : TextUtils.convertDate(getEarlyHivTime(), "dd/MM/yyyy"));
        pacPatient.setVirusLoadConfirmDate(getVirusLoadConfirmDate() == null || getVirusLoadConfirmDate().equals("") ? null : TextUtils.convertDate(getVirusLoadConfirmDate(), "dd/MM/yyyy"));
        pacPatient.setVirusLoadArvDate(getVirusLoadArvDate() == null || getVirusLoadArvDate().equals("") ? null : TextUtils.convertDate(getVirusLoadArvDate(), "dd/MM/yyyy"));
        try {
            pacPatient.setCdFourResult(StringUtils.isNotBlank(getCdFourResult()) ? Long.valueOf(getCdFourResult()) : 0);
        } catch (Exception e) {
        }
        pacPatient.setCdFourResultDate(getCdFourResultDate() == null || getCdFourResultDate().equals("") ? null : TextUtils.convertDate(getCdFourResultDate(), "dd/MM/yyyy"));
        pacPatient.setClinicalStage(getClinicalStage());
        pacPatient.setClinicalStageDate(getClinicalStageDate() == null || getClinicalStageDate().equals("") ? null : TextUtils.convertDate(getClinicalStageDate(), "dd/MM/yyyy"));
        pacPatient.setAidsStatus(getAidsStatus());
        pacPatient.setAidsStatusDate(getAidsStatusDate() == null || getAidsStatusDate().equals("") ? null : TextUtils.convertDate(getAidsStatusDate(), "dd/MM/yyyy"));

        pacPatient.setCdFourResultCurrent(StringUtils.isNotEmpty(getCdFourResultCurrent()) ? Long.parseLong(getCdFourResultCurrent()) : null);
        pacPatient.setCdFourResultLastestDate(getCdFourResultLastestDate() == null || getCdFourResultLastestDate().equals("") ? null : TextUtils.convertDate(getCdFourResultLastestDate(), "dd/MM/yyyy"));
        pacPatient.setVirusLoadArvCurrent(getVirusLoadArvCurrent());
        pacPatient.setVirusLoadArvLastestDate(StringUtils.isEmpty(getVirusLoadArvLastestDate()) ? null : TextUtils.convertDate(getVirusLoadArvLastestDate(), "dd/MM/yyyy"));
        pacPatient.setStatusOfChangeTreatmentID(getStatusOfChangeTreatmentID());
        pacPatient.setConfirmCode(getConfirmCode());
        if (StringUtils.isNotEmpty(getChangeTreatmentDate())) {
            pacPatient.setChangeTreatmentDate(TextUtils.convertDate(getChangeTreatmentDate(), "dd/MM/yyyy"));
        }
        pacPatient.setOpcCode(getOpcCode());
        pacPatient.setHasHealthInsurance(getHasHealthInsurance());

        pacPatient.setVirusLoadNumber(getVirusLoadNumber());
        pacPatient.setEarlyBioName(getEarlyBioName());
        pacPatient.setEarlyDiagnose(getEarlyDiagnose());
        pacPatient.setInsuranceExpiry(StringUtils.isEmpty(getInsuranceExpiry()) ? null : TextUtils.convertDate(getInsuranceExpiry(), "dd/MM/yyyy"));
        pacPatient.setFirstTreatmentTime(StringUtils.isEmpty(getFirstTreatmentTime()) ? null : TextUtils.convertDate(getFirstTreatmentTime(), "dd/MM/yyyy"));
        pacPatient.setRegistrationTime(StringUtils.isEmpty(getRegistrationTime()) ? null : TextUtils.convertDate(getRegistrationTime(), "dd/MM/yyyy"));
        pacPatient.setEndTime(StringUtils.isEmpty(getEndTime()) ? null : TextUtils.convertDate(getEndTime(), "dd/MM/yyyy"));
        pacPatient.setFirstTreatmentRegimenId(getFirstTreatmentRegimenId());
        pacPatient.setRegistrationType(getRegistrationType());
        pacPatient.setEndCase(getEndCase());

        return pacPatient;
    }

    /**
     * Chuyển dữ liệu từ entity sang form
     *
     * @param entity
     * @author pdthang
     */
    public void setFormPac(PacPatientInfoEntity entity) {

        String formatDate = "dd/MM/yyyy";
        if (entity == null) {
            entity = new PacPatientInfoEntity();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);

        setID(entity.getID());
        setFullname(entity.getFullname());
        setGenderID(entity.getGenderID());
        setRaceID(entity.getRaceID());
        setYearOfBirth((entity.getYearOfBirth() == null || entity.getYearOfBirth() == 0) ? "" : String.valueOf(entity.getYearOfBirth()));
        setPatientPhone(entity.getPatientPhone());
        setJobID(entity.getJobID());
        setHealthInsuranceNo(entity.getHealthInsuranceNo());
        setIdentityCard(entity.getIdentityCard());
        setObjectGroupID(entity.getObjectGroupID());
        setModeOfTransmissionID(entity.getModeOfTransmissionID());
        setRiskBehaviorID(entity.getRiskBehaviorID());
        if (entity.getCauseOfDeath() != null) {
            List<String> causes = new ArrayList<>();
            for (String string : entity.getCauseOfDeath()) {
                if (string.isEmpty() || "null".equals(string)) {
                    continue;
                }
                causes.add(string);
            }
            setCauseOfDeath(causes);
        }

        setSourceServiceID(entity.getSourceServiceID());

        setPermanentAddressNo(entity.getPermanentAddressNo());
        setPermanentAddressGroup(entity.getPermanentAddressGroup());
        setPermanentAddressStreet(entity.getPermanentAddressStreet());
        setPermanentProvinceID(entity.getPermanentProvinceID());
        setPermanentDistrictID(entity.getPermanentDistrictID());
        setPermanentWardID(entity.getPermanentWardID());

        setCurrentAddressNo(entity.getCurrentAddressNo());
        setCurrentAddressGroup(entity.getCurrentAddressGroup());
        setCurrentAddressStreet(entity.getCurrentAddressStreet());
        setCurrentProvinceID(entity.getCurrentProvinceID());
        setCurrentDistrictID(entity.getCurrentDistrictID());
        setCurrentWardID(entity.getCurrentWardID());

        setStatusOfResidentID(entity.getStatusOfResidentID());
        if (entity.getConfirmTime() != null) {
            setConfirmTime(simpleDateFormat.format(entity.getConfirmTime()));
        }
        if (entity.getAcceptTime() != null) {
            setAcceptTime(entity.getAcceptTime());
        }
        setSiteConfirmID(entity.getSiteConfirmID());
        setBloodBaseID(entity.getBloodBaseID());
        setStatusOfTreatmentID(entity.getStatusOfTreatmentID());
        if (entity.getStartTreatmentTime() != null) {
            setStartTreatmentTime(simpleDateFormat.format(entity.getStartTreatmentTime()));
        }
        setSiteTreatmentFacilityID(entity.getSiteTreatmentFacilityID());
        setTreatmentRegimenID(entity.getTreatmentRegimenID());
        setSymptomID(entity.getSymptomID());
        if (entity.getDeathTime() != null) {
            setDeathTime(simpleDateFormat.format(entity.getDeathTime()));
        }
        if (entity.getRequestDeathTime() != null) {
            setRequestDeathTime(simpleDateFormat.format(entity.getRequestDeathTime()));
        }
        setNote(entity.getNote());

        //thêm mới
        setVirusLoadConfirm(entity.getVirusLoadConfirm());
        setVirusLoadArv(entity.getVirusLoadArv());
        setEarlyHiv(entity.getEarlyHiv());
        if (entity.getVirusLoadConfirmDate() != null) {
            setVirusLoadConfirmDate(simpleDateFormat.format(entity.getVirusLoadConfirmDate()));
        }
        if (entity.getVirusLoadArvDate() != null) {
            setVirusLoadArvDate(simpleDateFormat.format(entity.getVirusLoadArvDate()));
        }
        if (entity.getEarlyHivTime() != null) {
            setEarlyHivTime(simpleDateFormat.format(entity.getEarlyHivTime()));
        }
        setCdFourResult(entity.getCdFourResult() == null || entity.getCdFourResult() == 0 ? "" : String.valueOf(entity.getCdFourResult()));

        if (entity.getCdFourResultDate() != null) {
            setCdFourResultDate(simpleDateFormat.format(entity.getCdFourResultDate()));
        }
        setClinicalStage(entity.getClinicalStage());
        if (entity.getClinicalStageDate() != null) {
            setClinicalStageDate(simpleDateFormat.format(entity.getClinicalStageDate()));
        }
        setAidsStatus(entity.getAidsStatus());
        if (entity.getAidsStatusDate() != null) {
            setAidsStatusDate(("".equals(entity.getAidsStatus()) || entity.getAidsStatus() == null || "1".equals(entity.getAidsStatus())) ? null : simpleDateFormat.format(entity.getAidsStatusDate()));
        }

        // phần thêm mới cập nhật 7/2/2020
        if (entity.getVirusLoadArvLastestDate() != null) {
            setVirusLoadArvLastestDate((simpleDateFormat.format(entity.getVirusLoadArvLastestDate())));
        }
        if (entity.getCdFourResultLastestDate() != null) {
            setCdFourResultLastestDate(simpleDateFormat.format(entity.getCdFourResultLastestDate()));
        }
        setCdFourResultCurrent(entity.getCdFourResultCurrent() == null || entity.getCdFourResultCurrent() == 0 ? "" : String.valueOf(entity.getCdFourResultCurrent()));
        setVirusLoadArvCurrent(entity.getVirusLoadArvCurrent());
        setStatusOfChangeTreatmentID(entity.getStatusOfChangeTreatmentID());
        setConfirmCode(entity.getConfirmCode());
        setAcceptPermanentTime(entity.getAcceptPermanentTime() != null ? TextUtils.formatDate(entity.getAcceptPermanentTime(), formatDate) : "");
        setChangeTreatmentDate(entity.getChangeTreatmentDate() != null ? TextUtils.formatDate(entity.getChangeTreatmentDate(), formatDate) : "");
        setRefParentID(entity.getRefParentID() == null || entity.getRefParentID() == 0 ? "" : entity.getRefParentID().toString());
        setOpcCode(entity.getOpcCode() == null ? "" : entity.getOpcCode().toUpperCase());
        setHasHealthInsurance(entity.getHasHealthInsurance());

        setVirusLoadNumber(entity.getVirusLoadNumber());
        setEarlyBioName(entity.getEarlyBioName());
        setEarlyDiagnose(entity.getEarlyDiagnose());
        if (entity.getInsuranceExpiry() != null) {
            setInsuranceExpiry((simpleDateFormat.format(entity.getInsuranceExpiry())));
        }
        if (entity.getFirstTreatmentTime() != null) {
            setFirstTreatmentTime((simpleDateFormat.format(entity.getFirstTreatmentTime())));
        }
        if (entity.getEndTime() != null) {
            setEndTime((simpleDateFormat.format(entity.getEndTime())));
        }
        if (entity.getRegistrationTime() != null) {
            setRegistrationTime((simpleDateFormat.format(entity.getRegistrationTime())));
        }
        setRegistrationType(entity.getRegistrationType());
        setFirstTreatmentRegimenId(entity.getFirstTreatmentRegimenId());
        setEndCase(entity.getEndCase());
    }
}

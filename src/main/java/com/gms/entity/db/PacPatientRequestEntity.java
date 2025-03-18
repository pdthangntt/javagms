package com.gms.entity.db;

import com.gms.components.StringListConverter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Thông tin bệnh nhân yêu cầu cập nhật của tuyến huyện,xã Duyệt thì copy
 * thông tin sang info, sau đó xoá request Trong trường hợp yêu cầu nhiều lần mà
 * chưa duyệt, thì là chỉnh sửa tại bảng yêu cầu
 */
@Entity
@Table(name = "PAC_PATIENT_REQUEST", indexes = {
    @Index(name = "PAR_PROVINCE_ID", columnList = "PROVINCE_ID")
    ,@Index(name = "PAR_DISTRICT_ID", columnList = "DISTRICT_ID")
    ,@Index(name = "PAR_WARD_ID", columnList = "WARD_ID")
})
@DynamicUpdate
@DynamicInsert
public class PacPatientRequestEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    private Long ID; //Mã ID bệnh nhân info

    @Column(name = "FULLNAME", length = 50, nullable = false)
    private String fullname;

    @Column(name = "RACE_ID", length = 50, nullable = true)
    private String raceID;

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID; //Giới tính

    @Column(name = "YEAR_OF_BIRTH", nullable = true)
    private Integer yearOfBirth;

    @Column(name = "IDENTITY_CARD", length = 50, nullable = true)
    private String identityCard; //Chứng minh thư nhân dân

    @Column(name = "INSURANCE_NO", length = 50, nullable = true)
    private String insuranceNo; //Thẻ bảo hiểm

    @Column(name = "JOB_ID", length = 50, nullable = true)
    private String jobID; //công việc

    @Column(name = "STATUS_OF_RESIDENT_ID", length = 50, nullable = true)
    private String statusOfResidentID; //Hiện trạng cư trú

    @Column(name = "PERMANENT_ADDRESS_NO", length = 50, nullable = true)
    private String permanentAddressNo; //Địa chỉ thường trú

    @Column(name = "PERMANENT_ADDRESS_GROUP", length = 50, nullable = true)
    private String permanentAddressGroup; //Tổ

    @Column(name = "PERMANENT_ADDRESS_STREET", length = 50, nullable = true)
    private String permanentAddressStreet; //Đường phố

    @Column(name = "PERMANENT_PROVINCE_ID", length = 5, nullable = true)
    private String permanentProvinceID;

    @Column(name = "PERMANENT_DISTRICT_ID", length = 5, nullable = true)
    private String permanentDistrictID;

    @Column(name = "PERMANENT_WARD_ID", length = 5, nullable = true)
    private String permanentWardID;

    @Column(name = "CURRENT_ADDRESS_NO", length = 50, nullable = true)
    private String currentAddressNo; //Địa chỉ cư trú

    @Column(name = "CURRENT_ADDRESS_GROUP", length = 50, nullable = true)
    private String currentAddressGroup; //Tổ

    @Column(name = "CURRENT_ADDRESS_STREET", length = 50, nullable = true)
    private String currentAddressStreet; //Đường phố

    @Column(name = "CURRENT_PROVINCE_ID", length = 5, nullable = true)
    private String currentProvinceID;

    @Column(name = "CURRENT_DISTRICT_ID", length = 5, nullable = true)
    private String currentDistrictID;

    @Column(name = "CURRENT_WARD_ID", length = 5, nullable = true)
    private String currentWardID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DEATH_TIME", nullable = true)
    private Date deathTime; //Ngày tử vong

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUEST_DEATH_TIME", nullable = true)
    private Date requestDeathTime; //Ngày bao tử vong

    @Column(name = "CAUSE_OF_DEATH", length = 220, nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> causeOfDeath; //Nguyên nhân tử vong

    @Column(name = "PROVINCE_ID", length = 5, nullable = false)
    private String provinceID; //Tỉnh thành quản lý

    @Column(name = "DISTRICT_ID", length = 5, nullable = true)
    private String districtID; //Quận huyện quản lý

    @Column(name = "WARD_ID", length = 5, nullable = true)
    private String wardID; //Phường xã quản lý

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "note", length = 500, nullable = true)
    private String note; //Ghi chú

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "PATIENT_PHONE", length = 13, nullable = true)
    private String patientPhone; //Số điện thoại bệnh nhân

    @Column(name = "MODE_OF_TRANSMISSION_ID", length = 50, nullable = true)
    private String modeOfTransmissionID; //Đưòng lây

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TREATMENT_TIME", nullable = true)
    private Date startTreatmentTime; //Ngày bắt đầu điều trị 

    @Column(name = "SITE_TREATMENT_FACILITY_ID", length = 50, nullable = true)
    private String siteTreatmentFacilityID; //Cơ sở điều trị - liên kết với treatment-facility

    @Column(name = "TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String treatmentRegimenID; //Phác đồ điều trị - liên kết với treatment-regimen

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHANGE_TREATMENT_DATE", nullable = true)
    private Date changeTreatmentDate; // Ngày biến động điều trị

    @Column(name = "STATUS_OF_CHANGE_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfChangeTreatmentID; //Trạng thái biến động điều trị

    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getModeOfTransmissionID() {
        return modeOfTransmissionID;
    }

    public void setModeOfTransmissionID(String modeOfTransmissionID) {
        this.modeOfTransmissionID = modeOfTransmissionID;
    }

    public Date getStartTreatmentTime() {
        return startTreatmentTime;
    }

    public void setStartTreatmentTime(Date startTreatmentTime) {
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

    public Date getChangeTreatmentDate() {
        return changeTreatmentDate;
    }

    public void setChangeTreatmentDate(Date changeTreatmentDate) {
        this.changeTreatmentDate = changeTreatmentDate;
    }

    public String getStatusOfChangeTreatmentID() {
        return statusOfChangeTreatmentID;
    }

    public void setStatusOfChangeTreatmentID(String statusOfChangeTreatmentID) {
        this.statusOfChangeTreatmentID = statusOfChangeTreatmentID;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public Date getRequestDeathTime() {
        return requestDeathTime;
    }

    public void setRequestDeathTime(Date requestDeathTime) {
        this.requestDeathTime = requestDeathTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
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

    public Date getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(Date deathTime) {
        this.deathTime = deathTime;
    }

    public List<String> getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(List<String> causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
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

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Long getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}

package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Bệnh án
 */
@Entity
@Table(name = "PQM_ARV",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CODE", "SITE_ID"})
        },
        indexes = {
            @Index(name = "OS_ID", columnList = "ID"),
            @Index(name = "OS_CODE", columnList = "CODE"),
            @Index(name = "OS_DOB", columnList = "DOB"),
            @Index(name = "OS_END_TIME", columnList = "END_TIME"),
            @Index(name = "OS_EXAMINATION_TIME", columnList = "EXAMINATION_TIME"),
            @Index(name = "OS_GENDER_ID", columnList = "GENDER_ID"),
            @Index(name = "OS_IDENTITY_CARD", columnList = "IDENTITY_CARD"),
            @Index(name = "OS_INH_FROM_TIME", columnList = "INH_FROM_TIME"),
            @Index(name = "OS_INSURANCE_NO", columnList = "INSURANCE_NO"),
            @Index(name = "OS_OBJECT_GROUP_ID", columnList = "OBJECT_GROUP_ID"),
            @Index(name = "OS_PATIENT_PHONE", columnList = "PATIENT_PHONE"),
            @Index(name = "OS_PERMANENT_ADDRESS", columnList = "PERMANENT_ADDRESS"),
            @Index(name = "OS_REGISTRATION_TIME", columnList = "REGISTRATION_TIME"),
            @Index(name = "OS_SITE_ID", columnList = "SITE_ID"),
            @Index(name = "OS_STATUS_OF_TREATMENT_ID", columnList = "STATUS_OF_TREATMENT_ID"),
            @Index(name = "OS_TEST_TIME", columnList = "TEST_TIME"),
            @Index(name = "OS_TREATMENT_TIME", columnList = "TREATMENT_TIME")

        })
@DynamicUpdate
@DynamicInsert
public class PqmArvEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý

    @Column(name = "CODE", length = 50, nullable = false)
    private String code; //Mã

    @Column(name = "FULLNAME", length = 100)
    private String fullName; //Họ và tên

    @Column(name = "DOB", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob; //Ngày/tháng/năm sinh

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID; //Giới tính

    @Column(name = "IDENTITY_CARD", length = 100)
    private String identityCard; //Chứng minh thư

    @Column(name = "PATIENT_PHONE", length = 100, nullable = true)
    private String patientPhone; //Số điện thoại bệnh nhân

    @Column(name = "INSURANCE_NO", length = 100)
    private String insuranceNo; //BHYT

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "PERMANENT_ADDRESS", length = 500)
    private String permanentAddress; // Địa chỉ 

    @Column(name = "CURRENT_ADDRESS", length = 500)
    private String currentAddress; // Địa chỉ 

    //Cac truong de lay thong tin moi nhat
//    stage:
    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TREATMENT_TIME", nullable = true)
    private Date treatmentTime; //Ngày ARV

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_TREATMENT_TIME", nullable = true)
    private Date firstTreatmentTime; //Ngày bắt đầu điều trị ARV

    @Column(name = "YEAR")
    private Integer year; // Năm sinh

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_TIME", nullable = true)
    private Date registrationTime; //NGày đăng ký vào OPC

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", nullable = true)
    private Date endTime; // Ngày kết thúc tại OPC
    //visit
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXAMINATION_TIME", nullable = true)
    private Date examinationTime; //Ngày khám bệnh
    //visral
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TEST_TIME", nullable = true)
    private Date testTime; // TLVR - Ngày xét nghiệm
    //test
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INH_FROM_TIME", nullable = true)
    private Date inhFromTime; //Lao từ ngày

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Column(name = "PERMANENT_PROVINCE_ID", length = 5, nullable = true)
    private String permanentProvinceID;

    @Column(name = "PERMANENT_DISTRICT_ID", length = 5, nullable = true)
    private String permanentDistrictID;

    @Column(name = "PERMANENT_WARD_ID", length = 5, nullable = true)
    private String permanentWardID;

    @Column(name = "CURRENT_PROVINCE_ID", length = 5, nullable = true)
    private String currentProvinceID;

    @Column(name = "CURRENT_DISTRICT_ID", length = 5, nullable = true)
    private String currentDistrictID;

    @Column(name = "CURRENT_WARD_ID", length = 5, nullable = true)
    private String currentWardID;

    @Column(name = "SITE_CONFIRM", length = 50, nullable = true)
    private String siteConfirm; //Cơ sở XN khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONFIRM_TIME", insertable = true, updatable = true, nullable = true)
    private Date confirmTime;

    @Column(name = "SOURCE", length = 50, nullable = true)
    private String source;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY")
    private Long updatedBY;

    @Column(name = "month_report", columnDefinition = "integer default 0")
    private Integer month_report;

    @Column(name = "year_report", columnDefinition = "integer default 0")
    private Integer year_report;

    @Column(name = "quantity", columnDefinition = "integer default 0")
    private Integer quantity;

    @Column(name = "patient_id", length = 50, nullable = true)
    private String patient_id;

    @Column(name = "province_id", length = 50, nullable = true)
    private String province_id;

    @Column(name = "customer_system_id", length = 50, nullable = true)
    private String customer_system_id;

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public Integer getMonth_report() {
        return month_report;
    }

    public void setMonth_report(Integer month_report) {
        this.month_report = month_report;
    }

    public Integer getYear_report() {
        return year_report;
    }

    public void setYear_report(Integer year_report) {
        this.year_report = year_report;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getCustomer_system_id() {
        return customer_system_id;
    }

    public void setCustomer_system_id(String customer_system_id) {
        this.customer_system_id = customer_system_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getSiteConfirm() {
        return siteConfirm;
    }

    public void setSiteConfirm(String siteConfirm) {
        this.siteConfirm = siteConfirm;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getFirstTreatmentTime() {
        return firstTreatmentTime;
    }

    public void setFirstTreatmentTime(Date firstTreatmentTime) {
        this.firstTreatmentTime = firstTreatmentTime;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public Date getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(Date treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(Date examinationTime) {
        this.examinationTime = examinationTime;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public Date getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(Date inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public Long getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(Long updatedBY) {
        this.updatedBY = updatedBY;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
    }
}

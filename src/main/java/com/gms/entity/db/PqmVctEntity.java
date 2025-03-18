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
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "PQM_VCT",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID"),
            @Index(name = "OS_ADVISORY_TIME", columnList = "ADVISORY_TIME"),
            @Index(name = "OS_ARRIVAL_TIME", columnList = "ARRIVAL_TIME"),
            @Index(name = "OS_CODE", columnList = "CODE"),
            @Index(name = "OS_CONFIRM_RESULTS_ID", columnList = "CONFIRM_RESULTS_ID"),
            @Index(name = "OS_CONFIRM_TEST_NO", columnList = "CONFIRM_TEST_NO"),
            @Index(name = "OS_CONFIRM_TIME", columnList = "CONFIRM_TIME"),
            @Index(name = "OS_EARLY_DIAGNOSE", columnList = "EARLY_DIAGNOSE"),
            @Index(name = "OS_EARLY_HIV_DATE", columnList = "EARLY_HIV_DATE"),
            @Index(name = "OS_EXCHANGE_TIME", columnList = "EXCHANGE_TIME"),
            @Index(name = "OS_GENDER_ID", columnList = "GENDER_ID"),
            @Index(name = "OS_OBJECT_GROUP_ID", columnList = "OBJECT_GROUP_ID"),
            @Index(name = "OS_PRE_TEST_TIME", columnList = "PRE_TEST_TIME"),
            @Index(name = "OS_REGISTER_THERAPY_TIME", columnList = "REGISTER_THERAPY_TIME"),
            @Index(name = "OS_REGISTER_THERAPY_SITE", columnList = "REGISTER_THERAPY_SITE"),
            @Index(name = "OS_RESULTS_SITE_TIME", columnList = "RESULTS_SITE_TIME"),
            @Index(name = "OS_RESULTS_TIME", columnList = "RESULTS_TIME"),
            @Index(name = "OS_SITE_ID", columnList = "SITE_ID"),
            @Index(name = "OS_TEST_RESULTS_ID", columnList = "TEST_RESULTS_ID"),
            @Index(name = "OS_THERAPY_NO", columnList = "THERAPY_NO"),
            @Index(name = "OS_YEAR_OF_BIRTH", columnList = "YEAR_OF_BIRTH"),
            @Index(name = "OS_IDENTITY_CARD", columnList = "IDENTITY_CARD"),
            @Index(name = "OS_INSURANCE_NO", columnList = "INSURANCE_NO"),
            @Index(name = "OS_PATIENT_PHONE", columnList = "PATIENT_PHONE"),
            @Index(name = "OS_BLOCKED", columnList = "BLOCKED"),
            @Index(name = "OS_SOURCE", columnList = "SOURCE")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"SITE_ID", "CODE"}, name = "pqm_vct_unique_site_code")
        })
public class PqmVctEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "CODE", length = 50, nullable = false)
    private String code;

    @Column(name = "PATIENT_NAME", length = 100, nullable = true)
    private String patientName;

    @Column(name = "PATIENT_PHONE", length = 100, nullable = true)
    private String patientPhone; //Số điện thoại bệnh nhân

    @Column(name = "PATIENT_ID", length = 50, nullable = true)
    private String patientID;

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID;

    @Column(name = "YEAR_OF_BIRTH", length = 4, nullable = true)
    private String yearOfBirth;

    @Column(name = "IDENTITY_CARD", length = 100, nullable = true)
    private String identityCard; //Chứng minh thư nhân dân

    @Column(name = "INSURANCE_NO", length = 100)
    private String insuranceNo; //BHYT

    @Column(name = "PERMANENT_ADDRESS", length = 500, nullable = true)
    private String permanentAddress; //Địa chỉ thường trú

    @Column(name = "CURRENT_ADDRESS", length = 500, nullable = true)
    private String currentAddress; //Địa chỉ cư trú

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "SOURCE", length = 50, nullable = true)
    private String source; //Nguon lay du lieu

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADVISORY_TIME", nullable = true)
    private Date advisoryeTime; //Ngày tư vấn

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRE_TEST_TIME", nullable = true)
    private Date preTestTime; //Ngày xét nghiệm sàng lọc

    @Column(name = "TEST_RESULTS_ID", length = 50, nullable = true)
    private String testResultsID; //Kết quả xét nghiệm sàng lọc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONFIRM_TIME", nullable = true)
    private Date confirmTime; //Ngày xét nghiệm khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESULTS_SITE_TIME", nullable = true)
    private Date resultsSiteTime; //Ngày cơ sở nhận kết quả xn khẳng định

    @Column(name = "CONFIRM_TEST_NO", length = 100, nullable = true)
    private String confirmTestNo; //Mã xn khẳng định

    @Column(name = "CONFIRM_RESULTS_ID", length = 50, nullable = true)
    private String confirmResultsID; //Kết quả xét nghiệm khẳng định

    @Temporal(TemporalType.DATE)
    @Column(name = "RESULTS_TIME", nullable = true)
    private Date resultsTime; //Ngày trả kết quả xét nghiệm

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXCHANGE_TIME", nullable = true)
    private Date exchangeTime; // Ngày chuyển gửi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ARRIVAL_TIME", nullable = true)
    private Date arrivalTime; // Ngày tiếp nhận

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTER_THERAPY_TIME", nullable = true)
    private Date registerTherapyTime; // Ngày đăng ký điều trị

    @Column(name = "REGISTER_THERAPY_SITE", length = 220, nullable = true)
    private String registeredTherapySite; // Tên cơ sở mà khách hành đã đăng ký điều trị

    @Column(name = "THERAPY_NO", length = 100, nullable = true)
    private String therapyNo; // Mã số điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EARLY_HIV_DATE")
    private Date earlyHivDate; // Ngày xn moi nhiem

    @Column(name = "EARLY_DIAGNOSE", length = 50, nullable = true)
    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY", nullable = false)
    private Long updatedBY;

    @Column(name = "blocked", columnDefinition = "integer default 0")
    private Integer blocked; // 1 là khoá

    @Column(name = "note", columnDefinition = "TEXT", nullable = true)
    private String note;

    //Thêm theo yêu cầu
    @Column(name = "province_code", length = 50, nullable = true)
    private String province_code;

    @Column(name = "month", columnDefinition = "integer default 0")
    private Integer month;

    @Column(name = "year", columnDefinition = "integer default 0")
    private Integer year;

    @Column(name = "quantity", columnDefinition = "integer default 0")
    private Integer quantity;

    @Column(name = "service_type", length = 50, nullable = true)
    private String service_type;

    @Column(name = "district1", length = 50, nullable = true)
    private String district1;

    @Column(name = "province1", length = 50, nullable = true)
    private String province1;

    @Column(name = "district2", length = 50, nullable = true)
    private String district2;

    @Column(name = "province2", length = 50, nullable = true)
    private String province2;

    @Column(name = "data_sharing_acceptance", columnDefinition = "integer default 0")
    private Integer data_sharing_acceptance;

    @Column(name = "customer_id", length = 50, nullable = true)
    private String customer_id;

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getDistrict1() {
        return district1;
    }

    public void setDistrict1(String district1) {
        this.district1 = district1;
    }

    public String getProvince1() {
        return province1;
    }

    public void setProvince1(String province1) {
        this.province1 = province1;
    }

    public String getDistrict2() {
        return district2;
    }

    public void setDistrict2(String district2) {
        this.district2 = district2;
    }

    public String getProvince2() {
        return province2;
    }

    public void setProvince2(String province2) {
        this.province2 = province2;
    }

    public Integer getData_sharing_acceptance() {
        return data_sharing_acceptance;
    }

    public void setData_sharing_acceptance(Integer data_sharing_acceptance) {
        this.data_sharing_acceptance = data_sharing_acceptance;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
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

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public Date getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(Date advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }

    public Date getPreTestTime() {
        return preTestTime;
    }

    public void setPreTestTime(Date preTestTime) {
        this.preTestTime = preTestTime;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getResultsSiteTime() {
        return resultsSiteTime;
    }

    public void setResultsSiteTime(Date resultsSiteTime) {
        this.resultsSiteTime = resultsSiteTime;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public Date getResultsTime() {
        return resultsTime;
    }

    public void setResultsTime(Date resultsTime) {
        this.resultsTime = resultsTime;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getRegisterTherapyTime() {
        return registerTherapyTime;
    }

    public void setRegisterTherapyTime(Date registerTherapyTime) {
        this.registerTherapyTime = registerTherapyTime;
    }

    public String getRegisteredTherapySite() {
        return registeredTherapySite;
    }

    public void setRegisteredTherapySite(String registeredTherapySite) {
        this.registeredTherapySite = registeredTherapySite;
    }

    public String getTherapyNo() {
        return therapyNo;
    }

    public void setTherapyNo(String therapyNo) {
        this.therapyNo = therapyNo;
    }

    public Date getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(Date earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
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
    public PqmVctEntity clone() throws CloneNotSupportedException {
        return (PqmVctEntity) super.clone();
    }

}

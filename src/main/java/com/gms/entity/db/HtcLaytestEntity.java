package com.gms.entity.db;

import com.gms.components.StringListConverter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvThành
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "HTC_LAYTEST",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"SITE_ID", "CODE"})
        }
)
public class HtcLaytestEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "CODE", length = 50, nullable = false)
    private String code;

    @Transient
    private String permanentAddressFull;    //Nơi đăng ký hộ khẩu thường trú

    @Transient
    private String currentAddressFull;     //Nơi cư trú

    @Column(name = "SERVICE_ID", length = 50, nullable = false)
    private String serviceID;

    @Column(name = "PATIENT_NAME", length = 100, nullable = true)
    private String patientName;

    @Column(name = "PATIENT_PHONE", length = 50, nullable = true)
    private String patientPhone;

    @Column(name = "PATIENT_ID", length = 50, nullable = true)
    private String patientID;

    @Column(name = "RACE_ID", length = 50, nullable = true)
    private String raceID;

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID;

    @Column(name = "YEAR_OF_BIRTH", length = 4, nullable = true)
    private Integer yearOfBirth;

    @Column(name = "PERMANENT_ADDRESS", length = 500, nullable = true)
    private String permanentAddress; //Địa chỉ thường trú

    @Column(name = "PERMANENT_PROVINCE", length = 5, nullable = true)
    private String permanentProvinceID;

    @Column(name = "PERMANENT_DISTRICT", length = 5, nullable = true)
    private String permanentDistrictID;

    @Column(name = "PERMANENT_WARD", length = 5, nullable = true)
    private String permanentWardID;

    @Column(name = "CURRENT_ADDRESS", length = 500, nullable = true)
    private String currentAddress; //Địa chỉ cư trú

    @Column(name = "CURRENT_PROVINCE_ID", length = 5, nullable = true)
    private String currentProvinceID;

    @Column(name = "CURRENT_DISTRICT_ID", length = 5, nullable = true)
    private String currentDistrictID;

    @Column(name = "CURRENT_WARD_ID", length = 5, nullable = true)
    private String currentWardID;

    @Column(name = "JOB_ID", length = 50, nullable = true)
    private String jobID;

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "RISK_BEHAVIOR_ID", length = 50, nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> riskBehaviorID; //Hành vi nguy cơ lây nhiễm

    @Column(name = "TEST_RESULTS_ID", length = 50, nullable = true)
    private String testResultsID; //Kết quả xét nghiệm

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADVISORY_TIME", nullable = true)
    private Date advisoryeTime; //Ngày tư vấn

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCEPT_DATE", nullable = true)
    private Date acceptDate; //Ngày tiếp nhận

    @Column(name = "IS_AGREE_TEST", nullable = true)
    private Boolean isAgreeTest;

    @Column(name = "SITE_VISIT_ID", length = 50, nullable = true)
    private String siteVisitID; // Cơ sở xét nghiệm sàng lọc

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

    @Column(name = "IS_REMOVE")
    private boolean remove;

    @Column(name = "IS_VISIT_REMOVE") // dánh dấu xóa htc laytest
    private boolean visitRemove;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SAMPLE_SENT_DATE", nullable = true)
    private Date sampleSentDate; // Ngày gửi mẫu máu (Ngày chuyển từ không chuyên sang sàng lọc)

    @Column(name = "HAS_TEST_BEFORE", nullable = true)
    private Boolean hasTestBefore;  // Đã từng tham gia xét nghiệm HIV chưa

    @Column(name = "MOST_RECENT_TEST", length = 50, nullable = true)
    private String mostRecentTest; // Lần test gần nhất

    @Column(name = "PROJECT_ID", insertable = true, updatable = false, nullable = true, length = 50)
    private String projectID; //Dự án tài trợ cho cơ sở

    @Column(name = "REFERRAL_SOURCE", length = 50, nullable = true)
    private String referralSource; //Nguồn giới thiệu

    @Column(name = "YOU_INJECT_CODE", length = 50, nullable = true)
    private String youInjectCode; //Mã số bạn tình, bạn chích ( mã số nguồn giới thiệu)

    @Column(name = "BIO_NAME", length = 50, nullable = true)
    private String bioName; // Tên sinh phẩm

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONFIRM_TIME")
    private Date confirmTime; // Ngày XN khẳng định

    @Column(name = "CONFIRM_RESULTS_ID", length = 50, nullable = true)
    private String confirmResultsID; //Kết quả xét nghiệm khẳng định

    @Column(name = "SITE_CONFIRM_TEST", length = 220, nullable = true)
    private String siteConfirmTest; // Cơ sở xét nghiệm khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXCHANGE_TIME", nullable = true)
    private Date exchangeTime; // Ngày chuyển gửi điều trị 

    @Column(name = "ARRIVAL_SITE", nullable = true)
    private String arrivalSite; // Tên cơ sở điều trị khách hàng được chuyển đến

    @Column(name = "EXCHANGE_STATUS", length = 50, nullable = true)
    private String exchangeStatus; // Kết quả chuyển gửi

    @Column(name = "CONTROL_LINE", length = 50, nullable = true)
    private String controlLine; // Vạch chứng

    @Column(name = "TEST_LINE", length = 50, nullable = true)
    private String testLine; // Keest quar vạch chứng

    @Column(name = "REGISTER_THERAPY_SITE", length = 220, nullable = true)
    private String registeredTherapySite; // Tên cơ sở mà khách hành đã đăng ký điều trị ( Nơi đăng ký ddiefu trị)

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTER_THERAPY_TIME", nullable = true)
    private Date registerTherapyTime; // Ngày đăng ký điều trị

    @Column(name = "NOTE", nullable = false, length = 255)
    private String note; // Ghi chú

    @Column(name = "PERMANENT_ADDRESS_STREET", length = 500, nullable = true)
    private String permanentAddressStreet; // Đường phố thương trú

    @Column(name = "PERMANENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String permanentAddressGroup; // Tổ ấp khu phố thường trú

    @Column(name = "CURRENT_ADDRESS_STREET", length = 500, nullable = true)
    private String currentAddressStreet; // Đường phố hiện tại

    @Column(name = "CURRENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String currentAddressGroup; // Tổ ấp khu phố hiện tại

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public String getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(String exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    public String getRegisteredTherapySite() {
        return registeredTherapySite;
    }

    public void setRegisteredTherapySite(String registeredTherapySite) {
        this.registeredTherapySite = registeredTherapySite;
    }

    public Date getRegisterTherapyTime() {
        return registerTherapyTime;
    }

    public void setRegisterTherapyTime(Date registerTherapyTime) {
        this.registerTherapyTime = registerTherapyTime;
    }

    public String getControlLine() {
        return controlLine;
    }

    public void setControlLine(String controlLine) {
        this.controlLine = controlLine;
    }

    public String getTestLine() {
        return testLine;
    }

    public void setTestLine(String testLine) {
        this.testLine = testLine;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public String getSiteConfirmTest() {
        return siteConfirmTest;
    }

    public void setSiteConfirmTest(String siteConfirmTest) {
        this.siteConfirmTest = siteConfirmTest;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getBioName() {
        return bioName;
    }

    public void setBioName(String bioName) {
        this.bioName = bioName;
    }

    public String getYouInjectCode() {
        return youInjectCode;
    }

    public void setYouInjectCode(String youInjectCode) {
        this.youInjectCode = youInjectCode;
    }

    public String getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(String referralSource) {
        this.referralSource = referralSource;
    }

    public boolean isVisitRemove() {
        return visitRemove;
    }

    public void setVisitRemove(boolean visitRemove) {
        this.visitRemove = visitRemove;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
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

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
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

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public Date getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(Date advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }

    public Boolean getIsAgreeTest() {
        return isAgreeTest;
    }

    public void setIsAgreeTest(Boolean isAgreeTest) {
        this.isAgreeTest = isAgreeTest;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getSiteVisitID() {
        return siteVisitID;
    }

    public void setSiteVisitID(String siteVisitID) {
        this.siteVisitID = siteVisitID;
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

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public Date getSampleSentDate() {
        return sampleSentDate;
    }

    public void setSampleSentDate(Date sampleSentDate) {
        this.sampleSentDate = sampleSentDate;
    }

    public Boolean getHasTestBefore() {
        return hasTestBefore;
    }

    public void setHasTestBefore(Boolean hasTestBefore) {
        this.hasTestBefore = hasTestBefore;
    }

    public String getMostRecentTest() {
        return mostRecentTest;
    }

    public void setMostRecentTest(String mostRecentTest) {
        this.mostRecentTest = mostRecentTest;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
        ignoreAttributes.add("code");
    }

}

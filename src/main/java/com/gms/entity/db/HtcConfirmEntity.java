package com.gms.entity.db;

import com.gms.components.StringListConverter;
import com.gms.entity.constant.HtcConfirmStatusEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Entity tạo bảng HTC_CONFIRM
 *
 * @author TrangBN
 */
@Entity
@Table(
        name = "HTC_CONFIRM",
        indexes = {
            @Index(name = "HTC_CONFIRM_SITE", columnList = "SITE_ID")
            ,@Index(name = "HTC_CONFIRM_VISIT", columnList = "SOURCE_ID")
            ,@Index(name = "HTC_CONFIRM_SITE", columnList = "SOURCE_SITE_ID")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CODE"}, name = "htc_confirm_unique_code")
        }
)
@DynamicInsert
@DynamicUpdate
public class HtcConfirmEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; // Mã cơ sở hiện tại

    @Column(name = "FULL_NAME", length = 100)
    private String fullname; // Họ và tên đầy đủ

    @Column(name = "YEAR")
    private Integer year; // Năm sinh

    @Column(name = "GENDER_ID", length = 50)
    private String genderID; // Mã giới tính

    @Column(name = "WARD_ID", length = 5)
    private String wardID; // Mã xã phường

    @Column(name = "DISTRICT_ID", length = 5)
    private String districtID; // Mã quận huyện

    @Column(name = "PROVINCE_ID", length = 5)
    private String provinceID; // Mã tỉnh thành

    @Column(name = "ADDRESS", length = 500)
    private String address; // Địa chỉ 

    @Column(name = "OBJECT_GROUP_ID", length = 50)
    private String objectGroupID; // Mã nhóm đối tượng 

    @Column(name = "CODE", length = 100)
    private String code; // Mã XN khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESULTS_RETURN_TIME")
    private Date resultsReturnTime; // Ngày trả kết quả XN khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONFIRM_TIME")
    private Date confirmTime; // Ngày XN khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SAMPLE_RECEIVE_TIME")
    private Date sampleReceiveTime; // Ngày nhận mẫu

    @Column(name = "TEST_STAFF_ID", length = 100)
    private String testStaffId; // Cán bộ XN khẳng định

    @Column(name = "BLOOD_SAMPLE", length = 100)
    private String bloodSample; // Mẫu máu

    @Column(name = "RESULTS_ID", length = 50)
    private String resultsID; // Kết quả XN khẳng định

    @Column(name = "PATIENT_ID", length = 50)
    private String patientID; // CMND

    @Column(name = "SOURCE_ID")
    private String sourceID; // Mã khách hàng tai nguon gui mau

    @Column(name = "IS_REMOVE")
    private Boolean remove; // Trạng thái đã xóa

    @Column(name = "SOURCE_SITE_ID")
    private Long sourceSiteID; // Mã nơi nguồn gửi mẫu

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCEPT_DATE")
    private Date acceptDate; // Ngày tiếp nhận

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_AT")
    private Date createdAt; // Ngày tạo bản ghi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_AT")
    private Date updatedAt; // Ngày cập nhật bản ghi

    @Column(name = "CREATE_BY")
    private Long createdBy; // Người tạo bản ghi

    @Column(name = "SAMPLE_QUALITY", length = 50, nullable = true)
    private String sampleQuality; // Chất lượng mẫu

    @Column(name = "BIO_NAME_1", length = 50, nullable = true)
    private String bioName1; // Tên sinh phẩm 1

    @Column(name = "BIO_NAME_RESULT_1", length = 50, nullable = true)
    private String bioNameResult1; // Kết quả sinh phẩm 1

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_BIO_DATE")
    private Date firstBioDate; // Ngày xét nghiệm sinh phẩm 1

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SECOND_BIO_DATE")
    private Date secondBioDate; // Ngày xét nghiệm sinh phẩm 2

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "THIRD_BIO_DATE")
    private Date thirdBioDate; // Ngày xét nghiệm sinh phẩm 3

    @Column(name = "BIO_NAME_2", length = 50, nullable = true)
    private String bioName2; // Tên sinh phẩm 2

    @Column(name = "BIO_NAME_RESULT_2", length = 50, nullable = true)
    private String bioNameResult2; // Kết quả sinh phẩm 2

    @Column(name = "BIO_NAME_3", length = 50, nullable = true)
    private String bioName3; // Tên sinh phẩm 3

    @Column(name = "BIO_NAME_RESULT_3", length = 50, nullable = true)
    private String bioNameResult3; // Kết quả sinh phẩm 3

    @Column(name = "TEST_RESULTS_ID", length = 50, nullable = true)
    private String testResultsID; //Kết quả XN sàng lọc

    @Column(name = "SOURCE_SERVICE_ID", length = 50, nullable = true)
    private String sourceServiceID; // Dịch vụ nơi gửi mẫu

    @Column(name = "OTHER_TECHNICAL", length = 50, nullable = true)
    private String otherTechnical; // Kỹ thuật khác

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SOURCE_RECEIVE_SAMPLE_TIME")
    private Date sourceReceiveSampleTime; // Ngày lấy mẫu tại cơ sở XN sàng lọc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SOURCE_SAMPLE_DATE")
    private Date sourceSampleDate; // Ngày cơ sở sàng lọc gửi mẫu

    @Column(name = "UPDATE_BY")
    private Long updatedBy; // Người cập nhật

    @Column(name = "PROJECT_ID", insertable = true, updatable = false, nullable = true, length = 50)
    private String projectID; //Dự án tài trợ cho cơ sở

    @Column(name = "FEEDBACK_STATUS")
    private String feedbackStatus; // Phản hồi 

    @Column(name = "FEEDBACK_MESSAGE")
    private String feedbackMessage; // Lý do tiếp nhận

    @Column(name = "EARLY_HIV", length = 50, nullable = true)
    private String earlyHiv; //kq xn moi nhiem

    @Column(name = "VIRUS_LOAD", length = 50, nullable = true)
    private String virusLoad; //Tải lượng virut

    @Column(name = "PERMANENT_ADDRESS_STREET", length = 500, nullable = true)
    private String permanentAddressStreet; // Đường phố thương trú

    @Column(name = "PERMANENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String permanentAddressGroup; // Tổ ấp khu phố thường trú

    @Column(name = "CURRENT_ADDRESS_STREET", length = 500, nullable = true)
    private String currentAddressStreet; // Đường phố hiện tại

    @Column(name = "CURRENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String currentAddressGroup; // Tổ ấp khu phố hiện tại

    @Column(name = "VIRUS_LOAD_NUMBER", length = 50, nullable = true)
    private String virusLoadNumber; //Kết quả xét nghiệm TLVR - nhập số

    @Column(name = "EARLY_BIO_NAME", length = 50, nullable = true)
    private String earlyBioName; // Tên sinh phẩm nhiễm mới

    @Column(name = "EARLY_DIAGNOSE", length = 50, nullable = true)
    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    @Column(name = "RACE_ID", length = 50, nullable = true)
    private String raceID;

    @Column(name = "PATIENT_PHONE", length = 50, nullable = true)
    private String patientPhone;

    @Column(name = "JOB_ID", length = 50, nullable = true)
    private String jobID;

    @Column(name = "RISK_BEHAVIOR_ID", length = 50, nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> riskBehaviorID; //Hành vi nguy cơ lây nhiễm

    @Transient
    private String addressFull; // Địa chỉ đầy đủ

    @Transient
    private String currentAddressFull; // Địa chỉ hiện tại đầy đủ

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAC_SENT_DATE", nullable = true)
    private Date pacSentDate;   // Trạng thái chuyển gửi GSPH căn cứ vào ngày này

    @Column(name = "PAC_PATIENT_ID", nullable = true)
    private Long pacPatientID;   // Id của bệnh nhân quản lý người nhiễm

    @Column(name = "SAMPLE_SAVE_CODE", length = 20)
    private String sampleSaveCode; // Mã số lưu mẫu xét nghiệm, dương tính cho nhập thêm trường này

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EARLY_HIV_DATE")
    private Date earlyHivDate; // Ngày xn Nhiễm mới

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIRUS_LOAD_DATE")
    private Date virusLoadDate; // Ngày xét nghiệm tải lượng virus

    @Column(name = "SAMPLE_SENT_SOURCE", length = 500)
    private String sampleSentSource; // Nơi gửi bệnh phẩm

    @Column(name = "SERVICE_ID", length = 50, nullable = true)
    private String serviceID;

    @Column(name = "CURRENT_ADDRESS", length = 500, nullable = true)
    private String currentAddress; //Địa chỉ cư trú

    @Column(name = "CURRENT_PROVINCE_ID", length = 5, nullable = true)
    private String currentProvinceID;

    @Column(name = "CURRENT_DISTRICT_ID", length = 5, nullable = true)
    private String currentDistrictID;

    @Column(name = "CURRENT_WARD_ID", length = 5, nullable = true)
    private String currentWardID;

    @Column(name = "MODE_OF_TRANSMISSION", length = 50, nullable = true)
    private String modeOfTransmission; // Đường lây

    @Column(name = "QR_CODE", length = 36, nullable = true)
    private String qrcode; //Mã QR

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUEST_CONFIRM_TIME")
    private Date requestConfirmTime; // ngày yêu cầu rà soát

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_CONFIRM_TIME")
    private Date updateConfirmTime; //Ngày rà soát

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUEST_HTC_TIME", nullable = true)
    private Date requestHtcTime;//Ngày yêu cầu bổ sung

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VERIFY_HTC_TIME", nullable = true)
    private Date verifyHtcTime;//Ngày xác nhận yêu cầu

    @Column(name = "INSURANCE", length = 50, nullable = true)
    private String insurance; //Có thẻ BHYT ?

    @Column(name = "INSURANCE_NO", length = 50, nullable = true)
    private String insuranceNo; //Số thẻ BHYT

    @Column(name = "EARLY_JOB", columnDefinition = "boolean default false")
    private boolean earlyJob; // Đồng bộ nhiễm mới

    public boolean isEarlyJob() {
        return earlyJob;
    }

    public void setEarlyJob(boolean earlyJob) {
        this.earlyJob = earlyJob;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getJobID() {
        return jobID;
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

    public Date getRequestConfirmTime() {
        return requestConfirmTime;
    }

    public void setRequestConfirmTime(Date requestConfirmTime) {
        this.requestConfirmTime = requestConfirmTime;
    }

    public Date getUpdateConfirmTime() {
        return updateConfirmTime;
    }

    public void setUpdateConfirmTime(Date updateConfirmTime) {
        this.updateConfirmTime = updateConfirmTime;
    }

    public Date getRequestHtcTime() {
        return requestHtcTime;
    }

    public void setRequestHtcTime(Date requestHtcTime) {
        this.requestHtcTime = requestHtcTime;
    }

    public Date getVerifyHtcTime() {
        return verifyHtcTime;
    }

    public void setVerifyHtcTime(Date verifyHtcTime) {
        this.verifyHtcTime = verifyHtcTime;
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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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

    public String getSampleSentSource() {
        return sampleSentSource;
    }

    public void setSampleSentSource(String sampleSentSource) {
        this.sampleSentSource = sampleSentSource;
    }

    public Date getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(Date earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public Date getVirusLoadDate() {
        return virusLoadDate;
    }

    public void setVirusLoadDate(Date virusLoadDate) {
        this.virusLoadDate = virusLoadDate;
    }

    public Date getFirstBioDate() {
        return firstBioDate;
    }

    public void setFirstBioDate(Date firstBioDate) {
        this.firstBioDate = firstBioDate;
    }

    public Date getSecondBioDate() {
        return secondBioDate;
    }

    public void setSecondBioDate(Date secondBioDate) {
        this.secondBioDate = secondBioDate;
    }

    public Date getThirdBioDate() {
        return thirdBioDate;
    }

    public void setThirdBioDate(Date thirdBioDate) {
        this.thirdBioDate = thirdBioDate;
    }

    public String getSampleSaveCode() {
        return sampleSaveCode;
    }

    public void setSampleSaveCode(String sampleSaveCode) {
        this.sampleSaveCode = sampleSaveCode;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
    }

    public Date getResultsReturnTime() {
        return resultsReturnTime;
    }

    public void setResultsReturnTime(Date resultsReturnTime) {
        this.resultsReturnTime = resultsReturnTime;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getSampleReceiveTime() {
        return sampleReceiveTime;
    }

    public void setSampleReceiveTime(Date sampleReceiveTime) {
        this.sampleReceiveTime = sampleReceiveTime;
    }

    public String getTestStaffId() {
        return testStaffId;
    }

    public void setTestStaffId(String testStaffId) {
        this.testStaffId = testStaffId;
    }

    public String getBloodSample() {
        return bloodSample;
    }

    public void setBloodSample(String bloodSample) {
        this.bloodSample = bloodSample;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getSampleQuality() {
        return sampleQuality;
    }

    public void setSampleQuality(String sampleQuality) {
        this.sampleQuality = sampleQuality;
    }

    public String getBioName1() {
        return bioName1;
    }

    public void setBioName1(String bioName1) {
        this.bioName1 = bioName1;
    }

    public String getBioNameResult1() {
        return bioNameResult1;
    }

    public void setBioNameResult1(String bioNameResult1) {
        this.bioNameResult1 = bioNameResult1;
    }

    public String getBioName2() {
        return bioName2;
    }

    public void setBioName2(String bioName2) {
        this.bioName2 = bioName2;
    }

    public String getBioNameResult2() {
        return bioNameResult2;
    }

    public void setBioNameResult2(String bioNameResult2) {
        this.bioNameResult2 = bioNameResult2;
    }

    public String getBioName3() {
        return bioName3;
    }

    public void setBioName3(String bioName3) {
        this.bioName3 = bioName3;
    }

    public String getBioNameResult3() {
        return bioNameResult3;
    }

    public void setBioNameResult3(String bioNameResult3) {
        this.bioNameResult3 = bioNameResult3;
    }

    public Date getSourceReceiveSampleTime() {
        return sourceReceiveSampleTime;
    }

    public void setSourceReceiveSampleTime(Date sourceReceiveSampleTime) {
        this.sourceReceiveSampleTime = sourceReceiveSampleTime;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public Date getSourceSampleDate() {
        return sourceSampleDate;
    }

    public void setSourceSampleDate(Date sourceSampleDate) {
        this.sourceSampleDate = sourceSampleDate;
    }

    public String getOtherTechnical() {
        return otherTechnical;
    }

    public void setOtherTechnical(String otherTechnical) {
        this.otherTechnical = otherTechnical;
    }

    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
    }

    public Date getPacSentDate() {
        return pacSentDate;
    }

    public void setPacSentDate(Date pacSentDate) {
        this.pacSentDate = pacSentDate;
    }

    public Long getPacPatientID() {
        return pacPatientID;
    }

    public void setPacPatientID(Long pacPatientID) {
        this.pacPatientID = pacPatientID;
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

    public String getModeOfTransmission() {
        return modeOfTransmission;
    }

    public void setModeOfTransmission(String modeOfTransmission) {
        this.modeOfTransmission = modeOfTransmission;
    }

    /**
     * Lấy label trạng thái xét nghiệm khẳng định
     *
     * @return
     */
    public String getLabelConfirmTestStatus() {
        if (StringUtils.isNotEmpty(this.resultsID) && this.resultsReturnTime != null && StringUtils.isNotEmpty(this.code)
                && this.confirmTime != null) {
            return HtcConfirmStatusEnum.PAID.getLabel();
        }
        if (StringUtils.isNotEmpty(this.resultsID)) {
            return HtcConfirmStatusEnum.DONE.getLabel();
        }
        if (this.acceptDate != null) {
            return HtcConfirmStatusEnum.DOING.getLabel();
        }
        if (this.acceptDate == null) {
            return HtcConfirmStatusEnum.PENDING.getLabel();
        }
        return null;
    }

    @Override
    public void setAttributeLabels() {
        super.setAttributeLabels();
//        attributeLabels.put("fullname", "Họ tên ");
//        attributeLabels.put("code", "MS");
//        attributeLabels.put("genderID", "Giới tính");
//        attributeLabels.put("year", "Năm sinh");
//        attributeLabels.put("address", "Địa chỉ");
//        attributeLabels.put("objectGroupID_1", "Đối tượng: Nghiện chích ma túy (NCMT)");
//        attributeLabels.put("objectGroupID_2", "Đối tượng: Phụ nữ bán dâm (PNBD)");
//        attributeLabels.put("objectGroupID_3", "Đối tượng: Nam quan hệ tình dục với nam (MSM)");
//        attributeLabels.put("objectGroupID_4", "Đối tượng: Vợ/chồng /bạn tình của người nhiễm HIV");
//        attributeLabels.put("objectGroupID_5", "Đối tượng: Phụ nữ mang thai");
//        attributeLabels.put("objectGroupID_6", "Đối tượng: BN Lao");
//        attributeLabels.put("objectGroupID_7", "Đối tượng: Các đối tượng khác");
//        attributeLabels.put("sourceSiteID", "Đơn vị gửi mẫu");
//        attributeLabels.put("sampleReceiveTime", "Ngày nhận mẫu");
//        attributeLabels.put("confirmTime", "Ngày XN khẳng định");
//        attributeLabels.put("resultsID", "Kết quả XN khẳng định");
//        attributeLabels.put("sourceID", "Mã số BN của PXN");

        attributeLabels.put("code", "Mã số");
        attributeLabels.put("fullname", "Họ tên");
        attributeLabels.put("genderID", "Giới tính");
        attributeLabels.put("year", "Năm sinh");
        attributeLabels.put("address", "Số nhà thường trú");
        attributeLabels.put("permanentAddressStreet", "Đường phố thường trú");
        attributeLabels.put("permanentAddressGroup", "Tổ/ấp/Khu phố thường trú");
        attributeLabels.put("wardID", "Phường xã thường trú");
        attributeLabels.put("districtID", "Quận/Huyện thường trú");
        attributeLabels.put("provinceID", "Tỉnh/Thành phố thường trú");
        attributeLabels.put("currentAddress", "Số nhà hiện tại");
        attributeLabels.put("currentAddressStreet", "Đường phố hiện tại");
        attributeLabels.put("currentAddressGroup", "Tổ/ấp/Khu phố hiện tại");
        attributeLabels.put("currentWardID", "Phường xã hiện tại");
        attributeLabels.put("currentDistrictID", "Quận/Huyện hiện tại");
        attributeLabels.put("currentProvinceID", "Tỉnh/Thành phố hiện tại");
        attributeLabels.put("patientID", "Số CMND");
        attributeLabels.put("insuranceNo", "Số thẻ BHYT");
        attributeLabels.put("objectGroupID", "Đối tượng");
        attributeLabels.put("modeOfTransmission", "Đường lây truyền");
        attributeLabels.put("sourceSiteID", "Cơ sở gửi mẫu");
        attributeLabels.put("sourceID", "Mã KH XN sàng lọc");
        attributeLabels.put("sourceReceiveSampleTime", "Ngày lấy mẫu");
        attributeLabels.put("testResultsID", "Kết quả XN sàng lọc");
        attributeLabels.put("sampleQuality", "Chất lượng mẫu");
        attributeLabels.put("sourceSampleDate", "Ngày gửi mẫu");
        attributeLabels.put("serviceID", "Loại dịch vụ");
        attributeLabels.put("sampleReceiveTime", "Ngày nhận mẫu");
        attributeLabels.put("bioName1", "Tên SP1");
        attributeLabels.put("bioNameResult1", "Kết quả SP1");
        attributeLabels.put("firstBioDate", "Ngày XN SP1");
        attributeLabels.put("bioName2", "Tên SP2");
        attributeLabels.put("bioNameResult2", "Kết quả SP2");
        attributeLabels.put("secondBioDate", "Ngày XN SP2");
        attributeLabels.put("bioName3", "Tên SP3");
        attributeLabels.put("bioNameResult3", "Kết quả SP3");
        attributeLabels.put("thirdBioDate", "Ngày XN SP3");
        attributeLabels.put("resultsID", "Kết quả XN khẳng định");
        attributeLabels.put("confirmTime", "Ngày XN khẳng định");
        attributeLabels.put("sampleSaveCode", "Mã số lưu mẫu");
        attributeLabels.put("earlyHiv", "KQXN nhiễm mới HIV");
        attributeLabels.put("earlyHivDate", "Ngày XN nhiễm mới HIV");
        attributeLabels.put("virusLoad", "KQXN tải lượng vi rút");
        attributeLabels.put("virusLoadDate", "Ngày XN tải lượng vi rút");
        attributeLabels.put("resultsReturnTime", "Ngày trả kết quả XN khẳng định");
        attributeLabels.put("testStaffId", "Cán bộ XN khẳng định");
    }
}

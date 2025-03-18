package com.gms.entity.db;

import com.gms.components.StringListConverter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
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
 *
 * @author vvthanh
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
        name = "HTC_VISIT",
        indexes = {
            @Index(name = "HTC_VISIT_SITE", columnList = "SITE_ID")
            ,@Index(name = "HTC_VISIT_CODE", columnList = "CODE")
            ,@Index(name = "HTC_VISIT_SERVICE", columnList = "SERVICE_ID")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"SITE_ID", "CODE"}, name = "htc_visit_unique_site_code")
        }
)
public class HtcVisitEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    //----- A. Thông tin khách hàng
    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "CODE", length = 50, nullable = false)
    private String code;

    @Column(name = "SERVICE_ID", length = 50, nullable = false)
    private String serviceID;

    @Column(name = "FIX_SERVICE_ID", length = 50, nullable = true)
    private String fixedServiceID;

    @Column(name = "PATIENT_NAME", length = 100, nullable = true)
    private String patientName;

    @Column(name = "PATIENT_PHONE", length = 50, nullable = true)
    private String patientPhone;

    @Column(name = "PATIENT_ID", length = 50, nullable = true)
    private String patientID;

    @Column(name = "PATIENT_ID_AUTHEN", length = 50, nullable = true)
    private String patientIDAuthen;

    @Column(name = "RACE_ID", length = 50, nullable = true)
    private String raceID;

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID;

    @Column(name = "YEAR_OF_BIRTH", length = 4, nullable = true)
    private String yearOfBirth;

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

    // Mã xét nghiệm cung cấp bởi cơ sở xét nghiệm cố định
    @Column(name = "TEST_NO_FIX_SITE", length = 100, nullable = true)
    private String testNoFixSite;

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "RISK_BEHAVIOR_ID", length = 50, nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> riskBehaviorID; //Hành vi nguy cơ lây nhiễm

    @Column(name = "REFERRAL_SOURCE", length = 50, nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> referralSource; //Nguồn giới thiệu

    @Column(name = "APPROACH_NO", length = 50, nullable = true)
    private String approacherNo; //Nguồn giới thiệu

    @Column(name = "YOU_INJECT_CODE", length = 50, nullable = true)
    private String youInjectCode; //Mã số bạn tình, bạn chích

    @Column(name = "TEST_RESULTS_ID", length = 50, nullable = true)
    private String testResultsID; //Kết quả xét nghiệm

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADVISORY_TIME", nullable = false)
    private Date advisoryeTime; //Ngày tư vấn

    @Temporal(TemporalType.DATE)
    @Column(name = "RESULTS_TIME", nullable = true)
    private Date resultsTime; //Ngày trả kết quả xét nghiệm sàng lọc

    @Column(name = "IS_AGREE_TEST", nullable = true)
    private Boolean isAgreeTest;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONFIRM_TIME", nullable = true)
    private Date confirmTime; //Ngày xét nghiệm khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESULTS_SITE_TIME", nullable = true)
    private Date resultsSiteTime; //Ngày cơ sở nhận kết quả xn khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESULTS_PATIEN_TIME", nullable = true)
    private Date resultsPatienTime; //Ngày xét trả kết quả xét nghiệm khẳng định cho khách hàng

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRE_TEST_TIME", nullable = true)
    private Date preTestTime; //Ngày xét nghiệm sàng lọc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXCHANGE_CONSULT_TIME", nullable = true)
    private Date exchangeConsultTime; // Ngày thực hiện tư vấn chuyển gửi điều trị

    @Column(name = "ARV_EXCHANGE_RESULT", nullable = true)
    private String arvExchangeResult; // Kết quả tư vấn chuyển điều trị ARV

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXCHANGE_TIME", nullable = true)
    private Date exchangeTime; // Ngày chuyển gửi

    @Column(name = "PARTNER_PROVIDE_RESULT", nullable = true)
    private String partnerProvideResult; // Kết quả tư vấn cung cấp thông tin thực hiện xét nghiệm theo dấu bạn tình/bạn chích

    @Column(name = "ARRIVAL_SITE", nullable = true)
    private String arrivalSite; // Tên cơ sở điều trị khách hàng được chuyển đến

    @Column(name = "ARRIVAL_SITE_ID", nullable = true)
    private Long arrivalSiteID; //Mã cơ sở chuyển đến - khác thì truyền name

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ARRIVAL_TIME", nullable = true)
    private Date arrivalTime; // Ngày tiếp nhận

    @Column(name = "EXCHANGE_PROVINCE_ID", length = 5, nullable = true)
    private String exchangeProvinceID; // Mã tỉnh nơi chuyển gửi đến

    @Column(name = "EXCHANGE_DISTRICT_ID", length = 5, nullable = true)
    private String exchangeDistrictID; // Mã huyện chuyển gửi đến

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTER_THERAPY_TIME", nullable = true)
    private Date registerTherapyTime; // Ngày đăng ký điều trị

    @Column(name = "REGISTER_THERAPY_SITE", length = 220, nullable = true)
    private String registeredTherapySite; // Tên cơ sở mà khách hành đã đăng ký điều trị

    @Column(name = "THERAPY_REGIST_PROVINCE_ID", length = 5, nullable = true)
    private String therapyRegistProvinceID; // Mã tỉnh đã đăng ký điều trị

    @Column(name = "THERAPY_REGIST_DISTRICT_ID", length = 5, nullable = true)
    private String therapyRegistDistrictID; // Mã huyện đã đăng ký điều trị

    @Column(name = "THERAPY_NO", length = 100, nullable = true)
    private String therapyNo; // Mã số điều trị

    @Column(name = "STAFF_BEFORE_TEST_ID", length = 50, nullable = true)
    private String staffBeforeTestID; //Nhân viên tư vấn trước xét nghiệm

    @Column(name = "STAFF_AFTER_TEST_ID", length = 50, nullable = true)
    private String staffAfterID; // Nhân viên tư vấn sau xét nghiệm

    @Column(name = "SITE_CONFIRM_TEST", length = 220, nullable = true)
    private String siteConfirmTest; // Cơ sở xét nghiệm khẳng định

    @Column(name = "ASANTE_TEST", length = 20, nullable = true)
    private String asanteTest;

    @Column(name = "CONFIRM_TEST_NO", length = 100, nullable = true)
    private String confirmTestNo;

    @Column(name = "CONFIRM_RESULTS_ID", length = 50, nullable = true)
    private String confirmResultsID; //Kết quả xét nghiệm khẳng định

    @Column(name = "MODE_OF_TRANSMISSION", length = 50, nullable = true)
    private String modeOfTransmission; // Đường lây

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
    private boolean isRemove;

    @Column(name = "IS_AGREE_PRETEST", length = 2, nullable = true)
    private String isAgreePreTest;

    @Column(name = "HAS_HEALTH_INSURANCE", length = 2, nullable = false)
    private String hasHealthInsurance;

    @Column(name = "HEALTH_INSURANCE_NO", length = 50, nullable = true)
    private String healthInsuranceNo;

    @Column(name = "CONFIRM_TEST_STATUS", length = 50, nullable = true)
    private String confirmTestStatus; // Trạng thái xét nghiệm khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAC_SENT_DATE", nullable = true)
    private Date pacSentDate;   // Trạng thái chuyển gửi GSPH căn cứ vào ngày này

    @Column(name = "PAC_PATIENT_ID", nullable = true)
    private Long pacPatientID;   // Id của bệnh nhân quản lý người nhiễm

    @Column(name = "DPLTMC_STATUS", length = 50, nullable = true)
    private String dpltmcStatus; // Trạng thái dự phòng láy truyền từ mẹ sang con

    @Column(name = "THERAPY_EXCHANGE_STATUS", length = 50, nullable = true)
    private String therapyExchangeStatus; // Trạng thái chuyển gửi điều trị

    //DSNAnh update feedback status 13/09/2019
    @Column(name = "FEEDBACK_STATUS", length = 50, nullable = true)
    private String feedbackStatus;   // Trạng thái đối chiếu

    @Column(name = "FEEDBACK_MESSAGE", length = 500, nullable = true)
    private String feedbackMessage;   // Lý do phản hồi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEDBACK_TIME", nullable = true)
    private Date feedbackTime;   // Thời gian phản hồi

    //Chuyển gửi 
    @Column(name = "IS_REMOVE_TRANFER", nullable = false)
    private boolean removeTranfer; //Xoá logic khi chuyển gửi

    //Ngày phản hồi kết quả tiếp nhận
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEDBACK_RECEIVE_TIME", nullable = true)
    private Date feedbackReceiveTime;

    @Column(name = "VIRUS_LOAD_NUMBER", length = 50, nullable = true)
    private String virusLoadNumber; // Nồng độ virus - nhập số

    @Column(name = "EARLY_BIO_NAME", length = 50, nullable = true)
    private String earlyBioName; // Tên sinh phẩm nhiễm mới

    @Column(name = "EARLY_DIAGNOSE", length = 50, nullable = true)
    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    @Column(columnDefinition = "boolean default false", name = "CONFIRM_NO_RECEIVE")
    private boolean confirmNoReceive;

    @Column(columnDefinition = "varchar(50) default '0'", name = "SOURCE_CREATED", nullable = true)
    private String sourceCreated;

    //auth vvThành Lấy full địa chỉ: Nơi đăng ký hộ khẩu thường trú
    @Transient
    private String permanentAddressFull;

    //auth vvThành Lấy full địa chỉ: Nơi cư trú
    @Transient
    private String currentAddressFull;

    // TrangBN lấy tên cơ sở khẳng định bên HTC
    @Transient
    private String siteConfirmTestName;

    // TrangBN lấy tên cơ sở chuyển gửi tới
    @Transient
    private String arrivalSiteName;

    // TrangBN Tên tỉnh/huyện chuyern gửi điều trị ARV
    @Transient
    private String exchangeProvinceName;

    @Transient
    private String exchangeDistrictName;

    // TrangBN Tên tỉnh/huyện nới đăng ký điều trị
    @Transient
    private String therapyRegistProvinceName;

    @Transient
    private String therapyRegistDistrictName;

    @Transient
    private String currentWardName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SAMPLE_SENT_DATE", nullable = true)
    private Date sampleSentDate; // Ngày gửi mẫu máu

    @Column(name = "EARLY_HIV", length = 50, nullable = true)
    private String earlyHiv;

    @Column(name = "VIRUS_LOAD", length = 50, nullable = true)
    private String virusLoad; //Tải lượng virut

    @Column(name = "PROJECT_ID", insertable = true, updatable = false, nullable = true, length = 50)
    private String projectID; //Dự án tài trợ cho cơ sở

    @Column(name = "PEPFAR_PROJECT_ID", nullable = true, length = 50)
    private String pepfarProjectID; //Dự án tài trợ cho cơ sở - pepfar, cho chọn

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OPC_SENT_DATE", nullable = true)
    private Date opcSentDate;   // Trạng thái chuyển gửi điều trị căn cứ vào ngày này

    @Column(name = "OPC_PATIENT_ID", nullable = true)
    private Long opcPatientID;   // Id của bệnh nhân quản lý điều trị

    @Column(name = "BIO_NAME", length = 50, nullable = true)
    private String bioName; // Tên sinh phẩm

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EARLY_HIV_DATE")
    private Date earlyHivDate; // Ngày XN Nhiễm mới

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIRUS_LOAD_DATE")
    private Date virusLoadDate; // Ngày xét nghiệm tải lượng virus

    @Column(name = "PERMANENT_ADDRESS_STREET", length = 500, nullable = true)
    private String permanentAddressStreet; // Đường phố thương trú

    @Column(name = "PERMANENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String permanentAddressGroup; // Tổ ấp khu phố thường trú

    @Column(name = "CURRENT_ADDRESS_STREET", length = 500, nullable = true)
    private String currentAddressStreet; // Đường phố hiện tại

    @Column(name = "CURRENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String currentAddressGroup; // Tổ ấp khu phố hiện tại

    @Column(name = "CD_SERVICE", length = 5, nullable = true)
    private String cdService; // Dịch vụ tại cơ sở TVXN cố định

    @Column(name = "TEST_METHOD", length = 5, nullable = true)
    private String testMethod; // Phương pháp XN sàng lọc

    @Column(name = "RESULT_ANTI", length = 5, nullable = true)
    private String resultAnti; // KQXN Kháng nguyên/Kháng thể

    @Column(name = "CONFIRM_TYPE", length = 5, nullable = true)
    private String confirmType; // Loại hình XN khẳng định

    @Column(name = "IS_PCR_HIV")
    private boolean pcrHiv; // Chọn câu trả lời PCR HIV

    @Column(name = "RESULT_PCR_HIV", length = 5, nullable = true)
    private String resultPcrHiv; // Xét nghiệm PCR HIV bổ sung

    @Column(name = "EXCHANGE_SERVICE", length = 5, nullable = true)
    private String exchangeService; // Dịch vụ tư vấn chuyển gửi

    @Column(name = "EXCHANGE_SERVICE_NAME", length = 100, nullable = true)
    private String exchangeServiceName; // Dịch vụ chuyển gửi khác

    @Column(name = "QR_CODE", length = 36, nullable = true)
    private String qrcode; //Mã QR

    @Column(name = "IS_CONFIRM_RESULT")
    private boolean confirmResutl; // Cờ đánh dấu nhận kết quả từ khẳng định

    // Bổ sung sau góp ý của tỉnh 8/10/2020
    @Column(name = "STAFF_KC", length = 50, nullable = true)
    private String staffKC; // Mã nhân viên không chuyên

    @Column(name = "CUSTOMER_TYPE", length = 50, nullable = true)
    private String customerType; // Loại khách hàng

    @Column(name = "LAO_VARIABLE", length = 50, nullable = true)
    private String laoVariable; // Thể LAO

    @Column(name = "LAO_VARIABLE_NAME", length = 100, nullable = true)
    private String laoVariableName; // Thể LAO khác

    @Column(name = "pqm_code", nullable = true)
    private Long pqmCode; //Mã pqm push

    @Column(name = "EARLY_JOB", columnDefinition = "boolean default false")
    private boolean earlyJob; // Đồng bộ nhiễm mới

    public boolean isEarlyJob() {
        return earlyJob;
    }

    public void setEarlyJob(boolean earlyJob) {
        this.earlyJob = earlyJob;
    }

    public Long getPqmCode() {
        return pqmCode;
    }

    public void setPqmCode(Long pqmCode) {
        this.pqmCode = pqmCode;
    }

    public String getSourceCreated() {
        return sourceCreated;
    }

    public void setSourceCreated(String sourceCreated) {
        this.sourceCreated = sourceCreated;
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

    public boolean isConfirmNoReceive() {
        return confirmNoReceive;
    }

    public void setConfirmNoReceive(boolean confirmNoReceive) {
        this.confirmNoReceive = confirmNoReceive;
    }

    public String getLaoVariable() {
        return laoVariable;
    }

    public void setLaoVariable(String laoVariable) {
        this.laoVariable = laoVariable;
    }

    public String getLaoVariableName() {
        return laoVariableName;
    }

    public void setLaoVariableName(String laoVariableName) {
        this.laoVariableName = laoVariableName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getStaffKC() {
        return staffKC;
    }

    public void setStaffKC(String staffKC) {
        this.staffKC = staffKC;
    }

    public boolean isConfirmResutl() {
        return confirmResutl;
    }

    public void setConfirmResutl(boolean confirmResutl) {
        this.confirmResutl = confirmResutl;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getCdService() {
        return cdService;
    }

    public void setCdService(String cdService) {
        this.cdService = cdService;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getResultAnti() {
        return resultAnti;
    }

    public void setResultAnti(String resultAnti) {
        this.resultAnti = resultAnti;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public boolean isPcrHiv() {
        return pcrHiv;
    }

    public void setPcrHiv(boolean pcrHiv) {
        this.pcrHiv = pcrHiv;
    }

    public String getResultPcrHiv() {
        return resultPcrHiv;
    }

    public void setResultPcrHiv(String resultPcrHiv) {
        this.resultPcrHiv = resultPcrHiv;
    }

    public String getExchangeService() {
        return exchangeService;
    }

    public void setExchangeService(String exchangeService) {
        this.exchangeService = exchangeService;
    }

    public String getExchangeServiceName() {
        return exchangeServiceName;
    }

    public void setExchangeServiceName(String exchangeServiceName) {
        this.exchangeServiceName = exchangeServiceName;
    }

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

    public Long getArrivalSiteID() {
        return arrivalSiteID;
    }

    public void setArrivalSiteID(Long arrivalSiteID) {
        this.arrivalSiteID = arrivalSiteID;
    }

    public Date getFeedbackReceiveTime() {
        return feedbackReceiveTime;
    }

    public void setFeedbackReceiveTime(Date feedbackReceiveTime) {
        this.feedbackReceiveTime = feedbackReceiveTime;
    }

    public boolean isRemoveTranfer() {
        return removeTranfer;
    }

    public void setRemoveTranfer(boolean removeTranfer) {
        this.removeTranfer = removeTranfer;
    }

    public String getExchangeProvinceName() {
        return exchangeProvinceName;
    }

    public void setExchangeProvinceName(String exchangeProvinceName) {
        this.exchangeProvinceName = exchangeProvinceName;
    }

    public String getExchangeDistrictName() {
        return exchangeDistrictName;
    }

    public void setExchangeDistrictName(String exchangeDistrictName) {
        this.exchangeDistrictName = exchangeDistrictName;
    }

    public String getTherapyRegistProvinceName() {
        return therapyRegistProvinceName;
    }

    public void setTherapyRegistProvinceName(String therapyRegistProvinceName) {
        this.therapyRegistProvinceName = therapyRegistProvinceName;
    }

    public String getTherapyRegistDistrictName() {
        return therapyRegistDistrictName;
    }

    public void setTherapyRegistDistrictName(String therapyRegistDistrictName) {
        this.therapyRegistDistrictName = therapyRegistDistrictName;
    }

    public String getCurrentWardName() {
        return currentWardName;
    }

    public void setCurrentWardName(String currentWardName) {
        this.currentWardName = currentWardName;
    }

    public String getArrivalSiteName() {
        return arrivalSiteName;
    }

    public void setArrivalSiteName(String arrivalSiteName) {
        this.arrivalSiteName = arrivalSiteName;
    }

    public String getSiteConfirmTestName() {
        return siteConfirmTestName;
    }

    public void setSiteConfirmTestName(String siteConfirmTestName) {
        this.siteConfirmTestName = siteConfirmTestName;
    }

    public String getPepfarProjectID() {
        return pepfarProjectID;
    }

    public void setPepfarProjectID(String pepfarProjectID) {
        this.pepfarProjectID = pepfarProjectID;
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

    public String getBioName() {
        return bioName;
    }

    public void setBioName(String bioName) {
        this.bioName = bioName;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
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

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
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

        if (StringUtils.isNotEmpty(code)) {
            return code.toUpperCase();
        }
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getYouInjectCode() {
        return youInjectCode;
    }

    public void setYouInjectCode(String youInjectCode) {
        this.youInjectCode = youInjectCode;
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

    public Date getResultsTime() {
        return resultsTime;
    }

    public void setResultsTime(Date resultsTime) {
        this.resultsTime = resultsTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public String getPatientIDAuthen() {
        return patientIDAuthen;
    }

    public void setPatientIDAuthen(String patientIDAuthen) {
        this.patientIDAuthen = patientIDAuthen;
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

    public Date getResultsPatienTime() {
        return resultsPatienTime;
    }

    public void setResultsPatienTime(Date resultsPatienTime) {
        this.resultsPatienTime = resultsPatienTime;
    }

    public String getStaffBeforeTestID() {
        return staffBeforeTestID;
    }

    public void setStaffBeforeTestID(String staffBeforeTestID) {
        this.staffBeforeTestID = staffBeforeTestID;
    }

    public String getStaffAfterID() {
        return staffAfterID;
    }

    public void setStaffAfterID(String staffAfterID) {
        this.staffAfterID = staffAfterID;
    }

    public String getSiteConfirmTest() {
        return siteConfirmTest;
    }

    public void setSiteConfirmTest(String siteConfirmTest) {
        this.siteConfirmTest = siteConfirmTest;
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

    public boolean isIsRemove() {
        return isRemove;
    }

    public void setIsRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }

    public String getFixedServiceID() {
        return fixedServiceID;
    }

    public void setFixedServiceID(String fixedServiceID) {
        this.fixedServiceID = fixedServiceID;
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

    public List<String> getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(List<String> referralSource) {
        this.referralSource = referralSource;
    }

    public String getApproacherNo() {
        return approacherNo;
    }

    public void setApproacherNo(String approacherNo) {
        this.approacherNo = approacherNo;
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

    public String getModeOfTransmission() {
        return modeOfTransmission;
    }

    public void setModeOfTransmission(String modeOfTransmission) {
        this.modeOfTransmission = modeOfTransmission;
    }

    public Boolean getIsAgreeTest() {
        return isAgreeTest;
    }

    public void setIsAgreeTest(Boolean isAgreeTest) {
        this.isAgreeTest = isAgreeTest;
    }

    public Date getExchangeConsultTime() {
        return exchangeConsultTime;
    }

    public void setExchangeConsultTime(Date exchangeConsultTime) {
        this.exchangeConsultTime = exchangeConsultTime;
    }

    public String getArvExchangeResult() {
        return arvExchangeResult;
    }

    public void setArvExchangeResult(String arvExchangeResult) {
        this.arvExchangeResult = arvExchangeResult;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getPartnerProvideResult() {
        return partnerProvideResult;
    }

    public void setPartnerProvideResult(String partnerProvideResult) {
        this.partnerProvideResult = partnerProvideResult;
    }

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getExchangeProvinceID() {
        return exchangeProvinceID;
    }

    public void setExchangeProvinceID(String exchangeProvinceID) {
        this.exchangeProvinceID = exchangeProvinceID;
    }

    public String getExchangeDistrictID() {
        return exchangeDistrictID;
    }

    public void setExchangeDistrictID(String exchangeDistrictID) {
        this.exchangeDistrictID = exchangeDistrictID;
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

    public String getTherapyRegistProvinceID() {
        return therapyRegistProvinceID;
    }

    public void setTherapyRegistProvinceID(String therapyRegistProvinceID) {
        this.therapyRegistProvinceID = therapyRegistProvinceID;
    }

    public String getTherapyRegistDistrictID() {
        return therapyRegistDistrictID;
    }

    public void setTherapyRegistDistrictID(String therapyRegistDistrictID) {
        this.therapyRegistDistrictID = therapyRegistDistrictID;
    }

    public String getTherapyNo() {
        return therapyNo;
    }

    public void setTherapyNo(String therapyNo) {
        this.therapyNo = therapyNo;
    }

    public String getTestNoFixSite() {
        return testNoFixSite;
    }

    public String getAsanteTest() {
        return asanteTest;
    }

    public void setAsanteTest(String asanteTest) {
        this.asanteTest = asanteTest;
    }

    public void setTestNoFixSite(String testNoFixSite) {
        this.testNoFixSite = testNoFixSite;
    }

    public String getPermanentAddressFull() {
        return permanentAddressFull == null || "".equals(permanentAddressFull) ? permanentAddress : permanentAddressFull;
    }

    public void setPermanentAddressFull(String permanentAddressFull) {
        this.permanentAddressFull = permanentAddressFull;
    }

    public String getCurrentAddressFull() {
        return currentAddressFull == null || "".equals(currentAddressFull) ? currentAddress : currentAddressFull;
    }

    public void setCurrentAddressFull(String currentAddressFull) {
        this.currentAddressFull = currentAddressFull;
    }

    public String getIsAgreePreTest() {
        return isAgreePreTest;
    }

    public void setIsAgreePreTest(String isAgreePreTest) {
        this.isAgreePreTest = isAgreePreTest;
    }

    public Date getPreTestTime() {
        return preTestTime;
    }

    public void setPreTestTime(Date preTestTime) {
        this.preTestTime = preTestTime;
    }

    public String getHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public void setHasHealthInsurance(String hasHealthInsurance) {
        this.hasHealthInsurance = hasHealthInsurance;
    }

    public String getHealthInsuranceNo() {
        return healthInsuranceNo;
    }

    public void setHealthInsuranceNo(String healthInsuranceNo) {
        this.healthInsuranceNo = healthInsuranceNo;
    }

    public String getConfirmTestStatus() {
        return confirmTestStatus;
    }

    public void setConfirmTestStatus(String confirmTestStatus) {
        this.confirmTestStatus = confirmTestStatus;
    }

    public String getDpltmcStatus() {
        return dpltmcStatus;
    }

    public void setDpltmcStatus(String dpltmcStatus) {
        this.dpltmcStatus = dpltmcStatus;
    }

    public String getTherapyExchangeStatus() {
        return therapyExchangeStatus;
    }

    public void setTherapyExchangeStatus(String therapyExchangeStatus) {
        this.therapyExchangeStatus = therapyExchangeStatus;
    }

    public Date getSampleSentDate() {
        return sampleSentDate;
    }

    public void setSampleSentDate(Date sampleSentDate) {
        this.sampleSentDate = sampleSentDate;
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

    public Date getOpcSentDate() {
        return opcSentDate;
    }

    public void setOpcSentDate(Date opcSentDate) {
        this.opcSentDate = opcSentDate;
    }

    public Long getOpcPatientID() {
        return opcPatientID;
    }

    public void setOpcPatientID(Long opcPatientID) {
        this.opcPatientID = opcPatientID;
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

    @Override
    public void setAttributeLabels() {
        super.setAttributeLabels();
        attributeLabels.put("order", "TT");
        attributeLabels.put("advisoryeTime", "Ngày tư vấn trước XN");
        attributeLabels.put("code", "Mã khách hàng");
        attributeLabels.put("patientName", "Họ tên khách hàng");
        attributeLabels.put("patientID", "Số CMND (Giấy tờ khác)");
        attributeLabels.put("genderID", "Giới tính");
        attributeLabels.put("yearOfBirth", "Năm sinh");
        attributeLabels.put("permanentAddress", "Số nhà/ tổ/ thôn/xóm/ấp");
        attributeLabels.put("province", "Tỉnh/ Thành phố");
        attributeLabels.put("district", "Quận/ huyện");
        attributeLabels.put("ward", "Phường/ xã");
        attributeLabels.put("phone", "Số Điện thoại");
        attributeLabels.put("hasHealthInsurance", "Có BHYT hay không");
        attributeLabels.put("isAgreePreTest", "Đồng ý Xét nghiệm");
        attributeLabels.put("objectGroupID_1", "Nhóm nguy cơ: Nghiện chích ma túy (NCMT)");
        attributeLabels.put("objectGroupID_2", "Nhóm nguy cơ: Phụ nữ bán dâm (PNBD)");
        attributeLabels.put("objectGroupID_3", "Nhóm nguy cơ: Nam quan hệ tình dục với nam (MSM)");
        attributeLabels.put("objectGroupID_4", "Nhóm nguy cơ: Vợ/chồng /bạn tình của người nhiễm HIV");
        attributeLabels.put("objectGroupID_5", "Nhóm nguy cơ: 5.1. Phụ nữ mang thai (Thời kỳ mang thai)");
        attributeLabels.put("objectGroupID_6", "Nhóm nguy cơ: 5.2. Phụ nữ mang thai (Giai đoạn chuyển dạ, đẻ)");
        attributeLabels.put("objectGroupID_7", "Nhóm nguy cơ: BN Lao");
        attributeLabels.put("objectGroupID_8", "Nhóm nguy cơ: Các đối tượng khác");
        attributeLabels.put("objectGroupID_9", "Nhóm nguy cơ: Phạm nhân");
        attributeLabels.put("testResultsID", "Kết quả xét nghiệm sàng lọc");
        attributeLabels.put("confirmResultsID", "Kết quả xét nghiệm khẳng định");
        attributeLabels.put("resultsTime", "Ngày nhận kết quả và tư vấn sau XN");
        attributeLabels.put("staffBeforeTestID", "Tên tư vấn viên trước XN");
        attributeLabels.put("staffAfterID", "Tên tư vấn viên sau XN");
        attributeLabels.put("sampleSentDate", "Ngày gửi mẫu XN");
        attributeLabels.put("preTestTime", "Ngày xét nghiệm sàng lọc");
        attributeLabels.put("confirmTime", "Ngày xét nghiệm khẳng định");
        attributeLabels.put("healthInsuranceNo", "Mã số thẻ BHYT");
    }

    /**
     * Label kết quả xét nghiệm khẳng định
     *
     * @param options
     * @return
     */
    public String getLabelConfirmStatus(HashMap<String, HashMap<String, String>> options) {
        try {
            if (this.confirmResultsID != null && !"".equals(this.confirmResultsID) && StringUtils.isNotBlank(this.getConfirmTestNo()) && this.getConfirmTime() != null) {
                return options.get(ParameterEntity.TEST_RESULTS_CONFIRM).getOrDefault(this.confirmResultsID, "");
            }
            return this.confirmTestStatus.equals("") || this.confirmTestStatus.equals("-1") ? "" : options.get(ParameterEntity.CONFIRM_TEST_STATUS).getOrDefault(this.confirmTestStatus, "");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

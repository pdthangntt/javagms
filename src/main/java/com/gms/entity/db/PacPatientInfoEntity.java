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
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Thông tin bệnh nhân
 */
@Entity
@Table(name = "PAC_PATIENT_INFO", indexes = {
    @Index(name = "PATIENT_INFO_INDEX_TYPE", columnList = "SOURCE_SERVICE_ID")
    ,@Index(name = "PATIENT_INFO_INDEX_TYPE_SOURCE_ID", columnList = "SOURCE_SERVICE_ID, SOURCE_ID")
    ,@Index(name = "PII_PERMANENT_PROVINCE_ID", columnList = "PERMANENT_PROVINCE_ID")
    ,@Index(name = "PII_PERMANENT_DISTRICT_ID", columnList = "PERMANENT_DISTRICT_ID")
    ,@Index(name = "PII_PERMANENT_WARD_ID", columnList = "PERMANENT_WARD_ID")
    ,@Index(name = "PII_CURRENT_PROVINCE_ID", columnList = "CURRENT_PROVINCE_ID")
    ,@Index(name = "PII_CURRENT_DISTRICT_ID", columnList = "CURRENT_DISTRICT_ID")
    ,@Index(name = "PII_CURRENT_WARD_ID", columnList = "CURRENT_WARD_ID")
    ,@Index(name = "PII_PROVINCE_ID", columnList = "PROVINCE_ID")
    ,@Index(name = "PII_DISTRICT_ID", columnList = "DISTRICT_ID")
    ,@Index(name = "PII_WARD_ID", columnList = "WARD_ID")
    ,@Index(name = "PII_IS_REMOVE", columnList = "IS_REMOVE")
    ,@Index(name = "PII_ACCEPT_PERMANENT_TIME", columnList = "ACCEPT_PERMANENT_TIME")
    ,@Index(name = "PII_REVIEW_PROVINCE_TIME", columnList = "REVIEW_PROVINCE_TIME")
    ,@Index(name = "PII_REVIEW_DISTRICT_TIME", columnList = "REVIEW_DISTRICT_TIME")
    ,@Index(name = "PII_REVIEW_WARD_TIME", columnList = "REVIEW_WARD_TIME")
    ,@Index(name = "PII_UPDATED_AT", columnList = "UPDATED_AT")
    ,@Index(name = "PII_CONFIRM_TIME", columnList = "CONFIRM_TIME")
    ,@Index(name = "PII_CREATED_AT", columnList = "CREATED_AT")
    ,@Index(name = "PII_AIDS_STATUS_DATE", columnList = "AIDS_STATUS_DATE")
    ,@Index(name = "PII_YEAR_OF_BIRTH", columnList = "YEAR_OF_BIRTH")
    ,@Index(name = "PII_GENDER_ID", columnList = "GENDER_ID")
    ,@Index(name = "PII_RACE_ID", columnList = "RACE_ID")
    ,@Index(name = "PII_OBJECT_GROUP_ID", columnList = "OBJECT_GROUP_ID")
    ,@Index(name = "PII_MODE_OF_TRANSMISSION_ID", columnList = "MODE_OF_TRANSMISSION_ID")
    ,@Index(name = "PII_STATUS_OF_RESIDENT_ID", columnList = "STATUS_OF_RESIDENT_ID")
    ,@Index(name = "PII_STATUS_OF_TREATMENT_ID", columnList = "STATUS_OF_TREATMENT_ID")
    ,@Index(name = "PII_META_NAME", columnList = "META_NAME")
    ,@Index(name = "PII_META_IDENTITY_CARD", columnList = "META_IDENTITY_CARD")
    ,@Index(name = "PII_BLOOD_BASE_ID", columnList = "BLOOD_BASE_ID")
    ,@Index(name = "PII_CONFIRM_TEST_ID", columnList = "CONFIRM_TEST_ID")
    ,@Index(name = "PII_SITE_TREATMENT_FACILITY_ID", columnList = "SITE_TREATMENT_FACILITY_ID")
    ,@Index(name = "PII_ACCEPT_TIME", columnList = "ACCEPT_TIME")
    ,@Index(name = "PII_DEATH_TIME", columnList = "DEATH_TIME")
    ,@Index(name = "PII_STATUS", columnList = "STATUS")
})
@DynamicUpdate
@DynamicInsert
public class PacPatientInfoEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "OPC_CODE", length = 50, nullable = true)
    private String opcCode; //Mã bệnh án bên OPC

    @Column(name = "PARENT_ID")
    private Long parentID; //Mã bản ghi chính

    @Column(name = "SOURCE_SERVICE_ID", length = 50, nullable = true)
    private String sourceServiceID; //Loại nguồn dịch vụ gửi đến

    @Column(name = "SOURCE_SITE_ID", nullable = true)
    private Long sourceSiteID; //mã cơ sở nguồn gửi đến

    @Column(name = "SOURCE_ID", nullable = true)
    private Long sourceID; //mã code đối tượng được gửi đến

    @Column(name = "META_NAME", length = 32, nullable = false)
    private String metaName;

    @Column(name = "RACE_ID", length = 50, nullable = true)
    private String raceID;

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID; //Giới tính

    @Column(name = "YEAR_OF_BIRTH", nullable = true)
    private Integer yearOfBirth;

    @Column(name = "META_IDENTITY_CARD", length = 50, nullable = true)
    private String metaIdentityCard; //Chứng minh thư nhân dân

    @Column(name = "META_HEALTH_INSURANCE_NO", length = 50, nullable = true)
    private String metaHealthInsuranceNo;

    @Column(name = "JOB_ID", length = 50, nullable = true)
    private String jobID; //công việc

    @Column(name = "RISK_BEHAVIOR_ID", length = 220, nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> riskBehaviorID; //Hành vi nguy cơ lây nhiễm

    @Column(name = "MODE_OF_TRANSMISSION_ID", length = 50, nullable = true)
    private String modeOfTransmissionID; //Đưòng lây

    @Column(name = "STATUS_OF_RESIDENT_ID", length = 50, nullable = true)
    private String statusOfResidentID; //Hiện trạng cư trú

    @Column(name = "META_PERMANENT_ADDRESS_NO", length = 50, nullable = true)
    private String metaPermanentAddressNo; //Địa chỉ thường trú

    @Column(name = "META_PERMANENT_ADDRESS_GROUP", length = 50, nullable = true)
    private String metaPermanentAddressGroup; //Tổ

    @Column(name = "META_PERMANENT_ADDRESS_STREET", length = 50, nullable = true)
    private String metaPermanentAddressStreet; //Đường phố

    @Column(name = "PERMANENT_PROVINCE_ID", length = 5, nullable = true)
    private String permanentProvinceID;

    @Column(name = "PERMANENT_DISTRICT_ID", length = 5, nullable = true)
    private String permanentDistrictID;

    @Column(name = "PERMANENT_WARD_ID", length = 5, nullable = true)
    private String permanentWardID;

    @Column(name = "META_CURRENT_ADDRESS_NO", length = 50, nullable = true)
    private String metaCurrentAddressNo; //Địa chỉ cư trú

    @Column(name = "META_CURRENT_ADDRESS_GROUP", length = 50, nullable = true)
    private String metaCurrentAddressGroup; //Tổ

    @Column(name = "META_CURRENT_ADDRESS_STREET", length = 50, nullable = true)
    private String metaCurrentAddressStreet; //Đường phố

    @Column(name = "CURRENT_PROVINCE_ID", length = 5, nullable = true)
    private String currentProvinceID;

    @Column(name = "CURRENT_DISTRICT_ID", length = 5, nullable = true)
    private String currentDistrictID;

    @Column(name = "CURRENT_WARD_ID", length = 5, nullable = true)
    private String currentWardID;

    @Column(name = "TYPE_OF_PATIENT", length = 50, nullable = true)
    private String typeOfPatientID; //Loại bệnh nhân

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONFIRM_TIME", nullable = true)
    private Date confirmTime; //Ngày xét nghiệm khẳng định

    @Column(name = "CONFIRM_TEST_ID", length = 50, nullable = true)
    private String siteConfirmID; //Cơ sở xét nghiệm khẳng định - liên kết với place-test

    @Column(name = "BLOOD_BASE_ID", length = 50, nullable = true)
    private String bloodBaseID; //Nơi lấy máu

    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị

    @Column(name = "OPC_STATUS", length = 50, nullable = true)
    private String opcStatus; //Trạng thái biến động điều trị // Sai // xem lại

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DEATH_TIME", nullable = true)
    private Date deathTime; //Ngày tử vong

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUEST_DEATH_TIME", nullable = true)
    private Date requestDeathTime; //Ngày bao tử vong

    @Column(name = "CAUSE_OF_DEATH", length = 220, nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> causeOfDeath; //Nguyên nhân tử vong

    @Column(name = "DETECT_PROVINCE_ID", length = 5, nullable = false)
    private String detectProvinceID; //Tỉnh thành phát hiện

    @Column(name = "DETECT_DISTRICT_ID", length = 5, nullable = true)
    private String detectDistrictID; //Quận huyện phát hiện

    @Column(name = "PROVINCE_ID", length = 5, nullable = false)
    private String provinceID; //Tỉnh thành quản lý

    @Column(name = "DISTRICT_ID", length = 5, nullable = true)
    private String districtID; //Quận huyện quản lý

    @Column(name = "WARD_ID", length = 5, nullable = true)
    private String wardID; //Phường xã quản lý

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REVIEW_WARD_TIME")
    private Date reviewWardTime; //Trạng thái rà soát cấp xã

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCEPT_TIME")
    private Date acceptTime; //Duyệt sang cần rà soát

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REVIEW_DISTRICT_TIME")
    private Date reviewDistrictTime; //Trạng thái rà soát cấp huyện

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REVIEW_PROVINCE_TIME")
    private Date reviewProvinceTime; //Trạng thái rà soát cấp tỉnh -> đưa vào quản lý bệnh nhân

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

    @Column(name = "note", length = 500, nullable = true)
    private String note; //Ghi chú

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TREATMENT_TIME", nullable = true)
    private Date startTreatmentTime; //Ngày bắt đầu điều trị 

    @Column(name = "SITE_TREATMENT_FACILITY_ID", length = 50, nullable = true)
    private String siteTreatmentFacilityID; //Cơ sở điều trị - liên kết với treatment-facility

    @Column(name = "SITE_ID_TF", nullable = true)
    private Long siteIDTF; //Mã cơ sở điều trị, trường ẩn - tự động lấy theo tham số

    @Column(name = "TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String treatmentRegimenID; //Phác đồ điều trị - liên kết với treatment-regimen

    @Column(name = "SYMPTOM_ID", length = 50, nullable = true)
    private String symptomID; //Triệu chứng - liên kết với symptom

    @Column(name = "IS_REMOVE")
    private boolean remove; //Xoá logic

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REMOVE_AT", nullable = true)
    private Date remoteAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCEPT_PERMANENT_TIME")
    private Date acceptPermanentTime; //Trạng thái duyệt chuyển sang ngoại tỉnh phát hiện

    @Column(name = "VIRUS_LOAD_CONFIRM", length = 50, nullable = true)
    private String virusLoadConfirm; //Tải lượng confirm

    @Column(name = "VIRUS_LOAD_ARV", length = 50, nullable = true)
    private String virusLoadArv; //Tải lượng virus ARV

    @Column(name = "EARLY_HIV", length = 50, nullable = true)
    private String earlyHiv;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EARLY_HIV_TIME", nullable = true)
    private Date earlyHivTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIRUS_LOAD_ARV_DATE", nullable = true)
    private Date virusLoadArvDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIRUS_LOAD_CONFIRM_DATE", nullable = true)
    private Date virusLoadConfirmDate;

    @Column(name = "CD_FOUR_RESULT", nullable = true)
    private Long cdFourResult; //CD4result

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CD_FOUR_RESULT_DATE", nullable = true)
    private Date cdFourResultDate; // CD4ResultDate

    @Column(name = "CLINICAL_STAGE", length = 50, nullable = true)
    private String clinicalStage; // Giai đoạn lâm sàng

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLINICAL_STAGE_DATE", nullable = true)
    private Date clinicalStageDate; // clinicalStageDate

    @Column(name = "AIDS_STATUS", length = 50, nullable = true)
    private String aidsStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AIDS_STATUS_DATE", nullable = true)
    private Date aidsStatusDate; // AIDS_STATUS_DATE

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUEST_TIME", nullable = true)
    private Date requestTime; // Ngày xã yêu cầu cập nhật

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LATLNG_TIME", nullable = true)
    private Date latlngTime; // Ngày cập nhật toạ độ địa lý

    @Column(name = "CD_FOUR_RESULT_CURRENT", nullable = true)
    private Long cdFourResultCurrent; //CD4result hiện tại

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CD_FOUR_RESULT_LASTEST_DATE", nullable = true)
    private Date cdFourResultLastestDate; // CD4ResultLastestDate Ngày XN CD4 ngày gần nhất

    @Column(name = "VIRUS_LOAD_ARV_CURRENT", length = 50, nullable = true)
    private String virusLoadArvCurrent; //Tải lượng virus ARV hiện tại

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIRUS_LOAD_ARV_LASTEST_DATE", nullable = true)
    private Date virusLoadArvLastestDate; // Ngày XN tải lượng virus gần đây nhất

    @Column(name = "STATUS_OF_CHANGE_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfChangeTreatmentID; //Trạng thái biến động điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHANGE_TREATMENT_DATE", nullable = true)
    private Date changeTreatmentDate; // Ngày biến động điều trị

    @Column(name = "CONFIRM_CODE", length = 100)
    private String confirmCode; // Mã XN khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECK_PROVINCE_TIME", nullable = true)
    private Date checkProvinceTime; //Ngày tỉnh yêu cầu rà soát lại

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECK_WARD_TIME", nullable = true)
    private Date checkWardTime; //Ngày xã cập nhật yêu cầu rà soát lại

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECK_DISTRICT_TIME", nullable = true)
    private Date checkDistrictTime; //Ngày huyện duyệt yêu cầu rà soát lại từ xã gửi lên

    @Column(name = "PATIENT_PHONE", length = 13, nullable = true)
    private String patientPhone; //Số điện thoại bệnh nhân

    @Column(name = "STATUS", length = 3, nullable = true)
    private String status; //Phân loại ca mới ca cũ

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUEST_VAAC_TIME", nullable = true)
    private Date requestVaacTime; //Ngày vaac chuyển quản lý -> tỉnh khác phát hiện

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REVIEW_VAAC_TIME", nullable = true)
    private Date reviewVaacTime; //Ngày vaac yêu cầu rà soát

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_PAC_TIME", nullable = true)
    private Date updatedPacTime; //Ngày pac cập nhật yêu cầu rà soát

    @Column(name = "REF_ID", nullable = true)
    private Long refID; //Mã người nhiễm clone sau khi vaac chuyển quản lý

    @Column(name = "REF_OPC_ID", nullable = true)
    private Long refOpcID; //Mã của thằng được kết nối điều trị

    @Column(name = "REF_PARENT_ID", nullable = true)
    private Long refParentID; //Mã thằng cha được thêm lịch sử

    @Column(name = "HAS_HEALTH_INSURANCE", length = 2, nullable = true)
    private String hasHealthInsurance;

    @Column(name = "VIRUS_LOAD_NUMBER", length = 50, nullable = true)
    private String virusLoadNumber; //Kết quả xét nghiệm TLVR - nhập số

    @Column(name = "EARLY_BIO_NAME", length = 50, nullable = true)
    private String earlyBioName; // Tên sinh phẩm nhiễm mới

    @Column(name = "EARLY_DIAGNOSE", length = 50, nullable = true)
    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSURANCE_EXPIRY", nullable = true)
    private Date insuranceExpiry;  //Ngày hết hạn bhyt

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_TREATMENT_TIME", nullable = true)
    private Date firstTreatmentTime;  //Ngày ARV đầu tiên

    @Column(name = "FIRST_TREATMENT_REGIMEN_ID", length = 50, nullable = true)
    private String firstTreatmentRegimenId; //Phác đồ điều trị đầu tiên

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_TIME", nullable = true)
    private Date registrationTime;  //Ngày đăng ký

    @Column(name = "REGISTRATION_TYPE", length = 50, nullable = true)
    private String registrationType; // Loại đăng ký

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", nullable = true)
    private Date endTime;  //Ngày kết thúc

    @Column(name = "END_CASE", length = 1, nullable = true)
    private String endCase; // Lý do kết thúc

    @Transient
    private String fullname;

    @Transient
    private String identityCard;

    @Transient
    private String healthInsuranceNo;

    @Transient
    private String permanentAddressNo; //Địa chỉ thường trú

    @Transient
    private String permanentAddressGroup; //Tổ

    @Transient
    private String permanentAddressStreet; //Đường phố

    @Transient
    private String currentAddressNo; //Địa chỉ cư trú

    @Transient
    private String currentAddressGroup; //Tổ

    @Transient
    private String currentAddressStreet; //Đường phố

    //auth vvThành Lấy full địa chỉ: Nơi đăng ký hộ khẩu thường trú
    @Transient
    private String permanentAddressFull;

    //auth vvThành Lấy full địa chỉ: Nơi cư trú
    @Transient
    private String currentAddressFull;

    @Transient
    private PacHivInfoEntity hivInfo;

    @Transient
    private String causesOfDead;

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

    public Long getRefOpcID() {
        return refOpcID;
    }

    public void setRefOpcID(Long refOpcID) {
        this.refOpcID = refOpcID;
    }

    public Long getRefParentID() {
        return refParentID;
    }

    public void setRefParentID(Long refParentID) {
        this.refParentID = refParentID;
    }

    public Date getReviewVaacTime() {
        return reviewVaacTime;
    }

    public void setReviewVaacTime(Date reviewVaacTime) {
        this.reviewVaacTime = reviewVaacTime;
    }

    public Date getUpdatedPacTime() {
        return updatedPacTime;
    }

    public void setUpdatedPacTime(Date updatedPacTime) {
        this.updatedPacTime = updatedPacTime;
    }

    public Long getRefID() {
        return refID;
    }

    public void setRefID(Long refID) {
        this.refID = refID;
    }

    public Date getRequestVaacTime() {
        return requestVaacTime;
    }

    public void setRequestVaacTime(Date requestVaacTime) {
        this.requestVaacTime = requestVaacTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getChangeTreatmentDate() {
        return changeTreatmentDate;
    }

    public void setChangeTreatmentDate(Date changeTreatmentDate) {
        this.changeTreatmentDate = changeTreatmentDate;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public Date getCheckProvinceTime() {
        return checkProvinceTime;
    }

    public void setCheckProvinceTime(Date checkProvinceTime) {
        this.checkProvinceTime = checkProvinceTime;
    }

    public Date getCheckWardTime() {
        return checkWardTime;
    }

    public void setCheckWardTime(Date checkWardTime) {
        this.checkWardTime = checkWardTime;
    }

    public Date getCheckDistrictTime() {
        return checkDistrictTime;
    }

    public void setCheckDistrictTime(Date checkDistrictTime) {
        this.checkDistrictTime = checkDistrictTime;
    }

    public Long getSiteIDTF() {
        return siteIDTF;
    }

    public void setSiteIDTF(Long siteIDTF) {
        this.siteIDTF = siteIDTF;
    }

    public String getCausesOfDead() {
        return causesOfDead;
    }

    public void setCausesOfDead(String causesOfDead) {
        this.causesOfDead = causesOfDead;
    }

    public Date getLatlngTime() {
        return latlngTime;
    }

    public void setLatlngTime(Date latlngTime) {
        this.latlngTime = latlngTime;
    }

    public Date getRequestDeathTime() {
        return requestDeathTime;
    }

    public void setRequestDeathTime(Date requestDeathTime) {
        this.requestDeathTime = requestDeathTime;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
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

    public Date getVirusLoadArvDate() {
        return virusLoadArvDate;
    }

    public void setVirusLoadArvDate(Date virusLoadArvDate) {
        this.virusLoadArvDate = virusLoadArvDate;
    }

    public Date getVirusLoadConfirmDate() {
        return virusLoadConfirmDate;
    }

    public void setVirusLoadConfirmDate(Date virusLoadConfirmDate) {
        this.virusLoadConfirmDate = virusLoadConfirmDate;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public Date getEarlyHivTime() {
        return earlyHivTime;
    }

    public Date getFirstTreatmentTime() {
        return firstTreatmentTime;
    }

    public void setFirstTreatmentTime(Date firstTreatmentTime) {
        this.firstTreatmentTime = firstTreatmentTime;
    }

    public String getFirstTreatmentRegimenId() {
        return firstTreatmentRegimenId;
    }

    public void setFirstTreatmentRegimenId(String firstTreatmentRegimenId) {
        this.firstTreatmentRegimenId = firstTreatmentRegimenId;
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

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public void setEarlyHivTime(Date earlyHivTime) {
        this.earlyHivTime = earlyHivTime;
    }

    public Long getCdFourResult() {
        return cdFourResult;
    }

    public void setCdFourResult(Long cdFourResult) {
        this.cdFourResult = cdFourResult;
    }

    public Date getCdFourResultDate() {
        return cdFourResultDate;
    }

    public void setCdFourResultDate(Date cdFourResultDate) {
        this.cdFourResultDate = cdFourResultDate;
    }

    public String getClinicalStage() {
        return clinicalStage;
    }

    public void setClinicalStage(String clinicalStage) {
        this.clinicalStage = clinicalStage;
    }

    public Date getClinicalStageDate() {
        return clinicalStageDate;
    }

    public void setClinicalStageDate(Date clinicalStageDate) {
        this.clinicalStageDate = clinicalStageDate;
    }

    public String getAidsStatus() {
        return aidsStatus;
    }

    public void setAidsStatus(String aidsStatus) {
        this.aidsStatus = aidsStatus;
    }

    public Date getAidsStatusDate() {
        return aidsStatusDate;
    }

    public void setAidsStatusDate(Date aidsStatusDate) {
        this.aidsStatusDate = aidsStatusDate;
    }

    public Date getReviewDistrictTime() {
        return reviewDistrictTime;
    }

    public void setReviewDistrictTime(Date reviewDistrictTime) {
        this.reviewDistrictTime = reviewDistrictTime;
    }

    public List<String> getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(List<String> causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public PacHivInfoEntity getHivInfo() {
        return hivInfo;
    }

    public void setHivInfo(PacHivInfoEntity hivInfo) {
        this.hivInfo = hivInfo;
    }

    public Date getRemoteAT() {
        return remoteAT;
    }

    public void setRemoteAT(Date remoteAT) {
        this.remoteAT = remoteAT;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public Date getAcceptPermanentTime() {
        return acceptPermanentTime;
    }

    public void setAcceptPermanentTime(Date acceptPermanentTime) {
        this.acceptPermanentTime = acceptPermanentTime;
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

    public String getSymptomID() {
        return symptomID;
    }

    public void setSymptomID(String symptomID) {
        this.symptomID = symptomID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
    }

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
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

    public String getMetaIdentityCard() {
        return metaIdentityCard;
    }

    public void setMetaIdentityCard(String metaIdentityCard) {
        this.metaIdentityCard = metaIdentityCard;
    }

    public String getMetaHealthInsuranceNo() {
        return metaHealthInsuranceNo;
    }

    public void setMetaHealthInsuranceNo(String metaHealthInsuranceNo) {
        this.metaHealthInsuranceNo = metaHealthInsuranceNo;
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

    public String getMetaPermanentAddressNo() {
        return metaPermanentAddressNo;
    }

    public void setMetaPermanentAddressNo(String metaPermanentAddressNo) {
        this.metaPermanentAddressNo = metaPermanentAddressNo;
    }

    public String getMetaPermanentAddressGroup() {
        return metaPermanentAddressGroup;
    }

    public void setMetaPermanentAddressGroup(String metaPermanentAddressGroup) {
        this.metaPermanentAddressGroup = metaPermanentAddressGroup;
    }

    public String getMetaPermanentAddressStreet() {
        return metaPermanentAddressStreet;
    }

    public void setMetaPermanentAddressStreet(String metaPermanentAddressStreet) {
        this.metaPermanentAddressStreet = metaPermanentAddressStreet;
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

    public String getMetaCurrentAddressNo() {
        return metaCurrentAddressNo;
    }

    public void setMetaCurrentAddressNo(String metaCurrentAddressNo) {
        this.metaCurrentAddressNo = metaCurrentAddressNo;
    }

    public String getMetaCurrentAddressGroup() {
        return metaCurrentAddressGroup;
    }

    public void setMetaCurrentAddressGroup(String metaCurrentAddressGroup) {
        this.metaCurrentAddressGroup = metaCurrentAddressGroup;
    }

    public String getMetaCurrentAddressStreet() {
        return metaCurrentAddressStreet;
    }

    public void setMetaCurrentAddressStreet(String metaCurrentAddressStreet) {
        this.metaCurrentAddressStreet = metaCurrentAddressStreet;
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

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
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

    public Date getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(Date deathTime) {
        this.deathTime = deathTime;
    }

    public String getDetectProvinceID() {
        return detectProvinceID;
    }

    public void setDetectProvinceID(String detectProvinceID) {
        this.detectProvinceID = detectProvinceID;
    }

    public String getDetectDistrictID() {
        return detectDistrictID;
    }

    public void setDetectDistrictID(String detectDistrictID) {
        this.detectDistrictID = detectDistrictID;
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

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public Date getReviewWardTime() {
        return reviewWardTime;
    }

    public void setReviewWardTime(Date reviewWardTime) {
        this.reviewWardTime = reviewWardTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getReviewProvinceTime() {
        return reviewProvinceTime;
    }

    public void setReviewProvinceTime(Date reviewProvinceTime) {
        this.reviewProvinceTime = reviewProvinceTime;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public Date getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(Date insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
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

    public String getOpcStatus() {
        return opcStatus;
    }

    public void setOpcStatus(String opcStatus) {
        this.opcStatus = opcStatus;
    }

    public Date getVirusLoadArvLastestDate() {
        return virusLoadArvLastestDate;
    }

    public void setVirusLoadArvLastestDate(Date virusLoadArvLastestDate) {
        this.virusLoadArvLastestDate = virusLoadArvLastestDate;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Long getCdFourResultCurrent() {
        return cdFourResultCurrent;
    }

    public void setCdFourResultCurrent(Long cdFourResultCurrent) {
        this.cdFourResultCurrent = cdFourResultCurrent;
    }

    public Date getCdFourResultLastestDate() {
        return cdFourResultLastestDate;
    }

    public void setCdFourResultLastestDate(Date cdFourResultLastestDate) {
        this.cdFourResultLastestDate = cdFourResultLastestDate;
    }

    public String getVirusLoadArvCurrent() {
        return virusLoadArvCurrent;
    }

    public void setVirusLoadArvCurrent(String virusLoadArvCurrent) {
        this.virusLoadArvCurrent = virusLoadArvCurrent;
    }

    public String getStatusOfChangeTreatmentID() {
        return statusOfChangeTreatmentID;
    }

    public void setStatusOfChangeTreatmentID(String statusOfChangeTreatmentID) {
        this.statusOfChangeTreatmentID = statusOfChangeTreatmentID;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
        ignoreAttributes.add("metaName");
        ignoreAttributes.add("metaIdentityCard");
        ignoreAttributes.add("metaHealthInsuranceNo");
        ignoreAttributes.add("metaPermanentAddressNo");
        ignoreAttributes.add("metaPermanentAddressGroup");
        ignoreAttributes.add("metaPermanentAddressStreet");
        ignoreAttributes.add("metaCurrentAddressNo");
        ignoreAttributes.add("metaCurrentAddressGroup");
        ignoreAttributes.add("metaCurrentAddressStreet");
    }

    @Override
    public void setAttributeLabels() {
        super.setAttributeLabels();
        attributeLabels.put("fullname", "Họ và tên");
        attributeLabels.put("raceID", "Dân tộc");
        attributeLabels.put("yearOfBirth", "Năm sinh");
        attributeLabels.put("genderID", "Giới tính");
        attributeLabels.put("healthInsuranceNo", "Số thẻ BHYT");
        attributeLabels.put("insuranceExpiry", "Ngày hết hạn BHYT");
        attributeLabels.put("identityCard", "Số CMND");
        attributeLabels.put("statusOfResidentID", "Xác minh hiện trạng cư trú");
        attributeLabels.put("sourceServiceID", "Nguồn dịch vụ");
        attributeLabels.put("causeOfDeath", "Nguyên nhân tử vong");
        attributeLabels.put("jobID", "Nghề nghiệp");
        attributeLabels.put("objectGroupID", "Nhóm đối tượng");
        attributeLabels.put("riskBehaviorID", "Hành vi nguy cơ");
        attributeLabels.put("modeOfTransmissionID", "Đường lây nhiễm");

        attributeLabels.put("permanentProvinceID", "Tỉnh/Thành thường trú");
        attributeLabels.put("permanentDistrictID", "Quận/huyện thường trú");
        attributeLabels.put("permanentWardID", "Xã/Phường thường trú");
        attributeLabels.put("permanentAddressNo", "Số nhà thường trú");
        attributeLabels.put("permanentAddressGroup", "Tổ/ấp/Khu phố thường trú");
        attributeLabels.put("permanentAddressStreet", "Đường phố thường trú");

        attributeLabels.put("currentProvinceID", "Tỉnh/Thành cư trú");
        attributeLabels.put("currentDistrictID", "Quận/huyện cư trú");
        attributeLabels.put("currentWardID", "Xã/Phường cư trú");
        attributeLabels.put("currentAddressNo", "Số nhà cư trú");
        attributeLabels.put("currentAddressGroup", "Tổ/ấp/Khu phố cư trú");
        attributeLabels.put("currentAddressStreet", "Đường phố cư trú");

        attributeLabels.put("typeOfPatientID", "Loại bệnh nhân");
        attributeLabels.put("confirmTime", "Ngày XN khẳng định");
        attributeLabels.put("siteConfirmID", "Cơ sở XN khẳng định");
        attributeLabels.put("bloodBaseID", "Nơi lấy mẫu");
        attributeLabels.put("statusOfTreatmentID", "Trạng thái điều trị");
        attributeLabels.put("deathTime", "Ngày tử vong");
        attributeLabels.put("note", "Ghi chú");
        attributeLabels.put("startTreatmentTime", "Ngày bắt đầu điều trị");
        attributeLabels.put("siteTreatmentFacilityID", "Cơ sở điều trị");
        attributeLabels.put("treatmentRegimenID", "Phác đồ điều trị");
        attributeLabels.put("symptomID", "Triệu chứng lâm sàng");
        attributeLabels.put("causeOfDeath", "Nguyên nhân tử vong");

        //update 04/09
        attributeLabels.put("patientPhone", "Số điện thoại");
        attributeLabels.put("confirmCode", "Mã XN khẳng định");
        attributeLabels.put("earlyHiv", "Kết quả XN nhiễm mới");
        attributeLabels.put("earlyHivTime", "Ngày XN nhiễm mới");
        attributeLabels.put("virusLoadConfirm", "Kết quả XN tải lượng virus");
        attributeLabels.put("virusLoadConfirmDate", "Ngày XN tải lượng virus");

        attributeLabels.put("cdFourResult", "Kết quả XN CD4 lần đầu tiên");
        attributeLabels.put("cdFourResultDate", "Ngày XN CD4 lần đầu tiên");
        attributeLabels.put("cdFourResultCurrent", "Kết quả XN CD4 hiện tại");
        attributeLabels.put("cdFourResultLastestDate", "Ngày XN CD4 lần gần đây nhất");
        attributeLabels.put("virusLoadArv", "Kết quả XN tải lượng virus lần đầu tiên");
        attributeLabels.put("virusLoadArvDate", "Ngày XN tải lượng virus lần đầu");
        attributeLabels.put("virusLoadArvCurrent", "KQ XN tải lượng virus hiện tại");
        attributeLabels.put("virusLoadArvLastestDate", "Ngày XN tải lượng gần nhất");
        attributeLabels.put("clinicalStage", "Giai đoạn lâm sàng hiện tại");

        attributeLabels.put("clinicalStageDate", "Ngày chẩn đoán lâm sàng");
        attributeLabels.put("opcCode", "Mã bệnh án");
        attributeLabels.put("aidsStatus", "Tình trạng AIDS");
        attributeLabels.put("aidsStatusDate", "Ngày chẩn đoán AIDS");
        attributeLabels.put("statusOfChangeTreatmentID", "Trạng thái biến động điều trị");
        attributeLabels.put("changeTreatmentDate", "Ngày biến động điều trị");
        attributeLabels.put("requestDeathTime", "Ngày báo tử vong");
        attributeLabels.put("note", "Ghi chú");

        attributeLabels.put("firstTreatmentTime", "Ngày ARV đầu tiên");
        attributeLabels.put("firstTreatmentRegimenId", "Phác đồ điều trị đầu tiên");
        attributeLabels.put("registrationTime", "Ngày đăng ký");
        attributeLabels.put("registrationType", "Loại đăng ký");
        attributeLabels.put("endTime", "Ngày kết thúc");
        attributeLabels.put("endCase", "Lý do kết thúc");
    }

    public boolean outProvince() {
        return (provinceID != null && permanentProvinceID != null && !provinceID.equals(permanentProvinceID))
                || (districtID != null && permanentDistrictID != null && !districtID.equals(permanentDistrictID))
                || (wardID != null && permanentWardID != null && !wardID.equals(permanentWardID));
    }
}

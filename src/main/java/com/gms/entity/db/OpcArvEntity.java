package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Bệnh án
 */
@Entity
@Table(name = "OPC_ARV",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CODE", "SITE_ID"})
        },
        indexes = {
            @Index(name = "OR_SITE_ID", columnList = "SITE_ID")
            ,@Index(name = "OR_PATIENT_ID", columnList = "PATIENT_ID")
            ,@Index(name = "OR_IS_REMOVE", columnList = "IS_REMOVE")
            ,@Index(name = "OR_PERMANENT_PROVINCE_ID", columnList = "PERMANENT_PROVINCE_ID")
            ,@Index(name = "OR_PERMANENT_DISTRICT_ID", columnList = "PERMANENT_DISTRICT_ID")
            ,@Index(name = "OR_PERMANENT_WARD_ID", columnList = "PERMANENT_WARD_ID")
            ,@Index(name = "OR_CURRENT_PROVINCE_ID", columnList = "CURRENT_PROVINCE_ID")
            ,@Index(name = "OR_CURRENT_DISTRICT_ID", columnList = "CURRENT_DISTRICT_ID")
            ,@Index(name = "OR_CURRENT_WARD_ID", columnList = "CURRENT_WARD_ID")
        })
@DynamicUpdate
@DynamicInsert
public class OpcArvEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "CODE", length = 50, nullable = false)
    private String code; //Mã

    @Column(name = "SOURCE_SERVICE_ID", length = 50, nullable = true)
    private String sourceServiceID; //Loại nguồn dịch vụ gửi đến

    @Column(name = "SOURCE_SITE_ID", nullable = true)
    private Long sourceSiteID; //mã cơ sở nguồn gửi đến

    @Column(name = "SOURCE_ARV_SITE_ID", nullable = true)
    private Long sourceArvSiteID; //mã cơ sở ARV chuyển gửi đến

    @Column(name = "SOURCE_ARV_SITE_NAME", nullable = true)
    private String sourceArvSiteName; //Cơ sở arv chuyển gửi

    @Column(name = "SOURCE_CODE", length = 50, nullable = true)
    private String sourceArvCode; //Mã bệnh án từ cơ sở khác chuyển tới

    @Column(name = "SOURCE_ID", nullable = true)
    private Long sourceID; //mã code đối tượng được gửi đến

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý

    @Column(name = "PATIENT_ID")
    private Long patientID; //FK thông tin cơ bản của bệnh nhân

    @Column(name = "JOB_ID", length = 50, nullable = true)
    private String jobID; //Nghề nghiệp

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "INSURANCE", length = 50, nullable = true)
    private String insurance; //Có thẻ BHYT ?

    @Column(name = "INSURANCE_NO", length = 50, nullable = true)
    private String insuranceNo; //Số thẻ BHYT

    @Column(name = "INSURANCE_TYPE", length = 3, nullable = true)
    private String insuranceType; //Loại thanh toán thẻ BHYT - Sử dụng enum

    @Column(name = "INSURANCE_SITE", nullable = true)
    private String insuranceSite; //Nơi đăng ký kcb ban đầu

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSURANCE_EXPIRY", nullable = true)
    private Date insuranceExpiry; //Ngày hết hạn thẻ BHYT

    @Column(name = "INSURANCE_PAY", length = 3, nullable = true)
    private String insurancePay; //Tỉ lệ thanh toán thẻ BHYT - Sử dụng enum

    @Column(name = "PATIENT_PHONE", length = 13, nullable = true)
    private String patientPhone; //Số điện thoại bệnh nhân

    @Column(name = "PERMANENT_ADDRESS", length = 500, nullable = true)
    private String permanentAddress; //Địa chỉ thường trú

    @Column(name = "PERMANENT_PROVINCE_ID", length = 5, nullable = true)
    private String permanentProvinceID;

    @Column(name = "PERMANENT_DISTRICT_ID", length = 5, nullable = true)
    private String permanentDistrictID;

    @Column(name = "PERMANENT_WARD_ID", length = 5, nullable = true)
    private String permanentWardID;

    @Column(name = "CURRENT_ADDRESS", length = 500, nullable = true)
    private String currentAddress; //Địa chỉ cư trú

    @Column(name = "CURRENT_PROVINCE_ID", length = 5, nullable = true)
    private String currentProvinceID;

    @Column(name = "CURRENT_DISTRICT_ID", length = 5, nullable = true)
    private String currentDistrictID;

    @Column(name = "CURRENT_WARD_ID", length = 5, nullable = true)
    private String currentWardID;

    @Column(name = "SUPPORTER_NAME", length = 100)
    private String supporterName; //Họ và tên người hỗ trợ

    @Column(name = "SUPPORTER_RELATION", length = 50, nullable = true)
    private String supporterRelation; //Quan hệ với bệnh nhân

    @Column(name = "SUPPORTER_PHONE", length = 13)
    private String supporterPhone; //Số điện thoại người hỗ trợ

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_TIME", nullable = false)
    private Date registrationTime; //NGày đăng ký vào OPC

     @Column(name = "REGISTRATION_TYPE", length = 1, nullable = true)
    private String registrationType; //Loại đăng ký - Sử dụng enum

    @Column(name = "CLINICAL_STAGE", length = 50, nullable = true)
    private String clinicalStage; // Giai đoạn lâm sàng

    @Column(name = "CD4", nullable = true)
    private String cd4; //CD4 hoặc cd4%

    @Column(name = "LAO", nullable = false)
    private boolean lao; //Có sàng lọc lao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_TEST_TIME", nullable = true)
    private Date laoTestTime; //Ngày xét nghiệm lao

    @Column(name = "LAO_OTHER_SYMPTOM", nullable = true)
    private String laoOtherSymptom; // triệu chứng khác

    @Column(name = "LAO_TREATMENT", nullable = false)
    private boolean laoTreatment; //Có điều trị lọc lao

    @Column(name = "LAO_RESULT", length = 50, nullable = true)
    private String laoResult; //Kết quả xét nghiệm Lao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_START_TIME", nullable = true)
    private Date laoStartTime; //Ngày bắt đầu điều trị lao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_END_TIME", nullable = true)
    private Date laoEndTime; //Ngày kết thúc điều trị lao

    @Column(name = "INH", nullable = false)
    private boolean inh; //Điều trị dự phòng INH - sử dụng enum

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INH_FROM_TIME", nullable = true)
    private Date inhFromTime; //Lao từ ngày

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INH_TO_TIME", nullable = true)
    private Date inhToTime; //Lao đến ngày

    @Column(name = "NTCH", nullable = false)
    private boolean ntch; //Nhiễm trùng cơ hội - ntch - sử dụng enum

    @Column(name = "NTCH_OTHER_SYMPTOM", nullable = true)
    private String ntchOtherSymptom; // triệu chứng khác ntch

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COTRIMOXAZOLE_FROM_TIME", nullable = true)
    private Date cotrimoxazoleFromTime; //cotrimoxazole từ ngày

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COTRIMOXAZOLE_TO_TIME", nullable = true)
    private Date cotrimoxazoleToTime; //cotrimoxazole đến ngày

    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị

    @Column(name = "TREATMENT_STAGE_ID", length = 50, nullable = true)
    private String treatmentStageID; //Trạng thái biến động điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TREATMENT_STAGE_TIME", nullable = true)
    private Date treatmentStageTime; //Ngày biến động điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_TREATMENT_TIME", nullable = true)
    private Date firstTreatmentTime; //Ngày ARV đầu tiên

    @Column(name = "FIRST_TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String firstTreatmentRegimenID; //Phác đồ điều trị đầu tiên - liên kết với treatment-regimen

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TREATMENT_TIME", nullable = true)
    private Date treatmentTime; //Ngày ARV tại cơ sở OPC

    @Column(name = "TREATMENT_REGIMENT_STAGE", length = 1, nullable = true)
    private String treatmentRegimenStage; //Bậc phác đồ điều trị - sử dụng enum

    @Column(name = "TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String treatmentRegimenID; //Phác đồ điều trị tại cơ sở OPC

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_EXAMINATION_TIME", nullable = true)
    private Date lastExaminationTime; //Ngày khám bệnh gần nhất

    @Column(name = "MEDICATION_ADHERENCE", nullable = true)
    private String medicationAdherence; //Tuân thủ điều trị

    @Column(name = "DAYS_RECEIVED", nullable = true)
    private int daysReceived; //Số ngày nhận thuốc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPOINMENT_TIME", nullable = true)
    private Date appointmentTime; // Ngày hẹn khám   

    @Column(name = "EXAMINATION_NOTE", nullable = true)
    private String examinationNote; //Các vấn đề trong lần khám gần nhất

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_CD4_TIME", nullable = true)
    private Date firstCd4Time; //Ngày xét nghiệm cd4 đầu tiên

    @Column(name = "FIRST_CD4_RESULT", nullable = true)
    private String firstCd4Result; //Kết quả xét nghiệm cd4

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CD4_TIME", nullable = true)
    private Date cd4Time; //Ngày xét nghiệm cd4

    @Column(name = "CD4_RESULT", nullable = true)
    private String cd4Result; //Kết quả xét nghiệm cd4 gần nhất

    @Column(name = "FIRST_VIRAL_LOAD_RESULT", length = 50, nullable = true)
    private String firstViralLoadResult; //Kết quả xét nghiệm TLVR

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_VIRAL_LOAD_TIME", nullable = true)
    private Date firstViralLoadTime; //TLVR - Ngày xét nghiệm đầu tiên

    @Column(name = "VIRAL_LOAD_RESULT", length = 50, nullable = true)
    private String viralLoadResult; //Kết quả xét nghiệm TLVR

    @Column(name = "VIRAL_LOAD_RESULT_NUMBER", length = 50, nullable = true)
    private String viralLoadResultNumber; //Kết quả xét nghiệm TLVR - nhập số

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIRAL_LOAD_TIME", nullable = true)
    private Date viralLoadTime; //TLVR - Ngày xét nghiệm

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIRAL_LOAD_RETRY_TIME", nullable = true)
    private Date viralLoadRetryTime; //TLVR - Ngày hẹn xét nghiệm lại

    @Column(name = "HBV", nullable = false)
    private boolean hbv; //Có xn hbv hay không - sử dụng enum

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HBV_TIME", nullable = true)
    private Date hbvTime; // Ngày xét nghiệm hbv

    @Column(name = "HBV_RESULT", length = 1, nullable = true)
    private String hbvResult; //Kết quả XN HBV

    @Column(name = "HBV_CASE", length = 500, nullable = true)
    private String hbvCase; //Lý do XN HBV

    @Column(name = "HCV", nullable = false)
    private boolean hcv; //Có xn HCV hay không - sử dụng enum

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HCV_TIME", nullable = true)
    private Date hcvTime; // Ngày xét nghiệm HCV

    @Column(name = "HCV_RESULT", length = 1, nullable = true)
    private String hcvResult; //Kết quả XN HCV

    @Column(name = "HCV_CASE", length = 500, nullable = true)
    private String hcvCase; //Lý do XN HCV

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", nullable = true)
    private Date endTime; // Ngày kết thúc tại OPC

    @Column(name = "END_CASE", length = 1, nullable = true)
    private String endCase; //Lý do kết thúc điều trị

    @Column(name = "TRANSFER_SITE_ID", nullable = true)
    private Long transferSiteID; //Cơ sở chuyển đi

    @Column(name = "TRANSFER_SITE_NAME", nullable = true)
    private String transferSiteName; //Tên Cơ sở chuyển đi

    @Column(name = "TRANSFER_CASE", length = 200, nullable = true)
    private String transferCase; //Lý do chuyển đi

    @Column(name = "NOTE", length = 500, nullable = true)
    private String note; //Lý do

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

    @Column(name = "IS_REMOVE", nullable = false)
    private boolean remove; //Xoá logic

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REMOVE_AT", nullable = true)
    private Date remoteAT;

    //Bổ sung PCR
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PCR_ONE_TIME", nullable = true)
    private Date pcrOneTime; //Ngày xn pcr lần 01

    @Column(name = "PCR_ONE_RESULT", length = 50, nullable = true)
    private String pcrOneResult; //Kết quả xn pcr lần 01

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PCR_TWO_TIME", nullable = true)
    private Date pcrTwoTime; //Ngày xn pcr lần 02

    @Column(name = "PCR_TWO_RESULT", length = 50, nullable = true)
    private String pcrTwoResult; //Kết quả xn pcr lần 02

    //Chuyển gửi
    @Column(name = "IS_REMOVE_TRANFER", nullable = false)
    private boolean removeTranfer; //Xoá logic khi chuyển gửi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANFER_FROM_TIME", nullable = true)
    private Date tranferFromTime; //Ngày chuyển gửi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANFER_TO_TIME", nullable = true)
    private Date tranferToTime; //Ngày tiếp nhận từ cơ sở chuyến đến

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANFER_TO_TIME_OPC", nullable = true)
    private Date tranferToTimeOpc; //Ngày tiếp nhận cơ sở chuyển đến

    @Column(name = "TRANFER_BY", nullable = true)
    private Long tranferBY; //Cán bộ chuyển gửi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEDBACK_RESULTS_RECEIVED_TIME", nullable = true)
    private Date feedbackResultsReceivedTime; //Ngày phản hồi kết quả tiếp nhận của cơ sở chuyển đến

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEDBACK_RESULTS_RECEIVED_TIME_OPC", nullable = true)
    private Date feedbackResultsReceivedTimeOpc; //Ngày phản hồi kết quả tiếp nhận về cơ sở chuyển đi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_OF_ARRIVAL", nullable = true)
    private Date dateOfArrival; //Ngày chuyển đến trên phiếu - khi loại đăng ký là chuyển đến

    @Column(name = "DATA_TYPE", length = 5, nullable = true)
    private String dataType; //Loại dữ liệu: ims/vnpt

    @Column(name = "DATA_ID", length = 50, nullable = true)
    private String dataID; //mã bệnh án bên nguồn đồng bộ

    @Column(name = "DATA_PATIENT_ID", length = 50, nullable = true)
    private String dataPatientID; //mã bệnh nhân bên nguồn đồng bộ
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIVED_WARD_DATE", length = 220, nullable = true)
    private Date receivedWardDate; //Nhận thuốc tại tuyến xã
    
    @Column(name = "RECEIVED_WARD", length = 220, nullable = true)
    private String receivedWard; //Nhận thuốc tại tuyến xã

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANSFER_TIME_GSPH", nullable = true)
    private Date transferTimeGSPH; // Ngày chuyển gửi GSPH

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME_GSPH", nullable = true)
    private Date updateTimeGSPH; // Ngày cập nhật sang GSPH

    @Column(name = "REGISTRATION_STATUS", length = 500, nullable = true)
    private String registrationStatus; //Tình trạng đăng ký

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "QUALIFILED_TREATMENT_TIME", nullable = true)
    private Date qualifiedTreatmentTime; // Ngày đủ tiêu chuẩn điều trị

    @Column(name = "GSPH_ID", nullable = true)
    private Long gsphID; // Mã bệnh nhân bên QLNN

    @Column(name = "PATIENT_WEIGHT", nullable = false)
    private float patientWeight; //Câng nặng

    @Column(name = "PATIENT_HEIGHT", nullable = false)
    private float patientHeight; //Chiều cao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DEATH_TIME", nullable = true)
    private Date deathTime; // Ngày tử vong
    
    @Column(name = "ROUTE_ID", length = 50, nullable = true)
    private String routeID; // Tuyến đăng ký bảo hiểm
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUPPLY_MEDICINE_DATE", nullable = true)
    private Date supplyMedicinceDate; // Ngày đầu tiên cấp thuốc nhiều tháng
    
    @Column(name = "PERMANENT_ADDRESS_STREET", length = 500, nullable = true)
    private String permanentAddressStreet; // Đường phố thương trú

    @Column(name = "PERMANENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String permanentAddressGroup; // Tổ ấp khu phố thường trú

    @Column(name = "CURRENT_ADDRESS_STREET", length = 500, nullable = true)
    private String currentAddressStreet; // Đường phố hiện tại

    @Column(name = "CURRENT_ADDRESS_GROUP", length = 500, nullable = true)
    private String currentAddressGroup; // Tổ ấp khu phố hiện tại
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_START_DATE", nullable = true)
    private Date pregnantStartDate; // Ngày bắt đầu thai ký
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_END_DATE", nullable = true)
    private Date pregnantEndDate; // Ngày chuyển dạ đẻ
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JOIN_BORN_DATE", nullable = true)
    private Date joinBornDate; // Ngày dự sinh
    
    @Column(name = "SUSPICIOUS_SYMPTOMS", length = 50, nullable = true)
    private String suspiciousSymptoms; // Triệu chứng nghi ngờ
    
    @Column(name = "EXAMINATION_AND_TEST", nullable = false)
    private boolean examinationAndTest; //Chuyển gửi khám và xét nghiệm
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_TEST_DATE", nullable = true)
    private Date laoTestDate; // Ngày xét nghiệm Lao
    
    @Column(name = "LAO_TEST_RESULTS", length = 50, nullable = true)
    private String laoTestResults; // Kết quả xét nghiệm Lao
    
    @Column(name = "LAO_DIAGNOSE", length = 50, nullable = true)
    private String laoDiagnose; // Chẩn đoán loại hình Lao
    
    @Column(name = "COTRIMOXAZOLE_OTHER_END_CAUSE", length = 255, nullable = true)
    private String cotrimoxazoleOtherEndCause; // Lý do kết thúc cotrimoxazole khác
    
    @Column(name = "PASS_TREATMENT", nullable = false)
    private boolean passTreatment; // Đạt tiêu chuẩn điều trị
    
    @Column(name = "QUICK_BY_TREATMENT_TIME", length = 50, nullable = true)
    private String quickByTreatmentTime; // ĐT nhanh tính theo ngày ĐK và ngày ĐT
    
    @Column(name = "QUICK_BY_CONFIRM_TIME", length = 50, nullable = true)
    private String quickByConfirmTime; // ĐT nhanh tính theo ngày XNKĐ
    
    //Trường chọn nhiều
    @Transient
    private List<String> laoSymptoms; //D3. Các biểu hiện nghi lao

    @Transient
    private List<String> inhEndCauses; //D8. Lý do kết thúc lao - inh

    @Transient
    private List<String> ntchSymptoms; //D10. Các biểu hiện ntch

    @Transient
    private List<String> cotrimoxazoleEndCauses; //Lý do kết thúc cotrimoxazole

    @Transient
    private List<String> firstCd4Causes; //F3. Lý do xét nghiệm cd4 đầu tiên

    @Transient
    private List<String> cd4Causes; //F6. Lý do xét nghiệm cd4 gần nhất

    @Transient
    private List<String> firstViralLoadCauses; //G3. TLVR - lý do xét nghiệm  đầu tiên

    @Transient
    private List<String> viralLoadCauses; //G6. TLVR - lý do xét nghiệm

    @Transient
    private String permanentAddressFull;

    @Transient
    private String currentAddressFull;

    @Transient
    private OpcPatientEntity patient;

    public String getQuickByTreatmentTime() {
        return quickByTreatmentTime;
    }

    public void setQuickByTreatmentTime(String quickByTreatmentTime) {
        this.quickByTreatmentTime = quickByTreatmentTime;
    }

    public String getQuickByConfirmTime() {
        return quickByConfirmTime;
    }

    public void setQuickByConfirmTime(String quickByConfirmTime) {
        this.quickByConfirmTime = quickByConfirmTime;
    }


    public boolean isPassTreatment() {
        return passTreatment;
    }

    public void setPassTreatment(boolean passTreatment) {
        this.passTreatment = passTreatment;
    }

    public String getCotrimoxazoleOtherEndCause() {
        return cotrimoxazoleOtherEndCause;
    }

    public void setCotrimoxazoleOtherEndCause(String cotrimoxazoleOtherEndCause) {
        this.cotrimoxazoleOtherEndCause = cotrimoxazoleOtherEndCause;
    }

    public String getSuspiciousSymptoms() {
        return suspiciousSymptoms;
    }

    public void setSuspiciousSymptoms(String suspiciousSymptoms) {
        this.suspiciousSymptoms = suspiciousSymptoms;
    }

    public boolean isExaminationAndTest() {
        return examinationAndTest;
    }

    public void setExaminationAndTest(boolean examinationAndTest) {
        this.examinationAndTest = examinationAndTest;
    }

    public Date getLaoTestDate() {
        return laoTestDate;
    }

    public void setLaoTestDate(Date laoTestDate) {
        this.laoTestDate = laoTestDate;
    }

    public String getLaoTestResults() {
        return laoTestResults;
    }

    public void setLaoTestResults(String laoTestResults) {
        this.laoTestResults = laoTestResults;
    }

    public String getLaoDiagnose() {
        return laoDiagnose;
    }

    public void setLaoDiagnose(String laoDiagnose) {
        this.laoDiagnose = laoDiagnose;
    }

    public Date getJoinBornDate() {
        return joinBornDate;
    }

    public void setJoinBornDate(Date joinBornDate) {
        this.joinBornDate = joinBornDate;
    }

    public Date getPregnantStartDate() {
        return pregnantStartDate;
    }

    public void setPregnantStartDate(Date pregnantStartDate) {
        this.pregnantStartDate = pregnantStartDate;
    }

    public Date getPregnantEndDate() {
        return pregnantEndDate;
    }

    public void setPregnantEndDate(Date pregnantEndDate) {
        this.pregnantEndDate = pregnantEndDate;
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

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }
    
    public Date getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(Date deathTime) {
        this.deathTime = deathTime;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public Date getQualifiedTreatmentTime() {
        return qualifiedTreatmentTime;
    }

    public void setQualifiedTreatmentTime(Date qualifiedTreatmentTime) {
        this.qualifiedTreatmentTime = qualifiedTreatmentTime;
    }

    public Date getTransferTimeGSPH() {
        return transferTimeGSPH;
    }

    public void setTransferTimeGSPH(Date transferTimeGSPH) {
        this.transferTimeGSPH = transferTimeGSPH;
    }

    public Date getUpdateTimeGSPH() {
        return updateTimeGSPH;
    }

    public void setUpdateTimeGSPH(Date updateTimeGSPH) {
        this.updateTimeGSPH = updateTimeGSPH;
    }

    public Long getGsphID() {
        return gsphID;
    }

    public void setGsphID(Long gsphID) {
        this.gsphID = gsphID;
    }

    public OpcArvEntity() {
        this.laoTreatment = false;
        this.patientHeight = 0;
        this.patientWeight = 0;
    }

    public float getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(float patientWeight) {
        this.patientWeight = patientWeight;
    }

    public float getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(float patientHeight) {
        this.patientHeight = patientHeight;
    }

    public Date getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(Date receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
    }

    public boolean isLaoTreatment() {
        return laoTreatment;
    }

    public void setLaoTreatment(boolean laoTreatment) {
        this.laoTreatment = laoTreatment;
    }

    public String getLaoResult() {
        return laoResult;
    }

    public void setLaoResult(String laoResult) {
        this.laoResult = laoResult;
    }

    public Date getLaoStartTime() {
        return laoStartTime;
    }

    public void setLaoStartTime(Date laoStartTime) {
        this.laoStartTime = laoStartTime;
    }

    public Date getLaoEndTime() {
        return laoEndTime;
    }

    public void setLaoEndTime(Date laoEndTime) {
        this.laoEndTime = laoEndTime;
    }

    public String getViralLoadResultNumber() {
        return viralLoadResultNumber;
    }

    public void setViralLoadResultNumber(String viralLoadResultNumber) {
        this.viralLoadResultNumber = viralLoadResultNumber;
    }

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(Date dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public Date getTranferToTimeOpc() {
        return tranferToTimeOpc;
    }

    public void setTranferToTimeOpc(Date tranferToTimeOpc) {
        this.tranferToTimeOpc = tranferToTimeOpc;
    }

    public Date getFeedbackResultsReceivedTimeOpc() {
        return feedbackResultsReceivedTimeOpc;
    }

    public void setFeedbackResultsReceivedTimeOpc(Date feedbackResultsReceivedTimeOpc) {
        this.feedbackResultsReceivedTimeOpc = feedbackResultsReceivedTimeOpc;
    }

    public Date getViralLoadRetryTime() {
        return viralLoadRetryTime;
    }

    public void setViralLoadRetryTime(Date viralLoadRetryTime) {
        this.viralLoadRetryTime = viralLoadRetryTime;
    }

    public String getDataPatientID() {
        return dataPatientID;
    }

    public void setDataPatientID(String dataPatientID) {
        this.dataPatientID = dataPatientID;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public Date getFeedbackResultsReceivedTime() {
        return feedbackResultsReceivedTime;
    }

    public void setFeedbackResultsReceivedTime(Date feedbackResultsReceivedTime) {
        this.feedbackResultsReceivedTime = feedbackResultsReceivedTime;
    }

    public Date getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(Date tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

    public Date getTranferToTime() {
        return tranferToTime;
    }

    public void setTranferToTime(Date tranferToTime) {
        this.tranferToTime = tranferToTime;
    }

    public Long getTranferBY() {
        return tranferBY;
    }

    public void setTranferBY(Long tranferBY) {
        this.tranferBY = tranferBY;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
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

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public Long getSourceArvSiteID() {
        return sourceArvSiteID;
    }

    public void setSourceArvSiteID(Long sourceArvSiteID) {
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

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
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

    public Date getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(Date insuranceExpiry) {
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

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
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

    public Date getLaoTestTime() {
        return laoTestTime;
    }

    public void setLaoTestTime(Date laoTestTime) {
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

    public Date getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(Date inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

    public Date getInhToTime() {
        return inhToTime;
    }

    public void setInhToTime(Date inhToTime) {
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

    public Date getCotrimoxazoleFromTime() {
        return cotrimoxazoleFromTime;
    }

    public void setCotrimoxazoleFromTime(Date cotrimoxazoleFromTime) {
        this.cotrimoxazoleFromTime = cotrimoxazoleFromTime;
    }

    public Date getCotrimoxazoleToTime() {
        return cotrimoxazoleToTime;
    }

    public void setCotrimoxazoleToTime(Date cotrimoxazoleToTime) {
        this.cotrimoxazoleToTime = cotrimoxazoleToTime;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getTreatmentStageID() {
        return treatmentStageID;
    }

    public void setTreatmentStageID(String treatmentStageID) {
        this.treatmentStageID = treatmentStageID;
    }

    public Date getTreatmentStageTime() {
        return treatmentStageTime;
    }

    public void setTreatmentStageTime(Date treatmentStageTime) {
        this.treatmentStageTime = treatmentStageTime;
    }

    public Date getFirstTreatmentTime() {
        return firstTreatmentTime;
    }

    public void setFirstTreatmentTime(Date firstTreatmentTime) {
        this.firstTreatmentTime = firstTreatmentTime;
    }

    public String getFirstTreatmentRegimenID() {
        return firstTreatmentRegimenID;
    }

    public void setFirstTreatmentRegimenID(String firstTreatmentRegimenID) {
        this.firstTreatmentRegimenID = firstTreatmentRegimenID;
    }

    public Date getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(Date treatmentTime) {
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

    public Date getLastExaminationTime() {
        return lastExaminationTime;
    }

    public void setLastExaminationTime(Date lastExaminationTime) {
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

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getExaminationNote() {
        return examinationNote;
    }

    public void setExaminationNote(String examinationNote) {
        this.examinationNote = examinationNote;
    }

    public Date getFirstCd4Time() {
        return firstCd4Time;
    }

    public void setFirstCd4Time(Date firstCd4Time) {
        this.firstCd4Time = firstCd4Time;
    }

    public String getFirstCd4Result() {
        return firstCd4Result;
    }

    public void setFirstCd4Result(String firstCd4Result) {
        this.firstCd4Result = firstCd4Result;
    }

    public Date getCd4Time() {
        return cd4Time;
    }

    public void setCd4Time(Date cd4Time) {
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

    public Date getFirstViralLoadTime() {
        return firstViralLoadTime;
    }

    public void setFirstViralLoadTime(Date firstViralLoadTime) {
        this.firstViralLoadTime = firstViralLoadTime;
    }

    public String getViralLoadResult() {
        return viralLoadResult;
    }

    public void setViralLoadResult(String viralLoadResult) {
        this.viralLoadResult = viralLoadResult;
    }

    public Date getViralLoadTime() {
        return viralLoadTime;
    }

    public void setViralLoadTime(Date viralLoadTime) {
        this.viralLoadTime = viralLoadTime;
    }

    public boolean isHbv() {
        return hbv;
    }

    public void setHbv(boolean hbv) {
        this.hbv = hbv;
    }

    public Date getHbvTime() {
        return hbvTime;
    }

    public void setHbvTime(Date hbvTime) {
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

    public Date getHcvTime() {
        return hcvTime;
    }

    public void setHcvTime(Date hcvTime) {
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public Long getTransferSiteID() {
        return transferSiteID;
    }

    public void setTransferSiteID(Long transferSiteID) {
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

    public boolean isRemoveTranfer() {
        return removeTranfer;
    }

    public void setRemoveTranfer(boolean removeTranfer) {
        this.removeTranfer = removeTranfer;
    }

    public Date getRemoteAT() {
        return remoteAT;
    }

    public void setRemoteAT(Date remoteAT) {
        this.remoteAT = remoteAT;
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

    public List<String> getFirstCd4Causes() {
        return firstCd4Causes;
    }

    public void setFirstCd4Causes(List<String> firstCd4Causes) {
        this.firstCd4Causes = firstCd4Causes;
    }

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

    public OpcPatientEntity getPatient() {
        return patient;
    }

    public void setPatient(OpcPatientEntity patient) {
        this.patient = patient;
    }

    public Date getPcrOneTime() {
        return pcrOneTime;
    }

    public void setPcrOneTime(Date pcrOneTime) {
        this.pcrOneTime = pcrOneTime;
    }

    public String getPcrOneResult() {
        return pcrOneResult;
    }

    public void setPcrOneResult(String pcrOneResult) {
        this.pcrOneResult = pcrOneResult;
    }

    public Date getPcrTwoTime() {
        return pcrTwoTime;
    }

    public void setPcrTwoTime(Date pcrTwoTime) {
        this.pcrTwoTime = pcrTwoTime;
    }

    public String getPcrTwoResult() {
        return pcrTwoResult;
    }

    public void setPcrTwoResult(String pcrTwoResult) {
        this.pcrTwoResult = pcrTwoResult;
    }

    @Override
    public OpcArvEntity clone() throws CloneNotSupportedException {
        return (OpcArvEntity) super.clone();
    }

    public Date getSupplyMedicinceDate() {
        return supplyMedicinceDate;
    }

    public void setSupplyMedicinceDate(Date supplyMedicinceDate) {
        this.supplyMedicinceDate = supplyMedicinceDate;
    }

    public String getReceivedWard() {
        return receivedWard;
    }

    public void setReceivedWard(String receivedWard) {
        this.receivedWard = receivedWard;
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

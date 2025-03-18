package com.gms.entity.db;

import com.gms.components.TextUtils;
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
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Giai đoạn Điều trị
 */
@Entity
@Table(name = "OPC_STAGE", indexes = {
    @Index(name = "OS_SITE_ID", columnList = "SITE_ID")
    ,@Index(name = "OS_ARV_ID", columnList = "ARV_ID")
    ,@Index(name = "OS_PATIENT_ID", columnList = "PATIENT_ID")
    ,@Index(name = "OS_IS_REMOVE", columnList = "IS_REMOVE")
    ,@Index(name = "OS_END_TIME", columnList = "END_TIME")
    ,@Index(name = "OS_TRANFER_FROM_TIME", columnList = "TRANFER_FROM_TIME")
    ,@Index(name = "OS_REGISTRATION_TIME", columnList = "REGISTRATION_TIME")
})
@DynamicUpdate
@DynamicInsert
public class OpcStageEntity extends BaseEntity implements Serializable, Cloneable {

    private static final String FORMATDATE = "dd/MM/yyyy";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Column(name = "PATIENT_ID")
    private Long patientID; //FK thông tin cơ bản của bệnh nhân

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_TIME", nullable = false)
    private Date registrationTime; //NGày đăng ký vào OPC

    @Column(name = "REGISTRATION_TYPE", length = 1, nullable = true)
    private String registrationType; //Loại đăng ký - Sử dụng enum

    @Column(name = "SOURCE_SITE_ID", nullable = true)
    private Long sourceSiteID; //mã cơ sở nguồn gửi đến - Tự động lấy từ place-test hoặc nguồn chuyển

    @Column(name = "SOURCE_SITE_NAME", nullable = true)
    private String sourceSiteName; //Tên cơ sở nguồn gửi đến

    @Column(name = "SOURCE_CODE", length = 50, nullable = true)
    private String sourceCode; //Mã bệnh án từ cơ sở khác chuyển tới

    @Column(name = "CLINICAL_STAGE", length = 50, nullable = true)
    private String clinicalStage; // Giai đoạn lâm sàng

    @Column(name = "CD4", nullable = true)
    private String cd4; //CD4 hoặc cd4%

    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị

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

    @Column(name = "IS_REMOVE")
    private boolean remove; //Xoá logic

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REMOVE_AT", nullable = true)
    private Date remoteAT;

    @Column(name = "TREATMENT_STAGE_ID", length = 50, nullable = true)
    private String treatmentStageID; //Trạng thái biến động điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TREATMENT_STAGE_TIME", nullable = true)
    private Date treatmentStageTime; //Ngày biến động điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANFER_FROM_TIME", nullable = true)
    private Date tranferFromTime; //Ngày chuyển gửi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANFER_TO_TIME", nullable = true)
    private Date tranferToTime; //Ngày tiếp nhận khi đã chuyển

    @Column(name = "TRANFER_BY", nullable = true)
    private Long tranferBY; //Cán bộ chuyển gửi

    @Column(name = "DATA_TYPE", length = 5, nullable = true)
    private String dataType; //Loại dữ liệu: ims/vnpt

    @Column(name = "DATA_ID", length = 50, nullable = true)
    private String dataID; //mã bên nguồn đồng bộ

    @Column(name = "OLD_TREATMENT_REGIMENT_STAGE", length = 1, nullable = true)
    private String oldTreatmentRegimenStage; //Bậc phác đồ điều trị cũ- sử dụng enum 

    @Column(name = "OLD_TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String oldTreatmentRegimenID; //Phác đồ điều trị cũ tại cơ sở OPC    

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGIMENT_STAGE_DATE", nullable = true)
    private Date regimenStageDate; // Ngày thay đổi bậc phác đồ
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGIMENT_DATE", nullable = true)
    private Date regimenDate; // Ngày thay đổi phác đồ
    
    @Column(name = "CAUSES_CHANGE", length = 50, nullable = true)
    private String causesChange; // Lý do thay đổi
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUPPLY_MEDICINE_DATE", nullable = true)
    private Date supplyMedicinceDate; // Ngày đầu tiên cáp thuốc nhiều tháng
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIVED_WARD_DATE", nullable = true)
    private Date receivedWardDate; // Ngày nhận thuốc tại xã
    
    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_START_DATE", nullable = true)
    private Date pregnantStartDate; // Ngày bắt đầu thai ký
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_END_DATE", nullable = true)
    private Date pregnantEndDate; // Ngày chuyển dạ đẻ
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTH_PLAN_DATE", nullable = true)
    private Date birthPlanDate; // Ngày dự sinh
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "QUALIFILED_TREATMENT_TIME", nullable = true)
    private Date qualifiedTreatmentTime; // Ngày đủ tiêu chuẩn điều trị

    public Date getBirthPlanDate() {
        return birthPlanDate;
    }

    public void setBirthPlanDate(Date birthPlanDate) {
        this.birthPlanDate = birthPlanDate;
    }
    
    public Date getQualifiedTreatmentTime() {
        return qualifiedTreatmentTime;
    }

    public void setQualifiedTreatmentTime(Date qualifiedTreatmentTime) {
        this.qualifiedTreatmentTime = qualifiedTreatmentTime;
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
    
    public String getOldTreatmentRegimenStage() {
        return oldTreatmentRegimenStage;
    }

    public void setOldTreatmentRegimenStage(String oldTreatmentRegimenStage) {
        this.oldTreatmentRegimenStage = oldTreatmentRegimenStage;
    }

    public String getOldTreatmentRegimenID() {
        return oldTreatmentRegimenID;
    }

    public void setOldTreatmentRegimenID(String oldTreatmentRegimenID) {
        this.oldTreatmentRegimenID = oldTreatmentRegimenID;
    }

    public Date getRegimenStageDate() {
        return regimenStageDate;
    }

    public void setRegimenStageDate(Date regimenStageDate) {
        this.regimenStageDate = regimenStageDate;
    }

    public Date getRegimenDate() {
        return regimenDate;
    }

    public void setRegimenDate(Date regimenDate) {
        this.regimenDate = regimenDate;
    }

    public String getCausesChange() {
        return causesChange;
    }

    public void setCausesChange(String causesChange) {
        this.causesChange = causesChange;
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

    public Long getArvID() {
        return arvID;
    }

    public void setArvID(Long arvID) {
        this.arvID = arvID;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
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

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getSourceSiteName() {
        return sourceSiteName;
    }

    public void setSourceSiteName(String sourceSiteName) {
        this.sourceSiteName = sourceSiteName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
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

    public Date getRemoteAT() {
        return remoteAT;
    }

    public void setRemoteAT(Date remoteAT) {
        this.remoteAT = remoteAT;
    }

    @Override
    public OpcStageEntity clone() throws CloneNotSupportedException {
        return (OpcStageEntity) super.clone();
    }

    public Date getSupplyMedicinceDate() {
        return supplyMedicinceDate;
    }

    public void setSupplyMedicinceDate(Date supplyMedicinceDate) {
        this.supplyMedicinceDate = supplyMedicinceDate;
    }

    public Date getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(Date receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
    }

    /**
     * Lấy tên giai đoạn điều trị
     * 
     * @return 
     */
    public String getName() {
        String startTimeStage = this.getTreatmentTime() == null ? TextUtils.formatDate(this.getRegistrationTime(), FORMATDATE) : TextUtils.formatDate(this.getTreatmentTime(), FORMATDATE);
        String endTimeStage = this.getEndTime() == null ? "hiện tại" : TextUtils.formatDate(this.getEndTime(), FORMATDATE);
        return String.format("Giai đoạn: %s - %s", startTimeStage, endTimeStage);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpcStageEntity stage = (OpcStageEntity) o;
        int resultFinal = 0;
        resultFinal += receivedWardDate == null && stage.getReceivedWardDate()== null ? 0 : (receivedWardDate != null && stage.getReceivedWardDate()!= null && receivedWardDate.compareTo(stage.getReceivedWardDate()) == 0) ? 0 : 1;
        resultFinal += registrationTime == null && stage.getRegistrationTime() == null ? 0 : (registrationTime != null && stage.getRegistrationTime() != null && registrationTime.compareTo(stage.getRegistrationTime()) == 0) ? 0 : 1;
        resultFinal += treatmentTime == null && stage.getTreatmentTime() == null ? 0 : (treatmentTime != null && stage.getTreatmentTime() != null && treatmentTime.compareTo(stage.getTreatmentTime()) == 0) ? 0 : 1;
        resultFinal += endTime == null && stage.getEndTime() == null ? 0 : (endTime != null && stage.getEndTime() != null && endTime.compareTo(stage.getEndTime()) == 0) ? 0 : 1;
        resultFinal += pregnantStartDate == null && stage.getPregnantStartDate() == null ? 0 : (pregnantStartDate != null && stage.getPregnantStartDate() != null && pregnantStartDate.compareTo(stage.getPregnantStartDate()) == 0) ? 0 : 1;
        resultFinal += pregnantEndDate == null && stage.getPregnantEndDate() == null ? 0 : (pregnantEndDate != null && stage.getPregnantEndDate() != null && pregnantEndDate.compareTo(stage.getPregnantEndDate()) == 0) ? 0 : 1;
        resultFinal += tranferFromTime == null && stage.getTranferFromTime() == null ? 0 : (tranferFromTime != null && stage.getTranferFromTime() != null && tranferFromTime.compareTo(stage.getTranferFromTime()) == 0) ? 0 : 1;
        resultFinal += patientID == null && stage.getPatientID() == null ? 0 : (patientID != null && stage.getPatientID() != null && patientID.compareTo(stage.getPatientID()) == 0) ? 0 : 1;
        resultFinal += StringUtils.isEmpty(registrationType) && StringUtils.isEmpty(stage.getRegistrationType()) ? 0 : (StringUtils.isNotEmpty(registrationType) && StringUtils.isNotEmpty(stage.getRegistrationType()) && registrationType.equals(stage.getRegistrationType())) ? 0 : 1;
        resultFinal += StringUtils.isEmpty(statusOfTreatmentID) && StringUtils.isEmpty(stage.getStatusOfTreatmentID()) ? 0 : (StringUtils.isNotEmpty(statusOfTreatmentID) && StringUtils.isNotEmpty(stage.getStatusOfTreatmentID()) && statusOfTreatmentID.equals(stage.getStatusOfTreatmentID())) ? 0 : 1;
        resultFinal += StringUtils.isEmpty(treatmentStageID) && StringUtils.isEmpty(stage.getTreatmentStageID()) ? 0 : (StringUtils.isNotEmpty(treatmentStageID) && StringUtils.isNotEmpty(stage.getTreatmentStageID()) && treatmentStageID.equals(stage.getTreatmentStageID())) ? 0 : 1;
        resultFinal += StringUtils.isEmpty(treatmentRegimenStage) && StringUtils.isEmpty(stage.getTreatmentRegimenStage()) ? 0 : (StringUtils.isNotEmpty(treatmentRegimenStage) && StringUtils.isNotEmpty(stage.getTreatmentRegimenStage()) && treatmentRegimenStage.equals(stage.getTreatmentRegimenStage())) ? 0 : 1;
        resultFinal += StringUtils.isEmpty(treatmentRegimenID) && StringUtils.isEmpty(stage.getTreatmentRegimenID()) ? 0 : (StringUtils.isNotEmpty(treatmentRegimenID) && StringUtils.isNotEmpty(stage.getTreatmentRegimenID()) && treatmentRegimenID.equals(stage.getTreatmentRegimenID())) ? 0 : 1;
        resultFinal += StringUtils.isEmpty(endCase) && StringUtils.isEmpty(stage.getEndCase()) ? 0 : (StringUtils.isNotEmpty(endCase) && StringUtils.isNotEmpty(stage.getEndCase()) && endCase.equals(stage.getEndCase())) ? 0 : 1;
        resultFinal += StringUtils.isEmpty(objectGroupID) && StringUtils.isEmpty(stage.getObjectGroupID()) ? 0 : (StringUtils.isNotEmpty(objectGroupID) && StringUtils.isNotEmpty(stage.getObjectGroupID()) && objectGroupID.equals(stage.getObjectGroupID())) ? 0 : 1;
        resultFinal += supplyMedicinceDate == null && stage.getSupplyMedicinceDate()== null ? 0 : (supplyMedicinceDate != null && stage.getSupplyMedicinceDate()!= null && supplyMedicinceDate.compareTo(stage.getSupplyMedicinceDate()) == 0) ? 0 : 1;
        resultFinal += daysReceived == stage.getDaysReceived() ? 0 : 1;
        resultFinal += isRemove() == stage.isRemove() ? 0 : 1;

        return resultFinal == 0;

    }
}

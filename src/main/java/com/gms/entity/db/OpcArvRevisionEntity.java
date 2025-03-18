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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author TrangBN
 * @des Log thông tin back date
 */
@Entity
@Table(name = "OPC_ARV_REVISION",
        indexes = {
            @Index(name = "OR_SITE_ID", columnList = "SITE_ID")
            ,@Index(name = "OR_PATIENT_ID", columnList = "PATIENT_ID")
            ,@Index(name = "OR_ARV_ID", columnList = "ARV_ID")
            ,@Index(name = "OR_ARV_STATUS_OF_TREATMENT_ID", columnList = "STATUS_OF_TREATMENT_ID")
            ,@Index(name = "OR_ARV_TREATMENT_TIME", columnList = "TREATMENT_TIME")
            ,@Index(name = "OR_ARV_CREATED_AT", columnList = "CREATED_AT")
            ,@Index(name = "OR_ARV_STAGE_ID", columnList = "STAGE_ID")
            ,@Index(name = "OR_ARV_PREGNANT_START_DATE", columnList = "PREGNANT_START_DATE")
            ,@Index(name = "OR_ARV_REGISTRATION_TYPE", columnList = "REGISTRATION_TYPE")
            ,@Index(name = "OR_ARV_TRANFER_FROM_TIME", columnList = "TRANFER_FROM_TIME")
            ,@Index(name = "OR_ARV_TREATMENT_REGIMENT_ID", columnList = "TREATMENT_REGIMENT_ID")
            ,@Index(name = "OR_ARV_TREATMENT_REGIMENT_STAGE", columnList = "TREATMENT_REGIMENT_STAGE")
            ,@Index(name = "OR_ARV_REGISTRATION_TIME", columnList = "REGISTRATION_TIME")
            ,@Index(name = "OR_ARV_IS_REMOVE", columnList = "IS_REMOVE")
        })
@DynamicUpdate
@DynamicInsert
public class OpcArvRevisionEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "ARV_ID", length = 50, nullable = false)
    private Long arvID; //Mã bệnh án

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý

    @Column(name = "STAGE_ID")
    private Long stageID; //Mã giai đoạn điều trị

    @Column(name = "PATIENT_ID")
    private Long patientID; //FK thông tin cơ bản của bệnh nhân

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_TIME", nullable = false)
    private Date registrationTime; //NGày đăng ký vào OPC

    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGIMENT_DATE", nullable = true)
    private Date regimenDate; // Ngày thay đổi phác đồ

    @Column(name = "TREATMENT_STAGE_ID", length = 50, nullable = true)
    private String treatmentStageID; //Trạng thái biến động điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TREATMENT_TIME", nullable = true)
    private Date treatmentTime; //Ngày ARV tại cơ sở OPC

    @Column(name = "TREATMENT_REGIMENT_STAGE", length = 1, nullable = true)
    private String treatmentRegimenStage; //Bậc phác đồ điều trị - sử dụng enum

    @Column(name = "TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String treatmentRegimenID; //Phác đồ điều trị tại cơ sở OPC

    @Column(name = "DAYS_RECEIVED", nullable = true)
    private int daysReceived; //Số ngày nhận thuốc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", nullable = true)
    private Date endTime; // Ngày kết thúc tại OPC

    @Column(name = "END_CASE", length = 1, nullable = true)
    private String endCase; //Lý do kết thúc điều trị

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_START_DATE", nullable = true)
    private Date pregnantStartDate; // Ngày bắt đầu thai ký

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_END_DATE", nullable = true)
    private Date pregnantEndDate; // Ngày chuyển dạ đẻ

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANFER_FROM_TIME", nullable = true)
    private Date tranferFromTime; //Ngày chuyển đi

    @Column(name = "REGISTRATION_TYPE", length = 1, nullable = true)
    private String registrationType; //Loại đăng ký - Sử dụng enum

    @Column(name = "RECEIVED_WARD", length = 220, nullable = true)
    private String receivedWard; //Nhận thuốc tại tuyến xã

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIVED_WARD_DATE", length = 220, nullable = true)
    private Date receivedWardDate; //Ngày nhận thuốc tại tuyến xã
    
    @Column(name = "IS_REMOVE", nullable = false)
    private boolean remove; //Xoá logic
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUPPLY_MEDICINE_DATE", nullable = true)
    private Date supplyMedicinceDate; // Ngày đầu tiên cấp thuốc nhiều tháng

    public Date getRegimenDate() {
        return regimenDate;
    }

    public void setRegimenDate(Date regimenDate) {
        this.regimenDate = regimenDate;
    }
    
    public Date getSupplyMedicinceDate() {
        return supplyMedicinceDate;
    }

    public void setSupplyMedicinceDate(Date supplyMedicinceDate) {
        this.supplyMedicinceDate = supplyMedicinceDate;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
    
    public Date getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(Date receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
    }

    public String getReceivedWard() {
        return receivedWard;
    }

    public void setReceivedWard(String receivedWard) {
        this.receivedWard = receivedWard;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
    }

    public Date getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(Date tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getArvID() {
        return arvID;
    }

    public void setArvID(Long arvID) {
        this.arvID = arvID;
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

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
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

    public int getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(int daysReceived) {
        this.daysReceived = daysReceived;
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
}

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
 * @author vvthanh
 * @des Giai đoạn Điều trị
 */
@Entity
@Table(name = "PQM_ARV_STAGE", indexes = {
    @Index(name = "OS_SITE_ID", columnList = "SITE_ID"),
    @Index(name = "OS_ARV_ID", columnList = "ARV_ID"),
    @Index(name = "OS_END_CASE", columnList = "END_CASE"),
    @Index(name = "OS_END_TIME", columnList = "END_TIME"),
    @Index(name = "OS_REGISTRATION_TIME", columnList = "REGISTRATION_TIME"),
    @Index(name = "OS_REGISTRATION_TYPE", columnList = "REGISTRATION_TYPE"),
    @Index(name = "OS_STATUS_OF_TREATMENT_ID", columnList = "STATUS_OF_TREATMENT_ID"),
    @Index(name = "OS_TRANFER_FROM_TIME", columnList = "TRANFER_FROM_TIME"),
    @Index(name = "OS_TREATMENT_TIME", columnList = "TREATMENT_TIME")
})
@DynamicUpdate
@DynamicInsert
public class PqmArvStageEntity extends BaseEntity implements Serializable, Cloneable {

    private static final String FORMATDATE = "dd/MM/yyyy";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_TIME", nullable = false)
    private Date registrationTime; //NGày đăng ký vào OPC

    @Column(name = "REGISTRATION_TYPE", length = 1000, nullable = true)
    private String registrationType; //Loại đăng ký - Sử dụng enum

    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TREATMENT_TIME", nullable = true)
    private Date treatmentTime; //Ngày ARV tại cơ sở OPC

    @Column(name = "END_CASE", length = 1, nullable = true)
    private String endCase; //Lý do kết thúc điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", nullable = true)
    private Date endTime; // Ngày kết thúc tại OPC

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANFER_FROM_TIME", nullable = true)
    private Date tranferFromTime; //Ngày chuyển gửi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY")
    private Long updatedBY;

    @Column(name = "treatment_session_id", length = 50, nullable = true)
    private String treatment_session_id;

    public String getTreatment_session_id() {
        return treatment_session_id;
    }

    public void setTreatment_session_id(String treatment_session_id) {
        this.treatment_session_id = treatment_session_id;
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

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(Date tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

}

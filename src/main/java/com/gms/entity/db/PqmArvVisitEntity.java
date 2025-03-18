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
 * @des Lượt khám
 */
@Entity
@Table(name = "PQM_ARV_VISIT", indexes = {
    @Index(name = "OS_APPOINMENT_TIME", columnList = "APPOINMENT_TIME"),
    @Index(name = "OS_ARV_ID", columnList = "ARV_ID"),
    @Index(name = "OS_DAYS_RECEIVED", columnList = "DAYS_RECEIVED"),
    @Index(name = "OS_EXAMINATION_TIME", columnList = "EXAMINATION_TIME"),
    @Index(name = "OS_STAGE_ID", columnList = "STAGE_ID"),
    @Index(name = "OS_MUTIPLE_MONTH", columnList = "MUTIPLE_MONTH")

})
@DynamicUpdate
@DynamicInsert
public class PqmArvVisitEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Column(name = "STAGE_ID")
    private Long stageID; // Mã giai đoạn điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXAMINATION_TIME", nullable = false)
    private Date examinationTime; //Ngày khám bệnh

    @Column(name = "DAYS_RECEIVED", nullable = true)
    private int daysReceived; //Số ngày nhận thuốc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPOINMENT_TIME", nullable = true)
    private Date appointmentTime; // Ngày hẹn khám  

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

    @Column(name = "MUTIPLE_MONTH")
    private String mutipleMonth;

    @Column(name = "admission_code", length = 50, nullable = true)
    private String admission_code;

    public String getAdmission_code() {
        return admission_code;
    }

    public void setAdmission_code(String admission_code) {
        this.admission_code = admission_code;
    }

    public String getMutipleMonth() {
        return mutipleMonth;
    }

    public void setMutipleMonth(String mutipleMonth) {
        this.mutipleMonth = mutipleMonth;
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
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

    public Date getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(Date examinationTime) {
        this.examinationTime = examinationTime;
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

}

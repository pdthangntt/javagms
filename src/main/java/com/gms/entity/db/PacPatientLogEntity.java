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
 * @des Thông tin bệnh nhân
 */
@Entity
@Table(name = "PAC_PATIENT_LOG", indexes = {
    @Index(name = "PAC_PATIENT_LOG_find", columnList = "PATIENT_ID")
    ,@Index(name = "PPL_TIME", columnList = "TIME")
})
@DynamicUpdate
@DynamicInsert
public class PacPatientLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "PATIENT_ID", nullable = false)
    private Long patientID;

    @Column(name = "STAFF_ID", nullable = true)
    private Long staffID;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME", insertable = true, updatable = false, nullable = false)
    private Date time;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}

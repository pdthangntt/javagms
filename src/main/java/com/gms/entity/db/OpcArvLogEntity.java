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

/**
 *
 * @author vvthanh
 * @des Lưu thông tin log case index
 */
@Entity
@Table(name = "OPC_ARV_LOG", indexes = {
    @Index(name = "OAL_ARV_ID", columnList = "ARV_ID")
    ,@Index(name = "OAL_PATIENT_ID", columnList = "PATIENT_ID")
    ,@Index(name = "OAL_STAGE_ID", columnList = "STAGE_ID")
    ,@Index(name = "OAL_TEST_ID", columnList = "TEST_ID")
    ,@Index(name = "OAL_VIRAL_LOAD_ID", columnList = "VIRAL_LOAD_ID")
    ,@Index(name = "OAL_FIND", columnList = "ARV_ID, TIME")
    ,@Index(name = "OAL_TIME", columnList = "TIME")
})
public class OpcArvLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Column(name = "PATIENT_ID")
    private Long patientID; //FK thông tin cơ bản của bệnh nhân

    @Column(name = "STAGE_ID", nullable = true)
    private Long stageID;

    @Column(name = "TEST_ID", nullable = true)
    private Long testID;

    @Column(name = "VIRAL_LOAD_ID", nullable = true)
    private Long viralLoadID;

    @Column(name = "VISIT_ID", nullable = true)
    private Long visitID;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // Nọi dung log

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME", insertable = true, updatable = false, nullable = false)
    private Date time; // Ngày tạo bản ghi

    @Column(name = "STAFF_ID", insertable = true, updatable = false, nullable = false)
    private Long staffID;

    public Long getVisitID() {
        return visitID;
    }

    public void setVisitID(Long visitID) {
        this.visitID = visitID;
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

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
    }

    public Long getTestID() {
        return testID;
    }

    public void setTestID(Long testID) {
        this.testID = testID;
    }

    public Long getViralLoadID() {
        return viralLoadID;
    }

    public void setViralLoadID(Long viralLoadID) {
        this.viralLoadID = viralLoadID;
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

    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

}

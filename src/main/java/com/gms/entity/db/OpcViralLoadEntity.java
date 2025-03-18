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

/**
 *
 * @author vvthanh
 * @des Tải lượng virut
 */
@Entity
@Table(name = "OPC_VIRAL_LOAD", indexes = {
    @Index(name = "OVL_SITE_ID", columnList = "SITE_ID")
    ,@Index(name = "OVL_ARV_ID", columnList = "ARV_ID")
    ,@Index(name = "OVL_PATIENT_ID", columnList = "PATIENT_ID")
    ,@Index(name = "OVL_IS_REMOVE", columnList = "IS_REMOVE")
})
public class OpcViralLoadEntity extends BaseEntity implements Serializable, Cloneable {

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
    @Column(name = "SAMPLE_TIME", nullable = true)
    private Date sampleTime; // Ngày lấy mẫu

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TEST_TIME", nullable = true)
    private Date testTime; // TLVR - Ngày xét nghiệm

    @Column(name = "TEST_SITE_ID", nullable = true)
    private Long testSiteID; //Cơ sở xét nghiệm

    @Column(name = "TEST_SITE_NAME", nullable = true)
    private String testSiteName; //Hoặc Tên Cơ sở xét nghiệm

    @Column(name = "RESULT", length = 50, nullable = true)
    private String result; //Kết quả xét nghiệm TLVR

    @Column(name = "RESULT_NUMBER", length = 50, nullable = true)
    private String resultNumber; //Nồng độ - nhập số

    @Temporal(TemporalType.DATE)
    @Column(name = "RESULT_TIME", nullable = true)
    private Date resultTime; //NGày trả kết quả

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RETRY_TIME", nullable = true)
    private Date retryTime; // Ngày hẹn xét nghiệm lại

    @Column(name = "NOTE", length = 500, nullable = true)
    private String note; //Ghi chú

    @Column(name = "DATA_ID", length = 50, nullable = true)
    private String dataID; //mã bên nguồn đồng bộ

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

    @Column(name = "STAGE_ID")
    private Long stageID; // Mã giai đoạn điều trị

    @Column(name = "IS_IMPORT", nullable = false)
    private boolean importable; // Đánh dấu bản ghi import

    @Transient
    private List<String> causes; //TLVR - lý do xét nghiệm

    @Transient
    private String dataStageID; //Mã giai đoạn điều trị cũ

    @Transient
    private String dataVisitID; //Mã giai đoạn điều trị cũ

    public String getDataStageID() {
        return dataStageID;
    }

    public void setDataStageID(String dataStageID) {
        this.dataStageID = dataStageID;
    }

    public String getDataVisitID() {
        return dataVisitID;
    }

    public void setDataVisitID(String dataVisitID) {
        this.dataVisitID = dataVisitID;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public boolean isImportable() {
        return importable;
    }

    public void setImportable(boolean importable) {
        this.importable = importable;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
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

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public Long getTestSiteID() {
        return testSiteID;
    }

    public void setTestSiteID(Long testSiteID) {
        this.testSiteID = testSiteID;
    }

    public String getTestSiteName() {
        return testSiteName;
    }

    public void setTestSiteName(String testSiteName) {
        this.testSiteName = testSiteName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public Date getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(Date retryTime) {
        this.retryTime = retryTime;
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

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    @Override
    public OpcViralLoadEntity clone() throws CloneNotSupportedException {
        return (OpcViralLoadEntity) super.clone();
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
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

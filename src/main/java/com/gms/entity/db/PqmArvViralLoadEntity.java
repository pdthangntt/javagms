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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Tải lượng virut
 */
@Entity
@Table(name = "PQM_ARV_VIRAL_LOAD", indexes = {
    @Index(name = "OS_ARV_ID", columnList = "ARV_ID")
    ,@Index(name = "OS_RESULT", columnList = "RESULT")
    ,@Index(name = "OS_RESULT_NUMBER", columnList = "RESULT_NUMBER")
    ,@Index(name = "OS_RESULT_TIME", columnList = "RESULT_TIME")
    ,@Index(name = "OS_STAGE_ID", columnList = "STAGE_ID")
    ,@Index(name = "OS_TEST_TIME", columnList = "TEST_TIME")
    ,@Index(name = "OS_INSURANCE_NO", columnList = "INSURANCE_NO")
   
})
@DynamicUpdate
@DynamicInsert
public class PqmArvViralLoadEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Column(name = "STAGE_ID")
    private Long stageID; // Mã giai đoạn điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TEST_TIME", nullable = true)
    private Date testTime; // TLVR - Ngày xét nghiệm

    @Column(name = "RESULT", length = 50, nullable = true)
    private String result; //Kết quả xét nghiệm TLVR

    @Column(name = "RESULT_NUMBER", length = 50, nullable = true)
    private String resultNumber; //Kết quả xét nghiệm TLVR số

    @Temporal(TemporalType.DATE)
    @Column(name = "RESULT_TIME", nullable = true)
    private Date resultTime; //NGày trả kết quả

    @Column(name = "INSURANCE_NO", length = 100)
    private String insuranceNo; //BHYT

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false )
    private Long createdBY;

    @Column(name = "UPDATED_BY" )
    private Long updatedBY;

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public Long getID() {
        return ID;
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
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

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
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

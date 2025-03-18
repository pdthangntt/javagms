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
 * @des Thông tin CD4 && HBV && HCV
 */
@Entity
@Table(name = "PQM_ARV_TEST", indexes = {
    @Index(name = "OS_ARV_ID", columnList = "ARV_ID"),
    @Index(name = "OS_INH_END_CAUSE", columnList = "INH_END_CAUSE"),
    @Index(name = "OS_INH_FROM_TIME", columnList = "INH_FROM_TIME"),
    @Index(name = "OS_INH_TO_TIME", columnList = "INH_TO_TIME"),
    @Index(name = "OS_STAGE_ID", columnList = "STAGE_ID")

})
@DynamicUpdate
@DynamicInsert
public class PqmArvTestEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INH_FROM_TIME", nullable = true)
    private Date inhFromTime; //Lao từ ngày

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INH_TO_TIME", nullable = true)
    private Date inhToTime; //Lao đến ngày

    @Column(name = "INH_END_CAUSE", nullable = true)
    private String inhEndCause; //Lý do kết thúc lao - inh

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

    @Column(name = "STAGE_ID")
    private Long stageID; // Mã giai đoạn điều trị

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

    public String getInhEndCause() {
        return inhEndCause;
    }

    public void setInhEndCause(String inhEndCause) {
        this.inhEndCause = inhEndCause;
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

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
    }

}

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
 * @author pdThang
 * @des Giai đoạn ddieuf trị
 */
@Entity
@Table(name = "PQM_PREP_STAGE", indexes = {
    @Index(name = "OS_ID", columnList = "ID"),
    @Index(name = "OS_END_TIME", columnList = "END_TIME"),
    @Index(name = "OS_PREP_ID", columnList = "PREP_ID"),
    @Index(name = "OS_START_TIME", columnList = "START_TIME"),
    @Index(name = "OS_TYPE", columnList = "TYPE")
})
@DynamicUpdate
@DynamicInsert
public class PqmPrepStageEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "PREP_ID")
    private Long prepID; //Mã PrEP

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME", nullable = false)
    private Date startTime; //Ngày bắt đàu điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Date endTime; //Ngày kết thúc điều trị

    @Column(name = "TYPE", length = 50)
    private String type; // Kết quả XN - Lấy thời enum kết quả khẳng định

    @Column(name = "END_CAUSE")
    private String endCause; // Lý do kết thúc

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

    public String getEndCause() {
        return endCause;
    }

    public void setEndCause(String endCause) {
        this.endCause = endCause;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPrepID() {
        return prepID;
    }

    public void setPrepID(Long prepID) {
        this.prepID = prepID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

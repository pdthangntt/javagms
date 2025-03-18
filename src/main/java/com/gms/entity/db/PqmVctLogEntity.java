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
 * @author vvThành
 */
@Entity
@Table(name = "PQM_VCT_LOG", indexes = {
    @Index(name = "PQM_VCT_target_id", columnList = "TARGET_ID")
})
public class PqmVctLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "TARGET_ID", nullable = false)
    private Long targetID; // Mã thông tin htc_visit

    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = true)
    private String content; // Nọi dung log

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_AT", insertable = true, updatable = false, nullable = false)
    private Date createAt; // Ngày tạo bản ghi

    @Column(name = "STAFF_ID", insertable = true, updatable = false, nullable = false)
    private Long staffID;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getTargetID() {
        return targetID;
    }

    public void setTargetID(Long targetID) {
        this.targetID = targetID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

}

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
 * @author TrangBN
 * @des Lưu thông tin log bảng HTC_VISIT
 */
@Entity
@Table(name = "HTC_VISIT_LOG", indexes = {
    @Index(name = "htc_visit_id", columnList = "HTC_VISIT_ID")
})
public class HtcVisitLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "HTC_VISIT_ID", nullable = false)
    private Long htcVisitID; // Mã thông tin htc_visit

    @Column(name = "content", length = 500, nullable = false)
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

    public Long getHtcVisitID() {
        return htcVisitID;
    }

    public void setHtcVisitID(Long htcVisitID) {
        this.htcVisitID = htcVisitID;
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

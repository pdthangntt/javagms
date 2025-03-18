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
 * @author TrangBN
 */
@Entity
@Table(name = "HTC_LAYTEST_LOG", indexes = {
    @Index(name = "htc_laytest_id", columnList = "HTC_LAYTEST_ID")
})
@DynamicUpdate
@DynamicInsert
public class HtcLaytestLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "HTC_LAYTEST_ID", nullable = false)
    private Long htcLaytestID; // Mã thông tin htc_confirm

    @Column(name = "CONTENT", length = 500, nullable = false)
    private String content; // Nội dung log

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_AT", insertable = true, updatable = false, nullable = false)
    private Date createAt; // Ngày tạo bản ghi

    @Column(name = "STAFF_ID", insertable = true, updatable = false, nullable = false)
    private Long staffID; // Người update

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getHtcLaytestID() {
        return htcLaytestID;
    }

    public void setHtcLaytestID(Long htcLaytestID) {
        this.htcLaytestID = htcLaytestID;
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

package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Thông tin chuyển gửi của laytest
 */
@Entity
@Table(name = "HTC_VISIT_LAYTEST", indexes = {})
@DynamicUpdate
@DynamicInsert
public class HtcVisitLaytestEntity extends BaseEntity implements Serializable {

    @Id
    private Long ID; //Mã HTC Visit

    @Column(name = "SOURCE_ID")
    private Long sourceID;  //Mã bên laytest

    @Column(name = "SOURCE_SITE_ID")
    private Long sourceSiteID; //Mã nơi nguồn gửi

    @Column(name = "SOURCE_SERVICE_ID", length = 50, nullable = true)
    private String sourceServiceID; // Dịch vụ nơi gửi mẫu

    @Column(name = "SOURCE_STAFF_ID")
    private Long sourceStaffID; // Cán bộ không chuyên

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCEPT_DATE")
    private Date acceptDate; //Ngày tiếp nhận

    @Column(name = "ACCEPT_STAFF_ID")
    private Long acceptStaffID; //Cán bộ tiếp nhận

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_AT")
    private Date createdAt; // Ngày tạo bản ghi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_AT")
    private Date updatedAt; // Ngày cập nhật bản ghi

    @Column(name = "CREATE_BY")
    private Long createdBy; // Người tạo bản ghi

    @Column(name = "UPDATE_BY")
    private Long updatedBy; // Người cập nhật
    
    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAt");
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
    }

    public Long getSourceStaffID() {
        return sourceStaffID;
    }

    public void setSourceStaffID(Long sourceStaffID) {
        this.sourceStaffID = sourceStaffID;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Long getAcceptStaffID() {
        return acceptStaffID;
    }

    public void setAcceptStaffID(Long acceptStaffID) {
        this.acceptStaffID = acceptStaffID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
}

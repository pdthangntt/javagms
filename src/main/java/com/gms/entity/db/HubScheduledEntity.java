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
 * @author pdthang
 */
@Entity
@Table(name = "HUB_SCHE")
public class HubScheduledEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "IS_RUN", columnDefinition = "boolean default false")
    private boolean isRun;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_AT", insertable = true, updatable = false, nullable = false)
    private Date createAt; // Ngày tạo bản ghi

    @Column(name = "STAFF_ID", insertable = true, updatable = false, nullable = false)
    private Long staffID;

    @Column(name = "PROVINCE_ID", length = 50, nullable = true)
    private String provinceID;

    @Column(name = "CURRENT_SITE_ID")
    private Long currentSiteID;

    public Long getID() {
        return ID;
    }

    public Long getCurrentSiteID() {
        return currentSiteID;
    }

    public void setCurrentSiteID(Long currentSiteID) {
        this.currentSiteID = currentSiteID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public boolean isIsRun() {
        return isRun;
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
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

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

}

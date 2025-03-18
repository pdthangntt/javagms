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
@Table(name = "PQM_LOG", indexes = {
    @Index(name = "OS_ID", columnList = "ID")
})
@DynamicUpdate
@DynamicInsert
public class PqmLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME", nullable = false)
    private Date time;

    @Column(name = "PROVINCE_ID", length = 50)
    private String provinceID;

    @Column(name = "STAFF_ID", length = 50)
    private Long staffID;

    @Column(name = "STAFF_NAME", length = 250)
    private String staffName;

    @Column(name = "ACTION", length = 500)
    private String action;

    @Column(name = "TOTAL")
    private int total;

    @Column(name = "SUCCESS")
    private int success;

    @Column(name = "ERROR")
    private int error;

    @Column(name = "OBJECT", length = 250)
    private String object;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "SERVICE", length = 250)
    private String service;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}

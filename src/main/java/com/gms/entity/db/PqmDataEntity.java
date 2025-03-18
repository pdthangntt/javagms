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
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvThanh Data chung của PQM tool
 */
@Entity
@Table(name = "PQM_DATA",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {
        "MONTH", "YEAR", "INDICATOR_CODE",
        "SITE_ID", "SEX_ID", "AGE_GROUP", "OBJECT_GROUP_ID"
    })},
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
            ,@Index(name = "OS_SITE_ID", columnList = "SITE_ID")
            ,@Index(name = "OS_PROVINCE_ID", columnList = "PROVINCE_ID")
            ,@Index(name = "OS_DISTRICT_ID", columnList = "DISTRICT_ID")
            ,@Index(name = "OS_MONTH", columnList = "MONTH")
            ,@Index(name = "OS_YEAR", columnList = "YEAR")
            ,@Index(name = "OS_INDICATOR_CODE", columnList = "INDICATOR_CODE")
            ,@Index(name = "OS_MONTH", columnList = "MONTH")
            ,@Index(name = "OS_QUANTITY", columnList = "QUANTITY")
            ,@Index(name = "OS_SEX_ID", columnList = "SEX_ID")
            ,@Index(name = "OS_AGE_GROUP", columnList = "AGE_GROUP")
            ,@Index(name = "OS_OBJECT_GROUP_ID", columnList = "OBJECT_GROUP_ID")
            ,@Index(name = "OS_STATUS", columnList = "STATUS")
            ,@Index(name = "OS_SEND_DATE", columnList = "SEND_DATE")
            ,@Index(name = "OS_MULTI_MONTH", columnList = "MULTI_MONTH")
        })
@DynamicUpdate
@DynamicInsert
public class PqmDataEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "PROVINCE_ID", length = 9)
    private String provinceID;

    @Column(name = "DISTRICT_ID", length = 9)
    private String districtID;

    @Column(name = "MONTH")
    private int month;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "INDICATOR_CODE", length = 50, nullable = false)
    private String indicatorCode;

    @Column(name = "QUANTITY")
    private Long quantity;

    @Column(name = "SEX_ID", length = 50, nullable = false)
    private String sexID;

    @Column(name = "AGE_GROUP", length = 20, nullable = false)
    private String ageGroup;

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "KEY_EARLY", length = 10)
    private String keyEarly; 

    @Column(name = "MULTI_MONTH")
    private int multiMonth;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SEND_DATE")
    private Date sendDate;

    @Column(name = "STATUS")
    private int status;

    public String getKeyEarly() {
        return keyEarly;
    }

    public void setKeyEarly(String keyEarly) {
        this.keyEarly = keyEarly;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSexID() {
        return sexID;
    }

    public void setSexID(String sexID) {
        this.sexID = sexID;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public int getMultiMonth() {
        return multiMonth;
    }

    public void setMultiMonth(int multiMonth) {
        this.multiMonth = multiMonth;
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

}

package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvThanh Data chung của dulieuhiv.vn
 */
@Entity
@Table(name = "PQM_REPORT",
        uniqueConstraints = {
            //            @UniqueConstraint(columnNames = {
            //        "MONTH", "YEAR", "INDICATOR_CODE",
            //        "SITE_CODE", "SEX", "AGE_GROUP", "KEY_POPULATION"
            //    }) ,
            @UniqueConstraint(columnNames = {
        "MONTH", "YEAR", "INDICATOR_CODE",
        "SITE_ID", "SEX_ID", "AGE_GROUP", "KEY_POPULATION_ID"
    })
        })
@DynamicUpdate
@DynamicInsert
public class PqmReportEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "MONTH")
    private int month;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "QUANTITY")
    private Long quantity;

    @Column(name = "INDICATOR_CODE", length = 50, nullable = false)
    private String indicatorCode;

    @Column(name = "PROVINCE_ID", length = 5, nullable = false)
    private String provinceID;

    @Column(name = "PROVINCE_CODE", length = 50, nullable = false)
    private String provinceCode;

    @Column(name = "PROVINCE_NAME", length = 220, nullable = false)
    private String provinceName;

    @Column(name = "DISTRICT_ID", length = 5, nullable = false)
    private String districtID;

    @Column(name = "DISTRICT_CODE", length = 50, nullable = false)
    private String districtCode;

    @Column(name = "DISTRICT_NAME", length = 1000, nullable = false)
    private String districtName;

    @Column(name = "site_id")
    private Long siteID;

    @Column(name = "SITE_CODE", length = 50, nullable = false)
    private String siteCode;

    @Column(name = "SITE_NAME", length = 220, nullable = false)
    private String siteName;

    @Column(name = "SEX_ID", length = 50, nullable = false)
    private String sexID;

    @Column(name = "SEX", length = 20, nullable = false)
    private String sex;

    @Column(name = "AGE_GROUP", length = 20, nullable = false)
    private String ageGroup;

    @Column(name = "KEY_POPULATION_ID", length = 50, nullable = false)
    private String keyPopulationID;

    @Column(name = "KEY_POPULATION", length = 50, nullable = false)
    private String keyPopulation;

    @Column(name = "MULTI_MONTH")
    private int multiMonth;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

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

    public String getSexID() {
        return sexID;
    }

    public void setSexID(String sexID) {
        this.sexID = sexID;
    }

    public String getKeyPopulationID() {
        return keyPopulationID;
    }

    public void setKeyPopulationID(String keyPopulationID) {
        this.keyPopulationID = keyPopulationID;
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

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getKeyPopulation() {
        return keyPopulation;
    }

    public void setKeyPopulation(String keyPopulation) {
        this.keyPopulation = keyPopulation;
    }

    public int getMultiMonth() {
        return multiMonth;
    }

    public void setMultiMonth(int multiMonth) {
        this.multiMonth = multiMonth;
    }

}

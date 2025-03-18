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
 * @author vvThanh Data chung của PQM chỉ số shi tool
 */
@Entity
@Table(name = "PQM_SHI_DATA",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"PROVINCE_ID", "SITE_ID", "MONTH", "YEAR"}, name = "pqm_shi_art_unique_site_code")
        },
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
            ,@Index(name = "OS_SITE_ID", columnList = "SITE_ID")
            ,@Index(name = "OS_PROVINCE_ID", columnList = "PROVINCE_ID")
            ,@Index(name = "OS_MONTH", columnList = "MONTH")
            ,@Index(name = "OS_YEAR", columnList = "YEAR")
            ,@Index(name = "OS_SHI_ART", columnList = "SHI_ART")
            ,@Index(name = "OS_SHI_MMD", columnList = "SHI_MMD") 
        })
@DynamicUpdate
@DynamicInsert
public class PqmShiDataEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "PROVINCE_ID")
    private String provinceID;

    @Column(name = "PROVINCE_CODE")
    private String province_code;

    @Column(name = "DATA_CODE")
    private String data_code;

    @Column(name = "VERSION")
    private String version;

    @Column(name = "MONTH")
    private int month;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "AGE_GROUP")
    private String age_group;

    @Column(name = "SHI_ART")
    private int shi_art;

    @Column(name = "SHI_MMD")
    private int shi_mmd;

    @Column(name = "FACILITY_NAME")
    private String facility_name;

    @Column(name = "FACILITY_CODE")
    private String facility_code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

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

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getData_code() {
        return data_code;
    }

    public void setData_code(String data_code) {
        this.data_code = data_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge_group() {
        return age_group;
    }

    public void setAge_group(String age_group) {
        this.age_group = age_group;
    }

    public int getShi_art() {
        return shi_art;
    }

    public void setShi_art(int shi_art) {
        this.shi_art = shi_art;
    }

    public int getShi_mmd() {
        return shi_mmd;
    }

    public void setShi_mmd(int shi_mmd) {
        this.shi_mmd = shi_mmd;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public String getFacility_code() {
        return facility_code;
    }

    public void setFacility_code(String facility_code) {
        this.facility_code = facility_code;
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

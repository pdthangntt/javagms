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
 * @author pdThang
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "PQM_DRUG_ELMISE",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"YEAR", "QUARTER", "PROVINCE_ID", "SITE_CODE", "DRUG_NAME", "DRUG_CODE", "TT_THAU"}, name = "pqm_unique_drug_elmi")
        })
public class PqmDrugeLMISEEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "QUARTER")
    private int quarter;

    @Column(name = "PROVINCE_ID", length = 9, nullable = false)
    private String provinceID;

    @Column(name = "SITE_CODE", nullable = false)
    private String siteCode;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "DRUG_CODE", nullable = false)
    private String drugCode;

    @Column(name = "DRUG_NAME", nullable = false)
    private String drugName;

    @Column(name = "SO_DANG_KY")
    private String soDangKy;

    @Column(name = "TT_THAU")
    private String ttThau;

    @Column(name = "DU_TRU")
    private Long duTru;

    @Column(name = "TONG_SU_DUNG")
    private Long tongSuDung;

    @Column(name = "TON_KHO")
    private Long tonKho;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getSoDangKy() {
        return soDangKy;
    }

    public void setSoDangKy(String soDangKy) {
        this.soDangKy = soDangKy;
    }

    public String getTtThau() {
        return ttThau;
    }

    public void setTtThau(String ttThau) {
        this.ttThau = ttThau;
    }

    public Long getDuTru() {
        return duTru;
    }

    public void setDuTru(Long duTru) {
        this.duTru = duTru;
    }

    public Long getTongSuDung() {
        return tongSuDung;
    }

    public void setTongSuDung(Long tongSuDung) {
        this.tongSuDung = tongSuDung;
    }

    public Long getTonKho() {
        return tonKho;
    }

    public void setTonKho(Long tonKho) {
        this.tonKho = tonKho;
    }

}

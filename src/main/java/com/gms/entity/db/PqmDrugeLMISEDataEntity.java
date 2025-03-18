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
@DynamicUpdate
@DynamicInsert
@Table(name = "PQM_DRUG_ELMISE_DATA",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
            ,
            @Index(name = "OS_SITE_ID", columnList = "SITE_ID")
            ,
            @Index(name = "OS_QUARTER", columnList = "QUARTER")
            ,
            @Index(name = "OS_YEAR", columnList = "YEAR")
            ,
            @Index(name = "OS_PROVINCE_ID", columnList = "PROVINCE_ID")
            ,
            @Index(name = "OS_DRUG_NAME", columnList = "DRUG_NAME")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"SITE_ID", "QUARTER", "YEAR", "PROVINCE_ID", "DRUG_NAME"}, name = "pqm_drug_elmise_data_unique")
        })
public class PqmDrugeLMISEDataEntity extends BaseEntity implements Serializable {

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

    @Column(name = "DRUG_NAME", nullable = false)
    private String drugName;

    @Column(name = "TON")
    private Long ton;

    @Column(name = "THANG")
    private Double thang;

    @Column(name = "SU_DUNG")
    private Long suDung;

    @Column(name = "TRONG_KY")
    private Long trongKy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

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

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Long getTon() {
        return ton;
    }

    public void setTon(Long ton) {
        this.ton = ton;
    }

    public Double getThang() {
        return thang;
    }

    public void setThang(Double thang) {
        this.thang = thang;
    }

    public Long getSuDung() {
        return suDung;
    }

    public void setSuDung(Long suDung) {
        this.suDung = suDung;
    }

    public Long getTrongKy() {
        return trongKy;
    }

    public void setTrongKy(Long trongKy) {
        this.trongKy = trongKy;
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

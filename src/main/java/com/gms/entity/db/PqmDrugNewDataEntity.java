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

@Entity
@Table(name = "PQM_DRUG_NEW_DATA", indexes = {
    @Index(name = "OS_ID", columnList = "ID"),
    @Index(name = "OS_PROVINCE_ID", columnList = "PROVINCE_ID"),
    @Index(name = "OS_SITE_ID", columnList = "SITE_ID"),
    @Index(name = "OS_DRUG_NAME", columnList = "DRUG_NAME"),
    @Index(name = "OS_SOURCE", columnList = "SOURCE")
})
@DynamicUpdate
@DynamicInsert
public class PqmDrugNewDataEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "PROVINCE_ID", length = 5)
    private String provinceID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "QUARTER")
    private int quarter;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "DRUG_NAME", length = 220)
    private String drugName;

    @Column(name = "SOURCE", length = 20)
    private String source;

    @Column(name = "TLCB", length = 120)
    private double tlcb;

    @Column(name = "TLDG", length = 120)
    private double tldg;

    @Column(name = "TDK")
    private Long tdk;

    @Column(name = "NDK")
    private Long ndk;

    @Column(name = "NK")
    private Long nk;

    @Column(name = "XCBNTK")
    private Long xcbntk;

    @Column(name = "XDCTK")
    private Long xdctk;

    @Column(name = "HH")
    private Long hh;

    @Column(name = "TCK")
    private Long tck;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getTlcb() {
        return tlcb;
    }

    public void setTlcb(double tlcb) {
        this.tlcb = tlcb;
    }

    public double getTldg() {
        return tldg;
    }

    public void setTldg(double tldg) {
        this.tldg = tldg;
    }

    public Long getTdk() {
        return tdk;
    }

    public void setTdk(Long tdk) {
        this.tdk = tdk;
    }

    public Long getNdk() {
        return ndk;
    }

    public void setNdk(Long ndk) {
        this.ndk = ndk;
    }

    public Long getNk() {
        return nk;
    }

    public void setNk(Long nk) {
        this.nk = nk;
    }

    public Long getXcbntk() {
        return xcbntk;
    }

    public void setXcbntk(Long xcbntk) {
        this.xcbntk = xcbntk;
    }

    public Long getXdctk() {
        return xdctk;
    }

    public void setXdctk(Long xdctk) {
        this.xdctk = xdctk;
    }

    public Long getHh() {
        return hh;
    }

    public void setHh(Long hh) {
        this.hh = hh;
    }

    public Long getTck() {
        return tck;
    }

    public void setTck(Long tck) {
        this.tck = tck;
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

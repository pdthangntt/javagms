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
 * @author vvthanh
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "PQM_SHI_ART",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
            ,@Index(name = "OS_SITE_ID", columnList = "SITE_ID")
            ,@Index(name = "OS_PROVINCE_ID", columnList = "PROVINCE_ID")
            ,@Index(name = "OS_MONTH", columnList = "MONTH")
            ,@Index(name = "OS_YEAR", columnList = "YEAR")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"PROVINCE_ID", "SITE_ID", "MONTH", "YEAR"}, name = "pqm_shi_art_unique_site_code")
        })
public class PqmShiArtEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "SITE_CODE")
    private String siteCode;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "PROVINCE_ID", length = 9)
    private String provinceID;

    @Column(name = "MONTH")
    private int month;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "BNNT")
    private int bnnt;//- Số BN hiện nhận thuốc

    @Column(name = "BNM")
    private int bnm;//- Số BN mới

    @Column(name = "BNQLDT")
    private int bnqldt;//- Số BN quay lại điều trị

    @Column(name = "BNDT12T")
    private int bndt12t;//- Số BN điều trị trên 12 tháng

    @Column(name = "BNNHTK")
    private int bnnhtk;//- Số BN nhỡ hẹn tái khám

    @Column(name = "TLBNNHTK")
    private double tlbnnhtk;//- Tỷ lệ BN nhỡ hẹn tái khám

    @Column(name = "BNBTTK")
    private int bnbttk;//- Số BN bỏ trị trong kỳ

    @Column(name = "BNBTLK")
    private int bnbtlk;//- Số BN bỏ trị lũy kế

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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
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

    public int getBnnt() {
        return bnnt;
    }

    public void setBnnt(int bnnt) {
        this.bnnt = bnnt;
    }

    public int getBnm() {
        return bnm;
    }

    public void setBnm(int bnm) {
        this.bnm = bnm;
    }

    public int getBnqldt() {
        return bnqldt;
    }

    public void setBnqldt(int bnqldt) {
        this.bnqldt = bnqldt;
    }

    public int getBndt12t() {
        return bndt12t;
    }

    public void setBndt12t(int bndt12t) {
        this.bndt12t = bndt12t;
    }

    public int getBnnhtk() {
        return bnnhtk;
    }

    public void setBnnhtk(int bnnhtk) {
        this.bnnhtk = bnnhtk;
    }

    public double getTlbnnhtk() {
        return tlbnnhtk;
    }

    public void setTlbnnhtk(double tlbnnhtk) {
        this.tlbnnhtk = tlbnnhtk;
    }

    public int getBnbttk() {
        return bnbttk;
    }

    public void setBnbttk(int bnbttk) {
        this.bnbttk = bnbttk;
    }

    public int getBnbtlk() {
        return bnbtlk;
    }

    public void setBnbtlk(int bnbtlk) {
        this.bnbtlk = bnbtlk;
    }
}

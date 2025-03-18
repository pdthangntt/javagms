package com.gms.entity.form.pqm_shi_art_api;

import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class Art extends BaseEntity {

    private String siteCode;

    private String siteName;

    private String zipCode;

    private String provinceID;

    private String month;

    private String year;

    private String bnnt;//- Số BN hiện nhận thuốc

    private String bnm;//- Số BN mới

    private String bnqldt;//- Số BN quay lại điều trị

    private String bndt12t;//- Số BN điều trị trên 12 tháng

    private String bnnhtk;//- Số BN nhỡ hẹn tái khám

    private String tlbnnhtk;//- Tỷ lệ BN nhỡ hẹn tái khám

    private String bnbttk;//- Số BN bỏ trị trong kỳ

    private String bnbtlk;//- Số BN bỏ trị lũy kế

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBnnt() {
        return bnnt;
    }

    public void setBnnt(String bnnt) {
        this.bnnt = bnnt;
    }

    public String getBnm() {
        return bnm;
    }

    public void setBnm(String bnm) {
        this.bnm = bnm;
    }

    public String getBnqldt() {
        return bnqldt;
    }

    public void setBnqldt(String bnqldt) {
        this.bnqldt = bnqldt;
    }

    public String getBndt12t() {
        return bndt12t;
    }

    public void setBndt12t(String bndt12t) {
        this.bndt12t = bndt12t;
    }

    public String getBnnhtk() {
        return bnnhtk;
    }

    public void setBnnhtk(String bnnhtk) {
        this.bnnhtk = bnnhtk;
    }

    public String getTlbnnhtk() {
        return tlbnnhtk;
    }

    public void setTlbnnhtk(String tlbnnhtk) {
        this.tlbnnhtk = tlbnnhtk;
    }

    public String getBnbttk() {
        return bnbttk;
    }

    public void setBnbttk(String bnbttk) {
        this.bnbttk = bnbttk;
    }

    public String getBnbtlk() {
        return bnbtlk;
    }

    public void setBnbtlk(String bnbtlk) {
        this.bnbtlk = bnbtlk;
    }

}

package com.gms.entity.form.pqm_elmis_api;

import com.gms.entity.form.pqm_arv_api.*;
import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Elmis extends BaseEntity {

    private String quarter;
    private String year;

    private String siteCode;

    private String siteName;

    private String drugCode;

    private String drugName;

    private String soDangKy;

    private String ttThau;

    private String duTru;

    private String tongSuDung;

    private String tonKho;

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getDuTru() {
        return duTru;
    }

    public void setDuTru(String duTru) {
        this.duTru = duTru;
    }

    public String getTongSuDung() {
        return tongSuDung;
    }

    public void setTongSuDung(String tongSuDung) {
        this.tongSuDung = tongSuDung;
    }

    public String getTonKho() {
        return tonKho;
    }

    public void setTonKho(String tonKho) {
        this.tonKho = tonKho;
    }
    
    

}

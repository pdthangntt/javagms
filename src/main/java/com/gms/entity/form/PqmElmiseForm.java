package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author pdthang
 */
public class PqmElmiseForm extends BaseEntity implements Serializable {

    private int row;
    private int q;
    private int y;

    private String siteCode;

    private String siteName;

    private String drugCode;

    private String drugName;

    private String soDangKy;

    private String ttThau;

    private String duTru;

    private String tongSuDung;

    private String tonKho;

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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

package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * Form báo cáo hiện trạng cư trú
 *
 *
 * @author pdthang
 */
public class PacDetectHivTreatmentTableForm implements Serializable, Cloneable {

    private String stt;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String displayName;
    private int dangdieutri;
    private int trongtinh;
    private int ngoaitinh;
    private int chuadieutri;
    private int chodieutri;
    private int bodieutri;
    private int matdau;
    private int tuvong;
    private int khongthongtin;

    private List<PacDetectHivTreatmentTableForm> childs;

    public int getDangdieutri() {
        return dangdieutri;
    }

    public void setDangdieutri(int dangdieutri) {
        this.dangdieutri = dangdieutri;
    }

    public String getTrongtinhPercent() {
        return TextUtils.toPercent(this.trongtinh, this.getTong());
    }
    
    public String getNgoaitinhPercent() {
        return TextUtils.toPercent(this.ngoaitinh, this.getTong());
    }
    
    public String getChuadieutriPercent() {
        return TextUtils.toPercent(this.chuadieutri, this.getTong());
    }
    
    public String getChodieutriPercent() {
        return TextUtils.toPercent(this.chodieutri, this.getTong());
    }
    
    public String getBodieutriPercent() {
        return TextUtils.toPercent(this.bodieutri, this.getTong());
    }
    
    public String getMatdauPercent() {
        return TextUtils.toPercent(this.matdau, this.getTong());
    }
    
    public String getTuvongPercent() {
        return TextUtils.toPercent(this.tuvong, this.getTong());
    }
    
    public String getKhongthongtinPercent() {
        return TextUtils.toPercent(this.khongthongtin, this.getTong());
    }
    
    public String getTongPercent(int sum) {
        return TextUtils.toPercent(this.getTong(), sum);
    }
    
    public List<PacDetectHivTreatmentTableForm> getChilds() {
        return childs;
    }

    public void setChilds(List<PacDetectHivTreatmentTableForm> childs) {
        this.childs = childs;
    }
    
    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getTrongtinh() {
        return trongtinh;
    }

    public void setTrongtinh(int trongtinh) {
        this.trongtinh = trongtinh;
    }

    public int getNgoaitinh() {
        return ngoaitinh;
    }

    public void setNgoaitinh(int ngoaitinh) {
        this.ngoaitinh = ngoaitinh;
    }

    public int getChuadieutri() {
        return chuadieutri;
    }

    public void setChuadieutri(int chuadieutri) {
        this.chuadieutri = chuadieutri;
    }

    public int getChodieutri() {
        return chodieutri;
    }

    public void setChodieutri(int chodieutri) {
        this.chodieutri = chodieutri;
    }

    public int getBodieutri() {
        return bodieutri;
    }

    public void setBodieutri(int bodieutri) {
        this.bodieutri = bodieutri;
    }

    public int getMatdau() {
        return matdau;
    }

    public void setMatdau(int matdau) {
        this.matdau = matdau;
    }

    public int getTuvong() {
        return tuvong;
    }

    public void setTuvong(int tuvong) {
        this.tuvong = tuvong;
    }

    public int getKhongthongtin() {
        return khongthongtin;
    }

    public void setKhongthongtin(int khongthongtin) {
        this.khongthongtin = khongthongtin;
    }

    
    public int getTong() {
        return  this.getTrongtinh() + 
                this.getNgoaitinh() + 
                this.getChuadieutri() + 
                this.getBodieutri() + 
                this.getTuvong() + 
                this.getChodieutri() + 
                this.getMatdau() + 
                this.getKhongthongtin();
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

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    @Override
    public PacDetectHivTreatmentTableForm clone() throws CloneNotSupportedException {
        return (PacDetectHivTreatmentTableForm) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}

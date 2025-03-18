/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Admin
 */
public class TableDetectHivTransmissionForm implements Serializable, Cloneable {
    
    private String stt;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String displayName;
    private int blood;
    private int sexuality;
    private int momtochild;
    private int unclear;
    private int noinformation;
    
    private List<TableDetectHivTransmissionForm> childs;

    public List<TableDetectHivTransmissionForm> getChilds() {
        return childs;
    }

    public void setChilds(List<TableDetectHivTransmissionForm> childs) {
        this.childs = childs;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getSexuality() {
        return sexuality;
    }

    public void setSexuality(int sexuality) {
        this.sexuality = sexuality;
    }

    public int getMomtochild() {
        return momtochild;
    }

    public void setMomtochild(int momtochild) {
        this.momtochild = momtochild;
    }

    public int getUnclear() {
        return unclear;
    }

    public void setUnclear(int unclear) {
        this.unclear = unclear;
    }

    public int getNoinformation() {
        return noinformation;
    }

    public void setNoinformation(int noinformation) {
        this.noinformation = noinformation;
    }

    public String getBloodPercent() {
         return TextUtils.toPercent(this.blood, getTong());
    }
    
    public String getSexualityPercent() {
         return TextUtils.toPercent(this.sexuality, getTong());
    }
    
    public String getMomtochildPercent() {
         return TextUtils.toPercent(this.momtochild, getTong());
    }
    
    public String getNoinformationPercent() {
         return TextUtils.toPercent(this.noinformation, getTong());
    }
    
    public String getUnclearPercent() {
        return TextUtils.toPercent(this.unclear, getTong());
    }
    
    public String getTongPercent(int sum) {
        return TextUtils.toPercent(this.getTong(), sum);
    }

    
    public int getTong() {
        return this != null ? 
                this.blood 
                + this.momtochild 
                + this.noinformation
                + this.sexuality
                + this.unclear : 0;
    }
    
    @Override
    public TableDetectHivTransmissionForm clone() throws CloneNotSupportedException {
        return (TableDetectHivTransmissionForm) super.clone();
    }
    
}

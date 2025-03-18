package com.gms.entity.form.pac;

import java.io.Serializable;

/**
 *
 * @author TrangBN
 */
public class TablePacEarlyHivForm implements Serializable, Cloneable {

    private int stt;
    private String wardName;
    private String wardID;
    private String districtName;
    private String districtID;
    private String provinceName;
    private String provinceID;
    private int male;
    private int female;
    private int other;
    
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }
    
    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
    }

    public int getFemale() {
        return female;
    }

    public void setFemale(int female) {
        this.female = female;
    }

    
    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public int getTotal() {
        return male + female + other;
    }

    @Override
    public TablePacEarlyHivForm clone() throws CloneNotSupportedException {
        return (TablePacEarlyHivForm) super.clone();
    }

}

package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * TableDetectHivGenderForm
 *
 * @author TrangBN
 */
public class TableDetectHivGenderForm implements Serializable, Cloneable {

    private int stt;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String displayName;
    private int male;
    private int female;
    private int other;
    private List<TableDetectHivGenderForm> childs;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
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

    public List<TableDetectHivGenderForm> getChilds() {
        return childs;
    }

    public void setChilds(List<TableDetectHivGenderForm> childs) {
        this.childs = childs;
    }

    public String getOtherPercent() {
        return TextUtils.toPercent(this.other, this.getSum());
    }

    public String getMalePercent() {
        return TextUtils.toPercent(this.male, this.getSum());
    }

    public String getFemalePercent() {
        return TextUtils.toPercent(this.female, this.getSum());
    }

    public String getSumPercent(int total) {
        return TextUtils.toPercent(this.getSum(), total);
    }

    public int getSum() {
        return male + female + other;
    }

    @Override
    public TableDetectHivGenderForm clone() throws CloneNotSupportedException {
        return (TableDetectHivGenderForm) super.clone();
    }

}

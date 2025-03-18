package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * TableDetectHivAgeForm
 * 
 * @author TrangBN
 */
public class TableDetectHivAgeForm implements Serializable, Cloneable {

    private int stt;
    private String provinceID;
    private String provinceName;
    private String otherProvinceID;
    private String districtID;
    private String searchDistrictID;
    private String districtName;
    private String wardID; 
    private String searchWardID; 
    private String wardName; 
    private String displayName;
    
    private int age1;
    private int age2;
    private int age3;
    private int age4;
    private int age5;
    
    private List<TableDetectHivAgeForm> childs;

    public String getAge1Percent() {
        return TextUtils.toPercent(this.age1, this.getSum());
    }

    public String getAge2Percent() {
        return TextUtils.toPercent(this.age2, this.getSum());
    }

    public String getAge3Percent() {
        return TextUtils.toPercent(this.age3, this.getSum());
    }

    public String getAge4Percent() {
        return TextUtils.toPercent(this.age4, this.getSum());
    }

    public String getAge5Percent() {
        return TextUtils.toPercent(this.age5, this.getSum());
    }
    
    public String getSumPercent(int total) {
        return TextUtils.toPercent(this.getSum(), total);
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public List<TableDetectHivAgeForm> getChilds() {
        return childs;
    }

    public void setChilds(List<TableDetectHivAgeForm> childs) {
        this.childs = childs;
    }
    
    public int getStt() {
        return stt;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public String getOtherProvinceID() {
        return otherProvinceID;
    }

    public void setOtherProvinceID(String otherProvinceID) {
        this.otherProvinceID = otherProvinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getSearchDistrictID() {
        return searchDistrictID;
    }

    public void setSearchDistrictID(String searchDistrictID) {
        this.searchDistrictID = searchDistrictID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getSearchWardID() {
        return searchWardID;
    }

    public void setSearchWardID(String searchWardID) {
        this.searchWardID = searchWardID;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public int getAge1() {
        return age1;
    }

    public void setAge1(int age1) {
        this.age1 = age1;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public int getAge3() {
        return age3;
    }

    public void setAge3(int age3) {
        this.age3 = age3;
    }

    public int getAge4() {
        return age4;
    }

    public void setAge4(int age4) {
        this.age4 = age4;
    }

    public int getAge5() {
        return age5;
    }

    public void setAge5(int age5) {
        this.age5 = age5;
    }
    
    public int getSum() {
        return age1 + age2 + age3 + age4 + age5;
    }
    
    @Override
    public TableDetectHivAgeForm clone() throws CloneNotSupportedException {
        return (TableDetectHivAgeForm) super.clone();
    }
}

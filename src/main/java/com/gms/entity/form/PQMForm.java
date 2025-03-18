package com.gms.entity.form;

import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PQMForm implements Serializable {

    private String ID;
    private String month;
    private String year;
    private String quantity;
    private String indicatorCode;
    private String indicatorName;
    private String provinceID;
    private String districtID;
    private String siteID;
    private String sexID;
    private String ageGroup;
    private String objectGroupID; //Nhóm đối tượng
    private String objectGroupName; //Nhóm đối tượng
    private String multiMonth;

    public String getObjectGroupName() {
        return objectGroupName;
    }

    public void setObjectGroupName(String objectGroupName) {
        this.objectGroupName = objectGroupName;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
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

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getSexID() {
        return sexID;
    }

    public void setSexID(String sexID) {
        this.sexID = sexID;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getMultiMonth() {
        return multiMonth;
    }

    public void setMultiMonth(String multiMonth) {
        this.multiMonth = multiMonth;
    }

}

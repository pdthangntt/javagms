package com.gms.entity.form.ql;

/**
 *
 * @author vanthanh
 */
public class Local {

    private String permanentProvinceID;
    private String permanentDistrictID;
    private String permanentWardID;
    private String currentProvinceID;
    private String currentDistrictID;
    private String currentWardID;
    private int month;
    private int year;

    public String getPermanentWardID() {
        return permanentWardID;
    }

    public void setPermanentWardID(String permanentWardID) {
        this.permanentWardID = permanentWardID;
    }

    public String getCurrentWardID() {
        return currentWardID;
    }

    public void setCurrentWardID(String currentWardID) {
        this.currentWardID = currentWardID;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getPermanentDistrictID() {
        return permanentDistrictID;
    }

    public void setPermanentDistrictID(String permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getCurrentDistrictID() {
        return currentDistrictID;
    }

    public void setCurrentDistrictID(String currentDistrictID) {
        this.currentDistrictID = currentDistrictID;
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

    public Local(String permanentProvinceID, String permanentDistrictID, String permanentWardID, String currentProvinceID, String currentDistrictID, String currentWardID, String month, String year) {
        this.permanentProvinceID = permanentProvinceID;
        this.permanentDistrictID = permanentDistrictID;
        this.permanentWardID = permanentWardID;
        this.currentProvinceID = currentProvinceID;
        this.currentDistrictID = currentDistrictID;
        this.currentWardID = currentWardID;
        this.month = Integer.valueOf(month);
        this.year = Integer.valueOf(year);
    }

    public Local(String permanentProvinceID, String permanentDistrictID, String month, String year) {
        this.permanentProvinceID = permanentProvinceID;
        this.permanentDistrictID = permanentDistrictID;
        this.month = Integer.valueOf(month);
        this.year = Integer.valueOf(year);
    }

    public Local() {
    }

}

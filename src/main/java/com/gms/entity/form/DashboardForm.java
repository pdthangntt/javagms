package com.gms.entity.form;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class DashboardForm implements Serializable {

    private String indicatorName;
    private String provinceID;
    private String provinceName;
    private List<String> district;
    private List<String> ward;
    private List<Long> site;
    private List<String> serviceTest;
    private String year;
    private String month;
    private String day;
    private List<String> quarter;
    private List<String> gender;
    private List<String> modesOfTransmision;
    private List<String> testObjectGroup;
    private String quantityPatient;
    private List<String> ages;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<String> getAges() {
        return ages;
    }

    public void setAges(List<String> ages) {
        this.ages = ages;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getQuantityPatient() {
        return quantityPatient;
    }

    public void setQuantityPatient(String quantityPatient) {
        this.quantityPatient = quantityPatient;
    }

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public List<String> getModesOfTransmision() {
        return modesOfTransmision;
    }

    public void setModesOfTransmision(List<String> modesOfTransmision) {
        this.modesOfTransmision = modesOfTransmision;
    }

    public List<String> getTestObjectGroup() {
        return testObjectGroup;
    }

    public void setTestObjectGroup(List<String> testObjectGroup) {
        this.testObjectGroup = testObjectGroup;
    }

    public List<String> getWard() {
        return ward;
    }

    public void setWard(List<String> ward) {
        this.ward = ward;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public List<String> getDistrict() {
        return district;
    }

    public void setDistrict(List<String> district) {
        this.district = district;
    }

    public List<Long> getSite() {
        return site;
    }

    public void setSite(List<Long> site) {
        this.site = site;
    }

    public List<String> getServiceTest() {
        return serviceTest;
    }

    public void setServiceTest(List<String> serviceTest) {
        this.serviceTest = serviceTest;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getQuarter() {
        return quarter;
    }

    public void setQuarter(List<String> quarter) {
        this.quarter = quarter;
    }

}

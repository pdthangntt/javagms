package com.gms.entity.input;

import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public class PacHivDetectSearch {

    private String addressType;
    private String statusAlive;
    private String searchTime;
    private String fromTime;
    private String toTime;
    private String provinceID;
    private String otherProvinceID;
    private String districtID;
    private String wardID;
    private String job;
    private String testObject;
    private String statusTreatment;
    private String statusResident;
    private String gender;
    private Set<String> testObjects; 
    private String flag;
    private String age;
    
    private String modeOfTransmissionID;

    public String getModeOfTransmissionID() {
        return modeOfTransmissionID;
    }

    public void setModeOfTransmissionID(String modeOfTransmissionID) {
        this.modeOfTransmissionID = modeOfTransmissionID;
    }


    public String getOtherProvinceID() {
        return "null".equals(otherProvinceID) ? StringUtils.EMPTY : otherProvinceID;
    }

    public void setOtherProvinceID(String otherProvinceID) {
        this.otherProvinceID = otherProvinceID;
    }
    
    public Set<String> getTestObjects() {
        return testObjects;
    }

    public void setTestObjects(Set<String> testObjects) {
        this.testObjects = testObjects;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAddressType() {
        return "null".equals(addressType) ? StringUtils.EMPTY : addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getStatusAlive() {
        return "null".equals(statusAlive) ? StringUtils.EMPTY : statusAlive;
    }

    public void setStatusAlive(String statusAlive) {
        this.statusAlive = statusAlive;
    }

    public String getSearchTime() {
        return "null".equals(searchTime) ? StringUtils.EMPTY : searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public String getFromTime() {
        return "null".equals(fromTime) ? StringUtils.EMPTY : fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return "null".equals(toTime) ? StringUtils.EMPTY : toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDistrictID() {
        return "null".equals(districtID) ? StringUtils.EMPTY : districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return "null".equals(wardID) ? StringUtils.EMPTY : wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getJob() {
        return "null".equals(job) ? StringUtils.EMPTY : job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTestObject() {
        return "null".equals(testObject) ? StringUtils.EMPTY : testObject;
    }

    public void setTestObject(String testObject) {
        this.testObject = testObject;
    }

    public String getStatusTreatment() {
        return "null".equals(statusTreatment) ? StringUtils.EMPTY : statusTreatment;
    }

    public void setStatusTreatment(String statusTreatment) {
        this.statusTreatment = statusTreatment;
    }

    public String getStatusResident() {
        return "null".equals(statusResident) ? StringUtils.EMPTY : statusResident;
    }

    public void setStatusResident(String statusResident) {
        this.statusResident = statusResident;
    }

    public String getGender() {
        return "null".equals(gender) ? StringUtils.EMPTY : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvinceID() {
        return "null".equals(provinceID) ? StringUtils.EMPTY : provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

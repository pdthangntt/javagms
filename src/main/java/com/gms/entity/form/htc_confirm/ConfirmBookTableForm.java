/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.htc_confirm;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class ConfirmBookTableForm {
    
    private Long ID;
    private String confirmTime; 
    private String acceptDate;
    private String serviceID;
    private Integer yearOfBirth;
    private String patientID;
    private String fixedServiceID;
    private String code;
    private String patientName;
    private String gender;
    private String objectGroupID; //Nhóm đối tượng
    private String addressFull; //Địa chỉ thường trú
    private String fullname;
    private String sampleSaveCode;
    private String sourceID;
    private String sourceSiteID;
    private String resultID;
    private String bioName1; // Tên sinh phẩm 1
    private String bioNameResult1; // Kết quả sinh phẩm 1
    private String firstBioDate;
    private String bioName2; // Tên sinh phẩm 2
    private String bioNameResult2; // Kết quả sinh phẩm 2
    private String secondBioDate;
    private String bioName3; // Tên sinh phẩm 3
    private String bioNameResult3; // Kết quả sinh phẩm 3
    private String thirdBioDate;
    private String note;
    private String virusLoad;
    private String virusLoadDate;
    private String earlyHiv;
    private String earlyHivDate;
    private Long siteID;

    public String getFirstBioDate() {
        return firstBioDate;
    }

    public void setFirstBioDate(String firstBioDate) {
        this.firstBioDate = firstBioDate;
    }

    public String getSecondBioDate() {
        return secondBioDate;
    }

    public void setSecondBioDate(String secondBioDate) {
        this.secondBioDate = secondBioDate;
    }

    public String getThirdBioDate() {
        return thirdBioDate;
    }

    public void setThirdBioDate(String thirdBioDate) {
        this.thirdBioDate = thirdBioDate;
    }

    public String getVirusLoadDate() {
        return virusLoadDate;
    }

    public void setVirusLoadDate(String virusLoadDate) {
        this.virusLoadDate = virusLoadDate;
    }

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }
    
    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBioName1() {
        return bioName1;
    }

    public void setBioName1(String bioName1) {
        this.bioName1 = bioName1;
    }

    public String getBioNameResult1() {
        return bioNameResult1;
    }

    public void setBioNameResult1(String bioNameResult1) {
        this.bioNameResult1 = bioNameResult1;
    }

    public String getBioName2() {
        return bioName2;
    }

    public void setBioName2(String bioName2) {
        this.bioName2 = bioName2;
    }

    public String getBioNameResult2() {
        return bioNameResult2;
    }

    public void setBioNameResult2(String bioNameResult2) {
        this.bioNameResult2 = bioNameResult2;
    }

    public String getBioName3() {
        return bioName3;
    }

    public void setBioName3(String bioName3) {
        this.bioName3 = bioName3;
    }

    public String getBioNameResult3() {
        return bioNameResult3;
    }

    public void setBioNameResult3(String bioNameResult3) {
        this.bioNameResult3 = bioNameResult3;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSampleSaveCode() {
        return sampleSaveCode;
    }

    public void setSampleSaveCode(String sampleSaveCode) {
        this.sampleSaveCode = sampleSaveCode;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getFixedServiceID() {
        return fixedServiceID;
    }

    public void setFixedServiceID(String fixedServiceID) {
        this.fixedServiceID = fixedServiceID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
    }
    public Long getSiteID() {
        return siteID;
    }
    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }
    public Integer getYearOfBirth() {
        return yearOfBirth;
    }
    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.htc;

/**
 *
 * @author DSNAnh
 */
public class TestBookTableForm {

    private Long ID;
    private Long siteID;
    private String patientName;
    private String code;
    private String gender;
    private String yearOfBirth;
    private String permanentAddress;
    private String objectGroupID; //Nhóm đối tượng
    private String preTestTime;
    private String bioName;
    private String bioNameResult;
    private String testResultsID;
    private String confirmResultID;
    private String note;
    private String staffBeforeTestID;

    public String getStaffBeforeTestID() {
        return staffBeforeTestID;
    }

    public void setStaffBeforeTestID(String staffBeforeTestID) {
        this.staffBeforeTestID = staffBeforeTestID;
    }

    public String getBioNameResult() {
        return bioNameResult;
    }

    public void setBioNameResult(String bioNameResult) {
        this.bioNameResult = bioNameResult;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getPreTestTime() {
        return preTestTime;
    }

    public void setPreTestTime(String preTestTime) {
        this.preTestTime = preTestTime;
    }

    public String getBioName() {
        return bioName;
    }

    public void setBioName(String bioName) {
        this.bioName = bioName;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getConfirmResultID() {
        return confirmResultID;
    }

    public void setConfirmResultID(String confirmResultID) {
        this.confirmResultID = confirmResultID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}

package com.gms.entity.form.htc;

import java.util.HashMap;

/**
 * Form hiển thị phiếu trả kết quả sàng lọc âm tính
 *
 * @author TrangBN
 */
public class VisitAnswerResultForm {

    private Long siteID;
    private String siteAddress;
    private String sitePhone;
    private String code;

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;

    private String patientName;
    private String genderID;
    private String yearOfBirth;
    private String fullAddress;
    private String identityNo;
    private String preTestTime;//Ngày XN sàng lọc(Ngày đăng kí xét nghiệm)
    private String bioName;
    private String testResult;
    private String patientIDAuthen;
    private String objectGroupID; //Nhóm đối tượng

    private HashMap<String, String> config;

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public HashMap<String, String> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, String> config) {
        this.config = config;
    }

    public String getPatientIDAuthen() {
        return patientIDAuthen;
    }

    public void setPatientIDAuthen(String patientIDAuthen) {
        this.patientIDAuthen = patientIDAuthen;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSitePhone() {
        return sitePhone;
    }

    public void setSitePhone(String sitePhone) {
        this.sitePhone = sitePhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
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

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}

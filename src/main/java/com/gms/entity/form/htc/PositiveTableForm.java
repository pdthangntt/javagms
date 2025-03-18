package com.gms.entity.form.htc;

/**
 * Form Danh sách ca nhiễm dương tính
 * @author TrangBN
 */
public class PositiveTableForm {
    
    // Họ tên khách hàng
    private String patientName;
    // Dân tộc
    private String race;
    // Giới tính
    private String gender;
    // Năm sinh
    private String yearOfBirth;
    // CMND
    private String patientID;
    // Địa chỉ tạm trú hiện tại
    private String currentAddress;
    // Địa chỉ thường trú
    private String permanentAddress;
    // Công việc
    private String job;
    // Nhóm đối tượng
    private String objectGroupID;
    // Hành vi nguy cơ lây nhiễm
    private String riskBehaviorID;
    // Đường lây truyền
    private String modeOfTransmission;
    //Ngày xét nghiệm khẳng định
    private String confirmTime;
    // Cơ sở xét nghiệm khẳng định
    private String siteConfirmTest;
    // Tên cơ sở xét nghiệm sàng lọc
    private String siteName;
    // Kết quả phản ứng
    private String testResultsID;
    // Nơi lấy máu
    private String bloodPlace;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
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

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getModeOfTransmission() {
        return modeOfTransmission;
    }

    public void setModeOfTransmission(String modeOfTransmission) {
        this.modeOfTransmission = modeOfTransmission;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getSiteConfirmTest() {
        return siteConfirmTest;
    }

    public void setSiteConfirmTest(String siteConfirmTest) {
        this.siteConfirmTest = siteConfirmTest;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getRiskBehaviorID() {
        return riskBehaviorID;
    }

    public void setRiskBehaviorID(String riskBehaviorID) {
        this.riskBehaviorID = riskBehaviorID;
    }

    public String getBloodPlace() {
        return bloodPlace;
    }

    public void setBloodPlace(String bloodPlace) {
        this.bloodPlace = bloodPlace;
    }

}

package com.gms.entity.json.hivinfo.patient;

/**
 *
 * @author vvthanh
 */
public class Gsph {

    private String code;
    private String fullname;
    private Integer raceID;
    private String patientID; // Số CMND
    private Integer genderID; // Mã giới tính
    private Integer yearOfBirth; // Năm sinh
    private Integer permanentWardID;  // ID Xã hộ khẩu
    private Integer permanentDistrictID;  // ID huyện hộ khẩu
    private Integer permanentProvinceID;   // ID Tỉnh/thành hộ khẩu
    private String permanentStreet;  // Đường phố hộ khẩu
    private String permanentGroup;  // Tổ hộ khẩu
    private String permanentHomeNo;  // Số nhà hộ khẩu
    private Integer currentWardID;  // ID xã tạm trú
    private Integer currentDistrictID;  // ID huyện tạm trú
    private Integer currentProviceID;  // ID tỉnh tạm trú
    private String currentStreet;  // Đường phố tạm trú
    private String currentGroup;  // Tổ tạm trú
    private String currentHomeNo;  // Số nhà tạm trú
    private Integer jobID;  // Mã nghề nghiệp`
    private Integer objectID;  // Mã đối tượng
    private Integer modeOfTransmission;  // Mã đường lây
    private Integer riskBehaviorID;  // Mã nguy cơ
    private Integer statusID;  // Mã Tình trạng
    private String note;  // ghi chú
    private Integer registerID;  // Mã nơi đăng ký
    private Integer sent;
    private String eclinicaID;  // Mã MAECLINICA
    private Integer sttHCM;  // Số thứ tự HCM
    private Integer maTheHCM;  // Mã thẻ HCM
    private Integer remove;  // Đề nghị xóa
    private Integer syncIms;  // Đồng bộ vs ims
    private String syncTime;  // Ngày trigger run

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getRaceID() {
        return raceID;
    }

    public void setRaceID(Integer raceID) {
        this.raceID = raceID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public Integer getGenderID() {
        return genderID;
    }

    public void setGenderID(Integer genderID) {
        this.genderID = genderID;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Integer getPermanentWardID() {
        return permanentWardID;
    }

    public void setPermanentWardID(Integer permanentWardID) {
        this.permanentWardID = permanentWardID;
    }

    public Integer getPermanentDistrictID() {
        return permanentDistrictID;
    }

    public void setPermanentDistrictID(Integer permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public Integer getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(Integer permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getPermanentStreet() {
        return permanentStreet;
    }

    public void setPermanentStreet(String permanentStreet) {
        this.permanentStreet = permanentStreet;
    }

    public String getPermanentGroup() {
        return permanentGroup;
    }

    public void setPermanentGroup(String permanentGroup) {
        this.permanentGroup = permanentGroup;
    }

    public String getPermanentHomeNo() {
        return permanentHomeNo;
    }

    public void setPermanentHomeNo(String permanentHomeNo) {
        this.permanentHomeNo = permanentHomeNo;
    }

    public Integer getCurrentWardID() {
        return currentWardID;
    }

    public void setCurrentWardID(Integer currentWardID) {
        this.currentWardID = currentWardID;
    }

    public Integer getCurrentDistrictID() {
        return currentDistrictID;
    }

    public void setCurrentDistrictID(Integer currentDistrictID) {
        this.currentDistrictID = currentDistrictID;
    }

    public Integer getCurrentProviceID() {
        return currentProviceID;
    }

    public void setCurrentProviceID(Integer currentProviceID) {
        this.currentProviceID = currentProviceID;
    }

    public String getCurrentStreet() {
        return currentStreet;
    }

    public void setCurrentStreet(String currentStreet) {
        this.currentStreet = currentStreet;
    }

    public String getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }

    public String getCurrentHomeNo() {
        return currentHomeNo;
    }

    public void setCurrentHomeNo(String currentHomeNo) {
        this.currentHomeNo = currentHomeNo;
    }

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getModeOfTransmission() {
        return modeOfTransmission;
    }

    public void setModeOfTransmission(Integer modeOfTransmission) {
        this.modeOfTransmission = modeOfTransmission;
    }

    public Integer getRiskBehaviorID() {
        return riskBehaviorID;
    }

    public void setRiskBehaviorID(Integer riskBehaviorID) {
        this.riskBehaviorID = riskBehaviorID;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getRegisterID() {
        return registerID;
    }

    public void setRegisterID(Integer registerID) {
        this.registerID = registerID;
    }

    public Integer getSent() {
        return sent;
    }

    public void setSent(Integer sent) {
        this.sent = sent;
    }

    public String getEclinicaID() {
        return eclinicaID;
    }

    public void setEclinicaID(String eclinicaID) {
        this.eclinicaID = eclinicaID;
    }

    public Integer getSttHCM() {
        return sttHCM;
    }

    public void setSttHCM(Integer sttHCM) {
        this.sttHCM = sttHCM;
    }

    public Integer getMaTheHCM() {
        return maTheHCM;
    }

    public void setMaTheHCM(Integer maTheHCM) {
        this.maTheHCM = maTheHCM;
    }

    public Integer getRemove() {
        return remove;
    }

    public void setRemove(Integer remove) {
        this.remove = remove;
    }

    public Integer getSyncIms() {
        return syncIms;
    }

    public void setSyncIms(Integer syncIms) {
        this.syncIms = syncIms;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

}

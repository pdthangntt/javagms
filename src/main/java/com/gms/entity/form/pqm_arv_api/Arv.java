package com.gms.entity.form.pqm_arv_api;

import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Arv extends BaseEntity {

    private String provinceID;

    private String code; //Mã

    private String fullName; //Họ và tên

    private String dob; //Ngày/tháng/năm sinh

    private String genderID; //Giới tính

    private String identityCard; //Chứng minh thư

    private String patientPhone; //Số điện thoại bệnh nhân

    private String insuranceNo; //BHYT

    private String objectGroupID; //Nhóm đối tượng

    private String permanentAddress; // Địa chỉ 

    private String currentAddress; // Địa chỉ 

    private String statusOfTreatmentID; //Trạng thái điều trị

    private String treatmentTime; //Ngày ARV

    private String firstTreatmentTime; //Ngày bắt đầu điều trị ARV

    private String year; // Năm sinh

    private String registrationTime; //NGày đăng ký vào OPC

    private String endTime; // Ngày kết thúc tại OPC
    //visit
    private String examinationTime; //Ngày khám bệnh
    //visral
    private String testTime; // TLVR - Ngày xét nghiệm
    //test
    private String inhFromTime; //Lao từ ngày

    private String permanentProvinceID;

    private String permanentDistrictID;

    private String permanentWardID;

    private String currentProvinceID;

    private String currentDistrictID;

    private String currentWardID;

    private String siteConfirm; //Cơ sở XN khẳng định

    private String confirmTime;
    
    private String patient_id;
    private String customer_system_id;

    private List<Stage> stages;
    private List<Test> tests;
    private List<Viral> virals;
    private List<Visit> visits;

    private String siteCode;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getCustomer_system_id() {
        return customer_system_id;
    }

    public void setCustomer_system_id(String customer_system_id) {
        this.customer_system_id = customer_system_id;
    }
    
    

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

    public String getPermanentWardID() {
        return permanentWardID;
    }

    public void setPermanentWardID(String permanentWardID) {
        this.permanentWardID = permanentWardID;
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

    public String getCurrentWardID() {
        return currentWardID;
    }

    public void setCurrentWardID(String currentWardID) {
        this.currentWardID = currentWardID;
    }

    public String getSiteConfirm() {
        return siteConfirm;
    }

    public void setSiteConfirm(String siteConfirm) {
        this.siteConfirm = siteConfirm;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public List<Viral> getVirals() {
        return virals;
    }

    public void setVirals(List<Viral> virals) {
        this.virals = virals;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getFirstTreatmentTime() {
        return firstTreatmentTime;
    }

    public void setFirstTreatmentTime(String firstTreatmentTime) {
        this.firstTreatmentTime = firstTreatmentTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(String examinationTime) {
        this.examinationTime = examinationTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(String inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

}

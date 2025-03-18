package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PqmArvOneSheetImportForm extends BaseEntity implements Serializable {

    private String code; //Mã
    private Long siteID; //Mã
    private String fullName; //Họ và tên
    private String dob; //Ngày/tháng/năm sinh
    private String genderID; 
    private String identityCard; 
    private String insuranceNo;
    private String patientPhone; 
    private String address;

    private String startTime;
    private String registrationTime; //NGày đăng ký vào OPC
    private String registrationType; //Loại đăng ký - Sử dụng enum

    private String endTime;
    private String endCase;

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
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

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

}

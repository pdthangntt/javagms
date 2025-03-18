package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.db.OpcTestEntity;
import java.util.List;

/**
 *
 * @author Admin
 */
public class OpcPreArvTableForm implements Comparable<OpcPreArvTableForm> {
    String arvID;
    String registrationTime;
    String fullName;
    String code;
    String genderID;
    String dob;
    String permanentAddress;
    String permanentAddressGroup;
    String permanentAddressStreet;
    String permanentWardID;
    String permanentDistrictID;
    String permanentProvinceID;
    String permanentAddressFull;
    String registrationStatus;
    String clinicalStage;
    String cd4;
    float patientWeight;
    float patientHeight;
    String qualifiedTreatmentTime;
    String treatmentTime;
    String cd4Result;
    String cd4TestTime;
    String cd4ResultTime;
    boolean inh;
    String inhFromTime;
    String inhToTime;
    String inhEndCauses;
    String cotrimoxazoleFromTime;
    String cotrimoxazoleToTime;
    String cotrimoxazoleEndCauses;
    String type;
    String statusOfTreatmentID;
    String endCase;
    String endTime;
    List<OpcPreArvChildForm> childs;
    String testTime;
    String testResult;
    String year;
    String quarter;
    String month;
    private String tranferFromTime;

    public String getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(String tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
    public String getArvID() {
        return arvID;
    }

    public void setArvID(String arvID) {
        this.arvID = arvID;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
   
    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentAddressGroup() {
        return permanentAddressGroup;
    }

    public void setPermanentAddressGroup(String permanentAddressGroup) {
        this.permanentAddressGroup = permanentAddressGroup;
    }

    public String getPermanentAddressStreet() {
        return permanentAddressStreet;
    }

    public void setPermanentAddressStreet(String permanentAddressStreet) {
        this.permanentAddressStreet = permanentAddressStreet;
    }

    public String getPermanentWardID() {
        return permanentWardID;
    }

    public void setPermanentWardID(String permanentWardID) {
        this.permanentWardID = permanentWardID;
    }

    public String getPermanentDistrictID() {
        return permanentDistrictID;
    }

    public void setPermanentDistrictID(String permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OpcPreArvChildForm> getChilds() {
        return childs;
    }

    public void setChilds(List<OpcPreArvChildForm> childs) {
        this.childs = childs;
    }
    
    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPermanentAddressFull() {
        return permanentAddressFull;
    }

    public void setPermanentAddressFull(String permanentAddressFull) {
        this.permanentAddressFull = permanentAddressFull;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getClinicalStage() {
        return clinicalStage;
    }

    public void setClinicalStage(String clinicalStage) {
        this.clinicalStage = clinicalStage;
    }

    public String getCd4() {
        return cd4;
    }

    public void setCd4(String cd4) {
        this.cd4 = cd4;
    }

    public float getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(float patientWeight) {
        this.patientWeight = patientWeight;
    }

    public float getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(float patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getQualifiedTreatmentTime() {
        return qualifiedTreatmentTime;
    }

    public void setQualifiedTreatmentTime(String qualifiedTreatmentTime) {
        this.qualifiedTreatmentTime = qualifiedTreatmentTime;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getCd4Result() {
        return cd4Result;
    }

    public void setCd4Result(String cd4Result) {
        this.cd4Result = cd4Result;
    }

    public String getCd4TestTime() {
        return cd4TestTime;
    }

    public void setCd4TestTime(String cd4TestTime) {
        this.cd4TestTime = cd4TestTime;
    }

    public String getCd4ResultTime() {
        return cd4ResultTime;
    }

    public void setCd4ResultTime(String cd4ResultTime) {
        this.cd4ResultTime = cd4ResultTime;
    }

    public boolean isInh() {
        return inh;
    }

    public void setInh(boolean inh) {
        this.inh = inh;
    }

    public String getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(String inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

    public String getInhToTime() {
        return inhToTime;
    }

    public void setInhToTime(String inhToTime) {
        this.inhToTime = inhToTime;
    }

    public String getInhEndCauses() {
        return inhEndCauses;
    }

    public void setInhEndCauses(String inhEndCauses) {
        this.inhEndCauses = inhEndCauses;
    }

    public String getCotrimoxazoleFromTime() {
        return cotrimoxazoleFromTime;
    }

    public void setCotrimoxazoleFromTime(String cotrimoxazoleFromTime) {
        this.cotrimoxazoleFromTime = cotrimoxazoleFromTime;
    }

    public String getCotrimoxazoleToTime() {
        return cotrimoxazoleToTime;
    }

    public void setCotrimoxazoleToTime(String cotrimoxazoleToTime) {
        this.cotrimoxazoleToTime = cotrimoxazoleToTime;
    }

    public String getCotrimoxazoleEndCauses() {
        return cotrimoxazoleEndCauses;
    }

    public void setCotrimoxazoleEndCauses(String cotrimoxazoleEndCauses) {
        this.cotrimoxazoleEndCauses = cotrimoxazoleEndCauses;
    }
    
    @Override
    public int compareTo(OpcPreArvTableForm item) {
        return TextUtils.convertDate(this.getRegistrationTime(), "dd/MM/yyyy").compareTo(TextUtils.convertDate(item.getRegistrationTime(), "dd/MM/yyyy"));
    }
}

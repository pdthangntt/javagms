/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ArvBookTableForm implements Comparable<ArvBookTableForm>{
    private String arvID;
    private String stageID;
    private String registrationTime;
    private String code;
    private String fullName;
    private String genderID;
    private String dob;
    private String treatmentTime;
    private String firstTreatmentRegimentID;
    private String treatmentRegimentID;
    private String treatmentRegimentIDChange;
    private String clinicalStage;
    private String cd4;
    private float patientWeight;
    private float patientHeight;
    private String regimentDate;
    private String endTime;
    private String endCase;
    private String causesChange;
    private List<ArvBookChildForm> childs;
    private String testTime;
    private String testResult;
    private String year;
    private String quarter;
    private int month;
    private String type;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTreatmentRegimentIDChange() {
        return treatmentRegimentIDChange;
    }

    public void setTreatmentRegimentIDChange(String treatmentRegimentIDChange) {
        this.treatmentRegimentIDChange = treatmentRegimentIDChange;
    }

    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    public String getFirstTreatmentRegimentID() {
        return firstTreatmentRegimentID;
    }

    public void setFirstTreatmentRegimentID(String firstTreatmentRegimentID) {
        this.firstTreatmentRegimentID = firstTreatmentRegimentID;
    }

    public String getArvID() {
        return arvID;
    }

    public void setArvID(String arvID) {
        this.arvID = arvID;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
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

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getTreatmentRegimentID() {
        return treatmentRegimentID;
    }

    public void setTreatmentRegimentID(String treatmentRegimentID) {
        this.treatmentRegimentID = treatmentRegimentID;
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

    public String getRegimentDate() {
        return regimentDate;
    }

    public void setRegimentDate(String regimentDate) {
        this.regimentDate = regimentDate;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public String getCausesChange() {
        return causesChange;
    }

    public void setCausesChange(String causesChange) {
        this.causesChange = causesChange;
    }

    public List<ArvBookChildForm> getChilds() {
        return childs;
    }

    public void setChilds(List<ArvBookChildForm> childs) {
        this.childs = childs;
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(ArvBookTableForm item) {
        return TextUtils.convertDate(this.getTreatmentTime(), "dd/MM/yyyy").compareTo(TextUtils.convertDate(item.getTreatmentTime(), "dd/MM/yyyy"));
    }
}

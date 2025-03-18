package com.gms.entity.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author NamAnh_HaUI
 */
public class PacReviewForm implements Serializable {

    protected static final String FORMATDATE = "dd/MM/yyyy";

    private String fullname;
    private String yearOfBirth;
    private String genderID;
    private String raceID;
    private String identityCard;
    private String healthInsuranceNo;

    private String permanentAddressNo;
    private String permanentAddressGroup;
    private String permanentAddressStreet;
    private String permanentProvinceID;
    private String permanentDistrictID;
    private String permanentWardID;
    private String currentAddressNo;
    private String currentAddressGroup;
    private String currentAddressStreet;
    private String currentProvinceID;
    private String currentDistrictID;
    private String currentWardID;

    private String permanentAddressFull;
    private String currentAddressFull;
    private String jobID;
    private String objectGroupID;
    private String modeOfTransmissionID;
    private String confirmTime;
    private String siteConfirmID;
    private String bloodBaseID;
    private String statusOfTreatmentID;
    private String startTreatmentTime;
    private String siteTreatmentFacilityID;
    private String treatmentRegimenID;
    private String symptomID;
    private String deathTime;
    private String reviewWardTime;
    private String statusOfResidentID;
    private String requestDeathTime;
    private String note;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> riskBehaviorID;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> causeOfDeath;

    public String getRequestDeathTime() {
        return requestDeathTime;
    }

    public void setRequestDeathTime(String requestDeathTime) {
        this.requestDeathTime = requestDeathTime;
    }

    public String getPermanentAddressNo() {
        return permanentAddressNo;
    }

    public void setPermanentAddressNo(String permanentAddressNo) {
        this.permanentAddressNo = permanentAddressNo;
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

    public String getCurrentAddressNo() {
        return currentAddressNo;
    }

    public void setCurrentAddressNo(String currentAddressNo) {
        this.currentAddressNo = currentAddressNo;
    }

    public String getCurrentAddressGroup() {
        return currentAddressGroup;
    }

    public void setCurrentAddressGroup(String currentAddressGroup) {
        this.currentAddressGroup = currentAddressGroup;
    }

    public String getCurrentAddressStreet() {
        return currentAddressStreet;
    }

    public void setCurrentAddressStreet(String currentAddressStreet) {
        this.currentAddressStreet = currentAddressStreet;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getHealthInsuranceNo() {
        return healthInsuranceNo;
    }

    public void setHealthInsuranceNo(String healthInsuranceNo) {
        this.healthInsuranceNo = healthInsuranceNo;
    }

    public String getPermanentAddressFull() {
        return permanentAddressFull;
    }

    public void setPermanentAddressFull(String permanentAddressFull) {
        this.permanentAddressFull = permanentAddressFull;
    }

    public String getCurrentAddressFull() {
        return currentAddressFull;
    }

    public void setCurrentAddressFull(String currentAddressFull) {
        this.currentAddressFull = currentAddressFull;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getModeOfTransmissionID() {
        return modeOfTransmissionID;
    }

    public void setModeOfTransmissionID(String modeOfTransmissionID) {
        this.modeOfTransmissionID = modeOfTransmissionID;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getSiteConfirmID() {
        return siteConfirmID;
    }

    public void setSiteConfirmID(String siteConfirmID) {
        this.siteConfirmID = siteConfirmID;
    }

    public String getBloodBaseID() {
        return bloodBaseID;
    }

    public void setBloodBaseID(String bloodBaseID) {
        this.bloodBaseID = bloodBaseID;
    }

    public List<String> getRiskBehaviorID() {
        return riskBehaviorID;
    }

    public void setRiskBehaviorID(List<String> riskBehaviorID) {
        this.riskBehaviorID = riskBehaviorID;
    }

    public List<String> getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(List<String> causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getStartTreatmentTime() {
        return startTreatmentTime;
    }

    public void setStartTreatmentTime(String startTreatmentTime) {
        this.startTreatmentTime = startTreatmentTime;
    }

    public String getSiteTreatmentFacilityID() {
        return siteTreatmentFacilityID;
    }

    public void setSiteTreatmentFacilityID(String siteTreatmentFacilityID) {
        this.siteTreatmentFacilityID = siteTreatmentFacilityID;
    }

    public String getTreatmentRegimenID() {
        return treatmentRegimenID;
    }

    public void setTreatmentRegimenID(String treatmentRegimenID) {
        this.treatmentRegimenID = treatmentRegimenID;
    }

    public String getSymptomID() {
        return symptomID;
    }

    public void setSymptomID(String symptomID) {
        this.symptomID = symptomID;
    }

    public String getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(String deathTime) {
        this.deathTime = deathTime;
    }

    public String getReviewWardTime() {
        return reviewWardTime;
    }

    public void setReviewWardTime(String reviewWardTime) {
        this.reviewWardTime = reviewWardTime;
    }

    public String getStatusOfResidentID() {
        return statusOfResidentID;
    }

    public void setStatusOfResidentID(String statusOfResidentID) {
        this.statusOfResidentID = statusOfResidentID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setModel(PacPatientInfoEntity model) {
        model.setFullname(getFullname());
        if (StringUtils.isNotEmpty(getYearOfBirth())) {
            model.setYearOfBirth(Integer.parseInt(getYearOfBirth()));
        }
        model.setGenderID(getGenderID());
        model.setRaceID(getRaceID());
        model.setIdentityCard(getIdentityCard());
        model.setHealthInsuranceNo(getHealthInsuranceNo());
        model.setPermanentAddressFull(getPermanentAddressFull());
        model.setCurrentAddressFull(getCurrentAddressFull());
        model.setJobID(getJobID());
        model.setObjectGroupID(getObjectGroupID());
        model.setModeOfTransmissionID(getModeOfTransmissionID());
        model.setRiskBehaviorID(getRiskBehaviorID());
        model.setCauseOfDeath(getCauseOfDeath());
        model.setConfirmTime(TextUtils.convertDate(getConfirmTime(), FORMATDATE));
        model.setDeathTime(TextUtils.convertDate(getDeathTime(), FORMATDATE));
        model.setRequestDeathTime(TextUtils.convertDate(getRequestDeathTime(), FORMATDATE));
        model.setSiteConfirmID(getSiteConfirmID());
        model.setBloodBaseID(getBloodBaseID());
        model.setStartTreatmentTime(TextUtils.convertDate(getStartTreatmentTime(), FORMATDATE));
        model.setSiteTreatmentFacilityID(getSiteTreatmentFacilityID());
        model.setTreatmentRegimenID(getTreatmentRegimenID());
        model.setStatusOfResidentID(getStatusOfResidentID());
        model.setNote(getNote());
        model.setReviewWardTime(new Date());
        model.setPermanentAddressNo(getPermanentAddressNo());
        model.setPermanentAddressGroup(getPermanentAddressGroup());
        model.setPermanentAddressStreet(getPermanentAddressStreet());
        model.setPermanentProvinceID(getPermanentProvinceID());
        model.setPermanentDistrictID(getPermanentDistrictID());
        model.setPermanentWardID(getPermanentWardID());
        model.setCurrentAddressNo(getCurrentAddressNo());
        model.setCurrentAddressGroup(getCurrentAddressGroup());
        model.setCurrentAddressStreet(getCurrentAddressStreet());
        model.setCurrentProvinceID(getCurrentProvinceID());
        model.setCurrentDistrictID(getCurrentDistrictID());
        model.setCurrentWardID(getCurrentWardID());
    }

}

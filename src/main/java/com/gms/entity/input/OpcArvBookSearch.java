package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author pdThang
 */
public class OpcArvBookSearch {

    private Set<Long> siteID;
    private String code;
    private String name;
    private String identityCard;
    private String insuranceNo;
    private String permanentProvinceID;
    private String permanentDistrictID;
    private String permanentWardID;
    private String registrationTimeFrom;
    private String registrationTimeTo;
    private String treatmentTimeFrom;
    private String treatmentTimeTo;
    private String viralTimeForm;
    private String viralTimeTo;
    private String viralRetryForm;
    private String viralRetryTo;
    private String resultID;
    private String statusOfTreatmentID;
    private String therapySiteID;

    private String insurance;
    private String insuranceType;
    private String treatmentStageTimeFrom;
    private String treatmentStageTimeTo;
    private String treatmentStageID;
    private String tab;
    private boolean remove;

    private String dateOfArrivalFrom;
    private String dateOfArrivalTo;
    private String tranferToTimeFrom;
    private String tranferToTimeTo;
    private String tranferFromTimeFrom;
    private String tranferFromTimeTo;
    private String keywords;
    private String registrationType;

    //Phân trang
    private int pageIndex;
    private int pageSize;

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }
    
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public String getViralRetryForm() {
        return viralRetryForm;
    }

    public void setViralRetryForm(String viralRetryForm) {
        this.viralRetryForm = viralRetryForm;
    }

    public String getViralRetryTo() {
        return viralRetryTo;
    }

    public void setViralRetryTo(String viralRetryTo) {
        this.viralRetryTo = viralRetryTo;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getDateOfArrivalFrom() {
        return dateOfArrivalFrom;
    }

    public void setDateOfArrivalFrom(String dateOfArrivalFrom) {
        this.dateOfArrivalFrom = dateOfArrivalFrom;
    }

    public String getDateOfArrivalTo() {
        return dateOfArrivalTo;
    }

    public void setDateOfArrivalTo(String dateOfArrivalTo) {
        this.dateOfArrivalTo = dateOfArrivalTo;
    }

    public String getTranferToTimeFrom() {
        return tranferToTimeFrom;
    }

    public void setTranferToTimeFrom(String tranferToTimeFrom) {
        this.tranferToTimeFrom = tranferToTimeFrom;
    }

    public String getTranferToTimeTo() {
        return tranferToTimeTo;
    }

    public void setTranferToTimeTo(String tranferToTimeTo) {
        this.tranferToTimeTo = tranferToTimeTo;
    }

    public String getTranferFromTimeFrom() {
        return tranferFromTimeFrom;
    }

    public void setTranferFromTimeFrom(String tranferFromTimeFrom) {
        this.tranferFromTimeFrom = tranferFromTimeFrom;
    }

    public String getTranferFromTimeTo() {
        return tranferFromTimeTo;
    }

    public void setTranferFromTimeTo(String tranferFromTimeTo) {
        this.tranferFromTimeTo = tranferFromTimeTo;
    }

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }

    public String getViralTimeForm() {
        return viralTimeForm;
    }

    public void setViralTimeForm(String viralTimeForm) {
        this.viralTimeForm = viralTimeForm;
    }

    public String getViralTimeTo() {
        return viralTimeTo;
    }

    public void setViralTimeTo(String viralTimeTo) {
        this.viralTimeTo = viralTimeTo;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getTreatmentStageTimeFrom() {
        return treatmentStageTimeFrom;
    }

    public void setTreatmentStageTimeFrom(String treatmentStageTimeFrom) {
        this.treatmentStageTimeFrom = treatmentStageTimeFrom;
    }

    public String getTreatmentStageTimeTo() {
        return treatmentStageTimeTo;
    }

    public void setTreatmentStageTimeTo(String treatmentStageTimeTo) {
        this.treatmentStageTimeTo = treatmentStageTimeTo;
    }

    public String getTreatmentStageID() {
        return treatmentStageID;
    }

    public void setTreatmentStageID(String treatmentStageID) {
        this.treatmentStageID = treatmentStageID;
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

    public Set<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(Set<Long> siteID) {
        this.siteID = siteID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRegistrationTimeFrom() {
        return registrationTimeFrom;
    }

    public void setRegistrationTimeFrom(String registrationTimeFrom) {
        this.registrationTimeFrom = registrationTimeFrom;
    }

    public String getRegistrationTimeTo() {
        return registrationTimeTo;
    }

    public void setRegistrationTimeTo(String registrationTimeTo) {
        this.registrationTimeTo = registrationTimeTo;
    }

    public String getTreatmentTimeFrom() {
        return treatmentTimeFrom;
    }

    public void setTreatmentTimeFrom(String treatmentTimeFrom) {
        this.treatmentTimeFrom = treatmentTimeFrom;
    }

    public String getTreatmentTimeTo() {
        return treatmentTimeTo;
    }

    public void setTreatmentTimeTo(String treatmentTimeTo) {
        this.treatmentTimeTo = treatmentTimeTo;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getTherapySiteID() {
        return therapySiteID;
    }

    public void setTherapySiteID(String therapySiteID) {
        this.therapySiteID = therapySiteID;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}

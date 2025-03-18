package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author vvThành
 */
public class OpcArvSearch {

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
    private boolean isOpcManage;

    private String dateOfArrivalFrom;
    private String dateOfArrivalTo;
    private String tranferToTimeFrom;
    private String tranferToTimeTo;
    private String tranferFromTimeFrom;
    private String tranferFromTimeTo;
    private String keywords;
    private String registrationType;
    private String treatmentRegimenID;
    private String insuranceExpiry;
    private String lastExaminationTimeFrom;
    private String lastExaminationTimeTo;
    private String gsph;

    private String inhFrom;
    private String inhTo;
    private String cotriFrom;
    private String cotriTo;
    private String laoFrom;
    private String laoTo;
    private String time90;
    private String time84;
    private String time30;
    
    private Set<String> statusOfTreatmentIDs;
    private Set<String> treatmentStageIDs;
    private Set<String> treatmentRegimenIDs;

    //Phân trang
    private int pageIndex;
    private int pageSize;

    public Set<String> getTreatmentRegimenIDs() {
        return treatmentRegimenIDs;
    }

    public void setTreatmentRegimenIDs(Set<String> treatmentRegimenIDs) {
        this.treatmentRegimenIDs = treatmentRegimenIDs;
    }

    public Set<String> getTreatmentStageIDs() {
        return treatmentStageIDs;
    }

    public void setTreatmentStageIDs(Set<String> treatmentStageIDs) {
        this.treatmentStageIDs = treatmentStageIDs;
    }

    public Set<String> getStatusOfTreatmentIDs() {
        return statusOfTreatmentIDs;
    }

    public void setStatusOfTreatmentIDs(Set<String> statusOfTreatmentIDs) {
        this.statusOfTreatmentIDs = statusOfTreatmentIDs;
    }

    public boolean isIsOpcManage() {
        return isOpcManage;
    }

    public void setIsOpcManage(boolean isOpcManage) {
        this.isOpcManage = isOpcManage;
    }

    public String getGsph() {
        return gsph;
    }

    public void setGsph(String gsph) {
        this.gsph = gsph;
    }

    public String getTime84() {
        return time84;
    }

    public void setTime84(String time84) {
        this.time84 = time84;
    }

    public String getTime30() {
        return time30;
    }

    public void setTime30(String time30) {
        this.time30 = time30;
    }

    public String getTime90() {
        return time90;
    }

    public void setTime90(String time90) {
        this.time90 = time90;
    }
    
    public String getInhFrom() {
        return inhFrom;
    }

    public void setInhFrom(String inhFrom) {
        this.inhFrom = inhFrom;
    }

    public String getInhTo() {
        return inhTo;
    }

    public void setInhTo(String inhTo) {
        this.inhTo = inhTo;
    }

    public String getCotriFrom() {
        return cotriFrom;
    }

    public void setCotriFrom(String cotriFrom) {
        this.cotriFrom = cotriFrom;
    }

    public String getCotriTo() {
        return cotriTo;
    }

    public void setCotriTo(String cotriTo) {
        this.cotriTo = cotriTo;
    }

    public String getLaoFrom() {
        return laoFrom;
    }

    public void setLaoFrom(String laoFrom) {
        this.laoFrom = laoFrom;
    }

    public String getLaoTo() {
        return laoTo;
    }

    public void setLaoTo(String laoTo) {
        this.laoTo = laoTo;
    }

    public String getLastExaminationTimeFrom() {
        return lastExaminationTimeFrom;
    }

    public void setLastExaminationTimeFrom(String lastExaminationTimeFrom) {
        this.lastExaminationTimeFrom = lastExaminationTimeFrom;
    }

    public String getLastExaminationTimeTo() {
        return lastExaminationTimeTo;
    }

    public void setLastExaminationTimeTo(String lastExaminationTimeTo) {
        this.lastExaminationTimeTo = lastExaminationTimeTo;
    }

    public String getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(String insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public String getTreatmentRegimenID() {
        return treatmentRegimenID;
    }

    public void setTreatmentRegimenID(String treatmentRegimenID) {
        this.treatmentRegimenID = treatmentRegimenID;
    }

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

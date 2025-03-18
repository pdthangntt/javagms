package com.gms.entity.form;

import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmPrepTableImportForm {

    //Trường cơ bản prep
    private String site;
    private String fullName;
    private String yearOfBirth;
    private String genderID;
    private String code;
    private String type;
    private String identityCard;
    private String patientPhone;
    private String objectGroupID;
    private String source;
    private String otherSite;
    private String projectSupport;
    private String startTime;
    private String treatmentRegimen;
    private String daysReceived;
    private String appointmentTime;
    private String resultsID;
    private String creatinin;
    private String hBsAg;
    private String ganC;
    private String giangMai;
    private String clamydia;
    private String lau;
    private String sot;
    private String metMoi;
    private String dauCo;
    private String phatBan;
    private String endTime; //Ngày kết thúc điều trị
    private String endCause; //Ngày kết thúc điều trị

    private List<PqmPrepVisitTableImportForm> visits;

    public String getEndCause() {
        return endCause;
    }

    public void setEndCause(String endCause) {
        this.endCause = endCause;
    }

    public List<PqmPrepVisitTableImportForm> getVisits() {
        return visits;
    }

    public void setVisits(List<PqmPrepVisitTableImportForm> visits) {
        this.visits = visits;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOtherSite() {
        return otherSite;
    }

    public void setOtherSite(String otherSite) {
        this.otherSite = otherSite;
    }

    public String getProjectSupport() {
        return projectSupport;
    }

    public void setProjectSupport(String projectSupport) {
        this.projectSupport = projectSupport;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTreatmentRegimen() {
        return treatmentRegimen;
    }

    public void setTreatmentRegimen(String treatmentRegimen) {
        this.treatmentRegimen = treatmentRegimen;
    }

    public String getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(String daysReceived) {
        this.daysReceived = daysReceived;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    public String getCreatinin() {
        return creatinin;
    }

    public void setCreatinin(String creatinin) {
        this.creatinin = creatinin;
    }

    public String gethBsAg() {
        return hBsAg;
    }

    public void sethBsAg(String hBsAg) {
        this.hBsAg = hBsAg;
    }

    public String getGanC() {
        return ganC;
    }

    public void setGanC(String ganC) {
        this.ganC = ganC;
    }

    public String getGiangMai() {
        return giangMai;
    }

    public void setGiangMai(String giangMai) {
        this.giangMai = giangMai;
    }

    public String getClamydia() {
        return clamydia;
    }

    public void setClamydia(String clamydia) {
        this.clamydia = clamydia;
    }

    public String getLau() {
        return lau;
    }

    public void setLau(String lau) {
        this.lau = lau;
    }

    public String getSot() {
        return sot;
    }

    public void setSot(String sot) {
        this.sot = sot;
    }

    public String getMetMoi() {
        return metMoi;
    }

    public void setMetMoi(String metMoi) {
        this.metMoi = metMoi;
    }

    public String getDauCo() {
        return dauCo;
    }

    public void setDauCo(String dauCo) {
        this.dauCo = dauCo;
    }

    public String getPhatBan() {
        return phatBan;
    }

    public void setPhatBan(String phatBan) {
        this.phatBan = phatBan;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}

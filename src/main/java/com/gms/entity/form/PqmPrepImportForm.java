package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class PqmPrepImportForm extends BaseEntity implements Serializable {

    //Trường cơ bản prep
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

    private String endTime;
    private String endCause;

    private String examinationTime1;
    private String resultsID1;
    private String treatmentStatus1;
    private String treatmentRegimen1;
    private String daysReceived1;
    private String appointmentTime1;

    private String creatinin1;
    private String hBsAg1;
    private String ganC1;
    private String giangMai1;
    private String clamydia1;
    private String lau1;
    private String sot1;
    private String metMoi1;
    private String dauCo1;
    private String phatBan1;

    private String tuanThuDieuTri1;
    private String tacDungPhu1;

    private int firstIndex;

    public String getEndCause() {
        return endCause;
    }

    public void setEndCause(String endCause) {
        this.endCause = endCause;
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

    public String getCreatinin1() {
        return creatinin1;
    }

    public void setCreatinin1(String creatinin1) {
        this.creatinin1 = creatinin1;
    }

    public String gethBsAg1() {
        return hBsAg1;
    }

    public void sethBsAg1(String hBsAg1) {
        this.hBsAg1 = hBsAg1;
    }

    public String getGanC1() {
        return ganC1;
    }

    public void setGanC1(String ganC1) {
        this.ganC1 = ganC1;
    }

    public String getGiangMai1() {
        return giangMai1;
    }

    public void setGiangMai1(String giangMai1) {
        this.giangMai1 = giangMai1;
    }

    public String getClamydia1() {
        return clamydia1;
    }

    public void setClamydia1(String clamydia1) {
        this.clamydia1 = clamydia1;
    }

    public String getLau1() {
        return lau1;
    }

    public void setLau1(String lau1) {
        this.lau1 = lau1;
    }

    public String getSot1() {
        return sot1;
    }

    public void setSot1(String sot1) {
        this.sot1 = sot1;
    }

    public String getMetMoi1() {
        return metMoi1;
    }

    public void setMetMoi1(String metMoi1) {
        this.metMoi1 = metMoi1;
    }

    public String getDauCo1() {
        return dauCo1;
    }

    public void setDauCo1(String dauCo1) {
        this.dauCo1 = dauCo1;
    }

    public String getPhatBan1() {
        return phatBan1;
    }

    public void setPhatBan1(String phatBan1) {
        this.phatBan1 = phatBan1;
    }

    public String getTuanThuDieuTri1() {
        return tuanThuDieuTri1;
    }

    public void setTuanThuDieuTri1(String tuanThuDieuTri1) {
        this.tuanThuDieuTri1 = tuanThuDieuTri1;
    }

    public String getTacDungPhu1() {
        return tacDungPhu1;
    }

    public void setTacDungPhu1(String tacDungPhu1) {
        this.tacDungPhu1 = tacDungPhu1;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
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

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
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

    public String getExaminationTime1() {
        return examinationTime1;
    }

    public void setExaminationTime1(String examinationTime1) {
        this.examinationTime1 = examinationTime1;
    }

    public String getResultsID1() {
        return resultsID1;
    }

    public void setResultsID1(String resultsID1) {
        this.resultsID1 = resultsID1;
    }

    public String getTreatmentStatus1() {
        return treatmentStatus1;
    }

    public void setTreatmentStatus1(String treatmentStatus1) {
        this.treatmentStatus1 = treatmentStatus1;
    }

    public String getTreatmentRegimen1() {
        return treatmentRegimen1;
    }

    public void setTreatmentRegimen1(String treatmentRegimen1) {
        this.treatmentRegimen1 = treatmentRegimen1;
    }

    public String getDaysReceived1() {
        return daysReceived1;
    }

    public void setDaysReceived1(String daysReceived1) {
        this.daysReceived1 = daysReceived1;
    }

    public String getAppointmentTime1() {
        return appointmentTime1;
    }

    public void setAppointmentTime1(String appointmentTime1) {
        this.appointmentTime1 = appointmentTime1;
    }

}

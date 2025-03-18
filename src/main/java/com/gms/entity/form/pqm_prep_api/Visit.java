package com.gms.entity.form.pqm_prep_api;

import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class Visit extends BaseEntity {

    private String examinationTime; //Ngày khám bệnh - Ngày bắt đầu điều trị với T0

    private String resultsID; // Kết quả XN - Lấy thời enum kết quả khẳng định

    private String treatmentStatus; //Kết quả điều trị - Sử dụng enum

    private String treatmentRegimen; //Phác đồ điều trị

    private String daysReceived; //Số ngày nhận thuốc

    private String appointmentTime; // Ngày hẹn khám  

    private String creatinin;

    private String hBsAg;

    private String ganC;

    private String giangMai;

    private String clamydia;

    private String lau;

    private String tuanThuDieuTri;

    private String tacDungPhu;

    private String sot;

    private String metMoi;

    private String dauCo;

    private String phatBan;

    public String getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(String examinationTime) {
        this.examinationTime = examinationTime;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    public String getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(String treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
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

    public String getTuanThuDieuTri() {
        return tuanThuDieuTri;
    }

    public void setTuanThuDieuTri(String tuanThuDieuTri) {
        this.tuanThuDieuTri = tuanThuDieuTri;
    }

    public String getTacDungPhu() {
        return tacDungPhu;
    }

    public void setTacDungPhu(String tacDungPhu) {
        this.tacDungPhu = tacDungPhu;
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

}

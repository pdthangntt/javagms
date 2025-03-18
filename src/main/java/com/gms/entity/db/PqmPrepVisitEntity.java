package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Lượt khám
 */
@Entity
@Table(name = "PQM_PREP_VISIT", indexes = {
    @Index(name = "OS_ID", columnList = "ID"),
    @Index(name = "OS_APPOINMENT_TIME", columnList = "APPOINMENT_TIME"),
    @Index(name = "OS_DAYS_RECEIVED", columnList = "DAYS_RECEIVED"),
    @Index(name = "OS_EXAMINATION_TIME", columnList = "EXAMINATION_TIME"),
    @Index(name = "OS_PREP_ID", columnList = "PREP_ID"),
    @Index(name = "OS_RESULTS_ID", columnList = "RESULTS_ID"),
    @Index(name = "OS_TREATMENT_REGIMENT", columnList = "TREATMENT_REGIMENT"),
    @Index(name = "OS_TREATMENT_STATUS", columnList = "TREATMENT_STATUS"),
    @Index(name = "OS_STAGE_ID", columnList = "STAGE_ID")
})
@DynamicUpdate
@DynamicInsert
public class PqmPrepVisitEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "PREP_ID")
    private Long prepID; //Mã PrEP

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXAMINATION_TIME", nullable = false)
    private Date examinationTime; //Ngày khám bệnh - Ngày bắt đầu điều trị với T0

    @Column(name = "RESULTS_ID", length = 50)
    private String resultsID; // Kết quả XN - Lấy thời enum kết quả khẳng định

    @Column(name = "TREATMENT_STATUS")
    private String treatmentStatus; //Kết quả điều trị - Sử dụng enum

    @Column(name = "TREATMENT_REGIMENT")
    private String treatmentRegimen; //Phác đồ điều trị

    @Column(name = "DAYS_RECEIVED", nullable = true)
    private int daysReceived; //Số ngày nhận thuốc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPOINMENT_TIME", nullable = true)
    private Date appointmentTime; // Ngày hẹn khám  

    @Column(name = "STAGE_ID", nullable = false)
    private Long stageID;

    @Column(name = "CREATININ")
    private String creatinin;

    @Column(name = "HBSAG")
    private String hBsAg;

    @Column(name = "GAN_C")
    private String ganC;

    @Column(name = "GIANG_MAI")
    private String giangMai;

    @Column(name = "CLAMYDIA")
    private String clamydia;

    @Column(name = "LAU")
    private String lau;

    @Column(name = "TUAN_THU_DIEU_TRI")
    private String tuanThuDieuTri;

    @Column(name = "TAC_DUNG_PHU")
    private String tacDungPhu;

    @Column(name = "SOT")
    private String sot;

    @Column(name = "MET_MOI")
    private String metMoi;

    @Column(name = "DAU_CO")
    private String dauCo;

    @Column(name = "PHAT_BAN")
    private String phatBan;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY", nullable = false)
    private Long updatedBY;

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

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
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

    public int getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(int daysReceived) {
        this.daysReceived = daysReceived;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPrepID() {
        return prepID;
    }

    public void setPrepID(Long prepID) {
        this.prepID = prepID;
    }

    public Date getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(Date examinationTime) {
        this.examinationTime = examinationTime;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public Long getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(Long updatedBY) {
        this.updatedBY = updatedBY;
    }

}

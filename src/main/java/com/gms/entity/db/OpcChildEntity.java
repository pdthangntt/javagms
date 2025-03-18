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
 * @author TrangBN
 * @des Thông tin trẻ sinh ra từ mẹ nhiễm HIV
 */
@Entity
@Table(name = "OPC_CHILD",
        indexes = {
            @Index(name = "OR_VISIT_ID", columnList = "VISIT_ID")
            ,@Index(name = "OR_PATIENT_ID", columnList = "PATIENT_ID")
            ,@Index(name = "OR_ARV_ID", columnList = "ARV_ID")
        })
@DynamicUpdate
@DynamicInsert
public class OpcChildEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "ARV_ID", length = 50, nullable = false)
    private Long arvID; //Mã bệnh án

    @Column(name = "PATIENT_ID")
    private Long patientID; //FK thông tin cơ bản của bệnh nhân
    
    @Column(name = "STAGE_ID")
    private Long stageID; //Mã giai đoạn điều trị
    
    @Column(name = "VISIT_ID")
    private Long visitID; // Mã lượt khám

    @Column(name = "DOB", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob; //Ngày/tháng/năm sinh
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COTRIMOXAZOLE_FROM_TIME", nullable = true)
    private Date cotrimoxazoleFromTime; //ngày cotrimoxazole bắt đầu

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COTRIMOXAZOLE_TO_TIME", nullable = true)
    private Date cotrimoxazoleToTime; // ngày cotrimoxazole kết thúc
    
    @Column(name = "PREVENTIVE_COTRIMOXAZOLE", nullable = false)
    private boolean preventiveCotrimoxazole; // Dự phòng cotrimoxazole
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PCR_BLOOD_ONE_TIME", nullable = true)
    private Date pcrBloodOneTime; //Ngày lấy mẫu PCR lần 1
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PCR_ONE_TIME", nullable = true)
    private Date pcrOneTime; //Ngày xn pcr lần 01
    
    @Column(name = "PCR_ONE_RESULT", length = 50, nullable = true)
    private String pcrOneResult; //Kết quả xn pcr lần 01
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PCR_BLOOD_TWO_TIME", nullable = true)
    private Date pcrBloodTwoTime; //Ngày lấy mẫu PCR lần 2
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PCR_TWO_TIME", nullable = true)
    private Date pcrTwoTime; //Ngày xn pcr lần 02
    
    @Column(name = "PCR_TWO_RESULT", length = 50, nullable = true)
    private String pcrTwoResult; //Kết quả xn pcr lần 02
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TREATMENT_TIME", nullable = true)
    private Date treatmentTime; //Ngày điều trị ARV tại cơ sở OPC

    @Column(name = "PREVENTIVE_DATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date preventiveDate; // Ngày bắt đầu dự phòng ARV
    
    @Column(name = "PREVENTIVE_END_DATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date preventiveEndDate; // Ngày kết thúc dự phòng ARV

    public Date getPreventiveEndDate() {
        return preventiveEndDate;
    }

    public void setPreventiveEndDate(Date preventiveEndDate) {
        this.preventiveEndDate = preventiveEndDate;
    }
    
    public Date getPcrBloodOneTime() {
        return pcrBloodOneTime;
    }

    public void setPcrBloodOneTime(Date pcrBloodOneTime) {
        this.pcrBloodOneTime = pcrBloodOneTime;
    }

    public Date getPcrOneTime() {
        return pcrOneTime;
    }

    public void setPcrOneTime(Date pcrOneTime) {
        this.pcrOneTime = pcrOneTime;
    }

    public Date getPcrBloodTwoTime() {
        return pcrBloodTwoTime;
    }

    public void setPcrBloodTwoTime(Date pcrBloodTwoTime) {
        this.pcrBloodTwoTime = pcrBloodTwoTime;
    }

    public Date getPcrTwoTime() {
        return pcrTwoTime;
    }

    public void setPcrTwoTime(Date pcrTwoTime) {
        this.pcrTwoTime = pcrTwoTime;
    }

    public String getPcrTwoResult() {
        return pcrTwoResult;
    }

    public void setPcrTwoResult(String pcrTwoResult) {
        this.pcrTwoResult = pcrTwoResult;
    }
    
    public Date getPreventiveDate() {
        return preventiveDate;
    }

    public void setPreventiveDate(Date preventiveDate) {
        this.preventiveDate = preventiveDate;
    }
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getArvID() {
        return arvID;
    }

    public void setArvID(Long arvID) {
        this.arvID = arvID;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public Long getVisitID() {
        return visitID;
    }

    public void setVisitID(Long visitID) {
        this.visitID = visitID;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getCotrimoxazoleFromTime() {
        return cotrimoxazoleFromTime;
    }

    public void setCotrimoxazoleFromTime(Date cotrimoxazoleFromTime) {
        this.cotrimoxazoleFromTime = cotrimoxazoleFromTime;
    }

    public Date getCotrimoxazoleToTime() {
        return cotrimoxazoleToTime;
    }

    public void setCotrimoxazoleToTime(Date cotrimoxazoleToTime) {
        this.cotrimoxazoleToTime = cotrimoxazoleToTime;
    }

    public boolean isPreventiveCotrimoxazole() {
        return preventiveCotrimoxazole;
    }

    public void setPreventiveCotrimoxazole(boolean preventiveCotrimoxazole) {
        this.preventiveCotrimoxazole = preventiveCotrimoxazole;
    }

    public String getPcrOneResult() {
        return pcrOneResult;
    }

    public void setPcrOneResult(String pcrOneResult) {
        this.pcrOneResult = pcrOneResult;
    }

    public Date getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(Date treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
    }
}

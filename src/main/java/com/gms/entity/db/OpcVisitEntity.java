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
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Lượt khám
 */
@Entity
@Table(name = "OPC_VISIT", indexes = {
    @Index(name = "OS_SITE_ID", columnList = "SITE_ID")
    ,@Index(name = "OS_ARV_ID", columnList = "ARV_ID")
    ,@Index(name = "OS_PATIENT_ID", columnList = "PATIENT_ID")
    ,@Index(name = "OS_EXAMINATION_TIME", columnList = "EXAMINATION_TIME")
    ,@Index(name = "OS_IS_REMOVE", columnList = "IS_REMOVE")})
@DynamicUpdate
@DynamicInsert
public class OpcVisitEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Column(name = "STAGE_ID")
    private Long stageID; //Mã giai đoạn điều trị

    @Transient
    private String dataStageID; //Mã giai đoạn điều trị cũ

    @Column(name = "PATIENT_ID")
    private Long patientID; //FK thông tin cơ bản của bệnh nhân

    @Column(name = "INSURANCE", length = 50, nullable = false)
    private String insurance; //Có thẻ BHYT ? - mặc định không có thông tin

    @Column(name = "INSURANCE_NO", length = 50, nullable = true)
    private String insuranceNo; //Số thẻ BHYT

    @Column(name = "INSURANCE_TYPE", length = 3, nullable = true)
    private String insuranceType; //Loại thanh toán thẻ BHYT - Sử dụng enum

    @Column(name = "INSURANCE_SITE", nullable = true)
    private String insuranceSite; //Nơi đăng ký kcb ban đầu

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSURANCE_EXPIRY", nullable = true)
    private Date insuranceExpiry; //Ngày hết hạn thẻ BHYT

    @Column(name = "INSURANCE_PAY", length = 3, nullable = true)
    private String insurancePay; //Tỉ lệ thanh toán thẻ BHYT - Sử dụng enum

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXAMINATION_TIME", nullable = false)
    private Date examinationTime; //Ngày khám bệnh

    @Column(name = "TREATMENT_REGIMENT_STAGE", length = 1, nullable = true)
    private String treatmentRegimenStage; //Bậc phác đồ điều trị - sử dụng enum

    @Column(name = "TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String treatmentRegimenID; //Phác đồ điều trị tại cơ sở OPC

    @Column(name = "CIRCUIT", length = 3, nullable = true)
    private String circuit; //Mạch lần/phút - chỉ nhập số

    @Column(name = "BLOOD_PRESSURE", length = 20, nullable = true)
    private String bloodPressure; //Huyết áp - chỉ nhập số

    @Column(name = "BODY_TEMPERATURE", length = 5, nullable = true)
    private String bodyTemperature; //Thân nhiệt - chỉ nhập số

    @Column(name = "DIAGNOSTIC", columnDefinition = "TEXT", nullable = false)
    private String diagnostic; //Chuẩn đoán

    @Column(name = "MEDICATION_ADHERENCE", nullable = true)
    private String medicationAdherence; //Tuân thủ điều trị

    @Column(name = "DAYS_RECEIVED", nullable = true)
    private int daysReceived; //Số ngày nhận thuốc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPOINMENT_TIME", nullable = true)
    private Date appointmentTime; // Ngày hẹn khám   

    @Column(name = "NOTE", columnDefinition = "TEXT", nullable = true)
    private String note; //Ghi chú

    @Column(name = "MSG", columnDefinition = "TEXT", nullable = true)
    private String msg; //Lời dặn đơn thuốc

    @Column(name = "CLINICAL", length = 220, nullable = true)
    private String clinical; //Lâm sàng

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

    @Column(name = "IS_REMOVE")
    private boolean remove; //Xoá logic

    @Column(name = "CONSULT")
    private boolean consult; // Tư vấn tăng cường tuân thủ điều trị

    @Column(name = "IS_IMPORT")
    private boolean importable; // Đánh dấu bản ghi import

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REMOVE_AT", nullable = true)
    private Date remoteAT;

    @Column(name = "DATA_TYPE", length = 5, nullable = true)
    private String dataType; //Loại dữ liệu: ims/vnpt

    @Column(name = "DATA_ID", length = 50, nullable = true)
    private String dataID; //mã bên nguồn đồng bộ

    @Column(name = "PATIENT_WEIGHT", nullable = false)
    private float patientWeight; //Câng nặng

    @Column(name = "PATIENT_HEIGHT", nullable = false)
    private float patientHeight; //Chiều cao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUPPLY_MEDICINE_DATE", nullable = true)
    private Date supplyMedicinceDate; // Ngày đầu tiên cáp thuốc nhiều tháng

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIVED_WARD_DATE", nullable = true)
    private Date receivedWardDate; // Ngày nhận thuốc tại xã

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_START_DATE", nullable = true)
    private Date pregnantStartDate; // Ngày bắt đầu thai ký

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PREGNANT_END_DATE", nullable = true)
    private Date pregnantEndDate; // Ngày chuyển dạ đẻ

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "ROUTE_ID", length = 50, nullable = true)
    private String routeID; // Tuyến đăng ký bảo hiểm

    @Column(name = "OLD_TREATMENT_REGIMENT_STAGE", length = 1, nullable = true)
    private String oldTreatmentRegimenStage; //Bậc phác đồ điều trị cũ- sử dụng enum 

    @Column(name = "OLD_TREATMENT_REGIMENT_ID", length = 50, nullable = true)
    private String oldTreatmentRegimenID; //Phác đồ điều trị cũ tại cơ sở OPC  

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGIMENT_STAGE_DATE", nullable = true)
    private Date regimenStageDate; // Ngày thay đổi bậc phác đồ

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGIMENT_DATE", nullable = true)
    private Date regimenDate; // Ngày thay đổi phác đồ

    @Column(name = "CAUSES_CHANGE", length = 50, nullable = true)
    private String causesChange; // Lý do thay đổi

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTH_PLAN_DATE", nullable = true)
    private Date birthPlanDate; // Ngày dự sinh

    public String getDataStageID() {
        return dataStageID;
    }

    public void setDataStageID(String dataStageID) {
        this.dataStageID = dataStageID;
    }

    public boolean isImportable() {
        return importable;
    }

    public void setImportable(boolean importable) {
        this.importable = importable;
    }

    public Date getBirthPlanDate() {
        return birthPlanDate;
    }

    public void setBirthPlanDate(Date birthPlanDate) {
        this.birthPlanDate = birthPlanDate;
    }

    public boolean isConsult() {
        return consult;
    }

    public void setConsult(boolean consult) {
        this.consult = consult;
    }

    public Date getRegimenStageDate() {
        return regimenStageDate;
    }

    public void setRegimenStageDate(Date regimenStageDate) {
        this.regimenStageDate = regimenStageDate;
    }

    public Date getRegimenDate() {
        return regimenDate;
    }

    public void setRegimenDate(Date regimenDate) {
        this.regimenDate = regimenDate;
    }

    public String getCausesChange() {
        return causesChange;
    }

    public void setCausesChange(String causesChange) {
        this.causesChange = causesChange;
    }

    public String getOldTreatmentRegimenStage() {
        return oldTreatmentRegimenStage;
    }

    public void setOldTreatmentRegimenStage(String oldTreatmentRegimenStage) {
        this.oldTreatmentRegimenStage = oldTreatmentRegimenStage;
    }

    public String getOldTreatmentRegimenID() {
        return oldTreatmentRegimenID;
    }

    public void setOldTreatmentRegimenID(String oldTreatmentRegimenID) {
        this.oldTreatmentRegimenID = oldTreatmentRegimenID;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public Date getPregnantStartDate() {
        return pregnantStartDate;
    }

    public void setPregnantStartDate(Date pregnantStartDate) {
        this.pregnantStartDate = pregnantStartDate;
    }

    public Date getPregnantEndDate() {
        return pregnantEndDate;
    }

    public void setPregnantEndDate(Date pregnantEndDate) {
        this.pregnantEndDate = pregnantEndDate;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public Date getSupplyMedicinceDate() {
        return supplyMedicinceDate;
    }

    public void setSupplyMedicinceDate(Date supplyMedicinceDate) {
        this.supplyMedicinceDate = supplyMedicinceDate;
    }

    public Date getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(Date receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getClinical() {
        return clinical;
    }

    public void setClinical(String clinical) {
        this.clinical = clinical;
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
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

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceSite() {
        return insuranceSite;
    }

    public void setInsuranceSite(String insuranceSite) {
        this.insuranceSite = insuranceSite;
    }

    public Date getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(Date insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public String getInsurancePay() {
        return insurancePay;
    }

    public void setInsurancePay(String insurancePay) {
        this.insurancePay = insurancePay;
    }

    public Date getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(Date examinationTime) {
        this.examinationTime = examinationTime;
    }

    public String getTreatmentRegimenStage() {
        return treatmentRegimenStage;
    }

    public void setTreatmentRegimenStage(String treatmentRegimenStage) {
        this.treatmentRegimenStage = treatmentRegimenStage;
    }

    public String getTreatmentRegimenID() {
        return treatmentRegimenID;
    }

    public void setTreatmentRegimenID(String treatmentRegimenID) {
        this.treatmentRegimenID = treatmentRegimenID;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getMedicationAdherence() {
        return medicationAdherence;
    }

    public void setMedicationAdherence(String medicationAdherence) {
        this.medicationAdherence = medicationAdherence;
    }

    public int getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(int daysReceived) {
        this.daysReceived = daysReceived;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public Date getRemoteAT() {
        return remoteAT;
    }

    public void setRemoteAT(Date remoteAT) {
        this.remoteAT = remoteAT;
    }

}

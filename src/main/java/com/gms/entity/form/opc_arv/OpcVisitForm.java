package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.db.BaseEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcChildEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author vvThành
 */
public class OpcVisitForm extends BaseEntity implements Serializable {
    private String ID;
    private String patientID;
    private String siteID;
    private String insurance; //Có thẻ BHYT ? - mặc định không có thông tin
    private String insuranceNo; //Số thẻ BHYT
    private String insuranceType; //Loại thanh toán thẻ BHYT - Sử dụng enum
    private String insuranceSite; //Nơi đăng ký kcb ban đầu
    private String insuranceExpiry; //Ngày hết hạn thẻ BHYT
    private String insurancePay; //Tỉ lệ thanh toán thẻ BHYT - Sử dụng enum
    private String lastExaminationTime; //Ngày khám bệnh
    private String treatmentRegimenStage; //Bậc phác đồ điều trị - sử dụng enum
    private String treatmentRegimenID; //Phác đồ điều trị tại cơ sở OPC
    private String circuit; //Mạch lần/phút - chỉ nhập số
    private String bloodPressure; //Huyết áp - chỉ nhập số
    private String bodyTemperature; //Thân nhiệt - chỉ nhập số
    private String diagnostic; //Chuẩn đoán
    private String medicationAdherence; //Tuân thủ điều trị
    private String daysReceived; //Số ngày nhận thuốc
    private String appointmentTime; // Ngày hẹn khám   
    private String clinical; //Lâm sàng
    private String msg; //Lời dặn thuốc
    private String note; //Ghi chú
    private String regimenStageDate; //Ghi chú
    private String regimenDate; // Ngày thay đổi phác đồ
    private String causesChange; // Lý do thay đổi
    private String supplyMedicinceDate;
    private String receivedWardDate; // Ngày nhận thuốc tại xã
    private String objectID;
    private String pregnantStartDate; // Ngày bắt đầu thai ký
    private String pregnantEndDate; // Ngày chuyển dạ đẻ
    private String stageID;
    private String patientWeight;
    private String patientHeight;
    private String route;
    private String treatmentTime;
    private String registrationTime;
    private String statusOfTreatmentID;
    private String endTime;
    private String oldTreatmentRegimenStage; //Bậc phác đồ điều trị cũ - sử dụng enum
    private String oldTreatmentRegimenID; //Phác đồ điều trị tại cũ cơ sở OPC
    private String baseTreatmentRegimenStage; //Bậc phác đồ điều trị ban đầu - sử dụng enum
    private String baseTreatmentRegimenID; //Phác đồ điều trị tại ban đầu cơ sở OPC
    private boolean isOtherSite;
    private String isPrinted;
    private String consult;
    private String birthPlanDate; // Ngày dự sinh
    private List<OpcChildForm> childs; // Danh sách người được giới thiệu

    public String getBirthPlanDate() {
        return birthPlanDate;
    }

    public void setBirthPlanDate(String birthPlanDate) {
        this.birthPlanDate = birthPlanDate;
    }
    
    public String getConsult() {
        return consult;
    }

    public void setConsult(String consult) {
        this.consult = consult;
    }

    public String getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(String isPrinted) {
        this.isPrinted = isPrinted;
    }

    public List<OpcChildForm> getChilds() {
        return childs;
    }

    public void setChilds(List<OpcChildForm> childs) {
        this.childs = childs;
    }

    public String getBaseTreatmentRegimenStage() {
        return baseTreatmentRegimenStage;
    }

    public void setBaseTreatmentRegimenStage(String baseTreatmentRegimenStage) {
        this.baseTreatmentRegimenStage = baseTreatmentRegimenStage;
    }

    public String getBaseTreatmentRegimenID() {
        return baseTreatmentRegimenID;
    }

    public void setBaseTreatmentRegimenID(String baseTreatmentRegimenID) {
        this.baseTreatmentRegimenID = baseTreatmentRegimenID;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getCausesChange() {
        return causesChange;
    }

    public void setCausesChange(String causesChange) {
        this.causesChange = causesChange;
    }

    public String getSupplyMedicinceDate() {
        return supplyMedicinceDate;
    }

    public void setSupplyMedicinceDate(String supplyMedicinceDate) {
        this.supplyMedicinceDate = supplyMedicinceDate;
    }

    public String getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(String receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getPregnantStartDate() {
        return pregnantStartDate;
    }

    public void setPregnantStartDate(String pregnantStartDate) {
        this.pregnantStartDate = pregnantStartDate;
    }

    public String getPregnantEndDate() {
        return pregnantEndDate;
    }

    public void setPregnantEndDate(String pregnantEndDate) {
        this.pregnantEndDate = pregnantEndDate;
    }

    public String getRegimenStageDate() {
        return regimenStageDate;
    }

    public void setRegimenStageDate(String regimenStageDate) {
        this.regimenStageDate = regimenStageDate;
    }

    public String getRegimenDate() {
        return regimenDate;
    }

    public void setRegimenDate(String regimenDate) {
        this.regimenDate = regimenDate;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }

    public String getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(String patientHeight) {
        this.patientHeight = patientHeight;
    }

    public boolean isIsOtherSite() {
        return isOtherSite;
    }

    public void setIsOtherSite(boolean isOtherSite) {
        this.isOtherSite = isOtherSite;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getInsurancePay() {
        return insurancePay;
    }

    public void setInsurancePay(String insurancePay) {
        this.insurancePay = insurancePay;
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

    public String getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(String daysReceived) {
        this.daysReceived = daysReceived;
    }

    public String getClinical() {
        return clinical;
    }

    public void setClinical(String clinical) {
        this.clinical = clinical;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(String insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public String getLastExaminationTime() {
        return lastExaminationTime;
    }

    public void setLastExaminationTime(String lastExaminationTime) {
        this.lastExaminationTime = lastExaminationTime;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    
    public OpcVisitForm(){
        
    }
    public OpcVisitForm(OpcVisitEntity model) {
        if(model == null){
            model = new OpcVisitEntity();
        }
        if(model.getID() != null){
            ID = model.getID().toString();
        }
        stageID = model.getStageID() == null ? "" : model.getStageID().toString();
        insurance = model.getInsurance() == null ? "" : model.getInsurance();
        insuranceNo = model.getInsuranceNo();
        insuranceType = model.getInsuranceType() == null ? "" : model.getInsuranceType();
        insuranceSite = model.getInsuranceSite();
        insuranceExpiry = TextUtils.formatDate(model.getInsuranceExpiry(), "dd/MM/yyyy");
        lastExaminationTime = TextUtils.formatDate(model.getExaminationTime(), "dd/MM/yyyy");
        insurancePay = model.getInsurancePay() == null ? "" : model.getInsurancePay();
        route = model.getRouteID() == null ? "" : model.getRouteID();
        treatmentRegimenStage = model.getTreatmentRegimenStage() == null ? "" : model.getTreatmentRegimenStage();
        treatmentRegimenID = model.getTreatmentRegimenID() == null ? "" : model.getTreatmentRegimenID();
        objectID = model.getObjectGroupID() == null ? "" : model.getObjectGroupID();
        pregnantStartDate = TextUtils.formatDate(model.getPregnantStartDate(), "dd/MM/yyyy");
        pregnantEndDate = TextUtils.formatDate(model.getPregnantEndDate(), "dd/MM/yyyy");
        oldTreatmentRegimenID = model.getOldTreatmentRegimenID() == null ? "" : model.getOldTreatmentRegimenID();
        oldTreatmentRegimenStage = model.getOldTreatmentRegimenStage() == null ? "" : model.getOldTreatmentRegimenStage();
        regimenStageDate = TextUtils.formatDate(model.getRegimenStageDate(), "dd/MM/yyyy");
        regimenDate = TextUtils.formatDate(model.getRegimenDate(), "dd/MM/yyyy");
        causesChange = model.getCausesChange();
        
        circuit = model.getCircuit();
        bloodPressure = model.getBloodPressure();
        bodyTemperature = model.getBodyTemperature();
        clinical = model.getClinical();
        diagnostic = model.getDiagnostic();
        medicationAdherence = model.getMedicationAdherence() == null ? "" : model.getMedicationAdherence();
        daysReceived = model.getDaysReceived() == 0 ? "" : (model.getDaysReceived() + "");
        appointmentTime = TextUtils.formatDate(model.getAppointmentTime(), "dd/MM/yyyy");
        msg = model.getMsg();
        note = model.getNote();
        patientWeight= model.getPatientWeight() == 0.0 ? "" : (model.getPatientWeight() + "");
        patientHeight = model.getPatientHeight() == 0.0 ? "" : ((int)model.getPatientHeight() + "");
        supplyMedicinceDate = TextUtils.formatDate(model.getSupplyMedicinceDate(), "dd/MM/yyyy");
        receivedWardDate = TextUtils.formatDate(model.getReceivedWardDate(), "dd/MM/yyyy");
        birthPlanDate = TextUtils.formatDate(model.getBirthPlanDate(), "dd/MM/yyyy");
        consult = model.isConsult() ? "1" : "0";
    }
    
    public OpcVisitForm(OpcArvEntity model, OpcStageEntity stage) {
        insurance = model.getInsurance();
        insuranceNo = model.getInsuranceNo();
        insuranceType = model.getInsuranceType();
        insuranceSite = model.getInsuranceSite();
        insuranceExpiry = TextUtils.formatDate(model.getInsuranceExpiry(), "dd/MM/yyyy");
        insurancePay = model.getInsurancePay();
        route = model.getRouteID();
        treatmentRegimenStage = model.getTreatmentRegimenStage();
        treatmentRegimenID = model.getTreatmentRegimenID();
        objectID = model.getObjectGroupID();
        pregnantStartDate = TextUtils.formatDate(model.getPregnantStartDate(), "dd/MM/yyyy");
        pregnantEndDate = TextUtils.formatDate(model.getPregnantEndDate(), "dd/MM/yyyy");
        birthPlanDate = TextUtils.formatDate(model.getJoinBornDate(), "dd/MM/yyyy");
        
        if(stage != null && stage.getID() != null){
           baseTreatmentRegimenID = stage.getTreatmentRegimenID();
           baseTreatmentRegimenStage = stage.getTreatmentRegimenStage();
           statusOfTreatmentID = stage.getStatusOfTreatmentID();
           registrationTime = TextUtils.formatDate(stage.getRegistrationTime(), "dd/MM/yyyy");
           treatmentTime = TextUtils.formatDate(stage.getTreatmentTime(), "dd/MM/yyyy");
           endTime = TextUtils.formatDate(stage.getEndTime(), "dd/MM/yyyy");
        }
    }
    
    public OpcVisitForm(OpcArvEntity model) {
        insurance = model.getInsurance();
        insuranceNo = model.getInsuranceNo();
        insuranceType = model.getInsuranceType();
        insuranceSite = model.getInsuranceSite();
        insuranceExpiry = TextUtils.formatDate(model.getInsuranceExpiry(), "dd/MM/yyyy");
        insurancePay = model.getInsurancePay();
        route = model.getRouteID();
        treatmentRegimenStage = model.getTreatmentRegimenStage() == null ? "" : model.getTreatmentRegimenStage();
        treatmentRegimenID = model.getTreatmentRegimenID() == null ? "" : model.getTreatmentRegimenID();
        objectID = model.getObjectGroupID() == null ? "" : model.getObjectGroupID();
        pregnantStartDate = TextUtils.formatDate(model.getPregnantStartDate(), "dd/MM/yyyy");
        pregnantEndDate = TextUtils.formatDate(model.getPregnantEndDate(), "dd/MM/yyyy");
        
        medicationAdherence = "";
        consult = "";
        diagnostic = "";
        clinical = "Bình thường";
        daysReceived = "30";
    }
    
     public OpcVisitEntity setToEntity(OpcVisitEntity model){
        model.setObjectGroupID(objectID);
        model.setStageID(Long.parseLong(getStageID().split("-")[0]));
        model.setInsurance(getInsurance());
        model.setInsuranceNo(StringUtils.isNotEmpty(getInsuranceNo()) ? getInsuranceNo().trim().toUpperCase() : "");
        model.setInsuranceType(getInsuranceType());
        model.setInsuranceSite(getInsuranceSite());
        model.setInsuranceExpiry(TextUtils.convertDate(getInsuranceExpiry(), "dd/MM/yyyy"));
        model.setInsurancePay(getInsurancePay());
        model.setRouteID(route);
        model.setExaminationTime(TextUtils.convertDate(getLastExaminationTime(), "dd/MM/yyyy"));
        model.setTreatmentRegimenStage(getTreatmentRegimenStage());
        model.setTreatmentRegimenID(getTreatmentRegimenID());
        model.setCircuit(StringUtils.isNotEmpty(getCircuit()) ? getCircuit().trim() : "");
        model.setBloodPressure(StringUtils.isNotEmpty(getBloodPressure()) ? getBloodPressure().trim() : "");
        model.setBodyTemperature(StringUtils.isNotEmpty(getBodyTemperature()) ? getBodyTemperature().trim() : "");
        model.setClinical(getClinical());
        model.setDiagnostic(getDiagnostic());
        model.setMedicationAdherence(getMedicationAdherence());
        model.setDaysReceived(Integer.parseInt(StringUtils.isEmpty(daysReceived) ? "0" : getDaysReceived()));
        model.setAppointmentTime(TextUtils.convertDate(getAppointmentTime(), "dd/MM/yyyy"));
        model.setMsg(getMsg());
        model.setNote(getNote());
        if(getPatientWeight() != null){
            model.setPatientWeight("".equals(getPatientWeight()) ? 0.0f : Float.parseFloat(getPatientWeight()));
        }
        if(getPatientHeight() != null){
            model.setPatientHeight("".equals(getPatientHeight()) ? 0.0f : Float.parseFloat(getPatientHeight()));
        }
        
        model.setRegimenStageDate(TextUtils.convertDate(getRegimenStageDate(), "dd/MM/yyyy"));
        model.setRegimenDate(TextUtils.convertDate(getRegimenDate(), "dd/MM/yyyy"));
        model.setOldTreatmentRegimenID(oldTreatmentRegimenID);
        model.setOldTreatmentRegimenStage(oldTreatmentRegimenStage);
        model.setCausesChange(causesChange);
        model.setSupplyMedicinceDate(TextUtils.convertDate(getSupplyMedicinceDate(), "dd/MM/yyyy"));
        model.setReceivedWardDate(TextUtils.convertDate(getReceivedWardDate(), "dd/MM/yyyy"));
        model.setPregnantStartDate(TextUtils.convertDate(getPregnantStartDate(), "dd/MM/yyyy"));
        model.setPregnantEndDate(TextUtils.convertDate(getPregnantEndDate(), "dd/MM/yyyy"));
        model.setBirthPlanDate(TextUtils.convertDate(getBirthPlanDate(), "dd/MM/yyyy"));
        model.setConsult("1".equals(consult));
        model.setBirthPlanDate(TextUtils.convertDate(getBirthPlanDate(), "dd/MM/yyyy"));
        return model;
     }
}

package com.gms.entity.form.pqm_arv_api;

import com.gms.entity.form.drug_new_api.*;
import com.gms.entity.form.opc_arv.*;
import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class Stage extends BaseEntity {

    private String registrationTime; //NGày đăng ký vào OPC

    private String registrationType; //Loại đăng ký - Sử dụng enum

    private String statusOfTreatmentID; //Trạng thái điều trị

    private String treatmentTime; //Ngày ARV tại cơ sở OPC

    private String endCase; //Lý do kết thúc điều trị

    private String endTime; // Ngày kết thúc tại OPC

    private String tranferFromTime; //Ngày chuyển gửi

    private String treatment_session_id;

    public String getTreatment_session_id() {
        return treatment_session_id;
    }

    public void setTreatment_session_id(String treatment_session_id) {
        this.treatment_session_id = treatment_session_id;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
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

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(String tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

}

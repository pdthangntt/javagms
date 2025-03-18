package com.gms.entity.form.pqm_arv_api;

import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class Visit extends BaseEntity {

    private String examinationTime; //Ngày khám bệnh

    private String daysReceived; //Số ngày nhận thuốc

    private String appointmentTime; // Ngày hẹn khám  

    private String mutipleMonth;

    private String admission_code;

    public String getAdmission_code() {
        return admission_code;
    }

    public void setAdmission_code(String admission_code) {
        this.admission_code = admission_code;
    }

    public String getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(String examinationTime) {
        this.examinationTime = examinationTime;
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

    public String getMutipleMonth() {
        return mutipleMonth;
    }

    public void setMutipleMonth(String mutipleMonth) {
        this.mutipleMonth = mutipleMonth;
    }

}

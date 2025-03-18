package com.gms.entity.form;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmArvSheet3ImportForm {

    private String arvCode; // mã arv
    private String examinationTime; //Ngày khám bệnh

    private String daysReceived; //Số ngày nhận thuốc

    private String appointmentTime; // Ngày hẹn khám  
    private String row;
    private String mutipleMonth;

    public String getMutipleMonth() {
        return mutipleMonth;
    }

    public void setMutipleMonth(String mutipleMonth) {
        this.mutipleMonth = mutipleMonth;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getArvCode() {
        return arvCode;
    }

    public void setArvCode(String arvCode) {
        this.arvCode = arvCode;
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

    public boolean set(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(this, fieldValue);
        return true;
    }

}

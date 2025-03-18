package com.gms.entity.form;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmArvSheet2ImportForm {

    private String arvCode; //Mã ARV

    private String registrationTime; //NGày đăng ký vào OPC

    private String registrationType; //Loại đăng ký - Sử dụng enum

    private String endTime;

    private String endCase;
    private String treatmentTime;

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public String getArvCode() {
        return arvCode;
    }

    public void setArvCode(String arvCode) {
        this.arvCode = arvCode;
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

    private String row;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public boolean set(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(this, fieldValue);
        return true;
    }

}

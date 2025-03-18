package com.gms.entity.form;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmArvSheet4ImportForm {

    private String arvCode; //Mã ARV

    private String type;
    private String startTime;
    private String endTime;
    private String endCase;
    private String row;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public boolean set(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(this, fieldValue);
        return true;
    }

}

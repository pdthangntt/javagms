package com.gms.entity.form;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmArvSheet5ImportForm {

    private String arvCode; // mã arv
    private String testTime; // TLVR - Ngày xét nghiệm

    private String result; //Kết quả xét nghiệm TLVR

    private String resultNumber; //Kết quả xét nghiệm TLVR số

    private String resultTime; //NGày trả kết quả
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

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }
    
    public boolean set(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(this, fieldValue);
        return true;
    }

}

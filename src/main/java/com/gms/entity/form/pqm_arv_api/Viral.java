package com.gms.entity.form.pqm_arv_api;

import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class Viral extends BaseEntity {

    private String testTime; // TLVR - Ngày xét nghiệm

    private String result; //Kết quả xét nghiệm TLVR

    private String resultNumber; //Kết quả xét nghiệm TLVR số

    private String resultTime; //NGày trả kết quả

    private String insuranceNo; //BHYT

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

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

}

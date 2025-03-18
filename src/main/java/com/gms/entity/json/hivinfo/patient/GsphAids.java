package com.gms.entity.json.hivinfo.patient;

import java.io.Serializable;
import java.util.Date;

/**
 * Lưu thông tin AIDS
 *
 */
public class GsphAids implements Serializable {

    private String code;
    private Date aidsDiagnosticDate; // Ngày chuẩn đoán AIDS
    private Integer testSiteID; // Mã nơi xét nghiệm
    private Integer therapyStatusID;  // Mã trạng thái điều trị
    private Integer registerSiteID;  // Mã nơi đăng ký

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getAidsDiagnosticDate() {
        return aidsDiagnosticDate;
    }

    public void setAidsDiagnosticDate(Date aidsDiagnosticDate) {
        this.aidsDiagnosticDate = aidsDiagnosticDate;
    }

    public Integer getTestSiteID() {
        return testSiteID;
    }

    public void setTestSiteID(Integer testSiteID) {
        this.testSiteID = testSiteID;
    }

    public Integer getTherapyStatusID() {
        return therapyStatusID;
    }

    public void setTherapyStatusID(Integer therapyStatusID) {
        this.therapyStatusID = therapyStatusID;
    }

    public Integer getRegisterSiteID() {
        return registerSiteID;
    }

    public void setRegisterSiteID(Integer registerSiteID) {
        this.registerSiteID = registerSiteID;
    }

}

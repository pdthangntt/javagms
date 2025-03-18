package com.gms.entity.json.hivinfo.patient;

import java.io.Serializable;

/**
 * Lưu thông tin mẫu giám sát phát hiện HIV
 *
 */
public class GsphHiv implements Serializable {

    private String code;
    private String testDate; // Ngày xét nghiệm
    private Integer sourceBloodSiteID; // Mã nơi lấy máu
    private Integer testSiteID; // Mã nơi xét nghiệm
    private Integer registerSiteID; // Mã nơi đăng ký
    private Integer hivTherapyStatusID; // Mã trạng thái điều trị HIV
    private String sourceData; // Nguồn số liệu

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public Integer getSourceBloodSiteID() {
        return sourceBloodSiteID;
    }

    public void setSourceBloodSiteID(Integer sourceBloodSiteID) {
        this.sourceBloodSiteID = sourceBloodSiteID;
    }

    public Integer getTestSiteID() {
        return testSiteID;
    }

    public void setTestSiteID(Integer testSiteID) {
        this.testSiteID = testSiteID;
    }

    public Integer getRegisterSiteID() {
        return registerSiteID;
    }

    public void setRegisterSiteID(Integer registerSiteID) {
        this.registerSiteID = registerSiteID;
    }

    public Integer getHivTherapyStatusID() {
        return hivTherapyStatusID;
    }

    public void setHivTherapyStatusID(Integer hivTherapyStatusID) {
        this.hivTherapyStatusID = hivTherapyStatusID;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

}

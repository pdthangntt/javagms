package com.gms.entity.json.hivinfo.patient;

import java.io.Serializable;

/**
 * Lưu thông tin mẫu giám sát phát hiện HIV Điều trị
 *
 * @author vvthanh
 */
public class GsphHivDieuTri implements Serializable {

    private String code;
    private Integer siteID; // Mã cơ sở
    private String startDate; // Ngày bắt đầu
    private String endDate; // Ngày kết thúc
    private Integer hasHealthInsurance; // Có bảo hiểm
    private String startDateHealthNo; // Ngày bắt đầu bảo hiểm
    private String endDateHealthNo; // Ngày kết thúc bảo hiểm
    private String healthInsuranceNo; // Số thẻ Bảo hiểm
    private String startDateTherapy; // Ngày bắt đầu điều trị
    private String startRegimen; // Phác đồ bắt đầu
    private String therapyRegimen; // Phác đồ điều trị
    private String registerType; // Loại đăng ký
    private String endReason; // Lý do kết thúc

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSiteID() {
        return siteID;
    }

    public void setSiteID(Integer siteID) {
        this.siteID = siteID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public void setHasHealthInsurance(Integer hasHealthInsurance) {
        this.hasHealthInsurance = hasHealthInsurance;
    }

    public String getStartDateHealthNo() {
        return startDateHealthNo;
    }

    public void setStartDateHealthNo(String startDateHealthNo) {
        this.startDateHealthNo = startDateHealthNo;
    }

    public String getEndDateHealthNo() {
        return endDateHealthNo;
    }

    public void setEndDateHealthNo(String endDateHealthNo) {
        this.endDateHealthNo = endDateHealthNo;
    }

    public String getHealthInsuranceNo() {
        return healthInsuranceNo;
    }

    public void setHealthInsuranceNo(String healthInsuranceNo) {
        this.healthInsuranceNo = healthInsuranceNo;
    }

    public String getStartDateTherapy() {
        return startDateTherapy;
    }

    public void setStartDateTherapy(String startDateTherapy) {
        this.startDateTherapy = startDateTherapy;
    }

    public String getStartRegimen() {
        return startRegimen;
    }

    public void setStartRegimen(String startRegimen) {
        this.startRegimen = startRegimen;
    }

    public String getTherapyRegimen() {
        return therapyRegimen;
    }

    public void setTherapyRegimen(String therapyRegimen) {
        this.therapyRegimen = therapyRegimen;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getEndReason() {
        return endReason;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

}

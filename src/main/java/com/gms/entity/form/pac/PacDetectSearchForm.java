package com.gms.entity.form.pac;

/**
 * Form tìm kiếm cho giám sát phát hiện
 * 
 * @author TrangBN
 */
public class PacDetectSearchForm {

    private String addressFilter; // Tiêu chí lọc theo địa chỉ
    private String patientStatus; // Trạng thái người nhiêm
    private String dateFilter;    // Lọc theo ngày xét nghiệm / Ngày chuyển sang quản lý
    private String fromTime;      // Thời gian từ
    private String toTime;        // Thời gian đến
    
    private String provinceID;
    private String districtID;
    private String wardID;
    private String provinceName;
    private String districtName;
    private String wardName;
    
    private String jobID;
    private String objectGroupID;    
    private String riskID;          // Nguy cơ lây nhiễm
    private String residentStatus;  // Hiện trạng cư trú
    private String statusTreatment;    //  Trạng thái điều trioj

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }
    
    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getRiskID() {
        return riskID;
    }

    public void setRiskID(String riskID) {
        this.riskID = riskID;
    }

    public String getResidentStatus() {
        return residentStatus;
    }

    public void setResidentStatus(String residentStatus) {
        this.residentStatus = residentStatus;
    }

    public String getStatusTreatment() {
        return statusTreatment;
    }

    public void setStatusTreatment(String statusTreatment) {
        this.statusTreatment = statusTreatment;
    }
}

package com.gms.entity.form.htc_confirm;

import java.util.List;

/**
 * Form cho sổ nhận và gửi kết quả xét nghiệm HIV
 * 
 * @author TrangBN
 */
public class ConfirmReceiveBookForm {
     
    // Tên báo cáo
    private String title;
    // Tên file
    private String fileName;
    // Tên cơ sở
    private String siteName;
    // Tên nhân viên
    private String staffName;
    // Tên cục, sở
    private String siteAgency;
    // Điều kiện ngày bắt đầu nhận mẫu
    private String sampleReceiveStartDate;
    // Điều kiện ngày kết thúc nhận mẫu
    private String sampleReceiveEndDate;
    // Điều kiện ngày bắt đầu làm xét nghiệm
    private String testStartDate;
    // Điều kiện ngày kết thúc làm xét nghiệm
    private String testEndDate;
    // Điều kiện ngày bắt đầu trả kết quả
    private String returnStartDate;
    // Điều kiện ngày kết thúc trả kết quả
    private String returnEndDate;
    // Danh sách ca dương tính
    private List<ConfirmReceiveBookTableForm> table;
    // ID của cơ sở hiện tại
    private Long siteID;
    // Site provider
    private String siteProvince;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSampleReceiveStartDate() {
        return sampleReceiveStartDate;
    }

    public void setSampleReceiveStartDate(String sampleReceiveStartDate) {
        this.sampleReceiveStartDate = sampleReceiveStartDate;
    }

    public String getSampleReceiveEndDate() {
        return sampleReceiveEndDate;
    }

    public void setSampleReceiveEndDate(String sampleReceiveEndDate) {
        this.sampleReceiveEndDate = sampleReceiveEndDate;
    }

    public String getTestStartDate() {
        return testStartDate;
    }

    public void setTestStartDate(String testStartDate) {
        this.testStartDate = testStartDate;
    }

    public String getTestEndDate() {
        return testEndDate;
    }

    public void setTestEndDate(String testEndDate) {
        this.testEndDate = testEndDate;
    }

    public String getReturnStartDate() {
        return returnStartDate;
    }

    public void setReturnStartDate(String returnStartDate) {
        this.returnStartDate = returnStartDate;
    }

    public String getReturnEndDate() {
        return returnEndDate;
    }

    public void setReturnEndDate(String returnEndDate) {
        this.returnEndDate = returnEndDate;
    }

    public List<ConfirmReceiveBookTableForm> getTable() {
        return table;
    }

    public void setTable(List<ConfirmReceiveBookTableForm> table) {
        this.table = table;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }    
}

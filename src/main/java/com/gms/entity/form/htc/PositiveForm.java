package com.gms.entity.form.htc;

import java.util.HashMap;
import java.util.List;

/**
 * Form for view PDF, excel, and sending mail
 *
 * @author TrangBN
 */
public class PositiveForm {

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
    // Điều kiện ngày bắt đầu tìm kiếm 
    private String start;
    // Điều kiện ngày kết thúc tìm kiếm
    private String end;
    private String code;
    // Dịch vụ
    private HashMap<String, String> services;
    // Đối tượng
    private HashMap<String, String> objects;
    // Danh sách ca dương tính
    private List<PositiveTableForm> table;
    // ID của cơ sở hiện tại
    private Long siteID;
    // Site provider
    private String siteProvince;
    //Danh sách định nghĩa cần hiển thị
    private HashMap<String, HashMap<String, String>> options;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public HashMap<String, String> getObjects() {
        return objects;
    }

    public void setObjects(HashMap<String, String> objects) {
        this.objects = objects;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public HashMap<String, String> getServices() {
        return services;
    }

    public void setServices(HashMap<String, String> services) {
        this.services = services;
    }

    public List<PositiveTableForm> getTable() {
        return table;
    }

    public void setTable(List<PositiveTableForm> table) {
        this.table = table;
    }

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.htc_confirm;

import com.gms.entity.form.htc.VisitBookTableForm;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ConfirmBookForm {
     
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
    // Dịch vụ
    private HashMap<String, String> services;
    // Danh sách ca dương tính
    private List<ConfirmBookTableForm> table;
    // ID của cơ sở hiện tại
    private Long siteID;
    // Site provider
    private String siteProvince;
    private String fullname;
    private String code;
    private String sourceID;
    private Long sourceSiteID;
    private String resultID;
    //Danh sách định nghĩa cần hiển thị
    private HashMap<String, HashMap<String, String>> options;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }
    
    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
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

    public List<ConfirmBookTableForm> getTable() {
        return table;
    }

    public void setTable(List<ConfirmBookTableForm> table) {
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

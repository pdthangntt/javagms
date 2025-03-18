package com.gms.entity.form.tt09;

import com.gms.entity.form.tt03.*;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class Phuluc01TT09Form {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String code;
    private int month;
    private int year;
    private HashMap<String, String> services;
    // Đối tượng
    private HashMap<String, String> objects;
    private String startDate;
    private String endDate;
    private List<TablePhuluc01Form> table;
    private String focusObjects;
    private String customerType;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    
    public String getFocusObjects() {
        return focusObjects;
    }

    public void setFocusObjects(String focusObjects) {
        this.focusObjects = focusObjects;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    

    public HashMap<String, String> getObjects() {
        return objects;
    }

    public void setObjects(HashMap<String, String> objects) {
        this.objects = objects;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public HashMap<String, String> getServices() {
        return services;
    }

    public void setServices(HashMap<String, String> services) {
        this.services = services;
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public List<TablePhuluc01Form> getTable() {
        return table;
    }

    public void setTable(List<TablePhuluc01Form> table) {
        this.table = table;
    }

}

package com.gms.entity.form.tt03;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class TT03Form {

    private String title;
    private String fileName;
    private String siteName;
    private String code;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private HashMap<String, String> services;
    // Đối tượng
    private HashMap<String, String> objects;
    private List<Table02Form> table02;
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

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
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

    public List<Table02Form> getTable02() {
        return table02;
    }

    public void setTable02(List<Table02Form> table02) {
        this.table02 = table02;
    }

}

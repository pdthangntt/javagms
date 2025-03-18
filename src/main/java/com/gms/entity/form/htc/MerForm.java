package com.gms.entity.form.htc;

import java.util.HashMap;
import java.util.Set;

/**
 * Form for view PDF, excel, and sending mail
 *
 * @author TrangBN
 */
public class MerForm {
    private Long siteID;
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String siteDistrict;
    private String siteWard;
    private String startDate;
    private String endDate;
    private Set<String> districtID;
    private HashMap<String, String> services;
    // Đối tượng
    private HashMap<String, String> objects;
    private HashMap<String, HashMap<String, MerTable01Form>> table01Forms;
    private HashMap<String, HashMap<String, MerTable02Form>> table02Forms;
    private HashMap<String, HashMap<String, MerTable03Form>> table03Forms;
    private HashMap<String, HashMap<String, MerTable04Form>> table04Forms;
    private HashMap<String, HashMap<String, MerTable05Form>> table05Forms;

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public HashMap<String, HashMap<String, MerTable03Form>> getTable03Forms() {
        return table03Forms;
    }

    public void setTable03Forms(HashMap<String, HashMap<String, MerTable03Form>> table03Forms) {
        this.table03Forms = table03Forms;
    }

    public HashMap<String, HashMap<String, MerTable01Form>> getTable01Forms() {
        return table01Forms;
    }

    public void setTable01Forms(HashMap<String, HashMap<String, MerTable01Form>> table01Forms) {
        this.table01Forms = table01Forms;
    }

    public HashMap<String, HashMap<String, MerTable02Form>> getTable02Forms() {
        return table02Forms;
    }

    public void setTable02Forms(HashMap<String, HashMap<String, MerTable02Form>> table02Forms) {
        this.table02Forms = table02Forms;
    }

    public HashMap<String, String> getObjects() {
        return objects;
    }

    public void setObjects(HashMap<String, String> objects) {
        this.objects = objects;
    }

    public String getSiteDistrict() {
        return siteDistrict;
    }

    public void setSiteDistrict(String siteDistrict) {
        this.siteDistrict = siteDistrict;
    }

    public String getSiteWard() {
        return siteWard;
    }

    public void setSiteWard(String siteWard) {
        this.siteWard = siteWard;
    }

    public Set<String> getDistrictID() {
        return districtID;
    }

    public void setDistrictID(Set<String> districtID) {
        this.districtID = districtID;
    }

    public HashMap<String, HashMap<String, MerTable04Form>> getTable04Forms() {
        return table04Forms;
    }

    public void setTable04Forms(HashMap<String, HashMap<String, MerTable04Form>> table04Forms) {
        this.table04Forms = table04Forms;
    }

    public HashMap<String, HashMap<String, MerTable05Form>> getTable05Forms() {
        return table05Forms;
    }

    public void setTable05Forms(HashMap<String, HashMap<String, MerTable05Form>> table05Forms) {
        this.table05Forms = table05Forms;
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

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
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

    public HashMap<String, String> getServices() {
        return services;
    }

    public void setServices(HashMap<String, String> services) {
        this.services = services;
    }

}

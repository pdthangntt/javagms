package com.gms.entity.form.htc;

import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.input.HtcElogSearch;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdthang
 */
public class HtcElogForm {

    // File information
    private String title;
    private String fileName;
    private String siteName;
    private String siteCode;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String advisoryeTime;
    private String permanentWardName;
    private String permanentDistrictName;
    private HashMap<String, String> wards;
    private HashMap<String, String> confirmSite;

    private HtcElogSearch search;
    private List<HtcVisitEntity> items;
    private HashMap<String, HashMap<String, String>> options;
    private HashMap<String, HashMap<String, String>> locations;

    public HashMap<String, String> getConfirmSite() {
        return confirmSite;
    }

    public void setConfirmSite(HashMap<String, String> confirmSite) {
        this.confirmSite = confirmSite;
    }
    
    public HashMap<String, String> getWards() {
        return wards;
    }

    public void setWards(HashMap<String, String> wards) {
        this.wards = wards;
    }
    
    public HashMap<String, HashMap<String, String>> getLocations() {
        return locations;
    }

    public void setLocations(HashMap<String, HashMap<String, String>> locations) {
        this.locations = locations;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }
    
    
    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
    
    

    public String getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(String advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
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


    public String getPermanentWardName() {
        return permanentWardName;
    }

    public void setPermanentWardName(String permanentWardName) {
        this.permanentWardName = permanentWardName;
    }

    public String getPermanentDistrictName() {
        return permanentDistrictName;
    }

    public void setPermanentDistrictName(String permanentDistrictName) {
        this.permanentDistrictName = permanentDistrictName;
    }

    public HtcElogSearch getSearch() {
        return search;
    }

    public void setSearch(HtcElogSearch search) {
        this.search = search;
    }

    public List<HtcVisitEntity> getItems() {
        return items;
    }

    public void setItems(List<HtcVisitEntity> items) {
        this.items = items;
    }

}

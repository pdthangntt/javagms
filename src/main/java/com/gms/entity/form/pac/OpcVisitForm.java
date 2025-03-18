package com.gms.entity.form.pac;

import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.input.OpcArvSearch;
import com.gms.entity.input.PacPatientAcceptSearch;
import com.gms.entity.input.PacPatientNewSearch;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class OpcVisitForm {

    // File information
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String confirmTime;
    private String permanentWardName;
    private String permanentDistrictName;
    private boolean isVAAC;

    private OpcArvSearch search;
    private HashMap<String, String> siteOptions;
    private HashMap<String, String> provinceNames;
    private HashMap<String, HashMap<String, String>> options;
    private List<OpcArvEntity> items;

    public int subtractDate(Date date) {

        if (date == null) {
            return 0;
        }
        Date currentDate = new Date();
        long diff = currentDate.getTime() - date.getTime();

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        return diffDays;
    }

    public boolean isIsVAAC() {
        return isVAAC;
    }

    public void setIsVAAC(boolean isVAAC) {
        this.isVAAC = isVAAC;
    }

    public HashMap<String, String> getProvinceNames() {
        return provinceNames;
    }

    public void setProvinceNames(HashMap<String, String> provinceNames) {
        this.provinceNames = provinceNames;
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

    public OpcArvSearch getSearch() {
        return search;
    }

    public void setSearch(OpcArvSearch search) {
        this.search = search;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
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

    public HashMap<String, String> getSiteOptions() {
        return siteOptions;
    }

    public void setSiteOptions(HashMap<String, String> siteOptions) {
        this.siteOptions = siteOptions;
    }

    public List<OpcArvEntity> getItems() {
        return items;
    }

    public void setItems(List<OpcArvEntity> items) {
        this.items = items;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public void setOptions(String options, HashMap<String, HashMap<String, String>> options0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

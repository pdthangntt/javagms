package com.gms.entity.form.pac;

import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.input.PacPatientReviewSearch;
import com.gms.entity.input.PacPatientNewSearch;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PacReviewForm {

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
    private String provinceName;
    private String districtName;
    private String wardName;
    private boolean isVAAC;

    private PacPatientReviewSearch search;
    private HashMap<String, String> siteOptions;
    private HashMap<String, String> provinceNames;
    private HashMap<String, HashMap<String, String>> options;
    private List<PacPatientInfoEntity> items;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public PacPatientReviewSearch getSearch() {
        return search;
    }

    public void setSearch(PacPatientReviewSearch search) {
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

    public List<PacPatientInfoEntity> getItems() {
        return items;
    }

    public void setItems(List<PacPatientInfoEntity> items) {
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

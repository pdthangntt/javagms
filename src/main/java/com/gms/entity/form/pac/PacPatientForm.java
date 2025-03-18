package com.gms.entity.form.pac;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.input.PacPatientNewSearch;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PacPatientForm {

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
    private String permanentProvinceName;
    private String hivCode;
    private String dateFilter;
    private String titleLocation;
    private String tab;
    private Long totalNew;

    private PacPatientNewSearch search;
    private HashMap<String, String> siteOptions;
    private HashMap<String, String> provinces;
    private HashMap<String, String> districts;
    private HashMap<String, String> wards;
    private HashMap<String, String> hivCodeOptions;
    private HashMap<String, String> wardHivInfoCodes;
    private HashMap<String, HashMap<String, String>> options;
    private HashMap<String, HashMap<String, String>> sOptions;
    private List<PacPatientInfoEntity> items;
    private DataPage<PacPatientInfoEntity> dataPage;

    public Long getTotalNew() {
        return totalNew;
    }

    public void setTotalNew(Long totalNew) {
        this.totalNew = totalNew;
    }

    public HashMap<String, String> getWardHivInfoCodes() {
        return wardHivInfoCodes;
    }

    public void setWardHivInfoCodes(HashMap<String, String> wardHivInfoCodes) {
        this.wardHivInfoCodes = wardHivInfoCodes;
    }

    public HashMap<String, HashMap<String, String>> getsOptions() {
        return sOptions;
    }

    public void setsOptions(HashMap<String, HashMap<String, String>> sOptions) {
        this.sOptions = sOptions;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getPermanentProvinceName() {
        return permanentProvinceName;
    }

    public void setPermanentProvinceName(String permanentProvinceName) {
        this.permanentProvinceName = permanentProvinceName;
    }

    public String getTitleLocation() {
        return titleLocation;
    }

    public void setTitleLocation(String titleLocation) {
        this.titleLocation = titleLocation;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    public HashMap<String, String> getProvinces() {
        return provinces;
    }

    public void setProvinces(HashMap<String, String> provinces) {
        this.provinces = provinces;
    }

    public HashMap<String, String> getDistricts() {
        return districts;
    }

    public void setDistricts(HashMap<String, String> districts) {
        this.districts = districts;
    }

    public HashMap<String, String> getWards() {
        return wards;
    }

    public void setWards(HashMap<String, String> wards) {
        this.wards = wards;
    }

    public DataPage<PacPatientInfoEntity> getDataPage() {
        return dataPage;
    }

    public void setDataPage(DataPage<PacPatientInfoEntity> dataPage) {
        this.dataPage = dataPage;
    }

    public HashMap<String, String> getHivCodeOptions() {
        return hivCodeOptions;
    }

    public void setHivCodeOptions(HashMap<String, String> hivCodeOptions) {
        this.hivCodeOptions = hivCodeOptions;
    }

    public String getHivCode() {
        return hivCode;
    }

    public void setHivCode(String hivCode) {
        this.hivCode = hivCode;
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

    public PacPatientNewSearch getSearch() {
        return search;
    }

    public void setSearch(PacPatientNewSearch search) {
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

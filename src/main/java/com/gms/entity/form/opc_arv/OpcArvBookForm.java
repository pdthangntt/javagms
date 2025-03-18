/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.opc_arv;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.input.OpcArvSearch;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author DSNAnh
 */
public class OpcArvBookForm {
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
    private String titleLocation;
    private String time;
    private Integer type;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String searchProvince;
    private String searchDistrict;
    private String searchWard;

    private OpcArvSearch search;
    private HashMap<String, String> siteOptions;
    private HashMap<String, HashMap<String, String>> options;
    private DataPage<OpcArvEntity> dataPage;
    private List<OpcArvEntity> items;
    private List<Integer> order;
    
    private Set<Long> siteID;
    private String treatmentTimeFrom;
    private String treatmentTimeTo;
    private boolean isOpcManager;

    public List<Integer> getOrder() {
        return order;
    }

    public void setOrder(List<Integer> order) {
        this.order = order;
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

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public String getTitleLocation() {
        return titleLocation;
    }

    public void setTitleLocation(String titleLocation) {
        this.titleLocation = titleLocation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getSearchProvince() {
        return searchProvince;
    }

    public void setSearchProvince(String searchProvince) {
        this.searchProvince = searchProvince;
    }

    public String getSearchDistrict() {
        return searchDistrict;
    }

    public void setSearchDistrict(String searchDistrict) {
        this.searchDistrict = searchDistrict;
    }

    public String getSearchWard() {
        return searchWard;
    }

    public void setSearchWard(String searchWard) {
        this.searchWard = searchWard;
    }

    public OpcArvSearch getSearch() {
        return search;
    }

    public void setSearch(OpcArvSearch search) {
        this.search = search;
    }

    public HashMap<String, String> getSiteOptions() {
        return siteOptions;
    }

    public void setSiteOptions(HashMap<String, String> siteOptions) {
        this.siteOptions = siteOptions;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public DataPage<OpcArvEntity> getDataPage() {
        return dataPage;
    }

    public void setDataPage(DataPage<OpcArvEntity> dataPage) {
        this.dataPage = dataPage;
    }

    public List<OpcArvEntity> getItems() {
        return items;
    }

    public void setItems(List<OpcArvEntity> items) {
        this.items = items;
    }

    public Set<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(Set<Long> siteID) {
        this.siteID = siteID;
    }

    public String getTreatmentTimeFrom() {
        return treatmentTimeFrom;
    }

    public void setTreatmentTimeFrom(String treatmentTimeFrom) {
        this.treatmentTimeFrom = treatmentTimeFrom;
    }

    public String getTreatmentTimeTo() {
        return treatmentTimeTo;
    }

    public void setTreatmentTimeTo(String treatmentTimeTo) {
        this.treatmentTimeTo = treatmentTimeTo;
    }

    public boolean isIsOpcManager() {
        return isOpcManager;
    }

    public void setIsOpcManager(boolean isOpcManager) {
        this.isOpcManager = isOpcManager;
    }
    
    
}

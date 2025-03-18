package com.gms.entity.form.opc_arv;

import com.gms.entity.db.PqmDrugEstimateDataEntity;
import com.gms.entity.db.PqmDrugNewDataEntity;
import com.gms.entity.db.PqmDrugNewEntity;
import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form BC pqm
 *
 * @author pdThang
 *
 */
public class PqmDrugNewForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String site;
    private String tab;
    private String siteSearch;
    private int month;
    private int year;
    private String donvi;
    private List<PqmDrugNewEntity> items;
    private List<PqmDrugNewDataEntity> itemAPI;
    private HashMap<String, String> keyMonth;
    private Map<Long, String> siteMaps;

    private HashMap<String, String> provincePQM;
    private HashMap<String, String> districtPQM;
    private HashMap<String, String> provinceNamePQM;
    private HashMap<String, String> districtNamePQM;
    private String provincePQMcode;
    private String districtPQMcode;
    private Map<String, String> sitePQMcodes;
    private String currentProvinceID;

    public Map<Long, String> getSiteMaps() {
        return siteMaps;
    }

    public void setSiteMaps(Map<Long, String> siteMaps) {
        this.siteMaps = siteMaps;
    }

    public HashMap<String, String> getProvincePQM() {
        return provincePQM;
    }

    public void setProvincePQM(HashMap<String, String> provincePQM) {
        this.provincePQM = provincePQM;
    }

    public HashMap<String, String> getDistrictPQM() {
        return districtPQM;
    }

    public void setDistrictPQM(HashMap<String, String> districtPQM) {
        this.districtPQM = districtPQM;
    }

    public HashMap<String, String> getProvinceNamePQM() {
        return provinceNamePQM;
    }

    public void setProvinceNamePQM(HashMap<String, String> provinceNamePQM) {
        this.provinceNamePQM = provinceNamePQM;
    }

    public HashMap<String, String> getDistrictNamePQM() {
        return districtNamePQM;
    }

    public void setDistrictNamePQM(HashMap<String, String> districtNamePQM) {
        this.districtNamePQM = districtNamePQM;
    }

    public String getProvincePQMcode() {
        return provincePQMcode;
    }

    public void setProvincePQMcode(String provincePQMcode) {
        this.provincePQMcode = provincePQMcode;
    }

    public String getDistrictPQMcode() {
        return districtPQMcode;
    }

    public void setDistrictPQMcode(String districtPQMcode) {
        this.districtPQMcode = districtPQMcode;
    }

    public Map<String, String> getSitePQMcodes() {
        return sitePQMcodes;
    }

    public void setSitePQMcodes(Map<String, String> sitePQMcodes) {
        this.sitePQMcodes = sitePQMcodes;
    }

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getSiteSearch() {
        return siteSearch;
    }

    public List<PqmDrugNewDataEntity> getItemAPI() {
        return itemAPI;
    }

    public void setItemAPI(List<PqmDrugNewDataEntity> itemAPI) {
        this.itemAPI = itemAPI;
    }

    public void setSiteSearch(String siteSearch) {
        this.siteSearch = siteSearch;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public HashMap<String, String> getKeyMonth() {
        return keyMonth;
    }

    public void setKeyMonth(HashMap<String, String> keyMonth) {
        this.keyMonth = keyMonth;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public List<PqmDrugNewEntity> getItems() {
        return items;
    }

    public void setItems(List<PqmDrugNewEntity> items) {
        this.items = items;
    }

}

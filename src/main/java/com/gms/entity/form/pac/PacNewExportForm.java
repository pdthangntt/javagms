package com.gms.entity.form.pac;

import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.input.PacPatientNewSearch;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author NamAnh_HaUI
 */
public class PacNewExportForm {

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
    private boolean isVAAC;

    private PacPatientNewSearch search;
    private HashMap<String, String> siteOptions;
    private HashMap<String, HashMap<String, String>> options;
    private List<PacPatientInfoEntity> items;
    private String code;
    private String name;
    private String blood;
    private String deathTime;
    private String addressFilter;
    private String ageFilter;
    private String dateFilter;
    private String testObject;
    private String race;
    private String gender;
    private String transmision;
    private String manageStatus;
    private String alive;
    private String treatment;
    private String resident;
    private HashMap<String, String> allProvincesName;

    public HashMap<String, String> getAllProvincesName() {
        return allProvincesName;
    }

    public void setAllProvincesName(HashMap<String, String> allProvincesName) {
        this.allProvincesName = allProvincesName;
    }

    public String getAgeFilter() {
        return ageFilter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public void setAgeFilter(String ageFilter) {
        this.ageFilter = ageFilter;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(String deathTime) {
        this.deathTime = deathTime;
    }

    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    public String getTestObject() {
        return testObject;
    }

    public void setTestObject(String testObject) {
        this.testObject = testObject;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }

    public String getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(String manageStatus) {
        this.manageStatus = manageStatus;
    }

    public String getSearchProvince() {
        return searchProvince;
    }

    public void setSearchProvince(String searchProvince) {
        this.searchProvince = searchProvince;
    }

    public boolean isIsVAAC() {
        return isVAAC;
    }

    public void setIsVAAC(boolean isVAAC) {
        this.isVAAC = isVAAC;
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

    public String getTitleLocation() {
        return titleLocation;
    }

    public void setTitleLocation(String titleLocation) {
        this.titleLocation = titleLocation;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public PacPatientNewSearch getSearch() {
        return search;
    }

    public void setSearch(PacPatientNewSearch search) {
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

    public List<PacPatientInfoEntity> getItems() {
        return items;
    }

    public void setItems(List<PacPatientInfoEntity> items) {
        this.items = items;
    }

}

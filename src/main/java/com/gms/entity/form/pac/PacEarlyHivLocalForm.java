package com.gms.entity.form.pac;

import java.util.List;

/**
 *
 * @author TrangBN
 */
public class PacEarlyHivLocalForm {

    private String title;
    private String fileName;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String siteAgency;
    private String siteName;
    private String siteProvince;
    private String staffName;

    private String province;
    private String district;
    private String ward;
    private boolean isTYT;
    private boolean serchWard;
    private int totalWard;
    private int totalMale;
    private int totalFemale;
    private int totalOther;

    private String startDate;
    private String endDate;
    
    //param hiện bên excel
    private String searchDistrict;
    private String searchWard;
    private String searchGender;
    private String searchAge;
    private String searchJob;
    private String searchObject;
    private String searchRisk;
    private String searchBloodBase;
    private String searchTreatment;
    private String searchResident;
    private String titleLocation;
    private String searchWardName;
    private String searchDistrictName;
    
    private List<TablePacEarlyHivForm> table;

    public String getSearchWardName() {
        return searchWardName;
    }

    public void setSearchWardName(String searchWardName) {
        this.searchWardName = searchWardName;
    }

    public String getSearchDistrictName() {
        return searchDistrictName;
    }

    public void setSearchDistrictName(String searchDistrictName) {
        this.searchDistrictName = searchDistrictName;
    }
    
    public String getTitleLocation() {
        return titleLocation;
    }

    public void setTitleLocation(String titleLocation) {
        this.titleLocation = titleLocation;
    }
    
    public int getTotalMale() {
        return totalMale;
    }

    public void setTotalMale(int totalMale) {
        this.totalMale = totalMale;
    }

    public int getTotalFemale() {
        return totalFemale;
    }

    public void setTotalFemale(int totalFemale) {
        this.totalFemale = totalFemale;
    }

    public int getTotalOther() {
        return totalOther;
    }

    public void setTotalOther(int totalOther) {
        this.totalOther = totalOther;
    }
    
    public int getTotalWard() {
        return totalWard;
    }

    public void setTotalWard(int totalWard) {
        this.totalWard = totalWard;
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

    public String getSearchGender() {
        return searchGender;
    }

    public void setSearchGender(String searchGender) {
        this.searchGender = searchGender;
    }

    public String getSearchAge() {
        return searchAge;
    }

    public void setSearchAge(String searchAge) {
        this.searchAge = searchAge;
    }

    public String getSearchJob() {
        return searchJob;
    }

    public void setSearchJob(String searchJob) {
        this.searchJob = searchJob;
    }

    public String getSearchObject() {
        return searchObject;
    }

    public void setSearchObject(String searchObject) {
        this.searchObject = searchObject;
    }

    public String getSearchRisk() {
        return searchRisk;
    }

    public void setSearchRisk(String searchRisk) {
        this.searchRisk = searchRisk;
    }

    public String getSearchBloodBase() {
        return searchBloodBase;
    }

    public void setSearchBloodBase(String searchBloodBase) {
        this.searchBloodBase = searchBloodBase;
    }

    public String getSearchTreatment() {
        return searchTreatment;
    }

    public void setSearchTreatment(String searchTreatment) {
        this.searchTreatment = searchTreatment;
    }

    public String getSearchResident() {
        return searchResident;
    }

    public void setSearchResident(String searchResident) {
        this.searchResident = searchResident;
    }

    public boolean isIsTYT() {
        return isTYT;
    }

    public void setIsTYT(boolean isTYT) {
        this.isTYT = isTYT;
    }

    public boolean isSerchWard() {
        return serchWard;
    }

    public void setSerchWard(boolean serchWard) {
        this.serchWard = serchWard;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
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

    public List<TablePacEarlyHivForm> getTable() {
        return table;
    }

    public void setTable(List<TablePacEarlyHivForm> table) {
        this.table = table;
    }
}

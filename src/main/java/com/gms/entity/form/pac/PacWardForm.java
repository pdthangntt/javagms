package com.gms.entity.form.pac;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Form báo cáo huyện xã
 *
 * @author TrangBN
 */
public class PacWardForm extends DetectHivForm {

    private boolean TYT;
    private boolean TTYT;
    private boolean PAC;
    private boolean print;

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String fromTime;
    private String toTime;
    private String searchWardName;
    private String searchDistrictName;
    private String titleLocation;
    private String titleLocationDisplay;
    private String levelDisplay;
    private String provinceName;
    private String manageStatus;

    //Thông tin quản lý
    private String siteProvinceID;
    private String siteDistrictID;
    private String siteWardID;

    // Thông tin tìm kiếm
    private String addressFilter;
    private String province;
    private String district;
    private String ward;
    private String month;
    private String year;
    private String genderID;
    private String objectID;

    private int years;

    private HashMap<String, HashMap<String, String>> options;
    private List<PacWardTableForm> table;
    private PacWardTableForm tableTotal;

    public String getManageStatus() {
        return manageStatus;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public void setManageStatus(String manageStatus) {
        this.manageStatus = manageStatus;
    }

    public PacWardTableForm getTableTotal() {
        return tableTotal;
    }

    public void setTableTotal(PacWardTableForm tableTotal) {
        this.tableTotal = tableTotal;
    }

    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public boolean isTYT() {
        return TYT;
    }

    public void setTYT(boolean TYT) {
        this.TYT = TYT;
    }

    public boolean isTTYT() {
        return TTYT;
    }

    public void setTTYT(boolean TTYT) {
        this.TTYT = TTYT;
    }

    public boolean isPAC() {
        return PAC;
    }

    public void setPAC(boolean PAC) {
        this.PAC = PAC;
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

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

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

    public String getTitleLocationDisplay() {
        return titleLocationDisplay;
    }

    public void setTitleLocationDisplay(String titleLocationDisplay) {
        this.titleLocationDisplay = titleLocationDisplay;
    }

    public String getLevelDisplay() {
        return levelDisplay;
    }

    public void setLevelDisplay(String levelDisplay) {
        this.levelDisplay = levelDisplay;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getSiteProvinceID() {
        return siteProvinceID;
    }

    public void setSiteProvinceID(String siteProvinceID) {
        this.siteProvinceID = siteProvinceID;
    }

    public String getSiteDistrictID() {
        return siteDistrictID;
    }

    public void setSiteDistrictID(String siteDistrictID) {
        this.siteDistrictID = siteDistrictID;
    }

    public String getSiteWardID() {
        return siteWardID;
    }

    public void setSiteWardID(String siteWardID) {
        this.siteWardID = siteWardID;
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

    public List<PacWardTableForm> getTable() {
        return table;
    }

    public void setTable(List<PacWardTableForm> table) {
        this.table = table;
    }

}

package com.gms.entity.form.pac;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Form báo cáo huyện xã
 *
 * @author TrangBN
 */
public class PacLocalForm extends DetectHivForm {

    private boolean TYT;
    private boolean TTYT;
    private boolean PAC;
    private boolean VAAC;

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

    //Thông tin quản lý
    private String siteProvinceID;
    private String siteDistrictID;
    private String siteWardID;

    // Thông tin tìm kiếm
    private String province;
    private String district;
    private String ward;

    private boolean defaultSearchPlace; // Mặc định đăng nhập hoặc không tìm kiếm tỉnh huyện xã
    private int sum;

    private int notReviewTotal;
    private int needReviewTotal;
    private int reviewedTotal;
    private int aliveTotal;
    private int deadTotal;
    private int provinceTotal;
    private int districtTotal;
    private int outProvinceTotal;
    private int wardTotal;

    private List<PacLocalTableForm> table;

    public int getOutProvinceTotal() {
        return outProvinceTotal;
    }

    public void setOutProvinceTotal(int outProvinceTotal) {
        this.outProvinceTotal = outProvinceTotal;
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

    public boolean isVAAC() {
        return VAAC;
    }

    public void setVAAC(boolean VAAC) {
        this.VAAC = VAAC;
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

    public List<PacLocalTableForm> getTable() {
        return table;
    }

    public void setTable(List<PacLocalTableForm> table) {
        this.table = table;
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

    public boolean isDefaultSearchPlace() {
        return defaultSearchPlace;
    }

    public void setDefaultSearchPlace(boolean defaultSearchPlace) {
        this.defaultSearchPlace = defaultSearchPlace;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public void setNotReviewTotal(int notReviewTotal) {
        this.notReviewTotal = notReviewTotal;
    }

    public void setNeedReviewTotal(int needReviewTotal) {
        this.needReviewTotal = needReviewTotal;
    }

    public void setReviewedTotal(int reviewedTotal) {
        this.reviewedTotal = reviewedTotal;
    }

    public int getNotReviewTotal() {
        return notReviewTotal;
    }

    public int getNeedReviewTotal() {
        return needReviewTotal;
    }

    public int getReviewedTotal() {
        return reviewedTotal;
    }

    public int getAliveTotal() {
        return aliveTotal;
    }

    public int getDeadTotal() {
        return deadTotal;
    }

    public int getProvinceTotal() {
        return provinceTotal;
    }

    public int getDistrictTotal() {
        return districtTotal;
    }

    public int getWardTotal() {
        return wardTotal;
    }

    public void setProvinceTotal(int provinceTotal) {
        this.provinceTotal = provinceTotal;
    }

    public void setDistrictTotal(int districtTotal) {
        this.districtTotal = districtTotal;
    }

    public void setWardTotal(int wardTotal) {
        this.wardTotal = wardTotal;
    }

    public void setAliveTotal(int aliveTotal) {
        this.aliveTotal = aliveTotal;
    }

    public void setDeadTotal(int deadTotal) {
        this.deadTotal = deadTotal;
    }

}

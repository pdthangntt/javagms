package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.util.HashMap;
import java.util.List;

/**
 * Form table bảng người nhiễm phát hiện theo nhóm tuổi
 * 
 * @author TrangBN
 */
public class DetectHivAgeForm extends DetectHivForm {

    private boolean TYT;
    private boolean TTYT;
    private boolean PAC;
    private boolean VAAC;
    
    private String title;
    private String fileName;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String siteAgency;
    private String siteName;
    private String siteProvince;
    private String staffName;
    private String titleLocation;
    private String age1;
    private String age2;
    private String age3;
    private String age4;
    private String age5;
    private String searchAge1;
    private String searchAge2;
    private String searchAge3;
    private String searchAge4;
    private String searchAge5;
    
    //Thông tin quản lý
    private String siteProvinceID;
    private String siteDistrictID;
    private String siteWardID;
    
    //Thông tin tìm kiếm
    private String province;
    private String dateFilter;
    private String district;
    private String ward;
    private String resident;
    private String object;
    private String treatment;
    private String job;
    private String race;
    private String gender;
    private String transmision;
    private String manageStatus;
    private String levelDisplay;
    private String addressFilter;
    private String confirmTimeFrom;
    private String confirmTimeTo;
    private String manageTimeFrom;
    private String manageTimeTo;
    private String inputTimeFrom;
    private String inputTimeTo;
    private String updateTimeFrom;
    private String updateTimeTo;
    private String patientStatus;
    private String titleLocationDisplay;
    private String placeTest;
    private String facility;
    private boolean defaultSearchPlace; // Mặc định đăng nhập hoặc không tìm kiếm tỉnh huyện xã
    private int sum;
    private String hasHealthInsurance;
    
    private HashMap<String, HashMap<String, String>> options;
    private List<TableDetectHivAgeForm> table;
    private String aidsFrom;
    private String aidsTo;

    public String getHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public void setHasHealthInsurance(String hasHealthInsurance) {
        this.hasHealthInsurance = hasHealthInsurance;
    }

    public String getUpdateTimeFrom() {
        return updateTimeFrom;
    }

    public void setUpdateTimeFrom(String updateTimeFrom) {
        this.updateTimeFrom = updateTimeFrom;
    }

    public String getUpdateTimeTo() {
        return updateTimeTo;
    }

    public void setUpdateTimeTo(String updateTimeTo) {
        this.updateTimeTo = updateTimeTo;
    }
    
    public String getAidsFrom() {
        return aidsFrom;
    }

    public void setAidsFrom(String aidsFrom) {
        this.aidsFrom = aidsFrom;
    }

    public String getAidsTo() {
        return aidsTo;
    }

    public void setAidsTo(String aidsTo) {
        this.aidsTo = aidsTo;
    }
    public int getAge1Total() {
        int total = 0;
        for (TableDetectHivAgeForm item : table) {
            total += item.getAge1();
        }
        return total;
    }

    public int getAge2Total() {
        int total = 0;
        for (TableDetectHivAgeForm item : table) {
            total += item.getAge2();
        }
        return total;
    }

    public int getAge3Total() {
        int total = 0;
        for (TableDetectHivAgeForm item : table) {
            total += item.getAge3();
        }
        return total;
    }

    public int getAge4Total() {
        int total = 0;
        for (TableDetectHivAgeForm item : table) {
            total += item.getAge4();
        }
        return total;
    }

    public int getAge5Total() {
        int total = 0;
        for (TableDetectHivAgeForm item : table) {
            total += item.getAge5();
        }
        return total;
    }

    public boolean isDefaultSearchPlace() {
        return defaultSearchPlace;
    }

    public void setDefaultSearchPlace(boolean defaultSearchPlace) {
        this.defaultSearchPlace = defaultSearchPlace;
    }
    
    public String getTitleLocationDisplay() {
        return titleLocationDisplay;
    }

    public void setTitleLocationDisplay(String titleLocationDisplay) {
        this.titleLocationDisplay = titleLocationDisplay;
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

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTitleLocation() {
        return titleLocation;
    }

    public void setTitleLocation(String titleLocation) {
        this.titleLocation = titleLocation;
    }

    public String getConfirmTimeFrom() {
        return confirmTimeFrom;
    }

    public void setConfirmTimeFrom(String confirmTimeFrom) {
        this.confirmTimeFrom = confirmTimeFrom;
    }

    public String getConfirmTimeTo() {
        return confirmTimeTo;
    }

    public void setConfirmTimeTo(String confirmTimeTo) {
        this.confirmTimeTo = confirmTimeTo;
    }

    public String getManageTimeFrom() {
        return manageTimeFrom;
    }

    public void setManageTimeFrom(String manageTimeFrom) {
        this.manageTimeFrom = manageTimeFrom;
    }

    public String getManageTimeTo() {
        return manageTimeTo;
    }

    public void setManageTimeTo(String manageTimeTo) {
        this.manageTimeTo = manageTimeTo;
    }

    public String getInputTimeFrom() {
        return inputTimeFrom;
    }

    public void setInputTimeFrom(String inputTimeFrom) {
        this.inputTimeFrom = inputTimeFrom;
    }

    public String getInputTimeTo() {
        return inputTimeTo;
    }

    public void setInputTimeTo(String inputTimeTo) {
        this.inputTimeTo = inputTimeTo;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
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

    public List<TableDetectHivAgeForm> getTable() {
        return table;
    }

    public void setTable(List<TableDetectHivAgeForm> table) {
        this.table = table;
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

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getLevelDisplay() {
        return levelDisplay;
    }

    public void setLevelDisplay(String levelDisplay) {
        this.levelDisplay = levelDisplay;
    }

    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
    
    public int getSum() {
        return sum;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    public String getAge1() {
        return age1;
    }

    public void setAge1(String age1) {
        this.age1 = age1;
    }

    public String getAge2() {
        return age2;
    }

    public void setAge2(String age2) {
        this.age2 = age2;
    }

    public String getAge3() {
        return age3;
    }

    public void setAge3(String age3) {
        this.age3 = age3;
    }

    public String getAge4() {
        return age4;
    }

    public void setAge4(String age4) {
        this.age4 = age4;
    }

    public String getAge5() {
        return age5;
    }

    public void setAge5(String age5) {
        this.age5 = age5;
    }

    public String getSearchAge2() {
        return searchAge2;
    }

    public void setSearchAge2(String searchAge2) {
        this.searchAge2 = searchAge2;
    }

    public String getSearchAge3() {
        return searchAge3;
    }

    public void setSearchAge3(String searchAge3) {
        this.searchAge3 = searchAge3;
    }

    public String getSearchAge4() {
        return searchAge4;
    }

    public void setSearchAge4(String searchAge4) {
        this.searchAge4 = searchAge4;
    }

    public String getSearchAge5() {
        return searchAge5;
    }

    public void setSearchAge5(String searchAge5) {
        this.searchAge5 = searchAge5;
    }

    public String getSearchAge1() {
        return searchAge1;
    }

    public void setSearchAge1(String searchAge1) {
        this.searchAge1 = searchAge1;
    }
    
    public String getAge1Percent() {
        return TextUtils.toPercent(this.getAge1Total(), this.sum);
    }
    
    public String getAge2Percent() {
        return TextUtils.toPercent(this.getAge2Total(), this.sum);
    }
    
    public String getAge3Percent() {
        return TextUtils.toPercent(this.getAge3Total(), this.sum);
    }
    
    public String getAge4Percent() {
        return TextUtils.toPercent(this.getAge4Total(), this.sum);
    }
    
    public String getAge5Percent() {
        return TextUtils.toPercent(this.getAge5Total(), this.sum);
    }

    public String getSumPercent() {
        return TextUtils.toPercent(this.sum, this.sum);
    }
    
    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public String getPlaceTest() {
        return placeTest;
    }

    public void setPlaceTest(String placeTest) {
        this.placeTest = placeTest;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }
}

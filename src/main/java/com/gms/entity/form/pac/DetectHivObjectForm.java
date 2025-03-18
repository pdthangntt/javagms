package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class DetectHivObjectForm extends DetectHivForm {
//những cái sử dụng

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

    private String confirmTimeFrom;
    private String confirmTimeTo;
    private String manageTimeFrom;
    private String manageTimeTo;
    private String inputTimeFrom;
    private String inputTimeTo;
    private String patientStatus;
    private String updateTimeFrom;
    private String updateTimeTo;

    //Thông tin quản lý
    private String siteProvinceID;
    private String siteDistrictID;
    private String siteWardID;

    //Thông tin tìm kiếm
    private String province;
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
    private HashMap<String, HashMap<String, String>> options;
    //tổng
    private int totalntmt;
    private int totalmd;
    private int totalmsm;
    private int totalvcbtbc;
    private int totalpnmt;
    private int totallao;
    private int totalkhac;
    private int totalkhongthongtin;
    private int totalhoalieu;
    private int totalphamnhan;
    private int totalmecon;
    private int totalnghiavu;
    private int total;
    private String titleLocation;

    private String wardID;//Cờ bỏ dữ liệu huyện khác
    private String districtID;//Cờ bỏ dữ liệu huyện khác
    private List<String> wards;//Cờ bỏ dữ liệu huyện khác
    private String aidsFrom;
    private String aidsTo;
    private String placeTest;
    private String facility;
    private String hasHealthInsurance;

    public int getTotalhoalieu() {
        return totalhoalieu;
    }

    public void setTotalhoalieu(int totalhoalieu) {
        this.totalhoalieu = totalhoalieu;
    }

    public int getTotalphamnhan() {
        return totalphamnhan;
    }

    public void setTotalphamnhan(int totalphamnhan) {
        this.totalphamnhan = totalphamnhan;
    }

    public int getTotalmecon() {
        return totalmecon;
    }

    public void setTotalmecon(int totalmecon) {
        this.totalmecon = totalmecon;
    }

    public int getTotalnghiavu() {
        return totalnghiavu;
    }

    public void setTotalnghiavu(int totalnghiavu) {
        this.totalnghiavu = totalnghiavu;
    }
    
    

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

    public List<String> getWards() {
        return wards;
    }

    public void setWards(List<String> wards) {
        this.wards = wards;
    }

    public boolean isWard(String ward1) {
        return this.getWards().contains(ward1);
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

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
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

    public String getTitleLocation() {
        return titleLocation;
    }

    public void setTitleLocation(String titleLocation) {
        this.titleLocation = titleLocation;
    }

    //table
    private List<TableDetectHivObjectForm> table;

    public String getSumTotal(int number, int total) {
        return TextUtils.toPercent(number, total);
    }

    public int getTotalntmt() {
        return totalntmt;
    }

    public void setTotalntmt(int totalntmt) {
        this.totalntmt = totalntmt;
    }

    public int getTotalmd() {
        return totalmd;
    }

    public void setTotalmd(int totalmd) {
        this.totalmd = totalmd;
    }

    public int getTotalmsm() {
        return totalmsm;
    }

    public void setTotalmsm(int totalmsm) {
        this.totalmsm = totalmsm;
    }

    public int getTotalvcbtbc() {
        return totalvcbtbc;
    }

    public void setTotalvcbtbc(int totalvcbtbc) {
        this.totalvcbtbc = totalvcbtbc;
    }

    public int getTotalpnmt() {
        return totalpnmt;
    }

    public void setTotalpnmt(int totalpnmt) {
        this.totalpnmt = totalpnmt;
    }

    public int getTotallao() {
        return totallao;
    }

    public void setTotallao(int totallao) {
        this.totallao = totallao;
    }

    public int getTotalkhac() {
        return totalkhac;
    }

    public void setTotalkhac(int totalkhac) {
        this.totalkhac = totalkhac;
    }

    public int getTotalkhongthongtin() {
        return totalkhongthongtin;
    }

    public void setTotalkhongthongtin(int totalkhongthongtin) {
        this.totalkhongthongtin = totalkhongthongtin;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public List<TableDetectHivObjectForm> getTable() {
        return table;
    }

    public void setTable(List<TableDetectHivObjectForm> table) {
        this.table = table;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

}

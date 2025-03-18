package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.util.HashMap;
import java.util.List;

/**
 * Form báo cáo Hiện trạng cư trú
 *
 * @author pdThang
 */
public class PacDetectHivResidentFrom extends DetectHivForm {

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
    private String titleLocation;

    private HashMap<String, HashMap<String, String>> options;
    private List<PacDetectHivResidentTableForm> table;

    private int matDau;
    private int dangODiaPhuong;
    private int khongCoThucTe;
    private int diTrai;
    private int chuyenDiNoiKhac;
    private int chuaXacDinhDuoc;
    private int diLamAnXa;
    private int chuyenTrongTinh;
    private int unclear;
    private int total;
    private String districtID;//Cờ bỏ dữ liệu huyện khác
    private List<String> wards;//Cờ bỏ dữ liệu huyện khác
    private String aidsFrom;
    private String aidsTo;
    private String placeTest;
    private String facility;
    private String hasHealthInsurance;

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

    public String getSumTotal(int number, int total) {
        return TextUtils.toPercent(number, total);
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public List<PacDetectHivResidentTableForm> getTable() {
        return table;
    }

    public void setTable(List<PacDetectHivResidentTableForm> table) {
        this.table = table;
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

    public String getTitleLocation() {
        return titleLocation;
    }

    public void setTitleLocation(String titleLocation) {
        this.titleLocation = titleLocation;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public int getMatDau() {
        return matDau;
    }

    public void setMatDau(int matDau) {
        this.matDau = matDau;
    }

    public int getDangODiaPhuong() {
        return dangODiaPhuong;
    }

    public void setDangODiaPhuong(int dangODiaPhuong) {
        this.dangODiaPhuong = dangODiaPhuong;
    }

    public int getKhongCoThucTe() {
        return khongCoThucTe;
    }

    public void setKhongCoThucTe(int khongCoThucTe) {
        this.khongCoThucTe = khongCoThucTe;
    }

    public int getDiTrai() {
        return diTrai;
    }

    public void setDiTrai(int diTrai) {
        this.diTrai = diTrai;
    }

    public int getChuyenDiNoiKhac() {
        return chuyenDiNoiKhac;
    }

    public void setChuyenDiNoiKhac(int chuyenDiNoiKhac) {
        this.chuyenDiNoiKhac = chuyenDiNoiKhac;
    }

    public int getChuaXacDinhDuoc() {
        return chuaXacDinhDuoc;
    }

    public void setChuaXacDinhDuoc(int chuaXacDinhDuoc) {
        this.chuaXacDinhDuoc = chuaXacDinhDuoc;
    }

    public int getDiLamAnXa() {
        return diLamAnXa;
    }

    public void setDiLamAnXa(int diLamAnXa) {
        this.diLamAnXa = diLamAnXa;
    }

    public int getChuyenTrongTinh() {
        return chuyenTrongTinh;
    }

    public void setChuyenTrongTinh(int chuyenTrongTinh) {
        this.chuyenTrongTinh = chuyenTrongTinh;
    }

    public int getUnclear() {
        return unclear;
    }

    public void setUnclear(int unclear) {
        this.unclear = unclear;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

}

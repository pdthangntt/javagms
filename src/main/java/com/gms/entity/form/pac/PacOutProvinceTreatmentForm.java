package com.gms.entity.form.pac;

import com.gms.entity.form.pac.PacOutProvinceTreatmentTable;
import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceTreatmentForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String tab;
    HashMap<String, HashMap<String, String>> options;

    //điều kiện tìm kiếm
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

    private String aidsFrom;
    private String aidsTo;
    private String placeTest;
    private String facility;
    private Map<String, HashMap<String, PacOutProvinceTreatmentTable>> items;
    private PacOutProvinceTreatmentTable table;

    public PacOutProvinceTreatmentTable getTotal(Map<String, HashMap<String, PacOutProvinceTreatmentTable>> item1, String key) {
        PacOutProvinceTreatmentTable model = new PacOutProvinceTreatmentTable();

        for (Map.Entry<String, HashMap<String, PacOutProvinceTreatmentTable>> entry : item1.entrySet()) {
            String key1 = entry.getKey();
            HashMap<String, PacOutProvinceTreatmentTable> value = entry.getValue();

            model.setDangdieutri(model.getDangdieutri() + value.get(key).getDangdieutri());
            model.setChuadieutri(model.getChuadieutri() + value.get(key).getChuadieutri());
            model.setChodieutri(model.getChodieutri() + value.get(key).getChodieutri());
            model.setBodieutri(model.getBodieutri() + value.get(key).getBodieutri());
            model.setMatdau(model.getMatdau() + value.get(key).getMatdau());
            model.setTuvong(model.getTuvong() + value.get(key).getTuvong());
            model.setChuacothongtin(model.getChuacothongtin() + value.get(key).getChuacothongtin());
            model.setKhongthongtin(model.getKhongthongtin() + value.get(key).getKhongthongtin());
        }
        return model;
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

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
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

    public Map<String, HashMap<String, PacOutProvinceTreatmentTable>> getItems() {
        return items;
    }

    public void setItems(Map<String, HashMap<String, PacOutProvinceTreatmentTable>> items) {
        this.items = items;
    }

    public PacOutProvinceTreatmentTable getTable() {
        return table;
    }

    public void setTable(PacOutProvinceTreatmentTable table) {
        this.table = table;
    }

}

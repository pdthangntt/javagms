package com.gms.entity.input;

/**
 *
 * @author vvThành
 */
public class PacOutProvinceSearch {

    //Phân trang
    private int pageIndex;
    private int pageSize;
    private String tab;

    private String provinceID;
    private String permanentProvinceID;
    private String currentProvinceID;
    private String detectProvinceID;
    private String fullname;
    private String yearOfBirth;
    private String genderID;
    private String identityCard;
    private String confirmTimeFrom;
    private String confirmTimeTo;
    private String bloodBaseID;
    private String sourceServiceID;
    private String siteConfirmID;
    private String siteTreatmentFacilityID;
    private String status;

    private String statusPatient;
    private String statusManage;
    private boolean remove;
    private boolean index;

    private String testObject;
    private String transmision;
    private String statusTreatment;
    private String statusResident;
    private Long hivID;

    public Long getHivID() {
        return hivID;
    }

    public void setHivID(Long hivID) {
        this.hivID = hivID;
    }

    public String getTestObject() {
        return testObject;
    }

    public void setTestObject(String testObject) {
        this.testObject = testObject;
    }

    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }

    public String getStatusTreatment() {
        return statusTreatment;
    }

    public void setStatusTreatment(String statusTreatment) {
        this.statusTreatment = statusTreatment;
    }

    public String getStatusResident() {
        return statusResident;
    }

    public void setStatusResident(String statusResident) {
        this.statusResident = statusResident;
    }

    public String getStatusPatient() {
        return statusPatient;
    }

    public void setStatusPatient(String statusPatient) {
        this.statusPatient = statusPatient;
    }

    public String getStatusManage() {
        return statusManage;
    }

    public void setStatusManage(String statusManage) {
        this.statusManage = statusManage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIndex() {
        return index;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getDetectProvinceID() {
        return detectProvinceID;
    }

    public void setDetectProvinceID(String detectProvinceID) {
        this.detectProvinceID = detectProvinceID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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

    public String getBloodBaseID() {
        return bloodBaseID;
    }

    public void setBloodBaseID(String bloodBaseID) {
        this.bloodBaseID = bloodBaseID;
    }

    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
    }

    public String getSiteConfirmID() {
        return siteConfirmID;
    }

    public void setSiteConfirmID(String siteConfirmID) {
        this.siteConfirmID = siteConfirmID;
    }

    public String getSiteTreatmentFacilityID() {
        return siteTreatmentFacilityID;
    }

    public void setSiteTreatmentFacilityID(String siteTreatmentFacilityID) {
        this.siteTreatmentFacilityID = siteTreatmentFacilityID;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}

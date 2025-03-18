package com.gms.entity.input;

import org.springframework.data.domain.Sort;

/**
 *
 * @author pdThang
 */
public class PacPatientAcceptSearch {

    private Long ID;
    private String fullname;
    private Integer yearOfBirth;
    private String genderID;
    private String confirmTimeFrom; //Ngày xét nghiệm từ
    private String confirmTimeTo; //Ngày xét nghiệm đến
    private boolean remove;
    private String currentPermanentProvinceID;//id tỉnh của cơ sở hiện tại//

    private String permanentProvinceID;// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
    private String permanentDistrictID;// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
    private String permanentWardID;// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
    private Sort sortable;
    private String statusOfResidentID; // hiện trạng cư trú

    private String provinceID; //tỉnh quản lý
    private String districtID; //tỉnh quản lý
    private String wardID; //tỉnh quản lý
    private String detectProvinceID;
    private String searchProvinceID;//tìm kiếm tỉnh khi phát hiện ngoại tỉnh
    private String searchDetectProvinceID;//tìm kiếm tỉnh khác phát hiện

    private int detectProvince;
    private int permanentProvince;
    private int dead; // kiểm tra trạng thái tử vong
    private String acceptPermanentTime;
    private String siteConfirmID;
    private String siteTreatmentFacilityID;

    //DSNAnh
    private String sourceSiteID;
    private String identityCard;
    private String healthInsuranceNo;
    private String startTreatmentTime;
    private String endTreatmentTime;
    private String opcStatus;
    private String connectStatus;
    private String addressFilter;

    //Rà soát lại
    private Integer hasReview;

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

    public Integer getHasReview() {
        return hasReview;
    }

    public void setHasReview(Integer hasReview) {
        this.hasReview = hasReview;
    }

    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
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

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public String getStatusOfResidentID() {
        return statusOfResidentID;
    }

    public void setStatusOfResidentID(String statusOfResidentID) {
        this.statusOfResidentID = statusOfResidentID;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getSearchDetectProvinceID() {
        return searchDetectProvinceID;
    }

    public void setSearchDetectProvinceID(String searchDetectProvinceID) {
        this.searchDetectProvinceID = searchDetectProvinceID;
    }

    public int getDetectProvince() {
        return detectProvince;
    }

    public void setDetectProvince(int detectProvince) {
        this.detectProvince = detectProvince;
    }

    public String getSearchProvinceID() {
        return searchProvinceID;
    }

    public void setSearchProvinceID(String searchProvinceID) {
        this.searchProvinceID = searchProvinceID;
    }

    public String getAcceptPermanentTime() {
        return acceptPermanentTime;
    }

    public void setAcceptPermanentTime(String acceptPermanentTime) {
        this.acceptPermanentTime = acceptPermanentTime;
    }

    public int getPermanentProvince() {
        return permanentProvince;
    }

    public void setPermanentProvince(int permanentProvince) {
        this.permanentProvince = permanentProvince;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDetectProvinceID() {
        return detectProvinceID;
    }

    public void setDetectProvinceID(String detectProvinceID) {
        this.detectProvinceID = detectProvinceID;
    }

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }

    //Phân trang
    private int pageIndex;
    private int pageSize;

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
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

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public String getCurrentPermanentProvinceID() {
        return currentPermanentProvinceID;
    }

    public void setCurrentPermanentProvinceID(String currentPermanentProvinceID) {
        this.currentPermanentProvinceID = currentPermanentProvinceID;
    }

    public String getPermanentDistrictID() {
        return permanentDistrictID;
    }

    public void setPermanentDistrictID(String permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public String getPermanentWardID() {
        return permanentWardID;
    }

    public void setPermanentWardID(String permanentWardID) {
        this.permanentWardID = permanentWardID;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getHealthInsuranceNo() {
        return healthInsuranceNo;
    }

    public void setHealthInsuranceNo(String healthInsuranceNo) {
        this.healthInsuranceNo = healthInsuranceNo;
    }

    public String getStartTreatmentTime() {
        return startTreatmentTime;
    }

    public void setStartTreatmentTime(String startTreatmentTime) {
        this.startTreatmentTime = startTreatmentTime;
    }

    public String getEndTreatmentTime() {
        return endTreatmentTime;
    }

    public void setEndTreatmentTime(String endTreatmentTime) {
        this.endTreatmentTime = endTreatmentTime;
    }

    public String getOpcStatus() {
        return opcStatus;
    }

    public void setOpcStatus(String opcStatus) {
        this.opcStatus = opcStatus;
    }
}

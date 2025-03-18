package com.gms.entity.input;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Sort;

/**
 *
 * @author pdThang
 */
public class PacPatientNewSearch {

    private Long ID;
    private Long hivID;
    private String fullname;
    private Integer yearOfBirth;
    private String genderID;
    private String raceID;
    private String modeOfTransmissionID;
    private String objectGroupID;
    private String confirmTimeFrom; //Ngày xét nghiệm từ
    private String confirmTimeTo; //Ngày xét nghiệm đến
    private String aidsFrom; //Ngày xét nghiệm từ
    private String aidsTo; //Ngày xét nghiệm đến
    private String requestDeathTimeFrom; //Ngày xét nghiệm từ
    private String requestDeathTimeTo; //Ngày xét nghiệm đến
    private String inputTimeFrom; //Ngày xét nghiệm từ
    private String inputTimeTo; //Ngày xét nghiệm đến
    private String from; //Ngày từ
    private String to; //Ngày đến
    private String reviewProvinceTimeFrom; //Ngày xét nghiệm từ
    private String reviewProvinceTimeTo; //Ngày xét nghiệm đến
    private boolean remove;
    private String currentPermanentProvinceID;//id tỉnh của cơ sở hiện tại//
    private String ageFrom;
    private String ageTo;
    private String ageSearchFrom;
    private String ageSearchTo;
    private String ageFromParam;
    private String ageToParam;
    private String currentAgeFrom;
    private String currentAgeTo;

    private String permanentProvinceID;// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
    private String permanentDistrictID;// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
    private String permanentWardID;// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
    private Sort sortable;
    private String statusOfResidentID; // hiện trạng cư trú
    private List<String> statusOfResidentIDs; // hiện trạng cư trú
    private String statusOfTreatmentID;

    private String siteConfirmID;
    private String siteTreatmentFacilityID;

    private String provinceID; //tỉnh quản lý
    private String detectProvinceID;
    private String searchProvinceID;//tìm kiếm tỉnh khi phát hiện ngoại tỉnh
    private String searchDetectProvinceID;//tìm kiếm tỉnh khác phát hiện

    private int detectProvince;
    private int permanentProvince;
    private int dead; // kiểm tra trạng thái tử vong
    private int tabProvi; // kiểm tra tab
    private String acceptPermanentTime;
    private String service;
    private String bloodBase;
    private String detectProvi;
    private String ageFilter;
    private String tab;
    private String status;

    //DSNAnh
    private String sourceSiteID;
    private String identityCard;
    private String healthInsuranceNo;
    private String startTreatmentTime;
    private String endTreatmentTime;
    private String opcStatus;
    private String connectStatus;
    private String earlyHiv;
    private String provinceIDt;

    private String addressFilter;
    private String patientStatus;
    private String dateFilter;
    private boolean isVAAC;
    private boolean isPAC;

    private Integer hasRequest;
    private Integer hasReview;
    private Integer other;

    private String deathTimeFrom;
    private String deathTimeTo;
    private String manageTimeFrom;
    private String manageTimeTo;
    private String updateTimeFrom;
    private String updateTimeTo;
    private String age;

    private String opcCode;
    private String code;
    private String name;
    private String blood;
    private String confirmCode;

    private List<String> pIDs;
    private List<String> dIDs;
    private List<String> wIDs;
    private List<String> genderIDs;
    private List<String> raceIDs;
    private List<String> testObjectIDs;
    private List<String> transmisionIDs;
    private List<String> aliveIDs;
    private List<String> residentIDs;
    private List<String> treamentIDs;

    private String manageStatus;
    private String patientID;

    private String districtID; // Huyển quản lý
    private String wardID; // Xã quản lý

    private String placeTest; // Cơ sở xét nghiệm khẳng định
    private String facility; // Nới điều trị

    private String sortName;
    private String sortType;
    private String statusTreatmentRule;

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public List<String> getStatusOfResidentIDs() {
        return statusOfResidentIDs;
    }

    public void setStatusOfResidentIDs(List<String> statusOfResidentIDs) {
//        if(statusOfResidentIDs.size() > 1){
//            statusOfResidentIDs.remove(0);
//        }
//        statusOfResidentIDs =Arrays.asList("1", "2");
        this.statusOfResidentIDs = statusOfResidentIDs;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Long getHivID() {
        return hivID;
    }

    public void setHivID(Long hivID) {
        this.hivID = hivID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTreatmentRule() {
        return statusTreatmentRule;
    }

    public void setStatusTreatmentRule(String statusTreatmentRule) {
        this.statusTreatmentRule = statusTreatmentRule;
    }

    public String getOpcCode() {
        return opcCode;
    }

    public void setOpcCode(String opcCode) {
        this.opcCode = opcCode;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getRequestDeathTimeFrom() {
        return requestDeathTimeFrom;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getAgeFromParam() {
        return ageFromParam;
    }

    public void setAgeFromParam(String ageFromParam) {
        this.ageFromParam = ageFromParam;
    }

    public String getAgeToParam() {
        return ageToParam;
    }

    public void setAgeToParam(String ageToParam) {
        this.ageToParam = ageToParam;
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

    public void setRequestDeathTimeFrom(String requestDeathTimeFrom) {
        this.requestDeathTimeFrom = requestDeathTimeFrom;
    }

    public String getRequestDeathTimeTo() {
        return requestDeathTimeTo;
    }

    public void setRequestDeathTimeTo(String requestDeathTimeTo) {
        this.requestDeathTimeTo = requestDeathTimeTo;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
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

    public Integer getHasReview() {
        return hasReview;
    }

    public void setHasReview(Integer hasReview) {
        this.hasReview = hasReview;
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

    public boolean isIsPAC() {
        return isPAC;
    }

    public void setIsPAC(boolean isPAC) {
        this.isPAC = isPAC;
    }

    public String getAgeSearchFrom() {
        return ageSearchFrom;
    }

    public void setAgeSearchFrom(String ageSearchFrom) {
        this.ageSearchFrom = ageSearchFrom;
    }

    public String getAgeSearchTo() {
        return ageSearchTo;
    }

    public void setAgeSearchTo(String ageSearchTo) {
        this.ageSearchTo = ageSearchTo;
    }

    public String getAgeFilter() {
        return ageFilter;
    }

    public void setAgeFilter(String ageFilter) {
        this.ageFilter = ageFilter;
    }

    public String getCurrentAgeFrom() {
        return currentAgeFrom;
    }

    public void setCurrentAgeFrom(String currentAgeFrom) {
        this.currentAgeFrom = currentAgeFrom;
    }

    public String getCurrentAgeTo() {
        return currentAgeTo;
    }

    public void setCurrentAgeTo(String currentAgeTo) {
        this.currentAgeTo = currentAgeTo;
    }

    public List<String> getAliveIDs() {
        return aliveIDs;
    }

    public void setAliveIDs(List<String> aliveIDs) {
        this.aliveIDs = aliveIDs;
    }

    public List<String> getResidentIDs() {
        return residentIDs;
    }

    public void setResidentIDs(List<String> residentIDs) {
        this.residentIDs = residentIDs;
    }

    public List<String> getTreamentIDs() {
        return treamentIDs;
    }

    public void setTreamentIDs(List<String> treamentIDs) {
        this.treamentIDs = treamentIDs;
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

    public String getDeathTimeFrom() {
        return deathTimeFrom;
    }

    public void setDeathTimeFrom(String deathTimeFrom) {
        this.deathTimeFrom = deathTimeFrom;
    }

    public String getDeathTimeTo() {
        return deathTimeTo;
    }

    public void setDeathTimeTo(String deathTimeTo) {
        this.deathTimeTo = deathTimeTo;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<String> getpIDs() {
        return pIDs;
    }

    public void setpIDs(List<String> pIDs) {
        this.pIDs = pIDs;
    }

    public List<String> getdIDs() {
        return dIDs;
    }

    public void setdIDs(List<String> dIDs) {
        this.dIDs = dIDs;
    }

    public List<String> getwIDs() {
        return wIDs;
    }

    public void setwIDs(List<String> wIDs) {
        this.wIDs = wIDs;
    }

    public List<String> getGenderIDs() {
        return genderIDs;
    }

    public void setGenderIDs(List<String> genderIDs) {
        this.genderIDs = genderIDs;
    }

    public List<String> getRaceIDs() {
        return raceIDs;
    }

    public void setRaceIDs(List<String> raceIDs) {
        this.raceIDs = raceIDs;
    }

    public List<String> getTestObjectIDs() {
        return testObjectIDs;
    }

    public void setTestObjectIDs(List<String> testObjectIDs) {
        this.testObjectIDs = testObjectIDs;
    }

    public List<String> getTransmisionIDs() {
        return transmisionIDs;
    }

    public void setTransmisionIDs(List<String> transmisionIDs) {
        this.transmisionIDs = transmisionIDs;
    }

    public String getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(String manageStatus) {
        this.manageStatus = manageStatus;
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

    public boolean isIsVAAC() {
        return isVAAC;
    }

    public void setIsVAAC(boolean isVAAC) {
        this.isVAAC = isVAAC;
    }

    public String getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(String ageFrom) {
        this.ageFrom = ageFrom;
    }

    public String getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(String ageTo) {
        this.ageTo = ageTo;
    }

    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getModeOfTransmissionID() {
        return modeOfTransmissionID;
    }

    public void setModeOfTransmissionID(String modeOfTransmissionID) {
        this.modeOfTransmissionID = modeOfTransmissionID;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getBloodBase() {
        return bloodBase;
    }

    public void setBloodBase(String bloodBase) {
        this.bloodBase = bloodBase;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReviewProvinceTimeFrom() {
        return reviewProvinceTimeFrom;
    }

    public void setReviewProvinceTimeFrom(String reviewProvinceTimeFrom) {
        this.reviewProvinceTimeFrom = reviewProvinceTimeFrom;
    }

    public String getReviewProvinceTimeTo() {
        return reviewProvinceTimeTo;
    }

    public void setReviewProvinceTimeTo(String reviewProvinceTimeTo) {
        this.reviewProvinceTimeTo = reviewProvinceTimeTo;
    }

    public String getProvinceIDt() {
        return provinceIDt;
    }

    public void setProvinceIDt(String provinceIDt) {
        this.provinceIDt = provinceIDt;
    }

    public Integer getHasRequest() {
        return hasRequest;
    }

    public void setHasRequest(Integer hasRequest) {
        this.hasRequest = hasRequest;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public int getTabProvi() {
        return tabProvi;
    }

    public void setTabProvi(int tabProvi) {
        this.tabProvi = tabProvi;
    }

    public String getDetectProvi() {
        return detectProvi;
    }

    public void setDetectProvi(String detectProvi) {
        this.detectProvi = detectProvi;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
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

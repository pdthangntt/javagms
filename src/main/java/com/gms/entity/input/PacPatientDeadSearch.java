package com.gms.entity.input;

import java.util.List;
import org.springframework.data.domain.Sort;

/**
 *
 * @author NamAnh_HaUI
 */
public class PacPatientDeadSearch {

    private boolean remove;
    private String deathTimeFrom;
    private String deathTimeTo;
    private String updateTimeFrom;
    private String updateTimeTo;
    private String inputTimeFrom;
    private String inputTimeTo;
    private String confirmTimeFrom;
    private String confirmTimeTo;
    private String requestDeathTimeFrom;
    private String requestDeathTimeTo;
    private String addressFilter;
    private String dateFilter;
    private String age;
    private String ageFrom;
    private String ageTo;

    private List<String> pIDs;
    private List<String> dIDs;
    private List<String> wIDs;
    private List<String> genderIDs;
    private List<String> raceIDs;
    private List<String> testObjectIDs;
    private List<String> transmisionIDs;

    private Sort sortable;
    private int pageIndex;
    private int pageSize;
    private boolean isVAAC;
    private boolean isTTYT;
    private boolean isTYT;
    private String manageStatus;

    private String provinceID; // Tỉnh quản lý
    private String districtID; // Huyển quản lý
    private String wardID; // Xã quản lý
    private String ageFromParam;
    private String ageToParam;

    private String code;
    private String name;
    private String blood;
    private String placeTest; // Cơ sở xét nghiệm khẳng định
    private String facility; // Nới điều trị

    public String getRequestDeathTimeFrom() {
        return requestDeathTimeFrom;
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

    public void setUpdateTimeTo(String updateTimeTo) {
        this.updateTimeTo = updateTimeTo;
    }

    public String getUpdateTimeTo() {
        return updateTimeTo;
    }

    public String getUpdateTimeFrom() {
        return updateTimeFrom;
    }

    public void setUpdateTimeFrom(String updateTimeFrom) {
        this.updateTimeFrom = updateTimeFrom;
    }

    public boolean isIsTTYT() {
        return isTTYT;
    }

    public void setIsTTYT(boolean isTTYT) {
        this.isTTYT = isTTYT;
    }

    public boolean isIsTYT() {
        return isTYT;
    }

    public void setIsTYT(boolean isTYT) {
        this.isTYT = isTYT;
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

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
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

    public String getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(String manageStatus) {
        this.manageStatus = manageStatus;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
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

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
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

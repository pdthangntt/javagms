package com.gms.entity.form;

import com.gms.components.TextUtils;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.HtcLaytestAgencyEntity;
import com.gms.entity.db.HtcLaytestEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author TrangBN
 */
public class HtcLaytestForm {
    
    private Long ID;
    private Long siteID;
    private String code;
    private String serviceID;
    private String patientName;
    private String patientPhone;
    private String patientID;
    private String raceID;
    private String genderID;
    private String yearOfBirth;
    private String permanentAddress;
    private String permanentAddressGroup;
    private String permanentAddressStreet;
    private String permanentProvinceID;
    private String permanentDistrictID;
    private String permanentWardID;
    private String currentAddress; 
    private String currentAddressStreet;
    private String currentAddressGroup;
    private String currentProvinceID;
    private String currentDistrictID;
    private String currentWardID;
    private String jobID;
    private String objectGroupID; 
    private List<String> riskBehaviorID;
    private String testResultsID; 
    private String advisoryeTime;
    private String isAgreeTest;
    private String siteVisitID;
    private Boolean remove;
    private String sampleSentDate; 
    private String acceptDate; 
    private String hasTestBefore;  
    private String mostRecentTest; 
    private Boolean isDisplayCopy;
    private String pageRedirect;
    
    private String referralSource; //Nguồn giới thiệu
    private String youInjectCode; //Mã số bạn tình, bạn chích
    private String bioName; // Tên sinh phẩm
    private String confirmTime; // Ngày XN khẳng định
    private String confirmResultsID; //Kết quả xét nghiệm khẳng định
    private String siteConfirmTest; // Cơ sở xét nghiệm khẳng định
    private String exchangeTime; // Ngày chuyển gửi điềut trị
    private String arrivalSite; // Tên cơ sở điều trị khách hàng được chuyển đến
    private String exchangeStatus; // Kết quả chuyển gửi
    private String controlLine; // Vạch chứng
    private String testLine; // Vạch kết quả
    private String registeredTherapySite; // Tên cơ sở mà khách hành đã đăng ký điều trị
    private String registerTherapyTime; // Ngày đăng ký điều trị
    private String note; // Ghi chú
    private List<HtcLaytestAgencyEntity> laytestAgencies; // Danh sách người được giới thiệu

    public String getPermanentAddressGroup() {
        return permanentAddressGroup;
    }

    public void setPermanentAddressGroup(String permanentAddressGroup) {
        this.permanentAddressGroup = permanentAddressGroup;
    }

    public String getPermanentAddressStreet() {
        return permanentAddressStreet;
    }

    public void setPermanentAddressStreet(String permanentAddressStreet) {
        this.permanentAddressStreet = permanentAddressStreet;
    }

    public String getCurrentAddressStreet() {
        return currentAddressStreet;
    }

    public void setCurrentAddressStreet(String currentAddressStreet) {
        this.currentAddressStreet = currentAddressStreet;
    }

    public String getCurrentAddressGroup() {
        return currentAddressGroup;
    }

    public void setCurrentAddressGroup(String currentAddressGroup) {
        this.currentAddressGroup = currentAddressGroup;
    }

    public List<HtcLaytestAgencyEntity> getLaytestAgencies() {
        return laytestAgencies;
    }

    public void setLaytestAgencies(List<HtcLaytestAgencyEntity> laytestAgencies) {
        this.laytestAgencies = laytestAgencies;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTestLine() {
        return testLine;
    }

    public void setTestLine(String testLine) {
        this.testLine = testLine;
    }
    
    public String getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(String referralSource) {
        this.referralSource = referralSource;
    }

    public String getYouInjectCode() {
        return youInjectCode;
    }

    public void setYouInjectCode(String youInjectCode) {
        this.youInjectCode = youInjectCode;
    }

    public String getBioName() {
        return bioName;
    }

    public void setBioName(String bioName) {
        this.bioName = bioName;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public String getSiteConfirmTest() {
        return siteConfirmTest;
    }

    public void setSiteConfirmTest(String siteConfirmTest) {
        this.siteConfirmTest = siteConfirmTest;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public String getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(String exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    public String getControlLine() {
        return controlLine;
    }

    public void setControlLine(String controlLine) {
        this.controlLine = controlLine;
    }

    public String getRegisteredTherapySite() {
        return registeredTherapySite;
    }

    public void setRegisteredTherapySite(String registeredTherapySite) {
        this.registeredTherapySite = registeredTherapySite;
    }

    public String getRegisterTherapyTime() {
        return registerTherapyTime;
    }

    public void setRegisterTherapyTime(String registerTherapyTime) {
        this.registerTherapyTime = registerTherapyTime;
    }
    
    public String getPageRedirect() {
        return pageRedirect;
    }

    public void setPageRedirect(String pageRedirect) {
        this.pageRedirect = pageRedirect;
    }
    
    public Boolean getIsDisplayCopy() {
        return isDisplayCopy;
    }

    public void setIsDisplayCopy(Boolean isDisplayCopy) {
        this.isDisplayCopy = isDisplayCopy;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getCode() {
        return StringUtils.isNotEmpty(code) ? code.trim() : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
    
    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
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

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getCurrentDistrictID() {
        return currentDistrictID;
    }

    public void setCurrentDistrictID(String currentDistrictID) {
        this.currentDistrictID = currentDistrictID;
    }

    public String getCurrentWardID() {
        return currentWardID;
    }

    public void setCurrentWardID(String currentWardID) {
        this.currentWardID = currentWardID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public List<String> getRiskBehaviorID() {
        return riskBehaviorID;
    }

    public void setRiskBehaviorID(List<String> riskBehaviorID) {
        this.riskBehaviorID = riskBehaviorID;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(String advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }

    public String getSiteVisitID() {
        return siteVisitID;
    }

    public void setSiteVisitID(String siteVisitID) {
        this.siteVisitID = siteVisitID;
    }

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public String getMostRecentTest() {
        return mostRecentTest;
    }

    public void setMostRecentTest(String mostRecentTest) {
        this.mostRecentTest = mostRecentTest;
    }

    public String getSampleSentDate() {
        return sampleSentDate;
    }

    public void setSampleSentDate(String sampleSentDate) {
        this.sampleSentDate = sampleSentDate;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getIsAgreeTest() {
        return isAgreeTest;
    }

    public void setIsAgreeTest(String isAgreeTest) {
        this.isAgreeTest = isAgreeTest;
    }

    public String getHasTestBefore() {
        return hasTestBefore;
    }

    public void setHasTestBefore(String hasTestBefore) {
        this.hasTestBefore = hasTestBefore;
    }
    
    /**
     * Lấy dữ liệu từ entity hiển thị ra form
     * 
     * @param entity 
     */
    public void setHtcLaytetsForm(HtcLaytestEntity entity){
        
        String formatDate = "dd/MM/yyyy";
        setID(entity.getID());
        setSiteID(entity.getSiteID());
        setSiteID(entity.getSiteID());
        setAdvisoryeTime(entity.getAdvisoryeTime()!= null ? TextUtils.formatDate(entity.getAdvisoryeTime(), formatDate): null);
        setSampleSentDate(entity.getSampleSentDate()!= null ? TextUtils.formatDate(entity.getSampleSentDate(), formatDate): null);
        setAcceptDate(entity.getAcceptDate() != null ? TextUtils.formatDate(entity.getAcceptDate(), formatDate): null);
        setConfirmTime(entity.getConfirmTime()!= null ? TextUtils.formatDate(entity.getConfirmTime(), formatDate): null);
        setExchangeTime(entity.getExchangeTime()!= null ? TextUtils.formatDate(entity.getExchangeTime(), formatDate): null);
        setRegisterTherapyTime(entity.getRegisterTherapyTime()!= null ? TextUtils.formatDate(entity.getRegisterTherapyTime(), formatDate): null);
        setServiceID(entity.getServiceID());
        setCode(entity.getCode());
        setPatientName(entity.getPatientName());
        setPatientPhone(entity.getPatientPhone());
        setPatientID(entity.getPatientID());
        setHasTestBefore(entity.getHasTestBefore() == null ? "" : entity.getHasTestBefore()? String.valueOf(BooleanEnum.TRUE.getKey()): String.valueOf(BooleanEnum.FALSE.getKey()) );
        setMostRecentTest(entity.getMostRecentTest());
        setRaceID(entity.getRaceID());
        setGenderID(entity.getGenderID());
        setYearOfBirth(entity.getYearOfBirth() != null ? String.valueOf(entity.getYearOfBirth()): "");
        setPermanentAddress(entity.getPermanentAddress());
        setPermanentAddressGroup(entity.getPermanentAddressGroup());
        setPermanentAddressStreet(entity.getPermanentAddressStreet());
        setPermanentProvinceID(entity.getPermanentProvinceID());
        setPermanentDistrictID(entity.getPermanentDistrictID());
        setPermanentWardID(entity.getPermanentWardID());
        setCurrentAddress(entity.getCurrentAddress());
        setCurrentAddressGroup(entity.getCurrentAddressGroup());
        setCurrentAddressStreet(entity.getCurrentAddressStreet());
        setCurrentProvinceID(entity.getCurrentProvinceID());
        setCurrentDistrictID(entity.getCurrentDistrictID());
        setCurrentWardID(entity.getCurrentWardID());
        setJobID(entity.getJobID());
        setObjectGroupID(entity.getObjectGroupID());
        setRiskBehaviorID(entity.getRiskBehaviorID());
        setTestResultsID(entity.getTestResultsID());
        if (entity.getIsAgreeTest() != null) {
            setIsAgreeTest(entity.getIsAgreeTest().toString());
        }
        setSiteVisitID(entity.getSiteVisitID());
        setBioName(entity.getBioName());
        setControlLine(entity.getControlLine());
        setTestLine(entity.getTestLine());
        setArrivalSite(entity.getArrivalSite());
        setConfirmResultsID(entity.getConfirmResultsID());
        setSiteConfirmTest(entity.getSiteConfirmTest());
        setExchangeStatus(entity.getExchangeStatus());
        setRegisteredTherapySite(entity.getRegisteredTherapySite());
        setReferralSource(entity.getReferralSource());
        setYouInjectCode(entity.getYouInjectCode());
        setNote(entity.getNote());
    }
    
    /**
     * Chuyển đổi từ form sang entity
     * thực hiện thêm mới cho Xét nghiệm không chuyên
     * 
     * @param siteID
     * @param laytest
     * @return
     * @throws ParseException 
     */
    public HtcLaytestEntity getHtcLayTestEntity(Long siteID, HtcLaytestEntity laytest) throws ParseException {

        String formatDate = "dd/MM/yyyy";
        
        if (laytest != null) {
            laytest.setServiceID(getServiceID());
            laytest.setPatientName(getPatientName());
            laytest.setPatientPhone(getPatientPhone());
            laytest.setHasTestBefore(StringUtils.isEmpty(getHasTestBefore())? null : String.valueOf(BooleanEnum.TRUE.getKey()).equals(getHasTestBefore()));
            laytest.setMostRecentTest(String.valueOf(BooleanEnum.TRUE.getKey()).equals(getHasTestBefore()) ? getMostRecentTest() : null);
            laytest.setPatientID(getPatientID());
            laytest.setRaceID(getRaceID());
            laytest.setGenderID(getGenderID());
            laytest.setYearOfBirth(StringUtils.isNotEmpty(getYearOfBirth()) ? Integer.valueOf(getYearOfBirth()) : null);
            laytest.setPermanentAddress(getPermanentAddress());
            laytest.setPermanentAddressGroup(permanentAddressGroup);
            laytest.setPermanentAddressStreet(permanentAddressStreet);
            laytest.setPermanentProvinceID(getPermanentProvinceID());
            laytest.setPermanentDistrictID(getPermanentDistrictID());
            laytest.setPermanentWardID(getPermanentWardID());
            laytest.setTestLine(getTestLine());
            laytest.setControlLine(getControlLine());

            if (getIsDisplayCopy() == null || getIsDisplayCopy()) {
                laytest.setCurrentAddress(laytest.getPermanentAddress());
                laytest.setCurrentAddressGroup(laytest.getPermanentAddressGroup());
                laytest.setCurrentAddressStreet(laytest.getPermanentAddressStreet());
                laytest.setCurrentProvinceID(laytest.getPermanentProvinceID());
                laytest.setCurrentDistrictID(laytest.getPermanentDistrictID());
                laytest.setCurrentWardID(getPermanentWardID());
            } else {
                laytest.setCurrentAddress(getCurrentAddress());
                laytest.setCurrentAddressGroup(getCurrentAddressGroup());
                laytest.setCurrentAddressStreet(getCurrentAddressStreet());
                laytest.setCurrentProvinceID(getCurrentProvinceID());
                laytest.setCurrentDistrictID(getCurrentDistrictID());
                laytest.setCurrentWardID(getCurrentWardID());
            }

            if (getRiskBehaviorID() != null) {
                laytest.setRiskBehaviorID(getRiskBehaviorID());
            } else {
                laytest.setRiskBehaviorID(null);
            }

            laytest.setJobID(getJobID());
            laytest.setRemove(false);
            laytest.setObjectGroupID(getObjectGroupID());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
            laytest.setSampleSentDate(StringUtils.isNotEmpty(getSampleSentDate()) ? simpleDateFormat.parse(getSampleSentDate()) : null);
            laytest.setAcceptDate(StringUtils.isNotEmpty(getAcceptDate()) ? simpleDateFormat.parse(getAcceptDate()) : null);
            laytest.setAdvisoryeTime(StringUtils.isNotEmpty(getAdvisoryeTime()) ? simpleDateFormat.parse(getAdvisoryeTime()) : null);
            laytest.setConfirmTime(StringUtils.isNotEmpty(getConfirmTime()) ? simpleDateFormat.parse(getConfirmTime()) : null);
            laytest.setExchangeTime(StringUtils.isNotEmpty(getExchangeTime()) ? simpleDateFormat.parse(getExchangeTime()) : null);
            laytest.setConfirmResultsID(getConfirmResultsID());
            laytest.setSiteConfirmTest(getSiteConfirmTest());
            laytest.setTestResultsID(getTestResultsID());
            laytest.setIsAgreeTest("true".equals(getIsAgreeTest()));
            laytest.setSiteVisitID(getSiteVisitID());
            laytest.setArrivalSite(getArrivalSite());
            laytest.setExchangeStatus(getExchangeStatus());
            laytest.setRegisterTherapyTime(StringUtils.isNotEmpty(getRegisterTherapyTime()) ? simpleDateFormat.parse(getRegisterTherapyTime()) : null);
            laytest.setRegisteredTherapySite(getRegisteredTherapySite());
            laytest.setNote(getNote());
            laytest.setBioName(getBioName());
            laytest.setReferralSource(getReferralSource());
            laytest.setYouInjectCode(getYouInjectCode());

            if (!TestResultEnum.CO_PHAN_UNG.getKey().equals(laytest.getTestResultsID())) {
                laytest.setIsAgreeTest(null);
                laytest.setSiteVisitID(null);
            }

            if (laytest.getIsAgreeTest() != null && !laytest.getIsAgreeTest()) {
                laytest.setSiteVisitID(null);
            }
            return laytest;
        }
        
        HtcLaytestEntity htcLaytestEntity = new HtcLaytestEntity();
        
        htcLaytestEntity.setID(getID());
        htcLaytestEntity.setServiceID(getServiceID());
        htcLaytestEntity.setSiteID(siteID);
        htcLaytestEntity.setCode(getCode());
        htcLaytestEntity.setPatientName(getPatientName());
        htcLaytestEntity.setPatientPhone(getPatientPhone());
        htcLaytestEntity.setHasTestBefore(StringUtils.isEmpty(getHasTestBefore())? null : String.valueOf(BooleanEnum.TRUE.getKey()).equals(getHasTestBefore()));
        htcLaytestEntity.setMostRecentTest(String.valueOf(BooleanEnum.TRUE.getKey()).equals(getHasTestBefore())?getMostRecentTest() : null);
        htcLaytestEntity.setPatientID(getPatientID());
        htcLaytestEntity.setRaceID(getRaceID());
        htcLaytestEntity.setGenderID(getGenderID());
        htcLaytestEntity.setYearOfBirth(StringUtils.isNotEmpty(getYearOfBirth()) ? Integer.valueOf(getYearOfBirth()): null);
        htcLaytestEntity.setPermanentAddress(getPermanentAddress());
        htcLaytestEntity.setPermanentAddressGroup(getPermanentAddressGroup());
        htcLaytestEntity.setPermanentAddressStreet(getPermanentAddressStreet());
        htcLaytestEntity.setPermanentProvinceID(getPermanentProvinceID());
        htcLaytestEntity.setPermanentDistrictID(getPermanentDistrictID());
        htcLaytestEntity.setPermanentWardID(getPermanentWardID());
        htcLaytestEntity.setTestLine(getTestLine());
        htcLaytestEntity.setControlLine(getControlLine());
            
        if (getIsDisplayCopy() == null || getIsDisplayCopy()) {
            htcLaytestEntity.setCurrentAddress(htcLaytestEntity.getPermanentAddress());
            htcLaytestEntity.setCurrentAddressGroup(htcLaytestEntity.getPermanentAddressGroup());
            htcLaytestEntity.setCurrentAddressStreet(htcLaytestEntity.getPermanentAddressStreet());
            htcLaytestEntity.setCurrentProvinceID(htcLaytestEntity.getPermanentProvinceID());
            htcLaytestEntity.setCurrentDistrictID(htcLaytestEntity.getPermanentDistrictID());
            htcLaytestEntity.setCurrentWardID(getPermanentWardID());
        } else {
            htcLaytestEntity.setCurrentAddress(getCurrentAddress());
            htcLaytestEntity.setCurrentAddressGroup(getCurrentAddressGroup());
            htcLaytestEntity.setCurrentAddressStreet(getCurrentAddressStreet());
            htcLaytestEntity.setCurrentProvinceID(getCurrentProvinceID());
            htcLaytestEntity.setCurrentDistrictID(getCurrentDistrictID());
            htcLaytestEntity.setCurrentWardID(getCurrentWardID());
        }
        
        if (getRiskBehaviorID() != null) {
            htcLaytestEntity.setRiskBehaviorID(getRiskBehaviorID());
        } else {
            htcLaytestEntity.setRiskBehaviorID(null);
        }
        
        htcLaytestEntity.setJobID(getJobID());
        htcLaytestEntity.setRemove(false);
        htcLaytestEntity.setObjectGroupID(getObjectGroupID());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
        htcLaytestEntity.setSampleSentDate(StringUtils.isNotEmpty(getSampleSentDate())? simpleDateFormat.parse(getSampleSentDate()): null);
        htcLaytestEntity.setAcceptDate(StringUtils.isNotEmpty(getAcceptDate())? simpleDateFormat.parse(getAcceptDate()): null);
        htcLaytestEntity.setAdvisoryeTime(StringUtils.isNotEmpty(getAdvisoryeTime()) ? simpleDateFormat.parse(getAdvisoryeTime()) :null);
        htcLaytestEntity.setConfirmTime(StringUtils.isNotEmpty(getConfirmTime()) ? simpleDateFormat.parse(getConfirmTime()) : null);
        htcLaytestEntity.setExchangeTime(StringUtils.isNotEmpty(getExchangeTime()) ? simpleDateFormat.parse(getExchangeTime()) : null);
        htcLaytestEntity.setConfirmResultsID(getConfirmResultsID());
        htcLaytestEntity.setSiteConfirmTest(getSiteConfirmTest());
        htcLaytestEntity.setTestResultsID(getTestResultsID());
        htcLaytestEntity.setIsAgreeTest( "true".equals(getIsAgreeTest()));
        htcLaytestEntity.setSiteVisitID(getSiteVisitID());
        htcLaytestEntity.setArrivalSite(getArrivalSite());
        htcLaytestEntity.setExchangeStatus(getExchangeStatus());
        htcLaytestEntity.setRegisterTherapyTime(StringUtils.isNotEmpty(getRegisterTherapyTime()) ? simpleDateFormat.parse(getRegisterTherapyTime()) : null);
        htcLaytestEntity.setRegisteredTherapySite(getRegisteredTherapySite());
        htcLaytestEntity.setNote(getNote());
        htcLaytestEntity.setBioName(getBioName());
        htcLaytestEntity.setReferralSource(getReferralSource());
        htcLaytestEntity.setYouInjectCode(getYouInjectCode());
        
        if(!TestResultEnum.CO_PHAN_UNG.getKey().equals(htcLaytestEntity.getTestResultsID()) ){
            htcLaytestEntity.setIsAgreeTest( null);
            htcLaytestEntity.setSiteVisitID(null);
        }
        
        if( htcLaytestEntity.getIsAgreeTest() != null && !htcLaytestEntity.getIsAgreeTest()){
            htcLaytestEntity.setSiteVisitID(null);
        }
        return htcLaytestEntity;
    }
}

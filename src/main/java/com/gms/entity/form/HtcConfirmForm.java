package com.gms.entity.form;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcConfirmEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author TrangBN
 */
public class HtcConfirmForm {

    private Long ID;
    private Long siteID;

    private String fullname;
    private String year;
    private String genderID;
    private String wardID;
    private String districtID;
    private String provinceID;
    private String address;
    private String permanentAddressStreet;
    private String permanentAddressGroup;
    private String objectGroupID;
    private String code;
    private String resultsReturnTime;
    private String confirmTime;
    private String sampleReceiveTime;

    private String testStaffID;
    private String bloodSample;
    private String resultsID;
    private String sourceServiceID; // Dịch vụ nơi gửi mẫu
    private String sourceID;
    private Boolean remove;
    private Long sourceSiteID;
    private String acceptDate;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private String addressFull;
    private String patientID;
    private String sampleQuality;
    private String bioName1;
    private String bioNameResult1;
    private String bioName2;
    private String bioNameResult2;
    private String bioName3;
    private String bioNameResult3;

    private String sourceReceiveSampleTime;
    private String siteName;
    private String testResultsID;
    private String sourceSampleDate;
    private String otherTechnical;
    private String earlyHiv;
    private String virusLoad;
    private String pacSentDate;   // Trạng thái chuyển gửi GSPH dựa trên ngày này
    private String pacPatientID;  // Id của bênh nhân bên quản lý người nhiễm
    private String sampleSaveCode; // Mã số lưu mẫu trường hợp dương tính ( không bắt buộc)
    private String earlyHivDate; // Ngày xét nghiệm nhiễm mới HIV
    private String virusLoadDate; // Ngày làm xét nghiệm tải lượng virus
    private String firstBioDate; // Ngày làm sinh phẩm 1
    private String secondBioDate; // Ngày làm sinh phẩm 2
    private String thirdBioDate; // Ngày làm sinh phẩm 3
    private String sampleSentSource; // Nơi gửi bệnh phẩm
    private String serviceID;

    // Bổ sung ngày 12-02-2020 yêu cẩu thêm
    private String currentAddress; //Địa chỉ đang cư trú
    private String currentAddressStreet;
    private String currentAddressGroup;
    private String currentProvinceID; // Mã tỉnh đang cư trú
    private String currentDistrictID; // Mã huyện đang cư trú
    private String currentWardID; // Mã phường/ xã đang cư trú
    private String modeOfTransmission; // Đường lây nhiễm
    private String insurance; //Có thẻ BHYT ?
    private String insuranceNo; //Số thẻ BHYT
    private Boolean isDisplayCopy;

    private boolean disabledEarlyHiv; // Đánh dầu disable nhiễm mới
    private boolean disabledVirusLoad; // Đánh dầu disable trường TLVR

    private String virusLoadNumber; //Kết quả xét nghiệm TLVR - nhập số

    private String earlyBioName; // Tên sinh phẩm nhiễm mới

    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    public String getVirusLoadNumber() {
        return virusLoadNumber;
    }

    public void setVirusLoadNumber(String virusLoadNumber) {
        this.virusLoadNumber = virusLoadNumber;
    }

    public String getEarlyBioName() {
        return earlyBioName;
    }

    public void setEarlyBioName(String earlyBioName) {
        this.earlyBioName = earlyBioName;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public boolean isDisabledEarlyHiv() {
        return disabledEarlyHiv;
    }

    public void setDisabledEarlyHiv(boolean disabledEarlyHiv) {
        this.disabledEarlyHiv = disabledEarlyHiv;
    }

    public boolean isDisabledVirusLoad() {
        return disabledVirusLoad;
    }

    public void setDisabledVirusLoad(boolean disabledVirusLoad) {
        this.disabledVirusLoad = disabledVirusLoad;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getPermanentAddressStreet() {
        return permanentAddressStreet;
    }

    public void setPermanentAddressStreet(String permanentAddressStreet) {
        this.permanentAddressStreet = permanentAddressStreet;
    }

    public String getPermanentAddressGroup() {
        return permanentAddressGroup;
    }

    public void setPermanentAddressGroup(String permanentAddressGroup) {
        this.permanentAddressGroup = permanentAddressGroup;
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

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getSampleSentSource() {
        return sampleSentSource;
    }

    public void setSampleSentSource(String sampleSentSource) {
        this.sampleSentSource = sampleSentSource;
    }

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getVirusLoadDate() {
        return virusLoadDate;
    }

    public void setVirusLoadDate(String virusLoadDate) {
        this.virusLoadDate = virusLoadDate;
    }

    public String getFirstBioDate() {
        return firstBioDate;
    }

    public void setFirstBioDate(String firstBioDate) {
        this.firstBioDate = firstBioDate;
    }

    public String getSecondBioDate() {
        return secondBioDate;
    }

    public void setSecondBioDate(String secondBioDate) {
        this.secondBioDate = secondBioDate;
    }

    public String getThirdBioDate() {
        return thirdBioDate;
    }

    public void setThirdBioDate(String thirdBioDate) {
        this.thirdBioDate = thirdBioDate;
    }

    public String getSampleSaveCode() {
        return sampleSaveCode;
    }

    public void setSampleSaveCode(String sampleSaveCode) {
        this.sampleSaveCode = sampleSaveCode;
    }

    public String getPacSentDate() {
        return pacSentDate;
    }

    public void setPacSentDate(String pacSentDate) {
        this.pacSentDate = pacSentDate;
    }

    public String getPacPatientID() {
        return pacPatientID;
    }

    public void setPacPatientID(String pacPatientID) {
        this.pacPatientID = pacPatientID;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getCode() {
        return StringUtils.isNotEmpty(code) ? code.trim() : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResultsReturnTime() {
        return resultsReturnTime;
    }

    public void setResultsReturnTime(String resultsReturnTime) {
        this.resultsReturnTime = resultsReturnTime;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getSampleReceiveTime() {
        return sampleReceiveTime;
    }

    public void setSampleReceiveTime(String sampleReceiveTime) {
        this.sampleReceiveTime = sampleReceiveTime;
    }

    public String getTestStaffID() {
        return testStaffID;
    }

    public void setTestStaffID(String testStaffID) {
        this.testStaffID = testStaffID;
    }

    public String getBloodSample() {
        return bloodSample;
    }

    public void setBloodSample(String bloodSample) {
        this.bloodSample = bloodSample;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    public String getSourceID() {
        return StringUtils.isNotEmpty(sourceID) ? sourceID.trim() : sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getSampleQuality() {
        return sampleQuality;
    }

    public void setSampleQuality(String sampleQuality) {
        this.sampleQuality = sampleQuality;
    }

    public String getBioName1() {
        return bioName1;
    }

    public void setBioName1(String bioName1) {
        this.bioName1 = bioName1;
    }

    public String getBioNameResult1() {
        return bioNameResult1;
    }

    public void setBioNameResult1(String bioNameResult1) {
        this.bioNameResult1 = bioNameResult1;
    }

    public String getBioName2() {
        return bioName2;
    }

    public void setBioName2(String bioName2) {
        this.bioName2 = bioName2;
    }

    public String getBioNameResult2() {
        return bioNameResult2;
    }

    public void setBioNameResult2(String bioNameResult2) {
        this.bioNameResult2 = bioNameResult2;
    }

    public String getBioName3() {
        return bioName3;
    }

    public void setBioName3(String bioName3) {
        this.bioName3 = bioName3;
    }

    public String getBioNameResult3() {
        return bioNameResult3;
    }

    public void setBioNameResult3(String bioNameResult3) {
        this.bioNameResult3 = bioNameResult3;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getOtherTechnical() {
        return otherTechnical;
    }

    public void setOtherTechnical(String otherTechnical) {
        this.otherTechnical = otherTechnical;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getSourceReceiveSampleTime() {
        return sourceReceiveSampleTime;
    }

    public void setSourceReceiveSampleTime(String sourceReceiveSampleTime) {
        this.sourceReceiveSampleTime = sourceReceiveSampleTime;
    }

    public String getSourceSampleDate() {
        return sourceSampleDate;
    }

    public void setSourceSampleDate(String sourceSampleDate) {
        this.sourceSampleDate = sourceSampleDate;
    }

    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
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

    public String getModeOfTransmission() {
        return modeOfTransmission;
    }

    public void setModeOfTransmission(String modeOfTransmission) {
        this.modeOfTransmission = modeOfTransmission;
    }

    public Boolean getIsDisplayCopy() {
        return isDisplayCopy;
    }

    public void setIsDisplayCopy(Boolean isDisplayCopy) {
        this.isDisplayCopy = isDisplayCopy;
    }

    /**
     * Chuyển dữ liệu từ entity sang form
     *
     * @param htcConfirmEntity
     * @author pdthang
     */
    public void setFormConfirm(HtcConfirmEntity htcConfirmEntity) {

        String formatDate = "dd/MM/yyyy";
        if (htcConfirmEntity == null) {
            htcConfirmEntity = new HtcConfirmEntity();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
        setID(htcConfirmEntity.getID());
        setSiteID(htcConfirmEntity.getSiteID());
        setCode(htcConfirmEntity.getCode());
        setFullname(htcConfirmEntity.getFullname());
        setGenderID(htcConfirmEntity.getGenderID());
        setYear(htcConfirmEntity.getYear() == null ? "" : String.valueOf(htcConfirmEntity.getYear()));
        setPatientID(htcConfirmEntity.getPatientID());
        setObjectGroupID(htcConfirmEntity.getObjectGroupID());
        setAddress(htcConfirmEntity.getAddress());
        setPermanentAddressGroup(htcConfirmEntity.getPermanentAddressGroup());
        setPermanentAddressStreet(htcConfirmEntity.getPermanentAddressStreet());
        setProvinceID(htcConfirmEntity.getProvinceID());
        setDistrictID(htcConfirmEntity.getDistrictID());
        setWardID(htcConfirmEntity.getWardID());
        setSourceID(htcConfirmEntity.getSourceID());
        setSourceSiteID(htcConfirmEntity.getSourceSiteID());
        setTestResultsID(htcConfirmEntity.getTestResultsID());
        setSampleQuality(htcConfirmEntity.getSampleQuality());
        setBioName1(htcConfirmEntity.getBioName1());
        setBioName2(htcConfirmEntity.getBioName2());
        setBioName3(htcConfirmEntity.getBioName3());
        setBioNameResult1(htcConfirmEntity.getBioNameResult1());
        setBioNameResult2(htcConfirmEntity.getBioNameResult2());
        setBioNameResult3(htcConfirmEntity.getBioNameResult3());
        setOtherTechnical(htcConfirmEntity.getOtherTechnical());
        setResultsID(htcConfirmEntity.getResultsID());
        setSourceServiceID(htcConfirmEntity.getSourceServiceID());
        setRemove(false);
        setEarlyHiv(htcConfirmEntity.getEarlyHiv());
        setVirusLoad(htcConfirmEntity.getVirusLoad());
        setSampleSaveCode(htcConfirmEntity.getSampleSaveCode());
        if (htcConfirmEntity.getSourceReceiveSampleTime() != null) {
            setSourceReceiveSampleTime(simpleDateFormat.format(htcConfirmEntity.getSourceReceiveSampleTime()));
        }
        if (htcConfirmEntity.getSourceSampleDate() != null) {
            setSourceSampleDate(simpleDateFormat.format(htcConfirmEntity.getSourceSampleDate()));
        }
        if (htcConfirmEntity.getSampleReceiveTime() != null) {
            setSampleReceiveTime(simpleDateFormat.format(htcConfirmEntity.getSampleReceiveTime()));
        }
        if (htcConfirmEntity.getAcceptDate() != null) {
            setAcceptDate(simpleDateFormat.format(htcConfirmEntity.getAcceptDate()));
        }
        if (htcConfirmEntity.getConfirmTime() != null) {
            setConfirmTime(simpleDateFormat.format(htcConfirmEntity.getConfirmTime()));
        }
        if (htcConfirmEntity.getResultsReturnTime() != null) {
            setResultsReturnTime(simpleDateFormat.format(htcConfirmEntity.getResultsReturnTime()));
        }
        setTestStaffID(htcConfirmEntity.getTestStaffId());
        setPacSentDate(htcConfirmEntity.getPacSentDate() != null ? TextUtils.formatDate(htcConfirmEntity.getPacSentDate(), formatDate) : null);
        setPacPatientID(htcConfirmEntity.getPacPatientID() != null ? htcConfirmEntity.getPacPatientID().toString() : null);
        setFirstBioDate(htcConfirmEntity.getFirstBioDate() != null ? TextUtils.formatDate(htcConfirmEntity.getFirstBioDate(), formatDate) : null);
        setSecondBioDate(htcConfirmEntity.getSecondBioDate() != null ? TextUtils.formatDate(htcConfirmEntity.getSecondBioDate(), formatDate) : null);
        setThirdBioDate(htcConfirmEntity.getThirdBioDate() != null ? TextUtils.formatDate(htcConfirmEntity.getThirdBioDate(), formatDate) : null);
        setEarlyHivDate(htcConfirmEntity.getEarlyHivDate() != null ? TextUtils.formatDate(htcConfirmEntity.getEarlyHivDate(), formatDate) : null);
        setVirusLoadDate(htcConfirmEntity.getVirusLoadDate() != null ? TextUtils.formatDate(htcConfirmEntity.getVirusLoadDate(), formatDate) : null);
        setSampleSentSource(StringUtils.isEmpty(htcConfirmEntity.getSampleSentSource()) ? StringUtils.EMPTY : htcConfirmEntity.getSampleSentSource());
        setServiceID(htcConfirmEntity.getServiceID());
        setCurrentAddress(htcConfirmEntity.getCurrentAddress());
        setCurrentAddressGroup(htcConfirmEntity.getCurrentAddressGroup());
        setCurrentAddressStreet(htcConfirmEntity.getCurrentAddressStreet());
        setCurrentProvinceID(htcConfirmEntity.getCurrentProvinceID());
        setCurrentDistrictID(htcConfirmEntity.getCurrentDistrictID());
        setCurrentWardID(htcConfirmEntity.getCurrentWardID());
        setModeOfTransmission(htcConfirmEntity.getModeOfTransmission());
        setInsurance(htcConfirmEntity.getInsurance());
        setInsuranceNo(htcConfirmEntity.getInsuranceNo());
        setDisabledEarlyHiv(htcConfirmEntity.getID() != null && StringUtils.isNotEmpty(htcConfirmEntity.getEarlyHiv()) && htcConfirmEntity.getEarlyHivDate() != null);
        setDisabledVirusLoad(htcConfirmEntity.getID() != null && StringUtils.isNotEmpty(htcConfirmEntity.getVirusLoad()) && htcConfirmEntity.getVirusLoadDate() != null);

        setVirusLoadNumber(htcConfirmEntity.getVirusLoadNumber());
        setEarlyBioName(htcConfirmEntity.getEarlyBioName());
        setEarlyDiagnose(htcConfirmEntity.getEarlyDiagnose());

    }

    /**
     * Chuyển dữ liệu từ form sang entity
     *
     * @param siteID
     * @param confirmEntity
     * @return
     * @throws ParseException
     */
    public HtcConfirmEntity getHtcConfirm(Long siteID, HtcConfirmEntity confirmEntity) throws ParseException {

        String formatDate = "dd/MM/yyyy";
        if (confirmEntity != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
            confirmEntity.setSiteID(siteID);
            confirmEntity.setFullname(getFullname());
            confirmEntity.setGenderID(getGenderID());
            confirmEntity.setYear(StringUtils.isNotBlank(getYear()) ? Integer.valueOf(getYear()) : null);
            confirmEntity.setPatientID(getPatientID());
            confirmEntity.setObjectGroupID(getObjectGroupID());
            confirmEntity.setAddress(getAddress());
            confirmEntity.setPermanentAddressGroup(getPermanentAddressGroup());
            confirmEntity.setPermanentAddressStreet(getPermanentAddressStreet());
            confirmEntity.setProvinceID(getProvinceID());
            confirmEntity.setDistrictID(getDistrictID());
            confirmEntity.setWardID(getWardID());
            confirmEntity.setSourceID(getSourceID());
            confirmEntity.setSourceSiteID(getSourceSiteID());
            confirmEntity.setBioName1(getBioName1());
            confirmEntity.setBioName2(getBioName2());
            confirmEntity.setBioName3(getBioName3());
            confirmEntity.setBioNameResult1(getBioNameResult1());
            confirmEntity.setBioNameResult2(getBioNameResult2());
            confirmEntity.setBioNameResult3(getBioNameResult3());
            confirmEntity.setOtherTechnical(getOtherTechnical());
            confirmEntity.setResultsID(StringUtils.isBlank(getResultsID()) ? null : getResultsID());
            confirmEntity.setSourceServiceID(getSourceServiceID());
            confirmEntity.setRemove(false);

            confirmEntity.setVirusLoadNumber(getVirusLoadNumber());
            confirmEntity.setEarlyBioName(getEarlyBioName());
            confirmEntity.setEarlyDiagnose(getEarlyDiagnose());

            if (confirmEntity.getID() == null || (confirmEntity.getID() != null && !ServiceEnum.HTC_CONFIRM.getKey().equals(confirmEntity.getSourceServiceID()))) {
                confirmEntity.setCode(getCode());
            }
            if (StringUtils.isNotEmpty(getSourceReceiveSampleTime())) {
                confirmEntity.setSourceReceiveSampleTime(simpleDateFormat.parse(getSourceReceiveSampleTime()));
            }
            confirmEntity.setTestResultsID(getTestResultsID());
            confirmEntity.setSampleQuality(getSampleQuality());
            if (StringUtils.isNotEmpty(getSourceSampleDate())) {
                confirmEntity.setSourceSampleDate(simpleDateFormat.parse(getSourceSampleDate()));
            }
            if (StringUtils.isNotEmpty(getSampleReceiveTime())) {
                confirmEntity.setSampleReceiveTime(simpleDateFormat.parse(getSampleReceiveTime()));
                confirmEntity.setAcceptDate(confirmEntity.getSampleReceiveTime());
            }
            if (StringUtils.isNotEmpty(getConfirmTime())) {
                confirmEntity.setConfirmTime(simpleDateFormat.parse(getConfirmTime()));
            }
            if (StringUtils.isNotEmpty(getResultsReturnTime())) {
                confirmEntity.setResultsReturnTime(simpleDateFormat.parse(getResultsReturnTime()));
            }
            confirmEntity.setTestStaffId(getTestStaffID());

            // Bổ sung thêm thông tin tải lương virus và kết quả HIV nhiễm mới
            confirmEntity.setEarlyHiv(StringUtils.isNotEmpty(confirmEntity.getResultsID()) && ConfirmTestResultEnum.DUONG_TINH.equals(confirmEntity.getResultsID())
                    ? getEarlyHiv() : null);

            if (ConfirmTestResultEnum.DUONG_TINH.equals(confirmEntity.getResultsID())) {
                confirmEntity.setVirusLoad(getVirusLoad());
            } else {
                confirmEntity.setVirusLoad(null);
            }
            confirmEntity.setPacSentDate(StringUtils.isNotEmpty(getPacSentDate()) ? simpleDateFormat.parse(getPacSentDate()) : null);
            confirmEntity.setPacPatientID(StringUtils.isNotEmpty(getPacPatientID()) ? Long.parseLong(getPacPatientID()) : null);
            confirmEntity.setSampleSaveCode(getSampleSaveCode());
            confirmEntity.setFirstBioDate(StringUtils.isNotEmpty(getBioName1()) ? (StringUtils.isEmpty(getFirstBioDate()) ? simpleDateFormat.parse(getConfirmTime()) : simpleDateFormat.parse(getFirstBioDate())) : null);
            confirmEntity.setSecondBioDate(StringUtils.isNotEmpty(getBioName2()) ? (StringUtils.isEmpty(getSecondBioDate()) ? simpleDateFormat.parse(getConfirmTime()) : simpleDateFormat.parse(getSecondBioDate())) : null);
            confirmEntity.setThirdBioDate(StringUtils.isNotEmpty(getBioName3()) ? (StringUtils.isEmpty(getThirdBioDate()) ? simpleDateFormat.parse(getConfirmTime()) : simpleDateFormat.parse(getThirdBioDate())) : null);
            confirmEntity.setVirusLoadDate(StringUtils.isNotEmpty(getVirusLoadDate()) ? simpleDateFormat.parse(getVirusLoadDate()) : null);
            confirmEntity.setEarlyHivDate(StringUtils.isNotEmpty(getEarlyHivDate()) ? simpleDateFormat.parse(getEarlyHivDate()) : null);
            confirmEntity.setSampleSentSource(getSampleSentSource());
            confirmEntity.setServiceID(getServiceID());
            confirmEntity.setInsurance(getInsurance());
            confirmEntity.setInsuranceNo(getInsuranceNo());

            confirmEntity.setModeOfTransmission(getModeOfTransmission());
            if (getIsDisplayCopy() == null || getIsDisplayCopy()) {
                confirmEntity.setCurrentAddress(confirmEntity.getAddress());
                confirmEntity.setCurrentAddressGroup(confirmEntity.getPermanentAddressGroup());
                confirmEntity.setCurrentAddressStreet(confirmEntity.getPermanentAddressStreet());
                confirmEntity.setCurrentProvinceID(confirmEntity.getProvinceID());
                confirmEntity.setCurrentDistrictID(confirmEntity.getDistrictID());
                confirmEntity.setCurrentWardID(confirmEntity.getWardID());
            } else {
                confirmEntity.setCurrentAddress(getCurrentAddress());
                confirmEntity.setCurrentAddressGroup(getCurrentAddressGroup());
                confirmEntity.setCurrentAddressStreet(getCurrentAddressStreet());
                confirmEntity.setCurrentProvinceID(getCurrentProvinceID());
                confirmEntity.setCurrentDistrictID(getCurrentDistrictID());
                confirmEntity.setCurrentWardID(getCurrentWardID());
            }
            return confirmEntity;
        }

        HtcConfirmEntity htcEntity = new HtcConfirmEntity();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
        htcEntity.setID(getID());
        htcEntity.setCode(getCode());
        htcEntity.setSiteID(siteID);
        htcEntity.setFullname(getFullname());
        htcEntity.setGenderID(getGenderID());
        htcEntity.setYear(StringUtils.isNotBlank(getYear()) ? Integer.valueOf(getYear()) : null);
        htcEntity.setPatientID(getPatientID());
        htcEntity.setObjectGroupID(getObjectGroupID());
        htcEntity.setAddress(getAddress());
        htcEntity.setPermanentAddressGroup(getPermanentAddressGroup());
        htcEntity.setPermanentAddressStreet(getPermanentAddressStreet());
        htcEntity.setProvinceID(getProvinceID());
        htcEntity.setDistrictID(getDistrictID());
        htcEntity.setWardID(getWardID());
        htcEntity.setSourceID(getSourceID());
        htcEntity.setSourceSiteID(getSourceSiteID());
        htcEntity.setBioName1(getBioName1());
        htcEntity.setBioName2(getBioName2());
        htcEntity.setBioName3(getBioName3());
        htcEntity.setBioNameResult1(getBioNameResult1());
        htcEntity.setBioNameResult2(getBioNameResult2());
        htcEntity.setBioNameResult3(getBioNameResult3());
        htcEntity.setOtherTechnical(getOtherTechnical());
        htcEntity.setResultsID(StringUtils.isBlank(getResultsID()) ? null : getResultsID());
        htcEntity.setSourceServiceID(getSourceServiceID());
        htcEntity.setRemove(false);
        if (htcEntity.getID() == null || (htcEntity.getID() != null && !ServiceEnum.HTC_CONFIRM.getKey().equals(htcEntity.getSourceServiceID()))) {
            htcEntity.setCode(getCode());
        }
        if (StringUtils.isNotEmpty(getSourceReceiveSampleTime())) {
            htcEntity.setSourceReceiveSampleTime(simpleDateFormat.parse(getSourceReceiveSampleTime()));
        }
        htcEntity.setTestResultsID(getTestResultsID());
        htcEntity.setSampleQuality(getSampleQuality());
        if (StringUtils.isNotEmpty(getSourceSampleDate())) {
            htcEntity.setSourceSampleDate(simpleDateFormat.parse(getSourceSampleDate()));
        }
        if (StringUtils.isNotEmpty(getSampleReceiveTime())) {
            htcEntity.setSampleReceiveTime(simpleDateFormat.parse(getSampleReceiveTime()));
            htcEntity.setAcceptDate(htcEntity.getSampleReceiveTime());
        }
        if (StringUtils.isNotEmpty(getConfirmTime())) {
            htcEntity.setConfirmTime(simpleDateFormat.parse(getConfirmTime()));
        }
        if (StringUtils.isNotEmpty(getResultsReturnTime())) {
            htcEntity.setResultsReturnTime(simpleDateFormat.parse(getResultsReturnTime()));
        }
        htcEntity.setTestStaffId(getTestStaffID());

        // Bổ sung thêm thông tin tải lương virus và kết quả HIV nhiễm mới
        htcEntity.setEarlyHiv(StringUtils.isNotEmpty(htcEntity.getResultsID()) && ConfirmTestResultEnum.DUONG_TINH.equals(htcEntity.getResultsID())
                ? getEarlyHiv() : null);

        if (ConfirmTestResultEnum.DUONG_TINH.equals(htcEntity.getResultsID())) {
            htcEntity.setVirusLoad(getVirusLoad());
        } else {
            htcEntity.setVirusLoad(null);
        }
        htcEntity.setPacSentDate(StringUtils.isNotEmpty(getPacSentDate()) ? simpleDateFormat.parse(getPacSentDate()) : null);
        htcEntity.setPacPatientID(StringUtils.isNotEmpty(getPacPatientID()) ? Long.parseLong(getPacPatientID()) : null);
        htcEntity.setSampleSaveCode(getSampleSaveCode());

        htcEntity.setFirstBioDate(StringUtils.isNotEmpty(getBioName1()) ? (StringUtils.isEmpty(getFirstBioDate()) ? simpleDateFormat.parse(getConfirmTime()) : simpleDateFormat.parse(getFirstBioDate())) : null);
        htcEntity.setSecondBioDate(StringUtils.isNotEmpty(getBioName2()) ? (StringUtils.isEmpty(getSecondBioDate()) ? simpleDateFormat.parse(getConfirmTime()) : simpleDateFormat.parse(getSecondBioDate())) : null);
        htcEntity.setThirdBioDate(StringUtils.isNotEmpty(getBioName3()) ? (StringUtils.isEmpty(getThirdBioDate()) ? simpleDateFormat.parse(getConfirmTime()) : simpleDateFormat.parse(getThirdBioDate())) : null);
        htcEntity.setVirusLoadDate(StringUtils.isNotEmpty(getVirusLoadDate()) ? simpleDateFormat.parse(getVirusLoadDate()) : null);
        htcEntity.setEarlyHivDate(StringUtils.isNotEmpty(getEarlyHivDate()) ? simpleDateFormat.parse(getEarlyHivDate()) : null);
        htcEntity.setSampleSentSource(getSampleSentSource());
        htcEntity.setServiceID(getServiceID());
        htcEntity.setModeOfTransmission(getModeOfTransmission());
        htcEntity.setInsurance(getInsurance());
        htcEntity.setInsuranceNo(getInsuranceNo());

        htcEntity.setVirusLoadNumber(getVirusLoadNumber());
        htcEntity.setEarlyBioName(getEarlyBioName());
        htcEntity.setEarlyDiagnose(getEarlyDiagnose());

        if (getIsDisplayCopy() == null || getIsDisplayCopy()) {
            htcEntity.setCurrentAddress(htcEntity.getAddress());
            htcEntity.setCurrentAddressGroup(htcEntity.getPermanentAddressGroup());
            htcEntity.setCurrentAddressStreet(htcEntity.getPermanentAddressStreet());
            htcEntity.setCurrentProvinceID(htcEntity.getProvinceID());
            htcEntity.setCurrentDistrictID(htcEntity.getDistrictID());
            htcEntity.setCurrentWardID(htcEntity.getWardID());
        } else {
            htcEntity.setCurrentAddress(getCurrentAddress());
            htcEntity.setCurrentAddressGroup(getCurrentAddressGroup());
            htcEntity.setCurrentAddressStreet(getCurrentAddressStreet());
            htcEntity.setCurrentProvinceID(getCurrentProvinceID());
            htcEntity.setCurrentDistrictID(getCurrentDistrictID());
            htcEntity.setCurrentWardID(getCurrentWardID());
        }

        return htcEntity;
    }
}

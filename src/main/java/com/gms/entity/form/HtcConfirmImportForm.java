package com.gms.entity.form;

import com.gms.entity.db.BaseEntity;
import java.io.Serializable;
import java.lang.reflect.Field;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author NamAnh_HaUI
 */
public class HtcConfirmImportForm extends BaseEntity implements Serializable   {
    
    private final String REGEX_VALIDATE_DATE = "^(?=\\d{2}([-.,\\/])\\d{2}\\1\\d{4}$)(?:0[1-9]|1\\d|[2][0-8]|29(?!.02.(?!(?!(?:[02468][1-35-79]|[13579][0-13-57-9])00)\\d{2}(?:[02468][048]|[13579][26])))|30(?!.02)|31(?=.(?:0[13578]|10|12))).(?:0[1-9]|1[012]).\\d{4}$";
    private final String REGEX_ALLOWED_CHAR = "(^[a-zA-Z0-9](?!.*\\-\\-)[a-zA-Z0-9\\-\\/]+[a-zA-Z0-9]$)";
    private final String REGEX_YEAR_CHAR = "^(19|20)\\d{2}$";
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max=100, message="Họ tên không quá 100 kí tự")
    private String fullname;
    @NotBlank(message = "Mã XN không được để trống")
    @Size(max=100, message="Mã XN không quá 100 kí tự")
    @Pattern(regexp = REGEX_ALLOWED_CHAR, message = "Mã khách hàng chứa ký tự số, dấu in hoa, '-'")
    private String code;
    @NotBlank(message = "Giới tính không được để trống")
    private String genderID;
    @NotBlank(message = "Năm sinh không được để trống")
    @Pattern(regexp = REGEX_YEAR_CHAR, message = "Năm sinh phải nhập số và đúng định dạng năm (1900-2099)")
    private String year;
    @Size(max=500, message="Số nhà thường trú không quá 500 kí tự")
    private String address;
    @NotBlank(message = "Đối tượng không được để trống")
    private String objectGroupID;
    @NotBlank(message = "Cơ sở gửi mẫu không được để trống")
    private String sourceSiteID;
    @NotBlank(message = "Ngày nhận mẫu không được để trống")
    @Pattern(regexp = REGEX_VALIDATE_DATE, message = "Định dạng ngày không đúng dd/mm/yyyy")
    private String sampleReceiveTime;
    @NotBlank(message = "Ngày XN khẳng định không được để trống")
    @Pattern(regexp = REGEX_VALIDATE_DATE, message = "Định dạng ngày không đúng dd/mm/yyyy")
    private String confirmTime;
    private String resultsID;
    @NotBlank(message = "Mã KH XN sàng lọc không được để trống")
    private String sourceID;
    private String wardID;
    private String districtID;
    private String provinceID;
    private String currentAddress; //Địa chỉ đang cư trú
    private String currentAddressStreet;
    private String currentAddressGroup;
    private String currentProvinceID; // Mã tỉnh đang cư trú
    private String currentDistrictID; // Mã huyện đang cư trú
    private String currentWardID; // Mã phường/ xã đang cư trú
    private String modeOfTransmission; // Đường lây nhiễm
    private String insurance; //Có thẻ BHYT ?
    private String insuranceNo; //Số thẻ BHYT
    private String earlyHiv;
    private String virusLoad;
    private String sourceServiceID; // Dịch vụ nơi gửi mẫu
    private String permanentAddressStreet;
    private String permanentAddressGroup;
    private String patientID;
    private String sourceReceiveSampleTime;
    private String testResultsID;
    private String sampleQuality;
    private String sourceSampleDate;
    private String serviceID;
    private String bioName1;
    private String bioNameResult1;
    private String bioName2;
    private String bioNameResult2;
    private String bioName3;
    private String bioNameResult3;
    
    private String pacSentDate;   // Trạng thái chuyển gửi GSPH dựa trên ngày này
    private String pacPatientID;  // Id của bênh nhân bên quản lý người nhiễm
    private String sampleSaveCode; // Mã số lưu mẫu trường hợp dương tính ( không bắt buộc)
    private String earlyHivDate; // Ngày xét nghiệm nhiễm mới HIV
    private String virusLoadDate; // Ngày làm xét nghiệm tải lượng virus
    private String firstBioDate; // Ngày làm sinh phẩm 1
    private String secondBioDate; // Ngày làm sinh phẩm 2
    private String thirdBioDate; // Ngày làm sinh phẩm 3
    private String sampleSentSource; // Nơi gửi bệnh phẩm
    private Long ID;
    private Long siteID;
    private String resultsReturnTime;
    private String testStaffId;
    private String bloodSample;
    private String acceptDate;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private String addressFull;
    private String otherTechnical;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getResultsReturnTime() {
        return resultsReturnTime;
    }

    public void setResultsReturnTime(String resultsReturnTime) {
        this.resultsReturnTime = resultsReturnTime;
    }

    public String getTestStaffId() {
        return testStaffId;
    }

    public void setTestStaffId(String testStaffId) {
        this.testStaffId = testStaffId;
    }

    public String getBloodSample() {
        return bloodSample;
    }

    public void setBloodSample(String bloodSample) {
        this.bloodSample = bloodSample;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
    }

    public String getOtherTechnical() {
        return otherTechnical;
    }

    public void setOtherTechnical(String otherTechnical) {
        this.otherTechnical = otherTechnical;
    }
    
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getSourceReceiveSampleTime() {
        return sourceReceiveSampleTime;
    }

    public void setSourceReceiveSampleTime(String sourceReceiveSampleTime) {
        this.sourceReceiveSampleTime = sourceReceiveSampleTime;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getSampleQuality() {
        return sampleQuality;
    }

    public void setSampleQuality(String sampleQuality) {
        this.sampleQuality = sampleQuality;
    }

    public String getSourceSampleDate() {
        return sourceSampleDate;
    }

    public void setSourceSampleDate(String sourceSampleDate) {
        this.sourceSampleDate = sourceSampleDate;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
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

    public String getSampleSaveCode() {
        return sampleSaveCode;
    }

    public void setSampleSaveCode(String sampleSaveCode) {
        this.sampleSaveCode = sampleSaveCode;
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

    public String getSampleSentSource() {
        return sampleSentSource;
    }

    public void setSampleSentSource(String sampleSentSource) {
        this.sampleSentSource = sampleSentSource;
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
    
    public String getSourceServiceID() {
        return sourceServiceID;
    }

    public void setSourceServiceID(String sourceServiceID) {
        this.sourceServiceID = sourceServiceID;
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

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getSampleReceiveTime() {
        return sampleReceiveTime;
    }

    public void setSampleReceiveTime(String sampleReceiveTime) {
        this.sampleReceiveTime = sampleReceiveTime;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    @Override
    public boolean set(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(this, fieldValue);
        return true;
    }
}

package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author pdthang
 */
public class PqmHtcApiElogForm extends BaseEntity implements Serializable {

    private String siteCode;//A3. Mã cơ sở 

    private String code;//A4. Số thứ tự khách hàng

    private String patientName;

    private String patientPhone; //Số điện thoại bệnh nhân

    private String patientID;

    private String genderID;

    private String yearOfBirth;

    private String identityCard; //Chứng minh thư nhân dân

    private String insuranceNo; //BHYT

    private String permanentAddress; //Địa chỉ thường trú

    private String currentAddress; //Địa chỉ cư trú

    private String objectGroupID; //Nhóm đối tượng

    private String source; //Nguon lay du lieu

    private String advisoryeTime; //Ngày tư vấn

    private String preTestTime; //Ngày xét nghiệm sàng lọc

    private String testResultsID; //Kết quả xét nghiệm sàng lọc

    private String confirmTime; //Ngày xét nghiệm khẳng định

    private String resultsSiteTime; //Ngày cơ sở nhận kết quả xn khẳng định

    private String confirmTestNo; //Mã xn khẳng định

    private String confirmResultsID; //Kết quả xét nghiệm khẳng định

    private String resultsTime; //Ngày trả kết quả xét nghiệm

    private String exchangeTime; // Ngày chuyển gửi

    private String arrivalTime; // Ngày tiếp nhận

    private String registerTherapyTime; // Ngày đăng ký điều trị

    private String registeredTherapySite; // Tên cơ sở mà khách hành đã đăng ký điều trị

    private String therapyNo; // Mã số điều trị

    private String earlyHivDate; // Ngày xn moi nhiem

    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    private String note;

    //Thêm theo yêu cầu
    private String province_code;
    private Integer month;
    private Integer year;
    private Integer quantity;
    private String service_type;
    private String district1;
    private String province1;
    private String district2;
    private String province2;
    private Integer data_sharing_acceptance;
    private String customer_id;

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getDistrict1() {
        return district1;
    }

    public void setDistrict1(String district1) {
        this.district1 = district1;
    }

    public String getProvince1() {
        return province1;
    }

    public void setProvince1(String province1) {
        this.province1 = province1;
    }

    public String getDistrict2() {
        return district2;
    }

    public void setDistrict2(String district2) {
        this.district2 = district2;
    }

    public String getProvince2() {
        return province2;
    }

    public void setProvince2(String province2) {
        this.province2 = province2;
    }

    public Integer getData_sharing_acceptance() {
        return data_sharing_acceptance;
    }

    public void setData_sharing_acceptance(Integer data_sharing_acceptance) {
        this.data_sharing_acceptance = data_sharing_acceptance;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(String advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }

    public String getPreTestTime() {
        return preTestTime;
    }

    public void setPreTestTime(String preTestTime) {
        this.preTestTime = preTestTime;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getResultsSiteTime() {
        return resultsSiteTime;
    }

    public void setResultsSiteTime(String resultsSiteTime) {
        this.resultsSiteTime = resultsSiteTime;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public String getResultsTime() {
        return resultsTime;
    }

    public void setResultsTime(String resultsTime) {
        this.resultsTime = resultsTime;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getRegisterTherapyTime() {
        return registerTherapyTime;
    }

    public void setRegisterTherapyTime(String registerTherapyTime) {
        this.registerTherapyTime = registerTherapyTime;
    }

    public String getRegisteredTherapySite() {
        return registeredTherapySite;
    }

    public void setRegisteredTherapySite(String registeredTherapySite) {
        this.registeredTherapySite = registeredTherapySite;
    }

    public String getTherapyNo() {
        return therapyNo;
    }

    public void setTherapyNo(String therapyNo) {
        this.therapyNo = therapyNo;
    }

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}

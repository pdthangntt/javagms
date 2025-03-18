package com.gms.entity.form.htc_confirm;

import java.util.Date;

/**
 *
 * @author NamAnh_HaUI
 */
public class HtcConfirmPositiveDistinctTableForm {

    private String fullName;
    private Integer year;
    private String genderID;
    private String identityCard;
    private String address;
    private String objectGroupID;
    private String sourceReceiveSampleTime;
    private String testResultsID;
    private String visitTestCode;
    private String sourceSiteID;
    private String sampleReceiveTime;
    private String confirmTime;
    private String code;
    private String note;

    private String sourceID; // Mã khách hàng tai nguon gui mau
    private String earlyBioName;//Tên sinh phẩm mới nhiễm
    private String earlyHivDate; // Ngày xn Nhiễm mới
    private String earlyHiv; //kq xn moi nhiem

    private String virusLoadDate; // Ngày xét nghiệm tải lượng virus
    private String virusLoadNumber; //Kết quả xét nghiệm TLVR - nhập số
    private String virusLoad; //Kết quả xét nghiệm TLVR 
    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getEarlyBioName() {
        return earlyBioName;
    }

    public void setEarlyBioName(String earlyBioName) {
        this.earlyBioName = earlyBioName;
    }

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getVirusLoadDate() {
        return virusLoadDate;
    }

    public void setVirusLoadDate(String virusLoadDate) {
        this.virusLoadDate = virusLoadDate;
    }

    public String getVirusLoadNumber() {
        return virusLoadNumber;
    }

    public void setVirusLoadNumber(String virusLoadNumber) {
        this.virusLoadNumber = virusLoadNumber;
    }

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
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

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getVisitTestCode() {
        return visitTestCode;
    }

    public void setVisitTestCode(String visitTestCode) {
        this.visitTestCode = visitTestCode;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSourceReceiveSampleTime() {
        return sourceReceiveSampleTime;
    }

    public void setSourceReceiveSampleTime(String sourceReceiveSampleTime) {
        this.sourceReceiveSampleTime = sourceReceiveSampleTime;
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

}

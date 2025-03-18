package com.gms.entity.form.htc_confirm;

import java.util.List;

/**
 *
 * @author pdThang
 */
public class TicketResultForm {

    private String title;
    private String fileName;

    private Long siteID;
    private String siteName;//teen dv
    private String siteAddress;//dia chi
    private String sitePhone;

    private String patientName;//tên khách hàng
    private String genderID;
    private String patientCode;//mã kh
    private String sourceID;
    private String yearOfBirth;
    private String patientID;
    private String patientAddress;
    private String confirmTime;
    private String resultsID;
    private String objectGroupID;
    private String sampleReceiveTime; // Ngày nhận mẫu
    private String sampleSentSource; // Nơi gửi bệnh phẩm 
    private List<String> bioNameResult;
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSampleSentSource() {
        return sampleSentSource;
    }

    public void setSampleSentSource(String sampleSentSource) {
        this.sampleSentSource = sampleSentSource;
    }

    public String getSampleReceiveTime() {
        return sampleReceiveTime;
    }

    public void setSampleReceiveTime(String sampleReceiveTime) {
        this.sampleReceiveTime = sampleReceiveTime;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public List<String> getBioNameResult() {
        return bioNameResult;
    }

    public void setBioNameResult(List<String> bioNameResult) {
        this.bioNameResult = bioNameResult;
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

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSitePhone() {
        return sitePhone;
    }

    public void setSitePhone(String sitePhone) {
        this.sitePhone = sitePhone;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
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

}

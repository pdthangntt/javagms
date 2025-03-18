package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author pdthang
 */
public class PqmHtcApiRecencyForm extends BaseEntity implements Serializable {

    private String siteCode;

    private String provinceID; //Mã tỉnh

    private String code; //Mã

    private String patientName; //Họ và tên

    private String yearOfBirth;

    private String genderID; //Giới tính

    private String identityCard; //Chứng minh thư

    private String objectGroupID; //Nhóm đối tượng

    private String address; //Địa chỉ

    private String confirmTestNo; //Mã xn khẳng định

    private String siteTesting; //Cơ sở gửi mẫu

    private String earlyHivDate; //Ngày xn nhiễm mới

    private String earlyDiagnose; //Kết quả xét nghiệm nhiễm mới

    private String pqmVctID; // ID khách hàng tư vấn xn

    private String siteConfirmID; // ID dv khẳng định

    private String siteConfirmCode; //Mã đv khẳng định

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
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

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getSiteTesting() {
        return siteTesting;
    }

    public void setSiteTesting(String siteTesting) {
        this.siteTesting = siteTesting;
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

    public String getPqmVctID() {
        return pqmVctID;
    }

    public void setPqmVctID(String pqmVctID) {
        this.pqmVctID = pqmVctID;
    }

    public String getSiteConfirmID() {
        return siteConfirmID;
    }

    public void setSiteConfirmID(String siteConfirmID) {
        this.siteConfirmID = siteConfirmID;
    }

    public String getSiteConfirmCode() {
        return siteConfirmCode;
    }

    public void setSiteConfirmCode(String siteConfirmCode) {
        this.siteConfirmCode = siteConfirmCode;
    }

}

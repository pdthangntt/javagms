package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class PqmVctRecencyForm extends BaseEntity implements Serializable {

    private String no; //TT
    private String name;
    private String code;
    private String testTime;
    private String earlyDiagnose;
    private String gender;
    private String yearOfBirth;
    private String province;
    private String district;
    private String ward;
    private String identityCard;
    private String objectGroupID;
    private String address;
    private String siteTesting;
    private String confirmTestNo;
    private String earlyHivDate;
    private String siteConfirmCode;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getSiteConfirmCode() {
        return siteConfirmCode;
    }

    public void setSiteConfirmCode(String siteConfirmCode) {
        this.siteConfirmCode = siteConfirmCode;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getSiteTesting() {
        return siteTesting;
    }

    public void setSiteTesting(String siteTesting) {
        this.siteTesting = siteTesting;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

}

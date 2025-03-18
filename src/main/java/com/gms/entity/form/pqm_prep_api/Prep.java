package com.gms.entity.form.pqm_prep_api;

import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Prep extends BaseEntity {

    private String siteCode;

    private String code;

    private String fullName; //Họ và tên

    private String patientPhone; //Số điện thoại bệnh nhân

    private String type; //Loại khách hàng

    private String genderID;

    private String yearOfBirth;

    private String objectGroupID; //Nhóm đối tượng

    private String identityCard; //Chứng minh thư nhân dân

    private String insuranceNo; //BHYT

    private String startTime; //Ngày bắt đầu điều trị

    private String endTime; //Ngày kết thúc điều trị

    private String source; //Nguồn chuyển gửi

    private String otherSite; //Chuyển từ cơ sở khác (ghi rõ tên cs)

    private String projectSupport; //Dự án hỗ trợ

    private String customer_id;
    private String district_code;
    private String client_address;
    private String prep_start_date; //Ngày bắt đầu điều trị PrEP của cả quá trình điều trị

    private List<Stage> stages;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getPrep_start_date() {
        return prep_start_date;
    }

    public void setPrep_start_date(String prep_start_date) {
        this.prep_start_date = prep_start_date;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOtherSite() {
        return otherSite;
    }

    public void setOtherSite(String otherSite) {
        this.otherSite = otherSite;
    }

    public String getProjectSupport() {
        return projectSupport;
    }

    public void setProjectSupport(String projectSupport) {
        this.projectSupport = projectSupport;
    }

}

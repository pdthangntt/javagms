package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author othoa
 */
@Entity
@Table(name = "PQM_VCT_RECENCY",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CONFIRM_TEST_NO", "SITE_ID", "EARLY_HIV_DATE"})
        },
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
            ,@Index(name = "OS_CODE", columnList = "CODE")
            ,@Index(name = "OS_PROVINCE_ID", columnList = "PROVINCE_ID")
            ,@Index(name = "OS_PATIENT_NAME", columnList = "PATIENT_NAME")
            ,@Index(name = "OS_YEAR_OF_BIRTH", columnList = "YEAR_OF_BIRTH")
            ,@Index(name = "OS_GENDER_ID", columnList = "GENDER_ID")
            ,@Index(name = "OS_IDENTITY_CARD", columnList = "IDENTITY_CARD")
            ,@Index(name = "OS_CONFIRM_TEST_NO", columnList = "CONFIRM_TEST_NO")
            ,@Index(name = "OS_EARLY_HIV_DATE", columnList = "EARLY_HIV_DATE")
            ,@Index(name = "OS_OBJECT_GROUP_ID", columnList = "OBJECT_GROUP_ID")
            ,@Index(name = "OS_EARLY_DIAGNOSE", columnList = "EARLY_DIAGNOSE")
            ,@Index(name = "OS_ADDRESS", columnList = "ADDRESS")
            ,@Index(name = "OS_SITE_ID", columnList = "SITE_ID")
            ,@Index(name = "OS_PQM_VCT_ID", columnList = "PQM_VCT_ID")
        })
@DynamicUpdate
@DynamicInsert
public class PqmVctRecencyEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý

    @Column(name = "PROVINCE_ID")
    private Long provinceID; //Mã tỉnh

    @Column(name = "CODE", length = 50, nullable = false)
    private String code; //Mã

    @Column(name = "PATIENT_NAME", length = 100)
    private String patientName; //Họ và tên

    @Column(name = "YEAR_OF_BIRTH", length = 4, nullable = true)
    private String yearOfBirth;

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID; //Giới tính

    @Column(name = "IDENTITY_CARD", length = 100)
    private String identityCard; //Chứng minh thư

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "ADDRESS", length = 500, nullable = true)
    private String address; //Địa chỉ

    @Column(name = "CONFIRM_TEST_NO", length = 100, nullable = true)
    private String confirmTestNo; //Mã xn khẳng định

    @Column(name = "SITE_TESTING", length = 100, nullable = true)
    private String siteTesting; //Cơ sở gửi mẫu

    @Temporal(TemporalType.DATE)
    @Column(name = "EARLY_HIV_DATE", nullable = true)
    private Date earlyHivDate; //Ngày xn nhiễm mới

    @Column(name = "EARLY_DIAGNOSE", length = 50, nullable = true)
    private String earlyDiagnose; //Kết quả xét nghiệm nhiễm mới

    @Column(name = "PQM_VCT_ID")
    private Long pqmVctID; // ID khách hàng tư vấn xn

    @Column(name = "SITE_CONFIRM_ID")
    private Long siteConfirmID; // ID dv khẳng định

    @Column(name = "SITE_CONFIRM_CODE", length = 50, nullable = true)
    private String siteConfirmCode; //Mã đv khẳng định

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

    public Long getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(Long provinceID) {
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

    public String getSiteTesting() {
        return siteTesting;
    }

    public void setSiteTesting(String siteTesting) {
        this.siteTesting = siteTesting;
    }

    public Long getSiteConfirmID() {
        return siteConfirmID;
    }

    public void setSiteConfirmID(Long siteConfirmID) {
        this.siteConfirmID = siteConfirmID;
    }

    public String getSiteConfirmCode() {
        return siteConfirmCode;
    }

    public void setSiteConfirmCode(String siteConfirmCode) {
        this.siteConfirmCode = siteConfirmCode;
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

    public Date getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(Date earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public Long getPqmVctID() {
        return pqmVctID;
    }

    public void setPqmVctID(Long pqmVctID) {
        this.pqmVctID = pqmVctID;
    }

}

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

/**
 *
 * @author vvthanh
 * @des Lưu thông tin cơ bản bệnh nhân
 */
@Entity
@Table(name = "OPC_PATIENT", indexes = {
    @Index(name = "OP_SITE_ID", columnList = "SITE_ID")
    ,@Index(name = "OP_GENDER_ID", columnList = "GENDER_ID")
    ,@Index(name = "OP_FULLNAME", columnList = "FULLNAME")
    ,@Index(name = "OP_IDENTITY_CARD", columnList = "IDENTITY_CARD")
})
public class OpcPatientEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi

    @Column(name = "FULLNAME", length = 100)
    private String fullName; //Họ và tên

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID; //Giới tính

    @Column(name = "dob", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob; //Ngày/tháng/năm sinh

    @Column(name = "RACE_ID", length = 50, nullable = true)
    private String raceID; //Dân tộc

    @Column(name = "IDENTITY_CARD", length = 100)
    private String identityCard; //Chứng minh thư

    @Column(name = "CONFIRM_CODE", length = 100)
    private String confirmCode; // Mã XN khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CONFIRM_TIME", nullable = true)
    private Date confirmTime; //Ngày xét nghiệm khẳng định

    @Column(name = "CONFIRM_SITE_ID", nullable = true)
    private Long confirmSiteID; //Cơ sở xét nghiệm khẳng định - lấy từ tham số nơi xét nghiệm khẳng định giống hivinfo

    @Column(name = "CONFIRM_SITE_NAME", nullable = true)
    private String confirmSiteName; //Hoặc Tên cơ sở xét nghiệm khẳng định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY", nullable = false)
    private Long updatedBY;

    @Column(name = "QR_CODE", length = 36, nullable = true)
    private String qrcode; //Mã QR

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Long getConfirmSiteID() {
        return confirmSiteID;
    }

    public void setConfirmSiteID(Long confirmSiteID) {
        this.confirmSiteID = confirmSiteID;
    }

    public String getConfirmSiteName() {
        return confirmSiteName;
    }

    public void setConfirmSiteName(String confirmSiteName) {
        this.confirmSiteName = confirmSiteName;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public Long getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(Long updatedBY) {
        this.updatedBY = updatedBY;
    }

}

package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Bảng lưu trữ mã QR của bệnh nhân
 */
@Entity
@Table(name = "QR_CODE", indexes = {
    @Index(name = "QR_IS_ACTIVE", columnList = "IS_ACTIVE")
    ,@Index(name = "QR_ID_CARD", columnList = "ID_CARD")
})
@DynamicUpdate
@DynamicInsert
public class QrCodeEntity extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID", length = 36)
    private String ID; //Mã QR

    @Column(name = "SECRET", length = 50, nullable = true)
    private String secret; //Mã bảo mật

    @Column(name = "ID_CARD", length = 50, nullable = false, unique = true)
    private String idCard; //Thẻ căn cước

    @Column(name = "IDENTITY_CARD", length = 9, nullable = true)
    private String identityCard; //Chứng minh thư nhân dân

    @Column(name = "PASSPORT", length = 50, nullable = true)
    private String passport; //Hộ chiếu

    @Column(name = "DRIVING_LICENSE", length = 50, nullable = true)
    private String drivingLicense; //Bằng lái xe

    @Column(name = "FULLNAME", length = 100)
    private String fullName; //Họ và tên

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID; //Giới tính

    @Column(name = "dob", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob; //Ngày/tháng/năm sinh

    @Column(name = "RACE_ID", length = 50, nullable = true)
    private String raceID; //Dân tộc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_ACCESSED_AT", nullable = true)
    private Date lastAccessedAt;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
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

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(Date lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

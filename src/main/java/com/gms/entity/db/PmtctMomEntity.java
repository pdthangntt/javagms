package com.gms.entity.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author TrangBN
 */

@Entity
@Table(
        name = "PMTCT_MOM_INFO",
        indexes = {@Index(name = "PMTCT_SITE", columnList = "SITE_ID"),
                @Index(name = "PMTCT_SOURCE_ID", columnList = "SOURCE_ID"),
                @Index(name = "PMTCT_SOURCE_SITE", columnList = "SOURCE_SITE_ID")
        }
)
public class PmtctMomEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "CLINICAL_RECORD_NO", length = 20)
    private String clinicalRecordNo; // Mã bệnh án OPC

    @Column(name = "SITE_ID")
    private Long siteID; // Mã cơ sở hiện tại

    @Column(name = "NAME", length = 20)
    private String name; // Tên khách hàng

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth; // Ngày sinh

    @Column(name = "SECURITY_NUMBER", length = 20)
    private String securityNumber; // CMND/ thẻ căn cước

    @Column(name = "PERMANENT_ADDRESS", length = 500)
    private String permanentAddress; // Địa chỉ thường trú

    @Column(name = "PHONE", length = 20)
    private String phone; // Số điện thoại

    @Column(name = "CONFIRM_TEST_TIME")
    private Date confirmTestTime; // Ngày xét nghiệm khẳng định

    @Column(name = "CONFIRM_TEST_SITE", length = 100)
    private String confirmTestSite; // Tên cơ sở xét nghiệm khẳng định

    @Column(name = "CD4_CELL_QUANTITY")
    private Long cd4CellQuantity; // Số lượng tế bào CD4/ giai đoạn lâm sàng

    @Column(name = "HIV_STAGE", length = 5)
    private String hivStage; // Giai đoạn nhiễm HIV

    @Column(name = "THERAPY_START_TIME")
    private Date therapyStartTime; // Ngày bắt đầu  điều trị

    @Column(name = "ARV_REGIMEN", length = 50)
    private String arvRegimen; // Phác đồ điều trị ARV

    @Column(name = "ARRIVAL_SITE", length = 100)
    private String arrivalSite; // Cơ sở chuyển đến ( nhập từ danh sach sđiều trị ARV)

    @Column(name = "IS_REMOVE")
    private Boolean isRemove; // Đã bị xóa ?

    @Column(name = "EXCHANGE_DATE")
    private Date exchangeDate; // Ngày chuyển mẹ tới cơ sở khác.

    @Column(name = "SOURCE_ID")
    private Long sourceId; // Mã ID của bảng nguồn chuyển tới

    @Column(name = "SOURCE_SITE_ID")
    private Long sourceSiteId; // Mã cơ sở nơi chuyển tới

    @Column(name = "SOURCE_TYPE", length = 50)
    private String sourceType; // loại nguồn: từ HTC, OPC khác chuyển tới

    @Column(name = "CREATED_AT")
    private Date createdAt; // Thời gian tạo

    @Column(name = "CREATED_BY")
    private Long createdBy; // Mã nhân viên tạo mới

    @Column(name = "UPDATED_AT")
    private Date updatedAt; // Thời gian cập nhật

    @Column(name = "UPDATED_BY")
    private Long updatedBy; // Mã nhân viên cập nhật

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getClinicalRecordNo() {
        return clinicalRecordNo;
    }

    public void setClinicalRecordNo(String clinicalRecordNo) {
        this.clinicalRecordNo = clinicalRecordNo;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getConfirmTestTime() {
        return confirmTestTime;
    }

    public void setConfirmTestTime(Date confirmTestTime) {
        this.confirmTestTime = confirmTestTime;
    }

    public String getConfirmTestSite() {
        return confirmTestSite;
    }

    public void setConfirmTestSite(String confirmTestSite) {
        this.confirmTestSite = confirmTestSite;
    }

    public Long getCd4CellQuantity() {
        return cd4CellQuantity;
    }

    public void setCd4CellQuantity(Long cd4CellQuantity) {
        this.cd4CellQuantity = cd4CellQuantity;
    }

    public String getHivStage() {
        return hivStage;
    }

    public void setHivStage(String hivStage) {
        this.hivStage = hivStage;
    }

    public Date getTherapyStartTime() {
        return therapyStartTime;
    }

    public void setTherapyStartTime(Date therapyStartTime) {
        this.therapyStartTime = therapyStartTime;
    }

    public String getArvRegimen() {
        return arvRegimen;
    }

    public void setArvRegimen(String arvRegimen) {
        this.arvRegimen = arvRegimen;
    }

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public Boolean getRemove() {
        return isRemove;
    }

    public void setRemove(Boolean remove) {
        isRemove = remove;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSourceSiteId() {
        return sourceSiteId;
    }

    public void setSourceSiteId(Long sourceSiteId) {
        this.sourceSiteId = sourceSiteId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}

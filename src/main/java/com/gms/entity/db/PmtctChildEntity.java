package com.gms.entity.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(
        name = "PMTCT_CHILD_INFO",
        indexes = {@Index(name = "PMTCT_SITE", columnList = "SITE_ID"),
                @Index(name = "PMTCT_MOM_ID", columnList = "PMTCT_MOM_INFO_ID")
        }
)
public class PmtctChildEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "PMTCT_MOM_INFO_ID")
    private Long pmtctMomInfoId;

    @Column(name = "SITE_ID")
    private Long siteID; // Mã cơ sở hiện tại

    @Column(name = "NAME", length = 20)
    private String name; // Tên khách hàng

    @Column(name = "ARV_REGIMEN", length = 50)
    private String arvRegimen; // Phác đồ điều trị ARV

    @Column(name = "FEED_METHOD", length = 50)
    private String feedMethod; // Hình thức nuôi dưỡng trẻ

    @Column(name = "CTX_BACKUP", length = 50)
    private String ctxBackup; // Ngày dự phòng CTX (co-trimoxazole) cho con

    @Column(name = "PCR_TEST_1", length = 50)
    private String pcrTest1; // Xét nghiệm PCR (Polemerase Chain Reaction) lần 1 cho con

    @Column(name = "PCR_TEST_2", length = 50)
    private String pcrTest2; // Xét nghiệm PCR (Polemerase Chain Reaction) lần 2 cho con

    @Column(name = "ANTIBODY_TEST", length = 50)
    private String antibodyTest; // Xét nghiệm kháng thể (đối với trẻ >18 tháng tuổi)

    @Column(name = "ARRIVAL_SITE", length = 100)
    private String arrivalSite; // Cơ sở chuyển đến ( nhập từ danh sách điều trị ARV)

    @Column(name = "EXCHANGE_DATE")
    private Date exchangeDate; // Ngày chuyển con tới cơ sở khác.

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPmtctMomInfoId() {
        return pmtctMomInfoId;
    }

    public void setPmtctMomInfoId(Long pmtctMomInfoId) {
        this.pmtctMomInfoId = pmtctMomInfoId;
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

    public String getArvRegimen() {
        return arvRegimen;
    }

    public void setArvRegimen(String arvRegimen) {
        this.arvRegimen = arvRegimen;
    }

    public String getFeedMethod() {
        return feedMethod;
    }

    public void setFeedMethod(String feedMethod) {
        this.feedMethod = feedMethod;
    }

    public String getCtxBackup() {
        return ctxBackup;
    }

    public void setCtxBackup(String ctxBackup) {
        this.ctxBackup = ctxBackup;
    }

    public String getPcrTest1() {
        return pcrTest1;
    }

    public void setPcrTest1(String pcrTest1) {
        this.pcrTest1 = pcrTest1;
    }

    public String getPcrTest2() {
        return pcrTest2;
    }

    public void setPcrTest2(String pcrTest2) {
        this.pcrTest2 = pcrTest2;
    }

    public String getAntibodyTest() {
        return antibodyTest;
    }

    public void setAntibodyTest(String antibodyTest) {
        this.antibodyTest = antibodyTest;
    }

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
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

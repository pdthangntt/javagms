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
 * @author pdThang
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "PQM_DRUG_PLAN",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"MONTH", "YEAR", "SITE_CODE", "DRUG_NAME", "SOURCE", "LOT_NUMBER", "UNIT"}, name = "pqm_prep_unique_drug_aaaaa")
        })
public class PqmDrugPlanEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
 
    @Column(name = "MONTH")
    private int month;

    @Column(name = "YEAR") 
    private int year;

    @Column(name = "PROVINCE_ID", length = 50)
    private String provinceID;

    @Column(name = "CURRENT_PROVINCE", length = 50)
    private String currentProvinceID;

    @Column(name = "SITE_CODE", length = 50)
    private String siteCode;

    @Column(name = "SITE_NAME", length = 110)
    private String siteName;

    @Column(name = "SOURCE", length = 50)
    private String source;

    @Column(name = "DRUG_NAME", length = 110)
    private String drugName;

    @Column(name = "PACKING" , length = 50)
    private String packing;

    @Column(name = "UNIT", length = 50)
    private String unit;

    @Column(name = "LOT_NUMBER", length = 50)
    private String lotNumber;

    @Column(name = "EXPIRY_DATE", length = 50)
    private String expiryDate;

    @Column(name = "BEGINNING", length = 50)
    private Long beginning;

    @Column(name = "IN_THE_PERIOD")
    private Long inThePeriod;

    @Column(name = "PATIENT")
    private Long patient;

    @Column(name = "TRANSFER")
    private Long transfer;

    @Column(name = "LOSS")
    private Long loss;

    @Column(name = "ENDING")
    private Long ending;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getBeginning() {
        return beginning;
    }

    public void setBeginning(Long beginning) {
        this.beginning = beginning;
    }

    public Long getInThePeriod() {
        return inThePeriod;
    }

    public void setInThePeriod(Long inThePeriod) {
        this.inThePeriod = inThePeriod;
    }

    public Long getPatient() {
        return patient;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public Long getTransfer() {
        return transfer;
    }

    public void setTransfer(Long transfer) {
        this.transfer = transfer;
    }

    public Long getLoss() {
        return loss;
    }

    public void setLoss(Long loss) {
        this.loss = loss;
    }

    public Long getEnding() {
        return ending;
    }

    public void setEnding(Long ending) {
        this.ending = ending;
    }

    @Override
    public PqmDrugPlanEntity clone() throws CloneNotSupportedException {
        return (PqmDrugPlanEntity) super.clone();
    }

}

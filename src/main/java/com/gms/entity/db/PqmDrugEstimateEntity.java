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
@DynamicInsert
@DynamicUpdate
@Table(name = "PQM_DRUG_ESTIMATE",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"YEAR", "PROVINCE_ID", "SITE_CODE", "DRUG_NAME", "SOURCE"}, name = "pqm_unique_drug")
        })
public class PqmDrugEstimateEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "PROVINCE_ID", length = 9, nullable = false)
    private String provinceID;

    @Column(name = "SITE_CODE", nullable = false)
    private String siteCode;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "SOURCE", nullable = false)
    private String source;

    @Column(name = "DRUG_NAME", nullable = false)
    private String drugName;

    @Column(name = "PACKING", nullable = false)
    private String packing;

    @Column(name = "HOW_TO_USE")
    private String howToUse;

    @Column(name = "EARLY_ESTIMATE")
    private Long earlyEstimate;

    @Column(name = "IN_ESTIMATE")
    private Long inEstimate;

    @Column(name = "FINAL_ESTIMATE")
    private Long finalEstimate;

    @Column(name = "EXIGENCY_TOTAL")
    private Long exigencyTotal;

    @Column(name = "EXIGENCY0")
    private Long exigency0;

    @Column(name = "EXIGENCY1")
    private Long exigency1;

    @Column(name = "EXIGENCY2")
    private Long exigency2;

    @Column(name = "EXIGENCY3")
    private Long exigency3;

    @Column(name = "UNIT")
    private String unit;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    @Override
    public PqmDrugEstimateEntity clone() throws CloneNotSupportedException {
        return (PqmDrugEstimateEntity) super.clone();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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

    public String getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(String howToUse) {
        this.howToUse = howToUse;
    }

    public Long getEarlyEstimate() {
        return earlyEstimate;
    }

    public void setEarlyEstimate(Long earlyEstimate) {
        this.earlyEstimate = earlyEstimate;
    }

    public Long getInEstimate() {
        return inEstimate;
    }

    public void setInEstimate(Long inEstimate) {
        this.inEstimate = inEstimate;
    }

    public Long getFinalEstimate() {
        return finalEstimate;
    }

    public void setFinalEstimate(Long finalEstimate) {
        this.finalEstimate = finalEstimate;
    }

    public Long getExigencyTotal() {
        return exigencyTotal;
    }

    public void setExigencyTotal(Long exigencyTotal) {
        this.exigencyTotal = exigencyTotal;
    }

    public Long getExigency0() {
        return exigency0;
    }

    public void setExigency0(Long exigency0) {
        this.exigency0 = exigency0;
    }

    public Long getExigency1() {
        return exigency1;
    }

    public void setExigency1(Long exigency1) {
        this.exigency1 = exigency1;
    }

    public Long getExigency2() {
        return exigency2;
    }

    public void setExigency2(Long exigency2) {
        this.exigency2 = exigency2;
    }

    public Long getExigency3() {
        return exigency3;
    }

    public void setExigency3(Long exigency3) {
        this.exigency3 = exigency3;
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

}

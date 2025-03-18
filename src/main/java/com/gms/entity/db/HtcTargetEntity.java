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

/**
 *
 * @author NamAnh_HaUI
 */
@Entity
@Table(name = "HTC_TARGET",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"SITE_ID", "YEARS"})},
        indexes = {
            @Index(name = "HTC_TARGET_SITE_ID", columnList = "SITE_ID")
            ,@Index(name = "HTC_TARGET_YEARS", columnList = "YEARS"),}
)
public class HtcTargetEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long ID;

    @Column(name = "SITE_ID", nullable = false)
    private Long siteID;

    @Column(name = "YEARS", nullable = false)
    private Long years;

    @Column(name = "NUMBER_CUSTOMER", nullable = false)
    private Long numberCustomer;

    @Column(name = "NUMBER_POSITIVE", nullable = false)
    private Long numberPositive;

    @Column(name = "NUMBER_GET_RESULT", nullable = false)
    private Long numberGetResult;

    @Column(name = "POSITIVE_AND_GET_RESULT", nullable = false)
    private Long positiveAndGetResult;

    @Column(name = "CARE_FOR_TREATMENT", nullable = false)
    private Double careForTreatment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY", nullable = false)
    private Long updatedBY;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
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

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public Long getYears() {
        return years;
    }

    public void setYears(Long years) {
        this.years = years;
    }

    public Long getNumberCustomer() {
        return numberCustomer;
    }

    public void setNumberCustomer(Long numberCustomer) {
        this.numberCustomer = numberCustomer;
    }

    public Long getNumberPositive() {
        return numberPositive;
    }

    public void setNumberPositive(Long numberPositive) {
        this.numberPositive = numberPositive;
    }

    public Long getNumberGetResult() {
        return numberGetResult;
    }

    public void setNumberGetResult(Long numberGetResult) {
        this.numberGetResult = numberGetResult;
    }

    public Long getPositiveAndGetResult() {
        return positiveAndGetResult;
    }

    public void setPositiveAndGetResult(Long positiveAndGetResult) {
        this.positiveAndGetResult = positiveAndGetResult;
    }

    public Double getCareForTreatment() {
        return careForTreatment;
    }

    public void setCareForTreatment(Double careForTreatment) {
        this.careForTreatment = careForTreatment;
    }


    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
    }

    @Override
    public void setAttributeLabels() {
        super.setAttributeLabels();
        attributeLabels.put("ID", "Mã");
        attributeLabels.put("siteID", "Cơ sở");
        attributeLabels.put("years", "Năm");
    }

}

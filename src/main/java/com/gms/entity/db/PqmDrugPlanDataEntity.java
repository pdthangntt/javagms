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
@Table(name = "PQM_DRUG_PLAN_DATA",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"MONTH", "YEAR", "DRUG_NAME", "UNIT", "PROVINCE_ID", "SITE_CODE"}, name = "pqm_drug_data_unique")
        })
public class PqmDrugPlanDataEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "MONTH")
    private int month;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "PROVINCE_ID", length = 9)
    private String provinceID;

    @Column(name = "SITE_CODE")
    private String siteCode;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "DRUG_NAME")
    private String drugName;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "TOTAL_DRUG_BACKLOG")
    private Long totalDrugBacklog;

    @Column(name = "TOTAL_DRUG_PLAN_2MONTH")
    private Long totalDrugPlan2Month;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

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

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getTotalDrugBacklog() {
        return totalDrugBacklog;
    }

    public void setTotalDrugBacklog(Long totalDrugBacklog) {
        this.totalDrugBacklog = totalDrugBacklog;
    }

    public Long getTotalDrugPlan2Month() {
        return totalDrugPlan2Month;
    }

    public void setTotalDrugPlan2Month(Long totalDrugPlan2Month) {
        this.totalDrugPlan2Month = totalDrugPlan2Month;
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
}

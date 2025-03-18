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
@Table(name = "PQM_DRUG_ESTIMATE_DATA",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID"),
            @Index(name = "OS_PROVINCE_CODE", columnList = "PROVINCE_CODE"),
            @Index(name = "OS_FACILYTY_CODE", columnList = "FACILYTY_CODE"),
            @Index(name = "OS_QUARTER", columnList = "QUARTER"),
            @Index(name = "OS_YEAR", columnList = "YEAR"),
            @Index(name = "OS_DRUG_NAME_LOWERCASE", columnList = "DRUG_NAME_LOWERCASE"),
            @Index(name = "OS_DRUG_UOM_LOWERCASE", columnList = "DRUG_UOM_LOWERCASE"),
            @Index(name = "OS_DRUG_NAME", columnList = "DRUG_NAME"),
            @Index(name = "OS_DRUG_UOM", columnList = "DRUG_UOM"),
            @Index(name = "OS_FACILYTY_CODE", columnList = "FACILYTY_CODE"),
            @Index(name = "OS_PLANNED_QUANTITY", columnList = "PLANNED_QUANTITY"),
            @Index(name = "OS_DISPENSED_QUANTITY", columnList = "DISPENSED_QUANTITY"),
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"PROVINCE_CODE", "FACILYTY_CODE", "QUARTER", "YEAR", "DRUG_NAME_LOWERCASE", "DRUG_UOM_LOWERCASE"}, name = "pqm_drug_data_unique")
        })
public class PqmDrugEstimateDataEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "PROVINCE_CODE", nullable = false)
    private String provinceCode;

    @Column(name = "DATA_CODE", nullable = false)
    private String dataCode;

    @Column(name = "FACILYTY_NAME")
    private String facilityName;

    @Column(name = "FACILYTY_CODE", nullable = false)
    private String facilityCode;

    @Column(name = "VERSION", nullable = false)
    private String version;

    @Column(name = "QUARTER", nullable = false)
    private int quarter;

    @Column(name = "YEAR", nullable = false)
    private int year;

    @Column(name = "DRUG_NAME", nullable = false)
    private String drugName;

    @Column(name = "DRUG_UOM", nullable = false)
    private String drugUom;

    @Column(name = "DRUG_NAME_LOWERCASE", nullable = false)
    private String drugNameLowercase;

    @Column(name = "DRUG_UOM_LOWERCASE", nullable = false)
    private String drugUomLowercase;

    @Column(name = "PLANNED_QUANTITY")
    private Long plannedQuantity;//mẫu

    @Column(name = "DISPENSED_QUANTITY")
    private Long dispensedQuantity;//tử

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugUom() {
        return drugUom;
    }

    public void setDrugUom(String drugUom) {
        this.drugUom = drugUom;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDrugNameLowercase() {
        return drugNameLowercase;
    }

    public void setDrugNameLowercase(String drugNameLowercase) {
        this.drugNameLowercase = drugNameLowercase;
    }

    public String getDrugUomLowercase() {
        return drugUomLowercase;
    }

    public void setDrugUomLowercase(String drugUomLowercase) {
        this.drugUomLowercase = drugUomLowercase;
    }

    public Long getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Long plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public Long getDispensedQuantity() {
        return dispensedQuantity;
    }

    public void setDispensedQuantity(Long dispensedQuantity) {
        this.dispensedQuantity = dispensedQuantity;
    }

}

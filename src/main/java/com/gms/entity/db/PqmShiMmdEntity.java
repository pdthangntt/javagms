package com.gms.entity.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author othoa
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "PQM_SHI_MMD",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID"),
            @Index(name = "OS_SITE_ID", columnList = "SITE_ID"),
            @Index(name = "OS_PROVINCE_ID", columnList = "PROVINCE_ID"),
            @Index(name = "OS_MONTH", columnList = "MONTH"),
            @Index(name = "OS_YEAR", columnList = "YEAR"),},
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"PROVINCE_ID", "YEAR", "MONTH", "SITE_ID"}, name = "pqm_shi_mmd_unique_site_code")
        })
public class PqmShiMmdEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "SITE_CODE")
    private String siteCode;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "PROVINCE_ID", length = 9)
    private String provinceID;

    @Column(name = "MONTH")
    private int month;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "TOTAL_TURN_TK")
    private int totalTurnTk;

    @Column(name = "TOTAL_TURN_LK")
    private int totalTurnLk;

    @Column(name = "TOTAL_PATIENT_TK")
    private int totalPatientTk;

    @Column(name = "TOTAL_PATIENT_LK")
    private int totalPatientLk;

    @Column(name = "TOTAL_PATIENT_LESS_1MONTH_TK")
    private int totalPatientLess1MonthTk;

    @Column(name = "TOTAL_PATIENT_LESS_1MONTH_LK")
    private int totalPatientLess1MonthLk;

    @Column(name = "TOTAL_TURN_LESS_1MONTH_TK")
    private int totalTurnLess1MonthTk;

    @Column(name = "TOTAL_TURN_LESS_1MONTH_LK")
    private int totalTurnLess1MonthLk;

    @Column(name = "TOTAL_TURN_1TO2_MONTH_TK")
    private int totalTurn1To2MonthTk;

    @Column(name = "TOTAL_TURN_1TO2_MONTH_LK")
    private int totalTurn1To2MonthLk;

    @Column(name = "TOTAL_PATIENT_1TO2_MONTH_TK")
    private int totalPatient1To2MonthTk;

    @Column(name = "TOTAL_PATIENT_1TO2_MONTH_LK")
    private int totalPatient1To2MonthLk;

    @Column(name = "TOTAL_TURN_2TO3_MONTH_TK")
    private int totalTurn2To3MonthTk;

    @Column(name = "TOTAL_TURN_2TO3_MONTH_LK")
    private int totalTurn2To3MonthLk;

    @Column(name = "TOTAL_PATIENT_2TO3_MONTH_TK")
    private int totalPatient2To3MonthTk;

    @Column(name = "TOTAL_PATIENT_2TO3_MONTH_LK")
    private int totalPatient2To3MonthLk;

    @Column(name = "TOTAL_TURN_3_MONTH_TK")
    private int totalTurn3MonthTk;

    @Column(name = "TOTAL_TURN_3_MONTH_LK")
    private int totalTurn3MonthLk;

    @Column(name = "TOTAL_PATIENT_3_MONTH_TK")
    private int totalPatient3MonthTk;

    @Column(name = "TOTAL_PATIENT_3_MONTH_LK")
    private int totalPatient3MonthLk;

    @Column(name = "RATIO_3MONTH_TK")
    private float ratio3MonthTk;

    @Column(name = "RATIO_3MONTH_LK")
    private float ratio3MonthLk;

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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
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

    public int getTotalTurnTk() {
        return totalTurnTk;
    }

    public void setTotalTurnTk(int totalTurnTk) {
        this.totalTurnTk = totalTurnTk;
    }

    public int getTotalTurnLk() {
        return totalTurnLk;
    }

    public void setTotalTurnLk(int totalTurnLk) {
        this.totalTurnLk = totalTurnLk;
    }

    public int getTotalPatientTk() {
        return totalPatientTk;
    }

    public void setTotalPatientTk(int totalPatientTk) {
        this.totalPatientTk = totalPatientTk;
    }

    public int getTotalPatientLk() {
        return totalPatientLk;
    }

    public void setTotalPatientLk(int totalPatientLk) {
        this.totalPatientLk = totalPatientLk;
    }

    public int getTotalPatientLess1MonthTk() {
        return totalPatientLess1MonthTk;
    }

    public void setTotalPatientLess1MonthTk(int totalPatientLess1MonthTk) {
        this.totalPatientLess1MonthTk = totalPatientLess1MonthTk;
    }

    public int getTotalPatientLess1MonthLk() {
        return totalPatientLess1MonthLk;
    }

    public void setTotalPatientLess1MonthLk(int totalPatientLess1MonthLk) {
        this.totalPatientLess1MonthLk = totalPatientLess1MonthLk;
    }

    public int getTotalTurnLess1MonthTk() {
        return totalTurnLess1MonthTk;
    }

    public void setTotalTurnLess1MonthTk(int totalTurnLess1MonthTk) {
        this.totalTurnLess1MonthTk = totalTurnLess1MonthTk;
    }

    public int getTotalTurnLess1MonthLk() {
        return totalTurnLess1MonthLk;
    }

    public void setTotalTurnLess1MonthLk(int totalTurnLess1MonthLk) {
        this.totalTurnLess1MonthLk = totalTurnLess1MonthLk;
    }

    public int getTotalTurn1To2MonthTk() {
        return totalTurn1To2MonthTk;
    }

    public void setTotalTurn1To2MonthTk(int totalTurn1To2MonthTk) {
        this.totalTurn1To2MonthTk = totalTurn1To2MonthTk;
    }

    public int getTotalTurn1To2MonthLk() {
        return totalTurn1To2MonthLk;
    }

    public void setTotalTurn1To2MonthLk(int totalTurn1To2MonthLk) {
        this.totalTurn1To2MonthLk = totalTurn1To2MonthLk;
    }

    public int getTotalPatient1To2MonthTk() {
        return totalPatient1To2MonthTk;
    }

    public void setTotalPatient1To2MonthTk(int totalPatient1To2MonthTk) {
        this.totalPatient1To2MonthTk = totalPatient1To2MonthTk;
    }

    public int getTotalPatient1To2MonthLk() {
        return totalPatient1To2MonthLk;
    }

    public void setTotalPatient1To2MonthLk(int totalPatient1To2MonthLk) {
        this.totalPatient1To2MonthLk = totalPatient1To2MonthLk;
    }

    public int getTotalTurn2To3MonthTk() {
        return totalTurn2To3MonthTk;
    }

    public void setTotalTurn2To3MonthTk(int totalTurn2To3MonthTk) {
        this.totalTurn2To3MonthTk = totalTurn2To3MonthTk;
    }

    public int getTotalTurn2To3MonthLk() {
        return totalTurn2To3MonthLk;
    }

    public void setTotalTurn2To3MonthLk(int totalTurn2To3MonthLk) {
        this.totalTurn2To3MonthLk = totalTurn2To3MonthLk;
    }

    public int getTotalPatient2To3MonthTk() {
        return totalPatient2To3MonthTk;
    }

    public void setTotalPatient2To3MonthTk(int totalPatient2To3MonthTk) {
        this.totalPatient2To3MonthTk = totalPatient2To3MonthTk;
    }

    public int getTotalPatient2To3MonthLk() {
        return totalPatient2To3MonthLk;
    }

    public void setTotalPatient2To3MonthLk(int totalPatient2To3MonthLk) {
        this.totalPatient2To3MonthLk = totalPatient2To3MonthLk;
    }

    public int getTotalTurn3MonthTk() {
        return totalTurn3MonthTk;
    }

    public void setTotalTurn3MonthTk(int totalTurn3MonthTk) {
        this.totalTurn3MonthTk = totalTurn3MonthTk;
    }

    public int getTotalTurn3MonthLk() {
        return totalTurn3MonthLk;
    }

    public void setTotalTurn3MonthLk(int totalTurn3MonthLk) {
        this.totalTurn3MonthLk = totalTurn3MonthLk;
    }

    public int getTotalPatient3MonthTk() {
        return totalPatient3MonthTk;
    }

    public void setTotalPatient3MonthTk(int totalPatient3MonthTk) {
        this.totalPatient3MonthTk = totalPatient3MonthTk;
    }

    public int getTotalPatient3MonthLk() {
        return totalPatient3MonthLk;
    }

    public void setTotalPatient3MonthLk(int totalPatient3MonthLk) {
        this.totalPatient3MonthLk = totalPatient3MonthLk;
    }

    public float getRatio3MonthTk() {
        return ratio3MonthTk;
    }

    public void setRatio3MonthTk(float ratio3MonthTk) {
        this.ratio3MonthTk = ratio3MonthTk;
    }

    public float getRatio3MonthLk() {
        return ratio3MonthLk;
    }

    public void setRatio3MonthLk(float ratio3MonthLk) {
        this.ratio3MonthLk = ratio3MonthLk;
    }

    public String getDisplayRatio3MonthTk() {
        if (getRatio3MonthTk() == 0) {
            return "0";
        }
        if (getRatio3MonthTk() % 1 == 0) {
            return String.valueOf(Math.round(getRatio3MonthTk()));
        }
        return String.valueOf(getRatio3MonthTk());
    }

    public String getDisplayRatio3MonthLk() {
        if (getRatio3MonthLk() == 0) {
            return "0";
        }
        if (getRatio3MonthLk() % 1 == 0) {
            return String.valueOf(Math.round(getRatio3MonthLk()));
        }
        return String.valueOf(getRatio3MonthLk());
    }

}

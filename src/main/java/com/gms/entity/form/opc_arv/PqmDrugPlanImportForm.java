package com.gms.entity.form.opc_arv;

import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class PqmDrugPlanImportForm extends BaseEntity {

    private String month;

    private String year;

    private String provinceID;

    private String siteCode;

    private String siteName;

    private String source;

    private String drugName;

    private String packing;

    private String unit;

    private String lotNumber;

    private String expiryDate;

    private String beginning;

    private String inThePeriod;

    private String patient;

    private String transfer;

    private String loss;

    private String ending;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
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

    public String getBeginning() {
        return beginning;
    }

    public void setBeginning(String beginning) {
        this.beginning = beginning;
    }

    public String getInThePeriod() {
        return inThePeriod;
    }

    public void setInThePeriod(String inThePeriod) {
        this.inThePeriod = inThePeriod;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getEnding() {
        return ending;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }

}

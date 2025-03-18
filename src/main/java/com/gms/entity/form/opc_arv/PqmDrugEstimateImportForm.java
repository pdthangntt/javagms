package com.gms.entity.form.opc_arv;

import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class PqmDrugEstimateImportForm extends BaseEntity {

    private int index;
    private int key;
    private String year;
    private String provinceID;
    private String siteCode;
    private String siteName;
    private String source;
    private String drugName;
    private String packing;
    private String howToUse;
    private String earlyEstimate;
    private String inEstimate;
    private String finalEstimate;
    private String exigencyTotal;
    private String exigency0;
    private String exigency1;
    private String exigency2;
    private String exigency3;
    private int exigencyRow;

    public int getExigencyRow() {
        return exigencyRow;
    }

    public void setExigencyRow(int exigencyRow) {
        this.exigencyRow = exigencyRow;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public String getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(String howToUse) {
        this.howToUse = howToUse;
    }

    public String getEarlyEstimate() {
        return earlyEstimate;
    }

    public void setEarlyEstimate(String earlyEstimate) {
        this.earlyEstimate = earlyEstimate;
    }

    public String getInEstimate() {
        return inEstimate;
    }

    public void setInEstimate(String inEstimate) {
        this.inEstimate = inEstimate;
    }

    public String getFinalEstimate() {
        return finalEstimate;
    }

    public void setFinalEstimate(String finalEstimate) {
        this.finalEstimate = finalEstimate;
    }

    public String getExigencyTotal() {
        return exigencyTotal;
    }

    public void setExigencyTotal(String exigencyTotal) {
        this.exigencyTotal = exigencyTotal;
    }

    public String getExigency0() {
        return exigency0;
    }

    public void setExigency0(String exigency0) {
        this.exigency0 = exigency0;
    }

    public String getExigency1() {
        return exigency1;
    }

    public void setExigency1(String exigency1) {
        this.exigency1 = exigency1;
    }

    public String getExigency2() {
        return exigency2;
    }

    public void setExigency2(String exigency2) {
        this.exigency2 = exigency2;
    }

    public String getExigency3() {
        return exigency3;
    }

    public void setExigency3(String exigency3) {
        this.exigency3 = exigency3;
    }

}

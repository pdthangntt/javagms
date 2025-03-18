package com.gms.entity.form.report;

import com.gms.entity.form.BaseForm;

/**
 *
 * @author othoa
 */
public class PqmDrugPlanForm extends BaseForm {

    private String drugName;
    private String unit;
    private String siteCode;
    private String siteName;
    private Long totalEndPeriod;
    private Long d1;
    private Long d2;
    private Long d3;

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

    public Long getTotalEndPeriod() {
        return totalEndPeriod;
    }

    public void setTotalEndPeriod(Long totalEndPeriod) {
        this.totalEndPeriod = totalEndPeriod;
    }

    public Long getD1() {
        return d1;
    }

    public void setD1(Long d1) {
        this.d1 = d1;
    }

    public Long getD2() {
        return d2;
    }

    public void setD2(Long d2) {
        this.d2 = d2;
    }

    public Long getD3() {
        return d3;
    }

    public void setD3(Long d3) {
        this.d3 = d3;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

}

package com.gms.entity.form.htc;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class HtcTransferTreatmentForm implements Serializable {
    private String exchangeConsultTime;
    private String partnerProvideResult;
    private String permanentProvinceID;
    private String permanentDistrictID;
    private String arrivalSiteID;
    private String arrivalSite;
    private String exchangeTime;
    private String resultsPatienTime;

    public String getResultsPatienTime() {
        return resultsPatienTime;
    }

    public void setResultsPatienTime(String resultsPatienTime) {
        this.resultsPatienTime = resultsPatienTime;
    }

    public String getExchangeConsultTime() {
        return exchangeConsultTime;
    }

    public void setExchangeConsultTime(String exchangeConsultTime) {
        this.exchangeConsultTime = exchangeConsultTime;
    }

    public String getPartnerProvideResult() {
        return partnerProvideResult;
    }

    public void setPartnerProvideResult(String partnerProvideResult) {
        this.partnerProvideResult = partnerProvideResult;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getPermanentDistrictID() {
        return permanentDistrictID;
    }

    public void setPermanentDistrictID(String permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public String getArrivalSiteID() {
        return arrivalSiteID;
    }

    public void setArrivalSiteID(String arrivalSiteID) {
        this.arrivalSiteID = arrivalSiteID;
    }

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }
    
    
    
}

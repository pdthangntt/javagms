package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author pdThang
 */
public class PqmVctSearch {

    private Set<Long> siteID;
    private String keyword;
    private String tab;
    private String genderID;
    private String objectGroupID;
    private String confirmTimeFrom;
    private String confirmTimeTo;
    private String earlyHivDateFrom;
    private String earlyHivDateTo;
    private String exchangeTimeFrom;
    private String exchangeTimeTo;
    private String registerTherapyTimeFrom;
    private String registerTherapyTimeTo;
    private String registerTherapyStatus;

    private String earlyDiagnose;

    //Phân trang
    private int pageIndex;
    private int pageSize;

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getEarlyHivDateFrom() {
        return earlyHivDateFrom;
    }

    public void setEarlyHivDateFrom(String earlyHivDateFrom) {
        this.earlyHivDateFrom = earlyHivDateFrom;
    }

    public String getEarlyHivDateTo() {
        return earlyHivDateTo;
    }

    public void setEarlyHivDateTo(String earlyHivDateTo) {
        this.earlyHivDateTo = earlyHivDateTo;
    }

    public String getExchangeTimeFrom() {
        return exchangeTimeFrom;
    }

    public void setExchangeTimeFrom(String exchangeTimeFrom) {
        this.exchangeTimeFrom = exchangeTimeFrom;
    }

    public String getExchangeTimeTo() {
        return exchangeTimeTo;
    }

    public void setExchangeTimeTo(String exchangeTimeTo) {
        this.exchangeTimeTo = exchangeTimeTo;
    }

    public String getRegisterTherapyTimeFrom() {
        return registerTherapyTimeFrom;
    }

    public void setRegisterTherapyTimeFrom(String registerTherapyTimeFrom) {
        this.registerTherapyTimeFrom = registerTherapyTimeFrom;
    }

    public String getRegisterTherapyTimeTo() {
        return registerTherapyTimeTo;
    }

    public void setRegisterTherapyTimeTo(String registerTherapyTimeTo) {
        this.registerTherapyTimeTo = registerTherapyTimeTo;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Set<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(Set<Long> siteID) {
        this.siteID = siteID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getConfirmTimeFrom() {
        return confirmTimeFrom;
    }

    public String getRegisterTherapyStatus() {
        return registerTherapyStatus;
    }

    public void setRegisterTherapyStatus(String registerTherapyStatus) {
        this.registerTherapyStatus = registerTherapyStatus;
    }

   

    public void setConfirmTimeFrom(String confirmTimeFrom) {
        this.confirmTimeFrom = confirmTimeFrom;
    }

    public String getConfirmTimeTo() {
        return confirmTimeTo;
    }

    public void setConfirmTimeTo(String confirmTimeTo) {
        this.confirmTimeTo = confirmTimeTo;
    }

}

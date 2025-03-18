package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author pdThang
 */
public class PqmVctRecencySearch {

    private Set<Long> siteID;
    private String keyword;
    private String tab;
    private String genderID;
    private Long provinceID;
    private String objectGroupID;
    private String earlyHivDateFrom;
    private String earlyHivDateTo;
    private String earlyDiagnose;

    //Phân trang
    private int pageIndex;
    private int pageSize;

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

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
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

    public String getEarlyHivDateFrom() {
        return earlyHivDateFrom;
    }

    public void setEarlyHivDateFrom(String earlyHivDateFrom) {
        this.earlyHivDateFrom = earlyHivDateFrom;
    }

    public Long getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(Long provinceID) {
        this.provinceID = provinceID;
    }

    public String getEarlyHivDateTo() {
        return earlyHivDateTo;
    }

    public void setEarlyHivDateTo(String earlyHivDateTo) {
        this.earlyHivDateTo = earlyHivDateTo;
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

}

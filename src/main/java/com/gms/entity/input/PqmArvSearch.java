package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author pdThang
 */
public class PqmArvSearch {

    private Set<Long> siteID;
    private String keyword;
    private String genderID;
    private String objectGroupID;
    private String statusOfTreatmentID;
    private String treatmentTimeFrom;
    private String treatmentTimeTo;

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

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getTreatmentTimeFrom() {
        return treatmentTimeFrom;
    }

    public void setTreatmentTimeFrom(String treatmentTimeFrom) {
        this.treatmentTimeFrom = treatmentTimeFrom;
    }

    public String getTreatmentTimeTo() {
        return treatmentTimeTo;
    }

    public void setTreatmentTimeTo(String treatmentTimeTo) {
        this.treatmentTimeTo = treatmentTimeTo;
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

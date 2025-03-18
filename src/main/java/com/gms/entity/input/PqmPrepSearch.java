package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author pdThang
 */
public class PqmPrepSearch {

    private Set<Long> siteID;
    private String keyword;
    private String genderID;
    private String objectGroupID;
    private String type;
    private String startTimeFrom;
    private String startTimeTo;
    private String examinationTimeFrom;
    private String examinationTimeTo;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTimeFrom() {
        return startTimeFrom;
    }

    public void setStartTimeFrom(String startTimeFrom) {
        this.startTimeFrom = startTimeFrom;
    }

    public String getStartTimeTo() {
        return startTimeTo;
    }

    public void setStartTimeTo(String startTimeTo) {
        this.startTimeTo = startTimeTo;
    }

    public String getExaminationTimeFrom() {
        return examinationTimeFrom;
    }

    public void setExaminationTimeFrom(String examinationTimeFrom) {
        this.examinationTimeFrom = examinationTimeFrom;
    }

    public String getExaminationTimeTo() {
        return examinationTimeTo;
    }

    public void setExaminationTimeTo(String examinationTimeTo) {
        this.examinationTimeTo = examinationTimeTo;
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

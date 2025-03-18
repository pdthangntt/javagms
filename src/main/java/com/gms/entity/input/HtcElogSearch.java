package com.gms.entity.input;

import java.util.Set;
import org.springframework.data.domain.Sort;

/**
 *
 * @author pdThang
 */
public class HtcElogSearch {

    private Long ID;
    private Set<Long> siteID;
    //Phân trang
    private int pageIndex;
    private int pageSize;
    private Sort sortable;

    //pram
    private String advisoryeTimeFrom;
    private String advisoryeTimeTo;
    private String service;
    private String resultID;

    public String getAdvisoryeTimeFrom() {
        return advisoryeTimeFrom;
    }

    public void setAdvisoryeTimeFrom(String advisoryeTimeFrom) {
        this.advisoryeTimeFrom = advisoryeTimeFrom;
    }

    public String getAdvisoryeTimeTo() {
        return advisoryeTimeTo;
    }

    public void setAdvisoryeTimeTo(String advisoryeTimeTo) {
        this.advisoryeTimeTo = advisoryeTimeTo;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Set<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(Set<Long> siteID) {
        this.siteID = siteID;
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

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }

}

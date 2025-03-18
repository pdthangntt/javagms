package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author pdThang
 */
public class VNPTConfigSearch {

    private Long ID;
    private String vnptSiteID;
    private String active;
    private Long site;
    private Set<Long> siteID;

    private int pageIndex;
    private int pageSize;

    public Long getSite() {
        return site;
    }

    public void setSite(Long site) {
        this.site = site;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getVnptSiteID() {
        return vnptSiteID;
    }

    public void setVnptSiteID(String vnptSiteID) {
        this.vnptSiteID = vnptSiteID;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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

}

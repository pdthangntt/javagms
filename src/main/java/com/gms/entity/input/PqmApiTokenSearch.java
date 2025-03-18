package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author pdThang
 */
public class PqmApiTokenSearch {

    private Long siteID;
    //Phân trang
    private int pageIndex;
    private int pageSize;

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
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

package com.gms.entity.input;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author pdThang
 */
public class OpcReceiveSearch {

    private Long sourceSiteID;
    private Long currentSiteID;
    private String code;
    private String name;
    private String confirmTimeFrom;
    private String confirmTimeTo;
    private String confirmTime;
    private String confirmCode;
    private String tranferTime;
    private String status;

    private boolean remove;

    //Phân trang
    private int pageIndex;
    private int pageSize;

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }
    
    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = StringUtils.isNotEmpty(confirmTime) ? confirmTime.trim() : confirmTime;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = StringUtils.isNotEmpty(confirmCode) ? confirmCode.trim() : confirmCode;
    }

    public Long getCurrentSiteID() {
        return currentSiteID;
    }

    public void setCurrentSiteID(Long currentSiteID) {
        this.currentSiteID = currentSiteID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = StringUtils.isNotEmpty(code) ? code.trim() : code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.isNotEmpty(name) ? name.trim() : name;
    }

    public String getConfirmTimeFrom() {
        return confirmTimeFrom;
    }

    public void setConfirmTimeFrom(String confirmTimeFrom) {
        this.confirmTimeFrom = StringUtils.isNotEmpty(confirmTimeFrom) ? confirmTimeFrom.trim() : confirmTimeFrom;
    }

    public String getConfirmTimeTo() {
        return confirmTimeTo;
    }

    public void setConfirmTimeTo(String confirmTimeTo) {
        this.confirmTimeTo = StringUtils.isNotEmpty(confirmTimeTo) ? confirmTimeTo.trim() : confirmTimeTo;
    }

    public String getTranferTime() {
        return tranferTime;
    }

    public void setTranferTime(String tranferTime) {
        this.tranferTime = StringUtils.isNotEmpty(tranferTime) ? tranferTime.trim() : tranferTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = StringUtils.isNotEmpty(status) ? status.trim() : status;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
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

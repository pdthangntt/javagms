package com.gms.entity.input;

import java.util.Set;
import org.springframework.data.domain.Sort;

/**
 *
 * @author pdThang
 */
public class LaytestSearch {

    private Long ID;
    private String name;
    private String code;
    private boolean remove;
    private int visitRemove;
    private String advisoryeTime;
    private String siteVisitID;
    private String status;
    private String sendStatus;
    private Long staffID;
    private Set<String> objectGroupID;
    private String advisoryeTimeFrom; //Ngày tư vấn từ
    private String advisoryeTimeTo; //Ngày tư vấn đến
    private String confirmTimeFrom; 
    private String confirmTimeTo; 
    private String exchangeTimeFrom; 
    private String exchangeTimeTo; 
    private boolean sampleSentDate;
    Set<Long> siteID;

    public Set<String> getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(Set<String> objectGroupID) {
        this.objectGroupID = objectGroupID;
    }
    
    public String getConfirmTimeFrom() {
        return confirmTimeFrom;
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
    
    public int getVisitRemove() {
        return visitRemove;
    }

    public void setVisitRemove(int visitRemove) {
        this.visitRemove = visitRemove;
    }

    public Set<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(Set<Long> siteID) {
        this.siteID = siteID;
    }
    
    public boolean isSampleSentDate() {
        return sampleSentDate;
    }

    public void setSampleSentDate(boolean sampleSentDate) {
        this.sampleSentDate = sampleSentDate;
    }
    
    
    
    //Phân trang
    private int pageIndex;
    private int pageSize;
    private Sort sortable;

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

   

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(String advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }
    
    

    public String getSiteVisitID() {
        return siteVisitID;
    }

    public void setSiteVisitID(String siteVisitID) {
        this.siteVisitID = siteVisitID;
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

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

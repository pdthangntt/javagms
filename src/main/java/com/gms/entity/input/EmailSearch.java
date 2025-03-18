package com.gms.entity.input;

import org.springframework.data.domain.Sort;

/**
 *
 * @author TrangBN
 */
public class EmailSearch {

    private String subject;
    private String from;
    private String to;
    private String sendAtFrom;
    private String sendAtTo;
    private int tab;

    //Phân trang
    private int pageIndex;
    private int pageSize;

    private Sort sortable;

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSendAtFrom() {
        return sendAtFrom;
    }

    public void setSendAtFrom(String sendAtFrom) {
        this.sendAtFrom = sendAtFrom;
    }

    public String getSendAtTo() {
        return sendAtTo;
    }

    public void setSendAtTo(String sendAtTo) {
        this.sendAtTo = sendAtTo;
    }

}

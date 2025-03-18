package com.gms.entity.form.htc_confirm;

import java.util.HashMap;
import java.util.List;

/**
 * Trả lời kết quả cho cơ sở xét nghiệm
 *
 * @author pdThang
 */
public class AnswerResultForm {

    private String title;
    private String fileName;

    private Long siteID;
    private String siteName;//teen dv
    private String siteAgency;
    private String siteAddress;//dia chi
    private String sitePhone;
    private String sourceSiteID;
    private String acceptTime;
    private String testTime;
    private HashMap<String, String> config;
    private List<AnswerResultTableForm> listAnswer;
    private List<TicketResultForm> tickets;

    public HashMap<String, String> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, String> config) {
        this.config = config;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public List<TicketResultForm> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResultForm> tickets) {
        this.tickets = tickets;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSitePhone() {
        return sitePhone;
    }

    public void setSitePhone(String sitePhone) {
        this.sitePhone = sitePhone;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public List<AnswerResultTableForm> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(List<AnswerResultTableForm> listAnswer) {
        this.listAnswer = listAnswer;
    }

}

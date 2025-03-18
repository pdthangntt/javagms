package com.gms.entity.form.htc;

import java.util.HashMap;
import java.util.List;

/**
 * Form lưu thông tin gửi mẫu XN
 * 
 * @author TrangBN
 */
public class TicketSampleSentForm {
    
    // File information
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private HashMap<String, String> siteOptions;
    HashMap<String, List<SampleSentTableForm>> listSampleSent;

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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public HashMap<String, List<SampleSentTableForm>> getListSampleSent() {
        return listSampleSent;
    }

    public void setListSampleSent(HashMap<String, List<SampleSentTableForm>> listSampleSent) {
        this.listSampleSent = listSampleSent;
    }

    public HashMap<String, String> getSiteOptions() {
        return siteOptions;
    }

    public void setSiteOptions(HashMap<String, String> siteOptions) {
        this.siteOptions = siteOptions;
    }

}

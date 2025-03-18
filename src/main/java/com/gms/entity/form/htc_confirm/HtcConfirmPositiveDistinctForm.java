package com.gms.entity.form.htc_confirm;

import java.util.List;

/**
 *
 * @author NamAnh_HaUI
 */
public class HtcConfirmPositiveDistinctForm {
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String start;
    private String end;
    private Long siteID;
    private String siteProvince;
    private List<HtcConfirmPositiveDistinctTableForm> table;

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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public List<HtcConfirmPositiveDistinctTableForm> getTable() {
        return table;
    }

    public void setTable(List<HtcConfirmPositiveDistinctTableForm> table) {
        this.table = table;
    }

}

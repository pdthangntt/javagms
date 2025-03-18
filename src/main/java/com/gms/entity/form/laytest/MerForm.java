package com.gms.entity.form.laytest;

import java.util.List;
import java.util.Set;

/**
 *
 * @author NamAnh_HaUI
 */
public class MerForm {
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String siteDistrict;
    private String siteWard;
    private String startDate;
    private String endDate;
    private List<MerTableForm> table01Forms;
    private List<MerTableForm> table02Forms;

    public String getSiteDistrict() {
        return siteDistrict;
    }

    public void setSiteDistrict(String siteDistrict) {
        this.siteDistrict = siteDistrict;
    }

    public String getSiteWard() {
        return siteWard;
    }

    public void setSiteWard(String siteWard) {
        this.siteWard = siteWard;
    }

    public List<MerTableForm> getTable01Forms() {
        return table01Forms;
    }

    public void setTable01Forms(List<MerTableForm> table01Forms) {
        this.table01Forms = table01Forms;
    }

    public List<MerTableForm> getTable02Forms() {
        return table02Forms;
    }

    public void setTable02Forms(List<MerTableForm> table02Forms) {
        this.table02Forms = table02Forms;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}

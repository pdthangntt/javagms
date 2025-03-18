package com.gms.entity.form.pac;

import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.input.PacPatientAcceptSearch;
import com.gms.entity.input.PacPatientNewSearch;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PQMForm {

    // File information
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String confirmTime;
    private String permanentWardName;
    private String permanentDistrictName;

    private List<PQMTable01Form> table01;

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

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getPermanentWardName() {
        return permanentWardName;
    }

    public void setPermanentWardName(String permanentWardName) {
        this.permanentWardName = permanentWardName;
    }

    public String getPermanentDistrictName() {
        return permanentDistrictName;
    }

    public void setPermanentDistrictName(String permanentDistrictName) {
        this.permanentDistrictName = permanentDistrictName;
    }

    public List<PQMTable01Form> getTable01() {
        return table01;
    }

    public void setTable01(List<PQMTable01Form> table01) {
        this.table01 = table01;
    }

}

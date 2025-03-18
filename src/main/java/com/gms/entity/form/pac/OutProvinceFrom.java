package com.gms.entity.form.pac;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.form.BaseForm;
import com.gms.entity.input.PacOutProvinceSearch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pdThang
 */
public class OutProvinceFrom extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String tab;
    private String confirmTime;
    private DataPage<PacPatientInfoEntity> dataPage;
    private PacOutProvinceSearch search;
    HashMap<String, HashMap<String, String>> options;

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public DataPage<PacPatientInfoEntity> getDataPage() {
        return dataPage;
    }

    public void setDataPage(DataPage<PacPatientInfoEntity> dataPage) {
        this.dataPage = dataPage;
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

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public PacOutProvinceSearch getSearch() {
        return search;
    }

    public void setSearch(PacOutProvinceSearch search) {
        this.search = search;
    }

}

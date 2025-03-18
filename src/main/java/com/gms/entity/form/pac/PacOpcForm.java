package com.gms.entity.form.pac;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.input.PacPatientNewSearch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pdThang
 */
public class PacOpcForm {

    // File information
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private boolean vaac;

    private Map<String, String> hivCodeOptions;
    private Map<String, String> connected;
    private HashMap<String, HashMap<String, String>> options;
    private DataPage<PacPatientInfoEntity> dataPage;

    public Map<String, String> getHivCodeOptions() {
        return hivCodeOptions;
    }

    public void setHivCodeOptions(Map<String, String> hivCodeOptions) {
        this.hivCodeOptions = hivCodeOptions;
    }

    public boolean isVaac() {
        return vaac;
    }

    public void setVaac(boolean vaac) {
        this.vaac = vaac;
    }

    public Map<String, String> getConnected() {
        return connected;
    }

    public void setConnected(Map<String, String> connected) {
        this.connected = connected;
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

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
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

}

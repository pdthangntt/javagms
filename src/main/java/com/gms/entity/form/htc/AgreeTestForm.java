package com.gms.entity.form.htc;

import com.gms.entity.db.HtcVisitEntity;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class AgreeTestForm {

    private String title;
    private String fileName;

    private Long siteID;
    private String siteName;//teen dv
    private String siteAgency;
    private String siteAddress;//dia chi
    private String siteProvince;
    private String patientCodeDsp;
    private List<HtcVisitEntity> entitys;
    private HashMap<String, String> config;

    public HashMap<String, String> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, String> config) {
        this.config = config;
    }

    public String getPatientCodeDsp() {
        return patientCodeDsp;
    }

    public void setPatientCodeDsp(String patientCodeDsp) {
        this.patientCodeDsp = patientCodeDsp;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
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

    public List<HtcVisitEntity> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<HtcVisitEntity> entitys) {
        this.entitys = entitys;
    }

}

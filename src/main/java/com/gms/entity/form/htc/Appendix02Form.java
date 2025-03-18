/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.htc;

import com.gms.entity.db.HtcVisitEntity;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Appendix02Form {
    private String title;
    private String fileName;
    private String shortName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private HashMap<String, String> siteOptions;
    List<HtcVisitEntity> listPatient;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public HashMap<String, String> getSiteOptions() {
        return siteOptions;
    }

    public void setSiteOptions(HashMap<String, String> siteOptions) {
        this.siteOptions = siteOptions;
    }

    public List<HtcVisitEntity> getListPatient() {
        return listPatient;
    }

    public void setListPatient(List<HtcVisitEntity> listPatient) {
        this.listPatient = listPatient;
    }
    
    
}

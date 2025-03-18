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
public class AgreeDiscloseForm {
    private String title;
    private String fileName;

    private Long siteID;
    private String siteName;//teen dv
    private String siteAgency;
    private String siteAddress;//dia chi
    private String siteProvince;
    private HtcVisitEntity entity;

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

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public HtcVisitEntity getEntity() {
        return entity;
    }

    public void setEntity(HtcVisitEntity entity) {
        this.entity = entity;
    }

    
    
}

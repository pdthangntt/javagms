package com.gms.entity.form;

import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PqmIndexTable implements Serializable {

    private String service;
    private String imported;
    private String siteImported;
    private String notImport;
    private String siteNotImported;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getImported() {
        return imported;
    }

    public void setImported(String imported) {
        this.imported = imported;
    }

    public String getSiteImported() {
        return siteImported;
    }

    public void setSiteImported(String siteImported) {
        this.siteImported = siteImported;
    }

    public String getNotImport() {
        return notImport;
    }

    public void setNotImport(String notImport) {
        this.notImport = notImport;
    }

    public String getSiteNotImported() {
        return siteNotImported;
    }

    public void setSiteNotImported(String siteNotImported) {
        this.siteNotImported = siteNotImported;
    }

}

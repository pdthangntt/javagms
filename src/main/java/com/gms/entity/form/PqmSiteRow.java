package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmSiteRow extends BaseEntity implements Serializable {

    //Trường cơ bản prep
    private Long ID;
    private String name;
    private String service;
    private String hub;
    private String hubSiteCode;
    private String pqmSiteCode;
    private String province;
    private String district;
    private List<String> services;

    private String elogSiteCode;
    private String arvSiteCode;
    private String prepSiteCode;
    private String hmedSiteCode;

    public String getElogSiteCode() {
        return elogSiteCode;
    }

    public void setElogSiteCode(String elogSiteCode) {
        this.elogSiteCode = elogSiteCode;
    }

    public String getArvSiteCode() {
        return arvSiteCode;
    }

    public void setArvSiteCode(String arvSiteCode) {
        this.arvSiteCode = arvSiteCode;
    }

    public String getPrepSiteCode() {
        return prepSiteCode;
    }

    public void setPrepSiteCode(String prepSiteCode) {
        this.prepSiteCode = prepSiteCode;
    }

    public String getHmedSiteCode() {
        return hmedSiteCode;
    }

    public void setHmedSiteCode(String hmedSiteCode) {
        this.hmedSiteCode = hmedSiteCode;
    }

    public String getPqmSiteCode() {
        return pqmSiteCode;
    }

    public void setPqmSiteCode(String pqmSiteCode) {
        this.pqmSiteCode = pqmSiteCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getHubSiteCode() {
        return hubSiteCode;
    }

    public void setHubSiteCode(String hubSiteCode) {
        this.hubSiteCode = hubSiteCode;
    }

}

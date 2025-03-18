/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author Admin
 */
public class HtcMERSearch {
    private String flag;
    private Long siteID;
    private String earlyHiv;
    private String genderID;
    private Integer fromAge;
    private Integer toAge;
    private String fromTime; 
    private String toTime; 
    private Set<String> objectGroupIDs; 
    private Set<String> serviceIDs; 
    private String serviceID; 
    //Bảng 3
    private String objectGroupID; 
     //Bảng 5
    private String confirmResultsID;

    public Set<String> getServiceIDs() {
        return serviceIDs;
    }

    public void setServiceIDs(Set<String> serviceIDs) {
        this.serviceIDs = serviceIDs;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public Integer getFromAge() {
        return fromAge;
    }

    public void setFromAge(Integer fromAge) {
        this.fromAge = fromAge;
    }

    public Integer getToAge() {
        return toAge;
    }

    public void setToAge(Integer toAge) {
        this.toAge = toAge;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public Set<String> getObjectGroupIDs() {
        return objectGroupIDs;
    }

    public void setObjectGroupIDs(Set<String> objectGroupIDs) {
        this.objectGroupIDs = objectGroupIDs;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
    
    
    
}

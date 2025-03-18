/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.htc;

import java.io.Serializable;

/**
 *
 * @author DSNAnh
 */
public class MerTable02Form  implements Serializable  {
    private String siteName; // Mã cơ sở
    private String siteID; // Mã cơ sở
    private String districtID; // Mã huyện
    private String serviceID; // Loại hình dịch vụ
    private String earlyHiv; // Kết quả xét nghiệm nhiễm mới
    private String genderID; // Giới tính
    private int unDefinedAge; // Không xác định
    private int underFifteen; // Dưới 1 tuổi 
    private int fifteenToNineteen; // 15 - 19
    private int twentyToTwentyFour; // 20 - 24
    private int twentyFiveToTwentyNine; // 25 - 29
    private int thirtyToThirtyFour; // 30 - 34
    private int thirtyFiveToThirtyNine; // 35 - 39
    private int fortyToFortyFour; // 40 - 44
    private int fortyFiveToFortyNine; // 45 - 49
    private int overFifty; // Trên 50

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
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

    public int getUnDefinedAge() {
        return unDefinedAge;
    }

    public void setUnDefinedAge(int unDefinedAge) {
        this.unDefinedAge = unDefinedAge;
    }

    public int getUnderFifteen() {
        return underFifteen;
    }

    public void setUnderFifteen(int underFifteen) {
        this.underFifteen = underFifteen;
    }

    public int getFifteenToNineteen() {
        return fifteenToNineteen;
    }

    public void setFifteenToNineteen(int fifteenToNineteen) {
        this.fifteenToNineteen = fifteenToNineteen;
    }

    public int getTwentyToTwentyFour() {
        return twentyToTwentyFour;
    }

    public void setTwentyToTwentyFour(int twentyToTwentyFour) {
        this.twentyToTwentyFour = twentyToTwentyFour;
    }

    public int getTwentyFiveToTwentyNine() {
        return twentyFiveToTwentyNine;
    }

    public void setTwentyFiveToTwentyNine(int twentyFiveToTwentyNine) {
        this.twentyFiveToTwentyNine = twentyFiveToTwentyNine;
    }

    public int getThirtyToThirtyFour() {
        return thirtyToThirtyFour;
    }

    public void setThirtyToThirtyFour(int thirtyToThirtyFour) {
        this.thirtyToThirtyFour = thirtyToThirtyFour;
    }

    public int getThirtyFiveToThirtyNine() {
        return thirtyFiveToThirtyNine;
    }

    public void setThirtyFiveToThirtyNine(int thirtyFiveToThirtyNine) {
        this.thirtyFiveToThirtyNine = thirtyFiveToThirtyNine;
    }

    public int getFortyToFortyFour() {
        return fortyToFortyFour;
    }

    public void setFortyToFortyFour(int fortyToFortyFour) {
        this.fortyToFortyFour = fortyToFortyFour;
    }

    public int getFortyFiveToFortyNine() {
        return fortyFiveToFortyNine;
    }

    public void setFortyFiveToFortyNine(int fortyFiveToFortyNine) {
        this.fortyFiveToFortyNine = fortyFiveToFortyNine;
    }

    public int getOverFifty() {
        return overFifty;
    }

    public void setOverFifty(int overFifty) {
        this.overFifty = overFifty;
    }
    
    public int getSum() {
        return this != null ? 
                this.unDefinedAge 
                + this.underFifteen
                + this.fifteenToNineteen 
                + this.twentyToTwentyFour 
                + this.twentyFiveToTwentyNine
                + this.thirtyToThirtyFour
                + this.thirtyFiveToThirtyNine
                + this.fortyToFortyFour
                + this.fortyFiveToFortyNine
                + this.overFifty : 0;
    }
    
}

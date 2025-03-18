package com.gms.entity.form.htc;

import java.io.Serializable;

/**
 *  Bảng 04: Số khách hàng nhận dịch vụ HTC và quay lại nhận kết quả 
 *  theo nhóm giới tính và nhóm tuổi
 * 
 * @author TrangBN
 */
public class MerTable04Form implements Serializable {

    private String districtID; // Mã huyện
    private String serviceID; // Loại hình dịch vụ
    private String confirmResultsID; // Kết quả xét nghiệm khẳng định
    private String genderID; // Giới tính
    private String yearOfBirth; // Nam sinh
    private int unDefinedAge; // Không xác định
    private int underOneAge; // Dưới 1 tuổi 
    private int oneToFour; // 1 - 4
    private int fiveToNine; // 5 - 9
    private int tenToFourteen; // 10 - 14
    private int fifteenToNineteen; // 15 - 19
    private int twentyToTwentyFour; // 20 - 24
    private int twentyFiveToTwentyNine; // 25 - 29
    private int thirtyToThirtyFour; // 30 - 34
    private int thirtyFiveToThirtyNine; // 35 - 39
    private int fortyToFortyFour; // 40 - 44
    private int fortyFiveToFortyNine; // 45 - 49
    private int overFifty; // Trên 50

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
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

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
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

    public int getUnderOneAge() {
        return underOneAge;
    }

    public void setUnderOneAge(int underOneAge) {
        this.underOneAge = underOneAge;
    }

    public int getTenToFourteen() {
        return tenToFourteen;
    }

    public void setTenToFourteen(int tenToFourteen) {
        this.tenToFourteen = tenToFourteen;
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

    public int getOverFifty() {
        return overFifty;
    }

    public void setOverFifty(int overFifty) {
        this.overFifty = overFifty;
    }

    public int getOneToFour() {
        return oneToFour;
    }

    public void setOneToFour(int oneToFour) {
        this.oneToFour = oneToFour;
    }

    public int getFiveToNine() {
        return fiveToNine;
    }

    public void setFiveToNine(int fiveToNine) {
        this.fiveToNine = fiveToNine;
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
    
    public int getSum() {
        return this != null ? 
                this.unDefinedAge 
                + this.underOneAge 
                + this.oneToFour
                + this.fiveToNine
                + this.tenToFourteen 
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

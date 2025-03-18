package com.gms.entity.form.laytest;

/**
 *
 * @author NamAnh_HaUI
 */
public class MerTableForm {
    private int stt;
    private String age;
    private String oValue;
    private Long oID;
    private String oGroupID;
    private Long oParentID;
    private String oCode;
    private int position;
//    private String oCode;
    private int positiveMale;
    private int positiveFemale;
    private int positive;
    private int negativeMale;
    private int negativeFemale;
    private int negative;
    private int tong;

    public int getTong() {
        return tong;
    }

    public void setTong(int tong) {
        this.tong = tong;
    }
    
    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getoGroupID() {
        return oGroupID;
    }

    public void setoGroupID(String oGroupID) {
        this.oGroupID = oGroupID;
    }

    public Long getoID() {
        return oID;
    }

    public void setoID(Long oID) {
        this.oID = oID;
    }

    public Long getoParentID() {
        return oParentID;
    }

    public void setoParentID(Long oParentID) {
        this.oParentID = oParentID;
    }

    public String getoCode() {
        return oCode;
    }

    public void setoCode(String oCode) {
        this.oCode = oCode;
    }

    public String getoValue() {
        return oValue;
    }

    public void setoValue(String oValue) {
        this.oValue = oValue;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public int getPositiveMale() {
        return positiveMale;
    }

    public void setPositiveMale(int positiveMale) {
        this.positiveMale = positiveMale;
    }

    public int getPositiveFemale() {
        return positiveFemale;
    }

    public void setPositiveFemale(int positiveFemale) {
        this.positiveFemale = positiveFemale;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNegativeMale() {
        return negativeMale;
    }

    public void setNegativeMale(int negativeMale) {
        this.negativeMale = negativeMale;
    }

    public int getNegativeFemale() {
        return negativeFemale;
    }

    public void setNegativeFemale(int negativeFemale) {
        this.negativeFemale = negativeFemale;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }
    
}

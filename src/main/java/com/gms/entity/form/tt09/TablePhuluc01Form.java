package com.gms.entity.form.tt09;

import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class TablePhuluc01Form implements Serializable, Cloneable {

    private int stt;
    private String doituongxetnghiem;
    private int age0_15Tong;
    private int age0_15DuongTinh;
    private int age15_25Tong;
    private int age15_25DuongTinh;
    private int age25_49Tong;
    private int age25_49DuongTinh;
    private int age49_maxTong;
    private int age49_maxDuongTinh;
    private int age_noneDuongtinh;
    private int age_noneTong;
    private int genderMaleTong;
    private int genderMaleDuongTinh;
    private int genderFemaleTong;
    private int genderFemaleDuongTinh;
    private Long oID;
    private Long oParentID;
    private String oCode;
    private int position;
    private int tong;
    private int tongDuongTinh;

    public String getoCode() {
        return oCode;
    }

    public void setoCode(String oCode) {
        this.oCode = oCode;
    }

    public int getAge_noneDuongtinh() {
        return age_noneDuongtinh;
    }

    public void setAge_noneDuongtinh(int age_noneDuongtinh) {
        this.age_noneDuongtinh = age_noneDuongtinh;
    }

    public int getAge_noneTong() {
        return age_noneTong;
    }

    public void setAge_noneTong(int age_noneTong) {
        this.age_noneTong = age_noneTong;
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

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getDoituongxetnghiem() {
        return doituongxetnghiem;
    }

    public void setDoituongxetnghiem(String doituongxetnghiem) {
        this.doituongxetnghiem = doituongxetnghiem;
    }

    public int getAge0_15Tong() {
        return age0_15Tong;
    }

    public void setAge0_15Tong(int age0_15Tong) {
        this.age0_15Tong = age0_15Tong;
    }

    public int getAge0_15DuongTinh() {
        return age0_15DuongTinh;
    }

    public void setAge0_15DuongTinh(int age0_15DuongTinh) {
        this.age0_15DuongTinh = age0_15DuongTinh;
    }

    public int getAge15_25Tong() {
        return age15_25Tong;
    }

    public void setAge15_25Tong(int age15_25Tong) {
        this.age15_25Tong = age15_25Tong;
    }

    public int getAge15_25DuongTinh() {
        return age15_25DuongTinh;
    }

    public void setAge15_25DuongTinh(int age15_25DuongTinh) {
        this.age15_25DuongTinh = age15_25DuongTinh;
    }

    public int getAge25_49Tong() {
        return age25_49Tong;
    }

    public void setAge25_49Tong(int age25_49Tong) {
        this.age25_49Tong = age25_49Tong;
    }

    public int getAge25_49DuongTinh() {
        return age25_49DuongTinh;
    }

    public void setAge25_49DuongTinh(int age25_49DuongTinh) {
        this.age25_49DuongTinh = age25_49DuongTinh;
    }

    public int getAge49_maxTong() {
        return age49_maxTong;
    }

    public void setAge49_maxTong(int age49_maxTong) {
        this.age49_maxTong = age49_maxTong;
    }

    public int getAge49_maxDuongTinh() {
        return age49_maxDuongTinh;
    }

    public void setAge49_maxDuongTinh(int age49_maxDuongTinh) {
        this.age49_maxDuongTinh = age49_maxDuongTinh;
    }

    public int getGenderMaleTong() {
        return genderMaleTong;
    }

    public void setGenderMaleTong(int genderMaleTong) {
        this.genderMaleTong = genderMaleTong;
    }

    public int getGenderMaleDuongTinh() {
        return genderMaleDuongTinh;
    }

    public void setGenderMaleDuongTinh(int genderMaleDuongTinh) {
        this.genderMaleDuongTinh = genderMaleDuongTinh;
    }

    public int getGenderFemaleTong() {
        return genderFemaleTong;
    }

    public void setGenderFemaleTong(int genderFemaleTong) {
        this.genderFemaleTong = genderFemaleTong;
    }

    public int getGenderFemaleDuongTinh() {
        return genderFemaleDuongTinh;
    }

    public void setGenderFemaleDuongTinh(int genderFemaleDuongTinh) {
        this.genderFemaleDuongTinh = genderFemaleDuongTinh;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTong() {
        return age0_15Tong + age15_25Tong + age25_49Tong + age49_maxTong + age_noneTong;
    }

    public int getTongDuongTinh() {
        return age0_15DuongTinh + age15_25DuongTinh + age25_49DuongTinh + age49_maxDuongTinh + age_noneDuongtinh;
    }

    @Override
    public TablePhuluc01Form clone() throws CloneNotSupportedException {
        return (TablePhuluc01Form) super.clone();
    }

}

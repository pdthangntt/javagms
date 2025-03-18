package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * Form báo cáo xã
 *
 * @author pdThang
 */
public class PacWardTableForm implements Serializable, Cloneable {

    private String districtName;
    private String districtID;
    private String wardName;
    private String wardID;

    private int stt;
    private int hiv;
    private int aids;
    private int tv;

    private int under15;
    private int over15;
    private int totalAge;

    private int matDau;
    private int khongCoThucTe;
    private int chuyenDiNoiKhac;
    private int chuaXacDinhDuoc;
    private int khongCoThongTin;
    private int totalUnclear;

    private int chuyenTrongTinh;
    private int diLamAnXa;
    private int diTrai;
    private int dangODiaPhuong;
    private int totalManage;

    private int countWard;
    private boolean active;

    //map sql resident
    private String resident;
    private int count;

    public int getUnder15() {
        return under15;
    }

    public void setUnder15(int under15) {
        this.under15 = under15;
    }

    public int getOver15() {
        return over15;
    }

    public void setOver15(int over15) {
        this.over15 = over15;
    }

    public int getTotalAge() {
        return under15 + over15;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getChuaXacDinhDuoc() {
        return chuaXacDinhDuoc;
    }

    public int getCountWard() {
        return countWard;
    }

    public void setCountWard(int countWard) {
        this.countWard = countWard;
    }

    public void setChuaXacDinhDuoc(int chuaXacDinhDuoc) {
        this.chuaXacDinhDuoc = chuaXacDinhDuoc;
    }

    public int getKhongCoThongTin() {
        return khongCoThongTin;
    }

    public void setKhongCoThongTin(int khongCoThongTin) {
        this.khongCoThongTin = khongCoThongTin;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getHiv() {
        return hiv;
    }

    public void setHiv(int hiv) {
        this.hiv = hiv;
    }

    public int getAids() {
        return aids;
    }

    public void setAids(int aids) {
        this.aids = aids;
    }

    public int getTv() {
        return tv;
    }

    public void setTv(int tv) {
        this.tv = tv;
    }

    public int getMatDau() {
        return matDau;
    }

    public void setMatDau(int matDau) {
        this.matDau = matDau;
    }

    public int getKhongCoThucTe() {
        return khongCoThucTe;
    }

    public void setKhongCoThucTe(int khongCoThucTe) {
        this.khongCoThucTe = khongCoThucTe;
    }

    public int getChuyenDiNoiKhac() {
        return chuyenDiNoiKhac;
    }

    public void setChuyenDiNoiKhac(int chuyenDiNoiKhac) {
        this.chuyenDiNoiKhac = chuyenDiNoiKhac;
    }

    public int getTotalUnclear() {
        return this.matDau + this.khongCoThucTe + this.chuyenDiNoiKhac + this.chuaXacDinhDuoc + this.khongCoThongTin;
    }

    public int getChuyenTrongTinh() {
        return chuyenTrongTinh;
    }

    public void setChuyenTrongTinh(int chuyenTrongTinh) {
        this.chuyenTrongTinh = chuyenTrongTinh;
    }

    public int getDiLamAnXa() {
        return diLamAnXa;
    }

    public void setDiLamAnXa(int diLamAnXa) {
        this.diLamAnXa = diLamAnXa;
    }

    public int getDiTrai() {
        return diTrai;
    }

    public void setDiTrai(int diTrai) {
        this.diTrai = diTrai;
    }

    public int getDangODiaPhuong() {
        return dangODiaPhuong;
    }

    public void setDangODiaPhuong(int dangODiaPhuong) {
        this.dangODiaPhuong = dangODiaPhuong;
    }

    public int getTotalManage() {
        return this.chuyenTrongTinh + this.diLamAnXa + this.diTrai + this.dangODiaPhuong;
    }

    @Override
    public PacWardTableForm clone() throws CloneNotSupportedException {
        return (PacWardTableForm) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}

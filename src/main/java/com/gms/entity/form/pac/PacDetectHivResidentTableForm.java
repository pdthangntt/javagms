package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * Form báo cáo hiện trạng cư trú
 *
 *
 * @author thangPD
 */
public class PacDetectHivResidentTableForm implements Serializable, Cloneable {

    private String stt;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String displayName;
    
    private int matDau;
    private int dangODiaPhuong;
    private int khongCoThucTe;
    private int diTrai;
    private int chuyenDiNoiKhac;
    private int chuaXacDinhDuoc;
    private int diLamAnXa;
    private int chuyenTrongTinh;
    private int unclear;
    private int sum;
    
    private List<PacDetectHivResidentTableForm> childs;
    
    //%
    
    

    public String getPercentmatDau() {
        return TextUtils.toPercent(this.getMatDau(), this.getSum());
    }

    public String getPercentdangODiaPhuong() {
        return TextUtils.toPercent(this.getDangODiaPhuong(), this.getSum());
    }

    public String getPercentkhongCoThucTe() {
        return TextUtils.toPercent(this.getKhongCoThucTe(), this.getSum());
    }

    public String getPercentdiTrai() {
        return TextUtils.toPercent(this.getDiTrai(), this.getSum());
    }

    public String getPercentchuyenDiNoiKhac() {
        return TextUtils.toPercent(this.getChuyenDiNoiKhac(), this.getSum());
    }

    public String getPercentchuaXacDinhDuoc() {
       return TextUtils.toPercent(this.getChuaXacDinhDuoc(), this.getSum());
    }

    public String getPercentdiLamAnXa() {
        return TextUtils.toPercent(this.getDiLamAnXa(), this.getSum());
    }

    public String getPercentchuyenTrongTinh() {
        return TextUtils.toPercent(this.getChuyenTrongTinh(), this.getSum());
    }

    public String getPercentunclear() {
        return TextUtils.toPercent(this.getUnclear(), this.getSum());
    }

    public String getSumPercent(int total) {
        return TextUtils.toPercent(this.getSum(), total);
    }
    
   //end% 
    
    
    
    
    

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMatDau() {
        return matDau;
    }

    public void setMatDau(int matDau) {
        this.matDau = matDau;
    }

    public int getDangODiaPhuong() {
        return dangODiaPhuong;
    }

    public void setDangODiaPhuong(int dangODiaPhuong) {
        this.dangODiaPhuong = dangODiaPhuong;
    }

    public int getKhongCoThucTe() {
        return khongCoThucTe;
    }

    public void setKhongCoThucTe(int khongCoThucTe) {
        this.khongCoThucTe = khongCoThucTe;
    }

    public int getDiTrai() {
        return diTrai;
    }

    public void setDiTrai(int diTrai) {
        this.diTrai = diTrai;
    }

    public int getChuyenDiNoiKhac() {
        return chuyenDiNoiKhac;
    }

    public void setChuyenDiNoiKhac(int chuyenDiNoiKhac) {
        this.chuyenDiNoiKhac = chuyenDiNoiKhac;
    }

    public int getChuaXacDinhDuoc() {
        return chuaXacDinhDuoc;
    }

    public void setChuaXacDinhDuoc(int chuaXacDinhDuoc) {
        this.chuaXacDinhDuoc = chuaXacDinhDuoc;
    }

    public int getDiLamAnXa() {
        return diLamAnXa;
    }

    public void setDiLamAnXa(int diLamAnXa) {
        this.diLamAnXa = diLamAnXa;
    }

    public int getChuyenTrongTinh() {
        return chuyenTrongTinh;
    }

    public void setChuyenTrongTinh(int chuyenTrongTinh) {
        this.chuyenTrongTinh = chuyenTrongTinh;
    }

    public int getUnclear() {
        return unclear;
    }

    public void setUnclear(int unclear) {
        this.unclear = unclear;
    }

    public int getSum() {
        return  this.getDangODiaPhuong()
                + this.getDiLamAnXa()
                + this.getDiTrai()
                + this.getChuyenDiNoiKhac()
                + this.getChuyenTrongTinh()
                + this.getMatDau()
                + this.getChuaXacDinhDuoc()
                + this.getKhongCoThucTe()
                + this.getUnclear()
                ;
    }

    public List<PacDetectHivResidentTableForm> getChilds() {
        return childs;
    }

    public void setChilds(List<PacDetectHivResidentTableForm> childs) {
        this.childs = childs;
    }
    
    

    @Override
    public PacDetectHivResidentTableForm clone() throws CloneNotSupportedException {
        return (PacDetectHivResidentTableForm) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}

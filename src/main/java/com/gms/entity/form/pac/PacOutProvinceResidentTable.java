package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceResidentTable extends BaseForm implements Serializable {

    public PacOutProvinceResidentTable() {
        matDau = 0;
        dangODiaPhuong = 0;
        khongCoThucTe = 0;
        diTrai = 0;
        chuyenDiNoiKhac = 0;
        chuaXacDinhDuoc = 0;
        diLamAnXa = 0;
        chuyenTrongTinh = 0;
        unclear = 0;
        sum = 0;

    }

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
        return this.getDangODiaPhuong()
                + this.getDiLamAnXa()
                + this.getDiTrai()
                + this.getChuyenDiNoiKhac()
                + this.getChuyenTrongTinh()
                + this.getMatDau()
                + this.getChuaXacDinhDuoc()
                + this.getKhongCoThucTe()
                + this.getUnclear();
    }

}

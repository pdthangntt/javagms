package com.gms.entity.xml.vpnt_arv;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vvthanh
 */
@XmlRootElement(name = "goi_du_lieu_kcb")
public class Main {

    private String ma_cskcb;
    private String tu_ngay;
    private String den_ngay;
    private String ngay;
    private String phien_ban;
    private String ung_dung;
    private String hoan_thanh;
    private DuLieuCapNhat du_lieu_cap_nhat;

    public String getMa_cskcb() {
        return ma_cskcb;
    }

    public void setMa_cskcb(String ma_cskcb) {
        this.ma_cskcb = ma_cskcb;
    }

    public String getTu_ngay() {
        return tu_ngay;
    }

    public void setTu_ngay(String tu_ngay) {
        this.tu_ngay = tu_ngay;
    }

    public String getDen_ngay() {
        return den_ngay;
    }

    public void setDen_ngay(String den_ngay) {
        this.den_ngay = den_ngay;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getPhien_ban() {
        return phien_ban;
    }

    public void setPhien_ban(String phien_ban) {
        this.phien_ban = phien_ban;
    }

    public String getUng_dung() {
        return ung_dung;
    }

    public void setUng_dung(String ung_dung) {
        this.ung_dung = ung_dung;
    }

    public String getHoan_thanh() {
        return hoan_thanh;
    }

    public void setHoan_thanh(String hoan_thanh) {
        this.hoan_thanh = hoan_thanh;
    }

    public DuLieuCapNhat getDu_lieu_cap_nhat() {
        return du_lieu_cap_nhat;
    }

    public void setDu_lieu_cap_nhat(DuLieuCapNhat du_lieu_cap_nhat) {
        this.du_lieu_cap_nhat = du_lieu_cap_nhat;
    }

}

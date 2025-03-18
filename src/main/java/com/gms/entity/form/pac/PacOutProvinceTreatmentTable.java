package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceTreatmentTable extends BaseForm implements Serializable {

    public PacOutProvinceTreatmentTable() {
        dangdieutri = 0;
        chuadieutri = 0;
        chodieutri = 0;
        bodieutri = 0;
        matdau = 0;
        tuvong = 0;
        chuacothongtin = 0;
        khongthongtin = 0;
        sum = 0;

    }

    private int dangdieutri;
    private int chuadieutri;
    private int chodieutri;
    private int bodieutri;
    private int matdau;
    private int tuvong;
    private int chuacothongtin;
    private int khongthongtin;
    private int sum;

    public int getSum() {
        return this.dangdieutri
                + this.chuadieutri
                + this.chodieutri
                + this.bodieutri
                + this.matdau
                + this.tuvong
                + this.chuacothongtin
                + this.khongthongtin;
    }

    public int getDangdieutri() {
        return dangdieutri;
    }

    public void setDangdieutri(int dangdieutri) {
        this.dangdieutri = dangdieutri;
    }

    public int getChuadieutri() {
        return chuadieutri;
    }

    public void setChuadieutri(int chuadieutri) {
        this.chuadieutri = chuadieutri;
    }

    public int getChodieutri() {
        return chodieutri;
    }

    public void setChodieutri(int chodieutri) {
        this.chodieutri = chodieutri;
    }

    public int getBodieutri() {
        return bodieutri;
    }

    public void setBodieutri(int bodieutri) {
        this.bodieutri = bodieutri;
    }

    public int getMatdau() {
        return matdau;
    }

    public void setMatdau(int matdau) {
        this.matdau = matdau;
    }

    public int getTuvong() {
        return tuvong;
    }

    public void setTuvong(int tuvong) {
        this.tuvong = tuvong;
    }

    public int getChuacothongtin() {
        return chuacothongtin;
    }

    public void setChuacothongtin(int chuacothongtin) {
        this.chuacothongtin = chuacothongtin;
    }

    public int getKhongthongtin() {
        return khongthongtin;
    }

    public void setKhongthongtin(int khongthongtin) {
        this.khongthongtin = khongthongtin;
    }

}

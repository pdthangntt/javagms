package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Admin
 */
public class TableDetectHivObjectForm implements Serializable, Cloneable {

    private String stt;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String displayName;
    private int ntmt;
    private int md;
    private int msm;
    private int vcbtbc;
    private int pnmt;
    private int lao;
    private int khac;
    private int khongthongtin;

    //thêm 17/11/2020
    private int hoalieu;
    private int phamnhan;
    private int mecon;
    private int nghiavu;

    private int sum;

    private List<TableDetectHivObjectForm> childs;

    public int getHoalieu() {
        return hoalieu;
    }

    public void setHoalieu(int hoalieu) {
        this.hoalieu = hoalieu;
    }

    public int getPhamnhan() {
        return phamnhan;
    }

    public void setPhamnhan(int phamnhan) {
        this.phamnhan = phamnhan;
    }

    public int getMecon() {
        return mecon;
    }

    public void setMecon(int mecon) {
        this.mecon = mecon;
    }

    public int getNghiavu() {
        return nghiavu;
    }

    public void setNghiavu(int nghiavu) {
        this.nghiavu = nghiavu;
    }

    public String getSumPercent(int total) {
        return TextUtils.toPercent(this.getSum(), total);
    }

    //%
    public String getPercentntmt() {
        return TextUtils.toPercent(this.getNtmt(), this.getSum());
    }

    public String getPercentmd() {
        return TextUtils.toPercent(this.getMd(), this.getSum());
    }

    public String getPercentmsm() {
        return TextUtils.toPercent(this.getMsm(), this.getSum());
    }

    public String getPercentvcbtbc() {
        return TextUtils.toPercent(this.getVcbtbc(), this.getSum());
    }

    public String getPercentpnmt() {
        return TextUtils.toPercent(this.getPnmt(), this.getSum());
    }

    public String getPercentlao() {
        return TextUtils.toPercent(this.getLao(), this.getSum());
    }

    public String getPercentkhac() {
        return TextUtils.toPercent(this.getKhac(), this.getSum());
    }

    public String getPercentkhongthongtin() {
        return TextUtils.toPercent(this.getKhongthongtin(), this.getSum());
    }

    public String getPercenthoalieu() {
        return TextUtils.toPercent(this.getHoalieu(), this.getSum());
    }

    public String getPercentphamnhan() {
        return TextUtils.toPercent(this.getPhamnhan(), this.getSum());
    }

    public String getPercentmecon() {
        return TextUtils.toPercent(this.getMecon(), this.getSum());
    }

    public String getPercentnghiavu() {
        return TextUtils.toPercent(this.getNghiavu(), this.getSum());
    }
    //%

    public int getSum() {
        return this.getNtmt()
                + this.getMd()
                + this.getMsm()
                + this.getVcbtbc()
                + this.getPnmt()
                + this.getLao()
                + this.getKhac()
                + this.getKhongthongtin()
                + this.getHoalieu()
                + this.getPhamnhan()
                + this.getMecon()
                + this.getNghiavu();
    }

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

    public int getNtmt() {
        return ntmt;
    }

    public void setNtmt(int ntmt) {
        this.ntmt = ntmt;
    }

    public int getMd() {
        return md;
    }

    public void setMd(int md) {
        this.md = md;
    }

    public int getMsm() {
        return msm;
    }

    public void setMsm(int msm) {
        this.msm = msm;
    }

    public int getVcbtbc() {
        return vcbtbc;
    }

    public void setVcbtbc(int vcbtbc) {
        this.vcbtbc = vcbtbc;
    }

    public int getPnmt() {
        return pnmt;
    }

    public void setPnmt(int pnmt) {
        this.pnmt = pnmt;
    }

    public int getLao() {
        return lao;
    }

    public void setLao(int lao) {
        this.lao = lao;
    }

    public int getKhac() {
        return khac;
    }

    public void setKhac(int khac) {
        this.khac = khac;
    }

    public int getKhongthongtin() {
        return khongthongtin;
    }

    public void setKhongthongtin(int khongthongtin) {
        this.khongthongtin = khongthongtin;
    }

    public List<TableDetectHivObjectForm> getChilds() {
        return childs;
    }

    public void setChilds(List<TableDetectHivObjectForm> childs) {
        this.childs = childs;
    }

    @Override
    public TableDetectHivObjectForm clone() throws CloneNotSupportedException {
        return (TableDetectHivObjectForm) super.clone();
    }
}

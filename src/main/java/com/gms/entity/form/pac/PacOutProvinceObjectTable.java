package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceObjectTable extends BaseForm implements Serializable {

    public PacOutProvinceObjectTable() {
        ntmt = 0;
        md = 0;
        msm = 0;
        vcbtbc = 0;
        pnmt = 0;
        lao = 0;
        khac = 0;
        khongthongtin = 0;
        sum = 0;

    }

    private int ntmt;
    private int md;
    private int msm;
    private int vcbtbc;
    private int pnmt;
    private int lao;
    private int khac;
    private int khongthongtin;
    private int sum;

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

    public int getSum() {
        return this.ntmt
                + this.md
                + this.msm
                + this.vcbtbc
                + this.pnmt
                + this.lao
                + this.khac
                + this.khongthongtin;

    }

}

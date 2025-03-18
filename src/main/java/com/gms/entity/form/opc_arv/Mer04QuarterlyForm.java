package com.gms.entity.form.opc_arv;

/**
 *
 * @author Admin
 */
public class Mer04QuarterlyForm {
    private int tong;
    private int ncmt;
    private int msm;
    private int pnmd;
    private int phamnhan;
    private int chuyengioi;
    private int hasChild;

    public Mer04QuarterlyForm() {
        ncmt = 0;
        msm = 0;
        pnmd = 0;
        phamnhan = 0;
        chuyengioi = 0;
        tong = 0;
        hasChild = 0;
    }

    public int getHasChild() {
        return hasChild;
    }

    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }

    public int getTong() {
        return tong;
    }

    public void setTong(int tong) {
        this.tong = tong;
    }

    public int getNcmt() {
        return ncmt;
    }

    public void setNcmt(int ncmt) {
        this.ncmt = ncmt;
    }

    public int getMsm() {
        return msm;
    }

    public void setMsm(int msm) {
        this.msm = msm;
    }

    public int getPnmd() {
        return pnmd;
    }

    public void setPnmd(int pnmd) {
        this.pnmd = pnmd;
    }

    public int getPhamnhan() {
        return phamnhan;
    }

    public void setPhamnhan(int phamnhan) {
        this.phamnhan = phamnhan;
    }

    public int getChuyengioi() {
        return chuyengioi;
    }

    public void setChuyengioi(int chuyengioi) {
        this.chuyengioi = chuyengioi;
    }

    
}

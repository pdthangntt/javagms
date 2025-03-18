package com.gms.entity.form.opc_arv;

/**
 * Theo dõi xn TLVR hiv
 *
 * @author vvthanh
 */
public class ViralloadTable {

    private int xnTrongThang;
    private int xnLan1;
    private int xnLan1Duoi200;
    private int xnLan1Tu200Den1000;
    private int xnLan1Tren1000;

    private int xnLan2;
    private int xnLan2Duoi200;
    private int xnLan2Tu200Den1000;
    private int xnLan2Tren1000;

    private int arvBac2;
    private int arvBac2Duoi200;
    private int arvBac2Tu200Den1000;
    private int arvBac2Tren1000;

    private int xn12ThangDuoi200;
    private int xn12ThangTu200Den1000;
    private int xn12ThangTren1000;

    public int getXnTrongThang() {
        return xnTrongThang;
    }

    public void setXnTrongThang(int xnTrongThang) {
        this.xnTrongThang = xnTrongThang;
    }

    public int getXnLan1() {
        return xnLan1Duoi200 + xnLan1Tu200Den1000 + xnLan1Tren1000;
    }

    public int getXnLan1Duoi200() {
        return xnLan1Duoi200;
    }

    public void setXnLan1Duoi200(int xnLan1Duoi200) {
        this.xnLan1Duoi200 = xnLan1Duoi200;
    }

    public int getXnLan1Tu200Den1000() {
        return xnLan1Tu200Den1000;
    }

    public void setXnLan1Tu200Den1000(int xnLan1Tu200Den1000) {
        this.xnLan1Tu200Den1000 = xnLan1Tu200Den1000;
    }

    public int getXnLan1Tren1000() {
        return xnLan1Tren1000;
    }

    public void setXnLan1Tren1000(int xnLan1Tren1000) {
        this.xnLan1Tren1000 = xnLan1Tren1000;
    }

    public int getXnLan2() {
        return xnLan2Duoi200 + xnLan2Tu200Den1000 + xnLan2Tren1000;
    }

    public int getXnLan2Duoi200() {
        return xnLan2Duoi200;
    }

    public void setXnLan2Duoi200(int xnLan2Duoi200) {
        this.xnLan2Duoi200 = xnLan2Duoi200;
    }

    public int getXnLan2Tu200Den1000() {
        return xnLan2Tu200Den1000;
    }

    public void setXnLan2Tu200Den1000(int xnLan2Tu200Den1000) {
        this.xnLan2Tu200Den1000 = xnLan2Tu200Den1000;
    }

    public int getXnLan2Tren1000() {
        return xnLan2Tren1000;
    }

    public void setXnLan2Tren1000(int xnLan2Tren1000) {
        this.xnLan2Tren1000 = xnLan2Tren1000;
    }

    public int getArvBac2() {
        return arvBac2Duoi200 + arvBac2Tu200Den1000 + arvBac2Tren1000;
    }

    public int getArvBac2Duoi200() {
        return arvBac2Duoi200;
    }

    public void setArvBac2Duoi200(int arvBac2Duoi200) {
        this.arvBac2Duoi200 = arvBac2Duoi200;
    }

    public int getArvBac2Tu200Den1000() {
        return arvBac2Tu200Den1000;
    }

    public void setArvBac2Tu200Den1000(int arvBac2Tu200Den1000) {
        this.arvBac2Tu200Den1000 = arvBac2Tu200Den1000;
    }

    public int getArvBac2Tren1000() {
        return arvBac2Tren1000;
    }

    public void setArvBac2Tren1000(int arvBac2Tren1000) {
        this.arvBac2Tren1000 = arvBac2Tren1000;
    }

    public int getXn12ThangDuoi200() {
        return xn12ThangDuoi200;
    }

    public void setXn12ThangDuoi200(int xn12ThangDuoi200) {
        this.xn12ThangDuoi200 = xn12ThangDuoi200;
    }

    public int getXn12ThangTu200Den1000() {
        return xn12ThangTu200Den1000;
    }

    public void setXn12ThangTu200Den1000(int xn12ThangTu200Den1000) {
        this.xn12ThangTu200Den1000 = xn12ThangTu200Den1000;
    }

    public int getXn12ThangTren1000() {
        return xn12ThangTren1000;
    }

    public void setXn12ThangTren1000(int xn12ThangTren1000) {
        this.xn12ThangTren1000 = xn12ThangTren1000;
    }

}

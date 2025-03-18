package com.gms.entity.form.opc_arv;

public class PqmReportTable {

    private int stt;
    private String indicatorKey;
    private String indicators;

    private int i10x14;
    private int i15x19;
    private int i20x24;
    private int i25x29;
    private int i30x34;
    private int i35x39;
    private int i40x44;
    private int i45x49;
    private int i50x;
    private int ina;

    private int male;
    private int female;
    private int gna;

    private int ntmt;
    private int md;
    private int pnmt;
    private int nhm;
    private int aids;
    private int lao;
    private int ltqdtd;
    private int nvqs;
    private int msm;
    private int other;
    private int ona;

    private int sum;

    private String keyEarly;

    public int getIna() {
        return ina;
    }

    public void setIna(int ina) {
        this.ina = ina;
    }

    public int getGna() {
        return gna;
    }

    public void setGna(int gna) {
        this.gna = gna;
    }

    public int getOna() {
        return ona;
    }

    public void setOna(int ona) {
        this.ona = ona;
    }

    public String getKeyEarly() {
        return keyEarly;
    }

    public void setKeyEarly(String keyEarly) {
        this.keyEarly = keyEarly;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getIndicatorKey() {
        return indicatorKey;
    }

    public void setIndicatorKey(String indicatorKey) {
        this.indicatorKey = indicatorKey;
    }

    public String getIndicators() {
        return indicators;
    }

    public void setIndicators(String indicators) {
        this.indicators = indicators;
    }

    public int getI10x14() {
        return i10x14;
    }

    public void setI10x14(int i10x14) {
        this.i10x14 = i10x14;
    }

    public int getI15x19() {
        return i15x19;
    }

    public void setI15x19(int i15x19) {
        this.i15x19 = i15x19;
    }

    public int getI20x24() {
        return i20x24;
    }

    public void setI20x24(int i20x24) {
        this.i20x24 = i20x24;
    }

    public int getI25x29() {
        return i25x29;
    }

    public void setI25x29(int i25x29) {
        this.i25x29 = i25x29;
    }

    public int getI30x34() {
        return i30x34;
    }

    public void setI30x34(int i30x34) {
        this.i30x34 = i30x34;
    }

    public int getI35x39() {
        return i35x39;
    }

    public void setI35x39(int i35x39) {
        this.i35x39 = i35x39;
    }

    public int getI40x44() {
        return i40x44;
    }

    public void setI40x44(int i40x44) {
        this.i40x44 = i40x44;
    }

    public int getI45x49() {
        return i45x49;
    }

    public void setI45x49(int i45x49) {
        this.i45x49 = i45x49;
    }

    public int getI50x() {
        return i50x;
    }

    public void setI50x(int i50x) {
        this.i50x = i50x;
    }

    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
    }

    public int getFemale() {
        return female;
    }

    public void setFemale(int female) {
        this.female = female;
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

    public int getPnmt() {
        return pnmt;
    }

    public void setPnmt(int pnmt) {
        this.pnmt = pnmt;
    }

    public int getNhm() {
        return nhm;
    }

    public void setNhm(int nhm) {
        this.nhm = nhm;
    }

    public int getAids() {
        return aids;
    }

    public void setAids(int aids) {
        this.aids = aids;
    }

    public int getLao() {
        return lao;
    }

    public void setLao(int lao) {
        this.lao = lao;
    }

    public int getLtqdtd() {
        return ltqdtd;
    }

    public void setLtqdtd(int ltqdtd) {
        this.ltqdtd = ltqdtd;
    }

    public int getNvqs() {
        return nvqs;
    }

    public void setNvqs(int nvqs) {
        this.nvqs = nvqs;
    }

    public int getMsm() {
        return msm;
    }

    public void setMsm(int msm) {
        this.msm = msm;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public int getSum() {
        return i10x14
                + i15x19
                + i20x24
                + i25x29
                + i30x34
                + i35x39
                + i40x44
                + i45x49
                + i50x
                + ina;
    }

}

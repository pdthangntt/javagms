package com.gms.entity.form.opc_arv;

public class PqmHtcConfirmReportTable {

    private int stt;
    private String indicators;
    private String key;
    private int under15;
    private int m15to25;
    private int m25to49;
    private int over49;
    private int male;
    private int female;
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

    private int sum;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getMd() {
        return md;
    }

    public void setMd(int md) {
        this.md = md;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getIndicators() {
        return indicators;
    }

    public void setIndicators(String indicators) {
        this.indicators = indicators;
    }

    public int getUnder15() {
        return under15;
    }

    public void setUnder15(int under15) {
        this.under15 = under15;
    }

    public int getM15to25() {
        return m15to25;
    }

    public void setM15to25(int m15to25) {
        this.m15to25 = m15to25;
    }

    public int getM25to49() {
        return m25to49;
    }

    public void setM25to49(int m25to49) {
        this.m25to49 = m25to49;
    }

    public int getOver49() {
        return over49;
    }

    public void setOver49(int over49) {
        this.over49 = over49;
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

    public int getSum() {
        return ntmt
                + md
                + pnmt
                + nhm
                + aids
                + lao
                + ltqdtd
                + nvqs
                + msm
                + other;
    }
}

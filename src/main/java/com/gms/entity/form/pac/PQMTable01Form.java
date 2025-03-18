package com.gms.entity.form.pac;

import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PQMTable01Form implements Serializable, Cloneable {

    private int stt;
    private String site;
    private int positive;
    private int recent;
    private int art;

    public int getSum() {
        return this.positive + this.recent + this.art;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getRecent() {
        return recent;
    }

    public void setRecent(int recent) {
        this.recent = recent;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

}

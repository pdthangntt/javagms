package com.gms.entity.rabbit;

import com.gms.entity.constant.IndicatorTableEnum;
import com.gms.entity.db.SiteEntity;

/**
 *
 * @author vvthanh
 */
public class IndicatorMessage extends RabbitMessage {

    private IndicatorTableEnum table;
    private int month;
    private int year;
    private SiteEntity site;

    public IndicatorMessage() {
    }

    public IndicatorTableEnum getTable() {
        return table;
    }

    public void setTable(IndicatorTableEnum table) {
        this.table = table;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public SiteEntity getSite() {
        return site;
    }

    public void setSite(SiteEntity site) {
        this.site = site;
    }

}

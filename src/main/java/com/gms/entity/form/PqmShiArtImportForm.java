package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class PqmShiArtImportForm extends BaseEntity implements Serializable {

    private String month; //Tháng
    private String year; //Năm
    
    private String provinceID; //Tỉnh

    private String siteID; //Cơ sở
    private String quantity; //Số lượng

    private String quantityNew; //Số lượng mới

    private String quantityNhtk; //Số lượng tái khám

    private String quantityQldt; //Số lượng quay lại
    private String quantityBt; //Số lượng bỏ trị
    private String time;//Thời gian năm và tháng

    private String row;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityNew() {
        return quantityNew;
    }

    public void setQuantityNew(String quantityNew) {
        this.quantityNew = quantityNew;
    }

    public String getQuantityNhtk() {
        return quantityNhtk;
    }

    public void setQuantityNhtk(String quantityNhtk) {
        this.quantityNhtk = quantityNhtk;
    }

    public String getQuantityQldt() {
        return quantityQldt;
    }

    public void setQuantityQldt(String quantityQldt) {
        this.quantityQldt = quantityQldt;
    }

    public String getQuantityBt() {
        return quantityBt;
    }

    public void setQuantityBt(String quantityBt) {
        this.quantityBt = quantityBt;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

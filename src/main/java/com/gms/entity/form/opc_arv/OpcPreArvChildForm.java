package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;

/**
 *
 * @author DSNAnh
 */
public class OpcPreArvChildForm implements Comparable<OpcPreArvChildForm>{
    private String arvID;
    private String year;
    private String quarter;
    private String month;
    private String type;
    private String time;
    private String result;
    private String statusOftreatment;

    public String getArvID() {
        return arvID;
    }

    public void setArvID(String arvID) {
        this.arvID = arvID;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = "CD".equals(type) ? "CĐ" : type;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatusOftreatment() {
        return statusOftreatment;
    }

    public void setStatusOftreatment(String statusOftreatment) {
        this.statusOftreatment = statusOftreatment;
    }
    
    @Override
    public int compareTo(OpcPreArvChildForm item) {
        if(this.getTime() != null && item.getTime() != null){
            return TextUtils.convertDate(this.getTime(), "dd/MM/yyyy").compareTo(TextUtils.convertDate(item.getTime(), "dd/MM/yyyy"));
        } else {
            return "1".compareTo("1");
        }
    }
}

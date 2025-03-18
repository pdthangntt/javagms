/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public class ArvBookChildForm  implements Comparable<ArvBookChildForm>{
    private String arvID;
    private boolean flag;
    private String stageID;
    private String year;
    private String quarter;
    private String month;
    private String type;
    private String time;
    private String result;
    private String statusOftreatment;
    private String treatmentRegimentID;
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    public String getTreatmentRegimentID() {
        return treatmentRegimentID;
    }

    public void setTreatmentRegimentID(String treatmentRegimentID) {
        this.treatmentRegimentID = treatmentRegimentID;
    }

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
        if(type != null && type.equals("CD")){
            type = "CĐ";
        }
        if(type != null && type.equals("DTL")){
            type = "ĐTL";
        }
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
    public int compareTo(ArvBookChildForm item) {
        if(StringUtils.isNotEmpty(this.getTime()) && StringUtils.isNotEmpty(item.getTime())){
            try{
                if(TextUtils.convertDate(this.getTime(), "dd/MM/yyyy").compareTo(TextUtils.convertDate(item.getTime(), "dd/MM/yyyy")) > 0){
                    return 1;
                } else if(TextUtils.convertDate(this.getTime(), "dd/MM/yyyy").compareTo(TextUtils.convertDate(item.getTime(), "dd/MM/yyyy")) < 0){
                    return -1;
                } else {
                    return 0;
                }
                
            } catch (Exception ex) {
                return "1".compareTo("1");
            }
        } else {
            return "1".compareTo("1");
        }
    }
}

package com.gms.entity.form.htc_confirm;

import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PQMForm implements Serializable {

    private int stt;
    private String indicator;
    private String gender;
    private String objectGroup;
    private String ageGroup;
    private int quantity;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getObjectGroup() {
        return objectGroup;
    }

    public void setObjectGroup(String objectGroup) {
        this.objectGroup = objectGroup;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

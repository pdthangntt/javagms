package com.gms.entity.form.drug;

import com.gms.entity.form.shi_hub.*;

/**
 *
 * @author pdThang
 */
public class OptionalDataForm {

    private String value;
    private String value_name;
    private String drug_name;
    private String unit_name;
    private String data_source;

    public String getData_source() {
        return data_source;
    }

    public void setData_source(String data_source) {
        this.data_source = data_source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue_name() {
        return value_name;
    }

    public void setValue_name(String value_name) {
        this.value_name = value_name;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

}

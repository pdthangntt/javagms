package com.gms.entity.form.drug;

/**
 *
 * @author pdThang
 */
public class DatasDrugForm {

    private String indicator_code;
    private String district_code;
    private String site_code;
    private DataDrugForm data;
    private OptionalDataForm optional_data;

    public OptionalDataForm getOptional_data() {
        return optional_data;
    }

    public void setOptional_data(OptionalDataForm optional_data) {
        this.optional_data = optional_data;
    }

    public String getIndicator_code() {
        return indicator_code;
    }

    public void setIndicator_code(String indicator_code) {
        this.indicator_code = indicator_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public DataDrugForm getData() {
        return data;
    }

    public void setData(DataDrugForm data) {
        this.data = data;
    }

}

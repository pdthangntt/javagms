package com.gms.entity.form.hub;


/**
 *
 * @author pdThang
 */
public class DatasForm {

    private String indicator_code;
    private String district_code;
    private String site_code;
    private DataForm data;

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

    public DataForm getData() {
        return data;
    }

    public void setData(DataForm data) {
        this.data = data;
    }

}

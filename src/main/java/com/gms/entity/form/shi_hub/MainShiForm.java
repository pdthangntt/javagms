package com.gms.entity.form.shi_hub;

import java.util.List;

/**
 *
 * @author pdThang
 */
public class MainShiForm {

    private String province_code;
    private String month;
    private String year;
    private List<DatasShiForm> datas;

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

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

    public List<DatasShiForm> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasShiForm> datas) {
        this.datas = datas;
    }

}

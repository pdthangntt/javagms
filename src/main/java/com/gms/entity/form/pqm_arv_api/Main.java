package com.gms.entity.form.pqm_arv_api;

import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Main extends BaseEntity {

    private Integer month;
    private Integer year;
    private Integer quantity;
    private String province_code;
    private String provinceID;
    private List<Arv> datas;

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public List<Arv> getDatas() {
        return datas;
    }

    public void setDatas(List<Arv> datas) {
        this.datas = datas;
    }

}

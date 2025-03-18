package com.gms.entity.form.drug_new_api;

import com.gms.entity.form.opc_arv.*;
import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmDrugNewApiMainForm extends BaseEntity {

    private Integer quarter;
    private Integer year;
    private Integer quantity;
    private String provinceID;

    private List<PqmDrugNewApiForm> datas;

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
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

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public List<com.gms.entity.form.drug_new_api.PqmDrugNewApiForm> getDatas() {
        return datas;
    }

    public void setDatas(List<com.gms.entity.form.drug_new_api.PqmDrugNewApiForm> datas) {
        this.datas = datas;
    }

}

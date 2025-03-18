package com.gms.entity.form;

import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmHtcApiMainForm extends BaseEntity {

    private Integer month;
    private Integer year;
    private Integer quantity;
    private String province_code;

    private List<PqmHtcApiElogForm> datas;

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

    public List<PqmHtcApiElogForm> getDatas() {
        return datas;
    }

    public void setDatas(List<PqmHtcApiElogForm> datas) {
        this.datas = datas;
    }

}

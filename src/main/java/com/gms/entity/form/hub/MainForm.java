package com.gms.entity.form.hub;

import com.gms.entity.form.htc.*;
import com.gms.entity.db.HtcVisitEntity;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class MainForm {

    private String province_code;
    private String month;
    private String year;
    private List<DatasForm> datas;

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

    public List<DatasForm> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasForm> datas) {
        this.datas = datas;
    }

}

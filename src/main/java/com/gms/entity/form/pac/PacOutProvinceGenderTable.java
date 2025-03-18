package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceGenderTable extends BaseForm implements Serializable {

    public PacOutProvinceGenderTable() {

        nam = 0;
        nu = 0;
        khongro = 0;
        sum = 0;
    }

    private int nam;
    private int nu;
    private int khongro;

    private int sum;

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public int getNu() {
        return nu;
    }

    public void setNu(int nu) {
        this.nu = nu;
    }

    public int getKhongro() {
        return khongro;
    }

    public void setKhongro(int khongro) {
        this.khongro = khongro;
    }

    public int getSum() {
        return this.nam + this.nu + this.khongro;
    }

}

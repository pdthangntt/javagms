package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceAgeTable extends BaseForm implements Serializable {

    public PacOutProvinceAgeTable() {

        under15 = 0;
        from15to24 = 0;
        from25to49 = 0;
        over49 = 0;
        sum = 0;
    }

    private int under15;
    private int from15to24;
    private int from25to49;
    private int over49;

    private int sum;

    public int getUnder15() {
        return under15;
    }

    public void setUnder15(int under15) {
        this.under15 = under15;
    }

    public int getFrom15to24() {
        return from15to24;
    }

    public void setFrom15to24(int from15to24) {
        this.from15to24 = from15to24;
    }

    public int getFrom25to49() {
        return from25to49;
    }

    public void setFrom25to49(int from25to49) {
        this.from25to49 = from25to49;
    }

    public int getOver49() {
        return over49;
    }

    public void setOver49(int over49) {
        this.over49 = over49;
    }

    public int getSum() {
        return this.under15 + this.from15to24 + this.from25to49 + this.over49;
    }

}

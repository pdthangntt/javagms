package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 * bảng opc mer
 *
 * @author pdThang
 */
public class OpcMerSixMonthTable extends BaseForm implements Serializable {

    public OpcMerSixMonthTable() {

        underOneAge = 0;
        oneToFourteen = 0;
        overFifteen = 0;
        sum = 0;
    }

    private int underOneAge;
    private int oneToFourteen;
    private int overFifteen;
    private int sum;

    public int getUnderOneAge() {
        return underOneAge;
    }

    public void setUnderOneAge(int underOneAge) {
        this.underOneAge = underOneAge;
    }

    public int getOneToFourteen() {
        return oneToFourteen;
    }

    public void setOneToFourteen(int oneToFourteen) {
        this.oneToFourteen = oneToFourteen;
    }

    public int getOverFifteen() {
        return overFifteen;
    }

    public void setOverFifteen(int overFifteen) {
        this.overFifteen = overFifteen;
    }

    public int getSum() {
        return underOneAge + oneToFourteen + overFifteen;
    }

    public int getTotal() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}

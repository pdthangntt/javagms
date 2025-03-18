package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 * bảng opc mer
 *
 * @author pdThang bảng tính tổng + %
 */
public class OpcMerSixMonthTableSum extends BaseForm implements Serializable {

    private int underOneAgeNam;
    private int oneToFourteenNam;
    private int overFifteenNam;
    private int underOneAgeNu;
    private int oneToFourteenNu;
    private int overFifteenNu;
    private int sum;

    public int getUnderOneAgeNam() {
        return underOneAgeNam;
    }

    public void setUnderOneAgeNam(int underOneAgeNam) {
        this.underOneAgeNam = underOneAgeNam;
    }

    public int getOneToFourteenNam() {
        return oneToFourteenNam;
    }

    public void setOneToFourteenNam(int oneToFourteenNam) {
        this.oneToFourteenNam = oneToFourteenNam;
    }

    public int getOverFifteenNam() {
        return overFifteenNam;
    }

    public void setOverFifteenNam(int overFifteenNam) {
        this.overFifteenNam = overFifteenNam;
    }

    public int getUnderOneAgeNu() {
        return underOneAgeNu;
    }

    public void setUnderOneAgeNu(int underOneAgeNu) {
        this.underOneAgeNu = underOneAgeNu;
    }

    public int getOneToFourteenNu() {
        return oneToFourteenNu;
    }

    public void setOneToFourteenNu(int oneToFourteenNu) {
        this.oneToFourteenNu = oneToFourteenNu;
    }

    public int getOverFifteenNu() {
        return overFifteenNu;
    }

    public void setOverFifteenNu(int overFifteenNu) {
        this.overFifteenNu = overFifteenNu;
    }

    public int getSum() {
        return underOneAgeNam
                + oneToFourteenNam
                + overFifteenNam
                + underOneAgeNu
                + oneToFourteenNu
                + overFifteenNu;
    }

}

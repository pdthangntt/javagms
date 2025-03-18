package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 * bảng opc mer
 *
 * @author pdThang bảng tính tổng + %
 */
public class OpcMerSixMonthTablePercent extends BaseForm implements Serializable {

    private String underOneAgeNam;
    private String oneToFourteenNam;
    private String overFifteenNam;
    private String underOneAgeNu;
    private String oneToFourteenNu;
    private String overFifteenNu;
    private String sum;

    public String getUnderOneAgeNam() {
        return underOneAgeNam;
    }

    public void setUnderOneAgeNam(String underOneAgeNam) {
        this.underOneAgeNam = underOneAgeNam;
    }

    public String getOneToFourteenNam() {
        return oneToFourteenNam;
    }

    public void setOneToFourteenNam(String oneToFourteenNam) {
        this.oneToFourteenNam = oneToFourteenNam;
    }

    public String getOverFifteenNam() {
        return overFifteenNam;
    }

    public void setOverFifteenNam(String overFifteenNam) {
        this.overFifteenNam = overFifteenNam;
    }

    public String getUnderOneAgeNu() {
        return underOneAgeNu;
    }

    public void setUnderOneAgeNu(String underOneAgeNu) {
        this.underOneAgeNu = underOneAgeNu;
    }

    public String getOneToFourteenNu() {
        return oneToFourteenNu;
    }

    public void setOneToFourteenNu(String oneToFourteenNu) {
        this.oneToFourteenNu = oneToFourteenNu;
    }

    public String getOverFifteenNu() {
        return overFifteenNu;
    }

    public void setOverFifteenNu(String overFifteenNu) {
        this.overFifteenNu = overFifteenNu;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

}

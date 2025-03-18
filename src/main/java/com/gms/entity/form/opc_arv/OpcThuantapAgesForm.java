package com.gms.entity.form.opc_arv;

import com.gms.entity.db.BaseEntity;
import java.io.Serializable;

/**
 * Form thuần tập 
 * 
 * @author TrangBN
 */
public class OpcThuantapAgesForm extends BaseEntity implements Serializable {

    public OpcThuantapAgesForm() {
        underFifteen = 0;
        overFifteen = 0;
        sum = 0;
    }
    
    private int underFifteen;
    private int overFifteen;
    private int sum;

    public int getOverFifteen() {
        return overFifteen;
    }

    public void setOverFifteen(int overFifteen) {
        this.overFifteen = overFifteen;
    }

    public int getSum() {
        return underFifteen + overFifteen;
    }
    
    public int getTotal() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getUnderFifteen() {
        return underFifteen;
    }

    public void setUnderFifteen(int underFifteen) {
        this.underFifteen = underFifteen;
    }
}

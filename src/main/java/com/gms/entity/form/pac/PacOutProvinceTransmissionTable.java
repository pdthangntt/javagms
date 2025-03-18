package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceTransmissionTable extends BaseForm implements Serializable {

    public PacOutProvinceTransmissionTable() {
        blood = 0;
        sexuality = 0;
        momtochild = 0;
        unclear = 0;
        noinformation = 0;
        sum = 0;
    }

    private int blood;
    private int sexuality;
    private int momtochild;
    private int unclear;
    private int noinformation;
    private int sum;

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getSexuality() {
        return sexuality;
    }

    public void setSexuality(int sexuality) {
        this.sexuality = sexuality;
    }

    public int getMomtochild() {
        return momtochild;
    }

    public void setMomtochild(int momtochild) {
        this.momtochild = momtochild;
    }

    public int getUnclear() {
        return unclear;
    }

    public void setUnclear(int unclear) {
        this.unclear = unclear;
    }

    public int getNoinformation() {
        return noinformation;
    }

    public void setNoinformation(int noinformation) {
        this.noinformation = noinformation;
    }

    public int getSum() {
        return this.blood
                + this.sexuality
                + this.momtochild
                + this.unclear
                + this.noinformation;

    }

}

package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;

/**
 *
 * @author vvthanh
 */
public class InsuranceTable extends BaseForm {

    private int insurance; //1.1 Hưởng 100% từ quỹ BHYT
    private int insurance100; //1.1 Hưởng 100% từ quỹ BHYT
    private int insurance95; //1.2 Hưởng 95% từ quỹ BHYT
    private int insurance80; //1.3 Hưởng 80% từ quỹ BHYT
    private int insuranceOther; //1.4 Hưởng khác từ quỹ BHYT
    private int insuranceNone; //Chưa có BHYT

    private int expire1; //Hết hạn trong vòng 1 tháng
    private int expire2; //Hết hạn trong vòng 2 tháng
    private int expire3; //Hết hạn trong vòng 3 tháng

    private int dungtuyen;
    private int traituyen;

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getInsurance100() {
        return insurance100;
    }

    public void setInsurance100(int insurance100) {
        this.insurance100 = insurance100;
    }

    public int getInsurance95() {
        return insurance95;
    }

    public void setInsurance95(int insurance95) {
        this.insurance95 = insurance95;
    }

    public int getInsurance80() {
        return insurance80;
    }

    public void setInsurance80(int insurance80) {
        this.insurance80 = insurance80;
    }

    public int getInsuranceOther() {
        return insuranceOther;
    }

    public void setInsuranceOther(int insuranceOther) {
        this.insuranceOther = insuranceOther;
    }

    public int getInsuranceNone() {
        return insuranceNone;
    }

    public void setInsuranceNone(int insuranceNone) {
        this.insuranceNone = insuranceNone;
    }

    public int getExpire1() {
        return expire1;
    }

    public void setExpire1(int expire1) {
        this.expire1 = expire1;
    }

    public int getExpire2() {
        return expire2;
    }

    public void setExpire2(int expire2) {
        this.expire2 = expire2;
    }

    public int getExpire3() {
        return expire3;
    }

    public void setExpire3(int expire3) {
        this.expire3 = expire3;
    }

    public int getDungtuyen() {
        return dungtuyen;
    }

    public void setDungtuyen(int dungtuyen) {
        this.dungtuyen = dungtuyen;
    }

    public int getTraituyen() {
        return traituyen;
    }

    public void setTraituyen(int traituyen) {
        this.traituyen = traituyen;
    }

}

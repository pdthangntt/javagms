package com.gms.entity.form.opc_arv;

/**
 * Quản lý điều trị ARV
 *
 * @author vvthanh
 */
public class TT03Table05 {

    private int preArv; //Bệnh nhân điều trị ARV cuối kỳ báo cáo trước
    private int firstRegister; //Số bệnh nhân bắt đầu điều trị lần đầu
    private int backTreatment;
    private int transferTo;
    private int moveAway;
    private int quitTreatment;
    private int dead; //tử vong
    private int arv; //Đang điều trị đến cuối kỳ báo cáo
    private int ward; //Nhận thuốc tại xã

    public int getPreArv() {
        return preArv;
    }

    public void setPreArv(int preArv) {
        this.preArv = preArv;
    }

    public int getFirstRegister() {
        return firstRegister;
    }

    public void setFirstRegister(int firstRegister) {
        this.firstRegister = firstRegister;
    }

    public int getBackTreatment() {
        return backTreatment;
    }

    public void setBackTreatment(int backTreatment) {
        this.backTreatment = backTreatment;
    }

    public int getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(int transferTo) {
        this.transferTo = transferTo;
    }

    public int getMoveAway() {
        return moveAway;
    }

    public void setMoveAway(int moveAway) {
        this.moveAway = moveAway;
    }

    public int getQuitTreatment() {
        return quitTreatment;
    }

    public void setQuitTreatment(int quitTreatment) {
        this.quitTreatment = quitTreatment;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public int getArv() {
        return arv;
    }

    public void setArv(int arv) {
        this.arv = arv;
    }

    public int getWard() {
        return ward;
    }

    public void setWard(int ward) {
        this.ward = ward;
    }

}

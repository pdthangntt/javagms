package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;

/**
 * Bảng lấy số liệu trong tháng
 * 
 * @author TrangBN
 */
public class OpcBookViralLoadMasterForm  extends BaseForm {

    private int xnLan1; // Tổng số BN được làm xét nghiệm lần đầu trong tháng 
    private int dtBac2; // Số BN điều trị ARV bậc 2 được làm XN trong tháng
    private int xnLan1DuoiNguongPhatHien; // Số BN làm XN lần 1 có KQXN dưới ngưỡng phát hiện
    private int xnLan1Duoi200; // Số BN làm XN lần 1 có KQXN dưới ngưỡng phát hiện đến dưới 200 bản sao/ml
    private int xnLan1F200T1000; // Số BN làm XN lần 1 có KQXN từ 200 bản sao đến dưới 1000 bản sao/ml
    private int xnLan1From1000; // Số BN làm XN lần 1 có KQXN >= 1000 bản sao/ml
    private int xnLan2From1000; // Tổng số BN XN lần 2 có kết quả >= 1000 bản sao/ml
    private int tbdt; // Số BN thất bại điều trị, chuyển phác đồ trong tháng

    public int getTbdt() {
        return tbdt;
    }

    public void setTbdt(int tbdt) {
        this.tbdt = tbdt;
    }

    public int getXnLan2From1000() {
        return xnLan2From1000;
    }

    public void setXnLan2From1000(int xnLan2From1000) {
        this.xnLan2From1000 = xnLan2From1000;
    }

    public int getXnLan1From1000() {
        return xnLan1From1000;
    }

    public void setXnLan1From1000(int xnLan1From1000) {
        this.xnLan1From1000 = xnLan1From1000;
    }

    public int getXnLan1F200T1000() {
        return xnLan1F200T1000;
    }

    public void setXnLan1F200T1000(int xnLan1F200T1000) {
        this.xnLan1F200T1000 = xnLan1F200T1000;
    }
    
    public int getXnLan1Duoi200() {
        return xnLan1Duoi200;
    }

    public void setXnLan1Duoi200(int xnLan1Duoi200) {
        this.xnLan1Duoi200 = xnLan1Duoi200;
    }

    public int getXnLan1DuoiNguongPhatHien() {
        return xnLan1DuoiNguongPhatHien;
    }

    public void setXnLan1DuoiNguongPhatHien(int xnLan1DuoiNguongPhatHien) {
        this.xnLan1DuoiNguongPhatHien = xnLan1DuoiNguongPhatHien;
    }
    
    public int getDtBac2() {
        return dtBac2;
    }

    public void setDtBac2(int dtBac2) {
        this.dtBac2 = dtBac2;
    }
    
    public int getXnLan1() {
        return xnLan1;
    }

    public void setXnLan1(int xnLan1) {
        this.xnLan1 = xnLan1;
    }
}

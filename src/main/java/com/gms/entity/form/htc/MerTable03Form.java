package com.gms.entity.form.htc;

import java.io.Serializable;

/**
 * Bảng 3: Kết quả xét nghiệm nhiễm mới theo cơ sở dịch vụ và nhóm đối tượng
 *
 * @author TrangBN
 */
public class MerTable03Form implements Serializable {

    private String objectGroupID;
    private String serviceID; // Loại hình dịch vụ
    private String earlyHiv; // Kết quả xn nhiễm mới
    private int ncmt; // Nghiện chích ma túy
    private int phuNuBanDam; // Phụ nữ bán dâm
    private int msm; // Nam quan hệ tình dục với nam
    private int chuyenGioi; // Người chuyển giói
    private int phamNhan; // Pham nhan
    private int khac; // Khac

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public int getNcmt() {
        return ncmt;
    }

    public void setNcmt(int ncmt) {
        this.ncmt = ncmt;
    }

    public int getPhuNuBanDam() {
        return phuNuBanDam;
    }

    public void setPhuNuBanDam(int phuNuBanDam) {
        this.phuNuBanDam = phuNuBanDam;
    }

    public int getMsm() {
        return msm;
    }

    public void setMsm(int msm) {
        this.msm = msm;
    }

    public int getChuyenGioi() {
        return chuyenGioi;
    }

    public void setChuyenGioi(int chuyenGioi) {
        this.chuyenGioi = chuyenGioi;
    }

    public int getPhamNhan() {
        return phamNhan;
    }

    public void setPhamNhan(int phamNhan) {
        this.phamNhan = phamNhan;
    }

    public int getKhac() {
        return khac;
    }

    public void setKhac(int khac) {
        this.khac = khac;
    }

    public int getSum() {
        return this != null ? this.ncmt
                + this.phuNuBanDam
                + this.msm
                + this.khac
                + this.phamNhan
                + this.chuyenGioi : 0;
    }
}

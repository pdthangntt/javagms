package com.gms.entity.form.htc;

import java.io.Serializable;

/**
 * Bảng 05: Số khách hàng nhận dịch vụ HTC và quay lại nhận kết quả 
 * theo nhóm giới tính và nhóm đối tượng
 * 
 * @author TrangBN
 */
public class MerTable05Form implements Serializable {
    private String objectGroupID;
    private String districtID; // Mã huyện
    private String serviceID; // Loại hình dịch vụ
    private String confirmResultsID; // Kết quả xét nghiệm khẳng định
    private int ncmt; // Nghiện chích ma túy
    private int phuNuBanDam; // Phụ nữ bán dâm
    private int msm; // Nam quan hệ tình dục với nam
    private int chuyenGioi; // Người chuyển giói
    private int phamNhan; // Pham nhan
    private int khac; // Pham nhan

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
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
    
    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
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

    public int getSum() {
        return this != null? this.ncmt 
                + this.phuNuBanDam 
                + this.msm 
                + this.khac
                + this.phamNhan 
                + this.chuyenGioi:0;
    }
}

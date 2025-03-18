package com.gms.entity.form.laytest;

import java.io.Serializable;

/**
 *
 * @author TrangBN
 */
public class LaytestTable01Form implements Serializable  {
    private int stt;
    private String danhMucBaoCao;
    private int tuVanTruocXetNghiem;
    private int duongTinhTong;
    private int duocXetNghiemDuongTinh;
    private int nhanKetQuaTong;
    private int nhanKetQuaDuongtinh;
    private int gioiThieuBanChich;
    private int chuyenGuiDieuTri;
    private Long oID;
    private Long oParentID;
    private String oCode;
    private int position;

    public int getGioiThieuBanChich() {
        return gioiThieuBanChich;
    }

    public void setGioiThieuBanChich(int gioiThieuBanChich) {
        this.gioiThieuBanChich = gioiThieuBanChich;
    }

    public int getChuyenGuiDieuTri() {
        return chuyenGuiDieuTri;
    }

    public void setChuyenGuiDieuTri(int chuyenGuiDieuTri) {
        this.chuyenGuiDieuTri = chuyenGuiDieuTri;
    }

    public String getoCode() {
        return oCode;
    }

    public void setoCode(String oCode) {
        this.oCode = oCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Long getoID() {
        return oID;
    }

    public void setoID(Long oID) {
        this.oID = oID;
    }

    public Long getoParentID() {
        return oParentID;
    }

    public void setoParentID(Long oParentID) {
        this.oParentID = oParentID;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getDanhMucBaoCao() {
        return danhMucBaoCao;
    }

    public void setDanhMucBaoCao(String danhMucBaoCao) {
        this.danhMucBaoCao = danhMucBaoCao;
    }

    public int getTuVanTruocXetNghiem() {
        return tuVanTruocXetNghiem;
    }

    public void setTuVanTruocXetNghiem(int tuVanTruocXetNghiem) {
        this.tuVanTruocXetNghiem = tuVanTruocXetNghiem;
    }

    public int getDuongTinhTong() {
        return duongTinhTong;
    }

    public void setDuongTinhTong(int duongTinhTong) {
        this.duongTinhTong = duongTinhTong;
    }

    public int getDuocXetNghiemDuongTinh() {
        return duocXetNghiemDuongTinh;
    }

    public void setDuocXetNghiemDuongTinh(int duocXetNghiemDuongTinh) {
        this.duocXetNghiemDuongTinh = duocXetNghiemDuongTinh;
    }

    public int getNhanKetQuaTong() {
        return nhanKetQuaTong;
    }

    public void setNhanKetQuaTong(int nhanKetQuaTong) {
        this.nhanKetQuaTong = nhanKetQuaTong;
    }

    public int getNhanKetQuaDuongtinh() {
        return nhanKetQuaDuongtinh;
    }

    public void setNhanKetQuaDuongtinh(int nhanKetQuaDuongtinh) {
        this.nhanKetQuaDuongtinh = nhanKetQuaDuongtinh;
    }
}

package com.gms.entity.form.tt03;

import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class Table02Form implements Serializable {

    private int stt;
    private String danhMucBaoCao;
    private int tuVanTruocXetNghiem;
    private int duocXetNghiemTong;
    private int duocXetNghiemDuongTinh;
    private int nhanKetQuaTong;
    private int nhanKetQuaDuongtinh;
    private Long oID;
    private Long oParentID;
    private String oCode;
    private int position;

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

    public int getDuocXetNghiemTong() {
        return duocXetNghiemTong;
    }

    public void setDuocXetNghiemTong(int duocXetNghiemTong) {
        this.duocXetNghiemTong = duocXetNghiemTong;
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

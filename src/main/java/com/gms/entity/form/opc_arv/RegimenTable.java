package com.gms.entity.form.opc_arv;

/**
 *
 * @author vvthành
 */
public class RegimenTable implements Cloneable {

    private String stt;
    private String regimentID;
    private String regimentName;
    private String type; //1 là người lớn/2 là trẻ em
    private int batdaunhan; //c
    private int dangnhan; //d
    private int bonhan_chuyendi; //e1
    private int bonhan_tuvong; //e2
    private int bonhan_botri; //e3
    private int bonhan_matdau; //e4
    private int bonhan_chuyenphacdo; //e5
    private int nhanThuoc; //f

    public int getBonhan() {
        return bonhan_chuyendi + bonhan_tuvong + bonhan_botri + bonhan_matdau + bonhan_chuyenphacdo;
    }

    //f
    public int getNhanThuoc() {
        return nhanThuoc;
    }

    //g: Ước tính
    public int getUocTinh() {
        int uoctinh = nhanThuoc + (batdaunhan * 3);
        int utBonhan = (getBonhan() * 3);
        if (uoctinh >= utBonhan) {
            uoctinh -= utBonhan;
        }
        return uoctinh;
    }

    public void setNhanThuoc(int nhanThuoc) {
        this.nhanThuoc = nhanThuoc;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getRegimentID() {
        return regimentID;
    }

    public void setRegimentID(String regimentID) {
        this.regimentID = regimentID;
    }

    public String getRegimentName() {
        return regimentName;
    }

    public void setRegimentName(String regimentName) {
        this.regimentName = regimentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBatdaunhan() {
        return batdaunhan;
    }

    public void setBatdaunhan(int batdaunhan) {
        this.batdaunhan = batdaunhan;
    }

    public int getDangnhan() {
        return dangnhan;
    }

    public void setDangnhan(int dangnhan) {
        this.dangnhan = dangnhan;
    }

    public int getBonhan_chuyendi() {
        return bonhan_chuyendi;
    }

    public void setBonhan_chuyendi(int bonhan_chuyendi) {
        this.bonhan_chuyendi = bonhan_chuyendi;
    }

    public int getBonhan_tuvong() {
        return bonhan_tuvong;
    }

    public void setBonhan_tuvong(int bonhan_tuvong) {
        this.bonhan_tuvong = bonhan_tuvong;
    }

    public int getBonhan_botri() {
        return bonhan_botri;
    }

    public void setBonhan_botri(int bonhan_botri) {
        this.bonhan_botri = bonhan_botri;
    }

    public int getBonhan_matdau() {
        return bonhan_matdau;
    }

    public void setBonhan_matdau(int bonhan_matdau) {
        this.bonhan_matdau = bonhan_matdau;
    }

    public int getBonhan_chuyenphacdo() {
        return bonhan_chuyenphacdo;
    }

    public void setBonhan_chuyenphacdo(int bonhan_chuyenphacdo) {
        this.bonhan_chuyenphacdo = bonhan_chuyenphacdo;
    }

}

package com.gms.entity.form.htc;

import java.util.HashMap;
import java.util.List;

/**
 * Form for view PDF, excel, and sending mail
 *
 * @author vvthành
 */
public class DashboardObjectGroupPercentForm {

    private String time;
    private int duongtinh;
    private int xetnghiem;
    private Long oID;
    private Long oParentID;
    private String oCode;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuongtinh() {
        return duongtinh;
    }

    public void setDuongtinh(int duongtinh) {
        this.duongtinh = duongtinh;
    }

    public int getXetnghiem() {
        return xetnghiem;
    }

    public void setXetnghiem(int xetnghiem) {
        this.xetnghiem = xetnghiem;
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

    public String getoCode() {
        return oCode;
    }

    public void setoCode(String oCode) {
        this.oCode = oCode;
    }

}

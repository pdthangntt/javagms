package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * TableDetectHivInsuranceForm
 *
 * @author TrangBN
 */
public class TableDetectHivInsuranceForm implements Serializable, Cloneable {

    private int stt;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String displayName;
    private int yes;
    private int no;
    private int other;
    private List<TableDetectHivInsuranceForm> childs;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public List<TableDetectHivInsuranceForm> getChilds() {
        return childs;
    }

    public void setChilds(List<TableDetectHivInsuranceForm> childs) {
        this.childs = childs;
    }

    public String getOtherPercent() {
        return TextUtils.toPercent(this.other, this.getSum());
    }

    public String getYesPercent() {
        return TextUtils.toPercent(this.yes, this.getSum());
    }

    public String getNoPercent() {
        return TextUtils.toPercent(this.no, this.getSum());
    }

    public String getSumPercent(int total) {
        return TextUtils.toPercent(this.getSum(), total);
    }

    public int getSum() {
        return yes + no + other;
    }

    @Override
    public TableDetectHivInsuranceForm clone() throws CloneNotSupportedException {
        return (TableDetectHivInsuranceForm) super.clone();
    }

}

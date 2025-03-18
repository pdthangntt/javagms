package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * Form QL
 *
 */
public class PacQLTableForm implements Serializable, Cloneable {

    private String provinceName;
    private String provinceID;
    private String districtName;
    private String districtID;
    private String displayName;

    private int color1;
    private int color2;
    private int color3;

    private int stt;
    private int month1;
    private int month2;
    private int month3;

    private int early1;
    private int early2;
    private int early3;

    private double agv1;
    private double agv2;
    private double agv3;

    private int valueD;
    private int colorD;
    private int valueE;

    private List<PacQLTableForm> childs;

    public int getEarly1() {
        return early1;
    }

    public void setEarly1(int early1) {
        this.early1 = early1;
    }

    public int getEarly2() {
        return early2;
    }

    public void setEarly2(int early2) {
        this.early2 = early2;
    }

    public int getEarly3() {
        return early3;
    }

    public void setEarly3(int early3) {
        this.early3 = early3;
    }

    public int getValueE() {
        return valueE;
    }

    public void setValueE(int valueE) {
        this.valueE = valueE;
    }

    public int getColorD() {
        return colorD;
    }

    public void setColorD(int colorD) {
        this.colorD = colorD;
    }

    public double getAgv1() {
        return agv1;
    }

    public void setAgv1(double agv1) {
        this.agv1 = agv1;
    }

    public double getAgv2() {
        return agv2;
    }

    public void setAgv2(double agv2) {
        this.agv2 = agv2;
    }

    public double getAgv3() {
        return agv3;
    }

    public void setAgv3(double agv3) {
        this.agv3 = agv3;
    }

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor3() {
        return color3;
    }

    public void setColor3(int color3) {
        this.color3 = color3;
    }

    public int getValueD() {
        return valueD;
    }

    public void setValueD(int valueD) {
        this.valueD = valueD;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getMonth1() {
        return month1;
    }

    public void setMonth1(int month1) {
        this.month1 = month1;
    }

    public int getMonth2() {
        return month2;
    }

    public void setMonth2(int month2) {
        this.month2 = month2;
    }

    public int getMonth3() {
        return month3;
    }

    public void setMonth3(int month3) {
        this.month3 = month3;
    }

    public List<PacQLTableForm> getChilds() {
        return childs;
    }

    public void setChilds(List<PacQLTableForm> childs) {
        this.childs = childs;
    }

    @Override
    public PacQLTableForm clone() throws CloneNotSupportedException {
        return (PacQLTableForm) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}

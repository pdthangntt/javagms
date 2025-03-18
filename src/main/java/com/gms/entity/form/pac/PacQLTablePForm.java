package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * Form QL
 *
 * @author TrangBN
 */
public class PacQLTablePForm implements Serializable, Cloneable {

    private String provinceName1;
    private String provinceName2;
    private String provinceName3;
    private String provinceID1;
    private String provinceID2;
    private String provinceID3;

    private int month1;
    private int month2;
    private int month3;
    private int month4;
    private int month5;
    private int month6;
    private int month7;
    private int month8;
    private int month9;

    private int color1;
    private int color2;
    private int color3;
    private int color4;
    private int color5;
    private int color6;
    private int color7;
    private int color8;
    private int color9;

    private List<PacQLTablePForm> childs;

    public String getProvinceID1() {
        return provinceID1;
    }

    public void setProvinceID1(String provinceID1) {
        this.provinceID1 = provinceID1;
    }

    public String getProvinceID2() {
        return provinceID2;
    }

    public void setProvinceID2(String provinceID2) {
        this.provinceID2 = provinceID2;
    }

    public String getProvinceID3() {
        return provinceID3;
    }

    public void setProvinceID3(String provinceID3) {
        this.provinceID3 = provinceID3;
    }

    public String getProvinceName1() {
        return provinceName1;
    }

    public void setProvinceName1(String provinceName1) {
        this.provinceName1 = provinceName1;
    }

    public String getProvinceName2() {
        return provinceName2;
    }

    public void setProvinceName2(String provinceName2) {
        this.provinceName2 = provinceName2;
    }

    public String getProvinceName3() {
        return provinceName3;
    }

    public void setProvinceName3(String provinceName3) {
        this.provinceName3 = provinceName3;
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

    public int getMonth4() {
        return month4;
    }

    public void setMonth4(int month4) {
        this.month4 = month4;
    }

    public int getMonth5() {
        return month5;
    }

    public void setMonth5(int month5) {
        this.month5 = month5;
    }

    public int getMonth6() {
        return month6;
    }

    public void setMonth6(int month6) {
        this.month6 = month6;
    }

    public int getMonth7() {
        return month7;
    }

    public void setMonth7(int month7) {
        this.month7 = month7;
    }

    public int getMonth8() {
        return month8;
    }

    public void setMonth8(int month8) {
        this.month8 = month8;
    }

    public int getMonth9() {
        return month9;
    }

    public void setMonth9(int month9) {
        this.month9 = month9;
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

    public int getColor4() {
        return color4;
    }

    public void setColor4(int color4) {
        this.color4 = color4;
    }

    public int getColor5() {
        return color5;
    }

    public void setColor5(int color5) {
        this.color5 = color5;
    }

    public int getColor6() {
        return color6;
    }

    public void setColor6(int color6) {
        this.color6 = color6;
    }

    public int getColor7() {
        return color7;
    }

    public void setColor7(int color7) {
        this.color7 = color7;
    }

    public int getColor8() {
        return color8;
    }

    public void setColor8(int color8) {
        this.color8 = color8;
    }

    public int getColor9() {
        return color9;
    }

    public void setColor9(int color9) {
        this.color9 = color9;
    }

    public List<PacQLTablePForm> getChilds() {
        return childs;
    }

    public void setChilds(List<PacQLTablePForm> childs) {
        this.childs = childs;
    }

    @Override
    public PacQLTablePForm clone() throws CloneNotSupportedException {
        return (PacQLTablePForm) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}

package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class PqmShiMmdImportForm extends BaseEntity implements Serializable {

    private String month; //Tháng
    private String year; //Năm
    
    private String provinceID; //Tỉnh
    private String siteID; //Cơ sở
    
    private String siteCode; //Cơ sở
    private String siteName;
    private String zipCode;

    private String totalTurnTK; //Lượt trong kỳ
    private String totalTurnLK; //Lượt lũy kế

    private String totalPatientTK; //BN trong kỳ
    private String totalPatientLK; //BN lũy kế

    private String totalPatientLess1MonthTK; //BN trong kỳ
    private String totalPatientLess1MonthLK; //BN lũy kế
    private String totalTurnLess1MonthTK; //Lượt trong kỳ
    private String totalTurnLess1MonthLK;//Lượt Lũy kế

    private String totalTurn1To2MonthTK;//Lượt trong kỳ
    private String totalTurn1To2MonthLK;//Lượt Lũy kế
    private String totalPatient1To2MonthTK;//BN trong kỳ
    private String totalPatient1To2MonthLK; //BN lũy kế

    private String totalTurn2To3MonthTK;//Lượt trong kỳ
    private String totalTurn2To3MonthLK;//Lượt Lũy kế
    private String totalPatient2To3MonthTK;//BN trong kỳ
    private String totalPatient2To3MonthLK; //BN lũy kế

    private String totalTurn3MonthTK;//Lượt trong kỳ
    private String totalTurn3MonthLK;//Lượt Lũy kế
    private String totalPatient3MonthTK;//BN trong kỳ
    private String totalPatient3MonthLK;//BN lũy kế

    private String ratioPatient3MonthTK;//BN trong kỳ
    private String ratioPatient3MonthLK;//BN lũy kế

    private int row;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTotalTurnTK() {
        return totalTurnTK;
    }

    public void setTotalTurnTK(String totalTurnTK) {
        this.totalTurnTK = totalTurnTK;
    }

    public String getTotalTurnLK() {
        return totalTurnLK;
    }

    public void setTotalTurnLK(String totalTurnLK) {
        this.totalTurnLK = totalTurnLK;
    }

    public String getTotalPatientTK() {
        return totalPatientTK;
    }

    public void setTotalPatientTK(String totalPatientTK) {
        this.totalPatientTK = totalPatientTK;
    }

    public String getTotalPatientLK() {
        return totalPatientLK;
    }

    public void setTotalPatientLK(String totalPatientLK) {
        this.totalPatientLK = totalPatientLK;
    }

    public String getTotalPatientLess1MonthTK() {
        return totalPatientLess1MonthTK;
    }

    public void setTotalPatientLess1MonthTK(String totalPatientLess1MonthTK) {
        this.totalPatientLess1MonthTK = totalPatientLess1MonthTK;
    }

    public String getTotalPatientLess1MonthLK() {
        return totalPatientLess1MonthLK;
    }

    public void setTotalPatientLess1MonthLK(String totalPatientLess1MonthLK) {
        this.totalPatientLess1MonthLK = totalPatientLess1MonthLK;
    }

    public String getTotalTurnLess1MonthTK() {
        return totalTurnLess1MonthTK;
    }

    public void setTotalTurnLess1MonthTK(String totalTurnLess1MonthTK) {
        this.totalTurnLess1MonthTK = totalTurnLess1MonthTK;
    }

    public String getTotalTurnLess1MonthLK() {
        return totalTurnLess1MonthLK;
    }

    public void setTotalTurnLess1MonthLK(String totalTurnLess1MonthLK) {
        this.totalTurnLess1MonthLK = totalTurnLess1MonthLK;
    }

    public String getTotalTurn1To2MonthTK() {
        return totalTurn1To2MonthTK;
    }

    public void setTotalTurn1To2MonthTK(String totalTurn1To2MonthTK) {
        this.totalTurn1To2MonthTK = totalTurn1To2MonthTK;
    }

    public String getTotalTurn1To2MonthLK() {
        return totalTurn1To2MonthLK;
    }

    public void setTotalTurn1To2MonthLK(String totalTurn1To2MonthLK) {
        this.totalTurn1To2MonthLK = totalTurn1To2MonthLK;
    }

    public String getTotalPatient1To2MonthTK() {
        return totalPatient1To2MonthTK;
    }

    public void setTotalPatient1To2MonthTK(String totalPatient1To2MonthTK) {
        this.totalPatient1To2MonthTK = totalPatient1To2MonthTK;
    }

    public String getTotalPatient1To2MonthLK() {
        return totalPatient1To2MonthLK;
    }

    public void setTotalPatient1To2MonthLK(String totalPatient1To2MonthLK) {
        this.totalPatient1To2MonthLK = totalPatient1To2MonthLK;
    }

    public String getTotalTurn2To3MonthTK() {
        return totalTurn2To3MonthTK;
    }

    public void setTotalTurn2To3MonthTK(String totalTurn2To3MonthTK) {
        this.totalTurn2To3MonthTK = totalTurn2To3MonthTK;
    }

    public String getTotalTurn2To3MonthLK() {
        return totalTurn2To3MonthLK;
    }

    public void setTotalTurn2To3MonthLK(String totalTurn2To3MonthLK) {
        this.totalTurn2To3MonthLK = totalTurn2To3MonthLK;
    }

    public String getTotalPatient2To3MonthTK() {
        return totalPatient2To3MonthTK;
    }

    public void setTotalPatient2To3MonthTK(String totalPatient2To3MonthTK) {
        this.totalPatient2To3MonthTK = totalPatient2To3MonthTK;
    }

    public String getTotalPatient2To3MonthLK() {
        return totalPatient2To3MonthLK;
    }

    public void setTotalPatient2To3MonthLK(String totalPatient2To3MonthLK) {
        this.totalPatient2To3MonthLK = totalPatient2To3MonthLK;
    }

    public String getTotalTurn3MonthTK() {
        return totalTurn3MonthTK;
    }

    public void setTotalTurn3MonthTK(String totalTurn3MonthTK) {
        this.totalTurn3MonthTK = totalTurn3MonthTK;
    }

    public String getTotalTurn3MonthLK() {
        return totalTurn3MonthLK;
    }

    public void setTotalTurn3MonthLK(String totalTurn3MonthLK) {
        this.totalTurn3MonthLK = totalTurn3MonthLK;
    }

    public String getTotalPatient3MonthTK() {
        return totalPatient3MonthTK;
    }

    public void setTotalPatient3MonthTK(String totalPatient3MonthTK) {
        this.totalPatient3MonthTK = totalPatient3MonthTK;
    }

    public String getTotalPatient3MonthLK() {
        return totalPatient3MonthLK;
    }

    public void setTotalPatient3MonthLK(String totalPatient3MonthLK) {
        this.totalPatient3MonthLK = totalPatient3MonthLK;
    }

    public String getRatioPatient3MonthTK() {
        return ratioPatient3MonthTK;
    }

    public void setRatioPatient3MonthTK(String ratioPatient3MonthTK) {
        this.ratioPatient3MonthTK = ratioPatient3MonthTK;
    }

    public String getRatioPatient3MonthLK() {
        return ratioPatient3MonthLK;
    }

    public void setRatioPatient3MonthLK(String ratioPatient3MonthLK) {
        this.ratioPatient3MonthLK = ratioPatient3MonthLK;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

}

package com.gms.entity.form.opc_arv;

import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class PqmArvViralBookImportForm extends BaseEntity {

    private String arvCode;
    private String testTime;
    private String resultNumberW;
    private String resultNumberX;

    private String resultQ;
    private String resultR;
    private String resultS;
    private String resultT;

    private String resultTime;
    private String row;
    private Long siteID;

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }
    
    

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getArvCode() {
        return arvCode;
    }

    public void setArvCode(String arvCode) {
        this.arvCode = arvCode;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getResultNumberW() {
        return resultNumberW;
    }

    public void setResultNumberW(String resultNumberW) {
        this.resultNumberW = resultNumberW;
    }

    public String getResultNumberX() {
        return resultNumberX;
    }

    public void setResultNumberX(String resultNumberX) {
        this.resultNumberX = resultNumberX;
    }

    public String getResultQ() {
        return resultQ;
    }

    public void setResultQ(String resultQ) {
        this.resultQ = resultQ;
    }

    public String getResultR() {
        return resultR;
    }

    public void setResultR(String resultR) {
        this.resultR = resultR;
    }

    public String getResultS() {
        return resultS;
    }

    public void setResultS(String resultS) {
        this.resultS = resultS;
    }

    public String getResultT() {
        return resultT;
    }

    public void setResultT(String resultT) {
        this.resultT = resultT;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

}

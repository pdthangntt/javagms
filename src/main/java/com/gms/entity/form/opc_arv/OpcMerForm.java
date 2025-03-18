package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form thông tin BC MER
 * 
 * @author TrangBN
 */
public class OpcMerForm extends BaseForm implements Serializable {
    private String ID;
    private List<Long> siteID;
    private String treatmentSiteName;
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private boolean opcManager;
    private boolean opc;
    private String startDate;
    private String endDate;
    private String year;
    private String quarter;
    private String quarterDisplay;
    
    private Map<String, HashMap<String,OpcMerFormAges>> table01Quarterly; // Bảng 01 BC quý
    private Mer02QuarterlyForm table0102Quarterly;
    private Mer02QuarterlyForm table02Quarterly; 
    private Map<String, HashMap<String,OpcMerFormAges>> table03Quarterly;
    private Mer04QuarterlyForm table04Quarterly;

    public String getTreatmentSiteName() {
        return treatmentSiteName;
    }

    public void setTreatmentSiteName(String treatmentSiteName) {
        this.treatmentSiteName = treatmentSiteName;
    }

    public List<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(List<Long> siteID) {
        this.siteID = siteID;
    }

    public String getQuarterDisplay() {
        return quarterDisplay;
    }

    public void setQuarterDisplay(String quarterDisplay) {
        this.quarterDisplay = quarterDisplay;
    }
    
    public Mer02QuarterlyForm getTable0102Quarterly() {
        return table0102Quarterly;
    }

    public void setTable0102Quarterly(Mer02QuarterlyForm table0102Quarterly) {
        this.table0102Quarterly = table0102Quarterly;
    }

    public Mer02QuarterlyForm getTable02Quarterly() {
        return table02Quarterly;
    }

    public void setTable02Quarterly(Mer02QuarterlyForm table02Quarterly) {
        this.table02Quarterly = table02Quarterly;
    }
    
    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public Mer04QuarterlyForm getTable04Quarterly() {
        return table04Quarterly;
    }

    public void setTable04Quarterly(Mer04QuarterlyForm table04Quarterly) {
        this.table04Quarterly = table04Quarterly;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Map<String, HashMap<String, OpcMerFormAges>> getTable01Quarterly() {
        return table01Quarterly;
    }

    public void setTable01Quarterly(Map<String, HashMap<String, OpcMerFormAges>> table01Quarterly) {
        this.table01Quarterly = table01Quarterly;
    }
    
    public Map<String, HashMap<String, OpcMerFormAges>> getTable03Quarterly() {
        return table03Quarterly;
    }

    public void setTable03Quarterly(Map<String, HashMap<String, OpcMerFormAges>> table03Quarterly) {
        this.table03Quarterly = table03Quarterly;
    }
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }
    
    public boolean isOpcManager() {
        return opcManager;
    }

    public void setOpcManager(boolean opcManager) {
        this.opcManager = opcManager;
    }

    public boolean isOpc() {
        return opc;
    }

    public void setOpc(boolean opc) {
        this.opc = opc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPercentValue(int tuso, int mauso){
        return TextUtils.toPercent(tuso, mauso);
    }
}

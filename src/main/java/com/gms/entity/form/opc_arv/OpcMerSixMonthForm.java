package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Form thông tin BC MER
 *
 * @author pdThang
 */
public class OpcMerSixMonthForm extends BaseForm implements Serializable {

    private String ID;
    private String siteID;
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private boolean opcManager;
    private boolean opc;
    private String startDateSixMonthBefore;
    private String endDateSixMonthBefore;
    private String startDate;
    private String endDate;
    private String month;
    private String year;
    private String searchSiteID;

    private Map<String, HashMap<String, OpcMerSixMonthTable>> table;
    private HashMap<String, OpcMerSixMonthTableSum> table02;
    private HashMap<String, OpcMerSixMonthTablePercent> table03;

    public HashMap<String, OpcMerSixMonthTablePercent> getTable03() {
        return table03;
    }

    public String getSearchSiteID() {
        return searchSiteID;
    }

    public void setSearchSiteID(String searchSiteID) {
        this.searchSiteID = searchSiteID;
    }

    public void setTable03(HashMap<String, OpcMerSixMonthTablePercent> table03) {
        this.table03 = table03;
    }

    public HashMap<String, OpcMerSixMonthTableSum> getTable02() {
        return table02;
    }

    public void setTable02(HashMap<String, OpcMerSixMonthTableSum> table02) {
        this.table02 = table02;
    }

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

    public String getStartDateSixMonthBefore() {
        return startDateSixMonthBefore;
    }

    public void setStartDateSixMonthBefore(String startDateSixMonthBefore) {
        this.startDateSixMonthBefore = startDateSixMonthBefore;
    }

    public String getEndDateSixMonthBefore() {
        return endDateSixMonthBefore;
    }

    public void setEndDateSixMonthBefore(String endDateSixMonthBefore) {
        this.endDateSixMonthBefore = endDateSixMonthBefore;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
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

    public Map<String, HashMap<String, OpcMerSixMonthTable>> getTable() {
        return table;
    }

    public void setTable(Map<String, HashMap<String, OpcMerSixMonthTable>> table) {
        this.table = table;
    }

}

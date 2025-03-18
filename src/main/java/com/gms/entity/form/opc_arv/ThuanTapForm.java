package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ThuanTapForm extends BaseForm{
    
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String site;
    private boolean opc;
    private boolean opcManager;
    private int month;
    private int year;
    private String startDate;
    private String endDate;
    private List<String> sites;
    private String siteLabel;
    private HashMap<String, HashMap<String, OpcThuantapAgesForm>> dataResults;

    public HashMap<String, HashMap<String, OpcThuantapAgesForm>> getDataResults() {
        return dataResults;
    }

    public void setDataResults(HashMap<String, HashMap<String, OpcThuantapAgesForm>> dataResults) {
        this.dataResults = dataResults;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public boolean isOpc() {
        return opc;
    }

    public void setOpc(boolean opc) {
        this.opc = opc;
    }

    public boolean isOpcManager() {
        return opcManager;
    }

    public void setOpcManager(boolean opcManager) {
        this.opcManager = opcManager;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
    }

    public String getSiteLabel() {
        return siteLabel;
    }

    public void setSiteLabel(String siteLabel) {
        this.siteLabel = siteLabel;
    }

}

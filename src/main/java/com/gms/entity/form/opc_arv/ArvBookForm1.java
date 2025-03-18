package com.gms.entity.form.opc_arv;

import com.gms.entity.bean.DataPage;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Form Thông tin sổ kháng thể arv
 *
 * @author TrangBN
 */
public class ArvBookForm1 extends BaseForm implements Serializable {

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
    private String from;
    private String to;
    private String month;
    private String year;
    private String searchSiteID;
    private HashMap<String, HashMap<String, String>> options;
    private DataPage<SoKhangTheArvForm> dataPage;
    private List<Integer> order;
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Integer> getOrder() {
        return order;
    }

    public void setOrder(List<Integer> order) {
        this.order = order;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public DataPage<SoKhangTheArvForm> getDataPage() {
        return dataPage;
    }

    public void setDataPage(DataPage<SoKhangTheArvForm> dataPage) {
        this.dataPage = dataPage;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public String getSearchSiteID() {
        return searchSiteID;
    }

    public void setSearchSiteID(String searchSiteID) {
        this.searchSiteID = searchSiteID;
    }

}

package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pdThang
 */
public class ViralloadManagerForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String tab;
    private String month;
    private String year;
    private boolean opc;
    private boolean opcManager;
    private Map<Long, ViralloadManagerTable> table;
    private ViralloadManagerTable02 table02;
    private HashMap<String, HashMap<String, String>> options;

    public ViralloadManagerTable getSumTable() {
        ViralloadManagerTable tableSum = new ViralloadManagerTable();
        tableSum.setXnbac1(0);
        tableSum.setXnbac2(0);
        tableSum.setXn6thang(0);
        tableSum.setXn12thang(0);
        tableSum.setXnkhac(0);
        tableSum.setTbdt(0);
        tableSum.setPnmt(0);
        tableSum.setXnLan1phatHien(0);
        tableSum.setXnLan1Duoi200(0);
        tableSum.setXnLan1Tu200Den1000(0);
        tableSum.setXnLan1Tren1000(0);
        tableSum.setTuvan(0);
        tableSum.setXnLan2duoi1000(0);
        tableSum.setXnLan2tren1000(0);
        tableSum.setThatbaiphacdo(0);

        for (Map.Entry<Long, ViralloadManagerTable> entry : table.entrySet()) {
            ViralloadManagerTable item = entry.getValue();
            tableSum.setXnbac1(tableSum.getXnbac1() + item.getXnbac1());
            tableSum.setXnbac2(tableSum.getXnbac2() + item.getXnbac2());
            tableSum.setXn6thang(tableSum.getXn6thang() + item.getXn6thang());
            tableSum.setXn12thang(tableSum.getXn12thang() + item.getXn12thang());
            tableSum.setXnkhac(tableSum.getXnkhac() + item.getXnkhac());
            tableSum.setTbdt(tableSum.getTbdt() + item.getTbdt());
            tableSum.setPnmt(tableSum.getPnmt() + item.getPnmt());
            tableSum.setXnLan1phatHien(tableSum.getXnLan1phatHien() + item.getXnLan1phatHien());
            tableSum.setXnLan1Duoi200(tableSum.getXnLan1Duoi200() + item.getXnLan1Duoi200());
            tableSum.setXnLan1Tu200Den1000(tableSum.getXnLan1Tu200Den1000() + item.getXnLan1Tu200Den1000());
            tableSum.setXnLan1Tren1000(tableSum.getXnLan1Tren1000() + item.getXnLan1Tren1000());
            tableSum.setTuvan(tableSum.getTuvan() + item.getTuvan());
            tableSum.setXnLan2duoi1000(tableSum.getXnLan2duoi1000() + item.getXnLan2duoi1000());
            tableSum.setXnLan2tren1000(tableSum.getXnLan2tren1000() + item.getXnLan2tren1000());
            tableSum.setThatbaiphacdo(tableSum.getThatbaiphacdo() + item.getThatbaiphacdo());
        }
        return tableSum;
    }

    public ViralloadManagerTable02 getTable02() {
        return table02;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public void setTable02(ViralloadManagerTable02 table02) {
        this.table02 = table02;
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

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
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

    public Map<Long, ViralloadManagerTable> getTable() {
        return table;
    }

    public void setTable(Map<Long, ViralloadManagerTable> table) {
        this.table = table;
    }

}

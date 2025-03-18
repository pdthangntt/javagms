package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form BC phác đồ
 *
 * @author vvThành
 *
 */
public class RegimenForm extends BaseForm {

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
    private HashMap<String, HashMap<String, RegimenTable>> children;
    private HashMap<String, HashMap<String, RegimenTable>> adults;

    /**
     * Phác đồ trẻ em
     *
     * @return
     */
    public HashMap<String, RegimenTable> getSumChildren() {
        HashMap<String, RegimenTable> map = new HashMap<>();
        RegimenTable table1 = new RegimenTable();
        RegimenTable table2 = new RegimenTable();

        for (Map.Entry<String, HashMap<String, RegimenTable>> entry : children.entrySet()) {
            HashMap<String, RegimenTable> value = entry.getValue();

            RegimenTable value1 = value.get("1");
            RegimenTable value2 = value.get("2");

            table1.setBatdaunhan(table1.getBatdaunhan() + value1.getBatdaunhan());
            table1.setBonhan_botri(table1.getBonhan_botri() + value1.getBonhan_botri());
            table1.setBonhan_chuyendi(table1.getBonhan_chuyendi() + value1.getBonhan_chuyendi());
            table1.setBonhan_chuyenphacdo(table1.getBonhan_chuyenphacdo() + value1.getBonhan_chuyenphacdo());
            table1.setBonhan_matdau(table1.getBonhan_matdau() + value1.getBonhan_matdau());
            table1.setBonhan_tuvong(table1.getBonhan_tuvong() + value1.getBonhan_tuvong());
            table1.setDangnhan(table1.getDangnhan() + value1.getDangnhan());
            table1.setNhanThuoc(table1.getNhanThuoc() + value1.getNhanThuoc());

            table2.setBatdaunhan(table2.getBatdaunhan() + value2.getBatdaunhan());
            table2.setBonhan_botri(table2.getBonhan_botri() + value2.getBonhan_botri());
            table2.setBonhan_chuyendi(table2.getBonhan_chuyendi() + value2.getBonhan_chuyendi());
            table2.setBonhan_chuyenphacdo(table2.getBonhan_chuyenphacdo() + value2.getBonhan_chuyenphacdo());
            table2.setBonhan_matdau(table2.getBonhan_matdau() + value2.getBonhan_matdau());
            table2.setBonhan_tuvong(table2.getBonhan_tuvong() + value2.getBonhan_tuvong());
            table2.setDangnhan(table2.getDangnhan() + value2.getDangnhan());
            table2.setNhanThuoc(table2.getNhanThuoc() + value2.getNhanThuoc());

            map.put("1", table1);
            map.put("2", table2);
        }
        return map;
    }

    public HashMap<String, RegimenTable> getSumAdults() {
        HashMap<String, RegimenTable> map = new HashMap<>();
        RegimenTable table1 = new RegimenTable();
        RegimenTable table2 = new RegimenTable();

        for (Map.Entry<String, HashMap<String, RegimenTable>> entry : adults.entrySet()) {
            HashMap<String, RegimenTable> value = entry.getValue();

            RegimenTable value1 = value.get("1");
            RegimenTable value2 = value.get("2");

            table1.setBatdaunhan(table1.getBatdaunhan() + value1.getBatdaunhan());
            table1.setBonhan_botri(table1.getBonhan_botri() + value1.getBonhan_botri());
            table1.setBonhan_chuyendi(table1.getBonhan_chuyendi() + value1.getBonhan_chuyendi());
            table1.setBonhan_chuyenphacdo(table1.getBonhan_chuyenphacdo() + value1.getBonhan_chuyenphacdo());
            table1.setBonhan_matdau(table1.getBonhan_matdau() + value1.getBonhan_matdau());
            table1.setBonhan_tuvong(table1.getBonhan_tuvong() + value1.getBonhan_tuvong());
            table1.setDangnhan(table1.getDangnhan() + value1.getDangnhan());
            table1.setNhanThuoc(table1.getNhanThuoc() + value1.getNhanThuoc());

            table2.setBatdaunhan(table2.getBatdaunhan() + value2.getBatdaunhan());
            table2.setBonhan_botri(table2.getBonhan_botri() + value2.getBonhan_botri());
            table2.setBonhan_chuyendi(table2.getBonhan_chuyendi() + value2.getBonhan_chuyendi());
            table2.setBonhan_chuyenphacdo(table2.getBonhan_chuyenphacdo() + value2.getBonhan_chuyenphacdo());
            table2.setBonhan_matdau(table2.getBonhan_matdau() + value2.getBonhan_matdau());
            table2.setBonhan_tuvong(table2.getBonhan_tuvong() + value2.getBonhan_tuvong());
            table2.setDangnhan(table2.getDangnhan() + value2.getDangnhan());
            table2.setNhanThuoc(table2.getNhanThuoc() + value2.getNhanThuoc());

            map.put("1", table1);
            map.put("2", table2);
        }
        return map;
    }

    public HashMap<String, HashMap<String, RegimenTable>> getChildren() {
        return children;
    }

    public void setChildren(HashMap<String, HashMap<String, RegimenTable>> children) {
        this.children = children;
    }

    public HashMap<String, HashMap<String, RegimenTable>> getAdults() {
        return adults;
    }

    public void setAdults(HashMap<String, HashMap<String, RegimenTable>> adults) {
        this.adults = adults;
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

}

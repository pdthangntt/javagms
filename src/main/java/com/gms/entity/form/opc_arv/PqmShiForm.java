package com.gms.entity.form.opc_arv;

import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form BC pqm
 *
 * @author pdThang
 *
 */
public class PqmShiForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private int month;
    private int year;

    private int totalArv;
    private int totalDrug;
    private String donvi;
    private List<PqmShiTable> items;
    private List<PqmShiTable> rowData;
    private List<PqmShiDataEntity> apiDatas;
    private HashMap<String, String> keyMonth;

    private HashMap<String, String> provincePQM;
    private HashMap<String, String> districtPQM;
    private HashMap<String, String> provinceNamePQM;
    private HashMap<String, String> districtNamePQM;
    private String provincePQMcode;
    private String districtPQMcode;
    private Map<Long, String> sitePQMcodes;
    private String currentProvinceID;

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public Map<Long, String> getSitePQMcodes() {
        return sitePQMcodes;
    }

    public void setSitePQMcodes(Map<Long, String> sitePQMcodes) {
        this.sitePQMcodes = sitePQMcodes;
    }

    public String getDistrictPQMcode() {
        return districtPQMcode;
    }

    public void setDistrictPQMcode(String districtPQMcode) {
        this.districtPQMcode = districtPQMcode;
    }

    public List<PqmShiDataEntity> getApiDatas() {
        return apiDatas;
    }

    public void setApiDatas(List<PqmShiDataEntity> apiDatas) {
        this.apiDatas = apiDatas;
    }

    public HashMap<String, String> getProvincePQM() {
        return provincePQM;
    }

    public void setProvincePQM(HashMap<String, String> provincePQM) {
        this.provincePQM = provincePQM;
    }

    public HashMap<String, String> getDistrictPQM() {
        return districtPQM;
    }

    public void setDistrictPQM(HashMap<String, String> districtPQM) {
        this.districtPQM = districtPQM;
    }

    public HashMap<String, String> getProvinceNamePQM() {
        return provinceNamePQM;
    }

    public void setProvinceNamePQM(HashMap<String, String> provinceNamePQM) {
        this.provinceNamePQM = provinceNamePQM;
    }

    public HashMap<String, String> getDistrictNamePQM() {
        return districtNamePQM;
    }

    public void setDistrictNamePQM(HashMap<String, String> districtNamePQM) {
        this.districtNamePQM = districtNamePQM;
    }

    public String getProvincePQMcode() {
        return provincePQMcode;
    }

    public void setProvincePQMcode(String provincePQMcode) {
        this.provincePQMcode = provincePQMcode;
    }

    public int getTotalArv() {
        return totalArv;
    }

    public void setTotalArv(int totalArv) {
        this.totalArv = totalArv;
    }

    public int getTotalDrug() {
        return totalDrug;
    }

    public void setTotalDrug(int totalDrug) {
        this.totalDrug = totalDrug;
    }

    public List<PqmShiTable> getRowData() {
        return rowData;
    }

    public void setRowData(List<PqmShiTable> rowData) {
        this.rowData = rowData;
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

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public List<PqmShiTable> getItems() {
        return items;
    }

    public void setItems(List<PqmShiTable> items) {
        this.items = items;
    }

    public HashMap<String, String> getKeyMonth() {
        return keyMonth;
    }

    public void setKeyMonth(HashMap<String, String> keyMonth) {
        this.keyMonth = keyMonth;
    }

}

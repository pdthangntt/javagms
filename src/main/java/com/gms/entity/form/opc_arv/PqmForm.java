package com.gms.entity.form.opc_arv;

import com.gms.entity.db.PqmDataEntity;
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
public class PqmForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String site;
    private boolean opc;
    private boolean htc;
    private boolean htcConfirm;
    private boolean pac;
    private boolean prep;
    private boolean edit;
    private boolean send;//đã tổng hợp hay chưa
    private boolean update;//Có đc sửa, thêm bc hay k
    private boolean opcManager;
    private String hub;
    private int month;
    private int year;
    private String startDate;
    private String donvi;
    private String endDate;
    private String currentProvinceID;
    private List<String> sites;
    private List<String> siteSends;
    private List<PqmReportTable> items;
    private List<PqmReportTable> items0;
    private List<PqmReportTable> items1;
    private List<PqmReportTable> items2;
    private List<PqmDataEntity> data;
    private Map<String, List<PqmDataEntity>> mapDataHub;
    private List<PqmDataEntity> dataSynthetic;
    private List<PqmDataEntity> dataSiteMonth;
    private String siteLabel;
    private HashMap<String, String> siteOptions;
    private HashMap<String, String> siteSearchOptions;
    private HashMap<String, String> keyMonth;
    private HashMap<String, String> siteSendOptions;
    private HashMap<String, String> siteNameOptions;
    private HashMap<String, String> provincePQM;
    private HashMap<String, String> districtPQM;
    private HashMap<String, String> provinceNamePQM;
    private HashMap<String, String> districtNamePQM;
    private String provincePQMcode;
    private String docMonth;

    private String currentProvince;

    public String getCurrentProvince() {
        return currentProvince;
    }

    public void setCurrentProvince(String currentProvince) {
        this.currentProvince = currentProvince;
    }

    public String getDocMonth() {
        return docMonth;
    }

    public void setDocMonth(String docMonth) {
        this.docMonth = docMonth;
    }

    public List<PqmDataEntity> getDataSiteMonth() {
        return dataSiteMonth;
    }

    public void setDataSiteMonth(List<PqmDataEntity> dataSiteMonth) {
        this.dataSiteMonth = dataSiteMonth;
    }

    public boolean isHtcConfirm() {
        return htcConfirm;
    }

    public void setHtcConfirm(boolean htcConfirm) {
        this.htcConfirm = htcConfirm;
    }

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getProvincePQMcode() {
        return provincePQMcode;
    }

    public void setProvincePQMcode(String provincePQMcode) {
        this.provincePQMcode = provincePQMcode;
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

    public Map<String, List<PqmDataEntity>> getMapDataHub() {
        return mapDataHub;
    }

    public void setMapDataHub(Map<String, List<PqmDataEntity>> mapDataHub) {
        this.mapDataHub = mapDataHub;
    }

    public HashMap<String, String> getSiteSearchOptions() {
        return siteSearchOptions;
    }

    public void setSiteSearchOptions(HashMap<String, String> siteSearchOptions) {
        this.siteSearchOptions = siteSearchOptions;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public HashMap<String, String> getKeyMonth() {
        return keyMonth;
    }

    public void setKeyMonth(HashMap<String, String> keyMonth) {
        this.keyMonth = keyMonth;
    }

    public List<PqmDataEntity> getDataSynthetic() {
        return dataSynthetic;
    }

    public void setDataSynthetic(List<PqmDataEntity> dataSynthetic) {
        this.dataSynthetic = dataSynthetic;
    }

    public HashMap<String, String> getSiteNameOptions() {
        return siteNameOptions;
    }

    public void setSiteNameOptions(HashMap<String, String> siteNameOptions) {
        this.siteNameOptions = siteNameOptions;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public List<PqmDataEntity> getData() {
        return data;
    }

    public void setData(List<PqmDataEntity> data) {
        this.data = data;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isHtc() {
        return htc;
    }

    public void setHtc(boolean htc) {
        this.htc = htc;
    }

    public boolean isPac() {
        return pac;
    }

    public void setPac(boolean pac) {
        this.pac = pac;
    }

    public boolean isPrep() {
        return prep;
    }

    public void setPrep(boolean prep) {
        this.prep = prep;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public HashMap<String, String> getSiteSendOptions() {
        return siteSendOptions;
    }

    public void setSiteSendOptions(HashMap<String, String> siteSendOptions) {
        this.siteSendOptions = siteSendOptions;
    }

    public List<String> getSiteSends() {
        return siteSends;
    }

    public void setSiteSends(List<String> siteSends) {
        this.siteSends = siteSends;
    }

    public List<PqmReportTable> getItems() {
        return items;
    }

    public void setItems(List<PqmReportTable> items) {
        this.items = items;
    }

    public List<PqmReportTable> getItems0() {
        return items0;
    }

    public void setItems0(List<PqmReportTable> items0) {
        this.items0 = items0;
    }

    public List<PqmReportTable> getItems1() {
        return items1;
    }

    public void setItems1(List<PqmReportTable> items1) {
        this.items1 = items1;
    }

    public List<PqmReportTable> getItems2() {
        return items2;
    }

    public void setItems2(List<PqmReportTable> items2) {
        this.items2 = items2;
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

    public HashMap<String, String> getSiteOptions() {
        return siteOptions;
    }

    public void setSiteOptions(HashMap<String, String> siteOptions) {
        this.siteOptions = siteOptions;
    }

}

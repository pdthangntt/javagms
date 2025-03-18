package com.gms.entity.form.report;

import com.gms.entity.form.BaseForm;
import com.gms.entity.form.tt03.Table02Form;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class TT03QuarterForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private List<String> services;
    private String quarter;
    private String year;
    private List<String> sites;
    private String district;
    private String serviceLabel;
    private String siteLabel;
    private boolean TTYT;
    private boolean PAC;
    private boolean VAAC;
    private List<Table02Form> table02;

    public String getServiceLabel() {
        return serviceLabel;
    }

    public void setServiceLabel(String serviceLabel) {
        this.serviceLabel = serviceLabel;
    }

    public String getSiteLabel() {
        return siteLabel;
    }

    public void setSiteLabel(String siteLabel) {
        this.siteLabel = siteLabel;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public boolean isTTYT() {
        return TTYT;
    }

    public void setTTYT(boolean TTYT) {
        this.TTYT = TTYT;
    }

    public boolean isPAC() {
        return PAC;
    }

    public void setPAC(boolean PAC) {
        this.PAC = PAC;
    }

    public boolean isVAAC() {
        return VAAC;
    }

    public void setVAAC(boolean VAAC) {
        this.VAAC = VAAC;
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

    public List<Table02Form> getTable02() {
        return table02;
    }

    public void setTable02(List<Table02Form> table02) {
        this.table02 = table02;
    }

}

package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;
import java.util.Map;

/**
 *
 * @author vvthanh
 */
public class InsuranceForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private boolean opc;
    private boolean opcManager;
    private Map<Long, InsuranceTable> table;

    public InsuranceTable getSumTable() {
        InsuranceTable total = new InsuranceTable();
        for (Map.Entry<Long, InsuranceTable> entry : table.entrySet()) {
            InsuranceTable item = entry.getValue();
            total.setInsurance100(total.getInsurance100() + item.getInsurance100());
            total.setInsurance95(total.getInsurance95() + item.getInsurance95());
            total.setInsurance80(total.getInsurance80() + item.getInsurance80());
            total.setInsuranceNone(total.getInsuranceNone() + item.getInsuranceNone());
            total.setInsuranceOther(total.getInsuranceOther() + item.getInsuranceOther());
            total.setExpire1(total.getExpire1() + item.getExpire1());
            total.setExpire2(total.getExpire2() + item.getExpire2());
            total.setExpire3(total.getExpire3() + item.getExpire3());
            total.setDungtuyen(total.getDungtuyen() + item.getDungtuyen());
            total.setTraituyen(total.getTraituyen() + item.getTraituyen());
            total.setInsurance(total.getInsurance() + item.getInsurance());
        }
        return total;
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

    public Map<Long, InsuranceTable> getTable() {
        return table;
    }

    public void setTable(Map<Long, InsuranceTable> table) {
        this.table = table;
    }

}

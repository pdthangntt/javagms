package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TrangBN
 */
public class OpcQuickTreatmentForm extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String tab;
    private boolean opc;
    private boolean opcManager;
    HashMap<String, String> options;
    List<QuickTreatmentTable> table;

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
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

    public List<QuickTreatmentTable> getTable() {
        return table;
    }

    public void setTable(List<QuickTreatmentTable> table) {
        this.table = table;
    }
    
    public QuickTreatmentTable getSum() {
        QuickTreatmentTable treatment = new QuickTreatmentTable();
        for (QuickTreatmentTable quick : table) {
            treatment.setArvTreatment( treatment.getArvTreatment() + quick.getArvTreatment());
            treatment.setArvQuickTreatment(treatment.getArvQuickTreatment()+ quick.getArvQuickTreatment());
            treatment.setArvTreatmentDuringDay(treatment.getArvTreatmentDuringDay() + quick.getArvTreatmentDuringDay());
            treatment.setReceiveMedicineMax90(treatment.getReceiveMedicineMax90() + quick.getReceiveMedicineMax90());
            treatment.setReceiveMedicineThreeMonth(treatment.getReceiveMedicineThreeMonth() + quick.getReceiveMedicineThreeMonth());
        }
        return treatment;
    }
}

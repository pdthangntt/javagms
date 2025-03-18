package com.gms.entity.form.opc_arv;

/**
 *
 * @author TrangBN
 */
public class QuickTreatmentTable {

    private Long  siteID;
    private int arvTreatment; // Đang điều trị ARV
    private int arvQuickTreatment; // Đang điều trị nhanh ARV trong kỳ BC
    private int arvTreatmentDuringDay; // Số điều trị ARV nhanh trong ngày trong kỳ báo cáo
    private int receiveMedicineMax90; // Số người bệnh đang được cấp thuốc tối đa 90 ngày trong kỳ báo cáo
    private int receiveMedicineThreeMonth; // Số người được cấp thuốc 3 tháng qua BHYT
    private String note; 

    public QuickTreatmentTable() {
    }
    
    public QuickTreatmentTable(Long siteID) {
        this.siteID = siteID;
        this.arvTreatment = 0;
        this.arvQuickTreatment = 0;
        this.arvTreatmentDuringDay = 0;
        this.receiveMedicineMax90 = 0;
        this.receiveMedicineThreeMonth = 0;
        this.note = "";
    }
    
    public int getArvQuickTreatment() {
        return arvQuickTreatment;
    }

    public void setArvQuickTreatment(int arvQuickTreatment) {
        this.arvQuickTreatment = arvQuickTreatment;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public int getArvTreatment() {
        return arvTreatment;
    }

    public void setArvTreatment(int arvTreatment) {
        this.arvTreatment = arvTreatment;
    }

    public int getArvTreatmentDuringDay() {
        return arvTreatmentDuringDay;
    }

    public void setArvTreatmentDuringDay(int arvTreatmentDuringDay) {
        this.arvTreatmentDuringDay = arvTreatmentDuringDay;
    }

    public int getReceiveMedicineMax90() {
        return receiveMedicineMax90;
    }

    public void setReceiveMedicineMax90(int receiveMedicineMax90) {
        this.receiveMedicineMax90 = receiveMedicineMax90;
    }

    public int getReceiveMedicineThreeMonth() {
        return receiveMedicineThreeMonth;
    }

    public void setReceiveMedicineThreeMonth(int receiveMedicineThreeMonth) {
        this.receiveMedicineThreeMonth = receiveMedicineThreeMonth;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

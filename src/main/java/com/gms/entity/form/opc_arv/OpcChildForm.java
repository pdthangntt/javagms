package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.db.OpcChildEntity;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public class OpcChildForm {
    private Long ID;
    private String dob;
    private String preventiveDate;
    private String preventiveEndDate;
    private String preventiveCotrimoxazole;
    private String cotrimoxazoleFromTime;
    private String cotrimoxazoleToTime;
    private String pcrOneResult;
    private String pcrBloodOneTime; //Ngày lấy mẫu PCR lần 1
    private String pcrTwoResult;
    private String pcrBloodTwoTime; //Ngày lấy mẫu PCR lần 1
    private String treatmentTime;
    private String pcrTwoTime; //Ngày xn pcr lần 02
    private String pcrOneTime; //Ngày xn pcr lần 01

    public String getPreventiveEndDate() {
        return preventiveEndDate;
    }

    public void setPreventiveEndDate(String preventiveEndDate) {
        this.preventiveEndDate = preventiveEndDate;
    }
    
    public String getPcrBloodOneTime() {
        return pcrBloodOneTime;
    }

    public void setPcrBloodOneTime(String pcrBloodOneTime) {
        this.pcrBloodOneTime = pcrBloodOneTime;
    }

    public String getPcrTwoResult() {
        return pcrTwoResult;
    }

    public void setPcrTwoResult(String pcrTwoResult) {
        this.pcrTwoResult = pcrTwoResult;
    }

    public String getPcrBloodTwoTime() {
        return pcrBloodTwoTime;
    }

    public void setPcrBloodTwoTime(String pcrBloodTwoTime) {
        this.pcrBloodTwoTime = pcrBloodTwoTime;
    }

    public String getPcrTwoTime() {
        return pcrTwoTime;
    }

    public void setPcrTwoTime(String pcrTwoTime) {
        this.pcrTwoTime = pcrTwoTime;
    }

    public String getPcrOneTime() {
        return pcrOneTime;
    }

    public void setPcrOneTime(String pcrOneTime) {
        this.pcrOneTime = pcrOneTime;
    }
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPreventiveDate() {
        return preventiveDate;
    }

    public void setPreventiveDate(String preventiveDate) {
        this.preventiveDate = preventiveDate;
    }

    public String getPreventiveCotrimoxazole() {
        return preventiveCotrimoxazole;
    }

    public void setPreventiveCotrimoxazole(String preventiveCotrimoxazole) {
        this.preventiveCotrimoxazole = preventiveCotrimoxazole;
    }

    public String getCotrimoxazoleFromTime() {
        return cotrimoxazoleFromTime;
    }

    public void setCotrimoxazoleFromTime(String cotrimoxazoleFromTime) {
        this.cotrimoxazoleFromTime = cotrimoxazoleFromTime;
    }

    public String getCotrimoxazoleToTime() {
        return cotrimoxazoleToTime;
    }

    public void setCotrimoxazoleToTime(String cotrimoxazoleToTime) {
        this.cotrimoxazoleToTime = cotrimoxazoleToTime;
    }

    public String getPcrOneResult() {
        return pcrOneResult;
    }

    public void setPcrOneResult(String pcrOneResult) {
        this.pcrOneResult = pcrOneResult;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }
    
    public OpcChildForm (){}
    public OpcChildForm (OpcChildEntity entity){
        ID = entity.getID();
        dob = TextUtils.formatDate(entity.getDob(), "dd/MM/yyyy");
        preventiveDate = TextUtils.formatDate(entity.getPreventiveDate(), "dd/MM/yyyy");
        preventiveEndDate = TextUtils.formatDate(entity.getPreventiveEndDate(), "dd/MM/yyyy");
        preventiveCotrimoxazole= entity.isPreventiveCotrimoxazole() ? "1" : "0";
        cotrimoxazoleFromTime = TextUtils.formatDate(entity.getCotrimoxazoleFromTime(), "dd/MM/yyyy");
        cotrimoxazoleToTime = TextUtils.formatDate(entity.getCotrimoxazoleToTime(), "dd/MM/yyyy");
        pcrOneResult = entity.getPcrOneResult();
        pcrTwoResult = entity.getPcrTwoResult();
        pcrBloodOneTime = TextUtils.formatDate(entity.getPcrBloodOneTime(), "dd/MM/yyyy");
        pcrBloodTwoTime = TextUtils.formatDate(entity.getPcrBloodTwoTime(), "dd/MM/yyyy");
        pcrTwoTime = TextUtils.formatDate(entity.getPcrTwoTime(), "dd/MM/yyyy");
        pcrOneTime = TextUtils.formatDate(entity.getPcrOneTime(), "dd/MM/yyyy");
        treatmentTime = TextUtils.formatDate(entity.getTreatmentTime(), "dd/MM/yyyy");
    }
    
    public OpcChildEntity setToEntity(OpcChildEntity model){
        model.setID(ID);
        model.setDob(TextUtils.convertDate(dob, "dd/MM/yyyy"));
        model.setPreventiveDate(TextUtils.convertDate(preventiveDate, "dd/MM/yyyy"));
        model.setPreventiveEndDate(TextUtils.convertDate(preventiveEndDate, "dd/MM/yyyy"));
        model.setPreventiveCotrimoxazole(StringUtils.isNotEmpty(preventiveCotrimoxazole) && "1".equals(preventiveCotrimoxazole));
        model.setCotrimoxazoleFromTime(TextUtils.convertDate(cotrimoxazoleFromTime, "dd/MM/yyyy"));
        model.setCotrimoxazoleToTime(TextUtils.convertDate(cotrimoxazoleToTime, "dd/MM/yyyy"));
        model.setPcrOneResult(pcrOneResult);
        model.setPcrTwoResult(pcrTwoResult);
        model.setTreatmentTime(TextUtils.convertDate(treatmentTime, "dd/MM/yyyy"));
        model.setPcrBloodOneTime(TextUtils.convertDate(pcrBloodOneTime, "dd/MM/yyyy"));
        model.setPcrBloodTwoTime(TextUtils.convertDate(pcrBloodTwoTime, "dd/MM/yyyy"));
        model.setPcrOneTime(TextUtils.convertDate(pcrOneTime, "dd/MM/yyyy"));
        model.setPcrTwoTime(TextUtils.convertDate(pcrTwoTime, "dd/MM/yyyy"));
        
        return model;
    }
}
